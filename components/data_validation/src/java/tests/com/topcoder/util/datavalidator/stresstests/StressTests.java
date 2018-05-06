/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Stress test cases.
 * </p>
 *
 * @author TopCoder
 * @version 1.1
 * @since 1.0
 */
public class StressTests extends TestCase {

    /**
     * Creates a test suite for stress tests.
     *
     * @return test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(UtilityValidatorTests.class);
        suite.addTestSuite(PrimitiveValidatorTests.class);
        suite.addTestSuite(TypeValidatorTests.class);
        suite.addTestSuite(CompositeValidatorTests.class);
        suite.addTestSuite(CustomValidatorTests.class);
        suite.addTestSuite(CompositeValidatorGetMessageTests.class);
        return suite;
    }
}
