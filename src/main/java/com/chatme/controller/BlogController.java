package com.chatme.controller;

import com.chatme.dto.BlogPostDto;
import com.chatme.dto.GetBlogPostQuery;
import com.chatme.dto.GetBlogPostsQuery;
import com.chatme.handler.GetBlogPostHandler;
import com.chatme.handler.GetBlogPostsHandler;
import com.fast.cqrs.cqrs.annotation.HttpController;
import com.fast.cqrs.cqrs.annotation.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@HttpController
@RequestMapping("/api/blog")
public interface BlogController {

    @GetMapping("/posts")
    @Query(handler = GetBlogPostsHandler.class, cache = "5m")
    List<BlogPostDto> getPosts(@ModelAttribute GetBlogPostsQuery query);

    @GetMapping("/posts/{id}")
    @Query(handler = GetBlogPostHandler.class, cache = "5m")
    BlogPostDto getPost(@ModelAttribute GetBlogPostQuery query);
}
