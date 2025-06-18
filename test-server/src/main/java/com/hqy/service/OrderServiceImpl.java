package com.hqy.service;

import com.hqy.api.OrderService;

public class OrderServiceImpl implements OrderService {
    @Override
    public String createOrder(String userId, String productId) {
        return "用户ID：" + userId + "; 产品ID：" + productId;
    }

    @Override
    public String getOrderStatus(String orderId) {
        return "已完成！";
    }
}
