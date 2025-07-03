package com.hqy.loadBalancer;

import com.hqy.enumeration.LoadBalanceType;
import com.hqy.loadBalancer.impl.ConsistentHashLoadBalancer;
import com.hqy.loadBalancer.impl.RandomLoadBalancer;
import com.hqy.loadBalancer.impl.RoundRobinLoadBalancer;

import java.util.HashMap;
import java.util.Map;

public class LoadBalancerFactory {

    private final static Map<LoadBalanceType, LoadBalancer> map = new HashMap<>();

    static {
        map.put(LoadBalanceType.RANDOM, new RandomLoadBalancer());
        map.put(LoadBalanceType.ROUND_ROBIN, new RoundRobinLoadBalancer());
        map.put(LoadBalanceType.CONSISTENT_HASH, new ConsistentHashLoadBalancer());
    }

    public static LoadBalancer getLoadBalancer(LoadBalanceType type) {
        if (map.containsKey(type)) {
            return map.get(type);
        }
        throw new IllegalArgumentException("不支持该负载均衡类型：" + type.toString());
    }
}
