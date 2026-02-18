package com.chatme.dto.qa;

import java.util.List;

public record CreateQuestionRequest(
    String title,
    String content,
    List<String> tags,
    String author_name,
    String author_avatar
) {}
