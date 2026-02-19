package com.chatme.controller;


import com.chatme.handler.github.GetGitHubInsightsHandler;
import com.chatme.handler.github.GetGitHubReposHandler;
import com.chatme.handler.github.GetGitHubUserHandler;
import com.fast.cqrs.cqrs.annotation.HttpController;
import com.fast.cqrs.cqrs.annotation.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@HttpController
@RequestMapping("/api/github")
public interface GitHubController {

    @GetMapping("/insights")
    @Query(handler = GetGitHubInsightsHandler.class, cache = "5m")
    GetGitHubInsightsHandler.Response getInsights(@ModelAttribute GetGitHubInsightsHandler.Query query);

    @GetMapping("/user")
    @Query(handler = GetGitHubUserHandler.class, cache = "5m")
    GetGitHubUserHandler.Response getUser(@ModelAttribute GetGitHubUserHandler.Request query);

    @GetMapping("/repos")
    @Query(handler = GetGitHubReposHandler.class, cache = "10m")
    java.util.List<GetGitHubReposHandler.Repo> getRepos(@ModelAttribute GetGitHubReposHandler.Query query);
}
