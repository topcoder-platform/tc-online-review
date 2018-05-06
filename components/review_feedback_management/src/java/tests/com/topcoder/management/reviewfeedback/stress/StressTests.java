/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback.stress;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This test case aggregates all stress test cases.
 *
 * @author BlackMagic
 * @version 1.0
 */
public class StressTests extends TestCase {
    /**
     * Aggregates all stress test cases.
     *
     * @return All unit test cases.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(JDBCReviewFeedbackManagerStressUnitTests.suite());
        return suite;
    }

}
