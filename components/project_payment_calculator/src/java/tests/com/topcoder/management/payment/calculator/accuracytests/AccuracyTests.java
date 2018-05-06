/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all accuracy unit test cases.
 * </p>
 *
 * @author lqz
 * @version 1.0
 */
public class AccuracyTests extends TestCase {
    /**
     * <p>
     * All accuracy unit test cases.
     * </p>
     *
     * @return The test suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(DefaultProjectPaymentCalculatorAccuracyTests.suite());
        suite.addTest(ProjectPaymentAdjustmentCalculatorAccuracyTests.suite());

        return suite;
    }

}
