package com.hqy.server;

import com.hqy.entity.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcServer {

    private int port;

    public RpcServer(int port) {
        this.port = port;
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatted = sdf.format(new Date());
        return formatted;
    }

    private void requestHanlder(Socket clientSocket) {
        System.out.println("线程开始执行时间：" + getCurrentTime());
        System.out.println("客户端已连接，IP：" + clientSocket.getLocalAddress());
        try {
            // 反序列
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            RpcRequest request = (RpcRequest)ois.readObject();
            System.out.println("收到的RPC请求：" + request.toString());
            // 解析RpcRequest, 并通过反射执行该方法
            // 硬编码，耦合度高
            Class<?> aClass = Class.forName("com.hqy.service." + request.getClassName() + "Impl");
            Object o = aClass.getConstructor().newInstance();
            Method method = aClass.getMethod(request.getMethodName(), request.getFieldClasses());
            Object result = method.invoke(o, request.getFieldValues());
            System.out.println("方法执行完毕！结果为：" + result.toString());
            Thread.sleep(5000);
            // 返回执行结果
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.writeObject(result);
            outputStream.flush();
            System.out.println("成功返回响应结果！结束时间：" + getCurrentTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port) ) {
            System.out.println("服务器端启动，监听端口：" + port);
            ExecutorService pool = Executors.newFixedThreadPool(10);
            while (true) {
                // 连接socket 当没有连接进来时，该方法会被阻塞
                Socket clientSocket = serverSocket.accept();
                pool.submit(() -> {
                    requestHanlder(clientSocket);
                });
//                requestHanlder(clientSocket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
