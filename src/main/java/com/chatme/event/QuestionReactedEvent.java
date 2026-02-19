package com.chatme.event;

public record QuestionReactedEvent(
    String reactionId,
    String questionId,
    String userId,
    String type,
    String questionAuthorId
) {}
