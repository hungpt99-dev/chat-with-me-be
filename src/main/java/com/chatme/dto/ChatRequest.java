package com.chatme.dto;

import java.util.List;

/**
 * Chat request from the frontend.
 */
public record ChatRequest(List<ChatMessage> messages) {}
