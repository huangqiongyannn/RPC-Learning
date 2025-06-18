package com.hqy.test;

import com.hqy.entity.RpcRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {
    public static void main(String[] args) {
        int port = 8888;
        try (ServerSocket socket = new ServerSocket(port) ) {
            System.out.println("服务器端启动，监听端口：" + port);
            while (true) {
                // 连接socket
                Socket clientSocket = socket.accept();
                System.out.println("客户端已连接，IP：" + clientSocket.getLocalAddress());
                ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                RpcRequest request = (RpcRequest)objectInputStream.readObject();
                System.out.println("收到的RPC请求：" + request.toString());
                // 解析RpcRequest, 并通过反射执行该方法
                Class<?> aClass = Class.forName("com.hqy.service." + request.getClassName() + "Impl");
                Object o = aClass.getConstructor().newInstance();
                Method method = aClass.getMethod(request.getMethodName(), request.getFieldClasses());
                Object result = method.invoke(o, request.getFieldValues());
                System.out.println("方法执行完毕！结果为：" + result.toString());
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.accept().getOutputStream());
                outputStream.writeObject(result);
                outputStream.flush();
                System.out.println("成功返回响应结果！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
