package com.nxest.grpc.spring.server.configure;

/**
 * Grpc server properties
 */
public class GrpcServerProperties {

    /**
     * Default grpc server port
     */
    public static final int DEFAULT_PORT = 6868;

    /**
     * The default  grpc server properties
     */
    public static final GrpcServerProperties DEFAULT = new GrpcServerProperties();

    /**
     * server port
     */
    private int port = DEFAULT_PORT;

    /**
     * Enables server reflection using <a href="https://github.com/grpc/grpc-java/blob/master/documentation/server-reflection-tutorial.md">ProtoReflectionService</a>.
     * Available only from gRPC 1.3 or higher.
     */
    private boolean enableReflection = false;

    /**
     * await server termination MILLISECONDS
     */
    private long shutdownDelayMillis = 1000L;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isEnableReflection() {
        return enableReflection;
    }

    public void setEnableReflection(boolean enableReflection) {
        this.enableReflection = enableReflection;
    }

    public long getShutdownDelayMillis() {
        return shutdownDelayMillis;
    }

    public void setShutdownDelayMillis(long shutdownDelayMillis) {
        this.shutdownDelayMillis = shutdownDelayMillis;
    }
}
