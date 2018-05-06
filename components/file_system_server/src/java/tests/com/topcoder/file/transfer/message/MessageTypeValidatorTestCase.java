/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.message;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This is used to test MessageTypeValidator for correctness.
 * @author FireIce
 * @version 1.0
 */
public class MessageTypeValidatorTestCase extends TestCase {
    /**
     * Represents the handle id.
     */
    private static final String HANDLE_ID = "handleId";

    /**
     * Represents the request id.
     */
    private static final String REQUEST_ID = "request";

    /**
     * Test <code>validate(FileSystemMessage)</code> method, if the message is not either RequestMessage or
     * ResponseMessage, return false.
     */
    public void testValidate() {
        assertFalse("if the message is not either RequestMessage or ResponseMessage, return false",
                MessageTypeValidator.validate(new MockFileSystemMessage(HANDLE_ID, REQUEST_ID)));
        // if the message is Requestmessage or ResponseMessage, test is located in RequestMessageTestCase or
        // ResponseMessageTestCase
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(MessageTypeValidatorTestCase.class);
    }
}
