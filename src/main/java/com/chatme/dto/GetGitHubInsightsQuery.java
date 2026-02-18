package com.chatme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Query for fetching GitHub insights. No parameters needed — username comes from config.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GetGitHubInsightsQuery() {}
