/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test cases for ProcessingException.
 * </p>
 *
 * <p>
 * This class is pretty simple. The test cases simply verifies the exception can be instantiated with the error message
 * and cause properly propagated.
 * </p>
 *
 * @author zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class ProcessingExceptionTest extends TestCase {
    /** The error message used for testing. */
    private static final String ERROR_MESSAGE = "test error message";

    /** An exception instance used to create the ProcessingException. */
    private final Throwable cause = new Throwable();

    /**
     * Test ProcessingException constructor with correct message, the message can be retrieved correctly later.
     */
    public void testCtor1() {
        ProcessingException cde = new ProcessingException(ProcessingExceptionTest.ERROR_MESSAGE);

        assertNotNull("Unable to instantiate ProcessingExceptionTest.", cde);
        assertEquals("Error message is not properly propagated to super class.",
            ProcessingExceptionTest.ERROR_MESSAGE, cde.getMessage());
    }

    /**
     * Test ProcessingException constructor with null reason, no exception is expected.
     */
    public void testCtor1WithNullReason() {
        // NO exception
        new ProcessingException(null);
    }

    /**
     * Test ProcessingException constructor with correct error message, cause, the message and cause can be
     * retrieved correctly later.
     */
    public void testCtor2() {
        ProcessingException ce = new ProcessingException(ProcessingExceptionTest.ERROR_MESSAGE, cause);

        assertNotNull("Unable to instantiate ProcessingExceptionTest.", ce);
        assertTrue("Error message is not properly propagated to super class.",
                ce.getMessage().indexOf(ProcessingExceptionTest.ERROR_MESSAGE) >= 0);
        assertEquals("The inner exception should match", ce.getCause(), cause);
    }

    /**
     * Test ProcessingException constructor with null reason, no exception is expected.
     */
    public void testCtor2WithNullReason() {
        // No exception
        new ProcessingException(null, cause);
    }

    /**
     * Test ProcessingException constructor with error message and null inner exception, no exception is expected.
     */
    public void testCtor2WithNullCause() {
        // No exception
        new ProcessingException(ProcessingExceptionTest.ERROR_MESSAGE, null);
    }
}
