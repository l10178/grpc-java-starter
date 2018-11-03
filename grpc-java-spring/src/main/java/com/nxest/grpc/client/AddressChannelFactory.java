package com.nxest.grpc.client;

import com.google.common.base.Strings;
import com.google.errorprone.annotations.concurrent.GuardedBy;
import com.nxest.grpc.client.configure.GrpcClientProperties;
import io.grpc.*;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.grpc.util.RoundRobinLoadBalancerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

public class AddressChannelFactory implements GrpcChannelFactory, DisposableBean {

    private static final Logger logger = Logger.getLogger(AddressChannelFactory.class.getName());

    private final GrpcClientProperties properties;
    private final LoadBalancer.Factory loadBalancerFactory;
    private final NameResolver.Factory nameResolverFactory;

    @GuardedBy("this")
    private final Map<String, ManagedChannel> channels = new ConcurrentHashMap<>();

    public AddressChannelFactory() {
        this(GrpcClientProperties.DEFAULT, RoundRobinLoadBalancerFactory.getInstance());
    }

    public AddressChannelFactory(GrpcClientProperties properties) {
        this(properties, RoundRobinLoadBalancerFactory.getInstance());
    }

    public AddressChannelFactory(GrpcClientProperties properties, LoadBalancer.Factory loadBalancerFactory) {
        this.properties = properties;
        this.loadBalancerFactory = loadBalancerFactory;
        this.nameResolverFactory = new AddressChannelResolverFactory(properties);
    }

    @Override
    public Channel createChannel() {
        return this.createChannel(DEFAULT_CHANEL_NAME);
    }

    @Override
    public Channel createChannel(String name) {
        return this.createChannel(name, null);
    }

    @Override
    public Channel createChannel(String name, Collection<ClientInterceptor> interceptors) {

        final Channel channel;
        synchronized (this) {
            channel = this.channels.computeIfAbsent(name, this::newChannel);
        }

        return ClientInterceptors.intercept(channel, sortedInterceptors(interceptors));
    }

    private List<ClientInterceptor> sortedInterceptors(Collection<ClientInterceptor> interceptors) {
        Stream<ClientInterceptor> stream = (interceptors == null ? Stream.empty() : interceptors.stream());
        //distinct and sort
        return stream
            .distinct()
            .sorted(AnnotationAwareOrderComparator.INSTANCE)
            .collect(Collectors.toList());
    }

    private ManagedChannel newChannel(String name) {
        NettyChannelBuilder builder = NettyChannelBuilder.forTarget(name)
            .loadBalancerFactory(loadBalancerFactory)
            .nameResolverFactory(nameResolverFactory);

        configurerSslContext(builder);
        configureKeepAlive(builder);

        configureLimits(builder);
        configureCompression(builder);
        return builder.build();
    }

    private void configureKeepAlive(final NettyChannelBuilder builder) {
        if (properties.isEnableKeepAlive()) {
            builder.keepAliveWithoutCalls(properties.isKeepAliveWithoutCalls())
                .keepAliveTime(properties.getKeepAliveTime(), TimeUnit.SECONDS)
                .keepAliveTimeout(properties.getKeepAliveTimeout(), TimeUnit.SECONDS);
        }
    }

    private void configureCompression(final NettyChannelBuilder builder) {
        if (properties.isFullStreamDecompression()) {
            builder.enableFullStreamDecompression();
        }
    }

    /**
     * Configures limits such as max message sizes that should be used by the channel.
     *
     * @param builder The channel builder to configure.
     */
    private void configureLimits(final NettyChannelBuilder builder) {
        final Integer maxInboundMessageSize = properties.getMaxInboundMessageSize();
        if (maxInboundMessageSize != null) {
            builder.maxInboundMessageSize(maxInboundMessageSize);
        }
    }

    private void configurerSslContext(final NettyChannelBuilder builder) {
        NegotiationType negotiationType = negotiationType();
        builder.negotiationType(negotiationType);

        if (NegotiationType.PLAINTEXT == negotiationType) {
            logger.warning("Grpc client SSL/TLS disabled. NegotiationType is PLAINTEXT. This is not recommended.");
            return;
        }

        try {
            GrpcClientProperties.SecurityProperties security = properties.getSecurity();
            logger.info(format("Grpc client SSL/TLS properties. %s", security));

            String trustCertCollectionFile = security.getTrustCertCollectionFile();
            String certChainFile = security.getCertChainFile();
            String privateKeyFile = security.getPrivateKeyFile();
            SslContextBuilder sslContextBuilder = GrpcSslContexts.forClient();

            if (trustCertCollectionFile != null) {
                sslContextBuilder.trustManager(ResourceUtils.getURL(trustCertCollectionFile).openStream());
            } else {
                sslContextBuilder.trustManager(InsecureTrustManagerFactory.INSTANCE);
            }

            /*
             *Note: certChainFile and privateKeyFile are only needed if mutual auth is desired.
             *And if you specify certChainFile you must also specify privateKeyFile
             */
            if (certChainFile != null && privateKeyFile != null) {
                sslContextBuilder.keyManager(ResourceUtils.getURL(certChainFile).openStream(), ResourceUtils.getURL(privateKeyFile).openStream());
            }

            builder.sslContext(sslContextBuilder.build());

        } catch (IOException e) {
            logger.warning("Failed init grpc client SSL/TLS." + e);
            throw new RuntimeException("Failed to init grpc client SSL/TLS.", e);
        }
    }

    private NegotiationType negotiationType() {
        String negotiationType = properties.getSecurity().getNegotiationType();
        if (Strings.isNullOrEmpty(negotiationType)) {
            return NegotiationType.TLS;
        }
        return NegotiationType.valueOf(negotiationType.toUpperCase());
    }

    @Override
    public void destroy() throws Exception {
        for (ManagedChannel channel : this.channels.values()) {
            shutdown(channel);
        }
        this.channels.clear();
    }

    private void shutdown(ManagedChannel channel) {
        try {
            channel.shutdown();
            channel.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.info("Grpc client channel shutdown." + e);
        }
    }
}
