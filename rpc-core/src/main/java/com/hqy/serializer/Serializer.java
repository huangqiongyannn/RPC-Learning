package com.hqy.serializer;

import com.hqy.Exception.SerializerException;

public interface Serializer {
    byte[] serialize(Object obj);
    <T> T  deserialize(byte[] bytes, Class<T> clazz) throws SerializerException;
}
