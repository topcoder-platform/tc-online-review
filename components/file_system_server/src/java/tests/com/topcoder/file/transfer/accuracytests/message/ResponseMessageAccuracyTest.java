/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.accuracytests.message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import com.topcoder.file.transfer.message.BytesMessageType;
import com.topcoder.file.transfer.message.MessageType;
import com.topcoder.file.transfer.message.ResponseMessage;

/**
 * <p>
 * Accuracy Test for ResponseMessage class.
 * </p>
 * 
 * @author arylio
 * @version 1.0
 */
public class ResponseMessageAccuracyTest extends TestCase {
    /**
     * <p>
     * Represents the hander id of message type.
     * </p>
     */
    private String handlerID = "handlerID";

    /**
     * <p>
     * Represents the request id of message type.
     * </p>
     */
    private String requestID = "requestID";

    /**
     * Tests ctor ResponseMessage(String handlerID, String requestId), an
     * instance should be created with the arguments.
     */
    public void testCtor1() {
        ResponseMessage msg = new ResponseMessage(handlerID, requestID);
        assertNotNull("Failed to create instance of ResponseMessage", msg);
        assertEquals("Failed to create instance of ResponseMessage correctly",
                handlerID, msg.getHandlerId());
        assertEquals("Failed to create instance of ResponseMessage correctly",
                requestID, msg.getRequestId());
        assertNull("Failed to create instance of ResponseMessage correctly",
                msg.getException());
        assertNull("Failed to create instance of ResponseMessage correctly",
                msg.getResult());
        assertNull("Failed to create instance of ResponseMessage correctly",
                msg.getType());
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type), an instance should be created with the arguments.
     * </p>
     */
    public void testCtor2_REMOVE_FILE() {
        innerTestCtor2(MessageType.REMOVE_FILE);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type), an instance should be created with the arguments.
     * </p>
     */
    public void testCtor2_RENAME_FILE() {
        innerTestCtor2(MessageType.RENAME_FILE);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type), an instance should be created with the arguments.
     * </p>
     */
    public void testCtor2_CREATE_GROUP() {
        innerTestCtor2(MessageType.CREATE_GROUP);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type), an instance should be created with the arguments.
     * </p>
     */
    public void testCtor2_UPDATE_GROUP() {
        innerTestCtor2(MessageType.UPDATE_GROUP);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type), an instance should be created with the arguments.
     * </p>
     */
    public void testCtor2_REMOVE_GROUP() {
        innerTestCtor2(MessageType.REMOVE_GROUP);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type), an instance should be created with the arguments.
     * </p>
     */
    public void testCtor2_ADD_FILE_TO_GROUP() {
        innerTestCtor2(MessageType.ADD_FILE_TO_GROUP);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type), an instance should be created with the arguments.
     * </p>
     */
    public void testCtor2_REMOVE_FILE_FROM_GROUP() {
        innerTestCtor2(MessageType.REMOVE_FILE_FROM_GROUP);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type), an instance should be created with the arguments.
     * </p>
     */
    public void testCtor2_UPLOAD_FILE_BYTES() {
        innerTestCtor2(BytesMessageType.UPLOAD_FILE_BYTES);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type), an instance should be created with the arguments.
     * </p>
     */
    public void testCtor2_STOP_UPLOAD_FILE_BYTES() {
        innerTestCtor2(BytesMessageType.STOP_UPLOAD_FILE_BYTES);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type), an instance should be created with the arguments.
     * </p>
     */
    public void testCtor2_STOP_RETRIEVE_FILE_BYTES() {
        innerTestCtor2(BytesMessageType.STOP_RETRIEVE_FILE_BYTES);
    }

