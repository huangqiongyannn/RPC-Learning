package com.hqy.test;

import com.hqy.config.RpcServerConfig;
import com.hqy.transport.api.RpcServer;
import com.hqy.transport.socket.SocketRpcServer;


public class ServerTest {
    public static void main(String[] args) {
        RpcServerConfig config = new RpcServerConfig();
        RpcServer rpcServer = new SocketRpcServer(config);
        rpcServer.start();
    }
}
