package com.hqy.loadBalance.impl;

import com.hqy.loadBalance.LoadBalancer;

import java.util.List;
import java.util.Random;

public class RandomLoadBalancer implements LoadBalancer {
    private final Random random = new Random();

    @Override
    public String select(String serviceName, List<String> serviceAddresses) {
        if (serviceAddresses == null || serviceAddresses.size() <= 0) {
            throw new IllegalArgumentException("服务地址列表为空，无法做负载均衡！");
        }
        int k = random.nextInt(serviceAddresses.size());

        return serviceAddresses.get(k);
    }
}
