package com.chatme.handler.qa;

import com.chatme.entity.Question;
import com.chatme.repository.QuestionRepository;
import com.chatme.repository.AnswerRepository;
import com.fast.cqrs.cqrs.QueryHandler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetQuestionsHandler implements QueryHandler<GetQuestionsHandler.Request, GetQuestionsHandler.Response> {

    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        int page,
        int limit,
        String sort,
        String search
    ) {}

    @Builder
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        private List<QuestionDto> data;
        private Meta meta;

        @Builder
        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class QuestionDto {
            private String id;
            private String title;
            private String content;
            private User author;
            private Instant created_at;
            private int answers_count;
            private int views;
        }

        @Builder
        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class User {
            private String id;
            private String name;
            private String avatar_url;
        }

        @Builder
        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Meta {
            private long total;
            private int page;
            private int limit;
            private int total_pages;
        }
    }

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public GetQuestionsHandler(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public Response handle(Request query) {
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

        List<Response.QuestionDto> dtos = questions.stream()
            .map(this::mapQuestion)
            .collect(Collectors.toList());

        return Response.builder()
            .data(dtos)
            .meta(Response.Meta.builder()
                .total(total)
                .page(page)
                .limit(limit)
                .total_pages((int) Math.ceil((double) total / limit))
                .build())
            .build();
    }

    private Response.QuestionDto mapQuestion(Question q) {
        return Response.QuestionDto.builder()
            .id(q.getId())
            .title(q.getTitle())
            .content(q.getContent())
            .author(Response.User.builder()
                .id(q.getAuthorId())
                .name(q.getAuthorName())
                .avatar_url(q.getAuthorAvatar())
                .build())
            .created_at(q.getCreatedAt())
            .answers_count((int) answerRepository.countByQuestionId(q.getId()))
            .views(q.getViews())
            .build();
    }
}
