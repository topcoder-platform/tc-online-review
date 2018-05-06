/*
 * Copyright (c) 2002-2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case tests the cache's <code>get()</code> method,
 * as well as other cache functionality:</p>
 * <ul>
 *  <li>maximum size limit</li>
 *  <li>object timeout</li>
 *  <li>memory management</li>
 * </ul>
 *
 * <p>See the separate <code>ConcurrencyTestCase</code> for tests
 * of multi-threaded behavior.</p>
 *
 * @author  srowen, rem
 * @version 2.0
 * @since   1.0
 */
public class GetObjectFromCacheTestCase extends TestCase {

    /**
     * Tests retrieving objects.
     */
    public void testGet() {
        SimpleCache cache = new SimpleCache();
        assertNull("Cache should return null for non-existent keys",
                   cache.get("foo1"));
        cache.put("foo1", "bar1");
        assertEquals("Cache should return objects that are added",
                     "bar1",
                     cache.get("foo1"));
    }

    /**
     * Verifies the maximum size of the cache.
     */
    public void testMaxSize() {
        SimpleCache cache =
            new SimpleCache(2, SimpleCache.NO_TIMEOUT, new FIFOCacheEvictionStrategy(),
                    SimpleCache.NO_MAX_CAPACITY, null, null, false);
        cache.put("foo1", "bar1");
        cache.put("foo2", "bar2");
        assertEquals("Cache should return objects that are added",
                     "bar1",
                     cache.get("foo1"));
        cache.put("foo3", "bar3");
        assertNull("Cache should evict objects in FIFO order by default",
                   cache.get("foo1"));
        assertEquals("Cache should return objects that are added",
                     "bar2",
                     cache.get("foo2"));
    }

    /**
     * Verifies that objects are removed that have timed out.
     */
    public void testTimeout() {
        SimpleCache cache =
            new SimpleCache(SimpleCache.NO_MAX_SIZE, 1000, new FIFOCacheEvictionStrategy(),
                    SimpleCache.NO_MAX_CAPACITY, null, null, false);
        cache.put("foo1", "bar1");
        assertEquals("Cache should return objects that are added",
                     "bar1",
                     cache.get("foo1"));

        try {
            Thread.sleep(1000 + 5000 + 500); // thread cleans up timed out entries every 5 seconds
        } catch (InterruptedException ie) {
            // continue - shouldn't happen anyway
        }

        cache.put("foo2", "bar2");
        assertNull("Cache should timeout entries",
                   cache.get("foo1"));
        assertEquals("Cache should return objects that are added",
                     "bar2",
                     cache.get("foo2"));
    }

    /**
     * Test illegal arguments for the get method.
     */
    public void testIllegalArguments() {
        SimpleCache cache = new SimpleCache();
        try {
            cache.get(null);
            fail("Null key should cause an IllegalArgumentException");
        } catch (NullPointerException ex) {
            // good
        }
    }

    /**
     * Suite used to run tests.
     *
     * @return TestSuite representing this class.
     */
    public static Test suite() {
        return new TestSuite(GetObjectFromCacheTestCase.class);
    }
}
