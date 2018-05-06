/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.management.payment.calculator.impl.BaseProjectPaymentCalculatorTest;
import com.topcoder.management.payment.calculator.impl.DefaultProjectPaymentCalculatorTest;
import com.topcoder.management.payment.calculator.impl.HelperTest;
import com.topcoder.management.payment.calculator.impl.ProjectPaymentAdjustmentCalculatorTest;


/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class UnitTests extends TestCase {
    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a Test suite for this test case.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(ProjectPaymentCalculatorConfigurationExceptionTest.suite());
        suite.addTest(ProjectPaymentCalculatorExceptionTest.suite());

        suite.addTest(BaseProjectPaymentCalculatorTest.suite());
        suite.addTest(DefaultProjectPaymentCalculatorTest.suite());
        suite.addTest(ProjectPaymentAdjustmentCalculatorTest.suite());
        suite.addTest(HelperTest.suite());

        suite.addTest(Demo.suite());

        return suite;
    }
}
