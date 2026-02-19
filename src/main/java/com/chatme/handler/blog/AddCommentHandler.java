package com.chatme.handler.blog;

import com.chatme.entity.BlogComment;
import com.chatme.entity.BlogPost;
import com.chatme.repository.BlogCommentRepository;
import com.chatme.repository.BlogPostRepository;
import com.fast.cqrs.cqrs.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.UUID;

import com.chatme.event.CommentAddedEvent;

@Component
@RequiredArgsConstructor
public class AddCommentHandler implements CommandHandler<AddCommentHandler.Request> {

    public record Request(String postId, String author, String content) {}

    private final BlogCommentRepository blogCommentRepository;
    private final BlogPostRepository blogPostRepository;
    private final com.fast.cqrs.cqrs.EventBus eventBus;

    @Override
    public void handle(Request cmd) {
        BlogPost post = blogPostRepository.findById(cmd.postId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        BlogComment comment = BlogComment.builder()
                .id(UUID.randomUUID().toString())
                .postId(cmd.postId())
                .authorName(cmd.author())
                .content(cmd.content())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();
        
        blogCommentRepository.save(comment);

        eventBus.publish(new CommentAddedEvent(
                comment.getId(),
                comment.getPostId(),
                comment.getAuthorName(),
                comment.getContent(),
                post.getAuthor(),
                comment.getCreatedAt() != null ? comment.getCreatedAt().toInstant() : null
        ));
    }
}
