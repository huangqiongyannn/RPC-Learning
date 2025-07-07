package com.hqy.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.hqy.Exception.SerializerException;
import com.hqy.serializer.Serializer;

public class JsonSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        if (obj == null) {
            return new byte[0];
        }
        return JSON.toJSONBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws SerializerException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return JSON.parseObject(bytes, clazz);
    }
}
