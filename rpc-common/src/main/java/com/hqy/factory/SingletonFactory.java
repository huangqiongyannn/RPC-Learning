package com.hqy.factory;

import java.lang.reflect.InvocationTargetException;
import java.rmi.server.ExportException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SingletonFactory {
    private static final Map<Class<?>, Object> objectMap = new ConcurrentHashMap<>();

    public static Object getInstance(Class<?> clazz){
        Object instance = objectMap.get(clazz);

        synchronized (clazz) {
            if (instance == null) {
                try {
                    instance = clazz.getDeclaredConstructor().newInstance();
                    objectMap.put(clazz, instance);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }

            }
        }

        return instance;
    }
}
