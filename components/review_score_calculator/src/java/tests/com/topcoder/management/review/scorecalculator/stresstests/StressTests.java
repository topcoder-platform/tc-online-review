/*
 *
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.topcoder.management.review.scorecalculator.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all stress test cases.
 * </p>
 *
 * @author crackme
 * @version 1.0
 */
public class StressTests extends TestCase {
    /**
     * Aggregates all stress test cases.
     *
     * @return Test instance
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(ScorecardCalculatorStressTests.class);

        return suite;
    }
}
