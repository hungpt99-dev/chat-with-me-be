package com.chatme.handler.blog;

import com.chatme.entity.BlogComment;
import com.chatme.repository.BlogCommentRepository;
import com.fast.cqrs.cqrs.QueryHandler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetPostCommentsHandler implements QueryHandler<GetPostCommentsHandler.Request, List<GetPostCommentsHandler.Response>> {

    public record Request(String postId) {}

    @Builder
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        private String id;
        private String author;
        private String content;
        private Instant created_at;
    }

    private final BlogCommentRepository blogCommentRepository;

    @Override
    public List<Response> handle(Request query) {
        return blogCommentRepository.findByPostId(query.postId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private Response mapToDto(BlogComment comment) {
        return Response.builder()
                .id(comment.getId())
                .author(comment.getAuthorName())
                .content(comment.getContent())
                .created_at(comment.getCreatedAt() != null ? comment.getCreatedAt().toInstant() : null)
                .build();
    }
}
