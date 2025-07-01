package com.hqy.serialize.impl;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.hqy.serialize.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessionSerializer implements Serializer {
    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            HessianOutput output = new HessianOutput(bos);
            output.writeObject(obj);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            HessianInput input = new HessianInput(bis);
            return (T) input.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
