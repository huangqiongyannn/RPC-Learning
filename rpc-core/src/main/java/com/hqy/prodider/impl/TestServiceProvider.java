package com.hqy.prodider.impl;

import com.hqy.api.OrderService;
import com.hqy.prodider.ServiceProvider;
import com.hqy.register.ServiceRegister;
import com.hqy.register.impl.TestServiceRegister;

import java.util.HashMap;
import java.util.Map;

public class TestServiceProvider implements ServiceProvider {
    private final static Map<String, Object> serviceMap = new HashMap<>();

    @Override
    public void addService(String serviceName, Object service) {
        if (!serviceMap.containsKey(serviceName)) {
            serviceMap.put(serviceName, service);
        }
    }

    @Override
    public Object getService(String serviceName) {
        System.out.println(serviceName);
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RuntimeException("找不到服务：" + serviceName);
        }
        return service;
    }
}
