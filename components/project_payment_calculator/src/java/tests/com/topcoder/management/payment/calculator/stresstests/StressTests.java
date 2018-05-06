/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all stress test cases.
 * </p>
 *
 * @author gjw99
 * @version 1.0
 */
public class StressTests extends TestCase {
    /**
     * The test suite.
     *
     * @return the test suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(DefaultProjectPaymentCalculatorTests.suite());
        suite.addTest(ProjectPaymentAdjustmentCalculatorTests.suite());

        return suite;
    }
}
