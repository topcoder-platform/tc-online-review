/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.search;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Tests functionality of SearchException. All consturctors are tested.
 * </p>
 * 
 * @author FireIce
 * @version 1.0
 */
public class SearchExceptionTestCase extends TestCase {
    /**
     * <p>
     * Represents a string with a detail message.
     * </p>
     */
    private static final String DETAIL_MESSAGE = "detail";

    /**
     * <p>
     * Represents a throwable cause.
     * </p>
     */
    private static final Exception CAUSE = new Exception();

    /**
     * <p>
     * Tests accuracy of <code>SearchException(String)</code> constructor. The detail error message should be
     * correct.
     * </p>
     */
    public void testSearchExceptionStringAccuracy() {
        // Construct SearchException with a detail message
        SearchException exception = new SearchException(DETAIL_MESSAGE);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message should be identical.", DETAIL_MESSAGE, exception.getMessage());
    }

    /**
     * <p>
     * Tests accuracy of <code>SearchException(String, Exception)</code> constructor. The detail error message and
     * the original cause of error should be correct.
     * </p>
     */
    public void testSearchExceptionStringExceptionAccuracy() {
        // Construct SearchException with a detail message and a cause
        SearchException exception = new SearchException(DETAIL_MESSAGE, CAUSE);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message with cause should be correct.", DETAIL_MESSAGE, exception.getMessage());

        // Verify that there is a cause
        assertNotNull("Should have cause.", exception.getCause());
        assertTrue("Cause should be identical.", CAUSE == exception.getCause());
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     * 
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(SearchExceptionTestCase.class);
    }
}
