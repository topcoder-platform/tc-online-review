/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.MessageFactory;
import com.topcoder.processor.ipserver.message.DefaultMessageFactory;
import com.topcoder.processor.ipserver.message.UnknownMessageException;
import com.topcoder.processor.ipserver.message.MessageSerializationException;
import com.topcoder.processor.ipserver.message.serializable.SimpleSerializableMessage;

import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test cases for DefaultMessageFactory.
 * </p>
 *
 * @author zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class DefaultMessageFactoryTest extends TestCase {

    /**
     * The configuration file for this test case.
     */
    private static final String CONFIG_FILE = "DefaultMessageFactoryTest.xml";

    /**
     * The namespaces in the configuration file.
     */
    private static final String[] NAMESPACES = new String[]{
        // valid configuration
        "com.topcoder.processor.ipserver.message.DefaultMessageFactory.DeveloperTest",
        // the following 6 namespaces are used for failure tests
        "com.topcoder.processor.ipserver.message.DefaultMessageFactory.DeveloperTest.Failure1",
        "com.topcoder.processor.ipserver.message.DefaultMessageFactory.DeveloperTest.Failure2",
        "com.topcoder.processor.ipserver.message.DefaultMessageFactory.DeveloperTest.Failure3",
        "com.topcoder.processor.ipserver.message.DefaultMessageFactory.DeveloperTest.Failure4",
        "com.topcoder.processor.ipserver.message.DefaultMessageFactory.DeveloperTest.Failure5",
        "com.topcoder.processor.ipserver.message.DefaultMessageFactory.DeveloperTest.Failure6",
        "com.topcoder.processor.ipserver.message.DefaultMessageFactory.DeveloperTest.Failure7"
    };

    /**
     * A DefaultMessageFactory instance used for testing.
     */
    private DefaultMessageFactory factory;

    /**
     * A Message instance for the following tests.
     */
    private Message message;

    /**
     * Set up the test environment. Load the configuration file and initialize the factory and message.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        cm.add(CONFIG_FILE);

        factory = new DefaultMessageFactory(NAMESPACES[0]);
        message = factory.getMessage("simple", "handler id", "request id");
    }

    /**
     * Clear the test environment. Release the namespaces in configuration manager.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        for (int loop = 0; loop < NAMESPACES.length; loop++) {
            if (cm.existsNamespace(NAMESPACES[loop])) {
                cm.removeNamespace(NAMESPACES[loop]);
            }
        }
    }

    /**
     * Tests if the the DefaultMessageFactory implements MessageFactory.
     */
    public void testClassType() {
        Object obj = factory;
        assertTrue("The DefaultMessageFactory does not implement MessageFactory.", obj instanceof MessageFactory);
    }

    /**
     * Tests if the constructor works correctly.
     *
     * @throws Exception to JUnit
     */
    public void testConstructor1() throws Exception {
        // no exception
        new DefaultMessageFactory(NAMESPACES[0]);
    }

    /**
     * Tests if the constructor handles null namespace properly.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorFailure1() throws Exception {
        try {
            new DefaultMessageFactory(null);
            fail("NullPointerException should be thrown for null namespace.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Tests if the constructor handles empty namespace properly.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorFailure2() throws Exception {
        try {
            new DefaultMessageFactory("      ");
            fail("IllegalArgumentException should be thrown for empty namespace.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Tests if the constructor handles invalid configuration properly.
     */
    public void testConstructorFailure3() {
        for (int loop = 1; loop < NAMESPACES.length; loop++) {
            try {
                new DefaultMessageFactory(NAMESPACES[loop]);
                fail("ConfigurationException should be thrown for invlaid configuration. The namespace is "
                    + NAMESPACES[loop]);
            } catch (ConfigurationException e) {
                // success
            }
        }
    }

    /**
     * Tests if the getMessage method works correctly.
     *
     * @throws Exception to JUnit
     */
    public void testGetMessage() throws Exception {
        Message msg = factory.getMessage("simple", "handler id", "request id");
        assertEquals("The handler ID is incorrect.", "handler id", msg.getHandlerId());
        assertEquals("The request ID is incorrect.", "request id", msg.getRequestId());
        assertEquals("The serializer type is incorrect.",
            "com.topcoder.processor.ipserver.message.serializable.SerializableMessageSerializer",
            msg.getSerializerType());
    }

    /**
     * Tests if the getMessage method handles null parameter properly.
     *
     * @throws Exception to JUnit
     */
    public void testGetMessageFailure1() throws Exception {
        try {
            factory.getMessage(null, "handler id", "request id");
            fail("NullPointerException should be thrown for null parameter.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Tests if the getMessage method handles null parameter properly.
     *
     * @throws Exception to JUnit
     */
    public void testGetMessageFailure2() throws Exception {
        try {
            factory.getMessage("simple", null, "request id");
            fail("NullPointerException should be thrown for null parameter.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Tests if the getMessage method handles invalid message type name properly.
     *
     * @throws Exception to JUnit
     */
    public void testGetMessageFailure4() throws Exception {
        try {
            factory.getMessage("   ", "handler id", "request id");
            fail("IllegalArgumentException should be thrown for empty message type name.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Tests if the getMessage method handles invalid message type name properly.
     *
     * @throws Exception to JUnit
     */
    public void testGetMessageFailure5() throws Exception {
        try {
            factory.getMessage("no such name", "handler id", "request id");
            fail("UnknownMessageException should be thrown for invalid message type name.");
        } catch (UnknownMessageException e) {
            // success
        }
    }

    /**
     * Tests if the serializeMessage and deserializeMessage methods work correctly.
     *
     * @throws Exception to JUnit
     */
    public void testSerialization() throws Exception {
        Message msg = factory.deserializeMessage(factory.serializeMessage(message));

        assertEquals("The serialization is incorrect.", msg.getHandlerId(), message.getHandlerId());
        assertEquals("The serialization is incorrect.", msg.getRequestId(), message.getRequestId());
        assertEquals("The serialization is incorrect.", msg.getSerializerType(), message.getSerializerType());
    }

    /**
     * Tests if the deserializeMessage method handles null parameter properly.
     *
     * @throws Exception to JUnit
     */
    public void testDeserializeMessageFailure1() throws Exception {
        try {
            factory.deserializeMessage(null);
            fail("NullPointerException should be thrown for null data.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Tests if the deserializeMessage method handles empty data properly.
     *
     * @throws Exception to JUnit
     */
    public void testDeserializeMessageFailure2() throws Exception {
        try {
            factory.deserializeMessage(new byte[0]);
            fail("IllegalArgumentException should be thrown for empty data.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Tests if the deserializeMessage method handles invalid data properly.
     *
     * @throws Exception to JUnit
     */
    public void testDeserializeMessageFailure3() throws Exception {
        try {
            factory.deserializeMessage("invalid".getBytes());
            fail("MessageSerializationException should be thrown for invalid data.");
        } catch (MessageSerializationException e) {
            // success
        }
    }

    /**
     * Tests if the serializeMessage method handles null parameter properly.
     *
     * @throws Exception to JUnit
     */
    public void testSerializeMessageFailure1() throws Exception {
        try {
            factory.serializeMessage(null);
            fail("NullPointerException should be thrown for null message.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Tests if the serializeMessage method handles invalid message properly.
     *
     * @throws Exception to JUnit
     */
    public void testSerializeMessageFailure2() throws Exception {
        try {
            factory.serializeMessage(new CustomMessage());
            fail("MessageSerializationException should be thrown when there's no suitable serializer.");
        } catch (MessageSerializationException e) {
            // success
        }
    }

    /**
     * Tests the add, clear and getMessageTypes methods.
     */
    public void testAddClearGetMessageTypes() {
        assertEquals("At first there should be two message types.", 2, factory.getMessageTypes().size());
        factory.clear();
        assertEquals("The message types are not cleared.", 0, factory.getMessageTypes().size());
        factory.add("simple", SimpleSerializableMessage.class);
        assertEquals("The new message type is not added correctly.", 1, factory.getMessageTypes().size());
        // The old message type should be replaced by the new one.
        factory.add("simple", CustomMessage.class);
        assertEquals("The new message type is not added correctly.", 1, factory.getMessageTypes().size());
        assertEquals("The old message type is not replaced by the new one.",
            CustomMessage.class, factory.getMessageTypes().get("simple"));
    }

    /**
     * Tests if the add method handles null parameter properly.
     */
    public void testAddFailure1() {
        try {
            factory.add(null, CustomMessage.class);
            fail("NullPointerException should be thrown for null parameter.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Tests if the add method handles null parameter properly.
     */
    public void testAddFailure2() {
        try {
            factory.add("simple", null);
            fail("NullPointerException should be thrown for null parameter.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Tests if the add method handles empty message type name properly.
     */
    public void testAddFailure3() {
        try {
            factory.add("   ", CustomMessage.class);
            fail("IllegalArgumentException should be thrown for empty message type name.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Tests if the add method handles invalid message type properly.
     */
    public void testAddFailure4() {
        try {
            factory.add("simple", String.class);
            fail("IllegalArgumentException should be thrown for invalid message type.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Tests if the add method handles invalid message type properly.
     */
    public void testAddFailure5() {
        try {
            factory.add("simple", Message.class);
            fail("IllegalArgumentException should be thrown for invalid message type.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Tests the remove and getMessageTypes methods.
     */
    public void testRemoveAndGetMessageTypes() {
        assertEquals("At first there should be two message types.", 2, factory.getMessageTypes().size());
        factory.remove("simple");
        assertEquals("The message type is not removed correctly.", 1, factory.getMessageTypes().size());
        factory.remove("no such name");
        assertEquals("Nothing should be done.", 1, factory.getMessageTypes().size());
    }

    /**
     * Tests if the remove method handles null parameter properly.
     */
    public void testRemoveFailure1() {
        try {
            factory.remove(null);
            fail("NullPointerException should be thrown for null parameter.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Tests if the remove method handles empty message type name properly.
     */
    public void testRemoveFailure2() {
        try {
            factory.remove("   ");
            fail("IllegalArgumentException should be thrown for empty message type name.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Tests the get method.
     */
    public void testGet() {
        Class cls = factory.get("simple");
        assertEquals("The get method doest not work correctly.", SimpleSerializableMessage.class, cls);
        cls = factory.get("no such name");
        assertNull(
            "The get method doest not work correctly, null should be returned is there's no message type is found.",
            cls);
    }

    /**
     * Tests if the get method handles null parameter properly.
     */
    public void testGetFailure1() {
        try {
            factory.get(null);
            fail("NullPointerException should be thrown for null parameter.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Tests if the get method handles empty message type name properly.
     */
    public void testGetFailure2() {
        try {
            factory.get("   ");
            fail("IllegalArgumentException should be thrown for empty message type name.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Tests the contains method.
     */
    public void testContains() {
        assertTrue("The contains method doest not work correctly.", factory.contains("simple"));
        assertFalse("The contains method doest not work correctly.", factory.contains("no such name"));
    }

    /**
     * Tests if the contains method handles null parameter properly.
     */
    public void testContainsFailure1() {
        try {
            factory.contains(null);
            fail("NullPointerException should be thrown for null parameter.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Tests if the contains method handles empty message type name properly.
     */
    public void testContainsFailure2() {
        try {
            factory.contains("   ");
            fail("IllegalArgumentException should be thrown for empty message type name.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Tests the getMessageTypes method. Modifying the result map should not affect the internal data structure.
     */
    public void testGetMessageTypes() {
        assertEquals("At first there should be two message types.", 2, factory.getMessageTypes().size());
        // Modifying the result map should not affect the internal data structure.
        factory.getMessageTypes().clear();
        assertEquals("At first there should be two message types.", 2, factory.getMessageTypes().size());
    }
}
