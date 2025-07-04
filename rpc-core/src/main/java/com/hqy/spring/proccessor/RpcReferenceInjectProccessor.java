package com.hqy.spring.proccessor;

import com.hqy.proxy.RpcClientProxy;
import com.hqy.spring.annotation.RpcReference;
import com.hqy.transport.Netty.NettyRpcClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class RpcReferenceInjectProccessor implements BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        System.out.println("before:" + beanName);
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(RpcReference.class)) {
//                System.out.println("before field:" + field.getName());
                // 创建动态代理对象
                RpcClientProxy proxy = new RpcClientProxy(new NettyRpcClient(), field.getType());
                field.setAccessible(true);
                try {
                    field.set(bean, proxy.getProxy());
                    System.out.println(field.getType().getName() + "的代理对象已创建成功！");
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bean;
    }
}
