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
import com.topcoder.file.transfer.message.RequestMessage;

/**
 * <p>
 * Accuracy Test for RequestMessage class.
 * </p>
 * 
 * @author arylio
 * @version 1.0
 */
public class RequestMessageAccuracyTest extends TestCase {
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
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId), the
     * instance should be created with the supplied args.
     * </p>
     */
    public void testCtor1() {
        RequestMessage msg = new RequestMessage(handlerID, requestID);
        assertNotNull("Failed to create instance of RequestMessage", msg);
        assertEquals("Failed to create instance of RequestMessage correctly",
                handlerID, msg.getHandlerId());
        assertEquals("Failed to create instance of RequestMessage correctly",
                requestID, msg.getRequestId());
        assertNull("Failed to create instance of RequestMessage correctly", msg
                .getType());
        assertNull("Failed to create instance of RequestMessage correctly", msg
                .getArgs());
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object arg), the instance should be created with the supplied args.
     * with type is REMOVE_FILE.
     * </p>
     */
    public void testCtor3_REMOVE_FILE() {
        innerTestCtor3(MessageType.REMOVE_FILE, "file id");
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object arg), the instance should be created with the supplied args.
     * with type is GET_FILE_SIZE.
     * </p>
     */
    public void testCtor3_GET_FILE_SIZE() {
        innerTestCtor3(MessageType.GET_FILE_SIZE, "file id");
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object arg), the instance should be created with the supplied args.
     * with type is RETRIEVE_FILE.
     * </p>
     */
    public void testCtor3_RETRIEVE_FILE() {
        innerTestCtor3(MessageType.RETRIEVE_FILE, "file id");
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object arg), the instance should be created with the supplied args.
     * with type is RETRIEVE_GROUP.
     * </p>
     */
    public void testCtor3_RETRIEVE_GROUP() {
        innerTestCtor3(MessageType.RETRIEVE_GROUP, "group name");
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is STOP_UPLOAD_FILE_BYTES,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor3_STOP_UPLOAD_FILE_BYTES() {
        innerTestCtor3(BytesMessageType.STOP_UPLOAD_FILE_BYTES,
                "bytesIteratorId");
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is START_RETRIEVE_FILE_BYTES,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor3_START_RETRIEVE_FILE_BYTES() {
        innerTestCtor3(BytesMessageType.START_RETRIEVE_FILE_BYTES,
                "bytesIteratorId");
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is RETRIEVE_FILE_BYTES,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor3_RETRIEVE_FILE_BYTES() {
        innerTestCtor3(BytesMessageType.RETRIEVE_FILE_BYTES, "bytesIteratorId");
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is STOP_RETRIEVE_FILE_BYTES,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor3_STOP_RETRIEVE_FILE_BYTES() {
        innerTestCtor3(BytesMessageType.STOP_RETRIEVE_FILE_BYTES,
                "bytesIteratorId");
    }

    /**
     * <p>
     * Inner test RequestMessage(String handlerID, String requestId, MessageType
     * type, Object args). 
     * </p>
     * 
     * @param type the message type
     * @param expected the object arg
     */
    public void innerTestCtor3(MessageType type, Object expected) {
        RequestMessage msg = new RequestMessage(handlerID, requestID, type,
                expected);
        assertNotNull("Failed to create instance of RequestMessage", msg);
        assertEquals("Failed to create instance of RequestMessage correctly",
                handlerID, msg.getHandlerId());
        assertEquals("Failed to create instance of RequestMessage correctly",
                requestID, msg.getRequestId());
        assertEquals("Failed to create instance of RequestMessage correctly",
                msg.getType(), type);
        Object[] objs = msg.getArgs();
        assertTrue("Failed to get the args correctly", 1 == objs.length);
        assertEquals("Failed to create instance of RequestMessage correctly",
                expected, objs[0]);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is UPLOAD_FILE,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_UPLOAD_FILE() {
        Object[] expected = new Object[] { "filename", "file id" };
        innerTestCtor4(MessageType.UPLOAD_FILE, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is CHECK_UPLOAD_FILE,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_CHECK_UPLOAD_FILE() {
        Object[] expected = new Object[] { "filename", new Long(1000) };
        innerTestCtor4(MessageType.CHECK_UPLOAD_FILE, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is REMOVE_FILE,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_REMOVE_FILE() {
        Object[] expected = new Object[] { "file id" };
        innerTestCtor4(MessageType.REMOVE_FILE, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is GET_FILE_SIZE,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_GET_FILE_SIZE() {
        Object[] expected = new Object[] { "file id" };
        innerTestCtor4(MessageType.GET_FILE_NAME, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is RENAME_FILE,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_RENAME_FILE() {
        Object[] expected = new Object[] { "file id", "new file name" };
        innerTestCtor4(MessageType.RENAME_FILE, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is RETRIEVE_FILE,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_RETRIEVE_FILE() {
        Object[] expected = new Object[] { "file id" };
        innerTestCtor4(MessageType.RETRIEVE_FILE, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is CREATE_GROUP,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_CREATE_GROUP() {
        List fileIds = new ArrayList();
        fileIds.add("file id 1");
        fileIds.add("file id 2");
        Object[] expected = new Object[] { "group name", fileIds };
        innerTestCtor4(MessageType.CREATE_GROUP, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is UPDATE_GROUP,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_UPDATE_GROUP() {
        List fileIds = new ArrayList();
        fileIds.add("file id 1");
        fileIds.add("file id 2");
        Object[] expected = new Object[] { "group name", fileIds };
        innerTestCtor4(MessageType.UPDATE_GROUP, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is RETRIEVE_GROUP,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_RETRIEVE_GROUP() {
        Object[] expected = new Object[] { "group name" };
        innerTestCtor4(MessageType.RETRIEVE_GROUP, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is REMOVE_GROUP,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_REMOVE_GROUP() {
        Object[] expected = new Object[] { "group name", new Boolean(true) };
        innerTestCtor4(MessageType.REMOVE_GROUP, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is ADD_FILE_TO_GROUP,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_ADD_FILE_TO_GROUP() {
        Object[] expected = new Object[] { "group name", "file id" };
        innerTestCtor4(MessageType.ADD_FILE_TO_GROUP, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is REMOVE_FILE_FROM_GROUP,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_REMOVE_FILE_FROM_GROUP() {
        Object[] expected = new Object[] { "group name", "file id" };
        innerTestCtor4(MessageType.REMOVE_FILE_FROM_GROUP, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is SEARCH_FILES,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_SEARCH_FILES() {
        Object[] expected = new Object[] { "searchnme", "criteria" };
        innerTestCtor4(MessageType.SEARCH_FILES, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is SEARCH_GROUPS,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_SEARCH_GROUPS() {
        Object[] expected = new Object[] { "groupname", "criteria" };
        innerTestCtor4(MessageType.SEARCH_GROUPS, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is START_UPLOAD_FILE_BYTES,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_START_UPLOAD_FILE_BYTES_1() {
        Object[] expected = new Object[] { "file name" };
        innerTestCtor4(BytesMessageType.START_UPLOAD_FILE_BYTES, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is START_UPLOAD_FILE_BYTES,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_START_UPLOAD_FILE_BYTES_2() {
        Object[] expected = new Object[] { "file name", "file id" };
        innerTestCtor4(BytesMessageType.START_UPLOAD_FILE_BYTES, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is UPLOAD_FILE_BYTES,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_UPLOAD_FILE_BYTES() {
        Object[] expected = new Object[] { "fileCreationId",
                new byte[] { 1, 2, 3, 4 } };
        innerTestCtor4(BytesMessageType.UPLOAD_FILE_BYTES, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is STOP_UPLOAD_FILE_BYTES,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_STOP_UPLOAD_FILE_BYTES() {
        Object[] expected = new Object[] { "fileCreationId" };
        innerTestCtor4(BytesMessageType.STOP_UPLOAD_FILE_BYTES, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is START_RETRIEVE_FILE_BYTES,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_START_RETRIEVE_FILE_BYTES() {
        Object[] expected = new Object[] { "bytesIteratorId" };
        innerTestCtor4(BytesMessageType.START_RETRIEVE_FILE_BYTES, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is RETRIEVE_FILE_BYTES,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_RETRIEVE_FILE_BYTES() {
        Object[] expected = new Object[] { "bytesIteratorId" };
        innerTestCtor4(BytesMessageType.RETRIEVE_FILE_BYTES, expected);
    }

    /**
     * <p>
     * Test ctor RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args), with message type is STOP_RETRIEVE_FILE_BYTES,
     * the instance should be created with the supplied args.
     * </p>
     */
    public void testCtor4_STOP_RETRIEVE_FILE_BYTES() {
        Object[] expected = new Object[] { "bytesIteratorId" };
        innerTestCtor4(BytesMessageType.STOP_RETRIEVE_FILE_BYTES, expected);
    }

    /**
     * <p>
     * Inner test RequestMessage(String handlerID, String requestId, MessageType
     * type, Object[] args).
     * </p>
     * 
     * @param type
     *            the message type
     * @param expected
     *            the object args
     */
    public void innerTestCtor4(MessageType type, Object[] expected) {
        RequestMessage msg = new RequestMessage(handlerID, requestID, type,
                expected);
        assertNotNull("Failed to create instance of RequestMessage", msg);
        assertEquals("Failed to create instance of RequestMessage correctly",
                handlerID, msg.getHandlerId());
        assertEquals("Failed to create instance of RequestMessage correctly",
                requestID, msg.getRequestId());
        assertEquals("Failed to create instance of RequestMessage correctly",
                msg.getType(), type);
        Object[] objs = msg.getArgs();
        assertTrue("Failed to get the args correctly",
                expected.length == objs.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(
                    "Failed to create instance of RequestMessage correctly",
                    expected[i], objs[i]);
            if (expected[i] instanceof List) {
                assertTrue(
                        "Failed to create instance of RequestMessage correctly",
                        objs[i] instanceof List);
                List list = (List) objs[i];
                assertEquals(
                        "Failed to create instance of RequestMessage correctly",
                        ((List) expected[i]).size(), list.size());
                Iterator itor = ((List) expected[i]).iterator();
                while (itor.hasNext()) {
                    assertTrue(
                            "Failed to create instance of RequestMessage correctly",
                            list.contains(itor.next()));
                }
            } else if (expected[i] instanceof byte[]) {
                byte[] datas = (byte[]) expected[i];
                byte[] bytes = (byte[]) objs[i];
                assertTrue(
                        "Failed to create instance of RequestMessage correctly",
                        datas.length == bytes.length);
                for (int j = 0; j < datas.length; j++) {
                    assertEquals(
                            "Failed to create instance of RequestMessage correctly",
                            datas[j], bytes[j]);
                }
            }
        }
    }
}