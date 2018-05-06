/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all test cases for <code>InconsistentDataException</code>.
 * </p>
 *
 * @author daiwb
 * @version 1.0
 */
public class InconsistentDataExceptionTest extends TestCase {
    /**
     * Error message used for tests.
     */
    private static final String MSG = "error.";

    /**
     * Returns the test suite of this test case.
     *
     * @return the test suite of this test case.
     */
    public static Test suite() {
        return new TestSuite(InconsistentDataExceptionTest.class);
    }

    /**
     * Test constructor <code>InconsistentDataException(String)</code>.
     */
    public void testInconsistentDataException_Accuracy() {
        InconsistentDataException exp = new InconsistentDataException(MSG);
        assertNotNull("The instance should be created successfully.", exp);
        assertNotNull("The InconsistentDataException should inherit from ReviewScoreAggregatorException.",
                (ReviewScoreAggregatorException) exp);
        assertEquals("The error message should be set successfully.", MSG, exp.getMessage());
    }

}
