
package com.nxest.grpc.spring.server;

import com.google.common.base.Preconditions;
import com.nxest.grpc.spring.server.configure.GrpcServerBuilderConfigurer;
import com.nxest.grpc.spring.server.configure.GrpcServerProperties;
import io.grpc.*;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.netty.handler.ssl.ClientAuth;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * {@code GrpcServerRunner} configures a grpc {@link Server} with services obtained from the {@link ApplicationContext}
 * and manages that server's lifecycle. Services are discovered by finding {@link GrpcService} implementations that
 * are annotated with {@link GrpcService}.
 */
public class GrpcServerRunner implements AutoCloseable, ApplicationContextAware, DisposableBean {

    private static final Logger logger = Logger.getLogger(GrpcServerRunner.class.getName());

    private ApplicationContext applicationContext;

    private volatile Server server;

    private final GrpcServerBuilderConfigurer serverBuilderConfigurer;

    private final GrpcServerProperties grpcServerProperties;

    public GrpcServerRunner() {
        this.serverBuilderConfigurer = new GrpcServerBuilderConfigurer();
        this.grpcServerProperties = GrpcServerProperties.DEFAULT;
    }

    public GrpcServerRunner(GrpcServerProperties grpcServerProperties) {
        this(new GrpcServerBuilderConfigurer(), grpcServerProperties);
    }

    public GrpcServerRunner(GrpcServerBuilderConfigurer serverBuilderConfigurer, GrpcServerProperties grpcServerProperties) {
        this.serverBuilderConfigurer = Preconditions.checkNotNull(serverBuilderConfigurer);
        this.grpcServerProperties = Preconditions.checkNotNull(grpcServerProperties);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = Preconditions.checkNotNull(applicationContext);
    }


    public void start() throws Exception {
        logger.info("Starting grpc Server ...");

        int port = grpcServerProperties.getPort();

        ServerBuilder serverBuilder = ServerBuilder.forPort(port);

        //server global interceptors
        Stream<ServerInterceptor> serverInterceptors = getServerInterceptorsWithAnnotation();

        // find and register all GRpcService-enabled beans and bind interceptors
        Map<String, Object> services = getServicesWithAnnotation();

        for (Map.Entry<String, Object> service : services.entrySet()) {
            BindableService srv = (BindableService) service.getValue();
            GrpcService serviceAnn = applicationContext.findAnnotationOnBean(service.getKey(), GrpcService.class);
            bindInterceptors(srv, serviceAnn, serverInterceptors);
            serverBuilder.addService(srv);
            logger.info(format("Grpc service %s has been registered.", srv.getClass().getName()));
        }

        //TODO:SSL/TLS supports

        //custom server configure
        serverBuilderConfigurer.configure(serverBuilder);
        server = serverBuilder.build();

        //start
        server.start();
        applicationContext.publishEvent(new GrpcServerInitializedEvent(server));
        logger.info(format("Grpc server started, listening on port %s.", port));

        //wait for stop
        blockUntilShutdown();
    }

    private Map<String, Object> getServicesWithAnnotation() {
        Map<String, Object> possibleServices = applicationContext.getBeansWithAnnotation(GrpcService.class);

        Collection<String> invalidServiceNames = possibleServices.entrySet().stream()
            .filter(e -> !(e.getValue() instanceof BindableService))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        if (!invalidServiceNames.isEmpty()) {
            throw new IllegalStateException((format(
                "The following beans are annotated with @GrpcService, but are not BindableServices: %s",
                String.join(", ", invalidServiceNames))));
        }
        return possibleServices;
    }


