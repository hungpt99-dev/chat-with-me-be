package com.chatme.event;

import java.time.Instant;

public record CommentAddedEvent(
    String commentId,
    String postId,
    String authorName,
    String content,
    String postAuthorId,
    Instant timestamp
) {}
