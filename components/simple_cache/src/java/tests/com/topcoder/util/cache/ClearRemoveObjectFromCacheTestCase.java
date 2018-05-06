/*
 * Copyright (c) 2002-2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case tests the cache's <code>remove()</code> and
 * <code>clear()</code> methods.</p>
 *
 * @author  srowen, rem
 * @version 2.0
 * @since   1.0
 */
public class ClearRemoveObjectFromCacheTestCase extends TestCase {

    /**
     * Tests removing items from the cache.
     */
    public void testRemove() {
        SimpleCache cache = new SimpleCache();

        cache.put("foo1", "bar1");
        assertEquals("Cache should return objects that are added",
                     "bar1",
                     cache.get("foo1"));
        cache.remove("foo1");
        assertNull("Removing keys should cause gets to return null",
                   cache.get("foo1"));

        // shouldn't throw any exception
        cache.remove("foo1");

        cache.put("foo1", "bar1");
        cache.put("foo2", "bar2");
        assertEquals("Cache should return objects that are added",
                     "bar1",
                     cache.get("foo1"));
        assertEquals("Cache should return objects that are added",
                     "bar2",
                     cache.get("foo2"));
        cache.remove("foo1");
        assertEquals("Removing one key should not affect another",
                     "bar2",
                     cache.get("foo2"));
    }

    /**
     * Tests clearing out the entire cache.
     */
    public void testClear() {
        SimpleCache cache = new SimpleCache();
        cache.put("foo1", "bar1");
        cache.put("foo2", "bar2");
        cache.put("foo3", "bar3");
        cache.clear();
        assertNull("Clear cache should cause all gets to return null",
                   cache.get("foo1"));
        assertNull("Clear cache should cause all gets to return null",
                   cache.get("foo2"));
        assertNull("Clear cache should cause all gets to return null",
                   cache.get("foo3"));
    }

    /**
     * Tests illegal arguments to the remove function.
     */
    public void testIllegalArguments() {
        SimpleCache cache = new SimpleCache();
        try {
            cache.remove(null);
            fail("Null key should cause an IllegalArgumentException");
        } catch (NullPointerException ex) {
            // good
        }
    }

    /**
     * Runs tests.
     *
     * @return TestSuite representing this class.
     */
    public static Test suite() {
        return new TestSuite(ClearRemoveObjectFromCacheTestCase.class);
    }
}
