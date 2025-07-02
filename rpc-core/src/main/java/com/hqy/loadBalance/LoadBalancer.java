package com.hqy.loadBalance;

import java.util.List;

public interface LoadBalancer {
    String select(List<String> serviceAddresses);
}
