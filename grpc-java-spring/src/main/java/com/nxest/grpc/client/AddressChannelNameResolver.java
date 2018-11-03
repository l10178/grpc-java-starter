package com.nxest.grpc.client;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.nxest.grpc.client.configure.GrpcClientProperties;
import io.grpc.Attributes;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import io.grpc.internal.SharedResourceHolder;

import javax.annotation.concurrent.GuardedBy;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;


public class AddressChannelNameResolver extends NameResolver {
    private static final Logger logger = Logger.getLogger(AddressChannelNameResolver.class.getName());

    private final String name;
    private final GrpcClientProperties properties;

    private final Attributes attributes;

    private final SharedResourceHolder.Resource<ExecutorService> executorResource;
    @GuardedBy("this")
    private boolean shutdown;
    @GuardedBy("this")
    private ExecutorService executor;
    @GuardedBy("this")
    private boolean resolving;
    @GuardedBy("this")
    private Listener listener;

    public AddressChannelNameResolver(String name, GrpcClientProperties properties, Attributes attributes, SharedResourceHolder.Resource<ExecutorService> executorResource) {
        this.name = name;
        this.properties = properties;
        this.attributes = attributes;
        this.executorResource = executorResource;
    }

    @Override
    public String getServiceAuthority() {
        return name;
    }

    @Override
    public final synchronized void start(Listener listener) {
        Preconditions.checkState(this.listener == null, "already started");
        executor = SharedResourceHolder.get(executorResource);
        this.listener = Preconditions.checkNotNull(listener, "listener");
        resolve();
    }

    @Override
    public final synchronized void refresh() {
        Preconditions.checkState(listener != null, "not started");
        resolve();
    }

    private final Runnable resolutionRunnable = new Runnable() {
        @Override
        public void run() {
            Listener savedListener;
            synchronized (AddressChannelNameResolver.this) {
                if (shutdown) {
                    return;
                }
                savedListener = listener;
                resolving = true;
            }
            try {
                List<EquivalentAddressGroup> equivalentAddressGroups = Lists.newArrayList();
                String host = properties.getHost();
                Integer port = properties.getPort();
                logger.info(String.format("Found gRPC server %s %s:%s", name, host, port));
                EquivalentAddressGroup addressGroup = new EquivalentAddressGroup(new InetSocketAddress(host, port), Attributes.EMPTY);
                equivalentAddressGroups.add(addressGroup);

                savedListener.onAddresses(equivalentAddressGroups, Attributes.EMPTY);
            } finally {
                synchronized (AddressChannelNameResolver.this) {
                    resolving = false;
                }
            }
        }
    };

    @GuardedBy("this")
    private void resolve() {
        if (resolving || shutdown) {
            return;
        }
        executor.execute(resolutionRunnable);
    }

    @Override
    public void shutdown() {
        if (shutdown) {
            return;
        }
        shutdown = true;
        if (executor != null) {
            executor = SharedResourceHolder.release(executorResource, executor);
        }
    }
}
