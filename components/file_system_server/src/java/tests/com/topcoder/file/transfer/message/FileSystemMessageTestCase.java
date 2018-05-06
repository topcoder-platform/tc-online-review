/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.message;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This is used to test FileSystemMessage for correctness.
 * @author FireIce
 * @version 1.0
 */
public class FileSystemMessageTestCase extends TestCase {
    /**
     * Test <code>FileSystemMessage(String, String)</code> constructor, if any argument is null,throw
     * NullPointerException.
     */
    public void testCtorStringStringNPE() {
        try {
            new MockFileSystemMessage(null, "");
            fail("if handleId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new MockFileSystemMessage("", null);
            fail("if requestId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>FileSystemMessage(String, String)</code> constructor, if any argument is valid, created with fields
     * set.
     */
    public void testCtorStringStringSuccess() {
        String handleId = "HandleId";
        String requestId = "RequestId";
        FileSystemMessage fileSystemMessage = new MockFileSystemMessage(handleId, requestId);
        assertEquals("not correct handleId", fileSystemMessage.getHandlerId(), handleId);
        assertEquals("not correct requestId", fileSystemMessage.getRequestId(), requestId);
        assertNull("type field should be null", fileSystemMessage.getType());
    }

    /**
     * Test <code>FileSystemMessage(String, String, MessageType)</code> constructor, if any argument is null,throw
     * NullPointerException.
     */
    public void testCtorStringStringMessageTypeNPE() {

        try {
            new MockFileSystemMessage(null, "", MessageType.UPLOAD_FILE);
            fail("if handleId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new MockFileSystemMessage("", null, MessageType.UPLOAD_FILE);
            fail("if requestId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new MockFileSystemMessage("", "", null);
            fail("if requestId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>FileSystemMessage(String, String, MessageType)</code> constructor, if any argument is valid, created
     * with fields set.
     */
    public void testCtorStringStringMessageTypeSuccess() {
        String handleId = "HandleId";
        String requestId = "RequestId";
        MessageType messageType = MessageType.ADD_FILE_TO_GROUP;
        FileSystemMessage fileSystemMessage = new MockFileSystemMessage(handleId, requestId, messageType);
        assertEquals("not correct handleId", fileSystemMessage.getHandlerId(), handleId);
        assertEquals("not correct requestId", fileSystemMessage.getRequestId(), requestId);
        assertTrue("type field should be " + messageType.getType(), messageType.equals(fileSystemMessage.getType()));
    }

    /**
     * Test <code>getType</code> method.
     */
    public void testGetType() {
        String handleId = "HandleId";
        String requestId = "RequestId";
        MessageType messageType = MessageType.ADD_FILE_TO_GROUP;
        FileSystemMessage fileSystemMessage = new MockFileSystemMessage(handleId, requestId);
        assertNull("type field should be null", fileSystemMessage.getType());
        fileSystemMessage = new MockFileSystemMessage(handleId, requestId, messageType);
        assertTrue("type field should be " + messageType.getType(), messageType.equals(fileSystemMessage.getType()));
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(FileSystemMessageTestCase.class);
    }
}
