/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.failuretests;

import com.topcoder.management.phase.autopilot.failuretests.impl.ActiveAutoPilotSourceFailureTests;
import com.topcoder.management.phase.autopilot.failuretests.impl.DefaultProjectPilotFailureTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Failure test cases.
 * </p>
 *
 * @author skatou
 * @version 1.0
 */
public class FailureTests extends TestCase {
    /**
     * Aggregates all Failure test cases.
     *
     * @return the test suite aggregates all Failure test cases.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(ActiveAutoPilotSourceFailureTests.class);
        suite.addTestSuite(DefaultProjectPilotFailureTests.class);

        suite.addTestSuite(AutoPilotResultFailureTests.class);
        suite.addTestSuite(AutoPilotFailureTests.class);
        suite.addTestSuite(AutoPilotJobFailureTests.class);

        return suite;
    }
}
