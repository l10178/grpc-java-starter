package com.nxest.grpc.spring.client;

import com.nxest.grpc.spring.client.configure.GrpcClientProperties;
import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.stub.AbstractStub;

import java.lang.annotation.*;

/**
 * An annotation for fields of type {@link Channel} or subclasses of {@link AbstractStub}/gRPC
 * client services. Annotated fields will be automatically populated by Spring.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GrpcClient {

    /**
     * The name of the grpc client. This name will be used to get the {@link GrpcClientProperties
     * config options} for this client.
     *
     * <p>
     * <b>Example:</b> <code>@GrpcClient("default")</code> &lt;-&gt;
     * <tt>grpc.client.default.port=9090</tt>
     * </p>
     *
     * @return The name of the grpc client.
     */
    String value() default GrpcChannelFactory.DEFAULT_CHANEL_NAME;

    /**
     * A list of {@link ClientInterceptor}s that should be used with this client in addition to the
     * globally defined ones. If a bean of the given type exists, it will be used; otherwise a new
     * instance of that class will be created via no-args constructor.
     *
     * <p>
     * <b>Note:</b> These interceptors will be applied after the global interceptors. But the
     * interceptors that were applied last, will be called first.
     * </p>
     *
     * @return A list of ClientInterceptors that should be used.
     */
    Class<? extends ClientInterceptor>[] interceptors() default {};

    /**
     * if need apply global interceptors
     *
     * @return if need apply global interceptors
     */
    boolean applyGlobalInterceptors() default true;

}
