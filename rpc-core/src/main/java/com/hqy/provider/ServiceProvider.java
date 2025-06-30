package com.hqy.provider;

public interface ServiceProvider {
    void addService(Object service);
    Object getService(String serviceName);
}
