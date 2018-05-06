/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.persistence;

import com.topcoder.management.review.persistence.accuracytests.AccuracyTests;
import com.topcoder.management.review.persistence.failuretests.FailureTests;
import com.topcoder.management.review.persistence.stresstests.StressTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This class aggregates all tests for this component.
 * </p>
 * @author TopCoder
 * @version 1.0
 */
public class AllTests extends TestCase {

    /**
     * Aggregates all tests in this class.
     * @return test suite aggregating all tests.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(UnitTests.suite());
        suite.addTest(AccuracyTests.suite());
        suite.addTest(FailureTests.suite());
        suite.addTest(StressTests.suite());
        return suite;
    }
}
