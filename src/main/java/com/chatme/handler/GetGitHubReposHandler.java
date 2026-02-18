package com.chatme.handler;

import com.chatme.dto.GetGitHubReposQuery;
import com.chatme.dto.GitHubRepoDto;
import com.fast.cqrs.cqrs.QueryHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
public class GetGitHubReposHandler implements QueryHandler<GetGitHubReposQuery, List<GitHubRepoDto>> {

    private static final Logger log = LoggerFactory.getLogger(GetGitHubReposHandler.class);
    private static final long CACHE_DURATION_MS = 10 * 60 * 1000; // 10 minutes cache

    private final RestClient githubRestClient;
    private final ObjectMapper objectMapper;

    @Value("${app.github.username}")
    private String username;

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    private record CacheEntry(List<GitHubRepoDto> data, long timestamp) {
        boolean isValid() {
            return System.currentTimeMillis() - timestamp < CACHE_DURATION_MS;
        }
    }

    public GetGitHubReposHandler(
            @Qualifier("githubRestClient") RestClient githubRestClient,
            ObjectMapper objectMapper) {
        this.githubRestClient = githubRestClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<GitHubRepoDto> handle(GetGitHubReposQuery query) {
        String cacheKey = "repos:" + username;
        CacheEntry cached = cache.get(cacheKey);
        if (cached != null && cached.isValid()) {
            return cached.data();
        }

        try {
            String response = githubRestClient.get()
                    .uri("/users/{username}/repos?sort=updated&per_page=100&type=owner", username)
                    .retrieve()
                    .body(String.class);

            List<GitHubRepoDto> repos = objectMapper.readValue(response, new TypeReference<List<GitHubRepoDto>>() {});
            
            // Filter out forks if needed, or keep them. 
            // The frontend displays all, so we return all for now.
            
            cache.put(cacheKey, new CacheEntry(repos, System.currentTimeMillis()));
            return repos;

        } catch (Exception e) {
            log.error("Error fetching GitHub repos", e);
            throw new RuntimeException("Failed to fetch GitHub repos", e);
        }
    }
}
