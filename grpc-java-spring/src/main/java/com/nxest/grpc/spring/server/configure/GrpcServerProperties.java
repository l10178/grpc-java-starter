package com.nxest.grpc.spring.server.configure;

import io.grpc.internal.GrpcUtil;

import java.util.concurrent.TimeUnit;

/**
 * Grpc server properties
 */
public class GrpcServerProperties {

    /**
     * Default grpc server port
     */
    public static final int DEFAULT_PORT = 6868;
    /**
     * Default grpc server host
     */
    public static final String DEFAULT_HOST = "0.0.0.0";

    /**
     * The default  grpc server properties
     */
    public static final GrpcServerProperties DEFAULT = new GrpcServerProperties();

    /**
     * Bind address for the server. Defaults to {@code 0.0.0.0}.
     */
    private String address = DEFAULT_HOST;

    /**
     * Server port
     */
    private int port = DEFAULT_PORT;

    /**
     * Enables server reflection using <a href="https://github.com/grpc/grpc-java/blob/master/documentation/server-reflection-tutorial.md">ProtoReflectionService</a>.
     * Available only from gRPC 1.3 or higher.
     */
    private boolean enableReflection = false;

    /**
     * Await server termination MILLISECONDS
     */
    private long shutdownDelayMillis = 1000L;

    /**
     * The maximum message size in bytes allowed to be received by the server. If not set ({@code null}) then it will
     * default to {@link GrpcUtil#DEFAULT_MAX_MESSAGE_SIZE DEFAULT_MAX_MESSAGE_SIZE}. If set to {@code -1} then it will
     * use {@link Integer#MAX_VALUE} as limit.
     */
    private Integer maxInboundMessageSize = null;

    /**
     * Enables SSL/TLS
     */
    private boolean enableSsl = true;

    /**
     * The cert chain file, eg. server.cer
     */
    private String certChainFile;

    /**
     * The private key file, eg. server.key
     */
    private String privateKeyFile;

    /**
     * The trust cert collection file, eg. ca.crt
     */
    private String trustCertCollectionFile;

    /**
     * SSL Provider, default 'openssl', see {@link io.grpc.netty.shaded.io.netty.handler.ssl.SslProvider}
     * <pre>
     *     JDK
     *     OPENSSL
     *     OPENSSL_REFCNT
     * </pre>
     */
    private String sslProvider = "openssl";

    private ExecutorProperties executor;

    public static class ExecutorProperties {
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;
        private int workQueueCapacity;
        private TimeUnit keepAliveTimeUnit = TimeUnit.SECONDS;

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public void setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }

        public long getKeepAliveTime() {
            return keepAliveTime;
        }

        public void setKeepAliveTime(long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
        }

        public int getWorkQueueCapacity() {
            return workQueueCapacity;
        }

        public void setWorkQueueCapacity(int workQueueCapacity) {
            this.workQueueCapacity = workQueueCapacity;
        }

        public TimeUnit getKeepAliveTimeUnit() {
            return keepAliveTimeUnit;
        }

        public void setKeepAliveTimeUnit(TimeUnit keepAliveTimeUnit) {
            this.keepAliveTimeUnit = keepAliveTimeUnit;
        }


        @Override
        public String toString() {
            return "ExecutorProperties{" +
                "corePoolSize=" + corePoolSize +
                ", maximumPoolSize=" + maximumPoolSize +
                ", keepAliveTime=" + keepAliveTime +
                ", workQueueCapacity=" + workQueueCapacity +
                ", keepAliveTimeUnit=" + keepAliveTimeUnit +
                '}';
        }
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public Integer getMaxInboundMessageSize() {
        return maxInboundMessageSize;
    }

    public void setMaxInboundMessageSize(Integer maxInboundMessageSize) {
        this.maxInboundMessageSize = maxInboundMessageSize;
    }

    public boolean isEnableSsl() {
        return enableSsl;
    }

    public void setEnableSsl(boolean enableSsl) {
        this.enableSsl = enableSsl;
    }

    public String getCertChainFile() {
        return certChainFile;
    }

    public void setCertChainFile(String certChainFile) {
        this.certChainFile = certChainFile;
    }

    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    public void setPrivateKeyFile(String privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    public String getTrustCertCollectionFile() {
        return trustCertCollectionFile;
    }

    public void setTrustCertCollectionFile(String trustCertCollectionFile) {
        this.trustCertCollectionFile = trustCertCollectionFile;
    }

    public String getSslProvider() {
        return sslProvider;
    }

    public void setSslProvider(String sslProvider) {
        this.sslProvider = sslProvider;
    }

    public ExecutorProperties getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorProperties executor) {
        this.executor = executor;
    }

    @Override
    public String toString() {
        return "GrpcServerProperties{" +
            "address='" + address + '\'' +
            ", port=" + port +
            ", enableReflection=" + enableReflection +
            ", shutdownDelayMillis=" + shutdownDelayMillis +
            ", maxInboundMessageSize=" + maxInboundMessageSize +
            ", enableSsl=" + enableSsl +
            ", certChainFile='" + certChainFile + '\'' +
            ", privateKeyFile='" + privateKeyFile + '\'' +
            ", trustCertCollectionFile='" + trustCertCollectionFile + '\'' +
            ", sslProvider='" + sslProvider + '\'' +
            '}';
    }
}
