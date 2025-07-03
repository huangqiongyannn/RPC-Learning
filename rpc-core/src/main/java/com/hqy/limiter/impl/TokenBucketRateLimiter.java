package com.hqy.limiter.impl;

import com.hqy.limiter.RateLimiter;

public class TokenBucketRateLimiter implements RateLimiter {
    private int BUCKET_CAPACITY = 5;
    private volatile int currCapacity = 5;
    private int RATE = 2; // 令牌产生速率（单位是毫秒，即每 RATE 毫秒生成一个令牌）
    private volatile long lastTime = System.currentTimeMillis();

    public TokenBucketRateLimiter(int BUCKET_CAPACITY, int RATE) {
        this.BUCKET_CAPACITY = BUCKET_CAPACITY;
        this.RATE = RATE;
        this.currCapacity = BUCKET_CAPACITY;
    }

    @Override
    public synchronized boolean getToken() {
        if (currCapacity > 0) {
            currCapacity--;
            return true;
        }
        long currTime = System.currentTimeMillis();
        // 判断是否可以添加新令牌
        if (currTime - lastTime > RATE) {
            if ((currTime - lastTime) / RATE >= 2) {
                currCapacity = (int) Math.min(BUCKET_CAPACITY, currCapacity + (currTime - lastTime) / RATE - 1);
            }
            lastTime = currTime;
            return true;
        }

        return false;
    }
}
