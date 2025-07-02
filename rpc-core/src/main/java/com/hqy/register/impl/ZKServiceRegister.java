package com.hqy.register.impl;

import com.google.common.cache.LoadingCache;
import com.hqy.register.ServiceRegister;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
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
            return client.getChildren().forPath("/" + serviceName);
        } catch (Exception e) {
            System.out.println("该服务不存在：" + serviceName);
            return null;
        }
    }


    public void subscribeWatcher(String serviceName, LoadingCache<String, List<String>> cache) {
        try {
            String path = "/" + serviceName;

            // 创建并启动 CuratorCache（监听整个子树）
            CuratorCache curatorCache = CuratorCache.build(client, path);

            // 创建监听器
            CuratorCacheListener listener = CuratorCacheListener.builder()
                    .forAll((type, oldNode, newNode) -> {
                        switch (type) {
                            case NODE_CREATED:
                            case NODE_CHANGED:
                            case NODE_DELETED:
                                List<String> updatedList = this.lookup(serviceName);
                                cache.put(serviceName, updatedList);
                                System.out.println("[ZK] 节点变化(" + type + ")，更新缓存：" + serviceName + " -> " + updatedList);
                                break;
                            default:
                                break;
                        }
                    })
                    .build();

            curatorCache.listenable().addListener(listener);
            curatorCache.start();

            System.out.println("[ZK] CuratorCache 监听启动：" + path);
        } catch (Exception e) {
            System.err.println("[ZK] CuratorCache 注册监听器失败：" + e.getMessage());
        }
    }
}
