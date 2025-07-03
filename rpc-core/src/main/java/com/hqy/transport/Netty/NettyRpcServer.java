package com.hqy.transport.Netty;

import com.hqy.config.RpcServerConfig;
import com.hqy.enumeration.SerializerType;
import com.hqy.handler.netty.NettyServerHandler;
import com.hqy.limiter.impl.TokenBucketRateLimiter;
import com.hqy.limiter.provider.TokenBucketRateLimiterProvider;
import com.hqy.transport.Netty.coder.RpcDecoder;
import com.hqy.transport.Netty.coder.RpcEncoder;
import com.hqy.transport.api.RpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyRpcServer implements RpcServer {
    private final RpcServerConfig config = RpcServerConfig.getInstance();
    private int port;

    public NettyRpcServer() {
        this.port = config.getPort();
    }

    @Override
    public void start() {

        // 监听连接请求
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        // 获取序列化器
        SerializerType serializerType = RpcServerConfig.getInstance().getSerializerType();
        System.out.println("服务器端使用的序列化方法是：" + serializerType.toString());
        // 配置netty启动类
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new RpcDecoder());
                            pipeline.addLast(new RpcEncoder(serializerType));
                            pipeline.addLast(new NettyServerHandler()); // 自定义业务处理
                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("启动netty监听端口：" + port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
