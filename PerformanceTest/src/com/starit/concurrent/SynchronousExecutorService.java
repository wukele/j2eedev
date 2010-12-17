package com.starit.concurrent;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A synchronous {@link java.util.concurrent.ExecutorService} which always invokes
 * the task in the caller thread (just a thread pool facade).
 * <p/>
 * There is no task queue, and no thread pool. The task will thus always be executed
 * by the caller thread in a fully synchronous method invocation.
 * <p/>
 * This implementation is very simple and does not support waiting for tasks to complete during shutdown.
 *
 * @version $Revision$
 */
public class SynchronousExecutorService extends AbstractExecutorService {

    private volatile boolean shutdown;

    public void shutdown() {
        shutdown = true;
    }

    public List<Runnable> shutdownNow() {
        // not implemented
        return null;
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public boolean isTerminated() {
        return shutdown;
    }

    public boolean awaitTermination(long time, TimeUnit unit) throws InterruptedException {
        // not implemented
        return true;
    }

    public void execute(Runnable runnable) {
        // run the task synchronously
        runnable.run();
    }

}