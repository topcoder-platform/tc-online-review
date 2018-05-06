/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.errorhandling.BaseCriticalException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This class tests the <code>ReviewManagementException</code> class.
 * </p>
 *
 * <p>
 * <em>Changes in 1.2:</em>
 * <ol>
 * <li>Added test cases for the new constructors.</li>
 * <li>Fixed the code to meet the TopCoder standard.</li>
 * </ol>
 * </p>
 *
 * @author icyriver, sparemax
 * @version 1.2
 */
public class ReviewManagementExceptionTest extends TestCase {
    /**
     * <p>
     * Represents a detail message.
     * </p>
     *
     * @since 1.2
     */
    private static final String DETAIL_MESSAGE = "detail";

    /**
     * <p>
     * Represents an error cause.
     * </p>
     *
     * @since 1.2
     */
    private static final Throwable CAUSE = new Exception("UnitTests");

    /**
     * <p>
     * Represents the exception data.
     * </p>
     *
     * @since 1.2
     */
    private static final ExceptionData EXCEPTION_DATA = new ExceptionData();

    /**
     * <p>
     * Represents the application code set in exception data.
     * </p>
     *
     * @since 1.2
     */
    private static final String APPLICATION_CODE = "Accuracy";

    /**
     * <p>
     * Initializes the exception data.
     * </p>
     *
     * @since 1.2
     */
    static {
        EXCEPTION_DATA.setApplicationCode(APPLICATION_CODE);
    }

    /**
     * <p>
     * Returns the test suite of <code>ReviewManagementExceptionTest</code>.
     * </p>
     *
     * @return the test suite of <code>ReviewManagementExceptionTest</code>.
     */
    public static Test suite() {
        return new TestSuite(ReviewManagementExceptionTest.class);
    }

    /**
     * <p>
     * <code>ReviewManagementException</code> should be subclass of <code>BaseCriticalException</code>.
     * </p>
     *
     * @since 1.2
     */
    public void testInheritance() {
        assertTrue("ReviewManagementException should be subclass of BaseCriticalException.",
            ReviewManagementException.class.getSuperclass() == BaseCriticalException.class);
    }

    /**
     * <p>
     * Tests accuracy of <code>ReviewManagementException(String)</code> constructor.<br>
     * The detail error message should be properly set.
     * </p>
     */
    public void testCtor1() {
        ReviewManagementException exception = new ReviewManagementException(DETAIL_MESSAGE);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message should be identical.", DETAIL_MESSAGE, exception.getMessage());
    }

    /**
     * <p>
     * Tests accuracy of <code>ReviewManagementException(String, Throwable)</code> constructor.<br>
     * The detail error message and the original cause of error should be properly set.
     * </p>
     */
    public void testCtor2() {
        ReviewManagementException exception = new ReviewManagementException(DETAIL_MESSAGE, CAUSE);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message with cause should be properly set.", DETAIL_MESSAGE, exception
            .getMessage());

        // Verify that there is a cause
        assertNotNull("Should have cause.", exception.getCause());
        assertSame("Cause should be identical.", CAUSE, exception.getCause());
    }

    /**
     * <p>
     * Tests accuracy of <code>ReviewManagementException(String, ExceptionData)</code> constructor.<br>
     * The detail error message and the exception data should be properly set.
     * </p>
     *
     * @since 1.2
     */
    public void testCtor3() {
        ReviewManagementException exception = new ReviewManagementException(DETAIL_MESSAGE, EXCEPTION_DATA);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message with cause should be properly set.", DETAIL_MESSAGE, exception
            .getMessage());

        // Verify that the exception data is correctly set.
        assertNotNull("Application code should not null.", exception.getApplicationCode());
        assertEquals("Exception data is not set.", APPLICATION_CODE, exception.getApplicationCode());
    }

    /**
     * <p>
     * Tests accuracy of <code>ReviewManagementException(String, Throwable, ExceptionData)</code> constructor.<br>
     * The detail error message, the cause exception and the exception data should be properly set.
     * </p>
     *
     * @since 1.2
     */
    public void testCtor4() {
        ReviewManagementException exception = new ReviewManagementException(DETAIL_MESSAGE, CAUSE, EXCEPTION_DATA);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message with cause should be properly set.", DETAIL_MESSAGE, exception
            .getMessage());

        // Verify that the exception data is correctly set.
        assertNotNull("Application code should not null.", exception.getApplicationCode());
        assertEquals("Exception data is not set.", APPLICATION_CODE, exception.getApplicationCode());

        // Verify that there is a cause
        assertNotNull("Should have cause.", exception.getCause());
        assertSame("Cause should be identical.", CAUSE, exception.getCause());
    }
}
