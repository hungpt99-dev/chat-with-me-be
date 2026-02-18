package com.chatme.dto.qa;

public record CreateAnswerCmd(
    String questionId,
    String content
) {}
