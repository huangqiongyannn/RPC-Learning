package com.hqy.transport.socket;

import com.hqy.config.RpcServerConfig;
import com.hqy.entity.RpcRequest;
import com.hqy.entity.RpcResponse;
import com.hqy.handler.socket.RequestHandler;
import com.hqy.serializer.SerializeFactory;
import com.hqy.serializer.Serializer;
import com.hqy.enumeration.SerializerType;
import com.hqy.transport.api.RpcServer;
import com.hqy.utils.TimeUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketRpcServer implements RpcServer {

    private RequestHandler handler = new RequestHandler();
    private int port;
    private SerializerType serializerType;
    private RpcServerConfig config;


    public SocketRpcServer() {
        this.config = RpcServerConfig.getInstance();
        this.port = config.getPort();
        this.serializerType = config.getSerializerType();
    }

    private void task(Socket clientSocket) {
        System.out.println("请求来自：" + clientSocket.getInetAddress().toString());
        System.out.println("线程开始执行时间：" + TimeUtil.getCurrentTime());
        try {

            InputStream is = clientSocket.getInputStream();
            OutputStream os = clientSocket.getOutputStream();
            DataInputStream dis = new DataInputStream(is);
            DataOutputStream dos = new DataOutputStream(os);

            // 读取请求
            int length = dis.readInt();
            byte[] requestBytes = new byte[length];
            dis.readFully(requestBytes);

            Serializer serializer = SerializeFactory.getSerializer(serializerType);

            // 反序列化
            RpcRequest request = serializer.deserialize(requestBytes, RpcRequest.class);

            // 方法执行
            RpcResponse response = handler.handle(request);

            // 序列化，返回执行结果
            byte[] reponseBytes = serializer.serialize(response);
            dos.writeInt(reponseBytes.length);
            dos.write(reponseBytes);
            dos.flush();

            // 返回执行结果
            System.out.println("线程执行结束时间：" + TimeUtil.getCurrentTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println(port + "监听端口已启动！");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.submit(()-> {
                    task(clientSocket);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
