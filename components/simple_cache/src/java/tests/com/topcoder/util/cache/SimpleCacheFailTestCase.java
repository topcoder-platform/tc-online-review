/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Tests methods of SimpleCache with failure tests.
 *
 * @author  rem
 * @version 2.0
 * @since   2.0
 */
public class SimpleCacheFailTestCase extends TestCase {

    /**
     * Instance to test.
     */
    private SimpleCache cache = new SimpleCache();

    /**
     * Returns the test suite for this class.
     *
     * @return a new TestSuite representing this class.
     */
    public static Test suite() {
        return new TestSuite(SimpleCacheFailTestCase.class);
    }

    /**
     * Tests method get.
     * <ul>
     * <li>Make sure NullPointerException is thrown when key is null.
     * </ul>
     */
    public void testGet() {
        try {
            cache.get(null);
            fail("get(null) should throw NullPointerException.");
        } catch (NullPointerException ex) {
            // expected exception.
        }
    }

    /**
     * Tests method put.
     * <ul>
     * <li>Make sure NullPointerException is thrown when key is null.
     * </ul>
     */
    public void testPut() {
        try {
            cache.put(null, new Object());
            fail("get(null, new Object()) should throw NullPointerException.");
        } catch (NullPointerException ex) {
            // expected exception.
        }
    }

    /**
     * Tests method remove.
     * <ul>
     * <li>Make sure NullPointerException is thrown when key is null.
     * </ul>
     */
    public void testRemove() {
        try {
            cache.remove(null);
            fail("remove(null) should throw NullPointerException.");
        } catch (NullPointerException ex) {
            // expected exception.
        }
    }

    /**
     * Tests iterator returned by method values.
     * <ul>
     * <li>Make sure it throws NoSuchElementException.
     * <li>Make sure it throws IllegalStateException.
     * </ul>
     */
    public void testValues() {
        Iterator it;

        it = cache.values();
        while (it.hasNext()) {
            it.next();
        }
        try {
            it.next();
            fail("NoSuchElementException should be thrown.");
        } catch (NoSuchElementException ex) {
            // expected exception.
        }

        it = cache.values();
        try {
            it.remove();
            fail("IllegalStateException should be thrown.");
        } catch (IllegalStateException ex) {
            // expected exception.
        }
    }

    /**
     * Tests method removeSet.
     * <ul>
     * <li>Make sure NullPointerException is thrown when keys is null.
     * </ul>
     */
    public void testRemoveSet() {
        try {
            cache.removeSet(null);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException ex) {
            // expected exception.
        }
    }

    /**
     * Tests method setEvictionStrategy.
     * <ul>
     * <li>Make sure NullPointerException is thrown when strategy is null.
     * </ul>
     */
    public void testSetEvictionStrategy() {
        try {
            cache.setEvictionStrategy(null);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException ex) {
            // expected exception.
        }
    }

    /**
     * Tests method removeLarger.
     * <ul>
     * <li>Make sure NullPointerException is thrown when value is null.
     * </ul>
     */
    public void testRemoveLarger() {
        try {
            cache.removeLarger(null);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException ex) {
            // expected exception.
        }
    }

    /**
     * Tests method removeOlder.
     * <ul>
     * <li>Make sure NullPointerException is thrown when key is null.
     * </ul>
     */
    public void testRemoveOlder() {
        try {
            cache.removeOlder(null);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException ex) {
            // expected exception.
        }
    }

    /**
     * Tests method removeLike.
     * <ul>
     * <li>Make sure NullPointerException is thrown when object is null.
     * </ul>
     */
    public void testRemoveLike() {
        try {
            cache.removeLike(null);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException ex) {
            // expected exception.
        }
    }

    /**
     * Tests method removeByPattern.
     * <ul>
     * <li>Make sure NullPointerException is thrown when regex is null.
     * <li>Make sure IllegalArgumentException is thrown when regex contains invalid regular expression.
     * </ul>
     */
    public void testRemoveByPattern() {
        try {
            cache.removeByPattern(null);
            fail("removeByPattern(null) should throw NullPointerException.");
        } catch (NullPointerException ex) {
            // expected exception.
        }
        try {
            cache.removeByPattern("[");
            fail("removeByPattern(invalid_regex) should throw IllegalArgumentException.");
        } catch (IllegalArgumentException ex) {
            // expected exception.
        }
    }
}
