package com.chatme.handler.qa;

import com.chatme.dto.qa.CreateAnswerCmd;
import com.chatme.entity.Answer;
import com.chatme.repository.AnswerRepository;
import com.fast.cqrs.cqrs.CommandHandler;
import com.fast.cqrs.util.IdGenerator;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CreateAnswerHandler implements CommandHandler<CreateAnswerCmd> {

    private final AnswerRepository answerRepository;

    public CreateAnswerHandler(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public void handle(CreateAnswerCmd cmd) {
        String id = IdGenerator.uuid();
        String authorId = "anon-user";
        String authorName = "Anonymous";
        String authorAvatar = "https://i.pravatar.cc/150?u=" + authorId;
        Instant now = Instant.now();

        Answer answer = Answer.builder()
            .id(id)
            .questionId(cmd.questionId())
            .content(cmd.content())
            .authorId(authorId)
            .authorName(authorName)
            .authorAvatar(authorAvatar)
            .createdAt(now)
            .build();

        answerRepository.save(answer);
    }
}
