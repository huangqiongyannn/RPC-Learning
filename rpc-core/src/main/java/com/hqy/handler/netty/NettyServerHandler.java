package com.hqy.handler.netty;

import com.hqy.entity.RpcRequest;
import com.hqy.entity.RpcResponse;
import com.hqy.limiter.impl.TokenBucketRateLimiter;
import com.hqy.limiter.provider.TokenBucketRateLimiterProvider;
import com.hqy.provider.service.ServiceProvider;
import com.hqy.provider.service.impl.ZKServiceProvider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;

public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private final ServiceProvider provider = ZKServiceProvider.getInstance();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {

        // 限流器
        String interfaceName = request.getClassName() + "#" + request.getMethodName();
        boolean token = TokenBucketRateLimiterProvider.getRateLimiter(interfaceName).getToken();
        if (!token) {
            System.out.println(interfaceName + "接口限流");
            ctx.writeAndFlush(RpcResponse.fail("系统繁忙，请稍后再试！"));
            ctx.close();
            return;
        }
        RpcResponse<Object> response = null;
        try {
            Object service = provider.getService(request.getClassName());
            Class<?> aClass = service.getClass();
            Object o = aClass.getConstructor().newInstance();
            Method method = aClass.getMethod(request.getMethodName(), request.getFieldClasses());
            Object result = method.invoke(o, request.getFieldValues());
//            Thread.sleep(5000);
            response = RpcResponse.success(result);
        } catch (Exception e) {
            response = RpcResponse.fail(e.getMessage());
            System.out.println("方法执行有问题！");
            e.printStackTrace();
        }
        ctx.writeAndFlush(response);
        ctx.close();
    }
}
