package com.nxest.grpc.server.configure;

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
     * Bind host for the server. Defaults to {@code 0.0.0.0}.
     */
    private String host = DEFAULT_HOST;

    /**
     * Server port
     */
    private int port = DEFAULT_PORT;


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

    private SecurityProperties security = new SecurityProperties();

    private ExecutorProperties executor;

    /**
     * This argument if set to {{@code True}}, allows keep-alive pings to be sent even if there are no calls in flight.
     */
    private Boolean permitKeepAliveWithoutCalls;

    /**
     * This argument controls the minimum time (in seconds) that gRPC Core would expect between receiving successive pings.
     * If the time between successive pings is less that than this time, then the ping will be considered a bad ping from the peer.
     * Such a ping counts as a ‘ping strike’.
     */
    private Long permitKeepAliveTimeInSeconds;

    public static class SecurityProperties {

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

        @Override
        public String toString() {
            return "SecurityProperties{" +
                "enableSsl=" + enableSsl +
                ", certChainFile='" + certChainFile + '\'' +
                ", privateKeyFile='" + privateKeyFile + '\'' +
                ", trustCertCollectionFile='" + trustCertCollectionFile + '\'' +
                ", sslProvider='" + sslProvider + '\'' +
                '}';
        }
    }

    public static class ExecutorProperties {
        private int corePoolSize = 2;
        private int maximumPoolSize = 4;
        private int workQueueCapacity = 1024;
        private long keepAliveTime = 30;
        private TimeUnit keepAliveTimeUnit = TimeUnit.SECONDS;
        private String rejectedPolicy = "Discard";

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

        public String getRejectedPolicy() {
            return rejectedPolicy;
        }

        public void setRejectedPolicy(String rejectedPolicy) {
            this.rejectedPolicy = rejectedPolicy;
        }

        @Override
        public String toString() {
            return "ExecutorProperties{" +
                "corePoolSize=" + corePoolSize +
                ", maximumPoolSize=" + maximumPoolSize +
                ", workQueueCapacity=" + workQueueCapacity +
                ", keepAliveTime=" + keepAliveTime +
                ", keepAliveTimeUnit=" + keepAliveTimeUnit +
                ", rejectedPolicy='" + rejectedPolicy + '\'' +
                '}';
        }
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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

    public SecurityProperties getSecurity() {
        return security;
    }

    public void setSecurity(SecurityProperties security) {
        this.security = security;
    }

    public ExecutorProperties getExecutor() {
        return executor;
    }

    public void setExecutor(ExecutorProperties executor) {
        this.executor = executor;
    }

    public Boolean getPermitKeepAliveWithoutCalls() {
        return permitKeepAliveWithoutCalls;
    }

    public void setPermitKeepAliveWithoutCalls(Boolean permitKeepAliveWithoutCalls) {
        this.permitKeepAliveWithoutCalls = permitKeepAliveWithoutCalls;
    }

    public Long getPermitKeepAliveTimeInSeconds() {
        return permitKeepAliveTimeInSeconds;
    }

    public void setPermitKeepAliveTimeInSeconds(Long permitKeepAliveTimeInSeconds) {
        this.permitKeepAliveTimeInSeconds = permitKeepAliveTimeInSeconds;
    }
}
