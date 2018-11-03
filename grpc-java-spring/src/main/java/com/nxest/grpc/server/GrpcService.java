package com.nxest.grpc.server;

import io.grpc.ServerInterceptor;
import org.springframework.stereotype.Service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code GrpcService} is an annotation that is used to mark a gRPC service implementation for automatic inclusion in
 * your server.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface GrpcService {

    /**
     * The value may indicate a suggestion for a logical component name
     *
     * @return the suggested component name, if any
     */
    String value() default "";

    /**
     * private server interceptors
     *
     * @return server interceptor classes
     */
    Class<? extends ServerInterceptor>[] interceptors() default {};

    /**
     * if need apply global interceptors
     *
     * @return if need apply global interceptors
     */
    boolean applyGlobalInterceptors() default true;
}
