package com.chatme.dto.qa;

import lombok.Builder;
import java.time.Instant;
import java.util.List;

@Builder
public record AnswerDto(
    String id,
    String content,
    UserDto author,
    Instant created_at,
    List<ReactionDto> reactions
) {}
