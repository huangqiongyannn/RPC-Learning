package com.hqy.transport.api;

import com.hqy.entity.RpcRequest;
import com.hqy.entity.RpcResponse;
import com.hqy.serialize.SerializerType;

import java.io.IOException;
import java.net.InetSocketAddress;

public interface RpcClient {
    RpcResponse sendRequest(RpcRequest request, InetSocketAddress address) throws IOException;
}
