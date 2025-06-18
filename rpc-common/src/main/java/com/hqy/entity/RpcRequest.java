package com.hqy.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
public class RpcRequest implements Serializable {
    // 类的全路径名
    private String className;
    // 方法名
    private String methodName;
    // 入参类型
    private Class<?>[] fieldClasses;
    // 入参名
    private String[] fieldNames;
    // 入参值
    private Object[] fieldValues;
    // 返回值类型
    private Class<?> returnClass;
}
