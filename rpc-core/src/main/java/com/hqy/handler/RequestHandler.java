package com.hqy.handler;

import com.hqy.entity.RpcRequest;
import com.hqy.entity.RpcResponse;
import com.hqy.prodider.ServiceProvider;
import com.hqy.prodider.impl.TestServiceProvider;

import java.lang.reflect.Method;

public class RequestHandler {
    private final ServiceProvider provider = new TestServiceProvider();
    // 通过反射来完成
    public  RpcResponse handle(RpcRequest request) {
        RpcResponse<Object> response = null;
        try {
            Object service = provider.getService(request.getClassName());
            System.out.println(service.getClass());
            Class<?> aClass = service.getClass();
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
