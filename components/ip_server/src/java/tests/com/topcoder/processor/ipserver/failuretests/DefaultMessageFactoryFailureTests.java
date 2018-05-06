/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved.
 */
package com.topcoder.processor.ipserver.failuretests;

import com.topcoder.processor.ipserver.ConfigurationException;
import com.topcoder.processor.ipserver.message.DefaultMessageFactory;
import com.topcoder.processor.ipserver.message.UnknownMessageException;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Failure tests for DefaultMessageFactory implementation.
 *
 * @author brain_cn
 * @version 2.0
 * @since 2.0
 */
public class DefaultMessageFactoryFailureTests extends FailureTestCase {
    /** The test DefaultMessageFactory instance. */
    private DefaultMessageFactory instance = null;

    /**
     * The namespace for testing.
     */
    private String namespace = "namespace";

    /**
     * The requestId for testing.
     */
    private String requestId = "requestId";

    /**
     * The handlerId for testing.
     */
    private String handlerId = "handlerId";


    /**
     * The messageTypeName for testing.
     */
    private String messageTypeName = "messageTypeName";


    /**
     * The messageType for testing.
     */
    private Class messageType = MockMessage.class;

    /**
     * Creates a test suite for the tests in this test case.
     *
     * @return a TestSuite for this test case
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(DefaultMessageFactoryFailureTests.class);

        return suite;
    }

    /**
     * Setup for failure test.
     *
     * @throws Exception if any unexpected exception occurs.
     */
    public void setUp() throws Exception {
        loadNamespaces();
        instance = new DefaultMessageFactory("com.topcoder.processor.ipserver.message");
    }

    /**
     * Test of DefaultMessageFactory with null namespace.
     *
     * <p>
     * Expects NullPointerException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testDefaultMessageFactory_Null_Namespace() throws Exception {
        try {
            new DefaultMessageFactory(null);
            fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of DefaultMessageFactory with empty namespace.
     *
     * <p>
     * Expects IllegalArgumentException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testDefaultMessageFactory_Empty_Namespace() throws Exception {
        try {
            new DefaultMessageFactory(" ");
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test of DefaultMessageFactory with no-existing namespace.
     *
     * <p>
     * Expects ConfigurationException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testDefaultMessageFactory_Not_Existing_Namespace() throws Exception {
        try {
            new DefaultMessageFactory("not-existing");
            fail("ConfigurationException is expected");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Test of DefaultMessageFactory with miss MessageTypes.
     *
     * <p>
     * Expects ConfigurationException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testDefaultMessageFactory_Miss_MessageTypes() throws Exception {
        try {
            new DefaultMessageFactory(namespace);
            fail("ConfigurationException is expected");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Test of DefaultMessageFactory with invalid MessageTypes.
     *
     * <p>
     * Expects ConfigurationException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testDefaultMessageFactory_Invalid_MessageTypes() throws Exception {
        try {
            new DefaultMessageFactory(namespace);
            fail("ConfigurationException is expected");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * Test of getMessage with null messageTypeName.
     *
     * <p>
     * Expects NullPointerException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testGetMessage_Null_MessageTypeName() throws Exception {
        try {
            instance.getMessage(null, handlerId, requestId);
            fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of getMessage with null handlerId.
     *
     * <p>
     * Expects NullPointerException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testGetMessage_Null_HandlerId() throws Exception {
        try {
            instance.getMessage(messageTypeName, null, requestId);
            fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of getMessage with null requestId.
     *
     * <p>
     * Expects NullPointerException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testGetMessage_Null_RequestId() throws Exception {
        try {
            instance.getMessage(messageTypeName, handlerId, null);
            fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of getMessage with empty messageTypeName.
     *
     * <p>
     * Expects IllegalArgumentException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testGetMessage_Empty_MessageTypeName() throws Exception {
        try {
            instance.getMessage(" ", handlerId, requestId);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test of getMessage with unknow messageTypeName.
     *
     * <p>
     * Expects UnknownMessageException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testGetMessage_Unknow_MessageTypeName() throws Exception {
        try {
            instance.getMessage("unknown name", handlerId, requestId);
            fail("UnknownMessageException is expected");
        } catch (UnknownMessageException e) {
            // good
        }
    }

    /**
     * Test of deserializeMessage with null data.
     *
     * <p>
     * Expects NullPointerException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testDeserializeMessage_Null_Data() throws Exception {
        try {
            instance.deserializeMessage(null);
            fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of deserializeMessage with empty data.
     *
     * <p>
     * Expects IllegalArgumentException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testDeserializeMessage_Empty_Data() throws Exception {
        try {
            instance.deserializeMessage(new byte[0]);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
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
            instance.serializeMessage(null);
            fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of add with null messageTypeName.
     *
     * <p>
     * Expects NullPointerException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testAdd_Null_MessageTypeName() throws Exception {
        try {
            instance.add(null, messageType);
            fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of add with null messageType.
     *
     * <p>
     * Expects NullPointerException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testAdd_Null_MessageType() throws Exception {
        try {
            instance.add(messageTypeName, null);
            fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of add with empty messageTypeName.
     *
     * <p>
     * Expects IllegalArgumentException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testAdd_Empty_MessageTypeName() throws Exception {
        try {
            instance.add(" ", messageType);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test of add with invalid messageType.
     *
     * <p>
     * Expects IllegalArgumentException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testAdd_Invalid_MessageType() throws Exception {
        try {
            instance.add(messageTypeName, Object.class);
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test of remove with null messageTypeName.
     *
     * <p>
     * Expects NullPointerException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testRemove_Null_MessageTypeName() throws Exception {
        try {
            instance.remove(null);
            fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of remove with empty messageTypeName.
     *
     * <p>
     * Expects IllegalArgumentException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testRemove_Empty_MessageTypeName() throws Exception {
        try {
            instance.remove(" ");
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test of get with null messageTypeName.
     *
     * <p>
     * Expects NullPointerException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testGet_Null_MessageTypeName() throws Exception {
        try {
            instance.get(null);
            fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of get with empty messageTypeName.
     *
     * <p>
     * Expects IllegalArgumentException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testGet_Empty_MessageTypeName() throws Exception {
        try {
            instance.get(" ");
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test of contains with null messageTypeName.
     *
     * <p>
     * Expects NullPointerException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testContains_Null_MessageTypeName() throws Exception {
        try {
            instance.contains(null);
            fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of contains with empty messageTypeName.
     *
     * <p>
     * Expects IllegalArgumentException.
     * </p>
     * @throws Exception if any unexpected exception occurs.
     */
    public void testContains_Empty_MessageTypeName() throws Exception {
        try {
            instance.contains(" ");
            fail("IllegalArgumentException is expected");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
}