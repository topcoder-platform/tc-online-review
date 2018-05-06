/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This test case aggregates all Unit test cases.
 *
 * @author assistant, victorsam
 * @version 1.1
 */
public class FailureTests extends TestCase {
    /**
     * Test suite for the failure tests.
     *
     * @return the test suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(DefaultPhaseManagerTest.class);
        suite.addTestSuite(DefaultPhaseValidatorTest.class);
        suite.addTestSuite(HandlerRegistryInfoTest.class);
        suite.addTest(OperationCheckResultFailureTests.suite());

        return suite;
    }
}
