package com.hqy.test;

import com.hqy.api.OrderService;
import com.hqy.entity.RpcRequest;

import java.io.*;
import java.net.Socket;

public class ClientTest {
    public static void main(String[] args) throws IOException {
        // 调用OrderService中createOrder方法，我怎么调用？
        // 1、自定义请求数据结构（请求哪个方法）
        // 2、通过Socket发送给服务器端
        // 3、服务器端接收到请求后，通过反射执行函数的调用，并将执行结果返回

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setClassName("OrderService");
        rpcRequest.setMethodName("createOrder");
        rpcRequest.setReturnClass(String.class);
        rpcRequest.setFieldClasses(new Class[]{String.class, String.class});
        rpcRequest.setFieldNames(new String[]{"userId", "productId"});
        rpcRequest.setFieldValues(new Object[]{"hqy", "1234"});

        Socket socket = new Socket("127.0.0.1", 8888);

        try (ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())) {
            outputStream.writeObject(rpcRequest);
            outputStream.flush();
            System.out.println("rpc请求发送成功！");
            Thread.sleep(2000);
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Object o = inputStream.readObject();
            System.out.println("方法执行成功！返回：" + o.toString());
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
