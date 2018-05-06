/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import com.topcoder.processor.ipserver.message.MessageCreationException;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test cases for MessageCreationException.
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
public class MessageCreationExceptionTest extends TestCase {
    /** The error message used for testing. */
    private static final String ERROR_MESSAGE = "test error message";

    /** An exception instance used to create the MessageCreationException. */
    private final Throwable cause = new Throwable();

    /**
     * Test MessageCreationException constructor with correct message, the message can be retrieved correctly later.
     */
    public void testCtor1() {
        MessageCreationException e = new MessageCreationException(MessageCreationExceptionTest.ERROR_MESSAGE);

        assertNotNull("Unable to instantiate MessageCreationExceptionTest.", e);
        assertEquals("Error message is not properly propagated to super class.",
            MessageCreationExceptionTest.ERROR_MESSAGE, e.getMessage());
    }

    /**
     * Test MessageCreationException constructor with null message, no exception is expected.
     */
    public void testCtor1WithNullReason() {
        // NO exception
        new MessageCreationException(null);
    }

    /**
     * Test MessageCreationException constructor with correct error message, cause, the message and cause can be
     * retrieved correctly later.
     */
    public void testCtor2() {
        MessageCreationException ce = new MessageCreationException(MessageCreationExceptionTest.ERROR_MESSAGE, cause);

        assertNotNull("Unable to instantiate MessageCreationExceptionTest.", ce);
        assertTrue("Error message is not properly propagated to super class.",
                ce.getMessage().indexOf(MessageCreationExceptionTest.ERROR_MESSAGE) >= 0);
        assertEquals("The inner exception should match", ce.getCause(), cause);
    }

    /**
     * Test MessageCreationException constructor with null message, no exception is expected.
     */
    public void testCtor2WithNullReason() {
        // No exception
        new MessageCreationException(null, cause);
    }

    /**
     * Test MessageCreationException constructor with error message and null inner exception, no exception is expected.
     */
    public void testCtor2WithNullCause() {
        // No exception
        new MessageCreationException(MessageCreationExceptionTest.ERROR_MESSAGE, null);
    }
}
