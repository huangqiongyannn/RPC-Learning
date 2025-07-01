package com.hqy.serialize;

import com.hqy.enumeration.SerializerType;
import com.hqy.serialize.impl.HessionSerializer;
import com.hqy.serialize.impl.JavaSerializer;
import com.hqy.serialize.impl.KryoSerializer;
import com.hqy.serialize.impl.ProtostuffSerialzier;

public class SerializeFactory {
    public static Serializer getSerializer(SerializerType type) {
        switch (type) {
            case JAVA:
                return new JavaSerializer();
            case KRYO:
                return new KryoSerializer();
            case HESSION:
                return new HessionSerializer();
            case PROTOSTUFF:
                return new ProtostuffSerialzier();
            default:
                throw new IllegalArgumentException("不支持该序列化类型：" + type);
        }
    }
}
