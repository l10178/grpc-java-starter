package com.nxest.grpc.spring.client;

import com.google.common.collect.Lists;
import io.grpc.ClientInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class AnnotationClientInterceptorDiscoverer implements ApplicationContextAware, InitializingBean, GrpcClientInterceptorDiscoverer {

    private final List<ClientInterceptor> clientInterceptors = Lists.newArrayList();

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, Object> possibleInterceptors = applicationContext.getBeansWithAnnotation(GrpcClientInterceptor.class);

        Collection<String> invalidInterceptors = possibleInterceptors.entrySet().stream()
            .filter(e -> !(e.getValue() instanceof ClientInterceptor))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        if (!invalidInterceptors.isEmpty()) {
            throw new IllegalStateException((format(
                "The following beans are annotated with @GrpcClientInterceptor, but are not ClientInterceptor: %s",
                String.join(", ", invalidInterceptors))));
        }
        List<ClientInterceptor> interceptors = possibleInterceptors.values()
            .stream()
            .map(s -> (ClientInterceptor) s)
            .filter(this::filterGlobalInterceptor)
            .collect(Collectors.toList());
        clientInterceptors.addAll(interceptors);
    }

    private boolean filterGlobalInterceptor(ClientInterceptor interceptor) {
        GrpcClientInterceptor annotation = AnnotationUtils.getAnnotation(interceptor.getClass(), GrpcClientInterceptor.class);
        return annotation.global();
    }

    @Override
    public Collection<ClientInterceptor> findGrpcClientInterceptors(GrpcClient annotation) {

        List<ClientInterceptor> clientInterceptors = Lists.newArrayList();

        for (Class<? extends ClientInterceptor> clientInterceptorClass : annotation.interceptors()) {
            ClientInterceptor clientInterceptor = applicationContext.getBean(clientInterceptorClass);
            if (clientInterceptor == null) {
                try {
                    clientInterceptor = clientInterceptorClass.newInstance();
                } catch (Exception e) {
                    throw new BeanCreationException("Failed to create client interceptor instance", e);
                }
            }
            clientInterceptors.add(clientInterceptor);
        }

        if (annotation.applyGlobalInterceptors()) {
            clientInterceptors.addAll(globalClientInterceptorsWithAnnotation());
        }
        return clientInterceptors;
    }

    private List<ClientInterceptor> globalClientInterceptorsWithAnnotation() {
        return clientInterceptors;
    }
}
