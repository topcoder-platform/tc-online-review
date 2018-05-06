/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.memoryusage.failuretests;

import junit.framework.TestCase;

import com.topcoder.util.memoryusage.MemoryUsageDetail;

/**
 * Tests the MemoryUsageDetail class.
 *
 * @author Thinfox
 * @version 2.0
 */
public class MemoryUsageDetailTests extends TestCase {
    /**
     * <p>
     * The default <code>MemoryUsageDetail</code> instance on which to perform tests.
     * </p>
     */
    private MemoryUsageDetail detail = null;

    /**
     * <p>
     * Tests the <code>MemoryUsageDetail(Class)</code> constructor with <code>null</code> class.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void testConstructor_Null() {
        try {
            new MemoryUsageDetail(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the <code>cumulate(int, long)</code> method with negative count.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void testCumulate_NegativeCount() {
        detail = new MemoryUsageDetail(String.class);
        try {
            detail.cumulate(-1, 1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the <code>cumulate(int, long)</code> method with negative userMemory.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void testCumulate_NegativeUserMemory() {
        detail = new MemoryUsageDetail(String.class);
        try {
            detail.cumulate(1, -1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the <code>cumulate(int, long)</code> method with zero count.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void testCumulate_ZeroCount() {
        detail = new MemoryUsageDetail(String.class);
        try {
            detail.cumulate(0, 1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the <code>cumulate(int, long)</code> method with zero userMemory.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void testCumulate_ZeroUserMemory() {
        detail = new MemoryUsageDetail(String.class);
        try {
            detail.cumulate(1, 0);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }
}