package com.chatme.config;

import com.google.genai.Client;
import com.google.genai.types.HttpOptions;
import org.springframework.ai.google.genai.GoogleGenAiEmbeddingConnectionDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Overrides the default GoogleGenAiEmbeddingConnectionDetails to use API version "v1"
 * instead of the default "v1beta", because text-embedding-004 is only available in v1.
 */
@Configuration
public class GoogleGenAiEmbeddingConfig {

    @Value("${spring.ai.google.genai.embedding.api-key:${spring.ai.google.genai.api-key:}}")
    private String apiKey;

    @Bean
    @Primary
    public GoogleGenAiEmbeddingConnectionDetails googleGenAiEmbeddingConnectionDetails() {
        Client client = Client.builder()
                .apiKey(apiKey)
                .httpOptions(HttpOptions.builder().apiVersion("v1").build())
                .build();

        return GoogleGenAiEmbeddingConnectionDetails.builder()
                .genAiClient(client)
                .build();
    }
}
