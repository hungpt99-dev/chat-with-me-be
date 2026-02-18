package com.chatme.handler;

import com.chatme.dto.ValidateStackCmd;
import com.chatme.dto.ValidationResultDto;
import com.fast.cqrs.cqrs.QueryHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Handler for validating tech stacks via AI analysis.
 * <p>
 * Uses Spring AI's {@link ChatModel} for provider-agnostic AI calls
 * with JSON output format for structured responses.
 */
@Component
public class ValidateStackHandler implements QueryHandler<ValidateStackCmd, ValidationResultDto> {

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
    public ValidationResultDto handle(ValidateStackCmd cmd) {
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

            return objectMapper.readValue(content, ValidationResultDto.class);

        } catch (Exception e) {
            log.error("Error validating tech stack", e);
            return ValidationResultDto.error("Error validating stack. Please try again.");
        }
    }

    private String buildUserPrompt(ValidateStackCmd cmd) {
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
