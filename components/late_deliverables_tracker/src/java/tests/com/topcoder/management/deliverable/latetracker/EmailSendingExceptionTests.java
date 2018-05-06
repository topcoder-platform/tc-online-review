/*
 * Copyright (C) 2010, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import junit.framework.TestCase;

import com.topcoder.util.errorhandling.ExceptionData;


/**
 * <p>
 * Unit test cases for <code>EmailSendingException</code> class.
 * </p>
 *
 * <p>
 * <em>Changes in 1.2:</em>
 * <ol>
 * <li>Moved from com.topcoder.management.deliverable.latetracker.processors to
 * com.topcoder.management.deliverable.latetracker package.</li>
 * </ol>
 * </p>
 *
 * @author myxgyy, sparemax
 * @version 1.2
 */
public class EmailSendingExceptionTests extends TestCase {
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
     * <code>EmailSendingException</code> should be
     * subclass of <code>LateDeliverablesProcessingException</code>.
     * </p>
     */
    public void testInheritance() {
        assertTrue(
            "EmailSendingException should be subclass of LateDeliverablesProcessingException.",
            EmailSendingException.class.getSuperclass()
            == LateDeliverablesProcessingException.class);
    }

    /**
     * Tests accuracy of <code>EmailSendingException(String)</code> constructor.
     * The detail error message should be correct.
     */
    public void testEmailSendingExceptionStringAccuracy() {
        // Construct EmailSendingException with a detail message
        EmailSendingException exception = new EmailSendingException(DETAIL_MESSAGE);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message should be identical.",
            DETAIL_MESSAGE, exception.getMessage());
    }

    /**
     * Tests accuracy of <code>EmailSendingException(String, ExceptionData)</code>
     * constructor. The detail error message and the exception data should be correct.
     */
    public void testEmailSendingExceptionStringExceptionDataAccuracy() {
        // Construct EmailSendingException with a detail message and a
        // cause
        EmailSendingException exception = new EmailSendingException(DETAIL_MESSAGE,
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
     * Tests accuracy of <code>EmailSendingException(String, Throwable)</code>
     * constructor. The detail error message and the original cause of error should be
     * correct.
     */
    public void testEmailSendingExceptionStringThrowableAccuracy() {
        // Construct EmailSendingException with a detail message and a
        // cause
        EmailSendingException exception = new EmailSendingException(DETAIL_MESSAGE,
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
     * <code>EmailSendingException(String, Throwable, ExceptionData)</code>
     * constructor. The detail error message, the cause exception and the exception data
     * should be correct.
     */
    public void testEmailSendingExceptionStringThrowableExceptionDataAccuracy() {
        // Construct EmailSendingException with a detail message and a
        // cause
        EmailSendingException exception = new EmailSendingException(DETAIL_MESSAGE,
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
