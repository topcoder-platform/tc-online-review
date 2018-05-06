/*
 * Copyright (c) 2002-2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case tests the cache's behavior when accessed
 * by multiple threads at once. It confirms that the cache is
 * thread-safe.</p>
 *
 * @author  srowen, rem
 * @version 1.0
 */
public class ConcurrencyTestCase extends TestCase {

    /**
     * Number of operations with cache.
     */
    private static final int TEST_CACHE_OPERATIONS = 250000;

    /**
     * Instance to test.
     */
    private Cache cache = new SimpleCache();

    /**
     * Test the cache's behavior across multiple threads.
     *
     * @throws Throwable if any error occurs.
     */
    public void testConcurrency() throws Throwable {
        // no exceptions should be thrown

        int numThreads = 8;
        // Kick off some threads... some read, some write.
        // Some use key "foo", others "bar"
        TestThread[] threads = new TestThread[numThreads];
        for (int i = 0; i < numThreads / 2; i++) {
            threads[i] = new TestThread(i % 2 == 0, "foo");
            threads[i].start();
        }
        for (int i = numThreads / 2; i < numThreads; i++) {
            threads[i] = new TestThread(i % 2 == 0, "bar");
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
        }

        for (int i = 0; i < numThreads; i++) {
            Throwable t = threads[i].getThrowable();
            if (t != null) {
                fail("Exception was thrown: " + t.getMessage());
            }
        }
    }

    /**
     * Runs tests.
     *
     * @return TestSuite representing this class.
     */
    public static Test suite() {
        return new TestSuite(ConcurrencyTestCase.class);
    }

    /**
     * Helper test thread for concurrency.
     */
    private class TestThread extends Thread {

        private boolean readOnly;
        private String key;
        private Throwable throwable;

        public TestThread(boolean readOnly, String key) {
            this.readOnly = readOnly;
            this.key = key;
        }

        public void run() {
            try {
                for (int i = 0; i < TEST_CACHE_OPERATIONS; i++) {
                    if (readOnly) {
                        cache.get(key);
                    } else {
                        cache.put(key, "baz");
                    }
                }
            } catch (Throwable t) {
                throwable = t;
            }
        }

        public Throwable getThrowable() {
            return this.throwable;
        }
    }
}
