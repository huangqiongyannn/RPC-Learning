import com.hqy.api.OrderService;
import com.hqy.config.RpcClientConfig;
import com.hqy.enumeration.LoadBalanceType;
import com.hqy.enumeration.SerializerType;
import com.hqy.limiter.RateLimiter;
import com.hqy.limiter.impl.TokenBucketRateLimiter;
import com.hqy.limiter.provider.TokenBucketRateLimiterProvider;
import com.hqy.proxy.RpcClientProxy;
import com.hqy.transport.Netty.NettyRpcClient;

import java.io.IOException;

public class TokenBucketTest {
    public static void main(String[] args) throws InterruptedException {
        RateLimiter limiter = TokenBucketRateLimiterProvider.getRateLimiter("test");

        // 模拟多个线程高并发抢令牌
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                RpcClientConfig.getInstance().setSerializerType(SerializerType.PROTOSTUFF);
                RpcClientConfig.getInstance().setLoadBalanceType(LoadBalanceType.CONSISTENT_HASH);
                // 创建客户端代理对象
                RpcClientProxy clientProxy = new RpcClientProxy(new NettyRpcClient(), OrderService.class);
                OrderService service = clientProxy.getProxy();
                String userId = "hqy";
                String orderId = "1234";
                String response = null;
                try {
                    response = service.createOrder(userId, orderId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("返回内容：" + response);
            }, "线程-" + i).start();
        }


        // 主线程睡眠防止程序立即退出
        Thread.sleep(10000); // 运行 10 秒钟观察结果
    }
}
