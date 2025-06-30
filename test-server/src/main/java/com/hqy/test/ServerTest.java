package com.hqy.test;

import com.hqy.api.OrderService;
import com.hqy.config.RpcServerConfig;
import com.hqy.provider.impl.LocalServiceProvider;
import com.hqy.service.OrderServiceImpl;
import com.hqy.transport.api.RpcServer;
import com.hqy.transport.socket.SocketRpcServer;


public class ServerTest {
    public static void main(String[] args) {
        // 注册服务
        LocalServiceProvider.getInstance().addService(new OrderServiceImpl());

        RpcServer server = new SocketRpcServer();
        // 启动服务
        server.start();
    }
}
