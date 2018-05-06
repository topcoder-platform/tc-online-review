/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 *
 * @(#)AddressExceptionTest.java
 */
package com.topcoder.message.email;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>Unit test for <code>AddressException</code> Exception.</p>
 *
 * @author  smell
 * @version 3.0
 */
public class AddressExceptionTest extends TestCase {

    /** Message String used to create the exception for test. */
    private String message = "Testing the exception";

    /** Cause used to create the exception for test. */
    private Throwable cause = new Throwable("foo");

    /**
     * Tests creating AddressException with null message. Null message is permitted.
     */
    public void testCreationNullMsg() {
        try {
            throw new AddressException(null);
        } catch (AddressException e) {
            assertNull("The message is not valid.", e.getMessage());
        }
    }

    /**
     * Tests creating AddressException with message.
     */
    public void testCreationMsg() {
        try {
            throw new AddressException(message);
        } catch (AddressException e) {
            assertEquals("The message is not valid.", message, e.getMessage());
        }
    }

    /**
     * Tests creating AddressException with null message and cause. Null message is permitted.
     */
    public void testCreationNullMsgCz() {
        try {
            throw new AddressException(null, cause);
        } catch (AddressException e) {
            // Check the message, note that getMessage() does not return null, it returns
            // "null, caused by [cause.message]" instead, where [cause.message] represent the message of the cause.
            assertEquals("The message is not valid", null, e.getMessage());

            // Check the cause.
            assertEquals("The cause is not valid", cause, e.getCause());
        }
    }

    /**
     * Tests creating AddressException with message and null cause. Null cause is permitted.
     */
    public void testCreationMsgNullCz() {
        try {
            throw new AddressException(message, null);
        } catch (AddressException e) {
            // Check the message
            assertEquals("The message is not valid", message, e.getMessage());

            // Check the cause.
            assertNull("The cause is not valid", e.getCause());
        }
    }

    /**
     * Tests creating AddressException with message and cause.
     */
    public void testCreationMsgCz() {
        try {
            throw new AddressException(message, cause);
        } catch (AddressException e) {
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
        return new TestSuite(AddressExceptionTest.class);
    }

}
