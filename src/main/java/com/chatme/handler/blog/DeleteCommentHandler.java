package com.chatme.handler.blog;

import com.chatme.repository.BlogCommentRepository;
import com.fast.cqrs.cqrs.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteCommentHandler implements CommandHandler<DeleteCommentHandler.Request> {

    public record Request(String id) {}

    private final BlogCommentRepository blogCommentRepository;

    @Override
    public void handle(Request cmd) {
        blogCommentRepository.deleteById(cmd.id());
    }
}
