/*
 * Copyright (C) 2005-2010 TopCoder Inc., All Rights Reserved.
 *
 * @(#)EmailEngineAccuracyTestV32.java
 */
package com.topcoder.message.email.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.message.email.EmailEngine;
import com.topcoder.message.email.PriorityLevel;
import com.topcoder.message.email.TCSEmailMessage;
import com.topcoder.message.email.failuretests.FailureTests;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * This class only contains accuracy test cases for update in version 3.2 for
 * <code>EmailEngine</code>.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.2
 */
public class EmailEngineAccuracyTestV32 extends TestCase {
    /**
     * The namespace.
     */
    private final static String NAMESPACE = "com.topcoder.message.email.EmailEngine";

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
     */
    protected void tearDown() {
        message = null;

    }

    /**
     * Tests send() without authentication.
     *
     * @throws Exception to JUnit
     */
    public void testSendV32_WithoutAuthentication() throws Exception {
        String subject = "TCS Email Engine V3.2 Accuracy Test";
        String body = "Tests send() without authentication.";

        message.setSubject(subject);
        message.setBody(body);
        message.setPriority(PriorityLevel.HIGHEST);

        addNamespace(new EmailEngine().getNamespace(), "accuracy/EmailEngineV32_accuracy1.xml", PROPERTIES_FORMAT);

        EmailEngine.send(message);

        removeNamespace(new EmailEngine().getNamespace());
        // no asserts here.  the recipient account should be checked manually.
    }

    /**
     * Tests send() with authentication.
     *
     * @throws Exception to JUnit
     */
    public void testSendV32_WithAuthentication() throws Exception {
        String subject = "TCS Email Engine V3.2 Accuracy Test";
        String body = "Tests send() with authentication.";

        message.setSubject(subject);
        message.setBody(body);
        message.setPriority(PriorityLevel.HIGHEST);

        addNamespace(new EmailEngine().getNamespace(), "accuracy/EmailEngineV32_accuracy2.xml", PROPERTIES_FORMAT);

        EmailEngine.send(message);

        removeNamespace(new EmailEngine().getNamespace());
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
        return new TestSuite(EmailEngineAccuracyTestV32.class);
    }

}
