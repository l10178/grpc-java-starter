package com.nxest.grpc.spring.server;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * {@code GrpcServerInterceptor} is an annotation that is used to mark a gRPC ServerInterceptor implementation for automatic inclusion in
 * your server.
 */
@Service
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GrpcServerInterceptor {

    /**
     * The value may indicate a suggestion for a logical component name
     *
     * @return the suggested component name, if any
     */
    String value() default "";
}
