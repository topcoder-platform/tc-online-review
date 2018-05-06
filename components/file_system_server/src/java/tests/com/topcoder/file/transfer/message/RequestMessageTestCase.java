/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.message;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This is used to test RequestMessage for correctness.
 * @author FireIce
 * @version 1.0
 */
public class RequestMessageTestCase extends TestCase {
    /**
     * Represents the handle id.
     */
    private static final String HANDLE_ID = "handleId";

    /**
     * Represents the request id.
     */
    private static final String REQUEST_ID = "request";

    /**
     * Represents a valid String that is not null and empty.
     */
    private static final String VALID_STR = "valid";

    /**
     * Represents the group name.
     */
    private static final String GROUP_NAME = "groupName";

    /**
     * Represents the file id.
     */
    private static final String FILE_ID = "fileId";

    /**
     * Test <code>RequestMessage(String, String)</code> constructor, if any argument is null,throw
     * NullPointerException.
     */
    public void testCtorStringStringNPE() {
        try {
            new RequestMessage(null, "");
            fail("if handleId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new RequestMessage("", null);
            fail("if requestId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>RequestMessage(String, String)</code> constructor, if any argument is valid, created with fields
     * set.
     */
    public void testCtorStringStringSuccess() {
        RequestMessage requestMessage = new RequestMessage(HANDLE_ID, REQUEST_ID);
        assertEquals("not correct handleId", requestMessage.getHandlerId(), HANDLE_ID);
        assertEquals("not correct requestId", requestMessage.getRequestId(), REQUEST_ID);
        assertNull("type field should be null", requestMessage.getType());
        assertNull("args field should be null", requestMessage.getArgs());
    }

    /**
     * Test <code>RequestMessage(String, String, MessageType)</code> constructor, if any argument is null,throw
     * NullPointerException.
     */
    public void testCtorStringStringMessageTypeNPE() {

        try {
            new RequestMessage(null, "", MessageType.REMOVE_FILE);
            fail("if handleId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new RequestMessage("", null, MessageType.REMOVE_FILE);
            fail("if requestId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new RequestMessage("", "", null);
            fail("if requestId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>RequestMessage(String, String, MessageType, Object)</code> constructor, if args should be something
     * different than a null object array is null,throw IllegalArgumentException.
     */
    public void testCtorStringStringMessageTypeIAE() {
        try {
            new RequestMessage("", "", MessageType.REMOVE_FILE);
            fail("the args shouldn't be null, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test <code>RequestMessage(String, String, MessageType)</code> constructor, if any argument is valid, created
     * with fields set.
     */
    public void testCtorStringStringMessageTypeSuccess() {
        MessageType messageType = MessageType.REMOVE_FILE;
        RequestMessage requestMessage = new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, FILE_ID);
        assertEquals("not correct handleId", requestMessage.getHandlerId(), HANDLE_ID);
        assertEquals("not correct requestId", requestMessage.getRequestId(), REQUEST_ID);
        assertTrue("type field should be " + messageType.getType(), messageType.equals(requestMessage.getType()));
        assertNotNull("args field shouldn't null", requestMessage.getArgs());
        assertEquals("args array's length is 1", 1, requestMessage.getArgs().length);
        assertEquals("args array contains " + FILE_ID, requestMessage.getArgs()[0], FILE_ID);
    }

    /**
     * Test <code>RequestMessage(String, String, MessageType, Object)</code> constructor, if any argument except null
     * is null,throw NullPointerException.
     */
    public void testCtorStringStringMessageTypeObjectNPE() {

        try {
            new RequestMessage(null, "", MessageType.REMOVE_FILE, null);
            fail("if handleId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new RequestMessage("", null, MessageType.REMOVE_FILE, null);
            fail("if requestId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new RequestMessage("", "", null, null);
            fail("if requestId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>RequestMessage(String, String, MessageType, Object)</code> constructor, if args should be something
     * different than a null object array is null,throw IllegalArgumentException.
     */
    public void testCtorStringStringMessageTypeObjectIAE() {
        try {
            new RequestMessage("", "", MessageType.REMOVE_FILE, new Object());
            fail("the args shouldn't contains non-String, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test <code>RequestMessage(String, String, MessageType, Object)</code> constructor, if any argument is valid,
     * created with fields set.
     */
    public void testCtorStringStringMessageTypeObjectSuccess() {
        MessageType messageType = MessageType.REMOVE_FILE;
        RequestMessage requestMessage = new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, FILE_ID);
        assertEquals("not correct handleId", requestMessage.getHandlerId(), HANDLE_ID);
        assertEquals("not correct requestId", requestMessage.getRequestId(), REQUEST_ID);
        assertTrue("type field should be " + messageType.getType(), messageType.equals(requestMessage.getType()));
        assertNotNull("args field shouldn't null", requestMessage.getArgs());
        assertEquals("args array's length is 1", 1, requestMessage.getArgs().length);
        assertEquals("args array contains " + FILE_ID, requestMessage.getArgs()[0], FILE_ID);
    }

    /**
     * Test <code>RequestMessage(String, String, MessageType, Object[])</code> constructor, if any argument except
     * null is null,throw NullPointerException.
     */
    public void testCtorStringStringMessageTypeObjectsNPE() {

        try {
            new RequestMessage(null, "", MessageType.REMOVE_FILE, null);
            fail("if handleId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new RequestMessage("", null, MessageType.REMOVE_FILE, null);
            fail("if requestId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new RequestMessage("", "", null, null);
            fail("if requestId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>RequestMessage(String, String, MessageType, Object[])</code> constructor, if args should be
     * something different than a null object array is null,throw IllegalArgumentException.
     */
    public void testCtorStringStringMessageTypeObjectsIAE() {
        try {
            new RequestMessage("", "", MessageType.REMOVE_FILE, new Object[0]);
            fail("the args shouldn't be empty array, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test <code>RequestMessage(String, String, MessageType, Object[])</code> constructor, if any argument is valid,
     * created with fields set.
     */
    public void testCtorStringStringMessageTypeObjectsSuccess() {
        MessageType messageType = MessageType.REMOVE_FILE;
        RequestMessage requestMessage = new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, FILE_ID);
        assertEquals("not correct handleId", requestMessage.getHandlerId(), HANDLE_ID);
        assertEquals("not correct requestId", requestMessage.getRequestId(), REQUEST_ID);
        assertTrue("type field should be " + messageType.getType(), messageType.equals(requestMessage.getType()));
        assertNotNull("args field shouldn't null", requestMessage.getArgs());
        assertEquals("args array's length is 1", 1, requestMessage.getArgs().length);
        assertEquals("args array contains " + FILE_ID, requestMessage.getArgs()[0], FILE_ID);
    }

    /**
     * Test <code>getArgs()</code> method.
     */
    public void testGetArgs() {
        assertNull("should be null", new RequestMessage(HANDLE_ID, REQUEST_ID).getArgs());
        RequestMessage requestMessage = new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.REMOVE_FILE, FILE_ID);
        assertNotNull("args field shouldn't null", requestMessage.getArgs());
        assertEquals("args array's length is 1", 1, requestMessage.getArgs().length);
        assertEquals("args array contains " + FILE_ID, requestMessage.getArgs()[0], FILE_ID);
    }

    // ---------------------Args validation testing---------------------------------------------------
    /**
     * validate args of group of the same requirement MessageType. Including REMOVE_FILE,GET_FILE_NAME,GET_FILE_SIZE...
     * this args shouldn't be null, the size of the object array should be 1, the only array item shouldn't be empty.
     * @param messageType
     *            the MessageType instance
     */
    private void validateGroup1(MessageType messageType) {
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType);
            fail("null object array is not valid");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, new Object[0]);
            fail("the object array size should be 1");
        } catch (IllegalArgumentException e) {
            // good
        }

        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, new Object());
            fail("the arg should be instance of String");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, " ");
            fail("the first string arg should not be empty");
        } catch (IllegalArgumentException e) {
            // good
        }
        new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, VALID_STR);
    }

    /**
     * validate args of group of the same requirement MessageType. Including
     * UPLOAD_FILE,RENAME_FILE,ADD_FILE_TO_GROUP..., this args shouldn't be null, the size of the object array should be
     * 2, the two array item shouldn't be empty string.
     * @param messageType
     *            the MessageType instance
     */
    private void validateArgsGroup2(MessageType messageType) {
        Object[] args = new Object[2];
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType);
            fail("null object array is not valid");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, new Object());
            fail("the object array size should be 2");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[0] = new Object();
        args[1] = VALID_STR;
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, args);
            fail("the first arg should be instance of String");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[0] = " ";
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, args);
            fail("the first should be instance of String, and not empty");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[0] = VALID_STR;
        args[1] = new Object();
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, args);
            fail("the second arg should be instance of String");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[1] = " ";
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, args);
            fail("the second arg should be instance of String, and not empty");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[0] = VALID_STR;
        args[1] = VALID_STR;
        // should not throw any exception
        new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, args);
    }

    /**
     * validate args of group of the same requirement MessageType. Including CREATE_GROUP and UPDATE_GROUP.
     * @param messageType
     *            the MessageType instance
     */
    private void validateArgsGroup3(MessageType messageType) {
        validateArgsGroup3Part1(messageType);
        validateArgsGroup3Part2(messageType);
    }

    /**
     * validate args of group of the same requirement MessageType. Including CREATE_GROUP and UPDATE_GROUP.
     * @param messageType
     *            the MessageType instance
     */
    private void validateArgsGroup3Part1(MessageType messageType) {
        List validEmptyList = new ArrayList();
        Object[] args = new Object[2];
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType);
            fail("null object array is not valid");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, new Object());
            fail("the object array size should be 2");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[0] = new Object();
        args[1] = validEmptyList;
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, args);
            fail("the first arg should be instance of String");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[0] = " ";
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, args);
            fail("the first arg should not be empty");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * validate args of group of the same requirement MessageType. Including CREATE_GROUP and UPDATE_GROUP.
     * @param messageType
     *            the MessageType instance
     */
    private void validateArgsGroup3Part2(MessageType messageType) {
        List validEmptyList = new ArrayList();
        Object[] args = new Object[2];
        args[0] = GROUP_NAME;
        validEmptyList.add(null);
        args[1] = validEmptyList;
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, args);
            fail("as the second arg shouldn't contain non-String item");
        } catch (IllegalArgumentException e) {
            // good
        }
        validEmptyList.clear();
        validEmptyList.add(" ");
        args[1] = validEmptyList;
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, args);
            fail("as the second arg shouldn't contain empty String item");
        } catch (IllegalArgumentException e) {
            // good
        }
        validEmptyList.clear();
        validEmptyList.add(VALID_STR);
        args[1] = validEmptyList;
        // should not throw any exception
        new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, args);
    }

    /**
     * validate args of group of the same requirement MessageType. Including SEARCH_FILES and SEARCH_GROUPS.
     * @param messageType
     *            the MessageType instance
     */
    private void validateArgsGroup4(MessageType messageType) {
        Object[] args = new Object[2];
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType);
            fail("null object array is not valid");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, new Object());
            fail("the object array size should be 2");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[0] = new Object();
        args[1] = VALID_STR;
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, args);
            fail("the first arg should be instance of String");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[0] = " ";
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, args);
            fail("the first should be instance of String, and not empty");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[0] = VALID_STR;
        args[1] = new Object();
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, args);
            fail("the second arg should be instance of String");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[1] = " ";
        new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, args);
        args[0] = VALID_STR;
        args[1] = VALID_STR;
        // should not throw any exception
        new RequestMessage(HANDLE_ID, REQUEST_ID, messageType, args);
    }

    /**
     * Test validataion of RequestMessage with MessageType.UPLOAD_FILE type.
     */
    public void testArgsUploadFile() {
        validateArgsGroup2(MessageType.UPLOAD_FILE);
    }

    /**
     * Test validataion of RequestMessage with MessageType.RENAME_FILE type.
     */
    public void testArgsRenameFile() {
        validateArgsGroup2(MessageType.RENAME_FILE);
    }

    /**
     * Test validataion of RequestMessage with MessageType.ADD_FILE_TO_GROUP type.
     */
    public void testArgsAddFileToGroup() {
        validateArgsGroup2(MessageType.ADD_FILE_TO_GROUP);
    }

    /**
     * Test validataion of RequestMessage with MessageType.REMOVE_FILE_FROM_GROUP type.
     */
    public void testArgsRemoveFileFromGroup() {
        validateArgsGroup2(MessageType.REMOVE_FILE_FROM_GROUP);
    }

    /**
     * Test validataion of RequestMessage with MessageType.CHECK_UPLOAD_FILE type.
     */
    public void testArgsCheckUploadFilePart1() {
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.CHECK_UPLOAD_FILE);
            fail("null object array is not valid");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.CHECK_UPLOAD_FILE, new Object());
            fail("the object array size should be 2");
        } catch (IllegalArgumentException e) {
            // good
        }
        Object[] args = new Object[2];
        args[0] = new Object();
        args[1] = new Long(1);
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.CHECK_UPLOAD_FILE, args);
            fail("this first arg should be instance of String");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[0] = " ";
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.CHECK_UPLOAD_FILE, args);
            fail("the first string arg should not be empty");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test validataion of RequestMessage with MessageType.CHECK_UPLOAD_FILE type.
     */
    public void testArgsCheckUploadFilePart2() {
        Object[] args = new Object[2];
        args[0] = VALID_STR;
        args[1] = new Object();
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.CHECK_UPLOAD_FILE, args);
            fail("the second arg should not be instanceof Long");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[1] = new Long(-1);
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.CHECK_UPLOAD_FILE, args);
            fail("the second Long arg should not be great than 0");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[1] = new Long(23);
        // should not throw any exception
        new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.CHECK_UPLOAD_FILE, args);
    }

    /**
     * Test validataion of RequestMessage with MessageType.REMOVE_FILE type.
     */
    public void testArgsRemoveFile() {
        validateGroup1(MessageType.REMOVE_FILE);
    }

    /**
     * Test validataion of RequestMessage with MessageType.GET_FILE_NAME type.
     */
    public void testArgsGetFileName() {
        validateGroup1(MessageType.GET_FILE_NAME);
    }

    /**
     * Test validataion of RequestMessage with MessageType.GET_FILE_SIZE type.
     */
    public void testArgsGetFileSize() {
        validateGroup1(MessageType.GET_FILE_SIZE);
    }

    /**
     * Test validataion of RequestMessage with RETRIEVE_FILE type.
     */
    public void testArgsRetrieveFile() {
        validateGroup1(MessageType.RETRIEVE_FILE);
    }

    /**
     * Test validataion of RequestMessage with MessageType.RETRIEVE_GROUP type.
     */
    public void testArgsRetrieveGroup() {
        validateGroup1(MessageType.RETRIEVE_GROUP);
    }

    /**
     * Test validataion of RequestMessage with BytesMessageType.STOP_UPLOAD_FILE_BYTES type.
     */
    public void testArgsStopUploadFileBytes() {
        validateGroup1(BytesMessageType.STOP_UPLOAD_FILE_BYTES);
    }

    /**
     * Test validataion of RequestMessage with BytesMessageType.START_RETRIEVE_FILE_BYTES type.
     */
    public void testArgsStartRetrieveFileBytes() {
        validateGroup1(BytesMessageType.START_RETRIEVE_FILE_BYTES);
    }

    /**
     * Test validataion of RequestMessage with BytesMessageType.RETRIEVE_FILE_BYTES type.
     */
    public void testArgsRetrieveFileBytes() {
        validateGroup1(BytesMessageType.RETRIEVE_FILE_BYTES);
    }

    /**
     * Test validataion of RequestMessage with BytesMessageType.STOP_RETRIEVE_FILE_BYTES type.
     */
    public void testArgsStopRetrieveFileBytes() {
        validateGroup1(BytesMessageType.STOP_RETRIEVE_FILE_BYTES);
    }

    /**
     * Test validataion of RequestMessage with MessageType.REMOVE_GROUP type.
     */
    public void testArgsStopRemoveGroup() {
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.REMOVE_GROUP);
            fail("null object array is not valid");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.REMOVE_GROUP, new Object());
            fail("the object array size should be 2");
        } catch (IllegalArgumentException e) {
            // good
        }
        Object[] args = new Object[2];
        args[0] = new Object();
        args[1] = Boolean.TRUE;
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.REMOVE_GROUP, args);
            fail("this first arg should be instance of String");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[0] = " ";
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.REMOVE_GROUP, args);
            fail("the first string arg should not be empty");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[0] = VALID_STR;
        args[1] = new Object();
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.REMOVE_GROUP, args);
            fail("the second arg should not be instanceof Long");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[1] = Boolean.TRUE;
        new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.REMOVE_GROUP, args);
    }

    /**
     * Test validataion of RequestMessage with MessageType.CREATE_GROUP type.
     */
    public void testArgsCreateGroup() {
        validateArgsGroup3(MessageType.CREATE_GROUP);
    }

    /**
     * Test validataion of RequestMessage with MessageType.UPDATE_GROUP type.
     */
    public void testArgsUpdateGroup() {
        validateArgsGroup3(MessageType.UPDATE_GROUP);
    }

    /**
     * Test validataion of RequestMessage with MessageType.SEARCH_FILES type.
     */
    public void testArgsSearchFiles() {
        validateArgsGroup4(MessageType.SEARCH_FILES);
    }

    /**
     * Test validataion of RequestMessage with MessageType.SEARCH_GROUPS type.
     */
    public void testArgsSearchGroups() {
        validateArgsGroup4(MessageType.SEARCH_GROUPS);
    }

    /**
     * Test validation of BytesMessageType.START_UPLOAD_FILE_BYTES type.
     */
    public void testArgsStartUploadFileBytes1() {
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, BytesMessageType.START_UPLOAD_FILE_BYTES);
            fail("null object array is not valid");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, BytesMessageType.START_UPLOAD_FILE_BYTES, new Object[0]);
            fail("the object array size should be 1 or 2");
        } catch (IllegalArgumentException e) {
            // good
        }
        Object[] args = new Object[1];
        args[0] = new Object();
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, BytesMessageType.START_UPLOAD_FILE_BYTES, args);
            fail("the arg should be instance of String");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[0] = " ";
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, BytesMessageType.START_UPLOAD_FILE_BYTES, args);
            fail("the arg should not be empty");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[0] = VALID_STR;
        // should not throw any exception
        new RequestMessage(HANDLE_ID, REQUEST_ID, BytesMessageType.START_UPLOAD_FILE_BYTES, args);
    }

    /**
     * Test validation of BytesMessageType.START_UPLOAD_FILE_BYTES type.
     */
    public void testArgsStartUploadFileBytes2() {
        Object[] args = new Object[2];
        args[0] = new Object();
        args[1] = VALID_STR;
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.REMOVE_GROUP, args);
            fail("this first arg should be instance of String");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[0] = " ";
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.REMOVE_GROUP, args);
            fail("the first string arg should not be empty");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[0] = VALID_STR;
        args[1] = new Object();
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, BytesMessageType.START_UPLOAD_FILE_BYTES, args);
            fail("both arg should be instance of String");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[1] = " ";
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, BytesMessageType.START_UPLOAD_FILE_BYTES, args);
            fail("both arg should be instance of String, and not empty");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[1] = VALID_STR;
        // should not throw any exception
        new RequestMessage(HANDLE_ID, REQUEST_ID, BytesMessageType.START_UPLOAD_FILE_BYTES, args);
    }

    /**
     * Test validation of BytesMessageType.UPLOAD_FILE_BYTES type.
     */
    public void testArgsUploadFileBytes() {
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, BytesMessageType.UPLOAD_FILE_BYTES);
            fail("null object array is not valid");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, BytesMessageType.UPLOAD_FILE_BYTES, new Object[0]);
            fail("the object array size should be 2");
        } catch (IllegalArgumentException e) {
            // good
        }
        Object[] args = new Object[2];
        args[0] = new Object();
        args[1] = new byte[2];
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, BytesMessageType.UPLOAD_FILE_BYTES, args);
            fail("the first arg should be instance of String");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[0] = " ";
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, BytesMessageType.UPLOAD_FILE_BYTES, args);
            fail("the first arg should not be empty");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[0] = VALID_STR;
        args[1] = new Object();
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, BytesMessageType.UPLOAD_FILE_BYTES, args);
            fail("the second arg should be instance of byte[]");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[1] = new byte[0];
        try {
            new RequestMessage(HANDLE_ID, REQUEST_ID, BytesMessageType.UPLOAD_FILE_BYTES, args);
            fail("the second arg's array length shoul great than 0");
        } catch (IllegalArgumentException e) {
            // good
        }
        args[1] = new byte[1];
        // should not throw any exception
        new RequestMessage(HANDLE_ID, REQUEST_ID, BytesMessageType.UPLOAD_FILE_BYTES, args);
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(RequestMessageTestCase.class);
    }
}
