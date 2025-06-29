package com.hqy.proxy;

import com.hqy.api.OrderService;
import com.hqy.config.RpcClientConfig;
import com.hqy.entity.RpcRequest;
import com.hqy.entity.RpcResponse;
import com.hqy.serialize.SerializerType;
import com.hqy.transport.api.RpcClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Instant;

public class ClientProxy implements OrderService {

    private RpcClient rpcClient;

    public ClientProxy(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public String createOrder(String userId, String productId) throws IOException {
        RpcRequest rpcRequest = RpcRequest.builder()
                .className("OrderService")
                .methodName("createOrder")
                .returnClass(String.class)
                .fieldClasses(new Class[]{String.class, String.class})
                .fieldNames(new String[]{"userId", "productId"})
                .fieldValues(new Object[]{"hqy", "1234"})
                .build();
        RpcResponse response = rpcClient.sendRequest(rpcRequest);
        if (response.isSuccess()) {
            return (String) response.getData();
        } else {
            throw new RuntimeException(response.getErrorMessage());
        }
    }

    @Override
    public String getOrderStatus(String orderId) {
        return null;
    }
}
