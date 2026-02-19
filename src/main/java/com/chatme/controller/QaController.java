package com.chatme.controller;

import com.chatme.handler.qa.*;
import com.chatme.handler.reaction.ReactToAnswerHandler;
import com.fast.cqrs.cqrs.annotation.Command;
import com.fast.cqrs.cqrs.annotation.HttpController;
import com.fast.cqrs.cqrs.annotation.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@HttpController
@RequestMapping("/api")
public interface QaController {

    @GetMapping("/questions")
    @Query(handler = GetQuestionsHandler.class)
    GetQuestionsHandler.Response getQuestions(@ModelAttribute GetQuestionsHandler.Request query);

    @PostMapping("/questions")
    @ResponseStatus(HttpStatus.CREATED)
    @Command(handler = CreateQuestionHandler.class)
    void createQuestion(@RequestBody CreateQuestionHandler.Request request);

    @GetMapping("/questions/{id}")
    @Query(handler = GetQuestionHandler.class)
    GetQuestionHandler.Response getQuestion(@ModelAttribute GetQuestionHandler.Request query);

    @GetMapping("/questions/{id}/answers")
    @Query(handler = GetAnswersHandler.class)
    GetAnswersHandler.Response getAnswers(@ModelAttribute GetAnswersHandler.Request query);

    @PostMapping("/questions/{id}/answers")
    @ResponseStatus(HttpStatus.CREATED)
    @Command(handler = CreateAnswerHandler.class)
    void createAnswer(@RequestBody CreateAnswerHandler.Request cmd);

    @PostMapping("/answers/{id}/react")
    @Command(handler = ReactToAnswerHandler.class)
    void reactToAnswer(@RequestBody ReactToAnswerHandler.Request cmd);
}
