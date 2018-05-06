/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Unit test cases for SchedulingException.
 * </p>
 *
 * <p>
 * This class is pretty simple. The test cases simply verifies the exception can be instantiated with the error message
 * and cause properly propagated, and that it comes with correct inheritance.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class SchedulingExceptionTests extends TestCase {
    /**
     * <p>
     * The error message used for testing.
     * </p>
     */
    private static final String ERROR_MESSAGE = "test error message";

    /**
     * <p>
     * An exception instance used to create the SchedulingException.
     * </p>
     */
    private static final Exception CAUSE_EXCEPTION = new NullPointerException();

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(SchedulingExceptionTests.class);
    }

    /**
     * <p>
     * Creation test.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testSchedulingException1() {
        SchedulingException exception = new SchedulingException(ERROR_MESSAGE);
        assertNotNull("Unable to instantiate SchedulingException.", exception);
        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, exception.getMessage());
    }

    /**
     * <p>
     * Creation test.
     * </p>
     *
     * <p>
     * Verifies the error message and the cause are properly propagated.
     * </p>
     */
    public void testSchedulingException2() {
        SchedulingException exception = new SchedulingException(ERROR_MESSAGE, CAUSE_EXCEPTION);

        assertNotNull("Unable to instantiate SchedulingException.", exception);
        assertTrue("The error message should match", exception.getMessage().indexOf(ERROR_MESSAGE) >= 0);
        assertEquals("Cause is not properly propagated to super class.", CAUSE_EXCEPTION, exception.getCause());
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     *
     * <p>
     * Verifies SchedulingException subclasses Exception.
     * </p>
     */
    public void testSchedulingExceptionInheritance1() {
        SchedulingException exception = new SchedulingException(ERROR_MESSAGE);
        assertTrue("SchedulingException does not subclass Exception.", exception instanceof Exception);
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     *
     * <p>
     * Verifies SchedulingException subclasses Exception.
     * </p>
     */
    public void testSchedulingExceptionInheritance2() {
        SchedulingException exception = new SchedulingException(ERROR_MESSAGE, CAUSE_EXCEPTION);
        assertTrue("SchedulingException does not subclass Exception.", exception instanceof Exception);
    }
}