package com.chatme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Query for fetching GitHub repositories.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetGitHubReposQuery() {}
