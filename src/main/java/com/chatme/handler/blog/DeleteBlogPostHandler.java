package com.chatme.handler.blog;

import com.chatme.repository.BlogPostRepository;
import com.fast.cqrs.cqrs.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteBlogPostHandler implements CommandHandler<DeleteBlogPostHandler.Request> {

    public record Request(String id) {}

    private final BlogPostRepository blogPostRepository;

    @Override
    public void handle(Request cmd) {
        blogPostRepository.deleteById(cmd.id());
    }
}
