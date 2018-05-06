/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.memoryusage.failuretests;

import junit.framework.TestCase;

import com.topcoder.util.memoryusage.FailureTestAnalyzer;

/**
 * Failure test fixture of <code>BaseAnalyzer</code> class.
 *
 * @author Thinfox
 * @version 2.0
 */
public class BaseAnalyzerTests extends TestCase {
    /**
     * <p>
     * The default <code>BaseAnalyzer</code> instance on which to perform tests.
     * </p>
     */
    private FailureTestAnalyzer analyzer;

    /**
     * <p>
     * Tests the <code>GetFieldSize()</code> method with <code>null</code> class.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void testGetFieldSize_Null() {
        analyzer = new FailureTestAnalyzer();
        try {
            analyzer.getFieldSize(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the <code>GetFieldSize()</code> method with <code>null</code> object.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void testGetArraySize_Null() {
        analyzer = new FailureTestAnalyzer();
        try {
            analyzer.getArraySize(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the <code>GetFieldSize()</code> method with non-array object.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void testGetArraySize_NonArray() {
        analyzer = new FailureTestAnalyzer();
        try {
            analyzer.getArraySize("Test");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }
}
