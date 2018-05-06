package com.topcoder.util.cache.accuracytests;

import com.topcoder.util.cache.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case tests the cache's <code>put()</code> method.</p>
 *
 * @author haha
 * @version 1.0
 */
public class AccuracyPutObjectInCacheTestCase extends TestCase {

    /** 
     * tests the put function for the cache
     */
    public void testPut() {
        SimpleCache cache = new SimpleCache();

        cache.put("foo1", "bar1");
        assertEquals("Cache should return objects that are added",
                     "bar1",
                     cache.get("foo1"));

        cache.put("foo1", "newbar");
        assertEquals("Puts on existing keys should replace old object",
                     "newbar",
                     cache.get("foo1"));

        cache.put("foo1", null);
        assertNull("Mapping keys to null should remove entry",
                   cache.get("foo1"));

        cache.put("foo1", "bar1");
        cache.put("foo2", "bar1");
        assertEquals("Mapping two keys to the same object should work",
                     "bar1",
                     cache.get("foo1"));
        assertEquals("Mapping two keys to the same object should work",
                     "bar1",
                     cache.get("foo2"));
    }

    /** 
     * runs these tests
     */
    public static Test suite() {
        return new TestSuite(AccuracyPutObjectInCacheTestCase.class);
    }
}
