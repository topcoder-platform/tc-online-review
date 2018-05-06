/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage.analyzers;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests of Sun15Analyzer.
 *
 * @author TexWiller
 * @version 2.0
 */
public class Sun15AnalyzerTest extends GenericAnalyzerTest {

    /**
     * Standard TestCase constructor: creates a new Sun15AnalyzerTest object.
     *
     * @param testName The name to be given to this TestCase.
     */
    public Sun15AnalyzerTest(String testName) {
        super(testName, "Sun Microsystems", "49.", true, -1);
    }

    /**
     * Sets up the fixture.
     */
    protected void setUp() {
        analyzer = new Sun15Analyzer();
    }

    /**
     * Static method expected by JUnit test runners. Returns a Test containing all
     * the testXXX methods in this class.
     *
     * @return A new Test, containing all the testXXX methods in this class.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(Sun15AnalyzerTest.class);
        return suite;
    }
}
