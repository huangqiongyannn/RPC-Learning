package com.hqy.transport.socket;

import com.hqy.entity.RpcRequest;
import com.hqy.entity.RpcResponse;
import com.hqy.serialize.SerializeFactory;
import com.hqy.serialize.Serializer;
import com.hqy.serialize.SerializerType;
import com.hqy.transport.api.RpcClient;
import com.hqy.utils.TimeUtil;

import java.io.*;
import java.net.Socket;
import java.time.Instant;

public class SocketRpcClient implements RpcClient {
    @Override
    public RpcResponse sendRequest(int port, String host, RpcRequest request, SerializerType serializerType) throws IOException {

        System.out.println("请求已发送出去，开始时间为: " + TimeUtil.getCurrentTime());
        Socket socket = new Socket(host, port);

        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        DataInputStream dis = new DataInputStream(is);

        // 序列化发送
        Serializer serializer = SerializeFactory.getSerializer(serializerType);
        byte[] requestBytes = serializer.serialize(request);
        dos.writeInt(requestBytes.length);
        dos.write(requestBytes);
        dos.flush();

        // 拿到返回的结果，进行反序列化
        int length = dis.readInt();
        byte[] reponseBytes = new byte[length];
        dis.readFully(reponseBytes);
        RpcResponse response = serializer.deserialize(reponseBytes, RpcResponse.class);
        System.out.println("已经成功返回结果，结束时间为：" + TimeUtil.getCurrentTime());
        return response;
    }
}
