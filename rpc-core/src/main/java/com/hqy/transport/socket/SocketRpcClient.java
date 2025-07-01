package com.hqy.transport.socket;

import com.hqy.config.RpcClientConfig;
import com.hqy.entity.RpcRequest;
import com.hqy.entity.RpcResponse;
import com.hqy.serialize.SerializeFactory;
import com.hqy.serialize.Serializer;
import com.hqy.enumeration.SerializerType;
import com.hqy.transport.api.RpcClient;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketRpcClient implements RpcClient {

    private SerializerType serializerType;
    private RpcClientConfig config = RpcClientConfig.getInstance();

    public SocketRpcClient() {
        this.serializerType = this.config.getSerializerType();
    }

    @Override
    public RpcResponse sendRequest(RpcRequest request, InetSocketAddress address) throws IOException {

        Socket socket = new Socket(address.getHostName(), address.getPort());

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
        return response;
    }
}
