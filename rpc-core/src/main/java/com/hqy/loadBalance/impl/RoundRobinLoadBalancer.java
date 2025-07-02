package com.hqy.loadBalance.impl;

import com.hqy.loadBalance.LoadBalancer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class RoundRobinLoadBalancer implements LoadBalancer {

    private final static Map<String, AtomicInteger> counterMap = new HashMap<>();

    @Override
    public String select(String serviceName, List<String> serviceAddresses) {
        if (serviceAddresses == null || serviceAddresses.isEmpty()) {
            throw new IllegalArgumentException("服务地址列表为空，无法做负载均衡！");
        }
        // 为当前 serviceName 创建或取出它的计数器
        AtomicInteger counter = counterMap.computeIfAbsent(serviceName, key -> new AtomicInteger(0));
        // 获取当前计数值，并做原子递增
        int index = counter.getAndUpdate(prev -> {
            // 递增后如果达到 Integer.MAX_VALUE 则归零，防止溢出
            return (prev == Integer.MAX_VALUE ? 0 : prev + 1);
        });
        // 计算在地址列表中的索引
        int pos = Math.abs(index) % serviceAddresses.size();
        return serviceAddresses.get(pos);
    }
}

