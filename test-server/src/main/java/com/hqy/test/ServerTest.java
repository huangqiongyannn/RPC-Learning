package com.hqy.test;

import com.hqy.config.RpcServerConfig;
import com.hqy.provider.service.impl.ZKServiceProvider;
import com.hqy.enumeration.SerializerType;
import com.hqy.service.OrderServiceImpl;
import com.hqy.transport.Netty.NettyRpcServer;
import com.hqy.transport.api.RpcServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;


@ComponentScan(basePackages = {"com.hqy"})
public class ServerTest {
    public static void main(String[] args) {
        // 自动启动服务注册
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ServerTest.class);

        // 配置信息
        RpcServerConfig.getInstance().setSerializerType(SerializerType.HESSION);

//        RpcServer server = new SocketRpcServer();
        RpcServer server = new NettyRpcServer();
        // 启动服务
        server.start();
    }
}
