package com.chatme.controller;

import com.chatme.handler.ai.ValidateStackHandler;
import com.fast.cqrs.cqrs.annotation.HttpController;
import com.fast.cqrs.cqrs.annotation.Query;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@HttpController
@RequestMapping("/api/validate-stack")
public interface ValidateStackController {

    /**
     * mapped as @Query because it returns data, even though it's a POST.
     * Fast Framework supports POST for queries if needed (common for complex search criteria).
     */
    @PostMapping
    @Query(handler = ValidateStackHandler.class)
    ValidateStackHandler.Response validate(@RequestBody ValidateStackHandler.Request cmd);
}
