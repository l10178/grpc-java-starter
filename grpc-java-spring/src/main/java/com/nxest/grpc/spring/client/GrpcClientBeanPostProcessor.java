package com.nxest.grpc.spring.client;

import com.google.common.collect.Maps;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.stub.AbstractStub;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeansException;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 根据注解为每个GRPC注入真实的Channel
 */
public class GrpcClientBeanPostProcessor implements BeanPostProcessor, AutoCloseable {

    private Map<String, List<Class>> beansToProcess = Maps.newHashMap();

    private GrpcChannelFactory channelFactory;

    private GrpcClientInterceptorDiscoverer clientInterceptorDiscoverer;

    public GrpcClientBeanPostProcessor(GrpcClientInterceptorDiscoverer clientInterceptorDiscoverer) {
        this(new AddressChannelFactory(), clientInterceptorDiscoverer);
    }

    public GrpcClientBeanPostProcessor(GrpcChannelFactory channelFactory, GrpcClientInterceptorDiscoverer clientInterceptorDiscoverer) {
        this.channelFactory = channelFactory;
        this.clientInterceptorDiscoverer = clientInterceptorDiscoverer;
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
        if (!beansToProcess.containsKey(beanName)) {
            return bean;
        }
        Object target = getTargetBean(bean);
        for (Class clazz : beansToProcess.get(beanName)) {
            for (Field field : clazz.getDeclaredFields()) {
                GrpcClient annotation = AnnotationUtils.getAnnotation(field, GrpcClient.class);
                if (annotation == null) {
                    continue;
                }
                // all client interceptors
                Collection<ClientInterceptor> interceptors = clientInterceptorDiscoverer.findGrpcClientInterceptors(annotation);

                Channel channel = channelFactory.createChannel(annotation.value(), interceptors);
                // field may be a channel or AbstractStub
                final Object value = getFieldValue(field, channel);

                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, target, value);
            }
        }
        return bean;
    }

    private Object getFieldValue(final Field field, final Channel channel) {
        final Class<?> fieldType = field.getType();
        //field is channel
        if (Channel.class.equals(fieldType)) {
            return channel;
        }
        if (AbstractStub.class.isAssignableFrom(fieldType)) {
            //field is AbstractStub
            try {
                Constructor<?> constructor = fieldType.getDeclaredConstructor(Channel.class);
                ReflectionUtils.makeAccessible(constructor);
                return constructor.newInstance(channel);
            } catch (final NoSuchMethodException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
                throw new BeanInstantiationException(fieldType,
                    "Failed to create gRPC client for field: " + field, e);
            }
        }
        throw new InvalidPropertyException(field.getDeclaringClass(), field.getName(),
            "Unsupported field type " + fieldType.getName());
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

    @Override
    public void close() {
        beansToProcess.clear();
    }

}
