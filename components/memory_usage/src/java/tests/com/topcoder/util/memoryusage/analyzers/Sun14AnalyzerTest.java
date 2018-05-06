/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage.analyzers;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests of Sun14Analyzer.
 *
 * @author TexWiller
 * @version 2.0
 */
public class Sun14AnalyzerTest extends GenericAnalyzerTest {

    /**
     * Standard TestCase constructor: creates a new Sun15AnalyzerTest object.
     *
     * @param testName The name to be given to this TestCase.
     */
    public Sun14AnalyzerTest(String testName) {
        super(testName, "Sun Microsystems", "48.", true, -1);
    }

    /**
     * Sets up the fixture.
     */
    protected void setUp() {
        analyzer = new Sun14Analyzer();
    }

    /**
     * Static method expected by JUnit test runners. Returns a Test containing all
     * the testXXX methods in this class.
     *
     * @return A new Test, containing all the testXXX methods in this class.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(Sun14AnalyzerTest.class);
        return suite;
    }
}
