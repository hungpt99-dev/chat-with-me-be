package com.chatme.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${app.github.token}")
    private String githubToken;

    @Value("${app.supabase.url}")
    private String supabaseUrl;

    @Value("${app.supabase.anon-key}")
    private String supabaseAnonKey;

    @Bean("githubRestClient")
    public RestClient githubRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.github.com")
                .defaultHeader("Authorization", "Bearer " + githubToken)
                .defaultHeader("Accept", "application/vnd.github.v3+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();
    }

    @Bean("supabaseRestClient")
    public RestClient supabaseRestClient() {
        return RestClient.builder()
                .baseUrl(supabaseUrl)
                .defaultHeader("apikey", supabaseAnonKey)
                .defaultHeader("Authorization", "Bearer " + supabaseAnonKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}

