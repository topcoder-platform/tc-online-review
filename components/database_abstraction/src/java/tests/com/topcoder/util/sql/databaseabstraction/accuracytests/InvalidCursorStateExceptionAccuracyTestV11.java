/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.accuracytests;

import junit.framework.TestCase;

import com.topcoder.util.sql.databaseabstraction.InvalidCursorStateException;


/**
 * <p>
 * Unit test cases for <code>InvalidCursorStateException</code>.
 * </p>
 * 
 * <p>
 * This class is pretty simple. The test cases simply verifies the exception can be instantiated with the error message
 * and cause properly propagated, and that it comes with correct inheritance.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class InvalidCursorStateExceptionAccuracyTestV11 extends TestCase {
    /**
     * <p>
     * The error message used for testing.
     * </p>
     */
    private static final String ERROR_MESSAGE = "test error message";

    /**
     * <p>
     * Creation test.
     * </p>
     * 
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testInvalidCursorStateException1() {
        InvalidCursorStateException ce = new InvalidCursorStateException(ERROR_MESSAGE);

        assertNotNull("Unable to instantiate InvalidCursorStateException.", ce);
        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, ce.getMessage());
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
    public void testInvalidCursorStateException2() {
        Exception cause = new Exception();
        InvalidCursorStateException ce = new InvalidCursorStateException(ERROR_MESSAGE, cause);

        assertNotNull("Unable to instantiate InvalidCursorStateException.", ce);
        assertEquals("Cause is not properly propagated to super class.", cause, ce.getCause());
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     * 
     * <p>
     * Verifies InvalidCursorStateException subclasses Exception.
     * </p>
     */
    public void testInvalidCursorStateExceptionInheritance1() {
        assertTrue("InvalidCursorStateException does not subclass Exception.",
            new InvalidCursorStateException(ERROR_MESSAGE) instanceof Exception);
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     * 
     * <p>
     * Verifies InvalidCursorStateException subclasses Exception.
     * </p>
     */
    public void testInvalidCursorStateExceptionInheritance2() {
        assertTrue("InvalidCursorStateException does not subclass Exception.",
            new InvalidCursorStateException(ERROR_MESSAGE, new Exception()) instanceof Exception);
    }
}
