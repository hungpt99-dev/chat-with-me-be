package com.chatme.controller;

import com.chatme.handler.blog.*;
import com.fast.cqrs.cqrs.annotation.Command;
import com.fast.cqrs.cqrs.annotation.HttpController;
import com.fast.cqrs.cqrs.annotation.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@HttpController
@RequestMapping("/api/blog")
public interface BlogController {

    @GetMapping("/posts")
    @Query(handler = GetBlogPostsHandler.class, cache = "5m")
    List<GetBlogPostsHandler.Response> getPosts(@ModelAttribute GetBlogPostsHandler.Request query);

    @GetMapping("/posts/{id}")
    @Query(handler = GetBlogPostHandler.class, cache = "5m")
    GetBlogPostHandler.Response getPost(@ModelAttribute GetBlogPostHandler.Request query);

    @PostMapping("/posts/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    @Command(handler = AddCommentHandler.class)
    void addComment(@PathVariable("id") String postId, @RequestBody AddCommentHandler.Request cmd);

    @GetMapping("/posts/{id}/comments")
    @Query(handler = GetPostCommentsHandler.class)
    List<GetPostCommentsHandler.Response> getComments(@ModelAttribute GetPostCommentsHandler.Request query);
}
