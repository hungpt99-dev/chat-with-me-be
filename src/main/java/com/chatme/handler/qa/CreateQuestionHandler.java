package com.chatme.handler.qa;

import com.chatme.dto.qa.CreateQuestionRequest;
import com.chatme.entity.Question;
import com.chatme.repository.QuestionRepository;
import com.fast.cqrs.cqrs.CommandHandler;
import com.fast.cqrs.util.IdGenerator;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreateQuestionHandler implements CommandHandler<CreateQuestionRequest> {

    private final QuestionRepository questionRepository;

    public CreateQuestionHandler(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public void handle(CreateQuestionRequest cmd) {
        String id = IdGenerator.uuid();
        String authorId = "anon-user";
        String authorName = cmd.author_name() != null ? cmd.author_name() : "Anonymous";
        String authorAvatar = cmd.author_avatar() != null ? cmd.author_avatar() : "https://i.pravatar.cc/150?u=" + authorId;
        Instant now = Instant.now();

        List<String> tagsList = cmd.tags() != null ? cmd.tags() : new ArrayList<>();
        String[] tagsArray = tagsList.toArray(new String[0]);

        Question question = Question.builder()
            .id(id)
            .title(cmd.title())
            .content(cmd.content())
            .authorId(authorId)
            .authorName(authorName)
            .authorAvatar(authorAvatar)
            .createdAt(now)
            .views(0)
            .tags(tagsArray)
            .build();

        questionRepository.save(question);
    }
}
