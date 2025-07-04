package com.hqy.spring.proccessor;

import com.hqy.provider.service.impl.ZKServiceProvider;
import com.hqy.spring.annotation.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class RpcServiceRegisterProccessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        System.out.println("after:" + beanName);
        Class<?> clazz = bean.getClass();
        if (clazz.isAnnotationPresent(RpcService.class)) {
            if (clazz.getInterfaces().length == 0) {
                throw new RuntimeException("服务必须实现接口！");
            }
            // 服务注册
            ZKServiceProvider.getInstance().addService(bean);
//            System.out.println("已自动注册服务：" + clazz.getName());
        }
        return bean;
    }
}
