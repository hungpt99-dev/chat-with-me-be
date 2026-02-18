package com.chatme.dto.qa;

import lombok.Builder;

@Builder
public record GetQuestionsQuery(
    int page,
    int limit,
    String sort,
    String search
) {}
