package com.chatme.handler.blog;

import com.chatme.entity.BlogPost;
import com.chatme.repository.BlogPostRepository;
import com.fast.cqrs.cqrs.QueryHandler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetBlogPostHandler implements QueryHandler<GetBlogPostHandler.Request, GetBlogPostHandler.Response> {

    public record Request(String id) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        String id,
        String title,
        String date,
        String author,
        List<String> tags,
        String category,
        String description,
        String content,
        String imageUrl,
        String readTime,
        String created_at,
        String updated_at
    ) {}

    private final BlogPostRepository blogPostRepository;

    public GetBlogPostHandler(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    @Override
    public Response handle(Request query) {
        return blogPostRepository.findById(query.id())
                .map(this::mapToDto)
                .orElse(null);
    }

    private Response mapToDto(BlogPost post) {
        return new Response(
            post.getId(),
            post.getTitle(),
            post.getCreatedDate(),
            post.getAuthor(),
            post.getTags() != null ? List.of(post.getTags()) : List.of(),
            post.getCategory(),
            post.getDescription(),
            post.getBody(),
            post.getImageUrl(),
            post.getReadTime(),
            post.getCreatedDate(),
            post.getCreatedDate()
        );
    }
}
