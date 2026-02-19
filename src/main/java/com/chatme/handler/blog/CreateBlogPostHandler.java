package com.chatme.handler.blog;

import com.chatme.config.SecurityUtils;
import com.chatme.entity.BlogPost;
import com.chatme.repository.BlogPostRepository;
import com.fast.cqrs.cqrs.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateBlogPostHandler implements CommandHandler<CreateBlogPostHandler.Request> {

    public record Request(
        String id,
        String title,
        String content,
        String description,
        String author,
        String category,
        String imageUrl
    ) {}

    private final BlogPostRepository blogPostRepository;

    @Override
    public void handle(Request cmd) {
        String id = cmd.id();
        if (id == null || id.isBlank()) {
            if (cmd.title() != null && !cmd.title().isBlank()) {
                id = toSlug(cmd.title());
            } else {
                id = UUID.randomUUID().toString();
            }
        }

        String readTime = calculateReadTime(cmd.content());
        String createdDate = DateTimeFormatter.ofPattern("MMM dd, yyyy").format(LocalDate.now());

        BlogPost post = BlogPost.builder()
                .id(id)
                .title(cmd.title())
                .body(cmd.content())
                .description(cmd.description())
                .author(SecurityUtils.getAuthorName(cmd.author() != null ? cmd.author() : "Admin"))
                .category(cmd.category())
                .imageUrl(cmd.imageUrl())
                .readTime(readTime)
                .createdDate(createdDate)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

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

    private String toSlug(String input) {
        return input.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }
}
