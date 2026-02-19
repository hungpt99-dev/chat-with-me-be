package com.chatme.event;

public record AnswerReactedEvent(
    String reactionId,
    String answerId,
    String userId,
    String type,
    String answerAuthorId
) {}
