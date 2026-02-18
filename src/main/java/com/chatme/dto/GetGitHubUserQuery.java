package com.chatme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Query for fetching GitHub user profile. No parameters needed — username comes from config.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetGitHubUserQuery() {}
