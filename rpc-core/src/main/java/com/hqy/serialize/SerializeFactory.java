package com.hqy.serialize;

import com.hqy.serialize.impl.JavaSerializer;

public class SerializeFactory {
    public static Serializer getSerializer(SerializerType type) {
        switch (type) {
            case JAVA:
                return new JavaSerializer();
            default:
                throw new IllegalArgumentException("不支持该序列化类型：" + type);
        }
    }
}
