/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.validator;

import com.topcoder.file.transfer.message.MessageType;
import com.topcoder.file.transfer.message.RequestMessage;
import com.topcoder.file.transfer.message.ResponseMessage;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This is used to test UploadRequestValidator for correctness.
 * @author FireIce
 * @version 1.0
 */
public class UploadRequestValidatorTestCase extends TestCase {
    /**
     * Represents the file location directory.
     */
    private static final String FILE_LOCATION = "test_files/";

    /**
     * Represents the handle id.
     */
    private static final String HANDLE_ID = "handleId";

    /**
     * Represents the request id.
     */
    private static final String REQUEST_ID = "request";

    /**
     * Represents the UploadRequestValidator instance used in tests.
     */
    private UploadRequestValidator uploadRequestValidator;

    /**
     * <p>
     * Sets up the test environment. The test instances are created.
     * </p>
     * @throws Exception
     *             if any Exception occurs.
     */
    protected void setUp() throws Exception {
        uploadRequestValidator = new UploadRequestValidator(new FreeDiskSpaceNonNativeChecker(FILE_LOCATION));
    }

    /**
     * <p>
     * Cleans up the test environment. The test instances are disposed.
     * </p>
     * @throws Exception
     *             if any Exception occurs.
     */
    protected void tearDown() throws Exception {
        uploadRequestValidator = null;
    }

    /**
     * Test constructor if the argument is null, throw NullPointerExcetpion.
     */
    public void testCtorNPE() {
        try {
            new UploadRequestValidator(null);
            fail("if the argument is null, throw NullPointerExcetpion");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test constructor performed correctly.
     */
    public void testCtorSuccess() {
        new UploadRequestValidator(new FreeDiskSpaceNonNativeChecker(FILE_LOCATION));
    }

    /**
     * Test <code>valid(Object)</code> method, if argument is null, throw NullPointerException.
     */
    public void testValidNullPointerException() {
        assertNotNull("setup fails", uploadRequestValidator);
        try {
            uploadRequestValidator.valid(null);
            fail("if argument is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>valid(Object)</code> method, if argument is valid, throw NullPointerException.
     */
    public void testValidSuccess() {
        assertNotNull("setup fails", uploadRequestValidator);
        ResponseMessage responseMessage = new ResponseMessage(HANDLE_ID, REQUEST_ID);
        assertFalse("not type of RequestMessage, return false", uploadRequestValidator.valid(responseMessage));
        RequestMessage requestMessage = new RequestMessage(HANDLE_ID, REQUEST_ID);
        assertFalse("RequestMessage's type is not MessageType.CHECK_UPLOAD_FILE, should return false",
                uploadRequestValidator.valid(requestMessage));
        Object[] args = new Object[2];
        args[0] = "FILEID";
        args[1] = new Long(Long.MAX_VALUE);
        requestMessage = new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.CHECK_UPLOAD_FILE, args);
        assertFalse("not enough diskspace, should return false", uploadRequestValidator.valid(requestMessage));
        args = new Object[2];
        args[0] = "FILEID";
        args[1] = new Long(1);
        requestMessage = new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.CHECK_UPLOAD_FILE, args);
        assertTrue("there is enough diskspace, should return true", uploadRequestValidator.valid(requestMessage));
    }

    /**
     * Test <code>getMessage(Object)</code> method, if argument is null, throw NullPointerException.
     */
    public void testGetMessageNullPointerException() {
        assertNotNull("setup fails", uploadRequestValidator);
        try {
            uploadRequestValidator.getMessage(null);
            fail("if argument is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test <code>getMessage(Object)</code> method, if argument is valid, throw NullPointerException.
     */
    public void testGetMessageSuccess() {
        assertNotNull("setup fails", uploadRequestValidator);
        ResponseMessage responseMessage = new ResponseMessage(HANDLE_ID, REQUEST_ID);
        assertEquals("invalid return message", "the object is not type of RequestMessage", uploadRequestValidator
                .getMessage(responseMessage));
        RequestMessage requestMessage = new RequestMessage(HANDLE_ID, REQUEST_ID);
        assertEquals("invalid return message", "the RequestMesage's type not equal with MessageType.CHECK_UPLOAD_FILE",
                uploadRequestValidator.getMessage(requestMessage));
        Object[] args = new Object[2];
        args[0] = "FILEID";
        args[1] = new Long(Long.MAX_VALUE);
        requestMessage = new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.CHECK_UPLOAD_FILE, args);
        assertEquals("not enough diskspace, should return File size exceeds disk size message string",
                "File size exceeds disk size", uploadRequestValidator.getMessage(requestMessage));
        args = new Object[2];
        args[0] = "FILEID";
        args[1] = new Long(1);
        requestMessage = new RequestMessage(HANDLE_ID, REQUEST_ID, MessageType.CHECK_UPLOAD_FILE, args);
        assertNull("there is enough diskspace, should return null", uploadRequestValidator.getMessage(requestMessage));
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(UploadRequestValidatorTestCase.class);
    }
}
