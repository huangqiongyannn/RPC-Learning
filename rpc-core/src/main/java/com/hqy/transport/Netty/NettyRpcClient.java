package com.hqy.transport.Netty;

import com.hqy.entity.RpcRequest;
import com.hqy.entity.RpcResponse;
import com.hqy.handler.netty.NettyClientHandler;
import com.hqy.transport.api.RpcClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.io.IOException;
import java.net.InetSocketAddress;

public class NettyRpcClient implements RpcClient {
    private final Bootstrap bootstrap;

    public NettyRpcClient() {
        EventLoopGroup worker = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new RpcDecoder(RpcResponse.class));
                        pipeline.addLast(new RpcEncoder());
                        pipeline.addLast(new NettyClientHandler()); // 自定义业务处理
                    }
                });
    }


    @Override
    public RpcResponse sendRequest(RpcRequest request, InetSocketAddress address) throws IOException {
        String host = address.getHostString();
        int port = address.getPort();
        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            channel.writeAndFlush(request);
            channel.closeFuture().sync();
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("RPCResponse");
            RpcResponse response = channel.attr(key).get();
            System.out.println(response);
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
