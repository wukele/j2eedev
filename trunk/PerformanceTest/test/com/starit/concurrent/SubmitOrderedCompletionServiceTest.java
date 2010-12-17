package com.starit.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import junit.framework.TestCase;

/**
 * @version $Revision: 834787 $
 */
public class SubmitOrderedCompletionServiceTest extends TestCase {

    private ExecutorService executor;
    private SubmitOrderedCompletionService<Object> service;

    @Override
    protected void setUp() throws Exception {
        executor = Executors.newFixedThreadPool(5);
        service = new SubmitOrderedCompletionService<Object>(executor);
    }

    @Override
    protected void tearDown() throws Exception {
        executor.shutdownNow();
    }

    public void testSubmitOrdered() throws Exception {

        service.submit(new Callable<Object>() {
            public Object call() throws Exception {
                return "A";
            }
        });

        service.submit(new Callable<Object>() {
            public Object call() throws Exception {
                return "B";
            }
        });

        Object a = service.take().get();
        Object b = service.take().get();

        assertEquals("A", a);
        assertEquals("B", b);
    }

    public void testSubmitOrderedFirstTaskIsSlow() throws Exception {

        service.submit(new Callable<Object>() {
            public Object call() throws Exception {
                // this task should be slower than B but we should still get it first
                Thread.sleep(200);
                return "A";
            }
        });

        service.submit(new Callable<Object>() {
            public Object call() throws Exception {
                return "B";
            }
        });

        Object a = service.take().get();
        Object b = service.take().get();

        assertEquals("A", a);
        assertEquals("B", b);
    }

    public void testSubmitOrderedSecondTaskIsSlow() throws Exception {

        service.submit(new Callable<Object>() {
            public Object call() throws Exception {
                return "A";
            }
        });

        service.submit(new Callable<Object>() {
            public Object call() throws Exception {
                Thread.sleep(200);
                return "B";
            }
        });

        Object a = service.take().get();
        Object b = service.take().get();

        assertEquals("A", a);
        assertEquals("B", b);
    }
}