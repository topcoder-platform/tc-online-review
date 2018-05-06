/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver.accuracytests;

import junit.framework.TestCase;
import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.serializable.SimpleSerializableMessage;
import com.topcoder.processor.ipserver.message.serializable.SerializableMessageSerializer;
import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * Accuracy test cases for SimpleSerializableMessage and SerializableMessageSerializer.
 *
 * @author WishingBone
 * @version 2.0
 */
public class SerializableMessageAccuracyTestsV2 extends TestCase {

    /**
     * This case tests whether SimpleSerializableMessage works by itself.
     */
    public void testSimpleSerializableMessage() {
        String handlerId = "u7p3$";
        String requestId = "i0-s@";
        SimpleSerializableMessage message = new SimpleSerializableMessage(handlerId, requestId);

        assertTrue("Message is not serialzable.", message instanceof Serializable);

        assertEquals("Incorrect handler id.", handlerId, message.getHandlerId());
        assertEquals("Incorrect request id.", requestId, message.getRequestId());

        assertEquals("Incorrect serializer type.", SerializableMessageSerializer.class.getName(), message.getSerializerType());
    }

    /**
     * This case tests whether SerializableMessageSerializer serializes message as specified.
     *
     * @throws Exception to JUnit.
     */
    public void testSerializeMessage() throws Exception {
        String handlerId = "u7p3$";
        String requestId = "i0-s@";
        AccuracySerializableMessage message = new AccuracySerializableMessage(handlerId, requestId);

        long magicNumber = 43265472;
        message.setMagicNumber(magicNumber);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        new SerializableMessageSerializer().serializeMessage(message, stream);

        assertByteArray(serialize(message), stream.toByteArray());
    }

    /**
     * This case tests whether SerializableMessageSerializer deserializes message as specified.
     *
     * @throws Exception to JUnit.
     */
    public void testDeserializeMessage() throws Exception {
        String handlerId = "u7p3$";
        String requestId = "i0-s@";
        AccuracySerializableMessage message = new AccuracySerializableMessage(handlerId, requestId);

        long magicNumber = 43265472;
        message.setMagicNumber(magicNumber);

        Message message2 = new SerializableMessageSerializer().
                deserializeMessage(new ByteArrayInputStream(serialize(message)));

        assertNotNull("Message is not deserialized.", message2);
        assertTrue("Incorrect message type.", message2 instanceof AccuracySerializableMessage);

        assertEquals("Message data is corrupted.", magicNumber,
                ((AccuracySerializableMessage) message2).getMagicNumber());
    }

    /**
     * Assert two byte arrays are equivalent.
     *
     * @param a the first array.
     * @param b the second array.
     */
    private void assertByteArray(byte[] a, byte[] b) {
        assertNotNull(a);
        assertNotNull(b);
        assertEquals("Length of the arrays does not match.", a.length, b.length);
        for (int i = 0; i < a.length; ++i) {
            assertEquals("Content of the arrays does not match.", a[i], b[i]);
        }
    }

    /**
     * Serialize an object into a byte array.
     *
     * @param object the object to serialize.
     *
     * @return the serialized byte array.
     *
     * @throws Exception to JUnit.
     */
    private byte[] serialize(Serializable object) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(stream);
        objectStream.writeObject(object);
        objectStream.flush();
        return stream.toByteArray();
    }

}
