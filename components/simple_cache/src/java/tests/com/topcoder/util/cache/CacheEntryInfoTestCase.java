/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Provides unit test cases for CacheEntryInfo class.
 *
 * @author  rem
 * @version 2.0
 * @since   2.0
 */
public class CacheEntryInfoTestCase extends TestCase {

    /**
     * Returns the test suite for this class.
     *
     * @return a new TestSuite representing this class.
     */
    public static Test suite() {
        return new TestSuite(CacheEntryInfoTestCase.class);
    }

    /**
     * Tests constructor with incorrect arguments.
     * <ul>
     * <li>Make sure IllegalArgumentException is thrown if any argument is < 0
     * </ul>
     */
    public void testConstructorFail() {
        // check size.
        try {
            new CacheEntryInfo(-1, 0, 1);
            fail("IllegalArgumentException should be thrown if 'size' is < 0");
        } catch (IllegalArgumentException ex) {
            // expected exception.
        }

        // check createTime.
        try {
            new CacheEntryInfo(1, -1, 0);
            fail("IllegalArgumentException should be thrown if 'createTime' is < 0");
        } catch (IllegalArgumentException ex) {
            // exptected exception.
        }

        // check lastAccessTime.
        try {
            new CacheEntryInfo(0, 1, -1);
            fail("IllegalArgumentException should be thrown if 'lastAccessTime' is < 0");
        } catch (IllegalArgumentException ex) {
            // exptected exception.
        }
    }

    /**
     * Tests constructor and all methods.
     *
     * <ul>
     * <li>Call constructor with correct arguments and make sure getSize(), getCacheEntryTime() and
     *     getLastAccessTime() return expected values.
     * </ul>
     */
    public void testConstructorAndAllMethods() {
        CacheEntryInfo info = new CacheEntryInfo(0, 1, 2);
        assertEquals("getSize() should return value set in constructor.", 0, info.getSize());
        assertEquals("getCacheEntryTime() should return value set in constructor.", 1, info.getCacheEntryTime());
        assertEquals("getLastAccessTime() should return value set in constructor.", 2, info.getLastAccessTime());
    }
}
