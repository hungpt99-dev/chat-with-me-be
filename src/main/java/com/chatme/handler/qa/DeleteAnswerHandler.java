package com.chatme.handler.qa;

import com.chatme.repository.AnswerRepository;
import com.fast.cqrs.cqrs.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteAnswerHandler implements CommandHandler<DeleteAnswerHandler.Request> {

    public record Request(String id) {}

    private final AnswerRepository answerRepository;

    @Override
    public void handle(Request cmd) {
        answerRepository.deleteById(cmd.id());
    }
}
