package com.hqy.proxy;

import com.hqy.config.RpcClientConfig;
import com.hqy.entity.RpcRequest;
import com.hqy.entity.RpcResponse;
import com.hqy.enumeration.LoadBalanceType;
import com.hqy.loadBalance.LoadBalancerFactory;
import com.hqy.register.ServiceRegister;
import com.hqy.register.cache.ZKServiceCacheManager;
import com.hqy.register.impl.ZKServiceRegister;
import com.hqy.transport.api.RpcClient;
import com.hqy.utils.TimeUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.List;

public class RpcClientProxy implements InvocationHandler {

    private RpcClient rpcClient;
    private Class<?> targetInterface;
//    private ServiceRegister register = ZKServiceRegister.getInstance();
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
//        List<String> serviceAddresses = register.lookup(targetInterface.getName());
        List<String> serviceAddresses = cacheManager.getService(targetInterface.getName());
        if (serviceAddresses == null || serviceAddresses.size() == 0) {
            throw new RuntimeException("服务发现失败！服务：" + targetInterface.getName());
        }

        // 负载均衡
        LoadBalanceType type = RpcClientConfig.getInstance().getLoadBalanceType();
        String select = LoadBalancerFactory.getLoadBalancer(type).select(serviceAddresses);

        String[] address = select.split(":");
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
}
