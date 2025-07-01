package com.hqy.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
@Getter
public enum SerializerType {
    JAVA(0),
    JSON(1),
    KRYO(2),
    HESSION(3),
    PROTOSTUFF(4);

    private final int  code;

    private static final Map<Integer, SerializerType> CODE_MAP = new HashMap<>();

    static {
        for (SerializerType type : SerializerType.values()) {
            CODE_MAP.put(type.getCode(), type);
        }
    }

    public static SerializerType fromCode(int code) {
        return CODE_MAP.get(code);
    }
}
