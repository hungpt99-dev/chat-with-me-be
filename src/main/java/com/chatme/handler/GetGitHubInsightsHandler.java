package com.chatme.handler;

import com.chatme.dto.GetGitHubInsightsQuery;
import com.chatme.dto.GitHubInsightsDto;
import com.fast.cqrs.cqrs.QueryHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handler for getting GitHub insights (language stats and top repos).
 */
@Component
public class GetGitHubInsightsHandler implements QueryHandler<GetGitHubInsightsQuery, GitHubInsightsDto> {

    private static final Logger log = LoggerFactory.getLogger(GetGitHubInsightsHandler.class);
    private static final long CACHE_DURATION_MS = 5 * 60 * 1000; // 5 minutes

    private final RestClient githubRestClient;
    private final ObjectMapper objectMapper;

    @Value("${app.github.username}")
    private String username;

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    private record CacheEntry(Object data, long timestamp) {
        boolean isValid() {
            return System.currentTimeMillis() - timestamp < CACHE_DURATION_MS;
        }
    }

    public GetGitHubInsightsHandler(
            @Qualifier("githubRestClient") RestClient githubRestClient,
            ObjectMapper objectMapper) {
        this.githubRestClient = githubRestClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public GitHubInsightsDto handle(GetGitHubInsightsQuery query) {
        String cacheKey = "insights:" + username;
        CacheEntry cached = cache.get(cacheKey);
        if (cached != null && cached.isValid()) {
            return (GitHubInsightsDto) cached.data();
        }

        try {
            JsonNode repos = fetchRepos();

            // Compute language stats
            Map<String, Integer> languageMap = new LinkedHashMap<>();
            for (JsonNode repo : repos) {
                if (repo.has("language") && !repo.get("language").isNull()) {
                    String lang = repo.get("language").asText();
                    languageMap.merge(lang, 1, Integer::sum);
                }
            }

            List<GitHubInsightsDto.LanguageStat> languages = languageMap.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(7)
                    .map(e -> new GitHubInsightsDto.LanguageStat(e.getKey(), e.getValue()))
                    .toList();

            // Top repos by stars
            List<GitHubInsightsDto.RepoStat> topRepos = new ArrayList<>();
            List<JsonNode> repoList = new ArrayList<>();
            repos.forEach(repoList::add);
            repoList.sort((a, b) -> {
                int starsA = a.has("stargazers_count") ? a.get("stargazers_count").asInt() : 0;
                int starsB = b.has("stargazers_count") ? b.get("stargazers_count").asInt() : 0;
                return Integer.compare(starsB, starsA);
            });
            for (int i = 0; i < Math.min(5, repoList.size()); i++) {
                JsonNode repo = repoList.get(i);
                topRepos.add(new GitHubInsightsDto.RepoStat(
                        repo.get("name").asText(),
                        repo.has("stargazers_count") ? repo.get("stargazers_count").asInt() : 0
                ));
            }

            GitHubInsightsDto result = new GitHubInsightsDto(languages, topRepos);
            cache.put(cacheKey, new CacheEntry(result, System.currentTimeMillis()));
            return result;

        } catch (Exception e) {
            log.error("Error fetching GitHub insights", e);
            throw new RuntimeException("Failed to fetch GitHub insights", e);
        }
    }

    private JsonNode fetchRepos() throws Exception {
        String response = githubRestClient.get()
                .uri("/users/{username}/repos?sort=updated&per_page=100", username)
                .retrieve()
                .body(String.class);

        return objectMapper.readTree(response);
    }
}
