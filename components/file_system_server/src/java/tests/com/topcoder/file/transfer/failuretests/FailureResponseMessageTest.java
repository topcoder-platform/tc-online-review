/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.failuretests;

import com.topcoder.file.transfer.message.MessageType;
import com.topcoder.file.transfer.message.ResponseMessage;

import junit.framework.TestCase;


/**
 * Test <code>ResponseMessage</code> class for failure.
 *
 * @author fairytale
 * @version 1.0
 */
public class FailureResponseMessageTest extends TestCase {
    /**
     * Test <code>ResponseMessage(String, String)</code> method for failure. <code>NullPointerException</code> should
     * be thrown if handlerId is null.
     */
    public void testResponseMessageForNullPointerException_2handlerId() {
        try {
            String handlerId = null;
            String requestId = "4321";
            new ResponseMessage(handlerId, requestId);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>ResponseMessage(String, String)</code> method for failure. <code>NullPointerException</code> should
     * be thrown if requestId is null.
     */
    public void testResponseMessageForNullPointerException_2requestId() {
        try {
            String handlerId = "1234";
            String requestId = null;
            new ResponseMessage(handlerId, requestId);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>ResponseMessage(String, String, MessageType)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if handlerId is null.
     */
    public void testResponseMessageForNullPointerException_3handlerId() {
        try {
            String handlerId = null;
            String requestId = "4321";
            MessageType type = new MessageType("type");
            new ResponseMessage(handlerId, requestId, type);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>ResponseMessage(String, String, MessageType)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if requestId is null.
     */
    public void testResponseMessageForNullPointerException_3requestId() {
        try {
            String handlerId = "1234";
            String requestId = null;
            MessageType type = new MessageType("type");
            new ResponseMessage(handlerId, requestId, type);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>ResponseMessage(String, String, MessageType)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if type is null.
     */
    public void testResponseMessageForNullPointerException_3type() {
        try {
            String handlerId = "1234";
            String requestId = "4321";
            MessageType type = null;
            new ResponseMessage(handlerId, requestId, type);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>ResponseMessage(String, String, MessageType, Object)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if handlerId is null.
     */
    public void testResponseMessageForNullPointerException_4handlerId() {
        try {
            String handlerId = null;
            String requestId = "4321";
            MessageType type = new MessageType("type");
            Object arg = "arg";
            new ResponseMessage(handlerId, requestId, type, arg);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>ResponseMessage(String, String, MessageType, Object)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if requestId is null.
     */
    public void testResponseMessageForNullPointerException_4requestId() {
        try {
            String handlerId = "1234";
            String requestId = null;
            MessageType type = new MessageType("type");
            Object arg = "arg";
            new ResponseMessage(handlerId, requestId, type, arg);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>ResponseMessage(String, String, MessageType, Object)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if type is null.
     */
    public void testResponseMessageForNullPointerException_4type() {
        try {
            String handlerId = "1234";
            String requestId = "4321";
            MessageType type = null;
            Object arg = "arg";
            new ResponseMessage(handlerId, requestId, type, arg);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>ResponseMessage(String, String, MessageType, Exception)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if handlerId is null.
     */
    public void testResponseMessageForNullPointerException_handlerId() {
        try {
            String handlerId = null;
            String requestId = "4321";
            MessageType type = new MessageType("type");
            Exception exception = new Exception();
            new ResponseMessage(handlerId, requestId, type, exception);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>ResponseMessage(String, String, MessageType, Exception)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if requestId is null.
     */
    public void testResponseMessageForNullPointerException_requestId() {
        try {
            String handlerId = "1234";
            String requestId = null;
            MessageType type = new MessageType("type");
            Exception exception = new Exception();
            new ResponseMessage(handlerId, requestId, type, exception);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>ResponseMessage(String, String, MessageType, Exception)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if type is null.
     */
    public void testResponseMessageForNullPointerException_type() {
        try {
            String handlerId = "1234";
            String requestId = "4321";
            MessageType type = null;
            Exception exception = new Exception();
            new ResponseMessage(handlerId, requestId, type, exception);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }

    /**
     * Test <code>ResponseMessage(String, String, MessageType, Exception)</code> method for failure.
     * <code>NullPointerException</code> should be thrown if args is null.
     */
    public void testResponseMessageForNullPointerException_args() {
        try {
            String handlerId = "1234";
            String requestId = "4321";
            MessageType type = new MessageType("type");
            Exception exception = null;
            new ResponseMessage(handlerId, requestId, type, exception);
            fail("the NullPointerException should be thrown!");
        } catch (NullPointerException e) {
            // Good
        }
    }
}
