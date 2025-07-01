package com.hqy.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum PackageType {
    REQUEST(0),
    RESPONSE(1);

    private final int code;

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
