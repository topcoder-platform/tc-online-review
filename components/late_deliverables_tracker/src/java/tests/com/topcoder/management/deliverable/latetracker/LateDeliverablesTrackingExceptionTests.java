/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import junit.framework.TestCase;

import com.topcoder.util.errorhandling.BaseCriticalException;
import com.topcoder.util.errorhandling.ExceptionData;


/**
 * <p>
 * Unit test cases for <code>LateDeliverablesTrackingException</code> class.
 * </p>
 *
 * @author myxgyy
 * @version 1.0
 */
public class LateDeliverablesTrackingExceptionTests extends TestCase {
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
     * <code>LateDeliverablesTrackingException</code> should be
     * subclass of <code>BaseCriticalException</code>.
     * </p>
     */
    public void testInheritance() {
        assertTrue(
            "LateDeliverablesTrackingException should be subclass of BaseCriticalException.",
            LateDeliverablesTrackingException.class.getSuperclass()
            == BaseCriticalException.class);
    }

    /**
     * Tests accuracy of <code>LateDeliverablesTrackingException(String)</code> constructor.
     * The detail error message should be correct.
     */
    public void testLateDeliverablesTrackingExceptionStringAccuracy() {
        // Construct LateDeliverablesTrackingException with a detail message
        LateDeliverablesTrackingException exception = new LateDeliverablesTrackingException(DETAIL_MESSAGE);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message should be identical.",
            DETAIL_MESSAGE, exception.getMessage());
    }

    /**
     * Tests accuracy of <code>LateDeliverablesTrackingException(String, ExceptionData)</code>
     * constructor. The detail error message and the exception data should be correct.
     */
    public void testLateDeliverablesTrackingExceptionStringExceptionDataAccuracy() {
        // Construct LateDeliverablesTrackingException with a detail message and a
        // cause
        LateDeliverablesTrackingException exception = new LateDeliverablesTrackingException(DETAIL_MESSAGE,
                EXCEPTION_DATA);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message with cause should be correct.",
            DETAIL_MESSAGE, exception.getMessage());

        // Verify that the exception data is correctly set.
        assertNotNull("application code should not null",
            exception.getApplicationCode());
        assertEquals("exception data is not set.", APPLICATION_CODE,
            exception.getApplicationCode());
    }

    /**
     * Tests accuracy of <code>LateDeliverablesTrackingException(String, Throwable)</code>
     * constructor. The detail error message and the original cause of error should be
     * correct.
     */
    public void testLateDeliverablesTrackingExceptionStringThrowableAccuracy() {
        // Construct LateDeliverablesTrackingException with a detail message and a
        // cause
        LateDeliverablesTrackingException exception = new LateDeliverablesTrackingException(DETAIL_MESSAGE,
                CAUSE);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message with cause should be correct.",
            DETAIL_MESSAGE, exception.getMessage());

        // Verify that there is a cause
        assertNotNull("Should have cause.", exception.getCause());
        assertSame("Cause should be identical.", CAUSE,
            exception.getCause());
    }

    /**
     * Tests accuracy of
     * <code>LateDeliverablesTrackingException(String, Throwable, ExceptionData)</code>
     * constructor. The detail error message, the cause exception and the exception data
     * should be correct.
     */
    public void testLateDeliverablesTrackingExceptionStringThrowableExceptionDataAccuracy() {
        // Construct LateDeliverablesTrackingException with a detail message and a
        // cause
        LateDeliverablesTrackingException exception = new LateDeliverablesTrackingException(DETAIL_MESSAGE,
                CAUSE, EXCEPTION_DATA);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message with cause should be correct.",
            DETAIL_MESSAGE, exception.getMessage());

        // Verify that the exception data is correctly set.
        assertNotNull("application code should not null",
            exception.getApplicationCode());
        assertEquals("exception data is not set.", APPLICATION_CODE,
            exception.getApplicationCode());

        // Verify that there is a cause
        assertNotNull("Should have cause.", exception.getCause());
        assertSame("Cause should be identical.", CAUSE,
            exception.getCause());
    }
}
