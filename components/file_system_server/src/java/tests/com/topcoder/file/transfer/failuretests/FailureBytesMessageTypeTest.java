/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.failuretests;

import com.topcoder.file.transfer.message.BytesMessageType;

import junit.framework.TestCase;


/**
 * Test <code>BytesMessageType</code> class for failure.
 *
 * @author fairytale
 * @version 1.0
 */
public class FailureBytesMessageTypeTest extends TestCase {
    /**
     * Test <code>BytesMessageType(String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if argument is null.
     */
    public void testBytesMessageTypeForNullPointerException() {
        try {
            new BytesMessageType(null);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>BytesMessageType(String)</code> method for failure. <code>IllegalArgumentException</code> should be
     * thrown if string is empty.
     */
    public void testBytesMessageTypeForIllegalArgumentException() {
        try {
            new BytesMessageType(" ");
            fail("the IllegalArgumentException should be thrown!");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }
}
