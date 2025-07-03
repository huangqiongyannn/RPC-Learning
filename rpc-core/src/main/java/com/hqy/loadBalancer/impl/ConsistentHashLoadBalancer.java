package com.hqy.loadBalancer.impl;

import com.hqy.loadBalancer.LoadBalancer;
import com.hqy.register.impl.ZKServiceRegister;
import org.apache.curator.framework.recipes.cache.ChildData;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConsistentHashLoadBalancer implements LoadBalancer {

    private static final int VM_NODE_NUMS = 5;

    // 每个 serviceName 对应自己的哈希环
    private static final Map<String, SortedMap<Integer, String>> hashRings = new ConcurrentHashMap<>();

    // 每个 serviceName 的真实节点集合
    private static final Map<String, Set<String>> realNodesMap = new ConcurrentHashMap<>();

    // 初始化状态标识
    private static final Set<String> initializedServices = ConcurrentHashMap.newKeySet();

    private static ZKServiceRegister zkClient = ZKServiceRegister.getInstance();

    private void init(String serviceName, List<String> nodes) {
        if (initializedServices.contains(serviceName)) return;

        synchronized (getInternLock(serviceName)) {
            if (initializedServices.contains(serviceName)) return;

            SortedMap<Integer, String> map = new TreeMap<>();
            Set<String> realNodes = new HashSet<>();

            for (String node : nodes) {
                if (realNodes.contains(node)) continue;

                realNodes.add(node);
                for (int i = 0; i < VM_NODE_NUMS; i++) {
                    String vmNode = node + "#" + i;
                    map.put(getHash(vmNode), vmNode);
                }
            }

            hashRings.put(serviceName, map);
            realNodesMap.put(serviceName, realNodes);
            initializedServices.add(serviceName);
            zkClient.subscribeWatcherForCHLoadBalancer(serviceName);
            System.out.println("[CH] 初始化一致性哈希环完成：" + serviceName + " -> " + realNodes);
        }
    }

    @Override
    public String select(String serviceName, List<String> serviceAddresses) {
        init(serviceName, serviceAddresses);
        if (!hashRings.containsKey(serviceName)) {
            throw new IllegalStateException("一致性哈希环未初始化：" + serviceName);
        }
        return select(UUID.randomUUID().toString(), serviceName, serviceAddresses);
    }

    public String select(String requestKey, String serviceName, List<String> serviceAddresses) {
        init(serviceName, serviceAddresses);
        String vmNode = getTargetNode(serviceName, requestKey);
        System.out.println(requestKey + " -> " + vmNode.substring(0, vmNode.indexOf('#')));
        return vmNode.substring(0, vmNode.indexOf('#'));
    }

    public void addNode(String serviceName, ChildData node) {
//        System.out.println("======" + node.getPath());
        if (node.getPath() == serviceName) return;
        String fullPath = node.getPath();
        String path = fullPath.substring(fullPath.lastIndexOf('/') + 1);
        Set<String> realNodes = realNodesMap.computeIfAbsent(serviceName, k -> new HashSet<>());
        SortedMap<Integer, String> map = hashRings.computeIfAbsent(serviceName, k -> new TreeMap<>());

        if (realNodes.contains(path)) return;

        realNodes.add(path);
        for (int i = 0; i < VM_NODE_NUMS; i++) {
            String vmNode = path + "#" + i;
            map.put(getHash(vmNode), vmNode);
        }

        System.out.println("[CH] 节点加入一致性哈希：" + serviceName + " -> " + path);
    }

    public void delNode(String serviceName, ChildData node) {
        System.out.println("====");
        String fullPath = node.getPath();
        String path = fullPath.substring(fullPath.lastIndexOf('/'));

        Set<String> realNodes = realNodesMap.get(serviceName);
        SortedMap<Integer, String> map = hashRings.get(serviceName);

        if (realNodes == null || map == null) return;

        realNodes.remove(path);
        for (int i = 0; i < VM_NODE_NUMS; i++) {
            String vmNode = path + "#" + i;
            map.remove(getHash(vmNode));
        }

        System.out.println("[CH] 节点移除一致性哈希：" + serviceName + " -> " + path);
    }

    private String getTargetNode(String serviceName, String key) {
        SortedMap<Integer, String> map = hashRings.get(serviceName);
        if (map == null || map.isEmpty()) throw new IllegalStateException("哈希环为空：" + serviceName);

        int hash = getHash(key);
        SortedMap<Integer, String> tail = map.tailMap(hash);
        return tail.isEmpty() ? map.get(map.firstKey()) : tail.get(tail.firstKey());
    }

    /**
     * 获取字符串的 FNV1_32_HASH 值
     */
    private static int getHash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++)
            hash = (hash ^ str.charAt(i)) * p;
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return hash < 0 ? Math.abs(hash) : hash;
    }

    /**
     * 给每个 serviceName 生成唯一锁（避免全类锁）
     */
    private static final Map<String, Object> locks = new ConcurrentHashMap<>();
    private static Object getInternLock(String serviceName) {
        return locks.computeIfAbsent(serviceName, k -> new Object());
    }
}
