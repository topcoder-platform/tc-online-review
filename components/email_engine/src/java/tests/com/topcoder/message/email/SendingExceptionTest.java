/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 *
 * @(#)SendingExceptionTest.java
 */
package com.topcoder.message.email;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>Unit test for <code>SendingException</code> Exception.</p>
 *
 * @author  smell
 * @version 3.0
 */
public class SendingExceptionTest extends TestCase {

    /** Message String used to create the exception for test. */
    private String message = "Testing the exception";

    /** Cause used to create the exception for test. */
    private Throwable cause = new Throwable("foo");

    /**
     * Tests creating SendingException with null message. Null message is permitted.
     */
    public void testCreationNullMsg() {
        try {
            throw new SendingException(null);
        } catch (SendingException e) {
            assertNull("The message is not valid.", e.getMessage());
        }
    }

    /**
     * Tests creating SendingException with message.
     */
    public void testCreationMsg() {
        try {
            throw new SendingException(message);
        } catch (SendingException e) {
            assertEquals("The message is not valid.", message, e.getMessage());
        }
    }

    /**
     * Tests creating SendingException with null message and cause. Null message is permitted.
     */
    public void testCreationNullMsgCz() {
        try {
            throw new SendingException(null, cause);
        } catch (SendingException e) {
            // Check the message, note that getMessage() does not return null, it returns
            // "null, caused by [cause.message]" instead, where [cause.message] represent the message of the cause.
            assertEquals("The message is not valid", null, e.getMessage());

            // Check the cause.
            assertEquals("The cause is not valid", cause, e.getCause());
        }
    }

    /**
     * Tests creating SendingException with message and null cause. Null cause is permitted.
     */
    public void testCreationMsgNullCz() {
        try {
            throw new SendingException(message, null);
        } catch (SendingException e) {
            // Check the message
            assertEquals("The message is not valid", message, e.getMessage());

            // Check the cause.
            assertNull("The cause is not valid", e.getCause());
        }
    }

    /**
     * Tests creating SendingException with message and cause.
     */
    public void testCreationMsgCz() {
        try {
            throw new SendingException(message, cause);
        } catch (SendingException e) {
            // Check the message, note that getMessage() does not return the message passed to the constructor, it
            // returns "[message], caused by [cause.message]" instead, where [message] and [cause.message] represent the
            // message passed to the constructor and the message of the cause respectively.
            assertEquals("The message is not valid", message, e.getMessage());

            // Check the cause.
            assertEquals("The cause is not valid", cause, e.getCause());
        }
    }

    /**
     * Returns the suit to run the test.
     *
     * @return  suite to run the test
     */
    public static Test suite() {
        return new TestSuite(SendingExceptionTest.class);
    }

}
