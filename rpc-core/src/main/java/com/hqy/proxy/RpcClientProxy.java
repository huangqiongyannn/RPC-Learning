package com.hqy.proxy;

import com.hqy.entity.RpcRequest;
import com.hqy.entity.RpcResponse;
import com.hqy.register.ServiceRegister;
import com.hqy.register.impl.ZKServiceRegister;
import com.hqy.transport.api.RpcClient;
import com.hqy.utils.TimeUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;

public class RpcClientProxy implements InvocationHandler {

    private RpcClient rpcClient;
    private Class<?> targetInterface;
    private ServiceRegister register = ZKServiceRegister.getInstance();

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
        System.out.println("rpc请求已发送出去，开始时间；" + TimeUtil.getCurrentTime());
        RpcRequest request = RpcRequest.builder()
                .className(targetInterface.getName())
                .methodName(method.getName())
                .returnClass(method.getReturnType())
                .fieldClasses(method.getParameterTypes())
//                .fieldNames(getParameterNames(method))  // 可选
                .fieldValues(args)
                .build();
        // 服务发现
        List<String> lookup = register.lookup(targetInterface.getName());
        if (lookup == null || lookup.size() == 0) {
            throw new RuntimeException("服务发现失败！服务：" + targetInterface.getName());
        }
        String[] address = lookup.get(0).split(":");
        InetSocketAddress socketAddress = new InetSocketAddress(address[0], Integer.parseInt(address[1]));
        RpcResponse response = rpcClient.sendRequest(request, socketAddress);
        if (response.isSuccess()) {
            System.out.println("rpc请求已处理完毕返回，结束时间；" + TimeUtil.getCurrentTime());
            return response.getData();
        } else {
            throw new RuntimeException("RPC 调用失败: " + response.getErrorMessage());
        }

    }
}
