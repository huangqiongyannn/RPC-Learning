package com.hqy.transport.Netty;

import com.hqy.config.RpcServerConfig;
import com.hqy.serialize.SerializeFactory;
import com.hqy.serialize.Serializer;
import com.hqy.serialize.SerializerType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcDecoder<T> extends ByteToMessageDecoder {
    private final Class<T> targetClass;

    public RpcDecoder(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) return;
        in.markReaderIndex();
        int length = in.readInt();
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[length];
        in.readBytes(data);
        Serializer serializer = SerializeFactory.getSerializer(RpcServerConfig.getInstance().getSerializerType());
        out.add(serializer.deserialize(data, targetClass));
    }
}