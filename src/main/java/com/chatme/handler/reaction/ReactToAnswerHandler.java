package com.chatme.handler.reaction;

import com.chatme.entity.Answer;
import com.chatme.entity.AnswerReaction;
import com.chatme.event.AnswerReactedEvent;
import com.chatme.repository.AnswerReactionRepository;
import com.chatme.repository.AnswerRepository;
import com.fast.cqrs.cqrs.CommandHandler;
import com.fast.cqrs.cqrs.EventBus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReactToAnswerHandler implements CommandHandler<ReactToAnswerHandler.Request> {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        String answerId,
        String userId,
        String type
    ) {}

    private final AnswerReactionRepository reactionRepository;
    private final AnswerRepository answerRepository;
    private final com.fast.cqrs.cqrs.EventBus eventBus;

    @Override
    public void handle(Request cmd) {
        Answer answer = answerRepository.findById(cmd.answerId())
                .orElseThrow(() -> new IllegalArgumentException("Answer not found"));

        if (cmd.answerId() == null) {
            throw new IllegalArgumentException("AnswerId is required");
        }
        
        String userId = cmd.userId();
        if (userId == null || userId.isBlank()) {
            userId = "anon-user"; // Fallback to anon-user
        }
        
        String type = cmd.type() != null ? cmd.type() : "LIKE";

        try {
            // Check for existing reaction
            AnswerReaction existing = reactionRepository.findByUniqueKey(cmd.answerId(), userId, type);
            
            if (existing != null) {
                // Toggle off
                reactionRepository.deleteByUniqueKey(cmd.answerId(), userId, type);
            } else {
                // Toggle on
                AnswerReaction reaction = AnswerReaction.builder()
                        .id(UUID.randomUUID().toString())
                        .answerId(cmd.answerId())
                        .userId(userId)
                        .type(type)
                        .build();

                reactionRepository.save(reaction);

                // Publish event only on add
                eventBus.publish(new AnswerReactedEvent(
                        reaction.getId(),
                        cmd.answerId(),
                        cmd.userId(), // can be null or anon
                        reaction.getType(),
                        answer.getAuthorId()
                ));
            }
        } catch (Exception e) {
            // Ignore duplicate or other errors
        }
    }
}
