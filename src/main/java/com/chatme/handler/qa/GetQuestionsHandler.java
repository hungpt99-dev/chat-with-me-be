package com.chatme.handler.qa;

import com.chatme.dto.qa.*;
import com.chatme.entity.Question;
import com.chatme.repository.QuestionRepository;
import com.chatme.repository.AnswerRepository;
import com.fast.cqrs.cqrs.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetQuestionsHandler implements QueryHandler<GetQuestionsQuery, QuestionsResponse> {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public GetQuestionsHandler(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public QuestionsResponse handle(GetQuestionsQuery query) {
        int page = query.page() < 1 ? 1 : query.page();
        int limit = query.limit() < 1 ? 10 : query.limit();
        int offset = (page - 1) * limit;

        List<Question> questions;
        long total;

        if (query.search() != null && !query.search().isBlank()) {
            questions = questionRepository.search("%" + query.search() + "%", limit, offset);
            total = questionRepository.countSearch("%" + query.search() + "%");
        } else if ("popular".equals(query.sort())) {
            questions = questionRepository.findAllPopular(limit, offset);
            total = questionRepository.count();
        } else {
            // Default newest
            questions = questionRepository.findAllNewest(limit, offset);
            total = questionRepository.count();
        }

        List<QuestionDto> dtos = questions.stream()
            .map(this::mapQuestion)
            .collect(Collectors.toList());

        return QuestionsResponse.builder()
            .data(dtos)
            .meta(QuestionsResponse.Meta.builder()
                .total(total)
                .page(page)
                .limit(limit)
                .total_pages((int) Math.ceil((double) total / limit))
                .build())
            .build();
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
