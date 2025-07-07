package com.hqy.register;

import java.util.List;

public interface ServiceRegister {
    void registry(String serviceName, String serviceAddress);
    List<String> lookup(String serviceName) throws InterruptedException;
}
