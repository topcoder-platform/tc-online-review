/*
 * Copyright (C) 2005-2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.message.email;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.topcoder.util.config.ConfigManager;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This aggregates all the tests for <code>EmailEngine</code> class of version 3.0.</p>
 * <p>
 * Add test cases to test the empty username/password for version 3.2.
 * </p>
 * @author  smell, TCSDEVELOPER
 * @version 3.2
 */
public class EmailEngineTestV3 extends TestCase {

    /** Namespace to load the address. */
    private static final String PROPERTIES_NAMESPACE = "EmailEngineTest";

    /** File location of the configuration file. */
    private static final String PROPERTIES_LOCATION = "com/topcoder/message/email/EmailEngineTest.xml";

    /** Configuration file format. */
    private static final String PROPERTIES_FORMAT = ConfigManager.CONFIG_XML_FORMAT;

    /** TCSEmailMessage instance for test. */
    private TCSEmailMessage message = null;

    /** An address for test. */
    private String addr1 = null;

    /** An address for test. */
    private String addr2 = null;

    /**
     * Sets up the fixtures. After setting up, message is a TCSEmailMessage instance and addr1 and addr2 is load from
     * the configuration.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        message = new TCSEmailMessage();

        if (addr1 == null || addr2 == null) {
            ConfigManager cm = ConfigManager.getInstance();
            try {
                addNamespace(PROPERTIES_NAMESPACE, PROPERTIES_LOCATION, PROPERTIES_FORMAT);
            } catch (Exception e) {
            }
            try {
                addr1 = cm.getString(PROPERTIES_NAMESPACE, "from");
            } catch (Exception e) {
                e.printStackTrace();
                addr1 = "smell@topcoder.com";
            }
            try {
                addr2 = cm.getString(PROPERTIES_NAMESPACE, "to");
            } catch (Exception e) {
                e.printStackTrace();
                addr2 = "BEHiker57W@topcoder.com";
            }
            removeNamespace(PROPERTIES_NAMESPACE);
        }

        message.setFromAddress(addr1);
        message.setToAddress(addr2, TCSEmailMessage.TO);
    }

    /**
     * Tears down the fixtures.
     * <p>
     * Remove the namespace introduced in the new test cases of version 3.2.
     * </p>
     * @throws Exception into JUnit
     */
    protected void tearDown() throws Exception {
        removeNamespace(EmailEngine.PROPERTIES_NAMESPACE);
        message = null;

    }
    /**
     * the junit test for the ctor.
     */
    public void testCtor() {
        assertNotNull("the instance should be created", new EmailEngine());
    }

    /**
     * Tests send() with null message, NullPointerException should be caught.
     *
     * @throws Exception to JUnit
     */
    public void testSendNull() throws Exception {
        try {
            EmailEngine.send(null);
            fail("NullPointerException should have been thrown.");
        } catch (NullPointerException expected) {
            // Success.
        }
    }

