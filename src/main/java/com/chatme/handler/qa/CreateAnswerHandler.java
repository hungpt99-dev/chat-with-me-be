package com.chatme.handler.qa;

import com.chatme.entity.Answer;
import com.chatme.repository.AnswerRepository;
import com.fast.cqrs.cqrs.CommandHandler;
import com.fast.cqrs.util.IdGenerator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class CreateAnswerHandler implements CommandHandler<CreateAnswerHandler.Request> {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        String questionId,
        String content
    ) {}

    private final AnswerRepository answerRepository;

    public CreateAnswerHandler(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public void handle(Request cmd) {
        String id = IdGenerator.uuid();
        String authorId = "anon-user";
        String authorName = "Anonymous";
        String authorAvatar = "https://i.pravatar.cc/150?u=" + id;
        Timestamp now = new Timestamp(System.currentTimeMillis());

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
