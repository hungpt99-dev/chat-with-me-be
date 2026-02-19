package com.chatme.handler.reaction;

import com.chatme.event.QuestionReactedEvent;
import com.chatme.entity.Question;
import com.chatme.entity.QuestionReaction;
import com.chatme.repository.QuestionReactionRepository;
import com.chatme.repository.QuestionRepository;
import com.fast.cqrs.cqrs.CommandHandler;
import com.fast.cqrs.cqrs.EventBus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReactToQuestionHandler implements CommandHandler<ReactToQuestionHandler.Request> {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        String questionId,
        String userId,
        String type
    ) {}

    private final QuestionReactionRepository reactionRepository;
    private final QuestionRepository questionRepository;
    private final com.fast.cqrs.cqrs.EventBus eventBus;

    @Override
    public void handle(Request cmd) {
        Question question = questionRepository.findById(cmd.questionId())
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        if (cmd.questionId() == null || cmd.userId() == null) {
            throw new IllegalArgumentException("QuestionId and UserId are required");
        }

        // Check availability (optional, unique constraint handles duplicates but throws)
        // Here we just save, letting DB handle duplicate check or ignore
        try {
            QuestionReaction reaction = QuestionReaction.builder()
                    .id(UUID.randomUUID().toString())
                    .questionId(cmd.questionId())
                    .userId(cmd.userId())
                    .type(cmd.type() != null ? cmd.type() : "LIKE")
                    .build();

            reactionRepository.save(reaction);

            // Publish event
            eventBus.publish(new QuestionReactedEvent(
                reaction.getId(),
                reaction.getQuestionId(),
                reaction.getUserId(),
                reaction.getType(),
                question.getAuthorId()
        ));

        } catch (Exception e) {
            // Likely duplicate reaction, ignore or log
            // In a real app we might toggle the reaction (delete if exists)
        }
    }
}
