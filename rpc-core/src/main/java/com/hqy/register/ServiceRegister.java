package com.hqy.register;

import java.util.List;

public interface ServiceRegister {
    void register(String serviceName, String serviceAddress);
    List<String> lookup(String serviceName);
}