    private void bindInterceptors(BindableService serviceDefinition, GrpcService serviceAnn, Stream<ServerInterceptor> globalInterceptors) {
        Stream<? extends ServerInterceptor> privateInterceptors = Stream.of(serviceAnn.interceptors())
            .map(interceptorClass -> {
                try {
                    return 0 < applicationContext.getBeanNamesForType(interceptorClass).length ?
                        applicationContext.getBean(interceptorClass) :
                        interceptorClass.newInstance();
                } catch (Exception e) {
                    throw new BeanCreationException("Failed to create interceptor instance.", e);
                }
            });

        Stream<ServerInterceptor> global = serviceAnn.applyGlobalInterceptors() ? globalInterceptors : Stream.empty();
        List<ServerInterceptor> interceptors = Stream.concat(global, privateInterceptors)
            .distinct()
            .sorted(AnnotationAwareOrderComparator.INSTANCE) ////Get service order and sort, support for spring @Order
            .collect(Collectors.toList());

        ServerInterceptors.intercept(serviceDefinition, interceptors);

        List<String> interceptorNames = interceptors.stream().map(s -> s.getClass().getName()).collect(Collectors.toList());

        logger.info(format("Grpc service %s bind interceptors: %s.", serviceDefinition.getClass().getName(), String.join(", ", interceptorNames)));
    }

    private SslContextBuilder getSslContextBuilder() {
        //        Init SSL/TLS
//        this.certChainFilePath = certChainFilePath;
//        this.privateKeyFilePath = privateKeyFilePath;
//        this.trustCertCollectionFilePath = trustCertCollectionFilePath;
//        System.out.println(
//            "USAGE: HelloWorldServerTls host port certChainFilePath privateKeyFilePath " +
//                "[trustCertCollectionFilePath]\n  Note: You only need to supply trustCertCollectionFilePath if you want " +
//                "to enable Mutual TLS.");
        String certChainFilePath = "certChainFilePath";
        String privateKeyFilePath = "privateKeyFilePath";
        String trustCertCollectionFilePath = "trustCertCollectionFilePath";
        SslContextBuilder sslClientContextBuilder = SslContextBuilder.forServer(new File(certChainFilePath),
            new File(privateKeyFilePath));
        if (trustCertCollectionFilePath != null) {
            sslClientContextBuilder.trustManager(new File(trustCertCollectionFilePath));
            sslClientContextBuilder.clientAuth(ClientAuth.REQUIRE);
        }
        return GrpcSslContexts.configure(sslClientContextBuilder, SslProvider.OPENSSL);
    }

    private void blockUntilShutdown() {
        Thread awaitThread = new Thread(() -> {
            try {
                GrpcServerRunner.this.server.awaitTermination();
            } catch (InterruptedException e) {
                logger.warning("Grpc server stopped." + e);
            }
        });
        awaitThread.setDaemon(false);
        awaitThread.start();
    }


    private Stream<ServerInterceptor> getServerInterceptorsWithAnnotation() {
        Map<String, Object> possibleInterceptors = applicationContext.getBeansWithAnnotation(GrpcServerInterceptor.class);

        Collection<String> invalidInterceptors = possibleInterceptors.entrySet().stream()
            .filter(e -> !(e.getValue() instanceof ServerInterceptor))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        if (!invalidInterceptors.isEmpty()) {
            throw new IllegalStateException((format(
                "The following beans are annotated with @GrpcServerInterceptor, but are not ServerInterceptor: %s",
                String.join(", ", invalidInterceptors))));
        }
        return possibleInterceptors.values().stream()
            .map(s -> (ServerInterceptor) s);
    }

    /**
     * Shutdown the grpc {@link Server} when this object is closed.
     */
    @Override
    public void close() {
        if (server != null) {

            logger.info("Shutting down grpc server ...");

            server.shutdown();

            try {
                server.awaitTermination(grpcServerProperties.getShutdownDelayMillis(), TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                logger.info("Grpc server stopped." + e);
            } finally {
                server.shutdownNow();
                this.server = null;
            }
            logger.info("Grpc server stopped.");
        }
    }

    /**
     * Shutdown the grpc {@link Server} when this object is closed.
     */
    @Override
    public void destroy() {
        this.close();
    }
}
