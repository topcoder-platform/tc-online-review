/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class FailureTests extends TestCase {
    /**
     * <p>
     * All unit test cases.
     * </p>
     *
     * @return The test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(BaseProjectPaymentCalculatorFailureTest.suite());
        suite.addTest(DefaultProjectPaymentCalculatorFailureTest.suite());
        suite.addTest(ProjectPaymentAdjustmentCalculatorFailureTest.suite());
        return suite;
    }

}
