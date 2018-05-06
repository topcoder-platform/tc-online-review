/*
 * Copyright (c) 2002-2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case tests the cache's <code>put()</code> method.</p>
 *
 * @author  srowen, rem
 * @version 2.0
 * @since   1.0
 */
public class PutObjectInCacheTestCase extends TestCase {

    /**
     * Tests placing an object in the cache.
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
     * Tests illegal arguments for the put method of simple cache.
     */
    public void testIllegalArguments() {
        SimpleCache cache = new SimpleCache();
        try {
            cache.put(null, "bar");
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
        return new TestSuite(PutObjectInCacheTestCase.class);
    }
}
