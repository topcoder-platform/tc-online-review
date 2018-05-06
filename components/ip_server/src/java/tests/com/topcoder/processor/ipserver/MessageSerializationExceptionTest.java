/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import com.topcoder.processor.ipserver.message.MessageSerializationException;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test cases for MessageSerializationException.
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
public class MessageSerializationExceptionTest extends TestCase {
    /** The error message used for testing. */
    private static final String ERROR_MESSAGE = "test error message";

    /** An exception instance used to create the MessageSerializationException. */
    private final Throwable cause = new Throwable();

    /**
     * Test MessageSerializationException constructor with correct message, the message can be retrieved correctly
     * later.
     */
    public void testCtor1() {
        MessageSerializationException e = new MessageSerializationException(
            MessageSerializationExceptionTest.ERROR_MESSAGE);

        assertNotNull("Unable to instantiate MessageSerializationExceptionTest.", e);
        assertEquals("Error message is not properly propagated to super class.",
            MessageSerializationExceptionTest.ERROR_MESSAGE, e.getMessage());
    }

    /**
     * Test MessageSerializationException constructor with null message, no exception is expected.
     */
    public void testCtor1WithNullReason() {
        // NO exception
        new MessageSerializationException(null);
    }

    /**
     * Test MessageSerializationException constructor with correct error message, cause, the message and cause can be
     * retrieved correctly later.
     */
    public void testCtor2() {
        MessageSerializationException ce = new MessageSerializationException(
            MessageSerializationExceptionTest.ERROR_MESSAGE, cause);

        assertNotNull("Unable to instantiate MessageSerializationExceptionTest.", ce);
        assertTrue("Error message is not properly propagated to super class.",
                ce.getMessage().indexOf(MessageSerializationExceptionTest.ERROR_MESSAGE) >= 0);
        assertEquals("The inner exception should match", ce.getCause(), cause);
    }

    /**
     * Test MessageSerializationException constructor with null message, no exception is expected.
     */
    public void testCtor2WithNullReason() {
        // No exception
        new MessageSerializationException(null, cause);
    }

    /**
     * Test MessageSerializationException constructor with error message and null inner exception, no exception
     * is expected.
     */
    public void testCtor2WithNullCause() {
        // No exception
        new MessageSerializationException(MessageSerializationExceptionTest.ERROR_MESSAGE, null);
    }
}
