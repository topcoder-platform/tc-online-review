/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment;

import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;

/**
 * Testcases for ReviewAssignmentAlgorithmException class.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class UnitTestReviewAssignmentAlgorithmException extends TestCase {
    /**
     * Represents a string with a detail message.
     */
    private static final String DETAIL_MESSAGE = "detail";

    /**
     * Represents a throwable cause.
     */
    private static final Throwable CAUSE = new Exception("UnitTest");

    /**
     * <p>
     * Represents the exception data.
     * </p>
     */
    private static final ExceptionData EXCEPTION_DATA = new ExceptionData();

    /**
     * <p>
     * Represents the application code set in exception data.
     * </p>
     */
    private static final String APPLICATION_CODE = "Accuracy";

    /**
     * <p>
     * Sets up the environment.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        EXCEPTION_DATA.setApplicationCode(APPLICATION_CODE);
    }

    /**
     * <p>
     * <code>ReviewAssignmentAlgorithmException</code> should be subclass of
     * <code>ReviewAssignmentAlgorithmException</code>.
     * </p>
     */
    public void testInheritance() {
        assertTrue(
            "ReviewAssignmentAlgorithmException should be" + " subclass of ReviewAssignmentException.",
            ReviewAssignmentAlgorithmException.class.getSuperclass() == ReviewAssignmentException.class);
    }

    /**
     * Tests accuracy of <code>ReviewAssignmentAlgorithmException(String)</code> constructor. The detail error
     * message should be correct.
     */
    public void testNotRespondedLateDeliverablesNotificationExceptionStringAccuracy() {
        // Construct ReviewAssignmentAlgorithmException with a detail message
        ReviewAssignmentAlgorithmException exception = new ReviewAssignmentAlgorithmException(DETAIL_MESSAGE);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message should be identical.", DETAIL_MESSAGE, exception.getMessage());
    }

    /**
     * Tests accuracy of <code>ReviewAssignmentAlgorithmException(String, ExceptionData)</code> constructor.
     * The detail error message and the exception data should be correct.
     */
    public void testNotRespondedLateDeliverablesNotificationExceptionStringExceptionDataAccuracy() {
        // Construct ReviewAssignmentAlgorithmException with a detail message and a cause
        ReviewAssignmentAlgorithmException exception = new ReviewAssignmentAlgorithmException(DETAIL_MESSAGE,
            EXCEPTION_DATA);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message with cause should be correct.", DETAIL_MESSAGE,
            exception.getMessage());

        // Verify that the exception data is correctly set.
        assertNotNull("application code should not null", exception.getApplicationCode());
        assertEquals("exception data is not set.", APPLICATION_CODE, exception.getApplicationCode());
    }

    /**
     * Tests accuracy of <code>ReviewAssignmentAlgorithmException(String, Throwable)</code> constructor. The
     * detail error message and the original cause of error should be correct.
     */
    public void testNotRespondedLateDeliverablesNotificationExceptionStringThrowableAccuracy() {
        // Construct ReviewAssignmentAlgorithmException with a detail message and a
        // cause
        ReviewAssignmentAlgorithmException exception = new ReviewAssignmentAlgorithmException(DETAIL_MESSAGE,
            CAUSE);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message with cause should be correct.", DETAIL_MESSAGE,
            exception.getMessage());

        // Verify that there is a cause
        assertNotNull("Should have cause.", exception.getCause());
        assertSame("Cause should be identical.", CAUSE, exception.getCause());
    }

    /**
     * Tests accuracy of <code>ReviewAssignmentAlgorithmException(String, Throwable, ExceptionData)</code>
     * constructor. The detail error message, the cause exception and the exception data should be correct.
     */
    public void testNotRespondedLateDeliverablesNotificationExceptionStringThrowableExceptionDataAccuracy() {
        // Construct ReviewAssignmentAlgorithmException with a detail message and a cause
        ReviewAssignmentAlgorithmException exception = new ReviewAssignmentAlgorithmException(DETAIL_MESSAGE,
            CAUSE, EXCEPTION_DATA);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message with cause should be correct.", DETAIL_MESSAGE,
            exception.getMessage());

        // Verify that the exception data is correctly set.
        assertNotNull("application code should not null", exception.getApplicationCode());
        assertEquals("exception data is not set.", APPLICATION_CODE, exception.getApplicationCode());

        // Verify that there is a cause
        assertNotNull("Should have cause.", exception.getCause());
        assertSame("Cause should be identical.", CAUSE, exception.getCause());
    }
}
