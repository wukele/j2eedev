package com.starit.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

public class ExecutorServiceHelperTest extends TestCase {

    public void testGetThreadName() {
        String name = ExecutorServiceHelper.getThreadName("Camel Thread ${counter} - ${name}", "foo");

        assertTrue(name.startsWith("Camel Thread"));
        assertTrue(name.endsWith("foo"));
    }

    public void testNewScheduledThreadPool() {
        ScheduledExecutorService pool = ExecutorServiceHelper.newScheduledThreadPool(1, "MyPool ${name}", "foo", true);
        assertNotNull(pool);
    }

    public void testNewThreadPool() {
        ExecutorService pool = ExecutorServiceHelper.newThreadPool("MyPool ${name}", "foo", 1, 1);
        assertNotNull(pool);
    }

    public void testNewThreadPool2() {
        ExecutorService pool = ExecutorServiceHelper.newThreadPool("MyPool ${name}", "foo", 1, 1, 20);
        assertNotNull(pool);
    }

    public void testNewThreadPool3() {
        ExecutorService pool = ExecutorServiceHelper.newThreadPool("MyPool ${name}", "foo", 1, 1,
                30, TimeUnit.SECONDS, 20, null, true);
        assertNotNull(pool);
    }

    public void testNewFixedThreadPool() {
        ExecutorService pool = ExecutorServiceHelper.newFixedThreadPool(1, "MyPool ${name}", "foo", true);
        assertNotNull(pool);
    }

    public void testNewSynchronousThreadPool() {
        ExecutorService pool = ExecutorServiceHelper.newSynchronousThreadPool();
        assertNotNull(pool);
    }

    public void testNewSingleThreadPool() {
        ExecutorService pool = ExecutorServiceHelper.newSingleThreadExecutor("MyPool ${name}", "foo", true);
        assertNotNull(pool);
    }

}