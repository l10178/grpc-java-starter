package com.nxest.grpc.spring.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GrpcClientBeanPostProcessor implements BeanPostProcessor {

    private Map<String, List<Class>> beansToProcess = Maps.newHashMap();

    private ListableBeanFactory beanFactory;

    private GrpcChannelFactory channelFactory;

    public GrpcClientBeanPostProcessor(ListableBeanFactory beanFactory, GrpcChannelFactory channelFactory) {
        this.beanFactory = beanFactory;
        this.channelFactory = channelFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class clazz = bean.getClass();
        do {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(GrpcClient.class)) {
                    if (!beansToProcess.containsKey(beanName)) {
                        beansToProcess.put(beanName, new ArrayList<>());
                    }
                    beansToProcess.get(beanName).add(clazz);
                }
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beansToProcess.containsKey(beanName)) {
            Object target = getTargetBean(bean);
            for (Class clazz : beansToProcess.get(beanName)) {
                for (Field field : clazz.getDeclaredFields()) {
                    GrpcClient annotation = AnnotationUtils.getAnnotation(field, GrpcClient.class);
                    if (null != annotation) {

                        List<ClientInterceptor> list = Lists.newArrayList();
                        for (Class<? extends ClientInterceptor> clientInterceptorClass : annotation.interceptors()) {
                            ClientInterceptor clientInterceptor;
                            if (beanFactory.getBeanNamesForType(ClientInterceptor.class).length > 0) {
                                clientInterceptor = beanFactory.getBean(clientInterceptorClass);
                            } else {
                                try {
                                    clientInterceptor = clientInterceptorClass.newInstance();
                                } catch (Exception e) {
                                    throw new BeanCreationException("Failed to create interceptor instance", e);
                                }
                            }
                            list.add(clientInterceptor);
                        }

                        Channel channel = channelFactory.createChannel(annotation.value(), list);
                        ReflectionUtils.makeAccessible(field);
                        ReflectionUtils.setField(field, target, channel);
                    }
                }
            }
        }
        return bean;
    }

    private Object getTargetBean(Object bean) {
        Object target = bean;
        while (AopUtils.isAopProxy(target)) {
            try {
                target = ((Advised) target).getTargetSource().getTarget();
            } catch (Exception e) {
                throw new RuntimeException("Error get target bean.", e);
            }
        }
        return target;
    }

}
