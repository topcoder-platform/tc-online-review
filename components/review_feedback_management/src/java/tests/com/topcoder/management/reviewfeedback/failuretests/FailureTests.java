/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all failure unit test cases.</p>
 *
 * @author hesibo, BlackMagic
 * @version 1.0
 */
public class FailureTests extends TestCase {
    /**
     * <p>
     * Aggregates all unit test cases.
     * </p>
     *
     * @return All unit test cases.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(JDBCReviewFeedbackManagerFailureTests.suite());
        return suite;
    }

}
