/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.cache.failuretests;

import com.topcoder.util.cache.CacheEntryInfo;

import junit.framework.TestCase;


/**
 * Failure test for CacheEntryInfo class.
 *
 * @author semi_sleep
 * @version 2.0
 */
public class CacheEntryInfoFailureTest extends TestCase {
    /**
     * Test the constructor of <code>CacheEntryInfo</code>. This method test the case that size is smaller than zero.
     * an IllegalArgumentException should be thrown in this case.
     *
     * @throws Exception if any error occurs during creation
     */
    public void testCtorSizeInvalid() throws Exception {
        try {
            new CacheEntryInfo(-1, 0, 0);
            fail("Should throw IllegalArgumentException when size is smaller than zero.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the constructor of <code>CacheEntryInfo</code>. This method test the case that createTime is smaller than zero.
     * an IllegalArgumentException should be thrown in this case.
     *
     * @throws Exception if any error occurs during creation
     */
    public void testCtorCreateTimeInvalid() throws Exception {
        try {
            new CacheEntryInfo(0, -1, 0);
            fail("Should throw IllegalArgumentException when createTime is smaller than zero.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the constructor of <code>CacheEntryInfo</code>. This method test the case that lastAccessTime is smaller than zero.
     * an IllegalArgumentException should be thrown in this case.
     *
     * @throws Exception if any error occurs during creation
     */
    public void testCtorCreateTimeInavlid() throws Exception {
        try {
            new CacheEntryInfo(0, 0, -1);
            fail("Should throw IllegalArgumentException when lastAccessTime is smaller than zero.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
}
