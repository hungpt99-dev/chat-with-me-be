package com.chatme.controller;


import com.chatme.handler.blog.CreateBlogPostHandler;
import com.chatme.handler.blog.DeleteBlogPostHandler;
import com.chatme.handler.blog.DeleteCommentHandler;
import com.chatme.handler.blog.UpdateBlogPostHandler;
import com.chatme.handler.notification.GetNotificationsHandler;
import com.chatme.handler.notification.MarkNotificationReadHandler;
import com.chatme.handler.qa.DeleteAnswerHandler;
import com.chatme.handler.qa.DeleteQuestionHandler;
import com.chatme.handler.system.GetDashboardStatsHandler;
import com.chatme.handler.system.GetSystemConfigHandler;
import com.chatme.handler.system.UpdateSystemConfigHandler;
import com.fast.cqrs.cqrs.annotation.Command;
import com.fast.cqrs.cqrs.annotation.HttpController;
import com.fast.cqrs.cqrs.annotation.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@HttpController
@RequestMapping("/api/admin")
public interface AdminController {

    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    @Command(handler = CreateBlogPostHandler.class)
    void createPost(@RequestBody CreateBlogPostHandler.Request cmd);

    @PutMapping("/posts/{id}")
    @Command(handler = UpdateBlogPostHandler.class)
    void updatePost(@PathVariable("id") String id, @RequestBody UpdateBlogPostHandler.Request cmd);

    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Command(handler = DeleteBlogPostHandler.class)
    void deletePost(@PathVariable("id") String id, @ModelAttribute DeleteBlogPostHandler.Request cmd);

    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Command(handler = DeleteCommentHandler.class)
    void deleteComment(@PathVariable("id") String id, @ModelAttribute DeleteCommentHandler.Request cmd);

    @DeleteMapping("/questions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Command(handler = DeleteQuestionHandler.class)
    void deleteQuestion(@PathVariable("id") String id, @ModelAttribute DeleteQuestionHandler.Request cmd);

    @DeleteMapping("/answers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Command(handler = DeleteAnswerHandler.class)
    void deleteAnswer(@PathVariable("id") String id, @ModelAttribute DeleteAnswerHandler.Request cmd);

    // Notifications
    @GetMapping("/notifications")
    @Query(handler = GetNotificationsHandler.class)
    List<GetNotificationsHandler.NotificationDto> getNotifications(@ModelAttribute GetNotificationsHandler.Request query);

    @PutMapping("/notifications/{id}/read")
    @Command(handler = MarkNotificationReadHandler.class)
    void markNotificationRead(@PathVariable("id") String id, @ModelAttribute MarkNotificationReadHandler.Request cmd);

    @GetMapping("/dashboard/stats")
    @Query(handler = GetDashboardStatsHandler.class, cache = "5m")
    GetDashboardStatsHandler.Response getDashboardStats(@ModelAttribute GetDashboardStatsHandler.Query query);

    // System Config
    @GetMapping("/system-config")
    @Query(handler = GetSystemConfigHandler.class, cache = "1h")
    java.util.List<GetSystemConfigHandler.Response> getSystemConfig(@ModelAttribute GetSystemConfigHandler.Query query);

    @PutMapping("/system-config")
    @Command(handler = UpdateSystemConfigHandler.class)
    void updateSystemConfig(@RequestBody UpdateSystemConfigHandler.Command cmd);

    // Dashboard

}
