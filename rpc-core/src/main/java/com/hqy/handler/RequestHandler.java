package com.hqy.handler;

import com.hqy.entity.RpcRequest;
import com.hqy.entity.RpcResponse;

import java.lang.reflect.Method;

public class RequestHandler {

    public static RpcResponse handleRequest(RpcRequest request) {
        RpcResponse<Object> response = null;
        try {
            Class<?> aClass = Class.forName("com.hqy.service." + request.getClassName() + "Impl");
            Object o = aClass.getConstructor().newInstance();
            Method method = aClass.getMethod(request.getMethodName(), request.getFieldClasses());
            Object result = method.invoke(o, request.getFieldValues());
            Thread.sleep(5000);
            response = RpcResponse.success(result);
            System.out.println("方法执行完毕！结果为：" + result.toString());
            return response;
        } catch (Exception e) {
            response = RpcResponse.fail(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }
}
