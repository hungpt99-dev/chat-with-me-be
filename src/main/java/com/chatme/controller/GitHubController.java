package com.chatme.controller;

import com.chatme.dto.GetGitHubInsightsQuery;
import com.chatme.dto.GetGitHubUserQuery;
import com.chatme.dto.GitHubInsightsDto;
import com.chatme.dto.GitHubUserDto;
import com.chatme.handler.GetGitHubInsightsHandler;
import com.chatme.handler.GetGitHubReposHandler;
import com.chatme.handler.GetGitHubUserHandler;
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
    GitHubInsightsDto getInsights(@ModelAttribute GetGitHubInsightsQuery query);

    @GetMapping("/user")
    @Query(handler = GetGitHubUserHandler.class, cache = "5m")
    GitHubUserDto getUser(@ModelAttribute GetGitHubUserQuery query);

    @GetMapping("/repos")
    @Query(handler = GetGitHubReposHandler.class, cache = "10m")
    java.util.List<com.chatme.dto.GitHubRepoDto> getRepos(@ModelAttribute com.chatme.dto.GetGitHubReposQuery query);
}
