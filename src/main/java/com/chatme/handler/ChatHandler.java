package com.chatme.handler;

import com.chatme.dto.ChatMessage;
import com.chatme.dto.ChatRequest;
import com.fast.cqrs.cqrs.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Handler for AI chat with RAG via Supabase vector store.
 * <p>
 * Flow: embed question → search Supabase vectors → build prompt with context → stream AI response.
 * Uses Spring AI's {@link ChatModel} and {@link EmbeddingModel} for provider-agnostic AI calls.
 */
@Component
public class ChatHandler implements QueryHandler<ChatRequest, SseEmitter> {

    private static final Logger log = LoggerFactory.getLogger(ChatHandler.class);

    private final ChatModel chatModel;
    private final EmbeddingModel embeddingModel;
    private final JdbcTemplate jdbcTemplate;

    private static final String SYSTEM_PROMPT = """
        You are Hung Pham, a full-stack developer answering questions about yourself on your portfolio website.
        
        ## Personality & Voice
        - Always speak in first person ("I", "me", "my") as Hung Pham
        - Be friendly, confident, and professional with a touch of enthusiasm
        - Keep responses concise and focused on the question
        - Show personality but prioritize being helpful and informative

        ## Content Guidelines
        - ONLY answer questions about your personal information, skills, experience, projects, education, or contact info
        - Base your answers STRICTLY on the context provided below - it contains your resume information
        - ALWAYS provide SPECIFIC details from your resume when asked about your experience, skills, etc.
        - If asked about topics outside your portfolio/resume, politely redirect

        ## Formatting
        - Use Markdown: bullet points, numbered lists, **bold**, ## headers
        - Format links as [text](url)
        """;

    private static final String FALLBACK_CONTEXT = """
        I am Hung Pham, a full-stack developer.
        I have worked at ePhilos AG as a Full Stack Developer, as a freelance Web Developer, and as a NodeJS Developer intern at HM Communication.
        My skills include React, Next.js, JavaScript, TypeScript, HTML, CSS, Node.js, and more.
        My email is oublihi.a@gmail.com.
        """;

    public ChatHandler(
            ChatModel chatModel,
            EmbeddingModel embeddingModel,
            JdbcTemplate jdbcTemplate) {
        this.chatModel = chatModel;
        this.embeddingModel = embeddingModel;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public SseEmitter handle(ChatRequest request) {
        SseEmitter emitter = new SseEmitter(180_000L);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                streamChat(request.messages(), emitter);
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }

    private void streamChat(List<ChatMessage> messages, SseEmitter emitter) {
        try {
            String currentQuestion = messages.getLast().content();

            // Build conversation history
            StringBuilder chatHistory = new StringBuilder();
            for (int i = 0; i < messages.size() - 1; i++) {
                ChatMessage msg = messages.get(i);
                String prefix = "user".equals(msg.role()) ? "Human" : "Assistant";
                chatHistory.append(prefix).append(": ").append(msg.content()).append("\n");
            }

            // RAG: search vector store for relevant context
            String resumeContext = searchVectorStore(currentQuestion);
            if (resumeContext == null || resumeContext.isBlank()) {
                resumeContext = FALLBACK_CONTEXT;
            }

            String userPromptText = String.format("""
                ## Resume Information
                %s
                
                ## Conversation History
                %s
                
                ## Current Question
                %s
                
                Your response:
                """, resumeContext, chatHistory, currentQuestion);

            // Use Spring AI's ChatModel for streaming
            Prompt prompt = new Prompt(List.of(
                    new SystemMessage(SYSTEM_PROMPT),
                    new UserMessage(userPromptText)
            ));

            chatModel.stream(prompt)
                    .doOnNext(response -> {
                        try {
                            var generation = response.getResult();
                            if (generation != null && generation.getOutput() != null) {
                                String content = generation.getOutput().getText();
                                if (content != null) {
                                    emitter.send(content);
                                }
                            }
                        } catch (Exception e) {
                            log.error("Error sending SSE chunk", e);
                        }
                    })
                    .doOnComplete(emitter::complete)
                    .doOnError(emitter::completeWithError)
                    .subscribe();

        } catch (Exception e) {
            log.error("Error in chat streaming", e);
            try {
                emitter.send("An error occurred while processing your request.");
                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        }
    }

    private String searchVectorStore(String question) {
        try {
            // Use Spring AI's EmbeddingModel
            float[] embedding = embeddingModel.embed(question);
            String embeddingString = Arrays.toString(embedding); // [0.1, 0.2, ...] format works for PGVector

            // Search using PGVector cosine distance (<->)
            String sql = "SELECT content FROM documents ORDER BY embedding <-> ?::vector LIMIT 5";
            
            List<String> results = jdbcTemplate.query(sql, 
                (rs, rowNum) -> rs.getString("content"), 
                embeddingString
            );

            if (results.isEmpty()) return null;

            return String.join("\n\n", results);

        } catch (Exception e) {
            log.error("Error searching vector store", e);
            return null;
        }
    }
}
