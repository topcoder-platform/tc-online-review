/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.memoryusage.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Accuracy test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class AccuracyTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(MemoryUsageDetailAccuracyTests.suite());
        suite.addTest(MemoryUsageResultAccuracyTests.suite());
        suite.addTest(MemoryUsageAccuracyTests.suite());
        suite.addTest(Sun14AnalyzerAccuracyTests.suite());
        suite.addTest(Sun13AnalyzerAccuracyTests.suite());
        return suite;
    }

}
