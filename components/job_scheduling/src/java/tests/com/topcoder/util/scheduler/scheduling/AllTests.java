/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.scheduler.scheduling.accuracytests.AccuracyTests;
import com.topcoder.util.scheduler.scheduling.failuretests.FailureTests;
import com.topcoder.util.scheduler.scheduling.stresstests.StressTests;

/**
 * <p>
 * This test case aggregates all test cases.
 * </p>
 *
 * @author TopCoder
 * @version 3.1
 */
public class AllTests extends TestCase {
    /**
     * <p>
     * This test case aggregates all test cases.
     * </p>
     *
     * @return all Unit test cases.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // unit tests
        suite.addTest(UnitTests.suite());

        // accuracy tests
        suite.addTest(AccuracyTests.suite());

        // failure tests
        suite.addTest(FailureTests.suite());

        // stress tests
        suite.addTest(StressTests.suite());

        return suite;
    }
}
