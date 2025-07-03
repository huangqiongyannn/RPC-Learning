package com.hqy.limiter.provider;

import com.hqy.limiter.RateLimiter;
import com.hqy.limiter.impl.TokenBucketRateLimiter;

import java.util.HashMap;
import java.util.Map;

public class TokenBucketRateLimiterProvider {
    private static final Map<String, RateLimiter> map = new HashMap<>();

    public static RateLimiter getRateLimiter(String interfaceName) {
        if (map.containsKey(interfaceName)) {
            return map.get(interfaceName);
        } else {
            TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(50, 10);
            map.put(interfaceName, limiter);
            return limiter;
        }
    }
}
