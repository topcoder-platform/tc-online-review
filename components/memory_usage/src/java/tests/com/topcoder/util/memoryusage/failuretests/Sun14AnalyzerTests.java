/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.memoryusage.failuretests;

import com.topcoder.util.memoryusage.analyzers.Sun14Analyzer;

import junit.framework.TestCase;

/**
 * Failure test fixture of <code>Sun14Analyzer</code> class.
 * 
 * @author Thinfox
 * @version 2.0
 */
public class Sun14AnalyzerTests extends TestCase {
    /**
     * <p>
     * The default <code>Sun14Analyzer</code> instance on which to perform tests.
     * </p>
     */
    private Sun14Analyzer analyzer;

    /**
     * <p>
     * Tests the <code>GetFieldSize()</code> method with <code>null</code> class.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void testGetFieldSize_Null() {
        analyzer = new Sun14Analyzer();

        try {
            analyzer.getFieldSize(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }
}
