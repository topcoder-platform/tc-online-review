/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * Unit tests for {@link ExceptionHelper} class.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
public class ExceptionHelperUnitTests {
    /**
     * <p>
     * Represents the message.
     * </p>
     */
    private String message = "An error occurred.";

    /**
     * <p>
     * Represents the cause.
     * </p>
     */
    private Exception cause;

    /**
     * <p>
     * Represents the exception class.
     * </p>
     */
    private Class<ConfigurationException> exceptionClass;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ExceptionHelperUnitTests.class);
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Before
    public void setUp() throws Exception {
        exceptionClass = ConfigurationException.class;
        cause = new Exception("Exception for testing.");
    }

    /**
     * <p>
     * Accuracy test for the method <code>constructException(Class&lt;T&gt; exceptionClass, String message)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_constructException1() {
        ConfigurationException res = ExceptionHelper.constructException(exceptionClass, message);

        assertEquals("'constructException' should be correct.", message, res.getMessage());
        assertNull("'constructException' should be correct.", res.getCause());
    }

    /**
     * <p>
     * Accuracy test for the method <code>constructException(Class&lt;T&gt; exceptionClass, String message,
     * Throwable cause)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_constructException2() {
        ConfigurationException res = ExceptionHelper.constructException(exceptionClass, message, cause);

        assertEquals("'constructException' should be correct.", message, res.getMessage());
        assertSame("'constructException' should be correct.", cause, res.getCause());
    }
}
