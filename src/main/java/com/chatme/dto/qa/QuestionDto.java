package com.chatme.dto.qa;

import lombok.Builder;
import java.time.Instant;
import java.util.List;

@Builder
public record QuestionDto(
    String id,
    String title,
    String content,
    UserDto author,
    Instant created_at,
    int answers_count,
    List<String> tags,
    int views
) {}
