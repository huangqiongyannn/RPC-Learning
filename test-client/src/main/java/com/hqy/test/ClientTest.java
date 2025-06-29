package com.hqy.test;

import com.hqy.api.OrderService;
import com.hqy.config.RpcClientConfig;
import com.hqy.proxy.RpcClientProxy;
import com.hqy.transport.socket.SocketRpcClient;

import java.io.*;
import java.lang.reflect.Proxy;

public class ClientTest {
    public static void main(String[] args) throws IOException{
        
        RpcClientConfig config = new RpcClientConfig();
        RpcClientProxy clientProxy = new RpcClientProxy(new SocketRpcClient(config), OrderService.class);
        OrderService service = clientProxy.getProxy();
        String userId = "hqy";
        String orderId = "1234";
        String response = service.createOrder(userId, orderId);
        System.out.println("返回内容：" + response);
    }
}
