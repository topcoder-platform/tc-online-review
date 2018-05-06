/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;

/**
 * Accuracy tests for PhaseNotSupportedException class.
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>add new test for new methood</li>
 * </ul>
 * </p>
 * @author bose_java, microsky
 * @version 1.6.1
 */
public class PhaseNotSupportedExceptionTest extends TestCase {
    /** constant to hold error message for testing. */
    private static final String ERROR_MSG = "error msg";

    /** constant to hold an exception for testing. */
    private static final Exception EXCEPTION = new Exception();

    /** constant to hold an exception for testing. */
    private static final ExceptionData EXCEPTION_DATA = new ExceptionData();

    /**
     * Test that PhaseNotSupportedException(String) constructor propagates the
     * message argument.
     */
    public void testPhaseNotSupportedExceptionString() {
        PhaseNotSupportedException exc = new PhaseNotSupportedException(
                        ERROR_MSG);
        assertEquals("error msg not propagated to super.", ERROR_MSG, exc
                        .getMessage());
    }

    /**
     * Test that PhaseNotSupportedException(String, Throwable) constructor
     * propagates the arguments.
     */
    public void testPhaseNotSupportedExceptionStringThrowable() {
        PhaseNotSupportedException exc = new PhaseNotSupportedException(ERROR_MSG,
                        EXCEPTION);
        assertEquals("error msg not propagated to super.", ERROR_MSG, exc
            .getMessage());
        assertEquals("Throwable not propagated to super.", EXCEPTION, exc
                        .getCause());
    }

    /**
     * Test that PhaseNotSupportedException(String, ExceptionData) constructor
     * propagates the arguments.
     */
    public void testPhaseNotSupportedExceptionStringExceptionData() {
        PhaseNotSupportedException exc = new PhaseNotSupportedException(ERROR_MSG,
            EXCEPTION_DATA);
        assertEquals("error msg not propagated to super.", ERROR_MSG, exc
            .getMessage());
    }

    /**
     * Test that PhaseNotSupportedException(String, Throwable, ExceptionData) constructor
     * propagates the arguments.
     */
    public void testPhaseNotSupportedExceptionStringThrowableExceptionData() {
        PhaseNotSupportedException exc = new PhaseNotSupportedException(ERROR_MSG,
                        EXCEPTION, EXCEPTION_DATA);
        assertEquals("error msg not propagated to super.", ERROR_MSG, exc
            .getMessage());
        assertEquals("Throwable not propagated to super.", EXCEPTION, exc
                        .getCause());
    }
}
