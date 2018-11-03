package com.nxest.grpc.server.executor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;

/**
 * A handler for rejected tasks that silently discards the
 * rejected task.
 */
public class GrpcDiscardPolicy implements RejectedExecutionHandler {

    private static final Logger logger = Logger.getLogger(GrpcDiscardPolicy.class.getName());

    /**
     * Creates a {@code DiscardPolicy}.
     */
    public GrpcDiscardPolicy() {
    }

    /**
     * Does nothing, which has the effect of discarding task r.
     *
     * @param r the runnable task requested to be executed
     * @param e the executor attempting to execute this task
     */
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        logger.warning(String.format("Grpc server queue is full. %s rejected, %s", String.valueOf(r), String.valueOf(e)));
    }
}
