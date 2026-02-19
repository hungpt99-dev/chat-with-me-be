package com.chatme.handler.blog;

import com.chatme.config.SecurityUtils;
import com.chatme.entity.BlogPost;
import com.chatme.repository.BlogPostRepository;
import com.fast.cqrs.cqrs.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateBlogPostHandler implements CommandHandler<UpdateBlogPostHandler.Request> {

    public record Request(
        String id,
        String title,
        String content,
        String description,
        String author,
        String category,
        String imageUrl,
        List<String> tags
    ) {}

    private final BlogPostRepository blogPostRepository;

    @Override
    public void handle(Request cmd) {
        BlogPost post = blogPostRepository.findById(cmd.id())
                .orElseThrow(() -> new IllegalArgumentException("Blog post not found: " + cmd.id()));

        if (cmd.title() != null) post.setTitle(cmd.title());
        if (cmd.content() != null) {
            post.setBody(cmd.content());
            post.setReadTime(calculateReadTime(cmd.content()));
        }
        if (cmd.description() != null) post.setDescription(cmd.description());
        // Author is always taken from the JWT token, never from the request body
        post.setAuthor(SecurityUtils.getAuthorName(post.getAuthor()));
        if (cmd.category() != null) post.setCategory(cmd.category());
        if (cmd.imageUrl() != null) post.setImageUrl(cmd.imageUrl());
        if (cmd.tags() != null) post.setTags(cmd.tags().toArray(new String[0]));

        post.setUpdatedAt(Instant.now());

        blogPostRepository.save(post);
    }

    private String calculateReadTime(String content) {
        if (content == null || content.isEmpty()) {
            return "1 min read";
        }
        int wordCount = content.split("\\s+").length;
        int readTime = (int) Math.ceil(wordCount / 200.0);
        return readTime + " min read";
    }
}
