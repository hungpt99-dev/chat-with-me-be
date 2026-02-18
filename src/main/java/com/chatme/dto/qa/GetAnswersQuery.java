package com.chatme.dto.qa;

import lombok.Builder;

@Builder
public record GetAnswersQuery(
    String questionId,
    int page,
    int limit
) {}
