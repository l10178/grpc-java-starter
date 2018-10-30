package com.nxest.grpc.spring.client;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * {@code GrpClientInterceptor} is an annotation that is used to mark a grpc ClientInterceptor implementation for automatic inclusion in
 * your client, it's a global interceptor.
 */
@Service
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GrpcClientInterceptor {

    /**
     * The value may indicate a suggestion for a logical component name
     *
     * @return the suggested component name, if any
     */
    String value() default "";
}
