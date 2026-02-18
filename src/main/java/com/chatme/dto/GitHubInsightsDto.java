package com.chatme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * GitHub insights response: language stats and top repos.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubInsightsDto(
    List<LanguageStat> languages,
    List<RepoStat> topRepos
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record LanguageStat(String name, int value) {}
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RepoStat(String name, int stars) {}
}
