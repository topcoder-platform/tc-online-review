/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Set;

/**
 * <p>This test case tests the cache's <code>keySet()</code> method.</p>
 *
 * @author  srowen, rem
 * @version 2.0
 * @since   1.0.1
 */
public class KeySetTestCase extends TestCase {

    /**
     * Tests keySet() method.
     */
    public void testKeySet() {
        SimpleCache cache = new SimpleCache();

        Set keySet = cache.keySet();
        assertNotNull("Key set should never be null", keySet);
        assertTrue("Key set should be empty", keySet.isEmpty());

        try {
            keySet.add("something");
            fail("Key set should be unmodifiable");
        } catch (UnsupportedOperationException uoe) {
            // good
        }

        cache.put("foo", "bar");
        assertTrue("Previous key set should not reflect cache changes", keySet.isEmpty());

        keySet = cache.keySet();
        assertTrue("Key set should have new key", keySet.size() == 1);
        assertTrue("Key set should have new key", keySet.contains("foo"));

        cache.put("baz", "boo");
        keySet = cache.keySet();
        assertTrue("Key set should contain both keys", keySet.size() == 2);
        assertTrue("Key set should contain both keys", keySet.contains("foo") && keySet.contains("baz"));

        cache.clear();
        keySet = cache.keySet();
        assertTrue("Key set should be empty after clear", keySet.isEmpty());
    }

    /**
     * Suite used to run tests.
     *
     * @return TestSuite representing this class.
     */
    public static Test suite() {
        return new TestSuite(KeySetTestCase.class);
    }
}
