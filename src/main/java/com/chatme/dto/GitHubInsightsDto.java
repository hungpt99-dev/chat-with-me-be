package com.chatme.dto;

import java.util.List;

/**
 * GitHub insights response: language stats and top repos.
 */
public record GitHubInsightsDto(
    List<LanguageStat> languages,
    List<RepoStat> topRepos
) {
    public record LanguageStat(String name, int value) {}
    public record RepoStat(String name, int stars) {}
}
