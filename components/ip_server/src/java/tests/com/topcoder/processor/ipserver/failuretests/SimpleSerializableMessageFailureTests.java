/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved.
 */
package com.topcoder.processor.ipserver.failuretests;

import com.topcoder.processor.ipserver.message.serializable.SimpleSerializableMessage;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Failure tests for SimpleSerializableMessage implementation.
 *
 * @author brain_cn
 * @version 2.0
 * @since 2.0
 */
public class SimpleSerializableMessageFailureTests extends FailureTestCase {
    /**
     * The requestId for testing.
     */
    private String requestId = "requestId";


    /**
     * The handlerId for testing.
     */
    private String handlerId = "handlerId";



    /**
     * Creates a test suite for the tests in this test case.
     *
     * @return a TestSuite for this test case
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(SimpleSerializableMessageFailureTests.class);

        return suite;
    }

    /**
     * Setup for failure test.
     *
     * @throws Exception if any unexpected exception occurs.
     */
    public void setUp() throws Exception {
        loadNamespaces();
    }


    /**
     * Test of SimpleSerializableMessage with null handlerId.
     *
     * <p>
     * Expects NullPointerException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testSimpleSerializableMessage_Null_HandlerId() throws Exception {
        try {
            new SimpleSerializableMessage(null, requestId);
            fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of SimpleSerializableMessage with null requestId.
     *
     * <p>
     * Expects NullPointerException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testSimpleSerializableMessage_Null_RequestId() throws Exception {
        try {
            new SimpleSerializableMessage(handlerId, null);
            fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            // good
        }
    }
}