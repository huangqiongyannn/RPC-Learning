package com.hqy.config;

import com.hqy.serialize.SerializerType;
import lombok.Data;

@Data
public class RpcServerConfig {
    private static final RpcServerConfig INSTANCE = new RpcServerConfig();

    private String applicationName;
    private int port = 8888;
    private String host = "127.0.0.1";
    private SerializerType serializerType = SerializerType.JAVA;

    private RpcServerConfig() {}

    public static RpcServerConfig getInstance() {
        return INSTANCE;
    }

    public void init(String appName, String host, int port, SerializerType serializerType) {
        this.applicationName = appName;
        this.host = host;
        this.port = port;
        this.serializerType = serializerType;
    }
}
