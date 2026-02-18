package com.chatme.handler;

import com.chatme.dto.BlogPostDto;
import com.chatme.dto.GetBlogPostsQuery;
import com.chatme.entity.BlogPost;
import com.chatme.repository.BlogPostRepository;
import com.fast.cqrs.cqrs.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetBlogPostsHandler implements QueryHandler<GetBlogPostsQuery, List<BlogPostDto>> {

    private final BlogPostRepository blogPostRepository;

    public GetBlogPostsHandler(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    @Override
    public List<BlogPostDto> handle(GetBlogPostsQuery query) {
        return blogPostRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private BlogPostDto mapToDto(BlogPost post) {
        return new BlogPostDto(
            post.getId(),
            post.getTitle(),
            post.getCreatedDate(), // Using date string as is for now
            post.getAuthor(),
            post.getTags() != null ? List.of(post.getTags()) : List.of(),
            post.getCategory(),
            post.getDescription(),
            post.getBody(), // Or summary if needed
            post.getImageUrl(),
            post.getReadTime(), // Assuming readTime is stored in DB
            post.getCreatedDate(), // created_at
            post.getCreatedDate() // updated_at
        );
    }
}
