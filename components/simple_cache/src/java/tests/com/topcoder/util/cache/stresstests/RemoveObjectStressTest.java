/*
 * RemoveObjectStressTest.java
 *
 * Copyright (c) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.cache.stresstests;

import com.topcoder.util.cache.SimpleCache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Stress tests for removing objects from the SimpleCache.
 *
 * @author colau
 * @version 2.0
 */
public class RemoveObjectStressTest extends TestCase {
    /**
     * The number of threads to start.
     */
    public static final int THREAD_COUNT = 10;

    /**
     * The number of loops in the thread.
     */
    public static final int THREAD_LOOP_COUNT = 1000;

    /**
     * The SimpleCache instance.
     */
    private SimpleCache cache = null;

    /**
     * Creates a test suite for the tests in this test case.
     *
     * @return a TestSuite for this test case
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(RemoveObjectStressTest.class);

        return suite;
    }

    /**
     * Removes objects from the cache with no compression.
     *
     * @throws Exception to JUnit
     */
    public void testRemove_NoCompress() throws Exception {
        removeObjects(false);
    }

    /**
     * Removes objects from the cache with compression.
     *
     * @throws Exception to JUnit
     */
    public void testRemove_Compress() throws Exception {
        removeObjects(true);
    }

    /**
     * Removes objects from the cache with the specified compression flag.
     *
     * @param compressionFlag defines if compression is on or off
     *
     * @throws Exception to JUnit
     */
    private void removeObjects(boolean compressionFlag)
        throws Exception
    {
        cache = new SimpleCache();
        cache.setCompressionFlag(compressionFlag);

        // put some objects first
        for (int i = 0; i < (THREAD_COUNT * THREAD_LOOP_COUNT); i++) {
            cache.put("key" + i, "value" + i);
        }

        Thread[] threads = new TestThread[THREAD_COUNT];

        // start the threads
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new TestThread(i * THREAD_LOOP_COUNT);

            threads[i].start();
        }

        // wait for all threads to stop
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        assertTrue("Fails to remove objects", cache.keySet().isEmpty());
    }

    /**
     * The thread class to remove objects.
     */
    private class TestThread extends Thread {
        /**
         * The starting index of the key.
         */
        private int start = 0;

        /**
         * Constructor with the specified starting index.
         *
         * @param start starting index of the key
         */
        public TestThread(int start) {
            this.start = start;
        }

        /**
         * Runs this thread by removing objects from the cache.
         */
        public void run() {
            for (int i = 0; i < THREAD_LOOP_COUNT; i++) {
                int index = start + i;

                cache.remove("key" + index);
            }
        }
    }
}
