/*
 * PutObjectStressTest.java
 *
 * Copyright (c) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.cache.stresstests;

import com.topcoder.util.cache.BOFCacheEvictionStrategy;
import com.topcoder.util.cache.CacheEvictionStrategy;
import com.topcoder.util.cache.FIFOCacheEvictionStrategy;
import com.topcoder.util.cache.LRUCacheEvictionStrategy;
import com.topcoder.util.cache.SimpleCache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Stress tests for putting objects into the SimpleCache.
 *
 * @author colau
 * @version 2.0
 */
public class PutObjectStressTest extends TestCase {
    /**
     * The number of threads to start.
     */
    public static final int THREAD_COUNT = 10;

    /**
     * The number of loops in the thread.
     */
    public static final int THREAD_LOOP_COUNT = 1000;

    /**
     * The maximum limiting cache size.
     */
    public static final int MAX_CACHE_SIZE = 5000;

    /**
     * The maximum limiting cache capacity.
     */
    public static final int MAX_CACHE_CAPACITY = 5000;

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
        TestSuite suite = new TestSuite(PutObjectStressTest.class);

        return suite;
    }

    /**
     * Puts objects into the cache limited by cache size, using FIFO and no compression.
     *
     * @throws Exception to JUnit
     */
    public void testPut_FIFO_CacheSize_NoCompress() throws Exception {
        putObjects(MAX_CACHE_SIZE, Integer.MAX_VALUE, new FIFOCacheEvictionStrategy(), false);
    }

    /**
     * Puts objects into the cache limited by cache capacity, using FIFO and no compression.
     *
     * @throws Exception to JUnit
     */
    public void testPut_FIFO_CacheCapacity_NoCompress()
        throws Exception
    {
        putObjects(Integer.MAX_VALUE, MAX_CACHE_CAPACITY, new FIFOCacheEvictionStrategy(), false);
    }

    /**
     * Puts objects into the cache limited by cache size, using FIFO and compression.
     *
     * @throws Exception to JUnit
     */
    public void testPut_FIFO_CacheSize_Compress() throws Exception {
        putObjects(MAX_CACHE_SIZE, Integer.MAX_VALUE, new FIFOCacheEvictionStrategy(), true);
    }

    /**
     * Puts objects into the cache limited by cache capacity, using FIFO and compression.
     *
     * @throws Exception to JUnit
     */
    public void testPut_FIFO_CacheCapacity_Compress() throws Exception {
        putObjects(Integer.MAX_VALUE, MAX_CACHE_CAPACITY, new FIFOCacheEvictionStrategy(), true);
    }

    /**
     * Puts objects into the cache limited by cache size, using LRU and no compression.
     *
     * @throws Exception to JUnit
     */
    public void testPut_LRU_CacheSize_NoCompress() throws Exception {
        putObjects(MAX_CACHE_SIZE, Integer.MAX_VALUE, new LRUCacheEvictionStrategy(), false);
    }

    /**
     * Puts objects into the cache limited by cache capacity, using LRU and no compression.
     *
     * @throws Exception to JUnit
     */
    public void testPut_LRU_CacheCapacity_NoCompress()
        throws Exception
    {
        putObjects(Integer.MAX_VALUE, MAX_CACHE_CAPACITY, new LRUCacheEvictionStrategy(), false);
    }

    /**
     * Puts objects into the cache limited by cache size, using LRU and compression.
     *
     * @throws Exception to JUnit
     */
    public void testPut_LRU_CacheSize_Compress() throws Exception {
        putObjects(MAX_CACHE_SIZE, Integer.MAX_VALUE, new LRUCacheEvictionStrategy(), true);
    }

    /**
     * Puts objects into the cache limited by cache capacity, using LRU and compression.
     *
     * @throws Exception to JUnit
     */
    public void testPut_LRU_CacheCapacity_Compress() throws Exception {
        putObjects(Integer.MAX_VALUE, MAX_CACHE_CAPACITY, new LRUCacheEvictionStrategy(), true);
    }

    /**
     * Puts objects into the cache limited by cache size, using BOF and no compression.
     *
     * @throws Exception to JUnit
     */
    public void testPut_BOF_CacheSize_NoCompress() throws Exception {
        putObjects(MAX_CACHE_SIZE, Integer.MAX_VALUE, new BOFCacheEvictionStrategy(), false);
    }

    /**
     * Puts objects into the cache limited by cache capacity, using BOF and no compression.
     *
     * @throws Exception to JUnit
     */
    public void testPut_BOF_CacheCapacity_NoCompress()
        throws Exception
    {
        putObjects(Integer.MAX_VALUE, MAX_CACHE_CAPACITY, new BOFCacheEvictionStrategy(), false);
    }

    /**
     * Puts objects into the cache limited by cache size, using BOF and compression.
     *
     * @throws Exception to JUnit
     */
    public void testPut_BOF_CacheSize_Compress() throws Exception {
        putObjects(MAX_CACHE_SIZE, Integer.MAX_VALUE, new BOFCacheEvictionStrategy(), true);
    }

    /**
     * Puts objects into the cache limited by cache capacity, using BOF and compression.
     *
     * @throws Exception to JUnit
     */
    public void testPut_BOF_CacheCapacity_Compress() throws Exception {
        putObjects(Integer.MAX_VALUE, MAX_CACHE_CAPACITY, new BOFCacheEvictionStrategy(), true);
    }

    /**
     * Puts objects into the cache with the specified parameters by running some threads.
     *
     * @param maxCacheSize maximum cache size in terms of number of entries
     * @param maxCacheCapacity maximum cache size in terms of bytes of memory
     * @param evictionStrategy eviction strategy to be used
     * @param compressionFlag defines if compression is on or off
     *
     * @throws Exception to JUnit
     */
    private void putObjects(int maxCacheSize, int maxCacheCapacity, CacheEvictionStrategy evictionStrategy,
        boolean compressionFlag) throws Exception
    {
        cache = new SimpleCache(maxCacheSize, SimpleCache.NO_TIMEOUT, evictionStrategy, maxCacheCapacity, null, null,
                compressionFlag);

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

        assertTrue("Exceeds cache size", cache.getSize() <= maxCacheSize);
        assertTrue("Exceeds cache capacity", cache.getByteSize() <= maxCacheCapacity);
    }

    /**
     * The thread class to put objects.
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
         * Runs this thread by putting objects into the cache.
         */
        public void run() {
            for (int i = 0; i < THREAD_LOOP_COUNT; i++) {
                int index = start + i;

                // put the object
                cache.put("key" + index, "value" + index);

                // attempt to access the object
                cache.get("key" + index);
            }
        }
    }
}
