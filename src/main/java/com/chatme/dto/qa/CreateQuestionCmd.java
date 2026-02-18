package com.chatme.dto.qa;

import java.util.List;

public record CreateQuestionCmd(
    String title,
    String content,
    List<String> tags
) {}
