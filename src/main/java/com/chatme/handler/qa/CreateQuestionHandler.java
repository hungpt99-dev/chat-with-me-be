package com.chatme.handler.qa;

import com.chatme.entity.Question;
import com.chatme.repository.QuestionRepository;
import com.fast.cqrs.cqrs.CommandHandler;
import com.fast.cqrs.util.IdGenerator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class CreateQuestionHandler implements CommandHandler<CreateQuestionHandler.Request> {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        String title,
        String content,
        String author_name,
        String author_avatar
    ) {}

    private final QuestionRepository questionRepository;

    public CreateQuestionHandler(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public void handle(Request cmd) {
        String id = IdGenerator.uuid();
        String authorId = "anon-user";
        String authorName = cmd.author_name() != null ? cmd.author_name() : "Anonymous";
        String authorAvatar = cmd.author_avatar() != null ? cmd.author_avatar() : "https://i.pravatar.cc/150?u=" + id;
        Timestamp now = new Timestamp(System.currentTimeMillis());

        Question question = Question.builder()
            .id(id)
            .title(cmd.title())
            .content(cmd.content())
            .authorId(authorId)
            .authorName(authorName)
            .authorAvatar(authorAvatar)
            .createdAt(now)
            .views(0)
            .build();

        questionRepository.save(question);
    }
}
