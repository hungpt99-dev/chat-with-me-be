package com.chatme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Chat request from the frontend.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatRequest(List<ChatMessage> messages) {}
