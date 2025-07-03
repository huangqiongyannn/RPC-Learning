package com.hqy.test;

import com.hqy.config.RpcServerConfig;
import com.hqy.provider.service.impl.ZKServiceProvider;
import com.hqy.enumeration.SerializerType;
import com.hqy.service.OrderServiceImpl;
import com.hqy.transport.Netty.NettyRpcServer;
import com.hqy.transport.api.RpcServer;


public class ServerTest {
    public static void main(String[] args) {
        RpcServerConfig.getInstance().setSerializerType(SerializerType.HESSION);
        // 注册服务
        ZKServiceProvider.getInstance().addService(new OrderServiceImpl());

//        RpcServer server = new SocketRpcServer();
        RpcServer server = new NettyRpcServer();
        // 启动服务
        server.start();
    }
}