    /**
     * <p>
     * Innert Test ResponseMessage(String handlerID, String requestId,
     * MessageType type, Exception exception).
     * </p>
     * 
     * @param type
     *            the message type
     * @param result
     *            the result
     */
    private void innerTestCtor2(MessageType type) {
        ResponseMessage msg = new ResponseMessage(handlerID, requestID, type);
        assertNotNull("Failed to create instance of ResponseMessage", msg);
        assertEquals("Failed to create instance of ResponseMessage correctly",
                handlerID, msg.getHandlerId());
        assertEquals("Failed to create instance of ResponseMessage correctly",
                type, msg.getType());
        assertEquals("Failed to create instance of ResponseMessage correctly",
                requestID, msg.getRequestId());
        assertNull("Failed to create instance of ResponseMessage correctly",
                msg.getResult());
        assertNull("Failed to create instance of ResponseMessage correctly",
                msg.getException());
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_UPLOAD_FILE() {
        innerTestCtor3(MessageType.UPLOAD_FILE, "file id");
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_CHECK_UPLOAD_FILE() {
        innerTestCtor3(MessageType.CHECK_UPLOAD_FILE, new Boolean(false));
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_REMOVE_FILE() {
        innerTestCtor3(MessageType.REMOVE_FILE, null);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_GET_FILE_NAME() {
        innerTestCtor3(MessageType.GET_FILE_NAME, "file name");
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_GET_FILE_SIZE() {
        innerTestCtor3(MessageType.GET_FILE_SIZE, new Long(111));
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_RENAME_FILE() {
        innerTestCtor3(MessageType.RENAME_FILE, null);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    /**
     * no such responce message public void testCtor3_RETRIEVE_FILE() {
     * innerTestCtor3(MessageType.RETRIEVE_FILE, null); }
     */

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_CREATE_GROUP() {
        innerTestCtor3(MessageType.CREATE_GROUP, null);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_UPDATE_GROUP() {
        innerTestCtor3(MessageType.UPDATE_GROUP, null);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_RETRIEVE_GROUP() {
        List fileIds = new ArrayList();
        fileIds.add("file id 1");
        fileIds.add("file id 2");
        innerTestCtor3(MessageType.RETRIEVE_GROUP, fileIds);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_REMOVE_GROUP() {
        innerTestCtor3(MessageType.REMOVE_GROUP, null);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_ADD_FILE_TO_GROUP() {
        innerTestCtor3(MessageType.ADD_FILE_TO_GROUP, null);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_REMOVE_FILE_FROM_GROUP() {
        innerTestCtor3(MessageType.REMOVE_FILE_FROM_GROUP, null);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_SEARCH_FILES() {
        List fileIds = new ArrayList();
        fileIds.add("file id 1");
        fileIds.add("file id 2");
        innerTestCtor3(MessageType.SEARCH_FILES, fileIds);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_SEARCH_GROUPS() {
        List groupNames = new ArrayList();
        groupNames.add("group name 1");
        groupNames.add("group name 2");
        innerTestCtor3(MessageType.SEARCH_GROUPS, groupNames);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_START_UPLOAD_FILE_BYTES() {
        innerTestCtor3(BytesMessageType.START_UPLOAD_FILE_BYTES,
                new Object[] {"fileid", "fileCreatedId"});
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_UPLOAD_FILE_BYTES() {
        innerTestCtor3(BytesMessageType.UPLOAD_FILE_BYTES, null);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_STOP_UPLOAD_FILE_BYTES() {
        innerTestCtor3(BytesMessageType.STOP_UPLOAD_FILE_BYTES, null);
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_START_RETRIEVE_FILE_BYTES() {
        innerTestCtor3(BytesMessageType.START_RETRIEVE_FILE_BYTES,
                new Object[] { "fileName", "bytesIteratorId" });
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_RETRIEVE_FILE_BYTES() {
        innerTestCtor3(BytesMessageType.RETRIEVE_FILE_BYTES, new byte[] { 1, 2,
                3, 4 });
    }

    /**
     * <p>
     * Tests ctor ResponseMessage(String handlerID, String requestId,
     * MessageType type, Object result), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor3_STOP_RETRIEVE_FILE_BYTES() {
        innerTestCtor3(BytesMessageType.STOP_RETRIEVE_FILE_BYTES, null);
    }

    /**
     * <p>
     * Innert Test ResponseMessage(String handlerID, String requestId,
     * MessageType type, Exception exception).
     * </p>
     * 
     * @param type
     *            the message type
     * @param result
     *            the result
     */
    private void innerTestCtor3(MessageType type, Object result) {
        ResponseMessage msg = new ResponseMessage(handlerID, requestID, type,
                result);
        assertNotNull("Failed to create instance of ResponseMessage", msg);
        assertEquals("Failed to create instance of ResponseMessage correctly",
                handlerID, msg.getHandlerId());
        assertEquals("Failed to create instance of ResponseMessage correctly",
                type, msg.getType());
        assertEquals("Failed to create instance of ResponseMessage correctly",
                requestID, msg.getRequestId());
        assertEquals("Failed to create instance of ResponseMessage correctly",
                result, msg.getResult());
        assertNull("Failed to create instance of ResponseMessage correctly",
                msg.getException());

        if (result instanceof List) {
            List expected = (List) result;
            List returned = (List) msg.getResult();
            Iterator itor = expected.iterator();
            assertTrue(
                    "Failed to create instance of ResponseMessage correctly",
                    expected.size() == returned.size());
            while (itor.hasNext()) {
                assertTrue(
                        "Failed to create instance of ResponseMessage correctly",
                        returned.contains(itor.next()));
            }
        }
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_ADD_FILE_TO_GROUP() {
        innerTestCtor4(MessageType.ADD_FILE_TO_GROUP);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_CHECK_UPLOAD_FILE() {
        innerTestCtor4(MessageType.CHECK_UPLOAD_FILE);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_CREATE_GROUP() {
        innerTestCtor4(MessageType.CREATE_GROUP);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_GET_FILE_NAME() {
        innerTestCtor4(MessageType.GET_FILE_NAME);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_GET_FILE_SIZE() {
        innerTestCtor4(MessageType.GET_FILE_SIZE);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_REMOVE_FILE() {
        innerTestCtor4(MessageType.REMOVE_FILE);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_REMOVE_FILE_FROM_GROUP() {
        innerTestCtor4(MessageType.REMOVE_FILE_FROM_GROUP);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_REMOVE_GROUP() {
        innerTestCtor4(MessageType.REMOVE_GROUP);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_RENAME_FILE() {
        innerTestCtor4(MessageType.RENAME_FILE);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_RETRIEVE_FILE() {
        innerTestCtor4(MessageType.RETRIEVE_FILE);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_RETRIEVE_GROUP() {
        innerTestCtor4(MessageType.RETRIEVE_GROUP);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_SEARCH_FILES() {
        innerTestCtor4(MessageType.SEARCH_FILES);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_SEARCH_GROUPS() {
        innerTestCtor4(MessageType.SEARCH_GROUPS);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_UPDATE_GROUP() {
        innerTestCtor4(MessageType.UPDATE_GROUP);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_UPLOAD_FILE() {
        innerTestCtor4(MessageType.UPLOAD_FILE);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_START_UPLOAD_FILE_BYTES() {
        innerTestCtor4(BytesMessageType.START_UPLOAD_FILE_BYTES);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_UPLOAD_FILE_BYTES() {
        innerTestCtor4(BytesMessageType.UPLOAD_FILE_BYTES);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_STOP_UPLOAD_FILE_BYTES() {
        innerTestCtor4(BytesMessageType.STOP_UPLOAD_FILE_BYTES);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_START_RETRIEVE_FILE_BYTES() {
        innerTestCtor4(BytesMessageType.START_RETRIEVE_FILE_BYTES);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_RETRIEVE_FILE_BYTES() {
        innerTestCtor4(BytesMessageType.RETRIEVE_FILE_BYTES);
    }

    /**
     * <p>
     * Test ctor ResponseMessage(String handlerID, String requestId, MessageType
     * type, Exception exception), an instance should be created with the
     * arguments.
     * </p>
     */
    public void testCtor4_STOP_RETRIEVE_FILE_BYTES() {
        innerTestCtor4(BytesMessageType.STOP_RETRIEVE_FILE_BYTES);
    }

    /**
     * <p>
     * Innert Test ResponseMessage(String handlerID, String requestId,
     * MessageType type, Exception exception).
     * </p>
     * 
     * @param type
     *            the message type
     */
    private void innerTestCtor4(MessageType type) {
        Exception expected = new Exception();
        ResponseMessage msg = new ResponseMessage(handlerID, requestID, type,
                expected);
        assertNotNull("Failed to create instance of ResponseMessage", msg);
        assertEquals("Failed to create instance of ResponseMessage correctly",
                handlerID, msg.getHandlerId());
        assertEquals("Failed to create instance of ResponseMessage correctly",
                type, msg.getType());
        assertEquals("Failed to create instance of ResponseMessage correctly",
                requestID, msg.getRequestId());
        assertEquals("Failed to create instance of ResponseMessage correctly",
                expected, msg.getException());
        assertNull("Failed to create instance of ResponseMessage correctly",
                msg.getResult());
    }
}