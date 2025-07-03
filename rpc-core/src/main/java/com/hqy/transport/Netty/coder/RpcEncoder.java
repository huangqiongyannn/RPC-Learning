package com.hqy.transport.Netty.coder;

import com.hqy.entity.RpcRequest;
import com.hqy.enumeration.PackageType;
import com.hqy.serializer.SerializeFactory;
import com.hqy.serializer.Serializer;
import com.hqy.enumeration.SerializerType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcEncoder extends MessageToByteEncoder<Object> {

    private final SerializerType serializerType;

    public RpcEncoder(SerializerType serializerType) {
        this.serializerType = serializerType;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) {

        out.writeInt(0xCAFEBABE); // 写入魔数
        if (in instanceof RpcRequest) {
            out.writeInt(PackageType.REQUEST.getCode());
        } else {
            out.writeInt(PackageType.RESPONSE.getCode());
        }
        out.writeInt(serializerType.getCode());
        Serializer serializer = SerializeFactory.getSerializer(serializerType);
        byte[] bytes = serializer.serialize(in);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
