package com.hqy.api;

import java.io.IOException;

public interface OrderService {
    String createOrder(String userId, String productId) throws IOException;
    String getOrderStatus(String orderId);
}