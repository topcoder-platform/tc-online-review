/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.MessageSerializer;
import com.topcoder.processor.ipserver.message.MessageSerializationException;
import com.topcoder.processor.ipserver.message.serializable.SerializableMessageSerializer;
import com.topcoder.processor.ipserver.message.serializable.SimpleSerializableMessage;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test cases for SerializableMessageSerializer.
 * </p>
 *
 * @author zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class SerializableMessageSerializerTest extends TestCase {

    /**
     * A message for the following tests.
     */
    private Message message;

    /**
     * A serializer for the following tests.
     */
    private MessageSerializer serializer;

    /**
     * <p>
     * setUp the environment, initialize the message and serializer.
     * </p>
     */
    protected void setUp() {
        this.message = new SimpleSerializableMessage("handler ID", "request ID");
        this.serializer = new SerializableMessageSerializer();
    }

    /**
     * Test if this class implements the MessageSerializer interface.
     */
    public void testClassType() {
        Object obj = new SerializableMessageSerializer();
        assertTrue("SerializableMessageSerializer does not implement MessageSerializer interface.",
            obj instanceof MessageSerializer);
    }

    /**
     * Test if Constructor works correctly. No exception should be thrown.
     */
    public void testConstructor() {
        // No exception
        new SerializableMessageSerializer();
    }

    /**
     * Test if the serializeMessage and deserializeMessage methods work correctly.
     *
     * @throws Exception to JUnit
     */
    public void testSerialization() throws Exception {
        // serialize the message to a byte array output stream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.serializeMessage(message, baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        // deserialize the message from the byte array input stream
        Message msg = serializer.deserializeMessage(bais);

        // The result message should be the same as the original message.
        assertEquals("The serialization is incorrect.", msg.getHandlerId(), message.getHandlerId());
        assertEquals("The serialization is incorrect.", msg.getRequestId(), message.getRequestId());
        assertEquals("The serialization is incorrect.", msg.getSerializerType(), message.getSerializerType());
    }

    /**
     * Test if the serializeMessage methods handles null message properly.
     *
     * @throws Exception to JUnit
     */
    public void testSerializeMessageFailure1() throws Exception {
        try {
            serializer.serializeMessage(null, new ByteArrayOutputStream());
            fail("NullPointerException should be thrown for null message.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test if the serializeMessage methods handles null output stream properly.
     *
     * @throws Exception to JUnit
     */
    public void testSerializeMessageFailure2() throws Exception {
        try {
            serializer.serializeMessage(message, null);
            fail("NullPointerException should be thrown for null output stream.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test if the serializeMessage methods handles incorrect message properly. If the message does not
     * extend java.io.Serializable, then MessageSerializationException should be thrown.
     */
    public void testSerializeMessageFailure3() {
        try {
            serializer.serializeMessage(new CustomMessage(), new ByteArrayOutputStream());
            fail("MessageSerializationException should be thrown for message that"
                + " does not extend java.io.Serializable.");
        } catch (MessageSerializationException e) {
            // success
        }
    }

    /**
     * Test if the deserializeMessage methods handles null message properly.
     *
     * @throws Exception to JUnit
     */
    public void testDeserializeMessageFailure1() throws Exception {
        try {
            serializer.deserializeMessage(null);
            fail("NullPointerException should be thrown for null message.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test if the deserializeMessage methods handles incorrect input stream properly.
     */
    public void testDeserializeMessageFailure2() {
        try {
            serializer.deserializeMessage(new ByteArrayInputStream("invalid things".getBytes()));
            fail("MessageSerializationException should be thrown for invalid input stream.");
        } catch (MessageSerializationException e) {
            // success
        }
    }
}
