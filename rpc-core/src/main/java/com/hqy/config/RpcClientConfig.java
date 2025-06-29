package com.hqy.config;

import com.hqy.serialize.SerializerType;
import lombok.Data;

@Data
public class RpcClientConfig {
    private String applicationName;
    private int port = 8888;
    private String host = "127.0.0.1";
    private SerializerType serializerType = SerializerType.JAVA;
//    private int serverPort = 8888;
//    private String serverHost = "127.0.0.1";
}
