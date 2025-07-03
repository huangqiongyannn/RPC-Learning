package com.hqy.provider.service;

public interface ServiceProvider {
    void addService(Object service);
    Object getService(String serviceName);
}
