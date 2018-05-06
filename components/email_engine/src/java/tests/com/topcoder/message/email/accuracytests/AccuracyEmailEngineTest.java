/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.message.email.accuracytests;

import com.topcoder.message.email.EmailEngine;
import com.topcoder.message.email.PriorityLevel;
import com.topcoder.message.email.TCSEmailMessage;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;

import junit.framework.TestCase;


/**
 * Test <code>EmailEngine</code> class for accuracy.
 *
 * @author fairytale, KLW
 * @version 3.1
 * @since 3.0
 */
public class AccuracyEmailEngineTest extends TestCase {
    /** The default loacation of config file. */
    private static String DEFAULT_CONFIG_FILE =
        "conf/com/topcoder/message/email/TCSEmailMessage.xml";

    /** The default loacation of config file. */
    private static String CONFIG_FILE = "test_files/accuracy/TCSEmailMessageDefault.xml";

    /** The temporary config file. */
    private static String TMP_DEFAULT_CONFIG_FILE =
        "test_files/accuracy/TCSEmailMessage.xml";

    /**
     * The ConfigManager namespace for the configuration properties for the email
     * messasge class.
     */
    private static String PROPERTIES_NAMESPACE =
        "com.topcoder.message.email.TCSEmailMessage";

    /**
     * Initialization for all tests here.
     *
     * @throws Exception to Junit.
     */
    protected void setUp() throws Exception {
        AccuracyConfigHelper.copyFile(DEFAULT_CONFIG_FILE, TMP_DEFAULT_CONFIG_FILE);
        AccuracyConfigHelper.copyFile(CONFIG_FILE, DEFAULT_CONFIG_FILE);
    }

    /**
     * Clear environment.
     *
     * @throws UnknownNamespaceException to Junit.
     */
    protected void tearDown() throws UnknownNamespaceException {
        AccuracyConfigHelper.copyFile(TMP_DEFAULT_CONFIG_FILE, DEFAULT_CONFIG_FILE);

        ConfigManager cm = ConfigManager.getInstance();

        if (cm.existsNamespace(PROPERTIES_NAMESPACE)) {
            cm.removeNamespace(PROPERTIES_NAMESPACE);
        }
    }

    /**
     * Test send(TCSEmailMessage) method for accuracy. Set priority to Highest.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracySendHighest() throws Exception {
        TCSEmailMessage message = new TCSEmailMessage();
        message.setSubject("Highest Priority");
        message.setBody("Pretty Important\n-Writer\n");
        message.setFromAddress("from@topcoder.com", "My Name");
        message.setToAddress("to@topcoder.com", "Your Name", TCSEmailMessage.TO);
        message.setPriority(PriorityLevel.HIGHEST);
        EmailEngine.send(message);
    }

    /**
     * Test send(TCSEmailMessage) method for accuracy. Set priority to Low.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracySendLow() throws Exception {
        TCSEmailMessage message = new TCSEmailMessage();
        message.setSubject("Low Priority");
        message.setBody("Not Important\n-Writer\n");
        message.setFromAddress("from@topcoder.com", "My Name");
        message.setToAddress("to@topcoder.com", "Your Name", TCSEmailMessage.TO);
        message.setPriority(PriorityLevel.LOW);
        EmailEngine.send(message);
    }

    /**
     * Test send(TCSEmailMessage) method for accuracy. Set priority to High.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracySendHigh() throws Exception {
        TCSEmailMessage message = new TCSEmailMessage();
        message.setSubject("High Priority");
        message.setBody("Important\n-Writer\n");
        message.setFromAddress("from@topcoder.com", "My Name");
        message.setToAddress("to@topcoder.com", "Your Name", TCSEmailMessage.TO);
        message.setPriority(PriorityLevel.HIGH);
        EmailEngine.send(message);
    }

    /**
     * Test send(TCSEmailMessage) method for accuracy. Set priority to Lowest.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracySendLowest() throws Exception {
        TCSEmailMessage message = new TCSEmailMessage();
        message.setSubject("Lowest Priority");
        message.setBody("least Important\n-Writer\n");
        message.setFromAddress("from@topcoder.com", "My Name");
        message.setToAddress("to@topcoder.com", "Your Name", TCSEmailMessage.TO);
        message.setPriority(PriorityLevel.LOWEST);
        EmailEngine.send(message);
    }

    /**
     * Test send(TCSEmailMessage) method for accuracy. Set priority to Normal.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracySendNormal() throws Exception {
        TCSEmailMessage message = new TCSEmailMessage();
        message.setSubject("Normal Priority");
        message.setBody("common mail\n-Writer\n");
        message.setFromAddress("from@topcoder.com", "My Name");
        message.setToAddress("to@topcoder.com", "Your Name", TCSEmailMessage.TO);
        message.setPriority(PriorityLevel.NORMAL);
        EmailEngine.send(message);
    }

    /**
     * Test send(TCSEmailMessage) method for accuracy. Default to none.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracySendNONE() throws Exception {
        TCSEmailMessage message = new TCSEmailMessage();
        message.setSubject("Default None Priority");
        message.setBody("common mail\n-Writer\n");
        message.setFromAddress("from@topcoder.com", "My Name");
        message.setToAddress("to@topcoder.com", "Your Name", TCSEmailMessage.TO);
        EmailEngine.send(message);
    }
    /**
     * Test send(TCSEmailMessage) method for accuracy. set the content type.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracySendWithContentType()throws Exception{
        TCSEmailMessage message = new TCSEmailMessage();
        message.setSubject("Normal Priority");
        message.setBody("common mail\n-Writer\n");
        message.setFromAddress("from@topcoder.com", "My Name");
        message.setToAddress("to@topcoder.com", "Your Name", TCSEmailMessage.TO);
        message.setPriority(PriorityLevel.NORMAL);
        message.setContentType("text/html");
        EmailEngine.send(message);
    }
}
