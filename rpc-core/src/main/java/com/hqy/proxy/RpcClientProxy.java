package com.hqy.proxy;

import com.hqy.entity.RpcRequest;
import com.hqy.entity.RpcResponse;
import com.hqy.transport.api.RpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

public class RpcClientProxy implements InvocationHandler {

    private RpcClient rpcClient;
    private Class<?> targetInterface;

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
        RpcRequest request = RpcRequest.builder()
                .className(targetInterface.getName())
                .methodName(method.getName())
                .returnClass(method.getReturnType())
                .fieldClasses(method.getParameterTypes())
//                .fieldNames(getParameterNames(method))  // 可选
                .fieldValues(args)
                .build();
        RpcResponse response = rpcClient.sendRequest(request);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw new RuntimeException("RPC 调用失败: " + response.getErrorMessage());
        }
    }
}
