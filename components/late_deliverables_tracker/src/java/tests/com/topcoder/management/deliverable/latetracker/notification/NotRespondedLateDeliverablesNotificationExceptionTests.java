/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.notification;

import junit.framework.TestCase;

import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackingException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * Unit test cases for <code>NotRespondedLateDeliverablesNotificationException</code> class.
 * </p>
 *
 * @author sparemax
 * @version 1.2
 * @since 1.2
 */
public class NotRespondedLateDeliverablesNotificationExceptionTests extends TestCase {
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
     * <code>NotRespondedLateDeliverablesNotificationException</code> should be subclass of
     * <code>LateDeliverablesTrackingException</code>.
     * </p>
     */
    public void testInheritance() {
        assertTrue(
            "NotRespondedLateDeliverablesNotificationException should be"
                + " subclass of LateDeliverablesTrackingException.",
            NotRespondedLateDeliverablesNotificationException.class.getSuperclass()
            == LateDeliverablesTrackingException.class);
    }

    /**
     * Tests accuracy of <code>NotRespondedLateDeliverablesNotificationException(String)</code> constructor. The
     * detail error message should be correct.
     */
    public void testNotRespondedLateDeliverablesNotificationExceptionStringAccuracy() {
        // Construct NotRespondedLateDeliverablesNotificationException with a detail message
        NotRespondedLateDeliverablesNotificationException exception =
            new NotRespondedLateDeliverablesNotificationException(DETAIL_MESSAGE);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message should be identical.", DETAIL_MESSAGE, exception.getMessage());
    }

    /**
     * Tests accuracy of <code>NotRespondedLateDeliverablesNotificationException(String, ExceptionData)</code>
     * constructor. The detail error message and the exception data should be correct.
     */
    public void testNotRespondedLateDeliverablesNotificationExceptionStringExceptionDataAccuracy() {
        // Construct NotRespondedLateDeliverablesNotificationException with a detail message and a cause
        NotRespondedLateDeliverablesNotificationException exception =
            new NotRespondedLateDeliverablesNotificationException(DETAIL_MESSAGE, EXCEPTION_DATA);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message with cause should be correct.", DETAIL_MESSAGE, exception.getMessage());

        // Verify that the exception data is correctly set.
        assertNotNull("application code should not null", exception.getApplicationCode());
        assertEquals("exception data is not set.", APPLICATION_CODE, exception.getApplicationCode());
    }

    /**
     * Tests accuracy of <code>NotRespondedLateDeliverablesNotificationException(String, Throwable)</code>
     * constructor. The detail error message and the original cause of error should be correct.
     */
    public void testNotRespondedLateDeliverablesNotificationExceptionStringThrowableAccuracy() {
        // Construct NotRespondedLateDeliverablesNotificationException with a detail message and a
        // cause
        NotRespondedLateDeliverablesNotificationException exception =
            new NotRespondedLateDeliverablesNotificationException(DETAIL_MESSAGE, CAUSE);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message with cause should be correct.", DETAIL_MESSAGE, exception.getMessage());

        // Verify that there is a cause
        assertNotNull("Should have cause.", exception.getCause());
        assertSame("Cause should be identical.", CAUSE, exception.getCause());
    }

    /**
     * Tests accuracy of
     * <code>NotRespondedLateDeliverablesNotificationException(String, Throwable, ExceptionData)</code> constructor.
     * The detail error message, the cause exception and the exception data should be correct.
     */
    public void testNotRespondedLateDeliverablesNotificationExceptionStringThrowableExceptionDataAccuracy() {
        // Construct NotRespondedLateDeliverablesNotificationException with a detail message and a cause
        NotRespondedLateDeliverablesNotificationException exception =
            new NotRespondedLateDeliverablesNotificationException(DETAIL_MESSAGE, CAUSE, EXCEPTION_DATA);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message with cause should be correct.", DETAIL_MESSAGE, exception.getMessage());

        // Verify that the exception data is correctly set.
        assertNotNull("application code should not null", exception.getApplicationCode());
        assertEquals("exception data is not set.", APPLICATION_CODE, exception.getApplicationCode());

        // Verify that there is a cause
        assertNotNull("Should have cause.", exception.getCause());
        assertSame("Cause should be identical.", CAUSE, exception.getCause());
    }
}
