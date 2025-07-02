package com.hqy.loadBalance.impl;

import com.hqy.loadBalance.LoadBalancer;

import java.util.List;

public class LRULoadBalancer implements LoadBalancer {
    @Override
    public String select(String serviceName, List<String> serviceAddresses) {
        return null;
    }
}
