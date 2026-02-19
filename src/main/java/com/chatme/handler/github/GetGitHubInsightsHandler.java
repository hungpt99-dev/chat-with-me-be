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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handler for getting GitHub insights (language stats and top repos).
 */
@Component
public class GetGitHubInsightsHandler implements QueryHandler<GetGitHubInsightsHandler.Query, GetGitHubInsightsHandler.Response> {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Query() {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(
        List<LanguageStat> languages,
        List<RepoStat> topRepos
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record LanguageStat(String name, int value) {}
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record RepoStat(String name, int stars) {}
    }

    private static final Logger log = LoggerFactory.getLogger(GetGitHubInsightsHandler.class);
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

    // Minimal record for fetching repos
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record GithubRepo(
        String name,
        String language,
        @JsonProperty("stargazers_count") int stargazersCount
    ) {}

    public GetGitHubInsightsHandler(@Qualifier("githubRestClient") RestClient githubRestClient) {
        this.githubRestClient = githubRestClient;
    }

    @Override
    public Response handle(Query query) {
        String cacheKey = "insights:" + username;
        CacheEntry cached = cache.get(cacheKey);
        if (cached != null && cached.isValid()) {
            return (Response) cached.data();
        }

        try {
            List<GithubRepo> repos = fetchRepos();

            // Compute language stats
            Map<String, Integer> languageMap = new LinkedHashMap<>();
            for (GithubRepo repo : repos) {
                if (repo.language() != null) {
                    languageMap.merge(repo.language(), 1, Integer::sum);
                }
            }

            List<Response.LanguageStat> languages = languageMap.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(7)
                    .map(e -> new Response.LanguageStat(e.getKey(), e.getValue()))
                    .toList();

            // Top repos by stars
            List<GithubRepo> sortedRepos = new ArrayList<>(repos);
            sortedRepos.sort((a, b) -> Integer.compare(b.stargazersCount(), a.stargazersCount()));

            List<Response.RepoStat> topRepos = new ArrayList<>();
            for (int i = 0; i < Math.min(5, sortedRepos.size()); i++) {
                GithubRepo repo = sortedRepos.get(i);
                topRepos.add(new Response.RepoStat(
                        repo.name(),
                        repo.stargazersCount()
                ));
            }

            Response result = new Response(languages, topRepos);
            cache.put(cacheKey, new CacheEntry(result, System.currentTimeMillis()));
            return result;

        } catch (Exception e) {
            log.error("Error fetching GitHub insights", e);
            throw new RuntimeException("Failed to fetch GitHub insights", e);
        }
    }

    private List<GithubRepo> fetchRepos() {
        List<GithubRepo> repos = githubRestClient.get()
                .uri("/users/{username}/repos?sort=updated&per_page=100", username)
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<>() {});
        
        return repos != null ? repos : List.of();
    }
}
