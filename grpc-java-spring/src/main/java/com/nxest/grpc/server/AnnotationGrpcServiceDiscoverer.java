package com.nxest.grpc.server;

import com.google.common.collect.Lists;
import io.grpc.*;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * Find {@link ServerInterceptor} with annotation {@link GrpcService} and bind interceptors.
 */
public class AnnotationGrpcServiceDiscoverer implements ApplicationContextAware, GrpcServiceDiscoverer {

    private static final Logger logger = Logger.getLogger(AnnotationGrpcServiceDiscoverer.class.getName());

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Collection<GrpcServiceDefinition> findGrpcServices() {
        Collection<String> beanNames =
            Arrays.asList(this.applicationContext.getBeanNamesForAnnotation(GrpcService.class));
        List<GrpcServiceDefinition> definitions = Lists.newArrayListWithCapacity(beanNames.size());

        List<ServerInterceptor> globalInterceptorList = globalInterceptorsWithAnnotation();
        for (String beanName : beanNames) {
            BindableService bindableService = this.applicationContext.getBean(beanName, BindableService.class);
            ServerServiceDefinition serviceDefinition = bindableService.bindService();
            GrpcService grpcServiceAnnotation = applicationContext.findAnnotationOnBean(beanName, GrpcService.class);
            serviceDefinition = bindInterceptors(serviceDefinition, grpcServiceAnnotation, globalInterceptorList);
            definitions.add(new GrpcServiceDefinition(beanName, bindableService.getClass(), serviceDefinition));
        }
        return definitions;
    }

    private ServerServiceDefinition bindInterceptors(ServerServiceDefinition serviceDefinition,
                                                     GrpcService grpcServiceAnnotation,
                                                     List<ServerInterceptor> globalInterceptorList) {
        List<ServerInterceptor> interceptors = Lists.newArrayList();
        if (grpcServiceAnnotation.applyGlobalInterceptors()) {
            interceptors.addAll(globalInterceptorList);
        }
        for (Class<? extends ServerInterceptor> serverInterceptorClass : grpcServiceAnnotation.interceptors()) {
            ServerInterceptor serverInterceptor;
            if (applicationContext.getBeanNamesForType(serverInterceptorClass).length > 0) {
                serverInterceptor = applicationContext.getBean(serverInterceptorClass);
            } else {
                try {
                    serverInterceptor = serverInterceptorClass.newInstance();
                } catch (Exception e) {
                    throw new BeanCreationException("Failed to create interceptor instance", e);
                }
            }
            interceptors.add(serverInterceptor);
        }
        //sort,support for spring @Order
        interceptors.sort(AnnotationAwareOrderComparator.INSTANCE);
        List<String> interceptorNames = interceptors.stream().map(s -> s.getClass().getName()).collect(Collectors.toList());

        logger.info(format("Grpc service %s bind interceptors: %s.", serviceDefinition.getClass().getName(), String.join(", ", interceptorNames)));
        return ServerInterceptors.intercept(serviceDefinition, Lists.newArrayList(interceptors));
    }


    private List<ServerInterceptor> globalInterceptorsWithAnnotation() {
        Map<String, Object> possibleInterceptors = applicationContext.getBeansWithAnnotation(GrpcServerInterceptor.class);

        Collection<String> invalidInterceptors = possibleInterceptors.entrySet().stream()
            .filter(e -> !(e.getValue() instanceof ServerInterceptor))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        if (!invalidInterceptors.isEmpty()) {
            throw new IllegalStateException((format(
                "The following beans are annotated with @GrpcServerInterceptor, but are not ServerInterceptor: %s",
                String.join(", ", invalidInterceptors))));
        }

        return possibleInterceptors.values().stream()
            .map(s -> (ServerInterceptor) s)
            .filter(this::filterGlobalInterceptor)
            .collect(Collectors.toList());
    }

    private boolean filterGlobalInterceptor(ServerInterceptor interceptor) {
        GrpcServerInterceptor annotation = AnnotationUtils.getAnnotation(interceptor.getClass(), GrpcServerInterceptor.class);
        return annotation.global();
    }

}
