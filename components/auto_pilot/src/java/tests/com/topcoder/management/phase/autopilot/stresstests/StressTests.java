/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Stress test cases.
 * </p>
 *
 * @author skatou
 * @version 1.0
 */
public class StressTests extends TestCase {
    /**
     * Aggregates all Stress test cases.
     *
     * @return the test suite aggregates all Stress test cases.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(AutoPilotStressTests.class);

        return suite;
    }
}
