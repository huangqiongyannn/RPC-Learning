package com.hqy.register.impl;

import com.hqy.api.OrderService;
import com.hqy.register.ServiceRegister;

import java.util.*;

public class TestServiceRegister implements ServiceRegister {
    private static final Map<String, List<String>> registry = new HashMap<>();

    public TestServiceRegister() {
        List<String> list = new ArrayList<>();
        list.add("127.0.01:8888");
        registry.put(OrderService.class.getName(), list);
    }

    @Override
    public void registry(String serviceName, String serviceAddress) {
//        registry.computeIfAbsent(serviceName, k -> new ArrayList<>()).add(serviceAddress);
//        System.out.println("服务注册成功！服务名为：" + serviceName + " 地址：" + serviceAddress);
    }

    @Override
    public List<String> lookup(String serviceName) {
        return registry.getOrDefault(serviceName, Collections.emptyList());
//        List<String> list = new ArrayList<>();
//        list.add("127.0.0.1:8888");
//        return list;
    }
}
