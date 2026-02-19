package com.chatme.handler.qa;

import com.chatme.entity.Answer;
import com.chatme.entity.AnswerReaction;
import com.chatme.repository.AnswerRepository;
import com.chatme.repository.AnswerReactionRepository;
import com.fast.cqrs.cqrs.QueryHandler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GetAnswersHandler implements QueryHandler<GetAnswersHandler.Request, GetAnswersHandler.Response> {

    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request(
        String questionId,
        int page,
        int limit
    ) {}

    @Builder
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        private List<AnswerDto> data;
        private Meta meta;

        @Builder
        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class AnswerDto {
            private String id;
            private String content;
            private User author;
            private Instant created_at;
            private List<ReactionDto> reactions;
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
        public static class ReactionDto {
            private String type;
            private int count;
            private boolean user_reacted;
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

    private final AnswerRepository answerRepository;
    private final AnswerReactionRepository reactionRepository;

    public GetAnswersHandler(AnswerRepository answerRepository, AnswerReactionRepository reactionRepository) {
        this.answerRepository = answerRepository;
        this.reactionRepository = reactionRepository;
    }

    @Override
    public Response handle(Request query) {
        int page = query.page() < 1 ? 1 : query.page();
        int limit = query.limit() < 1 ? 10 : query.limit();
        int offset = (page - 1) * limit;

        List<Answer> answers = answerRepository.findByQuestionId(query.questionId(), limit, offset);
        long total = answerRepository.countByQuestionId(query.questionId());

        List<Response.AnswerDto> dtos = answers.stream()
            .map(this::mapAnswer)
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

    private Response.AnswerDto mapAnswer(Answer a) {
        return Response.AnswerDto.builder()
            .id(a.getId())
            .content(a.getContent())
            .author(Response.User.builder()
                .id(a.getAuthorId())
                .name(a.getAuthorName())
                .avatar_url(a.getAuthorAvatar())
                .build())
            .created_at(a.getCreatedAt() != null ? a.getCreatedAt().toInstant() : null)
            .reactions(getReactions(a.getId()))
            .build();
    }

    private List<Response.ReactionDto> getReactions(String answerId) {
        List<AnswerReaction> reactions = reactionRepository.findByAnswerId(answerId);
        
        Map<String, Long> counts = reactions.stream()
            .collect(Collectors.groupingBy(AnswerReaction::getType, Collectors.counting()));

        return counts.entrySet().stream()
            .map(entry -> Response.ReactionDto.builder()
                .type(entry.getKey())
                .count(entry.getValue().intValue())
                .user_reacted(checkUserReacted(reactions, entry.getKey()))
                .build())
            .collect(Collectors.toList());
    }
    
    private boolean checkUserReacted(List<AnswerReaction> reactions, String type) {
        String userId = "anon-user"; // Simplified
        return reactions.stream()
            .anyMatch(r -> r.getUserId().equals(userId) && r.getType().equals(type));
    }
}
