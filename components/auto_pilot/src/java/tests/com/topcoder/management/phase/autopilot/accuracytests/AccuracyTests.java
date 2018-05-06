/**
 *
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.topcoder.management.phase.autopilot.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.management.phase.autopilot.accuracytests.impl.ActiveAutoPilotSourceAccuracyTest;
import com.topcoder.management.phase.autopilot.accuracytests.impl.DefaultProjectPilotAccuracyTest;

/**
 * <p>This test case aggregates all Accuracy test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class AccuracyTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(AutoPilotExceptionAccuracyTest.suite());
        suite.addTest(AutoPilotSourceExceptionAccuracyTest.suite());
        suite.addTest(ConfigurationExceptionAccuracyTest.suite());
        suite.addTest(PhaseOperationExceptionAccuracyTest.suite());

        suite.addTest(ActiveAutoPilotSourceAccuracyTest.suite());
        suite.addTest(DefaultProjectPilotAccuracyTest.suite());

        suite.addTest(AutoPilotResultAccuracyTest.suite());
        suite.addTest(AutoPilotAccuracyTest.suite());
        suite.addTest(AutoPilotJobAccuracyTest.suite());


        return suite;
    }

}
