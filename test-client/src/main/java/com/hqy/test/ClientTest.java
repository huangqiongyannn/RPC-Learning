package com.hqy.test;

import com.hqy.api.OrderService;
import com.hqy.config.RpcClientConfig;
import com.hqy.enumeration.LoadBalanceType;
import com.hqy.proxy.RpcClientProxy;
import com.hqy.enumeration.SerializerType;
import com.hqy.transport.Netty.NettyRpcClient;

import java.io.*;

public class ClientTest {
    public static void main(String[] args) throws IOException {
        RpcClientConfig.getInstance().setSerializerType(SerializerType.PROTOSTUFF);
        RpcClientConfig.getInstance().setLoadBalanceType(LoadBalanceType.CONSISTENT_HASH);
        // 创建客户端代理对象
        RpcClientProxy clientProxy = new RpcClientProxy(new NettyRpcClient(), OrderService.class);
        OrderService service = clientProxy.getProxy();
        String userId = "hqy";
        String orderId = "1234";
        String response = service.createOrder(userId, orderId);
        System.out.println("返回内容：" + response);

    }
}
