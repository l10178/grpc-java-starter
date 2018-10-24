package com.nxest.grpc.spring.server;

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
@Service
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GrpcService {

    /**
     * The value may indicate a suggestion for a logical component name
     *
     * @return the suggested component name, if any
     */
    String value() default "";
}
