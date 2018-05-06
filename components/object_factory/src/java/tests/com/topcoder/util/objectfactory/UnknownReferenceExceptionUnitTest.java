/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

import junit.framework.TestCase;

import com.topcoder.util.errorhandling.ExceptionData;

/**
 * Unit tests for {@link UnknownReferenceException}.
 *
 * @author mgmg, TCSDEVELOPER
 * @version 2.2
 */
public class UnknownReferenceExceptionUnitTest extends TestCase {
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
     * Tests accuracy of <code>UnknownReferenceException(String)</code>
     * constructor.
     * </p>
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testCtor1Accuracy() {
        UnknownReferenceException exception = new UnknownReferenceException(MESSAGE);
        assertNotNull("Unable to instantiate UnknownReferenceException.", exception);
        assertEquals("Error message is not properly propagated to super class.", MESSAGE, exception.getMessage());
    }

    /**
     * <p>
     * Tests accuracy of
     * <code>UnknownReferenceException(String, Throwable)</code> constructor.
     * </p>
     * <p>
     * Verifies the error message and the cause are properly propagated.
     * </p>
     */
    public void testCtor2Accuracy() {
        UnknownReferenceException exception = new UnknownReferenceException(MESSAGE, CAUSE);
        assertNotNull("Unable to instantiate UnknownReferenceException.", exception);
        assertEquals("Error message is not properly propagated to super class.", MESSAGE, exception.getMessage());
        assertEquals("Cause is not properly propagated to super class.", CAUSE, exception.getCause());
    }

    /**
     * <p>
     * Tests accuracy of
     * <code>UnknownReferenceException(String, ExceptionData)</code>
     * constructor.
     * </p>
     * <p>
     * Verifies the error message and the exceptionData are properly propagated.
     * </p>
     */
    public void testCtor3Accuracy() {
        UnknownReferenceException exception = new UnknownReferenceException(MESSAGE, EXCEPTIONDATA);
        assertNotNull("Unable to instantiate UnknownReferenceException.", exception);
        assertEquals("Error message is not properly propagated to super class.", MESSAGE, exception.getMessage());
        assertEquals("ExceptionData is not properly propagated to super class.", EXCEPTIONDATA.getErrorCode(),
                exception.getErrorCode());
    }

    /**
     * <p>
     * Tests accuracy of
     * <code>UnknownReferenceException(String, Throwable, ExceptionData)</code>
     * constructor.
     * </p>
     * <p>
     * Verifies the error message, the cause, and the exceptionData are properly
     * propagated.
     * </p>
     */
    public void testCtor4Accuracy() {
        UnknownReferenceException exception = new UnknownReferenceException(MESSAGE, CAUSE, EXCEPTIONDATA);
        assertNotNull("Unable to instantiate UnknownReferenceException.", exception);
        assertEquals("Error message is not properly propagated to super class.", MESSAGE, exception.getMessage());
        assertEquals("Cause is not properly propagated to super class.", CAUSE, exception.getCause());
        assertEquals("ExceptionData is not properly propagated to super class.", EXCEPTIONDATA.getErrorCode(),
                exception.getErrorCode());
    }

    /**
     * <p>
     * Inheritance test, verifies <code>UnknownReferenceException</code>
     * subclasses <code>SpecificationFactoryException</code>.
     * </p>
     */
    public void test_Inheritance() {
        assertTrue("UnknownReferenceException does not subclass SpecificationFactoryException.",
                new UnknownReferenceException(MESSAGE) instanceof SpecificationFactoryException);
    }
}
