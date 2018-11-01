package com.nxest.grpc.spring.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.stub.AbstractStub;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GrpcClientBeanPostProcessor implements BeanPostProcessor {

    private Map<String, List<Class>> beansToProcess = Maps.newHashMap();

    private GrpcChannelFactory channelFactory;

    private final ClientInterceptorRegistry clientInterceptorRegistry;

    public GrpcClientBeanPostProcessor(ClientInterceptorRegistry clientInterceptorRegistry) {
        this(new AddressChannelFactory(), clientInterceptorRegistry);
    }

    public GrpcClientBeanPostProcessor(GrpcChannelFactory channelFactory, ClientInterceptorRegistry clientInterceptorRegistry) {
        this.channelFactory = channelFactory;
        this.clientInterceptorRegistry = clientInterceptorRegistry;
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
                        List<ClientInterceptor> clientInterceptors = Lists.newArrayList();
                        for (Class<? extends ClientInterceptor> clientInterceptorClass : annotation.interceptors()) {
                            ClientInterceptor clientInterceptor = clientInterceptorRegistry.getClientInterceptorBean(clientInterceptorClass);
                            if (clientInterceptor == null) {
                                try {
                                    clientInterceptor = clientInterceptorClass.newInstance();
                                } catch (Exception e) {
                                    throw new BeanCreationException("Failed to create interceptor instance", e);
                                }
                            }
                            clientInterceptors.add(clientInterceptor);
                        }


                        if (annotation.applyGlobalInterceptors()) {
                            clientInterceptors.addAll(clientInterceptorRegistry.getClientInterceptors());
                        }

                        Channel channel = channelFactory.createChannel(annotation.value(), clientInterceptors);
                        final Class<?> fieldType = field.getType();
                        final Object value;
                        if (Channel.class.equals(fieldType)) {
                            value = channel;
                        } else if (AbstractStub.class.isAssignableFrom(fieldType)) {
                            try {
                                Constructor<?> constructor = fieldType.getDeclaredConstructor(Channel.class);
                                ReflectionUtils.makeAccessible(constructor);
                                value = constructor.newInstance(channel);
                            } catch (final NoSuchMethodException | InstantiationException | IllegalAccessException
                                | IllegalArgumentException | InvocationTargetException e) {
                                throw new BeanInstantiationException(fieldType,
                                    "Failed to create gRPC client for field: " + field, e);
                            }
                        } else {
                            throw new InvalidPropertyException(field.getDeclaringClass(), field.getName(),
                                "Unsupported field type " + fieldType.getName());
                        }
                        ReflectionUtils.makeAccessible(field);
                        ReflectionUtils.setField(field, target, value);
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
