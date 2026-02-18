package com.chatme.handler.qa;

import com.chatme.dto.qa.GetQuestionQuery;
import com.chatme.dto.qa.QuestionDto;
import com.chatme.dto.qa.UserDto;
import com.chatme.entity.Question;
import com.chatme.repository.QuestionRepository;
import com.chatme.repository.AnswerRepository;
import com.fast.cqrs.cqrs.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetQuestionHandler implements QueryHandler<GetQuestionQuery, QuestionDto> {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public GetQuestionHandler(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public QuestionDto handle(GetQuestionQuery query) {
        // Increment views
        questionRepository.incrementViews(query.id());

        Question q = questionRepository.findById(query.id());
        if (q == null) {
            throw new RuntimeException("Question not found");
        }

        return mapQuestion(q);
    }

    private QuestionDto mapQuestion(Question q) {
        return QuestionDto.builder()
            .id(q.getId())
            .title(q.getTitle())
            .content(q.getContent())
            .author(UserDto.builder()
                .id(q.getAuthorId())
                .name(q.getAuthorName())
                .avatar_url(q.getAuthorAvatar())
                .build())
            .created_at(q.getCreatedAt())
            .answers_count((int) answerRepository.countByQuestionId(q.getId()))
            .views(q.getViews())
            .tags(q.getTags() != null ? List.of(q.getTags()) : List.of())
            .build();
    }
}
