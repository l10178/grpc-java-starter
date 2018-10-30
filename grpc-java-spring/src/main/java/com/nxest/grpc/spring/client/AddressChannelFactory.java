package com.nxest.grpc.spring.client;

import com.google.common.base.Strings;
import com.nxest.grpc.spring.client.configure.GrpcClientProperties;
import io.grpc.*;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.ResourceUtils;

import javax.net.ssl.SSLException;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

public class AddressChannelFactory implements GrpcChannelFactory {

    private static final Logger logger = Logger.getLogger(AddressChannelFactory.class.getName());

    private final GrpcClientProperties properties;
    private final LoadBalancer.Factory loadBalancerFactory;
    private final NameResolver.Factory nameResolverFactory;


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
    public Channel createChannel(String name, List<ClientInterceptor> interceptors) {
        NettyChannelBuilder builder = NettyChannelBuilder.forTarget(name)
            .loadBalancerFactory(loadBalancerFactory)
            .nameResolverFactory(nameResolverFactory);
        initSsl(builder);
        if (properties.isEnableKeepAlive()) {
            builder.keepAliveWithoutCalls(properties.isKeepAliveWithoutCalls())
                .keepAliveTime(properties.getKeepAliveTime(), TimeUnit.SECONDS)
                .keepAliveTimeout(properties.getKeepAliveTimeout(), TimeUnit.SECONDS);
        }
        if (properties.getMaxInboundMessageSize() > 0) {
            builder.maxInboundMessageSize(properties.getMaxInboundMessageSize());
        }
        if (properties.isFullStreamDecompression()) {
            builder.enableFullStreamDecompression();
        }
        Channel channel = builder.build();

        //distinct and sort
        Stream<ClientInterceptor> stream = (interceptors == null ? Stream.empty() : interceptors.stream());

        List<ClientInterceptor> interceptorSet = stream
            .distinct()
            .sorted(AnnotationAwareOrderComparator.INSTANCE)
            .collect(Collectors.toList());
        return ClientInterceptors.intercept(channel, interceptorSet);
    }

    private void initSsl(NettyChannelBuilder builder) {
        try {
            String trustCertCollectionFile = properties.getTrustCertCollectionFile();
            String certChainFile = properties.getCertChainFile();
            String privateKeyFile = properties.getPrivateKeyFile();
            logger.info(format("Grpc client SSL/TLS trustCertCollectionFile is %s.", trustCertCollectionFile));
            logger.info(format("Grpc client SSL/TLS certChainFile is %s.", certChainFile));
            logger.info(format("Grpc client SSL/TLS privateKeyFile is %s.", certChainFile));
            SslContextBuilder sslContextBuilder = GrpcSslContexts.forClient();

            if (trustCertCollectionFile != null) {
                sslContextBuilder.trustManager(ResourceUtils.getFile(trustCertCollectionFile));
            } else {
                sslContextBuilder.trustManager(InsecureTrustManagerFactory.INSTANCE);
            }

            /*
             *Note: certChainFile and privateKeyFile are only needed if mutual auth is desired.
             *And if you specify certChainFile you must also specify privateKeyFile
             */
            if (certChainFile != null && privateKeyFile != null) {
                sslContextBuilder.keyManager(ResourceUtils.getFile(certChainFile), ResourceUtils.getFile(privateKeyFile));
            }

            builder.sslContext(sslContextBuilder.build());
            builder.negotiationType(negotiationType());
        } catch (SSLException | FileNotFoundException e) {
            logger.warning("Failed init grpc client SSL/TLS." + e);
            throw new RuntimeException("Failed to init grpc server SSL/TLS.", e);
        }
    }

    private NegotiationType negotiationType() {
        String negotiationType = properties.getNegotiationType();
        if (Strings.isNullOrEmpty(negotiationType)) {
            return NegotiationType.TLS;
        }
        return NegotiationType.valueOf(negotiationType.toUpperCase());
    }

}
