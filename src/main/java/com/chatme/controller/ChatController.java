package com.chatme.controller;

import com.chatme.handler.ai.ChatHandler;
import com.fast.cqrs.cqrs.annotation.HttpController;
import com.fast.cqrs.cqrs.annotation.Query;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@HttpController
@RequestMapping("/api/chat")
public interface ChatController {

    @PostMapping
    @Query(handler = ChatHandler.class)
    SseEmitter chat(@RequestBody ChatHandler.Request request);
}
