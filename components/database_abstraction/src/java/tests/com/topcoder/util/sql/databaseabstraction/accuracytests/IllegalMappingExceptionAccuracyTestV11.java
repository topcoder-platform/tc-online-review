/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.accuracytests;

import com.topcoder.util.sql.databaseabstraction.IllegalMappingException;

import junit.framework.TestCase;


/**
 * <p>
 * Unit test cases for <code>IllegalMappingException</code>.
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
public class IllegalMappingExceptionAccuracyTestV11 extends TestCase {
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
    public void testIllegalMappingException1() {
        IllegalMappingException ce = new IllegalMappingException(ERROR_MESSAGE);

        assertNotNull("Unable to instantiate IllegalMappingException.", ce);
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
    public void testIllegalMappingException2() {
        Exception cause = new Exception();
        IllegalMappingException ce = new IllegalMappingException(ERROR_MESSAGE, cause);

        assertNotNull("Unable to instantiate IllegalMappingException.", ce);
        assertEquals("Cause is not properly propagated to super class.", cause, ce.getCause());
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     * 
     * <p>
     * Verifies IllegalMappingException subclasses Exception.
     * </p>
     */
    public void testIllegalMappingExceptionInheritance1() {
        assertTrue("IllegalMappingException does not subclass Exception.",
            new IllegalMappingException(ERROR_MESSAGE) instanceof Exception);
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     * 
     * <p>
     * Verifies IllegalMappingException subclasses Exception.
     * </p>
     */
    public void testIllegalMappingExceptionInheritance2() {
        assertTrue("IllegalMappingException does not subclass Exception.",
            new IllegalMappingException(ERROR_MESSAGE, new Exception()) instanceof Exception);
    }
}
