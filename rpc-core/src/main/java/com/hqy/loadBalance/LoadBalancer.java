package com.hqy.loadBalance;

import java.util.List;

public interface LoadBalancer {
    String select(String serviceName, List<String> serviceAddresses);
}
