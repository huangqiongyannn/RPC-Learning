package com.hqy.test;

import com.hqy.server.RpcServer;


public class ServerTest {
    public static void main(String[] args) {
        int port = 8888;
        RpcServer rpcServer = new RpcServer(port);
        rpcServer.start();
    }
}
