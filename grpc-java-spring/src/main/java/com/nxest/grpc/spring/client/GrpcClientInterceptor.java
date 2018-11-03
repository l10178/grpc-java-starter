package com.nxest.grpc.spring.client;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * {@code GrpClientInterceptor} is an annotation that is used to mark a grpc ClientInterceptor implementation for automatic inclusion in
 * your client, by default it's a global interceptor.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface GrpcClientInterceptor {

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
