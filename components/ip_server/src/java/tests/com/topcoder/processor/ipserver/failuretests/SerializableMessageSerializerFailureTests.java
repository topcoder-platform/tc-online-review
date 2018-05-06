/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved.
 */
package com.topcoder.processor.ipserver.failuretests;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.MessageSerializationException;
import com.topcoder.processor.ipserver.message.serializable.SerializableMessageSerializer;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Failure tests for SerializableMessageSerializer implementation.
 *
 * @author brain_cn
 * @version 2.0
 * @since 2.0
 */
public class SerializableMessageSerializerFailureTests extends FailureTestCase {
    /** The test SerializableMessageSerializer instance. */
    private SerializableMessageSerializer instance = null;

    /**
     * The output for testing.
     */
    private OutputStream output = new ByteArrayOutputStream();

    /**
     * The message for testing.
     */
    private Message message = new MockMessage();

    /**
     * The invalidMessage for testing.
     */
    private Message invalidMessage = new MockMessageNotSerializable();

    /**
     * Creates a test suite for the tests in this test case.
     *
     * @return a TestSuite for this test case
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(SerializableMessageSerializerFailureTests.class);

        return suite;
    }

    /**
     * Setup for failure test.
     *
     * @throws Exception if any unexpected exception occurs.
     */
    public void setUp() throws Exception {
        loadNamespaces();
        instance = new SerializableMessageSerializer();
    }

    /**
     * Test of deserializeMessage with null input.
     *
     * <p>
     * Expects NullPointerException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testDeserializeMessage_Null_Input() throws Exception {
        try {
            instance.deserializeMessage(null);
            fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of serializeMessage with null message.
     *
     * <p>
     * Expects NullPointerException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testSerializeMessage_Null_Message() throws Exception {
        try {
            instance.serializeMessage(null, output);
            fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of serializeMessage with null output.
     *
     * <p>
     * Expects NullPointerException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testSerializeMessage_Null_Output() throws Exception {
        try {
            instance.serializeMessage(message, null);
            fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of serializeMessage with not Serializable.
     *
     * <p>
     * Expects MessageSerializationException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testSerializeMessage_Not_Serializable() throws Exception {
        try {
            instance.serializeMessage(invalidMessage, output);
            fail("MessageSerializationException is expected");
        } catch (MessageSerializationException e) {
            // good
        }
    }
}