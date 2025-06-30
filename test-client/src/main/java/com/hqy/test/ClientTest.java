package com.hqy.test;

import com.hqy.api.OrderService;
import com.hqy.proxy.RpcClientProxy;
import com.hqy.transport.socket.SocketRpcClient;

import java.io.*;

public class ClientTest {
    public static void main(String[] args) throws IOException{
        // 创建客户端代理对象
        RpcClientProxy clientProxy = new RpcClientProxy(new SocketRpcClient(), OrderService.class);
        OrderService service = clientProxy.getProxy();
        String userId = "hqy";
        String orderId = "1234";
        String response = service.createOrder(userId, orderId);
        System.out.println("返回内容：" + response);
    }
}
