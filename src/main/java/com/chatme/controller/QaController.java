package com.chatme.controller;

import com.chatme.dto.qa.*;
import com.chatme.handler.qa.*;
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
    QuestionsResponse getQuestions(@ModelAttribute GetQuestionsQuery query);

    @PostMapping("/questions")
    @ResponseStatus(HttpStatus.CREATED)
    @Command(handler = CreateQuestionHandler.class)
    void createQuestion(@RequestBody CreateQuestionRequest request);

    @GetMapping("/questions/{id}")
    @Query(handler = GetQuestionHandler.class)
    QuestionDto getQuestion(@ModelAttribute GetQuestionQuery query);

    @GetMapping("/questions/{id}/answers")
    @Query(handler = GetAnswersHandler.class)
    AnswersResponse getAnswers(@ModelAttribute GetAnswersQuery query);

    @PostMapping("/questions/{id}/answers")
    @ResponseStatus(HttpStatus.CREATED)
    @Command(handler = CreateAnswerHandler.class)
    void createAnswer(@RequestBody CreateAnswerCmd cmd);

    @PostMapping("/answers/{id}/react")
    @Command(handler = ReactToAnswerHandler.class)
    void reactToAnswer(@RequestBody ReactToAnswerCmd cmd);
}
