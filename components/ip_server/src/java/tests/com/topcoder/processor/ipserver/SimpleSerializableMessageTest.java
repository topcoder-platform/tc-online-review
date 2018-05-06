/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.serializable.SimpleSerializableMessage;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test cases for SimpleSerializableMessage.
 * </p>
 *
 * @author zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class SimpleSerializableMessageTest extends TestCase {

    /**
     * Test if this class implements the Message interface.
     */
    public void testClassType() {
        Object obj = new SimpleSerializableMessage("id", "id");
        assertTrue("SimpleSerializableMessage does not implement Message interface.", obj instanceof Message);
    }

    /**
     * Test if Constructor works correctly. No exception should be thrown for valid input.
     */
    public void testConstructor() {
        // No exception
        new SimpleSerializableMessage("id", "id");
        new SimpleSerializableMessage("", "id");
        new SimpleSerializableMessage("id", "");
        new SimpleSerializableMessage("    ", "id");
        new SimpleSerializableMessage("id", "    ");
    }

    /**
     * Test if the constructor handles null handler ID correctly.
     */
    public void testConstructorFailure1() {
        try {
            new SimpleSerializableMessage(null, "id");
            fail("NullPointerException is not thrown for null handler ID.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test if the constructor handles null request ID correctly.
     */
    public void testConstructorFailure2() {
        try {
            new SimpleSerializableMessage("id", null);
            fail("NullPointerException is not thrown for null request ID.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test if the getHandlerId() works properly.
     */
    public void testGetHandlerId() {
        Message msg = new SimpleSerializableMessage("Handler ID", "Request ID");
        assertEquals("The getHandlerId() method doest not work properly.", "Handler ID", msg.getHandlerId());
    }

    /**
     * Test if the getRequestId() works properly.
     */
    public void testGetRequestId() {
        Message msg = new SimpleSerializableMessage("Handler ID", "Request ID");
        assertEquals("The getRequestId() method doest not work properly.", "Request ID", msg.getRequestId());
    }

    /**
     * Test if the getSerializerType() works properly.
     */
    public void testGetSerializerType() {
        Message msg = new SimpleSerializableMessage("Handler ID", "Request ID");
        assertEquals("The getSerializerType() method doest not work properly.",
            "com.topcoder.processor.ipserver.message.serializable.SerializableMessageSerializer",
            msg.getSerializerType());
    }
}
