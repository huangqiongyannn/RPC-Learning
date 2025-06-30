package com.hqy.register.impl;

import com.hqy.register.ServiceRegister;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;

public class ZKServiceRegister implements ServiceRegister {
    private static final ZKServiceRegister INSTANCE = new ZKServiceRegister();
    private static final String ZK_ADDRESS = "127.0.0.1:2181";
    private static final String ROOT_PATH = "my-rpc-service"; // 当前服务在zk中的统一命名空间

    private CuratorFramework client;

    private ZKServiceRegister() {
        client = CuratorFrameworkFactory.builder()
                .connectString(ZK_ADDRESS)
                .sessionTimeoutMs(60000)
                .namespace(ROOT_PATH)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        System.out.println("Zookeeper连接成功！");
    }

    public static ZKServiceRegister getInstance() {
        return INSTANCE;
    }

    @Override
    public void registry(String serviceName, String serviceAddress) {
        try {
            // 1. 创建 serviceName 永久节点（如不存在）
            String servicePath = "/" + serviceName;
            if (client.checkExists().forPath(servicePath) == null) {
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(servicePath);
            }

            // 2. 创建 serviceAddress 临时节点（代表服务实例）
            String instancePath = servicePath + "/" + serviceAddress;
            if (client.checkExists().forPath(instancePath) == null) {
                client.create()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(instancePath);
                System.out.println(instancePath + " 注册成功！");
            } else {
                System.out.println("该服务地址已注册：" + instancePath);
            }

        } catch (Exception e) {
            System.out.println("服务注册失败！" + e.getMessage());
        }

    }

    @Override
    public List<String> lookup(String serviceName) {
        try {
            List<String> children = client.getChildren().forPath("/" + serviceName);
            return children;
        } catch (Exception e) {
            System.out.println("该服务不存在：" + serviceName);
            return null;
        }
    }
}
