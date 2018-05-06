/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * <p>
 * This class tests the <code>ReviewEntityNotFoundException</code> class.
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
public class ReviewEntityNotFoundExceptionTest extends TestCase {
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
     * Represents the id.
     * </p>
     *
     * @since 1.2
     */
    private static final long ID = 1;

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
     * Returns the test suite of <code>ReviewEntityNotFoundExceptionTest</code>.
     * </p>
     *
     * @return the test suite of <code>ReviewEntityNotFoundExceptionTest</code>.
     */
    public static Test suite() {
        return new TestSuite(ReviewEntityNotFoundExceptionTest.class);
    }

    /**
     * <p>
     * <code>ReviewEntityNotFoundException</code> should be subclass of <code>ReviewPersistenceException</code>.
     * </p>
     *
     * @since 1.2
     */
    public void testInheritance() {
        assertTrue("ReviewEntityNotFoundException should be subclass of ReviewPersistenceException.",
            ReviewEntityNotFoundException.class.getSuperclass() == ReviewPersistenceException.class);
    }

    /**
     * <p>
     * Tests accuracy of <code>ReviewEntityNotFoundException(String, long)</code> constructor.<br>
     * The detail error message should be properly set.
     * </p>
     */
    public void testCtor1() {
        ReviewEntityNotFoundException exception = new ReviewEntityNotFoundException(DETAIL_MESSAGE, ID);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message should be identical.", DETAIL_MESSAGE, exception.getMessage());

        assertEquals("Id should be identical.", ID, exception.getId());
    }

    /**
     * <p>
     * Tests accuracy of <code>ReviewEntityNotFoundException(String, Throwable, long)</code> constructor.<br>
     * The detail error message and the original cause of error should be properly set.
     * </p>
     *
     * @since 1.2
     */
    public void testCtor2() {
        ReviewEntityNotFoundException exception = new ReviewEntityNotFoundException(DETAIL_MESSAGE, CAUSE, ID);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message with cause should be properly set.", DETAIL_MESSAGE, exception
            .getMessage());

        // Verify that there is a cause
        assertNotNull("Should have cause.", exception.getCause());
        assertSame("Cause should be identical.", CAUSE, exception.getCause());

        assertEquals("Id should be identical.", ID, exception.getId());
    }

    /**
     * <p>
     * Tests accuracy of <code>ReviewEntityNotFoundException(String, ExceptionData, long)</code> constructor.<br>
     * The detail error message and the exception data should be properly set.
     * </p>
     *
     * @since 1.2
     */
    public void testCtor3() {
        ReviewEntityNotFoundException exception = new ReviewEntityNotFoundException(DETAIL_MESSAGE, EXCEPTION_DATA,
            ID);

        // Verify that there is a detail message
        assertNotNull("Should have message.", exception.getMessage());
        assertEquals("Detailed error message with cause should be properly set.", DETAIL_MESSAGE, exception
            .getMessage());

        // Verify that the exception data is correctly set.
        assertNotNull("Application code should not null.", exception.getApplicationCode());
        assertEquals("Exception data is not set.", APPLICATION_CODE, exception.getApplicationCode());

        assertEquals("Id should be identical.", ID, exception.getId());
    }

    /**
     * <p>
     * Tests accuracy of <code>ReviewEntityNotFoundException(String, Throwable, ExceptionData, long)</code>
     * constructor.<br>
     * The detail error message, the cause exception and the exception data should be properly set.
     * </p>
     *
     * @since 1.2
     */
    public void testCtor4() {
        ReviewEntityNotFoundException exception = new ReviewEntityNotFoundException(DETAIL_MESSAGE, CAUSE,
            EXCEPTION_DATA, ID);

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

        assertEquals("Id should be identical.", ID, exception.getId());
    }

    /**
     * <p>
     * Tests accuracy of <code>getId()</code> method.<br>
     * The result should be correct.
     * </p>
     */
    public void test_getId() {
        ReviewEntityNotFoundException exception = new ReviewEntityNotFoundException(DETAIL_MESSAGE, ID);

        assertEquals("'getId' should be identical.", ID, exception.getId());
    }
}
