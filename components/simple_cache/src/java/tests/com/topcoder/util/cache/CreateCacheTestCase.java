/*
 * Copyright (c) 2002-2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case tests the SimpleCache constructors.</p>
 *
 * @author  srowen, rem
 * @version 2.0
 * @since   1.0
 */
public class CreateCacheTestCase extends TestCase {

    /**
     * Tests creation of the cache.
     */
    public void testCreateCache() {
        SimpleCache simpleCache = new SimpleCache();
        assertEquals("Max size should default to no max",
                     SimpleCache.NO_MAX_SIZE,
                     simpleCache.getMaxCacheSize());
        assertEquals("Timeout should default to no timeout",
                     SimpleCache.NO_TIMEOUT,
                     simpleCache.getTimeoutMS());

        simpleCache =
            new SimpleCache(10000, 24 * 60 * 60 * 1000, new FIFOCacheEvictionStrategy(),
                    SimpleCache.NO_MAX_CAPACITY, null, null, false);
        assertEquals("Max size should match given size",
                     10000,
                     simpleCache.getMaxCacheSize());
        assertEquals("Timeout should match given timeout",
                     24 * 60 * 60 * 1000,
                     simpleCache.getTimeoutMS());
    }
    /**
     * Test the illegal arguments.
     */
    public void testIllegalConstructorArgs() {

        // should throw an IllegalArgumentException - see spec
        try {
            new SimpleCache(0, 1000, new LRUCacheEvictionStrategy(),
                    SimpleCache.NO_MAX_CAPACITY, null, null, false);
            fail("Nonpositive max size should cause an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // good
        }
        try {
            new SimpleCache(10000, 0, new LRUCacheEvictionStrategy(),
                    SimpleCache.NO_MAX_CAPACITY, null, null, false);
            fail("Nonpositive timeout should cause an IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // good
        }
        new SimpleCache(10000, 1000, null,
                SimpleCache.NO_MAX_CAPACITY, null, null, false);
    }

    /**
     * Suite to run test cases.
     *
     * @return TestSuite representing this class.
     */
    public static Test suite() {
        return new TestSuite(CreateCacheTestCase.class);
    }
}
