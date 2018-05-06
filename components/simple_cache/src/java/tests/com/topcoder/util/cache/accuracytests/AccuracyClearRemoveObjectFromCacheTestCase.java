package com.topcoder.util.cache.accuracytests;

import com.topcoder.util.cache.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case tests the cache's <code>remove()</code> and
 * <code>clear()</code> methods.</p>
 *
 * @author haha
 * @version 1.0
 */
public class AccuracyClearRemoveObjectFromCacheTestCase extends TestCase {

    /**
     * tests the remove functionality
     */
    public void testRemove() {
        SimpleCache cache = new SimpleCache();

        cache.put("foo1", "bar1");
        cache.put("foo2", "bar2");
        assertEquals("Cache should return objects that are added",
                     "bar1",
                     cache.get("foo1"));
        cache.remove("foo2");
        assertNull("Removing keys should cause gets to return null",
                   cache.get("foo2"));
        assertEquals("Removing one key should not affect another",
                     "bar1",
                     cache.get("foo1"));

        // shouldn't throw any exception
        cache.remove("foo2");

        // add key "foo1" again, the former "bar1" should be overwritten.
        cache.put("foo1", "bar2");
        // cached value can be the same
        cache.put("foo2", "bar2");
        assertEquals("Cache should return objects that are added",
                     "bar2",
                     cache.get("foo1"));
        assertEquals("Cache should return objects that are added",
                     "bar2",
                     cache.get("foo2"));
        cache.remove("foo1");
        assertNull("Removing keys should cause gets to return null",
                     cache.get("foo1"));
        assertEquals("Removing one key should not affect another",
                     "bar2",
                     cache.get("foo2"));
    }

    /**
     * tests clearing the cache
     */
    public void testClear() {
        SimpleCache cache = new SimpleCache();
        cache.put("foo1", "bar1");
        cache.put("foo2", "bar2");
        cache.put("foo3", "bar3");
        cache.clear();
        cache.put("foo4", "bar4");
        assertNull("Clear cache should cause all gets to return null",
                   cache.get("foo1"));
        assertNull("Clear cache should cause all gets to return null",
                   cache.get("foo2"));
        assertNull("Clear cache should cause all gets to return null",
                   cache.get("foo3"));
    }

    /**
     * runs the tests
     */
    public static Test suite() {
        return new TestSuite(AccuracyClearRemoveObjectFromCacheTestCase.class);
    }
}
