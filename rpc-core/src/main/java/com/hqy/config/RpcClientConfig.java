package com.hqy.config;

import com.hqy.enumeration.LoadBalanceType;
import com.hqy.enumeration.SerializerType;
import lombok.Data;

@Data
public class RpcClientConfig {
    private final static RpcClientConfig INSTANCE = new RpcClientConfig();

    private String applicationName;
    private int port = 8888;
    private String host = "127.0.0.1";
    private SerializerType serializerType = SerializerType.JAVA;
    private LoadBalanceType loadBalanceType = LoadBalanceType.ROUND_ROBIN;

    private RpcClientConfig() {}

    public static RpcClientConfig getInstance() {
        return INSTANCE;
    }

    @Override
    public String toString() {
        return "RpcClientConfig{" +
                "applicationName='" + applicationName + '\'' +
                ", port=" + port +
                ", host='" + host + '\'' +
                ", serializerType=" + serializerType +
                ", loadBalanceType=" + loadBalanceType +
                '}';
    }
}
