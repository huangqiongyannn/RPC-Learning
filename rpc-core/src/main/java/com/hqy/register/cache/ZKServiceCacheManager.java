package com.hqy.register.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.hqy.register.impl.ZKServiceRegister;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ZKServiceCacheManager {
    private final static ZKServiceCacheManager INSTANCE = new ZKServiceCacheManager();
    private final ZKServiceRegister zkClient = ZKServiceRegister.getInstance();

    private final LoadingCache<String, List<String>> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .maximumSize(100)
            .removalListener((RemovalListener<String, List<String>>) notification -> {
                System.out.println("[Guava] 缓存移除：" + notification.getKey());
            })
            .build(new CacheLoader<String, List<String>>() {
                @Override
                public List<String> load(String serviceName){
                    List<String> nodes = zkClient.lookup(serviceName);
                    zkClient.subscribeWatcher(serviceName, cache);
                    return nodes;
                }
            });


    public static ZKServiceCacheManager getInstance() {
        return INSTANCE;
    }

    public List<String> getService(String serviceName) {
        try {
            return cache.get(serviceName);
        } catch (Exception e) {
            throw new RuntimeException("获取服务失败：" + serviceName, e);
        }
    }
}
