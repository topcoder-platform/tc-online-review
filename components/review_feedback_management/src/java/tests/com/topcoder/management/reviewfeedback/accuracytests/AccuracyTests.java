/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all accuracy test cases.
 * </p>
 * @author liuliquan
 * @version 2.0
 */
public class AccuracyTests extends TestCase {

    /**
     * <p>
     * Aggregates all accuracy test cases.
     * </p>
     *
     * @return All accuracy test cases.
     */
    public static Test suite() {

        final TestSuite suite = new TestSuite();
        suite.addTest(JDBCReviewFeedbackManagerAccTests.suite());
        return suite;
    }
}
