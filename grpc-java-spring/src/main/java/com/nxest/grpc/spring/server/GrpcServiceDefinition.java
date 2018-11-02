package com.nxest.grpc.spring.server;

import io.grpc.ServerServiceDefinition;

public class GrpcServiceDefinition {

    private final String beanName;
    private final Class<?> beanClazz;
    private final ServerServiceDefinition definition;

    /**
     * Creates a new GrpcServiceDefinition.
     *
     * @param beanName The name of the grpc service bean in the spring context.
     * @param beanClazz The class of the grpc service bean.
     * @param definition The grpc service definition.
     */
    public GrpcServiceDefinition(final String beanName, final Class<?> beanClazz,
                                 final ServerServiceDefinition definition) {
        this.beanName = beanName;
        this.beanClazz = beanClazz;
        this.definition = definition;
    }

    /**
     * Gets the name of the grpc service bean.
     *
     * @return The name of the bean.
     */
    public String getBeanName() {
        return this.beanName;
    }

    /**
     * Gets the class of the grpc service bean.
     *
     * @return The class of the grpc service bean.
     */
    public Class<?> getBeanClazz() {
        return this.beanClazz;
    }

    /**
     * Gets the grpc service definition.
     *
     * @return The grpc service definition.
     */
    public ServerServiceDefinition getDefinition() {
        return this.definition;
    }
}
