package com.hqy.test;

import com.hqy.config.RpcClientConfig;
import com.hqy.proxy.ClientProxy;
import com.hqy.transport.socket.SocketRpcClient;

import java.io.*;

public class ClientTest {
    public static void main(String[] args) throws IOException{

        RpcClientConfig config = new RpcClientConfig();
        ClientProxy clientProxy = new ClientProxy(new SocketRpcClient(), config);
        String userId = "hqy";
        String orderId = "1234";
        String response = clientProxy.createOrder(userId, orderId);
        System.out.println("返回内容：" + response);
    }
}
