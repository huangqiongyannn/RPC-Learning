package com.hqy.loadBalance.impl;

import com.hqy.loadBalance.LoadBalancer;

import java.util.List;

public class RoundRobinLoadBalancer implements LoadBalancer {
    private static int next;

    public RoundRobinLoadBalancer() {
        next = 0;
    }

    @Override
    public String select(List<String> serviceAddresses) {
        if (serviceAddresses == null || serviceAddresses.size() <= 0) {
            throw new IllegalArgumentException("服务地址列表为空，无法做负载均衡！");
        }
        String serviceAddress = serviceAddresses.get(next);
        next = (next + 1) % serviceAddresses.size();
        return serviceAddress;
    }
}
