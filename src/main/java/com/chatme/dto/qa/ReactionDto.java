package com.chatme.dto.qa;

import lombok.Builder;

@Builder
public record ReactionDto(
    String type,
    int count,
    boolean user_reacted
) {}
