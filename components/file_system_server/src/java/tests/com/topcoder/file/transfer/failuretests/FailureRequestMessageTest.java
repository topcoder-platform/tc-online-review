/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.failuretests;

import com.topcoder.file.transfer.message.MessageType;
import com.topcoder.file.transfer.message.RequestMessage;

import junit.framework.TestCase;


/**
 * Test <code>RequestMessage</code> class for failure.
 *
 * @author fairytale
 * @version 1.0
 */
public class FailureRequestMessageTest extends TestCase {
    /**
     * Test <code>RequestMessage(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if handlerId is null.
     */
    public void testRequestMessageForNullPointerException_2handlerId() {
        try {
            String handlerId = null;
            String requestId = "4321";
            new RequestMessage(handlerId, requestId);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>RequestMessage(String, String)</code> method for failure. <code>NullPointerException</code> should be
     * thrown if requestId is null.
     */
    public void testRequestMessageForNullPointerException_2requestId() {
        try {
            String handlerId = "1234";
            String requestId = null;
            new RequestMessage(handlerId, requestId);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>RequestMessage(String, String, MessageType)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if handlerId is null.
     */
    public void testRequestMessageForNullPointerException_3handlerId() {
        try {
            String handlerId = null;
            String requestId = "4321";
            MessageType type = new MessageType("type");
            new RequestMessage(handlerId, requestId, type);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>RequestMessage(String, String, MessageType)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if requestId is null.
     */
    public void testRequestMessageForNullPointerException_3requestId() {
        try {
            String handlerId = "1234";
            String requestId = null;
            MessageType type = new MessageType("type");
            new RequestMessage(handlerId, requestId, type);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>RequestMessage(String, String, MessageType)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if type is null.
     */
    public void testRequestMessageForNullPointerException_3type() {
        try {
            String handlerId = "1234";
            String requestId = "4321";
            MessageType type = null;
            new RequestMessage(handlerId, requestId, type);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>RequestMessage(String, String, MessageType, Object)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if handlerId is null.
     */
    public void testRequestMessageForNullPointerException_4handlerId() {
        try {
            String handlerId = null;
            String requestId = "4321";
            MessageType type = new MessageType("type");
            Object arg = "arg";
            new RequestMessage(handlerId, requestId, type, arg);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>RequestMessage(String, String, MessageType, Object)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if requestId is null.
     */
    public void testRequestMessageForNullPointerException_4requestId() {
        try {
            String handlerId = "1234";
            String requestId = null;
            MessageType type = new MessageType("type");
            Object arg = "arg";
            new RequestMessage(handlerId, requestId, type, arg);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>RequestMessage(String, String, MessageType, Object)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if type is null.
     */
    public void testRequestMessageForNullPointerException_4type() {
        try {
            String handlerId = "1234";
            String requestId = "4321";
            MessageType type = null;
            Object arg = "arg";
            new RequestMessage(handlerId, requestId, type, arg);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>RequestMessage(String, String, MessageType, Object[])</code> method for failure.
     * <code>NullPointerException</code> should be thrown if handlerId is null.
     */
    public void testRequestMessageForNullPointerException_handlerId() {
        try {
            String handlerId = null;
            String requestId = "4321";
            MessageType type = new MessageType("type");
            Object[] args = new String[3];
            new RequestMessage(handlerId, requestId, type, args);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>RequestMessage(String, String, MessageType, Object[])</code> method for failure.
     * <code>NullPointerException</code> should be thrown if requestId is null.
     */
    public void testRequestMessageForNullPointerException_requestId() {
        try {
            String handlerId = "1234";
            String requestId = null;
            MessageType type = new MessageType("type");
            Object[] args = new String[3];
            new RequestMessage(handlerId, requestId, type, args);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>RequestMessage(String, String, MessageType, Object[])</code> method for failure.
     * <code>NullPointerException</code> should be thrown if type is null.
     */
    public void testRequestMessageForNullPointerException_type() {
        try {
            String handlerId = "1234";
            String requestId = "4321";
            MessageType type = null;
            Object[] args = new String[3];
            new RequestMessage(handlerId, requestId, type, args);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }
}
