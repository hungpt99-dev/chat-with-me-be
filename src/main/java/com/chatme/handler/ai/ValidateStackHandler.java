package com.chatme.handler.ai;

import com.fast.cqrs.cqrs.QueryHandler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Handler for validating tech stacks via AI analysis.
 * <p>
 * Uses Spring AI's {@link ChatModel} for provider-agnostic AI calls
 * with JSON output format for structured responses.
 */
@Component
public class ValidateStackHandler implements QueryHandler<ValidateStackHandler.Request, ValidateStackHandler.Response> {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        List<TechNode> nodes,
        List<TechEdge> edges
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record TechNode(String id, Map<String, Object> data) {}
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record TechEdge(String id, String source, String target) {}
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        boolean isValid,
        String message,
        int overall_score,
        Scores scores,
        Analysis analysis,
        CompatibilityMatrix compatibility_matrix,
        Recommendations recommendations
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Scores(
            int overall,
            int performance,
            int scalability,
            int maintainability,
            int security,
            int cost_efficiency
        ) {}

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Analysis(
            List<String> strengths,
            List<String> weaknesses,
            String performance_impact,
            String scalability_assessment,
            String security_considerations,
            String cost_efficiency,
            String learning_curve,
            String community_support
        ) {}

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record CompatibilityMatrix(
            List<String> compatible_pairs,
            List<String> incompatible_pairs,
            List<String> suggestions
        ) {}

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Recommendations(
            List<String> immediate_actions,
            List<String> future_considerations,
            List<String> alternative_technologies
        ) {}

        /**
         * Returns a default error result.
         */
        public static Response error(String message) {
            return new Response(
                false, message, 0,
                new Scores(0, 0, 0, 0, 0, 0),
                new Analysis(List.of(), List.of(), "", "", "", "", "", ""),
                new CompatibilityMatrix(List.of(), List.of(), List.of()),
                new Recommendations(List.of(), List.of(), List.of())
            );
        }
    }

    private static final Logger log = LoggerFactory.getLogger(ValidateStackHandler.class);

    private final ChatModel chatModel;
    private final ObjectMapper objectMapper;

    private static final String SYSTEM_PROMPT = """
        You are a technology stack validator. Analyze the provided technology stack and return a detailed analysis in JSON format with the following structure:
        {
          "isValid": boolean,
          "message": "A brief summary message",
          "overall_score": number (0-100),
          "scores": {
            "overall": number (0-100),
            "performance": number (0-100),
            "scalability": number (0-100),
            "maintainability": number (0-100),
            "security": number (0-100),
            "cost_efficiency": number (0-100)
          },
          "analysis": {
            "strengths": ["list of strengths"],
            "weaknesses": ["list of weaknesses"],
            "performance_impact": "detailed performance analysis",
            "scalability_assessment": "detailed scalability analysis",
            "security_considerations": "detailed security analysis",
            "cost_efficiency": "detailed cost analysis",
            "learning_curve": "learning curve assessment",
            "community_support": "community support assessment"
          },
          "compatibility_matrix": {
            "compatible_pairs": ["list of well-working technology combinations"],
            "incompatible_pairs": ["list of problematic combinations"],
            "suggestions": ["list of compatibility improvement suggestions"]
          },
          "recommendations": {
            "immediate_actions": ["list of immediate actions to take"],
            "future_considerations": ["list of things to consider for the future"],
            "alternative_technologies": ["list of alternative technologies to consider"]
          }
        }

        Provide a thorough analysis. Score each aspect from 0-100.
        Be specific in your recommendations and provide actionable insights.
        """;

    public ValidateStackHandler(ChatModel chatModel, ObjectMapper objectMapper) {
        this.chatModel = chatModel;
        this.objectMapper = objectMapper;
    }

    @Override
    public Response handle(Request cmd) {
        try {
            String userPromptText = buildUserPrompt(cmd);

            // Use Spring AI with JSON response format for Gemini
            Prompt prompt = new Prompt(
                    List.of(
                            new SystemMessage(SYSTEM_PROMPT),
                            new UserMessage(userPromptText)
                    ),
                    org.springframework.ai.chat.prompt.ChatOptions.builder()
                        .model("gemini-1.5-flash")
                        .temperature(0.7)
                        .build()
            );

            var response = chatModel.call(prompt);
            String content = response.getResult().getOutput().getText();

            return objectMapper.readValue(content, Response.class);

        } catch (Exception e) {
            log.error("Error validating tech stack", e);
            return Response.error("Error validating stack. Please try again.");
        }
    }

    private String buildUserPrompt(Request cmd) {
        String technologies = cmd.nodes().stream()
                .map(node -> {
                    if (node.data() != null && node.data().containsKey("label")) {
                        return Objects.toString(node.data().get("label"), "");
                    }
                    return "";
                })
                .filter(s -> !s.isBlank())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        String relationships = cmd.edges().stream()
                .map(edge -> {
                    String sourceLabel = cmd.nodes().stream()
                            .filter(n -> n.id().equals(edge.source()))
                            .findFirst()
                            .map(n -> Objects.toString(n.data().get("label"), ""))
                            .orElse("");
                    String targetLabel = cmd.nodes().stream()
                            .filter(n -> n.id().equals(edge.target()))
                            .findFirst()
                            .map(n -> Objects.toString(n.data().get("label"), ""))
                            .orElse("");
                    return sourceLabel.isEmpty() || targetLabel.isEmpty()
                            ? null
                            : sourceLabel + " -> " + targetLabel;
                })
                .filter(Objects::nonNull)
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        return String.format("""
            Analyze this technology stack:
            Technologies: %s
            %s
            
            Provide a detailed analysis of the stack's viability, strengths, weaknesses, and recommendations.
            """, technologies,
                relationships.isEmpty() ? "No relationships defined" : "Relationships: " + relationships);
    }
}
