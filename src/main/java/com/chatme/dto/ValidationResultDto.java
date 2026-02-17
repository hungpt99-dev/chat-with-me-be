package com.chatme.dto;

import java.util.List;

/**
 * Validation result DTO matching the FE ValidationResult interface.
 */
public record ValidationResultDto(
    boolean isValid,
    String message,
    int overall_score,
    Scores scores,
    Analysis analysis,
    CompatibilityMatrix compatibility_matrix,
    Recommendations recommendations
) {
    public record Scores(
        int overall,
        int performance,
        int scalability,
        int maintainability,
        int security,
        int cost_efficiency
    ) {}

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

    public record CompatibilityMatrix(
        List<String> compatible_pairs,
        List<String> incompatible_pairs,
        List<String> suggestions
    ) {}

    public record Recommendations(
        List<String> immediate_actions,
        List<String> future_considerations,
        List<String> alternative_technologies
    ) {}

    /**
     * Returns a default error result.
     */
    public static ValidationResultDto error(String message) {
        return new ValidationResultDto(
            false, message, 0,
            new Scores(0, 0, 0, 0, 0, 0),
            new Analysis(List.of(), List.of(), "", "", "", "", "", ""),
            new CompatibilityMatrix(List.of(), List.of(), List.of()),
            new Recommendations(List.of(), List.of(), List.of())
        );
    }
}
