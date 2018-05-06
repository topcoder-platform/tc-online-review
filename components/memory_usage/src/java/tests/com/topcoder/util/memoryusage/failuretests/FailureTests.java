/*
 * Copyright (C) 2006, TopCoder, Inc. All rights reserved
 */
 package com.topcoder.util.memoryusage.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Failure test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class FailureTests extends TestCase {


    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(MemoryUsageTests.class);
        suite.addTestSuite(MemoryUsageDetailTests.class);
        suite.addTestSuite(MemoryUsageResultTests.class);
        suite.addTestSuite(BaseAnalyzerTests.class);
        suite.addTestSuite(Sun14AnalyzerTests.class);
        suite.addTestSuite(Sun15AnalyzerTests.class);
        suite.addTestSuite(IBM14AnalyzerTests.class);
        suite.addTestSuite(IBM15AnalyzerTests.class);
        return suite;
    }
}
