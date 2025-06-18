package com.hqy.api;

public interface OrderService {
    String createOrder(String userId, String productId);
    String getOrderStatus(String orderId);
}