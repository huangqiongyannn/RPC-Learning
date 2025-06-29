package com.hqy.test;

import com.hqy.api.OrderService;
import com.hqy.config.RpcServerConfig;
import com.hqy.service.OrderServiceImpl;
import com.hqy.transport.api.RpcServer;
import com.hqy.transport.socket.SocketRpcServer;


public class ServerTest {
    public static void main(String[] args) {
        RpcServerConfig config = new RpcServerConfig();
        RpcServer server = new SocketRpcServer(config);

        // 服务注册
        server.publicService(new OrderServiceImpl(), OrderService.class.getName());
//        ServiceRegister register = new LocalServiceRegister();
//        System.out.println(OrderService.class.getName());
//        System.out.println(register.lookup(OrderService.class.getName()));
        // 启动服务
        server.start();
    }
}
