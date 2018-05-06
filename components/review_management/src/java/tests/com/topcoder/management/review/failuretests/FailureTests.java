/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Failure test cases.
 * </p>
 *
 * @author Chenhong
 * @version 1.2
 */
public class FailureTests extends TestCase {

    /**
     * Test suite for the failure tests.
     *
     * @return the failure test suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(TestChainFilterFailure.class);
        suite.addTestSuite(TestDefaultReviewManagerFailure.class);

        return suite;
    }

}