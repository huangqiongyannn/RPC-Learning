package com.hqy.handler.socket;

import com.hqy.entity.RpcRequest;
import com.hqy.entity.RpcResponse;
import com.hqy.provider.service.ServiceProvider;
import com.hqy.provider.service.impl.ZKServiceProvider;

import java.lang.reflect.Method;

public class RequestHandler {
    private final ServiceProvider provider = ZKServiceProvider.getInstance();
    // 通过反射来完成
    public  RpcResponse handle(RpcRequest request) {
        RpcResponse<Object> response = null;
        try {
            Object service = provider.getService(request.getClassName());
            Class<?> aClass = service.getClass();
            Object o = aClass.getConstructor().newInstance();
            Method method = aClass.getMethod(request.getMethodName(), request.getFieldClasses());
            Object result = method.invoke(o, request.getFieldValues());
//            Thread.sleep(5000);
            response = RpcResponse.success(result);
            return response;
        } catch (Exception e) {
            response = RpcResponse.fail(e.getMessage());
            System.out.println("方法执行有问题！");
            e.printStackTrace();
        }
        return response;
    }
}
