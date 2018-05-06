/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.management.deliverable.late.accuracytests.AccuracyTests;
import com.topcoder.management.deliverable.late.failuretests.FailureTests;
import com.topcoder.management.deliverable.late.stresstests.StressTests;

/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class AllTests extends TestCase {
    /**
     * <p>
     * Create unit tests suite.
     * </p>
     * @return unit tests suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(AccuracyTests.suite());
        suite.addTest(StressTests.suite());
        suite.addTest(FailureTests.suite());
        suite.addTest(UnitTests.suite());

        return suite;
    }
}
