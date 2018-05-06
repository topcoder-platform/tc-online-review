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
 * This is used to test ResponseMessage for correctness.
 * @author FireIce
 * @version 1.0
 */
public class ResponseMessageTestCase extends TestCase {
    /**
     * Represents the handle id.
     */
    private static final String HANDLE_ID = "handleId";

    /**
     * Represents the request id.
     */
    private static final String REQUEST_ID = "request";

    /**
     * Represents the Exception instance used in tests.
     */
    private static final Exception CAUSE = new Exception();

    /**
     * Test <code>ResponseMessage(String, String)</code> constructor, if any argument is null,throw
     * NullPointerException.
     */
    public void testCtorStringStringNPE() {
        try {
            new ResponseMessage(null, "");
            fail("if handleId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new ResponseMessage("", null);
            fail("if requestId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>ResponseMessage(String, String)</code> constructor, if any argument is valid, created with fields
     * set.
     */
    public void testCtorStringStringSuccess() {
        ResponseMessage responseMessage = new ResponseMessage(HANDLE_ID, REQUEST_ID);
        assertEquals("not correct handleId", responseMessage.getHandlerId(), HANDLE_ID);
        assertEquals("not correct requestId", responseMessage.getRequestId(), REQUEST_ID);
        assertNull("type field should be null", responseMessage.getType());
        assertNull("result field should be null", responseMessage.getResult());
        assertNull("exception field should be null", responseMessage.getException());
    }

    /**
     * Test <code>ResponseMessage(String, String, MessageType)</code> constructor, if any argument is null,throw
     * NullPointerException.
     */
    public void testCtorStringStringMessageTypeNPE() {
        try {
            new ResponseMessage(null, "", MessageType.REMOVE_FILE);
            fail("if handleId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new ResponseMessage("", null, MessageType.REMOVE_FILE);
            fail("if requestId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new ResponseMessage("", "", null);
            fail("if type is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>ResponseMessage(String, String, MessageType)</code> constructor, if result is invalid for the
     * MessageType, throw IllegalArgumentExeption.
     */
    public void testCtorStringStringMessageTypeIAE() {
        try {
            new ResponseMessage("", "", MessageType.CHECK_UPLOAD_FILE);
            fail("if result is invalid for the MessageType, throw IllegalArgumentExeption");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test <code>ResponseMessage(String, String, MessageType)</code> constructor, if any argument is valid, created
     * with fields set.
     */
    public void testCtorStringStringMessageTypeSuccess() {
        ResponseMessage responseMessage = new ResponseMessage(HANDLE_ID, REQUEST_ID, MessageType.REMOVE_FILE);
        assertEquals("not correct handleId", responseMessage.getHandlerId(), HANDLE_ID);
        assertEquals("not correct requestId", responseMessage.getRequestId(), REQUEST_ID);
        assertTrue("type field should be equal to the MessageType setted in constuctor", MessageType.REMOVE_FILE
                .equals(responseMessage.getType()));
        assertNull("result field should be null", responseMessage.getResult());
        assertNull("exception field should be null", responseMessage.getException());
    }

    /**
     * Test <code>ResponseMessage(String, String, MessageType, Object)</code> constructor, if any argument is
     * null,throw NullPointerException.
     */
    public void testCtorStringStringMessageTypeObjectNPE() {
        try {
            new ResponseMessage(null, "", MessageType.GET_FILE_NAME, "valid");
            fail("if handleId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new ResponseMessage("", null, MessageType.GET_FILE_NAME, "valid");
            fail("if requestId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new ResponseMessage("", "", null, "valid");
            fail("if type is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>ResponseMessage(String, String, MessageType, Object)</code> constructor, if result is invalid for
     * the MessageType, throw IllegalArgumentExeption.
     */
    public void testCtorStringStringMessageTypeObjectIAE() {
        try {
            new ResponseMessage("", "", MessageType.GET_FILE_NAME, "");
            fail("if result is invalid for the MessageType, throw IllegalArgumentExeption");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test <code>ResponseMessage(String, String, MessageType)</code> constructor, if any argument is valid, created
     * with fields set.
     */
    public void testCtorStringStringMessageTypeObjectSuccess() {
        ResponseMessage responseMessage = new ResponseMessage(HANDLE_ID, REQUEST_ID, MessageType.GET_FILE_NAME, "valid");
        assertEquals("not correct handleId", responseMessage.getHandlerId(), HANDLE_ID);
        assertEquals("not correct requestId", responseMessage.getRequestId(), REQUEST_ID);
        assertTrue("type field should be equal to the MessageType setted in constuctor", MessageType.GET_FILE_NAME
                .equals(responseMessage.getType()));
        assertTrue("result field should be a String instance", responseMessage.getResult() instanceof String);
        assertTrue("result field should be \"valid\"", "valid".equals(responseMessage.getResult()));
        assertNull("exception field should be null", responseMessage.getException());
    }

    /**
     * Test <code>ResponseMessage(String, String, MessageType, Exception)</code> constructor, if any argument is
     * null,throw NullPointerException.
     */
    public void testCtorStringStringMessageTypeExceptionNPE() {

        try {
            new ResponseMessage(null, "", MessageType.REMOVE_FILE, CAUSE);
            fail("if handleId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new ResponseMessage("", null, MessageType.REMOVE_FILE, CAUSE);
            fail("if requestId is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new ResponseMessage("", "", null, CAUSE);
            fail("if type is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
        try {
            new ResponseMessage("", "", MessageType.REMOVE_FILE, null);
            fail("if exception is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>ResponseMessage(String, String, MessageType, Exception)</code> constructor, if any argument is
     * valid, created with fields set.
     */
    public void testCtorStringStringMessageTypeExceptionSuccess() {
        ResponseMessage responseMessage = new ResponseMessage(HANDLE_ID, REQUEST_ID, MessageType.REMOVE_FILE, CAUSE);
        assertEquals("not correct handleId", responseMessage.getHandlerId(), HANDLE_ID);
        assertEquals("not correct requestId", responseMessage.getRequestId(), REQUEST_ID);
        assertTrue("type field should be equal to the MessageType setted in constuctor", MessageType.REMOVE_FILE
                .equals(responseMessage.getType()));
        assertNull("result field should be null", responseMessage.getResult());
        assertSame("exception field should be the valid setted in constructor", responseMessage.getException(), CAUSE);
    }

    /**
     * Test <code>getResult()</code> method.
     */
    public void testGetResult() {
        ResponseMessage responseMessage = new ResponseMessage(HANDLE_ID, REQUEST_ID);
        assertNull("result field should be null", responseMessage.getResult());
        responseMessage = new ResponseMessage(HANDLE_ID, REQUEST_ID, MessageType.REMOVE_FILE);
        assertNull("result field should be null", responseMessage.getResult());
        responseMessage = new ResponseMessage(HANDLE_ID, REQUEST_ID, MessageType.GET_FILE_NAME, "valid");
        assertTrue("result field should be a String instance", responseMessage.getResult() instanceof String);
        assertTrue("result field should be \"valid\"", "valid".equals(responseMessage.getResult()));
        responseMessage = new ResponseMessage(HANDLE_ID, REQUEST_ID, MessageType.REMOVE_FILE, CAUSE);
        assertNull("result field should be null", responseMessage.getResult());
    }

    /**
     * Test <code>getException()</code> method.
     */
    public void testGetException() {
        ResponseMessage responseMessage = new ResponseMessage(HANDLE_ID, REQUEST_ID);
        assertNull("exception field should be null", responseMessage.getException());
        responseMessage = new ResponseMessage(HANDLE_ID, REQUEST_ID, MessageType.REMOVE_FILE);
        assertNull("exception field should be null", responseMessage.getException());
        responseMessage = new ResponseMessage(HANDLE_ID, REQUEST_ID, MessageType.GET_FILE_NAME, "valid");
        assertNull("exception field should be null", responseMessage.getException());
        responseMessage = new ResponseMessage(HANDLE_ID, REQUEST_ID, MessageType.REMOVE_FILE, CAUSE);
        assertSame("exception field should be the valid setted in constructor", responseMessage.getException(), CAUSE);
    }

    // -----------------------------result validataion----------------------------------------------------------
    /**
     * validate the result that need a non-null non-empty String as a result.
     * @param messageType
     *            the MessageType instance
     */
    public void validateResultAsNonEmptyString(MessageType messageType) {
        try {
            new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType);
            fail("result shouldn't null and should be instanceof String");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, " ");
            fail("result should not be empty string");
        } catch (IllegalArgumentException e) {
            // good
        }
        new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, "valid");
    }

    /**
     * validate the result be null.
     * @param messageType
     *            the MessageType instance
     */
    public void validateResultAsNull(MessageType messageType) {
        new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType);
        try {
            new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, " ");
            fail("result should be null");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, "valid");
            fail("result should be null");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * validate the result that need a non-null non-empty String as a result.
     * @param messageType
     *            the MessageType instance
     */
    public void validateResultAsLong(MessageType messageType) {
        try {
            new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType);
            fail("result shouldn't null and should be instanceof Long");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, new Object());
            fail("result shouldn't null and should be instanceof Long");
        } catch (IllegalArgumentException e) {
            // good
        }
        new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, new Long(1));
    }

    /**
     * validate the result should be a Boolean instance.
     * @param messageType
     *            the MessageType instance
     */
    public void validateResultAsBoolean(MessageType messageType) {
        try {
            new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType);
            fail("result should be a Boolean instance");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, new Object());
            fail("result should be a Boolean instance");
        } catch (IllegalArgumentException e) {
            // good
        }
        new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, Boolean.FALSE);
    }

    /**
     * validate the result that should be list of String, no null or empty string item exist.
     * @param messageType
     *            the MessageType instance
     */
    public void validateResultAsStringList(MessageType messageType) {
        List list = new ArrayList();
        try {
            new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType);
            fail("result shouldn't null and should be instanceof List");
        } catch (IllegalArgumentException e) {
            // good
        }
        list.add(null);
        try {
            new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, list);
            fail("rresult list should not contains null item");
        } catch (IllegalArgumentException e) {
            // good
        }
        list.clear();
        list.add(" ");
        try {
            new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, list);
            fail("result list should not contains empty String");
        } catch (IllegalArgumentException e) {
            // good
        }
        list.clear();
        list.add("valid");
        new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, list);
    }

    /**
     * validate the result that should be byte array.
     * @param messageType
     *            the MessageType instance
     */
    public void validateResultAsByteArray(MessageType messageType) {
        try {
            new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType);
            fail("result shouldn't null and should be instanceof byte[]");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, new byte[0]);
            fail("result byte array size should be great than 0");
        } catch (IllegalArgumentException e) {
            // good
        }
        new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, new byte[10]);
    }

    /**
     * validate the result that should be two String array, which is not empty.
     * @param messageType
     *            the MessageType instance
     */
    public void validateResultAsTwoStringElemArray(MessageType messageType) {
        try {
            new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, new Object());
            fail("result should be type of Object[]");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, new Object[0]);
            fail("object array result should be sizeof 2");
        } catch (IllegalArgumentException e) {
            // good
        }
        Object[] result = new Object[2];
        result[0] = null;
        result[1] = "valid";
        try {
            new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, result);
            fail("object array result should be sizeof 2");
        } catch (IllegalArgumentException e) {
            // good
        }
        result[0] = " ";
        try {
            new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, result);
            fail("object array result should be sizeof 2");
        } catch (IllegalArgumentException e) {
            // good
        }
        result[0] = "valid";
        result[1] = null;
        try {
            new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, result);
            fail("object array result should be sizeof 2");
        } catch (IllegalArgumentException e) {
            // good
        }
        result[1] = " ";
        try {
            new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, result);
            fail("object array result should be sizeof 2");
        } catch (IllegalArgumentException e) {
            // good
        }
        result[1] = "valid";
        new ResponseMessage(HANDLE_ID, REQUEST_ID, messageType, result);
    }

    /**
     * validate result of the ResponseMessage with MessageType.UPLOAD_FILE type.
     */
    public void testValidateResultUploadFile() {
        validateResultAsNonEmptyString(MessageType.UPLOAD_FILE);
    }

    /**
     * validate result of the ResponseMessage with MessageType.GET_FILE_NAME type.
     */
    public void testValidateResultGetFileName() {
        validateResultAsNonEmptyString(MessageType.GET_FILE_NAME);
    }

    /**
     * validate result of the ResponseMessage with MessageType.RETRIEVE_FILE type.
     */
    public void testValidateResultRetrieveFile() {
        validateResultAsNonEmptyString(MessageType.RETRIEVE_FILE);
    }

    /**
     * validate result of the ResponseMessage with BytesMessageType.START_UPLOAD_FILE_BYTES type.
     */
    public void testValidateResultStartUploadFileBytes() {
        validateResultAsTwoStringElemArray(BytesMessageType.START_UPLOAD_FILE_BYTES);
    }

    /**
     * validate result of the ResponseMessage with BytesMessageType.START_RETRIEVE_FILE_BYTES type.
     */
    public void testValidateResultStartRetrieveFileBytes() {
        validateResultAsTwoStringElemArray(BytesMessageType.START_RETRIEVE_FILE_BYTES);
    }

    /**
     * validate result of the ResponseMessage with MessageType.CHECK_UPLOAD_FILE type.
     */
    public void testValidateResultCheckUploadFile() {
        validateResultAsBoolean(MessageType.CHECK_UPLOAD_FILE);
    }

    /**
     * validate result of the ResponseMessage with MessageType.REMOVE_FILE type.
     */
    public void testValidateResultRemoveFile() {
        validateResultAsNull(MessageType.REMOVE_FILE);
    }

    /**
     * validate result of the ResponseMessage with MessageType.RENAME_FILE type.
     */
    public void testValidateResultRenameFile() {
        validateResultAsNull(MessageType.RENAME_FILE);
    }

    /**
     * validate result of the ResponseMessage with MessageType.CREATE_GROUP type.
     */
    public void testValidateResultCreateGroup() {
        validateResultAsNull(MessageType.CREATE_GROUP);
    }

    /**
     * validate result of the ResponseMessage with MessageType.UPDATE_GROUP type.
     */
    public void testValidateResultUpdateGroup() {
        validateResultAsNull(MessageType.UPDATE_GROUP);
    }

    /**
     * validate result of the ResponseMessage with MessageType.REMOVE_GROUP type.
     */
    public void testValidateResultRemoveGroup() {
        validateResultAsNull(MessageType.REMOVE_GROUP);
    }

    /**
     * validate result of the ResponseMessage with MessageType.ADD_FILE_TO_GROUP type.
     */
    public void testValidateResultAddFileToGroup() {
        validateResultAsNull(MessageType.ADD_FILE_TO_GROUP);
    }

    /**
     * validate result of the ResponseMessage with MessageType.REMOVE_FILE_FROM_GROUP type.
     */
    public void testValidateResultRemoveFileFromGroup() {
        validateResultAsNull(MessageType.REMOVE_FILE_FROM_GROUP);
    }

    /**
     * validate result of the ResponseMessage with BytesMessageType.UPLOAD_FILE_BYTES type.
     */
    public void testValidateResultUploadFileBytes() {
        validateResultAsNull(BytesMessageType.UPLOAD_FILE_BYTES);
    }

    /**
     * validate result of the ResponseMessage withBytesMessageType.STOP_UPLOAD_FILE_BYTES type.
     */
    public void testValidateResultStopUploadFileBytes() {
        validateResultAsNull(BytesMessageType.STOP_UPLOAD_FILE_BYTES);
    }

    /**
     * validate result of the ResponseMessage with BytesMessageType.STOP_RETRIEVE_FILE_BYTES type.
     */
    public void testValidateResultStopRetrieveFileBytes() {
        validateResultAsNull(BytesMessageType.STOP_RETRIEVE_FILE_BYTES);
    }

    /**
     * validate result of the ResponseMessage with MessageType.GET_FILE_SIZE type.
     */
    public void testValidateResultGetFileSize() {
        validateResultAsLong(MessageType.GET_FILE_SIZE);
    }

    /**
     * validate result of the ResponseMessage with MessageType.RETRIEVE_GROUP type.
     */
    public void testValidateResultRetrieveGroup() {
        validateResultAsStringList(MessageType.RETRIEVE_GROUP);
    }

    /**
     * validate result of the ResponseMessage with MessageType.SEARCH_FILES type.
     */
    public void testValidateResultSearchFiles() {
        validateResultAsStringList(MessageType.SEARCH_FILES);
    }

    /**
     * validate result of the ResponseMessage with MessageType.SEARCH_GROUPS type.
     */
    public void testValidateResultSearchGroup() {
        validateResultAsStringList(MessageType.SEARCH_GROUPS);
    }

    /**
     * validate result of the ResponseMessage with BytesMessageType.RETRIEVE_FILE_BYTES type.
     */
    public void testValidateResultRetriveFileBytes() {
        validateResultAsByteArray(BytesMessageType.RETRIEVE_FILE_BYTES);
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(ResponseMessageTestCase.class);
    }
}
