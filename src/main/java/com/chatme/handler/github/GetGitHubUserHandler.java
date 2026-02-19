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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handler for getting GitHub user profile.
 */
@Component
public class GetGitHubUserHandler implements QueryHandler<GetGitHubUserHandler.Request, GetGitHubUserHandler.Response> {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Request() {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        String login,
        String name,
        @JsonProperty("avatar_url") String avatarUrl,
        @JsonProperty("public_repos") int publicRepos,
        int followers,
        int following,
        String bio
    ) {}

    private static final Logger log = LoggerFactory.getLogger(GetGitHubUserHandler.class);
    private static final long CACHE_DURATION_MS = 10 * 60 * 1000; // 10 minutes

    private final RestClient githubRestClient;

    @Value("${app.github.username}")
    private String username;

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    private record CacheEntry(Object data, long timestamp) {
        boolean isValid() {
            return System.currentTimeMillis() - timestamp < CACHE_DURATION_MS;
        }
    }

    public GetGitHubUserHandler(@Qualifier("githubRestClient") RestClient githubRestClient) {
        this.githubRestClient = githubRestClient;
    }

    @Override
    public Response handle(Request query) {
        String cacheKey = "user:" + username;
        CacheEntry cached = cache.get(cacheKey);
        if (cached != null && cached.isValid()) {
            return (Response) cached.data();
        }

        try {
            Response user = githubRestClient.get()
                    .uri("/users/{username}", username)
                    .retrieve()
                    .body(Response.class);

            if (user != null) {
                cache.put(cacheKey, new CacheEntry(user, System.currentTimeMillis()));
            }
            return user;

        } catch (Exception e) {
            log.error("Error fetching GitHub user", e);
            throw new RuntimeException("Failed to fetch GitHub user", e);
        }
    }
}
