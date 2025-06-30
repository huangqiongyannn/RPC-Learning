package com.hqy.transport.Netty;

import com.hqy.entity.RpcRequest;
import com.hqy.entity.RpcResponse;
import com.hqy.transport.api.RpcClient;

import java.io.IOException;
import java.net.InetSocketAddress;

public class NettyRpcClient implements RpcClient {
    @Override
    public RpcResponse sendRequest(RpcRequest request, InetSocketAddress address) throws IOException {
        return null;
    }
}
