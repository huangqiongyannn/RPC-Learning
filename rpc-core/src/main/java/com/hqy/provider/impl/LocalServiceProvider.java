package com.hqy.provider.impl;

import com.hqy.config.RpcServerConfig;
import com.hqy.provider.ServiceProvider;
import com.hqy.register.ServiceRegister;
import com.hqy.register.impl.ZKServiceRegister;

import java.util.HashMap;
import java.util.Map;

public class LocalServiceProvider implements ServiceProvider {
    private static final LocalServiceProvider INSTANCE = new LocalServiceProvider();
    // 本地映射表
    private final static Map<String, Object> serviceMap = new HashMap<>();

    private ServiceRegister register = ZKServiceRegister.getInstance();
    private RpcServerConfig config = RpcServerConfig.getInstance();

    private LocalServiceProvider() {}

    public static LocalServiceProvider getInstance() {
        return INSTANCE;
    }

    @Override
    public void addService(Object service) {
        Class<?>[] interfaces = service.getClass().getInterfaces();
        for (Class<?> clazz: interfaces) {
            // 向本地映射表注册
            serviceMap.put(clazz.getName(), service);

            // 向注册中心中注册服务
            String serverAddress = config.getHost() + ":" + config.getPort();
            register.registry(clazz.getName(), serverAddress);
        }
    }

    @Override
    public Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RuntimeException("获取不到该服务：" + serviceName);
        }
        return service;
    }
}
