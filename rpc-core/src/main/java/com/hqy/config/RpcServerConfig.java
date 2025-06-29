package com.hqy.config;

import com.hqy.serialize.SerializerType;
import lombok.Data;

@Data
public class RpcServerConfig {
    private String applicationName;
    private int port = 8888;
    private String host = "127.0.0.1";
    private SerializerType serializerType = SerializerType.JAVA;
}
