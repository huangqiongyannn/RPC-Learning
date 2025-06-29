package com.hqy.transport.api;

import com.hqy.entity.RpcRequest;
import com.hqy.entity.RpcResponse;
import com.hqy.serialize.SerializerType;

import java.io.IOException;

public interface RpcClient {
    RpcResponse sendRequest(int port, String ip, RpcRequest request, SerializerType type) throws IOException;
}
