package com.nxest.grpc.spring.server;

import io.grpc.Server;
import org.springframework.context.ApplicationEvent;

/**
 * Create a grpc server initialized event
 */
public class GrpcServerInitializedEvent extends ApplicationEvent {
    /**
     * Create grpc server initialized event.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public GrpcServerInitializedEvent(Server source) {
        super(source);
    }

    public Server getServer() {
        return (Server) getSource();
    }
}
