/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.failuretests;

import com.topcoder.file.transfer.message.MessageType;

import junit.framework.TestCase;


/**
 * Test <code>MessageType</code> class for failure.
 *
 * @author fairytale
 * @version 1.0
 */
public class FailureMessageTypeTest extends TestCase {
    /**
     * Test <code>MessageType(String)</code> method for failure. <code>NullPointerException</code> should be thrown if
     * argument is null.
     */
    public void testMessageTypeForNullPointerException() {
        try {
            new MessageType(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>MessageType(String)</code> method for failure. <code>IllegalArgumentException</code> should be thrown
     * if string is empty.
     */
    public void testMessageTypeForIllegalArgumentException() {
        try {
            new MessageType(" ");
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }
}
