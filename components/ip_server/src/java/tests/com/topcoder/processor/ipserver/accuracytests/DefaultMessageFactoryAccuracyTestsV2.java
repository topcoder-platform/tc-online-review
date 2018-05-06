/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver.accuracytests;

import junit.framework.TestCase;
import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.DefaultMessageFactory;
import com.topcoder.processor.ipserver.message.serializable.SimpleSerializableMessage;
import com.topcoder.util.config.ConfigManager;
import java.util.Map;
import java.util.Iterator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;

/**
 * Accuracy test cases for DefaultMessageFactory.
 *
 * @author WishingBone
 * @version 2.0
 */
public class DefaultMessageFactoryAccuracyTestsV2 extends TestCase {

    /**
     * The namespace to instantiate from.
     */
    private static final String NAMESPACE = "com.topcoder.processor.ipserver.message";

    /**
     * The factory instance to test.
     */
    private DefaultMessageFactory factory = null;

    /**
     * Load configuration files.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        clearConfiguration();
        ConfigManager cm = ConfigManager.getInstance();
        cm.add("accuracytests/MessageFactory.xml");

        factory = new DefaultMessageFactory(NAMESPACE);
    }

    /**
     * Unload all the configuration files.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        clearConfiguration();
    }

    /**
     * Remove all the config files from the Configuration Manager.
     *
     * @throws Exception to JUnit.
     */
    private void clearConfiguration() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        for (Iterator itr = cm.getAllNamespaces(); itr.hasNext();) {
            cm.removeNamespace((String) itr.next());
        }
    }

    /**
     * This case tests factory is instantiated from namespace properly.
     *
     * @throws Exception to JUnit.
     */
    public void testInstantiateFactory() throws Exception {
        factory = new DefaultMessageFactory(NAMESPACE);

        assertNotNull("Factory can not be instantiated.", factory);
        Map messageTypes = factory.getMessageTypes();

        assertEquals("Incorrect number of message types.", 3, messageTypes.size());

        assertTrue("Does not contain certian message type.", messageTypes.containsKey("simple"));
        assertEquals("Incorrect message mapping.", SimpleSerializableMessage.class, messageTypes.get("simple"));
        assertTrue("Does not contain certian message type.", messageTypes.containsKey("extended"));
        assertEquals("Incorrect message mapping.", AccuracySerializableMessage.class, messageTypes.get("extended"));
        assertTrue("Does not contain certian message type.", messageTypes.containsKey("custom"));
        assertEquals("Incorrect message mapping.", AccuracyCustomMessage.class, messageTypes.get("custom"));
    }

    /**
     * This case tests the manipulation methods.
     */
    public void testManipulation() {
        factory.clear();
        assertEquals("Factory still contains entries.", 0, factory.getMessageTypes().size());

        factory.add("simple", SimpleSerializableMessage.class);
        factory.add("extended", AccuracySerializableMessage.class);
        factory.add("custom", AccuracyCustomMessage.class);

        assertTrue("Incorrect contains result.", factory.contains("simple"));
        assertTrue("Incorrect contains result.", factory.contains("extended"));
        assertTrue("Incorrect contains result.", factory.contains("custom"));
        assertFalse("Incorrect contains result.", factory.contains("hello"));

        assertEquals("Incorrect message type.", SimpleSerializableMessage.class, factory.get("simple"));
        assertEquals("Incorrect message type.", AccuracySerializableMessage.class, factory.get("extended"));
        assertEquals("Incorrect message type.", AccuracyCustomMessage.class, factory.get("custom"));
        assertNull("Incorrect message type.", factory.get("hello"));

        factory.remove("simple");
        factory.remove("extended");
        factory.remove("custom");
        factory.remove("hello");

        assertEquals("Factory still contains entries.", 0, factory.getMessageTypes().size());
    }

    /**
     * This case tests creation of messages.
     *
     * @throws Exception to JUnit.
     */
    public void testCreateMessage() throws Exception {
        String handlerId = "u7p3$";
        String requestId = "i0-s@";

        Message message = factory.getMessage("simple", handlerId, requestId);
        assertTrue("Incorrect message type created.", message instanceof SimpleSerializableMessage);
        assertEquals("Incorrect handler id.", handlerId, message.getHandlerId());
        assertEquals("Incorrect request id.", requestId, message.getRequestId());

        message = factory.getMessage("extended", handlerId, requestId);
        assertTrue("Incorrect message type created.", message instanceof AccuracySerializableMessage);
        assertEquals("Incorrect handler id.", handlerId, message.getHandlerId());
        assertEquals("Incorrect request id.", requestId, message.getRequestId());

        message = factory.getMessage("custom", handlerId, requestId);
        assertTrue("Incorrect message type created.", message instanceof AccuracyCustomMessage);
        assertEquals("Incorrect handler id.", handlerId, message.getHandlerId());
        assertEquals("Incorrect request id.", requestId, message.getRequestId());
    }

    /**
     * This case tests serialization of messages.
     *
     * @throws Exception to JUnit.
     */
    public void testSerializeMessage() throws Exception {
        String handlerId = "u7p3$";
        String requestId = "i0-s@";
        Message message = new AccuracyCustomMessage(handlerId, requestId);

        assertByteArray(serialize(message), factory.serializeMessage(message));
    }

    /**
     * This case tests deserialization of messages.
     *
     * @throws Exception to JUnit.
     */
    public void testDeserializeMessage() throws Exception {
        String handlerId = "u7p3$";
        String requestId = "i0-s@";
        Message message = new AccuracyCustomMessage(handlerId, requestId);

        Message message2 = factory.deserializeMessage(serialize(message));

        assertNotNull("Message is not deserialized.", message2);
        assertTrue("Incorrect message type.", message2 instanceof AccuracyCustomMessage);

        assertEquals("Message data is corrupted.", handlerId, message2.getHandlerId());
        assertEquals("Message data is corrupted.", requestId, message2.getRequestId());
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
     * Serialize a message into a byte array.
     *
     * @param object the object to serialize.
     *
     * @return the serialized byte array.
     *
     * @throws Exception to JUnit.
     */
    private byte[] serialize(Message message) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(stream);
        dataStream.writeUTF(message.getSerializerType());
        ObjectOutputStream objectStream = new ObjectOutputStream(dataStream);
        objectStream.writeObject(message.getHandlerId());
        objectStream.writeObject(message.getRequestId());
        objectStream.flush();
        return stream.toByteArray();
    }

}
