/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.date.workdays;

import com.topcoder.date.workdays.accuracytests.AccuracyTests;
import com.topcoder.date.workdays.failuretests.FailureTests;
import com.topcoder.date.workdays.stresstests.StressTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit tests of this components.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class AllTests extends TestCase {

    /**
     * <p> Gets test suite. </p>
     *
     * @return test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(UnitTests.suite());
        suite.addTest(StressTests.suite());
        suite.addTest(AccuracyTests.suite());
        suite.addTest(FailureTests.suite());
        return suite;
    }

}
