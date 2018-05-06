/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

import junit.framework.TestCase;

import com.topcoder.util.errorhandling.BaseCriticalException;
import com.topcoder.util.errorhandling.ExceptionData;

/**
 * Unit tests for {@link ObjectFactoryException}.
 *
 * @author mgmg, TCSDEVELOPER
 * @version 2.2
 */
public class ObjectFactoryExceptionUnitTest extends TestCase {
    /**
     * <p>
     * Represents the error message for testing.
     * </p>
     */
    private static final String MESSAGE = "error message";

    /**
     * <p>
     * Represents the <code>Exception</code> instance used for testing.
     * </p>
     */
    private static final Exception CAUSE = new Exception();

    /**
     * <p>
     * Represents the <code>ExceptionData</code> instance used for testing.
     * </p>
     */
    private static final ExceptionData EXCEPTIONDATA = new ExceptionData();

    /**
     * <p>
     * Tests accuracy of <code>ObjectFactoryException(String)</code>
     * constructor.
     * </p>
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testCtor1Accuracy() {
        ObjectFactoryException exception = new ObjectFactoryException(MESSAGE);
        assertNotNull("Unable to instantiate ObjectFactoryException.", exception);
        assertEquals("Error message is not properly propagated to super class.", MESSAGE, exception.getMessage());
    }

    /**
     * <p>
     * Tests accuracy of <code>ObjectFactoryException(String, Throwable)</code>
     * constructor.
     * </p>
     * <p>
     * Verifies the error message and the cause are properly propagated.
     * </p>
     */
    public void testCtor2Accuracy() {
        ObjectFactoryException exception = new ObjectFactoryException(MESSAGE, CAUSE);
        assertNotNull("Unable to instantiate ObjectFactoryException.", exception);
        assertEquals("Error message is not properly propagated to super class.", MESSAGE, exception.getMessage());
        assertEquals("Cause is not properly propagated to super class.", CAUSE, exception.getCause());
    }

    /**
     * <p>
     * Tests accuracy of
     * <code>ObjectFactoryException(String, ExceptionData)</code> constructor.
     * </p>
     * <p>
     * Verifies the error message and the exceptionData are properly propagated.
     * </p>
     */
    public void testCtor3Accuracy() {
        ObjectFactoryException exception = new ObjectFactoryException(MESSAGE, EXCEPTIONDATA);
        assertNotNull("Unable to instantiate ObjectFactoryException.", exception);
        assertEquals("Error message is not properly propagated to super class.", MESSAGE, exception.getMessage());
        assertEquals("ExceptionData is not properly propagated to super class.", EXCEPTIONDATA.getErrorCode(),
                exception.getErrorCode());
    }

    /**
     * <p>
     * Tests accuracy of
     * <code>ObjectFactoryException(String, Throwable, ExceptionData)</code>
     * constructor.
     * </p>
     * <p>
     * Verifies the error message, the cause, and the exceptionData are properly
     * propagated.
     * </p>
     */
    public void testCtor4Accuracy() {
        ObjectFactoryException exception = new ObjectFactoryException(MESSAGE, CAUSE, EXCEPTIONDATA);
        assertNotNull("Unable to instantiate ObjectFactoryException.", exception);
        assertEquals("Error message is not properly propagated to super class.", MESSAGE, exception.getMessage());
        assertEquals("Cause is not properly propagated to super class.", CAUSE, exception.getCause());
        assertEquals("ExceptionData is not properly propagated to super class.", EXCEPTIONDATA.getErrorCode(),
                exception.getErrorCode());
    }

    /**
     * <p>
     * Inheritance test, verifies <code>ObjectFactoryException</code> subclasses
     * <code>BaseCriticalException</code>.
     * </p>
     */
    public void test_Inheritance() {
        assertTrue("ObjectFactoryException does not subclass BaseCriticalException.", new ObjectFactoryException(
                MESSAGE) instanceof BaseCriticalException);
    }
}
