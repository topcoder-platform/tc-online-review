/*
 * Copyright (c) 2002-2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This test case tests the CacheEvictionStrategy implementations.
 * Implementations have some leeway in their behavior (see Component
 * Design doc), so these tests stick to correctness of core
 * behavior.
 *
 * @author  srowen, rem
 * @version 2.0
 * @since   1.0
 */
public class CacheEvictionStrategyTestCase extends TestCase {

    /**
     * Tests the first in first out eviction strategy.
     */
    public void testFIFO() {
        FIFOCacheEvictionStrategy strategy =
            new FIFOCacheEvictionStrategy();
        strategy.notifyOfCachePut("foo");
        strategy.notifyOfCachePut("bar");
        strategy.notifyOfCachePut("baz");
        // other stuff...
        strategy.notifyOfCacheGet("bar");
        strategy.notifyOfCacheGet("baz");
        // check order
        assertEquals("Keys should be returned in FIFO order",
                     "foo",
                     strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in FIFO order",
                     "bar",
                     strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in FIFO order",
                     "baz",
                     strategy.getNextKeyToEvict());
    }

    /**
     * Tests the least recently used eviction strategy.
     */
    public void testLRU() {
        LRUCacheEvictionStrategy strategy =
            new LRUCacheEvictionStrategy();
        strategy.notifyOfCachePut("foo");
        strategy.notifyOfCachePut("bar");
        strategy.notifyOfCachePut("baz");
        // other stuff...
        strategy.notifyOfCacheGet("bar");
        strategy.notifyOfCacheGet("baz");
        // check order
        assertEquals("Keys should be returned in LRU order",
                     "foo",
                     strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in LRU order",
                     "bar",
                     strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in LRU order",
                     "baz",
                     strategy.getNextKeyToEvict());
    }

    /**
     * Runs the test.
     *
     * @return TestSuite representing this class.
     */
    public static Test suite() {
        return new TestSuite(CacheEvictionStrategyTestCase.class);
    }
}
