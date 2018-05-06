/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Unit tests for {@link LogException} class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class LogExceptionTest extends TestCase {
    /**
     * <p>
     * An error message used for testing.
     * </p>
     */
    private static final String ERROR_MESSAGE = "error message";

    /**
     * <p>
     * A Throwable instance used for testing.
     * </p>
     */
    private static final Throwable THROWABLE = new Throwable();

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(LogExceptionTest.class);
    }

    /**
     * <p>
     * Tests constructor for LogException(Throwable) for accuracy.
     * </p>
     *
     * <p>
     * Verify that LogException(Throwable) is correct.
     * </p>
     */
    public void testLogExceptionThrowable() {
        LogException exception = new LogException(THROWABLE);
        assertNotNull("The LogException instance should not be null.", exception);
        assertSame("The Throwable instance is incorrect.", THROWABLE, exception.getCause());
    }

    /**
     * <p>
     * Tests constructor for LogException(String) for accuracy.
     * </p>
     *
     * <p>
     * Verify that LogException(String) is correct.
     * </p>
     */
    public void testLogExceptionString() {
        LogException exception = new LogException(ERROR_MESSAGE);
        assertNotNull("The LogException instance should not be null.", exception);
        assertEquals("The message is incorrect.", ERROR_MESSAGE, exception.getMessage());
        assertNull("The Throwable instance should be null.", exception.getCause());
    }

    /**
     * <p>
     * Tests constructor for LogException(String, Throwable) for accuracy.
     * </p>
     *
     * <p>
     * Verify that LogException(String, Throwable) is correct.
     * </p>
     */
    public void testLogExceptionStringThrowable() {
        LogException exception = new LogException(ERROR_MESSAGE, THROWABLE);
        assertNotNull("The LogException instance should not be null.", exception);
        assertSame("The Throwable instance is incorrect.", THROWABLE, exception.getCause());
        assertTrue("The message is incorrect.", exception.getMessage().startsWith(ERROR_MESSAGE));
    }

}
