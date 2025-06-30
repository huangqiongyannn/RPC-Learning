package com.hqy.config;

import com.hqy.serialize.SerializerType;
import lombok.Data;

@Data
public class RpcClientConfig {
    private final static RpcClientConfig INSTANCE = new RpcClientConfig();

    private String applicationName;
    private int port = 8888;
    private String host = "127.0.0.1";
    private SerializerType serializerType = SerializerType.JAVA;

    private RpcClientConfig() {}

    public static RpcClientConfig getInstance() {
        return INSTANCE;
    }

    public void init(String appName, String host, int port, SerializerType serializerType) {
        this.applicationName = appName;
        this.host = host;
        this.port = port;
        this.serializerType = serializerType;
    }
}
