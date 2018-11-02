
package com.nxest.grpc.spring.server;

import com.nxest.grpc.spring.server.configure.GrpcServerProperties;
import io.grpc.Server;
import org.springframework.beans.factory.DisposableBean;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * {@code GrpcServer} extends grpc {@link Server}
 */
public class GrpcServer extends Server implements AutoCloseable, DisposableBean {

    private static final Logger logger = Logger.getLogger(GrpcServer.class.getName());

    private final Server server;
    private final GrpcServerProperties properties;

    public GrpcServer(Server server, GrpcServerProperties properties) {
        this.server = server;
        this.properties = properties;
    }

    @Override
    public Server start() {
        logger.info("Starting grpc Server ...");
        //start server
        try {
            server.start();
            // This can return -1 if there is no actual port or the result otherwise does not make sense.
            int port = server.getPort();
            logger.info(format("Grpc server started, listening on port %s.", port));
        } catch (Exception e) {
            logger.warning("Start server failed.");
            throw new RuntimeException("Start server failed.", e);
        }

        //wait for stop
        blockUntilShutdown();
        return server;
    }


    @Override
    public Server shutdown() {
        return server.shutdown();
    }

    @Override
    public Server shutdownNow() {
        return server.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return server.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return server.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return server.awaitTermination(timeout, unit);
    }

    @Override
    public void awaitTermination() throws InterruptedException {
        server.awaitTermination();
    }

    private void blockUntilShutdown() {
        Thread awaitThread = new Thread(() -> {
            try {
                GrpcServer.this.server.awaitTermination();
            } catch (InterruptedException e) {
                logger.warning("Grpc server stopped." + e);
            }
        });
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    /**
     * Gets the address the created server will be bound to.
     *
     * @return The address the server will be bound to.
     */
    public String getAddress() {
        return properties.getAddress();
    }


    /**
     * Returns the port number the server is listening on.  This can return -1 if there is no actual
     * port or the result otherwise does not make sense.  Result is undefined after the server is
     * terminated.
     *
     * @throws IllegalStateException if the server has not yet been started.
     */
    public int getPort() {
        return server.getPort();
    }

    /**
     * Shutdown the grpc {@link Server}
     */
    @Override
    public void close() {
        logger.info("Shutting down grpc server ...");
        this.shutdown();
        try {
            this.awaitTermination(properties.getShutdownDelayMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.info("Grpc server stopped." + e);
        } finally {
            this.shutdownNow();
        }
        logger.info("Grpc server stopped.");
    }

    /**
     * Shutdown the grpc {@link Server}
     */
    @Override
    public void destroy() {
        this.close();
    }
}
