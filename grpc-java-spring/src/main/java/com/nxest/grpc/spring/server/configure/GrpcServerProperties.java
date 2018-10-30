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

    public boolean isEnableSsl() {
        return enableSsl;
    }

    public void setEnableSsl(boolean enableSsl) {
        this.enableSsl = enableSsl;
    }
}
