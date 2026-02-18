package com.chatme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Individual chat message with role (user/assistant) and content.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatMessage(String role, String content) {}
