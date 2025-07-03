package com.hqy.loadBalancer.impl;

import com.hqy.loadBalancer.LoadBalancer;

import java.util.List;

public class LRULoadBalancer implements LoadBalancer {
    @Override
    public String select(String serviceName, List<String> serviceAddresses) {
        return null;
    }
}
