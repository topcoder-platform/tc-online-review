/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.memoryusage.failuretests;

import junit.framework.TestCase;

import com.topcoder.util.memoryusage.analyzers.IBM15Analyzer;

/**
 * Failure test fixture of <code>IBM15Analyzer</code> class.
 *
 * @author Thinfox
 * @version 2.0
 */
public class IBM15AnalyzerTests extends TestCase {
    /**
     * <p>
     * The default <code>IBM15Analyzer</code> instance on which to perform tests.
     * </p>
     */
    private IBM15Analyzer analyzer;

    /**
     * <p>
     * Tests the <code>GetFieldSize()</code> method with <code>null</code> class.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void testGetFieldSize_Null() {
        analyzer = new IBM15Analyzer();
        try {
            analyzer.getFieldSize(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }
}
