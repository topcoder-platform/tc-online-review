/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.management.phase.autopilot.impl.ActiveAutoPilotSourceFailure;
import com.topcoder.management.phase.autopilot.impl.ActiveAutoPilotSourceTest;
import com.topcoder.management.phase.autopilot.impl.DefaultProjectPilotFailure;
import com.topcoder.management.phase.autopilot.impl.DefaultProjectPilotTest;

/**
 * <p>This test case aggregates all Unit test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class UnitTests extends TestCase {

    /**
     * Aggregates the test suite.
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        //suite.addTest(XXX.suite());
        suite.addTest(AutoPilotTest.suite());
        suite.addTest(AutoPilotJobTest.suite());
        suite.addTest(AutoPilotResultTest.suite());

        suite.addTest(ActiveAutoPilotSourceTest.suite());
        suite.addTest(DefaultProjectPilotTest.suite());

        suite.addTest(AutoPilotExceptionTest.suite());
        suite.addTest(AutoPilotSourceExceptionTest.suite());
        suite.addTest(ConfigurationExceptionTest.suite());
        suite.addTest(PhaseOperationExceptionTest.suite());

        suite.addTest(AutoPilotJobFailure.suite());
        suite.addTest(AutoPilotFailure.suite());
        suite.addTest(AutoPilotResultFailure.suite());
        suite.addTest(ActiveAutoPilotSourceFailure.suite());
        suite.addTest(DefaultProjectPilotFailure.suite());

        suite.addTest(DemoTest.suite());

        return suite;
    }

}
