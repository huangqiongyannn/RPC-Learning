package com.hqy.client;

import com.hqy.api.OrderService;
import com.hqy.entity.RpcRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientProxy implements OrderService {


    @Override
    public String createOrder(String userId, String productId) throws IOException {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName("OrderService");
        rpcRequest.setMethodName("createOrder");
        rpcRequest.setReturnClass(String.class);
        rpcRequest.setFieldClasses(new Class[]{String.class, String.class});
        rpcRequest.setFieldNames(new String[]{userId, productId});
        rpcRequest.setFieldValues(new Object[]{"hqy", "1234"});

        Socket socket = new Socket("127.0.0.1", 8888);
        Object response = null;
        try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
            oos.writeObject(rpcRequest);
            oos.flush();
            System.out.println("rpc请求发送成功！等待执行......");
            Thread.sleep(4000);
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            response = inputStream.readObject();
            System.out.println("成功执行，执行结果已返回！");
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
        return (String) response;
    }

    @Override
    public String getOrderStatus(String orderId) {
        return null;
    }
}
