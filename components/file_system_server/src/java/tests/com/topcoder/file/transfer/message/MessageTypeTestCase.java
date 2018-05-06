/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.message;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This is used to test MessageType for correctness.
 * @author FireIce
 * @version 1.0
 */
public class MessageTypeTestCase extends TestCase {

    /**
     * Test Constructor, if the argument is null, throw NullPointerException.
     */
    public void testCtorNPE() {
        try {
            new MessageType(null);
            fail("if the argument is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test Constructor, if the argument is empty String, throw IllegalArgumentException.
     */
    public void testCtorIAE() {
        try {
            new MessageType(" ");
            fail("if the argument is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test Constructor, if the argument is valid, created successfully with no exception.
     */
    public void testCtorSuccess() {
        MessageType messageType = new MessageType("TYPE");
        assertEquals("the type value should be TYPE", messageType.getType(), "TYPE");
    }

    /**
     * Tests <code>getType()</code> method.
     */
    public void testGetType() {
        assertEquals("not correct type value", MessageType.UPLOAD_FILE.getType(), "UPLOAD_FILE");
    }

    /**
     * Tests <code>equals()</code> method.
     */
    public void testEquals() {
        assertFalse("should return false", MessageType.UPLOAD_FILE.equals(null));
        assertFalse("should return false", MessageType.UPLOAD_FILE.equals(new Object()));
        assertFalse("should return false", MessageType.UPLOAD_FILE.equals(MessageType.ADD_FILE_TO_GROUP));
        assertTrue("should return true", MessageType.UPLOAD_FILE.equals(MessageType.UPLOAD_FILE));
        assertTrue("should return true", MessageType.UPLOAD_FILE.equals(new MessageType("UPLOAD_FILE")));
        assertFalse("should return false", MessageType.UPLOAD_FILE.equals(new BytesMessageType("UPLOAD_FILE")));
    }

    /**
     * Tests <code>hashCode()</code> method.
     */
    public void testHashCode() {
        String type = "typeName";
        assertEquals("should be equal to the type's hashCode", type.hashCode(), new MessageType(type).hashCode());
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(MessageTypeTestCase.class);
    }
}
