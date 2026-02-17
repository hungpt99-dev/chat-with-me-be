package com.chatme.ratelimit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Registers the rate-limit interceptor for AI-heavy endpoints.
 * <p>
 * Configuration via application.yml:
 * <pre>
 * app.rate-limit.max-tokens: 10
 * app.rate-limit.refill-per-second: 1
 * </pre>
 */
@Configuration
public class RateLimitConfig implements WebMvcConfigurer {

    @Value("${app.rate-limit.max-tokens:10}")
    private int maxTokens;

    @Value("${app.rate-limit.refill-per-second:1}")
    private double refillPerSecond;

    @Bean
    public RateLimiter rateLimiter() {
        return new RateLimiter(maxTokens, refillPerSecond);
    }

    @Bean
    public RateLimitInterceptor rateLimitInterceptor(RateLimiter rateLimiter) {
        return new RateLimitInterceptor(rateLimiter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor(rateLimiter()))
                .addPathPatterns("/api/chat/**", "/api/validate-stack/**");
    }
}
