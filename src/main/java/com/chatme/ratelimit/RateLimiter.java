package com.chatme.ratelimit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token-bucket rate limiter keyed by client identifier (e.g., IP address).
 * <p>
 * Each bucket starts with {@code maxTokens} and refills at
 * {@code refillPerSecond} tokens per second.
 * Thread-safe via synchronized bucket access.
 */
public class RateLimiter {

    private final int maxTokens;
    private final double refillPerSecond;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public RateLimiter(int maxTokens, double refillPerSecond) {
        this.maxTokens = maxTokens;
        this.refillPerSecond = refillPerSecond;
    }

    /**
     * Try to consume one token for the given key.
     *
     * @param key client identifier (IP, user ID, etc.)
     * @return true if the request is allowed, false if rate-limited
     */
    public boolean tryConsume(String key) {
        Bucket bucket = buckets.computeIfAbsent(key, k -> new Bucket(maxTokens));
        return bucket.tryConsume(refillPerSecond, maxTokens);
    }

    private static class Bucket {
        private double tokens;
        private long lastRefillNanos;

        Bucket(int maxTokens) {
            this.tokens = maxTokens;
            this.lastRefillNanos = System.nanoTime();
        }

        synchronized boolean tryConsume(double refillPerSecond, int maxTokens) {
            long now = System.nanoTime();
            double elapsed = (now - lastRefillNanos) / 1_000_000_000.0;
            tokens = Math.min(maxTokens, tokens + elapsed * refillPerSecond);
            lastRefillNanos = now;

            if (tokens >= 1.0) {
                tokens -= 1.0;
                return true;
            }
            return false;
        }
    }
}
