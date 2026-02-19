package com.chatme.handler.github;

import com.fast.cqrs.cqrs.QueryHandler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handler for fetching GitHub repositories.
 */
@Component
public class GetGitHubReposHandler implements QueryHandler<GetGitHubReposHandler.Query, List<GetGitHubReposHandler.Repo>> {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Query() {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Repo(
        String name,
        @JsonProperty("full_name") String fullName,
        @JsonProperty("html_url") String htmlUrl,
        String description,
        @JsonProperty("stargazers_count") int stargazersCount,
        String language,
        @JsonProperty("updated_at") String updatedAt,
        String homepage,
        @JsonProperty("forks_count") int forksCount,
        boolean fork,
        @JsonProperty("topics") List<String> topics,
        @JsonProperty("pushed_at") String pushedAt
    ) {}

    private static final Logger log = LoggerFactory.getLogger(GetGitHubReposHandler.class);
    private static final long CACHE_DURATION_MS = 10 * 60 * 1000; // 10 minutes cache

    private final RestClient githubRestClient;
    
    @Value("${app.github.username}")
    private String username;

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    private record CacheEntry(List<Repo> data, long timestamp) {
        boolean isValid() {
            return System.currentTimeMillis() - timestamp < CACHE_DURATION_MS;
        }
    }

    public GetGitHubReposHandler(@Qualifier("githubRestClient") RestClient githubRestClient) {
        this.githubRestClient = githubRestClient;
    }

    @Override
    public List<Repo> handle(Query query) {
        String cacheKey = "repos:" + username;
        CacheEntry cached = cache.get(cacheKey);
        if (cached != null && cached.isValid()) {
            return cached.data();
        }

        try {
            List<Repo> repos = githubRestClient.get()
                    .uri("/users/{username}/repos?sort=updated&per_page=100&type=owner", username)
                    .retrieve()
                    .body(new org.springframework.core.ParameterizedTypeReference<>() {});

            if (repos == null) {
                repos = List.of();
            }
            
            cache.put(cacheKey, new CacheEntry(repos, System.currentTimeMillis()));
            return repos;

        } catch (Exception e) {
            log.error("Error fetching GitHub repos", e);
            throw new RuntimeException("Failed to fetch GitHub repos", e);
        }
    }
}
