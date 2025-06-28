package com.hqy.test;

import com.hqy.api.OrderService;
import com.hqy.client.ClientProxy;
import com.hqy.entity.RpcRequest;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ClientTest {
    public static void main(String[] args) throws IOException{
        // 调用OrderService中createOrder方法，我怎么调用？
        // 1、自定义请求数据结构（请求哪个方法）
        // 2、通过Socket发送给服务器端
        // 3、服务器端接收到请求后，通过反射执行函数的调用，并将执行结果返回

        ClientProxy clientProxy = new ClientProxy();
        String userId = "hqy";
        String orderId = "1234";
        String response = clientProxy.createOrder(userId, orderId);
        System.out.println("返回内容：" + response);
    }
}
