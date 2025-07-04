package com.hqy.controller;

import com.hqy.api.OrderService;
import com.hqy.spring.annotation.RpcReference;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Component
public class TestController {
    @RpcReference
    private OrderService orderService;

    public void test() throws IOException {
        String orderId = orderService.createOrder("huangqy", "13290");
//        System.out.println("订单ID：" + orderId);
//        String status = orderService.getOrderStatus(orderId);
//        System.out.println("订单状态：" + status);
    }
}
