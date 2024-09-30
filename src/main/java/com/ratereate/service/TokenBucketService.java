package com.ratereate.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TokenBucketService {
    private final int capacity;
    private final int refillTokens;
    private final long refillInterval;
    private AtomicInteger tokens;
    private long lastRefillTimestamp;

    public TokenBucketService(
            @Value("${rate.limit.capacity:10}") int capacity,
            @Value("${rate.limit.refillTokens:1}") int refillTokens,
            @Value("${rate.limit.refillInterval:1000000000}") long refillInterval // default 1 second
    ) {
        this.capacity = capacity;
        this.refillTokens = refillTokens;
        this.refillInterval = refillInterval;
        this.tokens = new AtomicInteger(capacity);
        this.lastRefillTimestamp = System.nanoTime();
    }

    public boolean tryConsume() {
        refill();
        if (tokens.get() > 0) {
            tokens.decrementAndGet();
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.nanoTime();
        long elapsedTime = now - lastRefillTimestamp;

        if (elapsedTime > refillInterval) {
            int tokensToAdd = (int) (elapsedTime / refillInterval) * refillTokens;
            int newTokenCount = Math.min(capacity, tokens.get() + tokensToAdd);
            tokens.set(newTokenCount);
            lastRefillTimestamp = now;
        }
    }
}
