package com.nxest.grpc.spring.client;

import com.google.common.collect.Lists;
import io.grpc.ClientInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * 全局的客户端Interceptor
 */
public class ClientInterceptorRegistry implements ApplicationContextAware, InitializingBean {

    private final List<ClientInterceptor> clientInterceptors = Lists.newArrayList();
    private ApplicationContext applicationContext;

    public void addClientInterceptors(List<ClientInterceptor> interceptors) {
        clientInterceptors.addAll(interceptors);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<ClientInterceptor> getClientInterceptors() {
        return clientInterceptors;
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
            .collect(Collectors.toList());
        addClientInterceptors(interceptors);
    }

    public ClientInterceptor getClientInterceptorBean(Class<? extends ClientInterceptor> clientInterceptorClass) {
        if (applicationContext.getBeanNamesForType(ClientInterceptor.class).length > 0) {
            return applicationContext.getBean(clientInterceptorClass);
        }
        return null;
    }
}
