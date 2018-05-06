/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.memoryusage.failuretests;

import junit.framework.TestCase;

import com.topcoder.util.memoryusage.MemoryUsageResult;

/**
 * Failure test fixture of <code>MemoryUsageResult</code> class.
 *
 * @author Thinfox
 * @version 2.0
 */
public class MemoryUsageResultTests extends TestCase {

    /**
     * <p>
     * The default <code>MemoryUsageResult</code> instance on which to perform tests.
     * </p>
     */
    private MemoryUsageResult result;

    /**
     * <p>
     * Tests the <code>cumulate(int, long)</code> method with negative count.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void testCumulate_NegativeCount() {
        result = new MemoryUsageResult();
        try {
            result.cumulate(-1, 1);
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
        result = new MemoryUsageResult();
        try {
            result.cumulate(1, -1);
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
        result = new MemoryUsageResult();
        try {
            result.cumulate(0, 1);
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
        result = new MemoryUsageResult();
        try {
            result.cumulate(1, 0);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the <code>setDetails</code> with <code>null</code> map.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void testSetDetails_Null() {
        result = new MemoryUsageResult();
        try {
            result.setDetails(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the <code>getDetail(Class)</code> method with <code>null</code> class.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> should be expected.
     * </p>
     */
    public void testGetDetail_Null() {
        result = new MemoryUsageResult();
        try {
            result.getDetail(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }
}
