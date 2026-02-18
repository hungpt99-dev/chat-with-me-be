package com.chatme.handler.qa;

import com.chatme.dto.qa.ReactToAnswerCmd;
import com.chatme.entity.AnswerReaction;
import com.chatme.repository.AnswerReactionRepository;
import com.fast.cqrs.cqrs.CommandHandler;
import com.fast.cqrs.util.IdGenerator;
import org.springframework.stereotype.Component;

@Component
public class ReactToAnswerHandler implements CommandHandler<ReactToAnswerCmd> {

    private final AnswerReactionRepository reactionRepository;

    public ReactToAnswerHandler(AnswerReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    @Override
    public void handle(ReactToAnswerCmd cmd) {
        String userId = "anon-user"; // Simplified
        
        AnswerReaction existing = reactionRepository.findByUniqueKey(cmd.answerId(), userId, cmd.type());
        
        if (existing != null) {
            // Toggle off
            reactionRepository.deleteByUniqueKey(cmd.answerId(), userId, cmd.type());
        } else {
            // Toggle on
            AnswerReaction reaction = AnswerReaction.builder()
                .id(IdGenerator.uuid())
                .answerId(cmd.answerId())
                .userId(userId)
                .type(cmd.type())
                .build();
            reactionRepository.save(reaction);
        }
    }
}
