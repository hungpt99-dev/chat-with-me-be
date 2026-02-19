package com.chatme.handler.qa;

import com.chatme.repository.QuestionRepository;
import com.fast.cqrs.cqrs.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteQuestionHandler implements CommandHandler<DeleteQuestionHandler.Request> {

    public record Request(String id) {}

    private final QuestionRepository questionRepository;

    @Override
    public void handle(Request cmd) {
        questionRepository.deleteById(cmd.id());
    }
}
