/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * @(#)NotDefaultPortTest.java
 */
package com.topcoder.message.email;

import junit.framework.TestCase;

import com.topcoder.util.config.ConfigManager;

/**
 * Unit test case to verify the Email Engine can send to SMTP server not running at default port 25.
 *
 * @author  smell
 * @version 1.0
 */
public class NotDefaultPortTest extends TestCase {

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

        ConfigManager cm = ConfigManager.getInstance();
        // try to read the address from configuration
        // if they are not configured, set them to default ones
        if (addr1 == null || addr2 == null) {
            try {
                addNamespace(PROPERTIES_NAMESPACE, PROPERTIES_LOCATION, PROPERTIES_FORMAT);
            } catch (Exception e) {
            }

            try {
                addr1 = cm.getString(PROPERTIES_NAMESPACE, "from");
            } catch (Exception e) {
                e.printStackTrace();
                addr1 = "from@topcoder.com";
            }
            try {
                addr2 = cm.getString(PROPERTIES_NAMESPACE, "to");
            } catch (Exception e) {
                e.printStackTrace();
                addr2 = "to@topcoder.com";
            }
            removeNamespace(PROPERTIES_NAMESPACE);
        }

        message.setFromAddress(addr1);
        message.setToAddress(addr2, TCSEmailMessage.TO);
    }

    /**
     * Tears down the fixtures.
     */
    protected void tearDown() {
        message = null;
    }

    /**
     * Tests send() to non default port.
     *
     * @throws Exception
     */
    public void testSendToNonDefaultPort() throws Exception {
        String subject = "TCS Email Engine V3.0 Test Message";
        String body = "This is test text. This message should have no priority";

        message.setSubject(subject);
        message.setBody(body);

        // call the method to cause the configuration file be loaded.
        try {
            EmailEngine.send(message);
        } catch (Exception ignore) {
        }

        // verify the SMTP server is running at non default port
        ConfigManager cm = ConfigManager.getInstance();
        String configuredPort = cm.getString(EmailEngine.PROPERTIES_NAMESPACE, EmailEngine.PROPERTY_SMTP_HOST_PORT);
        System.out.println("The SMTP Server is running at port " + configuredPort);
        if ("25".equals(configuredPort)) {
            fail("This test case should be used with SMTP Server that is not running at default port 25.");
        }

        // send the message
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

}
