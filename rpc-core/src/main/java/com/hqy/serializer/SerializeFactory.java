package com.hqy.serializer;

import com.hqy.enumeration.SerializerType;
import com.hqy.serializer.impl.HessionSerializer;
import com.hqy.serializer.impl.JavaSerializer;
import com.hqy.serializer.impl.KryoSerializer;
import com.hqy.serializer.impl.ProtostuffSerialzier;

import java.util.HashMap;
import java.util.Map;

public class SerializeFactory {
    private static final Map<SerializerType, Serializer> CACHE = new HashMap<>();

    static {
        CACHE.put(SerializerType.JAVA, new JavaSerializer());
        CACHE.put(SerializerType.KRYO, new KryoSerializer()); // 内部用 ThreadLocal
        CACHE.put(SerializerType.HESSION, new HessionSerializer());
        CACHE.put(SerializerType.PROTOSTUFF, new ProtostuffSerialzier());
    }

    public static Serializer getSerializer(SerializerType type) {
        Serializer serializer = CACHE.get(type);
        if (serializer == null) {
            throw new IllegalArgumentException("不支持该序列化类型：" + type);
        }
        return serializer;
    }
}

