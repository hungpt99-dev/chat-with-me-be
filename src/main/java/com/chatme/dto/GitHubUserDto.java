package com.chatme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GitHub user profile response.
 */
public record GitHubUserDto(
    String login,
    String name,
    @JsonProperty("avatar_url") String avatarUrl,
    @JsonProperty("public_repos") int publicRepos,
    int followers,
    int following,
    String bio
) {}
