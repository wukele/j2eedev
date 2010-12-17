package com.starit.concurrent;

import java.util.concurrent.ExecutorService;

import junit.framework.TestCase;

public class SynchronousExecutorServiceTest extends TestCase {

    private static boolean invoked;
    private static String name1;
    private static String name2;

    public void testSynchronousExecutorService() throws Exception {
        name1 = Thread.currentThread().getName();

        ExecutorService service = new SynchronousExecutorService();
        service.execute(new Runnable() {
            public void run() {
                invoked = true;
                name2 = Thread.currentThread().getName();
            }
        });

        assertTrue("Should have been invoked", invoked);
        assertEquals("Should use same thread", name1, name2);
    }

    public void testSynchronousExecutorServiceShutdown() throws Exception {
        ExecutorService service = new SynchronousExecutorService();
        service.execute(new Runnable() {
            public void run() {
                invoked = true;
            }
        });
        service.shutdown();

        assertTrue(service.isShutdown());
        assertTrue(service.isTerminated());
    }
}