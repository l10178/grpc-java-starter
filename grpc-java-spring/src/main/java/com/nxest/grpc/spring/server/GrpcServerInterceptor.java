package com.nxest.grpc.spring.server;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * {@code GrpcServerInterceptor} is an annotation that is used to mark a gRPC ServerInterceptor implementation for automatic inclusion in
 * your server, by default it's a global interceptor.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface GrpcServerInterceptor {

    /**
     * The value may indicate a suggestion for a logical component name
     *
     * @return the suggested component name, if any
     */
    String value() default "";

    /**
     * if is a global interceptors
     *
     * @return if is a global interceptors
     */
    boolean global() default true;
}
