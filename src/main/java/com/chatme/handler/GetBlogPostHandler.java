package com.chatme.handler;

import com.chatme.dto.BlogPostDto;
import com.chatme.dto.GetBlogPostQuery;
import com.chatme.entity.BlogPost;
import com.chatme.repository.BlogPostRepository;
import com.fast.cqrs.cqrs.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetBlogPostHandler implements QueryHandler<GetBlogPostQuery, BlogPostDto> {

    private final BlogPostRepository blogPostRepository;

    public GetBlogPostHandler(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    @Override
    public BlogPostDto handle(GetBlogPostQuery query) {
        return blogPostRepository.findById(query.id())
                .map(this::mapToDto)
                .orElse(null);
    }

    private BlogPostDto mapToDto(BlogPost post) {
        return new BlogPostDto(
            post.getId(),
            post.getTitle(),
            post.getCreatedDate(),
            post.getAuthor(),
            post.getTags() != null ? List.of(post.getTags()) : List.of(),
            post.getCategory(),
            post.getDescription(),
            post.getBody(), // Full body for single post
            post.getImageUrl(),
            post.getReadTime(),
            post.getCreatedDate(), // created_at
            post.getCreatedDate() // updated_at
        );
    }
}
