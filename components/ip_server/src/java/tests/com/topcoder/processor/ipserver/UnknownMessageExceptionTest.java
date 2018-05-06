/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import com.topcoder.processor.ipserver.message.UnknownMessageException;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test cases for UnknownMessageException.
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
public class UnknownMessageExceptionTest extends TestCase {
    /** The error message used for testing. */
    private static final String ERROR_MESSAGE = "test error message";

    /** An exception instance used to create the UnknownMessageException. */
    private final Throwable cause = new Throwable();

    /**
     * Test UnknownMessageException constructor with correct message, the message can be retrieved correctly later.
     */
    public void testCtor1() {
        UnknownMessageException e = new UnknownMessageException(UnknownMessageExceptionTest.ERROR_MESSAGE);

        assertNotNull("Unable to instantiate UnknownMessageExceptionTest.", e);
        assertEquals("Error message is not properly propagated to super class.",
            UnknownMessageExceptionTest.ERROR_MESSAGE, e.getMessage());
    }

    /**
     * Test UnknownMessageException constructor with null message, no exception is expected.
     */
    public void testCtor1WithNullReason() {
        // NO exception
        new UnknownMessageException(null);
    }

    /**
     * Test UnknownMessageException constructor with correct error message, cause, the message and cause can be
     * retrieved correctly later.
     */
    public void testCtor2() {
        UnknownMessageException ce = new UnknownMessageException(UnknownMessageExceptionTest.ERROR_MESSAGE, cause);

        assertNotNull("Unable to instantiate UnknownMessageExceptionTest.", ce);
        assertTrue("Error message is not properly propagated to super class.",
                ce.getMessage().indexOf(UnknownMessageExceptionTest.ERROR_MESSAGE) >= 0);
        assertEquals("The inner exception should match", ce.getCause(), cause);
    }

    /**
     * Test UnknownMessageException constructor with null message, no exception is expected.
     */
    public void testCtor2WithNullReason() {
        // No exception
        new UnknownMessageException(null, cause);
    }

    /**
     * Test UnknownMessageException constructor with error message and null inner exception, no exception is expected.
     */
    public void testCtor2WithNullCause() {
        // No exception
        new UnknownMessageException(UnknownMessageExceptionTest.ERROR_MESSAGE, null);
    }
}
