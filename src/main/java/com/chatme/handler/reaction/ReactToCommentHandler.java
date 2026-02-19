package com.chatme.handler.reaction;

import com.chatme.entity.CommentReaction;
import com.chatme.repository.CommentReactionRepository;
import com.fast.cqrs.cqrs.CommandHandler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReactToCommentHandler implements CommandHandler<ReactToCommentHandler.Request> {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        String commentId,
        String userId,
        String type
    ) {}

    private final CommentReactionRepository reactionRepository;

    @Override
    public void handle(Request cmd) {
        if (cmd.commentId() == null || cmd.userId() == null) {
            throw new IllegalArgumentException("CommentId and UserId are required");
        }

        try {
            CommentReaction reaction = CommentReaction.builder()
                    .id(UUID.randomUUID().toString())
                    .commentId(cmd.commentId())
                    .userId(cmd.userId())
                    .type(cmd.type() != null ? cmd.type() : "LIKE")
                    .build();

            reactionRepository.save(reaction);

        } catch (Exception e) {
            // Ignore duplicate
        }
    }
}
