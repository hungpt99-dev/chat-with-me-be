package com.chatme.dto;

import java.util.List;
import java.util.Map;

/**
 * Command for validating a tech stack via OpenAI.
 */
public record ValidateStackCmd(
    List<TechNode> nodes,
    List<TechEdge> edges
) {
    public record TechNode(String id, Map<String, Object> data) {}
    public record TechEdge(String id, String source, String target) {}
}
