package com.hqy.prodider;

import java.util.HashMap;
import java.util.Map;

public interface ServiceProvider {
    void addService(String serviceName, Object service);
    Object getService(String serviceName);
}
