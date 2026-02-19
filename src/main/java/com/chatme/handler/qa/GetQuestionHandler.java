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

@Component
public class GetQuestionHandler implements QueryHandler<GetQuestionHandler.Request, GetQuestionHandler.Response> {

    public record Request(String id) {}

    @Builder
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        private String id;
        private String title;
        private String content;
        private User author;
        private Instant created_at;
        private int answers_count;
        private List<String> tags;
        private int views;

        @Builder
        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class User {
            private String id;
            private String name;
            private String avatar_url;
        }
    }

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public GetQuestionHandler(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public Response handle(Request query) {
        // Increment views
        questionRepository.incrementViews(query.id());

        Question q = questionRepository.findById(query.id())
            .orElseThrow(() -> new RuntimeException("Question not found"));

        return mapQuestion(q);
    }

    private Response mapQuestion(Question q) {
        return Response.builder()
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
            .tags(q.getTags() != null ? List.of(q.getTags()) : List.of())
            .build();
    }
}