    /**
     * Tests send() with message containing no to address, IllegalArgumentException should be caught.
     *
     * @throws Exception to JUnit
     */
    public void testSendNoToAddress() throws Exception {
        TCSEmailMessage noToMessage = new TCSEmailMessage();
        noToMessage.setFromAddress(addr1);
        try {
            EmailEngine.send(noToMessage);
            fail("IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException expected) {
            // Success.
        }
    }

    /**
     * Tests send() with message containing no from address, IllegalArgumentException should be caught.
     *
     * @throws Exception to JUnit
     */
    public void testSendNoFromAddress() throws Exception {
        TCSEmailMessage noFromMessage = new TCSEmailMessage();
        noFromMessage.setToAddress(addr2, TCSEmailMessage.TO);
        try {
            EmailEngine.send(noFromMessage);
            fail("IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException expected) {
            // Success.
        }
    }

    /**
     * Tests send() with normal message without priority.
     *
     * @throws Exception into Junit
     */
    public void testSendNoPriority() throws Exception {
        String subject = "TCS Email Engine V3.0 Test Message";
        String body = "This is test text. This message should have no priority";

        message.setSubject(subject);
        message.setBody(body);

        EmailEngine.send(message);

        // no asserts here.  the recipient account should be checked manually.
    }

    /**
     * Tests send() with message with highest priority and use the default message header.
     *
     * @throws Exception to JUnit
     */
    public void testSendPriority() throws Exception {
        String subject = "TCS Email Engine V3.0 Test Message";
        String body = "This is test text. This message should not have highest priority set.";

        message.setSubject(subject);
        message.setBody(body);
        message.setPriority(PriorityLevel.HIGHEST);

        EmailEngine.send(message);

        // no asserts here.  the recipient account should be checked manually.
    }

    /**
     * Tests send() with message with low priority message with attachment.
     *
     * @throws Exception to JUnit
     */
    public void testSendPriorityWithAttachment() throws Exception {
        int numAtts = 20;
        String subject = "TCS Email Engine Test Message - " + numAtts + " attachments";
        String body = "This is test text.  this message should have " + numAtts + " attachments. And low priority";
        String attString = "this is attachment String ";

        message.setSubject(subject);
        message.setBody(body);
        message.setPriority(PriorityLevel.LOW);

        for (int i = 0; i < numAtts; i++) {
            String oneAttachmentString = attString + i + ".";
            InputStream oneIS = new ByteArrayInputStream(oneAttachmentString.getBytes());
            message.addAttachment(oneIS, "attachment " + i);
        }

        EmailEngine.send(message);

        // no asserts here.  the recipient account should be checked manually.
    }

    /**
     * Tests send() with message with normal priority and use the custom message header.
     *
     * @throws Exception to JUnit
     */
    public void testSendPriorityCustom() throws Exception {
        String namespace = "com.topcoder.message.email.TCSEmailMessage";
        String file = "CustomPriority.xml";
        addNamespace(namespace, file, ConfigManager.CONFIG_XML_FORMAT);

        String subject = "TCS Email Engine V3.0 Test Message";
        String body = "This is test text. This message should not have normal priority set with custom headers.";

        message.setSubject(subject);
        message.setBody(body);
        message.setPriority(PriorityLevel.NORMAL);
        message.setContentType("text/html");

        EmailEngine.send(message);

        // no asserts here.  the recipient account should be checked manually.
    }

    /**
     * Tests send() with message with high priority and use the custom message header. The custom configuration
     * specify to include no headers for the priority.
     *
     * @throws Exception to JUnit
     */
    public void testSendPriorityCustomNoHeaders() throws Exception {
        String namespace = "com.topcoder.message.email.TCSEmailMessage";
        String file = "CustomPriority.xml";
        addNamespace(namespace, file, ConfigManager.CONFIG_XML_FORMAT);

        String subject = "TCS Email Engine V3.0 Test Message";
        String body = "This is test text. This message should not have high priority set with custom headers. "
                + "The custom headers should be blank for this priority";

        message.setSubject(subject);
        message.setBody(body);
        message.setPriority(PriorityLevel.HIGH);

        EmailEngine.send(message);
        removeNamespace(namespace);

        // no asserts here.  the recipient account should be checked manually.
    }

    /**
     * Tests send() with message with no priority and use the custom message header. The custom configuration
     * specify to include headers for no priority.
     *
     * @throws Exception to JUnit
     */
    public void testSendPriorityCustomHeadersForNoPriority() throws Exception {
        String namespace = "com.topcoder.message.email.TCSEmailMessage";
        String file = "CustomPriority.xml";
        ConfigManager configManager = ConfigManager.getInstance();
        configManager.add(namespace, file, ConfigManager.CONFIG_XML_FORMAT);

        String subject = "TCS Email Engine V3.0 Test Message";
        String body = "This is test text. This message should not have no priority set with custom headers. "
                + "The custom headers should exist for this priority";

        message.setSubject(subject);
        message.setBody(body);
        message.setPriority(PriorityLevel.NONE);

        EmailEngine.send(message);
        removeNamespace(namespace);

        // no asserts here.  the recipient account should be checked manually.
    }
    /**
     * <p>
     * Test the send method with empty username and password in configuration
     * 'com.topcoder.message.email.EmailEngine'. It is an accuracy test case.
     * Added in version 3.2.
     * </p>
     * @throws Exception into JUnit
     */
    public void testSend_nullAuthenitcation_emptyvalue() throws Exception {
        //remove the existing namespace in case it exists
        removeNamespace(EmailEngine.PROPERTIES_NAMESPACE);
        //add the test namespaces
        addNamespace(EmailEngine.PROPERTIES_NAMESPACE, "EmailEngine_emptyusername.xml",
                     EmailEngine.PROPERTIES_FORMAT);
        String subject = "TCS Email Engine V3.0 Test Message";
        String body = "This is test text. This message should not have highest priority set.";

        message.setSubject(subject);
        message.setBody(body);
        message.setPriority(PriorityLevel.HIGHEST);

        EmailEngine.send(message);
        // no asserts here.  the recipient account should be checked manually.
    }
    /**
     * <p>
     * Test the send method with missed username and empty password in configuration
     * 'com.topcoder.message.email.EmailEngine'. It is an accuracy test case.
     * Added in version 3.2.
     * </p>
     * @throws Exception into JUnit
     */
    public void testSend_nullAuthenitcation_missing_username_property() throws Exception {
        //remove the existing namespace in case it exists
        removeNamespace(EmailEngine.PROPERTIES_NAMESPACE);
        //add the test namespaces
        addNamespace(EmailEngine.PROPERTIES_NAMESPACE, "EmailEngine_missingusername.xml",
                     EmailEngine.PROPERTIES_FORMAT);
        String subject = "TCS Email Engine V3.0 Test Message";
        String body = "This is test text. This message should not have highest priority set.";

        message.setSubject(subject);
        message.setBody(body);
        message.setPriority(PriorityLevel.HIGHEST);

        EmailEngine.send(message);
        // no asserts here.  the recipient account should be checked manually.
    }
    /**
     * <p>
     * Test the send method with empty username and missed password in configuration
     * 'com.topcoder.message.email.EmailEngine'. It is an accuracy test case.
     * Added in version 3.2.
     * </p>
     * @throws Exception into JUnit
     */
    public void testSend_nullAuthenitcation_missing_password_property() throws Exception {
        //remove the existing namespace in case it exists
        removeNamespace(EmailEngine.PROPERTIES_NAMESPACE);
        //add the test namespaces
        addNamespace(EmailEngine.PROPERTIES_NAMESPACE, "EmailEngine_missingpassword.xml",
                     EmailEngine.PROPERTIES_FORMAT);
        String subject = "TCS Email Engine V3.0 Test Message";
        String body = "This is test text. This message should not have highest priority set.";

        message.setSubject(subject);
        message.setBody(body);
        message.setPriority(PriorityLevel.HIGHEST);

        EmailEngine.send(message);
        // no asserts here.  the recipient account should be checked manually.
    }
    /**
     * Adds the configuration namespace.
     *
     * @param namespace the namespace to add
     * @param file      the configuration file
     * @param format    the file format
     * @throws Exception to JUnit
     */
    private void addNamespace(String namespace, String file, String format) throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();
        removeNamespace(namespace);
        configManager.add(namespace, file, format);
    }

    /**
     * Removes the namespace.
     *
     * @param namespace the namespace to remove.
     * @throws Exception to JUnit
     */
    private void removeNamespace(String namespace) throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();
        if (configManager.existsNamespace(namespace)) {
            configManager.removeNamespace(namespace);
        }
    }

    /**
     * Returns suite containing all the tests.
     *
     * @return suite to run the test
     */
    public static Test suite() {
        return new TestSuite(EmailEngineTestV3.class);
    }

}
