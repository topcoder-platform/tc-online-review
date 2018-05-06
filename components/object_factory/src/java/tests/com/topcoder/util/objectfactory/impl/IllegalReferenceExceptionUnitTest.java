/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl;

import junit.framework.TestCase;

import com.topcoder.util.errorhandling.ExceptionData;
import com.topcoder.util.objectfactory.SpecificationFactoryException;

/**
 * Unit tests for {@link IllegalReferenceException}.
 *
 * @author mgmg, TCSDEVELOPER
 * @version 2.2
 */
public class IllegalReferenceExceptionUnitTest extends TestCase {
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
     * Tests accuracy of <code>IllegalReferenceException(String)</code>
     * constructor.
     * </p>
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testCtor1Accuracy() {
        IllegalReferenceException exception = new IllegalReferenceException(MESSAGE);
        assertNotNull("Unable to instantiate IllegalReferenceException.", exception);
        assertEquals("Error message is not properly propagated to super class.", MESSAGE, exception.getMessage());
    }

    /**
     * <p>
     * Tests accuracy of
     * <code>IllegalReferenceException(String, Throwable)</code> constructor.
     * </p>
     * <p>
     * Verifies the error message and the cause are properly propagated.
     * </p>
     */
    public void testCtor2Accuracy() {
        IllegalReferenceException exception = new IllegalReferenceException(MESSAGE, CAUSE);
        assertNotNull("Unable to instantiate IllegalReferenceException.", exception);
        assertEquals("Error message is not properly propagated to super class.", MESSAGE, exception.getMessage());
        assertEquals("Cause is not properly propagated to super class.", CAUSE, exception.getCause());
    }

    /**
     * <p>
     * Tests accuracy of
     * <code>IllegalReferenceException(String, ExceptionData)</code>
     * constructor.
     * </p>
     * <p>
     * Verifies the error message and the exceptionData are properly propagated.
     * </p>
     */
    public void testCtor3Accuracy() {
        IllegalReferenceException exception = new IllegalReferenceException(MESSAGE, EXCEPTIONDATA);
        assertNotNull("Unable to instantiate IllegalReferenceException.", exception);
        assertEquals("Error message is not properly propagated to super class.", MESSAGE, exception.getMessage());
        assertEquals("ExceptionData is not properly propagated to super class.", EXCEPTIONDATA.getErrorCode(),
                exception.getErrorCode());
    }

    /**
     * <p>
     * Tests accuracy of
     * <code>IllegalReferenceException(String, Throwable, ExceptionData)</code>
     * constructor.
     * </p>
     * <p>
     * Verifies the error message, the cause, and the exceptionData are properly
     * propagated.
     * </p>
     */
    public void testCtor4Accuracy() {
        IllegalReferenceException exception = new IllegalReferenceException(MESSAGE, CAUSE, EXCEPTIONDATA);
        assertNotNull("Unable to instantiate IllegalReferenceException.", exception);
        assertEquals("Error message is not properly propagated to super class.", MESSAGE, exception.getMessage());
        assertEquals("Cause is not properly propagated to super class.", CAUSE, exception.getCause());
        assertEquals("ExceptionData is not properly propagated to super class.", EXCEPTIONDATA.getErrorCode(),
                exception.getErrorCode());
    }

    /**
     * <p>
     * Inheritance test, verifies <code>IllegalReferenceException</code>
     * subclasses <code>SpecificationFactoryException</code>.
     * </p>
     */
    public void test_Inheritance() {
        assertTrue("IllegalReferenceException does not subclass SpecificationFactoryException.",
                new IllegalReferenceException(MESSAGE) instanceof SpecificationFactoryException);
    }
}
