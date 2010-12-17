package com.starit.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A {@link java.util.concurrent.CompletionService} that orders the completed tasks
 * in the same order as they where submitted.
 *
 * @version $Revision: 937935 $
 */
public class SubmitOrderedCompletionService<V> implements CompletionService<V> {
    
    private final Executor executor;

    // the idea to order the completed task in the same order as they where submitted is to leverage
    // the delay queue. With the delay queue we can control the order by the getDelay and compareTo methods
    // where we can order the tasks in the same order as they where submitted.
    private final DelayQueue<SubmitOrderFutureTask> completionQueue = new DelayQueue<SubmitOrderFutureTask>();

    // id is the unique id that determines the order in which tasks was submitted (incrementing)
    private final AtomicInteger id = new AtomicInteger();
    // index is the index of the next id that should expire and thus be ready to take from the delayed queue
    private final AtomicInteger index = new AtomicInteger();

    private class SubmitOrderFutureTask extends FutureTask<V> implements Delayed {

        // the id this task was assigned
        private final long id;

        public SubmitOrderFutureTask(long id, Callable<V> voidCallable) {
            super(voidCallable);
            this.id = id;
        }

        public SubmitOrderFutureTask(long id, Runnable runnable, V result) {
            super(runnable, result);
            this.id = id;
        }

        public long getDelay(TimeUnit unit) {
            // if the answer is 0 then this task is ready to be taken
            return id - index.get();
        }
        
        public int compareTo(Delayed o) {
            SubmitOrderFutureTask other = (SubmitOrderFutureTask) o;
            return (int) (this.id - other.id);
        }

        @Override
        protected void done() {
            // when we are done add to the completion queue
            completionQueue.add(this);
        }
    }

    public SubmitOrderedCompletionService(Executor executor) {
        this.executor = executor;
    }

    @SuppressWarnings("unchecked")
    public Future<V> submit(Callable task) {
        if (task == null) {
            throw new IllegalArgumentException("Task must be provided");
        }
        SubmitOrderFutureTask f = new SubmitOrderFutureTask(id.incrementAndGet(), task);
        executor.execute(f);
        return (Future<V>) f;
    }

    public Future<V> submit(Runnable task, Object result) {
        if (task == null) {
            throw new IllegalArgumentException("Task must be provided");
        }
        SubmitOrderFutureTask f = new SubmitOrderFutureTask(id.incrementAndGet(), task, null);
        executor.execute(f);
        return f;
    }

    @SuppressWarnings("unchecked")
    public Future<V> take() throws InterruptedException {
        index.incrementAndGet();
        return (Future) completionQueue.take();
    }

    @SuppressWarnings("unchecked")
    public Future<V> poll() {
        index.incrementAndGet();
        return (Future) completionQueue.poll();
    }

    @SuppressWarnings("unchecked")
    public Future<V> poll(long timeout, TimeUnit unit) throws InterruptedException {
        index.incrementAndGet();
        return (Future) completionQueue.poll(timeout, unit);
    }
}