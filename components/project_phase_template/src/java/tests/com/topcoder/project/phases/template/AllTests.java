/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.phases.template;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.project.phases.template.failuretests.FailureTests;
import com.topcoder.project.phases.template.accuracytests.AccuracyTests;
import com.topcoder.project.phases.template.stresstests.StressTests;

/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 * <p>
 * Change for version 1.1: New test cases added.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.1
 * @since 1.0
 */
public class AllTests extends TestCase {
    /**
     * <p>
     * Return the aggregated unit tests.
     * </p>
     *
     * @return the aggregated unit tests
     */
    public static Test suite() {

        final TestSuite suite = new TestSuite();
        suite.addTest(UnitTests.suite());
        suite.addTest(FailureTests.suite());

        suite.addTest(AccuracyTests.suite());
        suite.addTest(StressTests.suite());

        return suite;
    }

}
