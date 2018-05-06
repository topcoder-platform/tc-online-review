/*
 * Copyright (C) 2012, 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.management.reviewfeedback.impl.JDBCReviewFeedbackManagerUnitTests;

/**
 * <p>
 * This test case aggregates all unit test cases.
 * </p>
 *
 * <p>
 * <em>Changes in 2.0:</em>
 * <ol>
 * <li>Added new tests</li>
 * </ol>
 * </p>
 *
 * @author amazingpig, sparemax
 * @version 2.0
 */
public class UnitTests extends TestCase {
    /**
     * <p>
     * Aggregates all unit test cases.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Added new tests</li>
     * </ol>
     * </p>
     *
     * @return All unit test cases.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(DemoTest.suite());
        suite.addTest(JDBCReviewFeedbackManagerUnitTests.suite());

        suite.addTest(IdentifiableEntityUnitTests.suite());
        suite.addTest(AuditableEntityUnitTests.suite());
        suite.addTest(ReviewFeedbackDetailUnitTests.suite());
        suite.addTest(ReviewFeedbackTest.suite());

        // Exceptions
        suite.addTest(ReviewFeedbackManagementConfigurationExceptionTest.suite());
        suite.addTest(ReviewFeedbackManagementEntityNotFoundExceptionTest.suite());
        suite.addTest(ReviewFeedbackManagementExceptionTest.suite());
        suite.addTest(ReviewFeedbackManagementPersistenceExceptionTest.suite());

        return suite;
    }

}
