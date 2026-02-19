package com.chatme.handler.reaction;

import com.chatme.entity.BlogPostReaction;
import com.chatme.repository.BlogPostReactionRepository;
import com.fast.cqrs.cqrs.CommandHandler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReactToPostHandler implements CommandHandler<ReactToPostHandler.Request> {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        String postId,
        String userId,
        String type
    ) {}

    private final BlogPostReactionRepository reactionRepository;
    // BlogPostRepository needed if we want to check existence or get author (but author is string)

    @Override
    public void handle(Request cmd) {
        if (cmd.postId() == null || cmd.userId() == null) {
            throw new IllegalArgumentException("PostId and UserId are required");
        }

        try {
            BlogPostReaction reaction = BlogPostReaction.builder()
                    .id(UUID.randomUUID().toString())
                    .postId(cmd.postId())
                    .userId(cmd.userId())
                    .type(cmd.type() != null ? cmd.type() : "LIKE")
                    .build();

            reactionRepository.save(reaction);

            // No event published for now as we don't have authorId reliable
            // Or we could publish with null authorId if we want stats

        } catch (Exception e) {
            // Ignore duplicate
        }
    }
}
