package com.hqy.test;

import com.hqy.serialize.SerializerType;
import com.hqy.transport.api.RpcServer;
import com.hqy.transport.socket.SocketRpcServer;


public class ServerTest {
    public static void main(String[] args) {
        int port = 8888;
        RpcServer rpcServer = new SocketRpcServer();
        rpcServer.start(port, SerializerType.JAVA);
    }
}
