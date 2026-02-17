package com.chatme.dto;

/**
 * Individual chat message with role (user/assistant) and content.
 */
public record ChatMessage(String role, String content) {}
