package com.hqy.test;

import com.hqy.api.OrderService;
import com.hqy.config.RpcClientConfig;
import com.hqy.controller.TestController;
import com.hqy.enumeration.LoadBalanceType;
import com.hqy.proxy.RpcClientProxy;
import com.hqy.enumeration.SerializerType;
import com.hqy.transport.Netty.NettyRpcClient;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.*;

@ComponentScan(basePackages = {"com.hqy"})
public class ClientTest {
    public static void main(String[] args) throws IOException {
        // 配置信息
        RpcClientConfig.getInstance().setSerializerType(SerializerType.PROTOSTUFF);
        RpcClientConfig.getInstance().setLoadBalanceType(LoadBalanceType.CONSISTENT_HASH);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ClientTest.class);


        TestController controller = (TestController)context.getBean("testController");
        controller.test();


//        // 创建客户端代理对象
//        RpcClientProxy clientProxy = new RpcClientProxy(new NettyRpcClient(), OrderService.class);
//        OrderService service = clientProxy.getProxy();
//        String userId = "hqy";
//        String orderId = "1234";
//        String response = service.createOrder(userId, orderId);
//        System.out.println("返回内容：" + response);

    }
}
