/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.management.reviewfeedback.accuracytests.AccuracyTests;
import com.topcoder.management.reviewfeedback.failuretests.FailureTests;
import com.topcoder.management.reviewfeedback.stress.StressTests;

/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author amazingpig, BlackMagic
 * @version 1.0
 */
public class AllTests extends TestCase {
    /**
     * <p>
     * Create unit tests suite.
     * </p>
     *
     * @return unit tests suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(UnitTests.suite());
        suite.addTest(FailureTests.suite());
        suite.addTest(StressTests.suite());
        suite.addTest(AccuracyTests.suite());

        return suite;
    }
}
