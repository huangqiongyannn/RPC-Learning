package com.hqy.retryer;

import com.github.rholder.retry.*;
import com.hqy.entity.RpcRequest;
import com.hqy.entity.RpcResponse;
import com.hqy.transport.api.RpcClient;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class GuavaRetry {
    public static RpcResponse sendRequestWityRetry(RpcRequest request, InetSocketAddress address, RpcClient rpcClient) {
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfException()
                .retryIfResult(response -> !response.isSuccess())
                .withWaitStrategy(WaitStrategies.fixedWait(2, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        System.out.println("请求方法正在进行第" + attempt.getAttemptNumber() + "次调用！");
                    }
                })
                .build();
        try {
            return retryer.call(() -> rpcClient.sendRequest(request, address));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RpcResponse.fail("远程Rpc接口调用失败！");
    }
}
