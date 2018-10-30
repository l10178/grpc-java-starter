package com.nxest.grpc.spring.client.configure;

import io.grpc.internal.GrpcUtil;
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;

public class GrpcClientProperties {

    public static final String DEFAULT_HOST = "127.0.0.1";
    public static final Integer DEFAULT_PORT = 6868;

    public static final GrpcClientProperties DEFAULT = new GrpcClientProperties();

    /**
     * host to connect to.
     */
    private String host = DEFAULT_HOST;

    /**
     * port to connect to.
     */
    private Integer port = DEFAULT_PORT;

    /**
     * Setting to enable keepalive. Default to {@code false}.
     */
    private boolean enableKeepAlive = false;

    /**
     * Sets whether keepalive will be performed when there are no outstanding RPC on a connection.
     * Defaults to {@code false}.
     */
    private boolean keepAliveWithoutCalls = false;

    /**
     * The default delay in seconds before we send a keepalive. Defaults to {@code 60}.
     */
    private long keepAliveTime = 60;

    /**
     * The default timeout in seconds for a keepalive ping request. Defaults to {@code 20}.
     */
    private long keepAliveTimeout = 20;

    /**
     * The maximum message size in bytes allowed to be received on the channel. If not set (<tt>-2</tt>)
     * then it will default to {@link GrpcUtil#DEFAULT_MAX_MESSAGE_SIZE DEFAULT_MAX_MESSAGE_SIZE}. If
     * set to <tt>-1</tt> then it will use {@link Integer#MAX_VALUE} as limit.
     */
    private int maxInboundMessageSize = -2;

    private boolean fullStreamDecompression = false;

    /**
     * The negotiation type to use on the connection. Either of {@link NegotiationType#TLS TLS}
     * (recommended), {@link NegotiationType#PLAINTEXT_UPGRADE PLAINTEXT_UPGRADE} or
     * {@link NegotiationType#PLAINTEXT PLAINTEXT}.
     */
    private String negotiationType = NegotiationType.TLS.toString();

    /**
     * The cert chain file, eg. server.cer
     * Note: certChainFile and privateKeyFile are only needed if mutual auth is desired.
     * And if you specify certChainFile you must also specify privateKeyFile
     */
    private String certChainFile;

    /**
     * The private key file, eg. server.key
     * Note: certChainFile and privateKeyFile are only needed if mutual auth is desired.
     * And if you specify certChainFile you must also specify privateKeyFile
     */
    private String privateKeyFile;

    /**
     * The trust cert collection file, eg. ca.crt
     */
    private String trustCertCollectionFile;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public boolean isEnableKeepAlive() {
        return enableKeepAlive;
    }

    public void setEnableKeepAlive(boolean enableKeepAlive) {
        this.enableKeepAlive = enableKeepAlive;
    }

    public boolean isKeepAliveWithoutCalls() {
        return keepAliveWithoutCalls;
    }

    public void setKeepAliveWithoutCalls(boolean keepAliveWithoutCalls) {
        this.keepAliveWithoutCalls = keepAliveWithoutCalls;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public long getKeepAliveTimeout() {
        return keepAliveTimeout;
    }

    public void setKeepAliveTimeout(long keepAliveTimeout) {
        this.keepAliveTimeout = keepAliveTimeout;
    }

    public int getMaxInboundMessageSize() {
        return maxInboundMessageSize;
    }

    public void setMaxInboundMessageSize(int maxInboundMessageSize) {
        this.maxInboundMessageSize = maxInboundMessageSize;
    }

    public boolean isFullStreamDecompression() {
        return fullStreamDecompression;
    }

    public void setFullStreamDecompression(boolean fullStreamDecompression) {
        this.fullStreamDecompression = fullStreamDecompression;
    }

    public String getNegotiationType() {
        return negotiationType;
    }

    public void setNegotiationType(String negotiationType) {
        this.negotiationType = negotiationType;
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
}
