package com.chatme.dto.qa;

import lombok.Builder;
import java.util.List;

@Builder
public record AnswersResponse(
    List<AnswerDto> data,
    Meta meta
) {
    @Builder
    public record Meta(
        long total,
        int page,
        int limit,
        int total_pages
    ) {}
}
