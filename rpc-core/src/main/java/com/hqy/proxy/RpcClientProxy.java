package com.hqy.proxy;

import com.hqy.config.RpcClientConfig;
import com.hqy.entity.RpcRequest;
import com.hqy.entity.RpcResponse;
import com.hqy.enumeration.LoadBalanceType;
import com.hqy.loadBalance.LoadBalancer;
import com.hqy.loadBalance.LoadBalancerFactory;
import com.hqy.loadBalance.impl.ConsistentHashLoadBalancer;
import com.hqy.register.ServiceRegister;
import com.hqy.register.cache.ZKServiceCacheManager;
import com.hqy.register.impl.ZKServiceRegister;
import com.hqy.transport.api.RpcClient;
import com.hqy.utils.TimeUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class RpcClientProxy implements InvocationHandler {

    private RpcClient rpcClient;
    private Class<?> targetInterface;
    private ServiceRegister register = ZKServiceRegister.getInstance();
    private ZKServiceCacheManager cacheManager = ZKServiceCacheManager.getInstance();

    public RpcClientProxy(RpcClient rpcClient, Class<?> targetInterface) {
        this.rpcClient = rpcClient;
        this.targetInterface = targetInterface;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(
                targetInterface.getClassLoader(),
                new Class[]{targetInterface},
                this
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RpcRequest request = new RpcRequest();
        request.setClassName(targetInterface.getName());
        request.setMethodName(method.getName());
        request.setReturnClass(method.getReturnType());
        request.setFieldClasses(method.getParameterTypes());
// request.setFieldNames(getParameterNames(method));  // 可选
        request.setFieldValues(args);

        // 服务发现
        String serviceName = targetInterface.getName();
        List<String> serviceAddresses = cacheManager.getService(serviceName);
        if (serviceAddresses == null || serviceAddresses.size() == 0) {
            throw new RuntimeException("服务发现失败！服务：" + targetInterface.getName());
        }

        // 负载均衡
        LoadBalanceType type = RpcClientConfig.getInstance().getLoadBalanceType();

        String selected = null;
        if (type == LoadBalanceType.CONSISTENT_HASH) {
            ConsistentHashLoadBalancer loadBalancer = (ConsistentHashLoadBalancer) LoadBalancerFactory.getLoadBalancer(LoadBalanceType.CONSISTENT_HASH);
            selected = loadBalancer.select(buildHashKey(method, args), serviceName, serviceAddresses);
        } else {
            LoadBalancer loadBalancer = LoadBalancerFactory.getLoadBalancer(type);
            selected = loadBalancer.select(serviceName, serviceAddresses);
        }


        String[] address = selected.split(":");
        InetSocketAddress socketAddress = new InetSocketAddress(address[0], Integer.parseInt(address[1]));
        System.out.println("rpc请求已发送出去，开始时间；" + TimeUtil.getCurrentTime());
        RpcResponse response = rpcClient.sendRequest(request, socketAddress);
        if (response.isSuccess()) {
            System.out.println("rpc请求已处理完毕返回，结束时间；" + TimeUtil.getCurrentTime());
            return response.getData();
        } else {
            throw new RuntimeException("RPC 调用失败: " + response.getErrorMessage());
        }

    }

    private String buildHashKey(Method method, Object[] args) {
        // 最简单策略：方法名 + 第一个参数的hash（如果有）
        StringBuilder sb = new StringBuilder(method.getName());
        if (args != null && args.length > 0 && args[0] != null) {
            sb.append("#").append(args[0].toString());
        }
        return sb.toString();
    }

}
