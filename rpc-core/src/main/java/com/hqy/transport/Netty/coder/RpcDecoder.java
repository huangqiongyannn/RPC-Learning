package com.hqy.transport.Netty.coder;

import com.hqy.config.RpcServerConfig;
import com.hqy.entity.RpcRequest;
import com.hqy.entity.RpcResponse;
import com.hqy.enumeration.PackageType;
import com.hqy.serialize.SerializeFactory;
import com.hqy.serialize.Serializer;
import com.hqy.enumeration.SerializerType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes() < 16) return;
        in.markReaderIndex();

        int magic = in.readInt();
        if (magic != 0xCAFEBABE) {
            ctx.close(); // 非协议包，断开连接
            return;
        }
        int packageType = in.readInt();
        int serializerType = in.readInt();
        int length = in.readInt();
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }
        byte[] data = new byte[length];
        in.readBytes(data);

        Serializer serializer = SerializeFactory.getSerializer(SerializerType.fromCode(serializerType));
        Class<?> targetClass = null;
        if (packageType == PackageType.REQUEST.getCode()) {
            targetClass = RpcRequest.class;
        } else if (packageType == PackageType.RESPONSE.getCode()) {
            targetClass = RpcResponse.class;
        }
        out.add(serializer.deserialize(data, targetClass));
    }
}