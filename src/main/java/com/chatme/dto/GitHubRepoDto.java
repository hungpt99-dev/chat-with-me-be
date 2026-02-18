package com.chatme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * GitHub repository details.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubRepoDto(
    String name,
    @JsonProperty("full_name") String fullName,
    @JsonProperty("html_url") String htmlUrl,
    String description,
    @JsonProperty("stargazers_count") int stargazersCount,
    String language,
    @JsonProperty("updated_at") String updatedAt,
    String homepage
) {}
