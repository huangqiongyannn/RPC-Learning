package com.hqy.handler;

import com.hqy.entity.RpcRequest;
import com.hqy.entity.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyRequestHandler extends SimpleChannelInboundHandler<RpcRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
        RequestHandler handler = new RequestHandler();
        RpcResponse reponse = handler.handle(rpcRequest);
        ctx.writeAndFlush(reponse);
        ctx.close();
    }
}
