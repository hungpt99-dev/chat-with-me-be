package com.chatme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;

/**
 * Command for validating a tech stack via OpenAI.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ValidateStackCmd(
    List<TechNode> nodes,
    List<TechEdge> edges
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TechNode(String id, Map<String, Object> data) {}
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TechEdge(String id, String source, String target) {}
}
