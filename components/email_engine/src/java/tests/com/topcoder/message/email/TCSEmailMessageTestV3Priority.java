/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 *
 * @(#)TCSEmailMessageTestV3Priority.java
 */
package com.topcoder.message.email;

import com.topcoder.util.config.ConfigManager;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates the tests for set and get priority functionalities of <code>TCSEmailMessage</code>
 * class.</p>
 *
 * @author  smell
 * @version 3.0
 */
public class TCSEmailMessageTestV3Priority extends TestCase {

    /** A TCSEmailMessage instance for test. */
    private TCSEmailMessage message = null;

    /** The namespace for test. */
    private static final String NAMESPACE = "com.topcoder.message.email.TCSEmailMessage";

    /**
     * Sets up the fixtures. After setting up, message is a blank TCSEmailMessage instance, And the namespace containing
     * configuration for the priority headers is added into the namespace.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        message = new TCSEmailMessage();

        ConfigManager configManager = ConfigManager.getInstance();
        if (configManager.existsNamespace(NAMESPACE)) {
            configManager.removeNamespace(NAMESPACE);
        }
        configManager.add(NAMESPACE, "CustomPriority.xml", ConfigManager.CONFIG_XML_FORMAT);
    }

    /**
     * Tears down the fixtures.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();
        if (configManager.existsNamespace(NAMESPACE)) {
            configManager.removeNamespace(NAMESPACE);
        }

        message = null;
    }

    /**
     * Tests getPriority() with default headers for no priority.
     */
    public void testGetPriorityDefault() {
        assertEquals("The priority is not valid.", PriorityLevel.NONE, message.getPriority());
        assertEquals("The headers are not valid.", 0, message.getHeadersMap().size());
    }

    /**
     * Tests setPriority() with null priority. NullPointerException should be caught.
     */
    public void testSetPriorityNull() {
        try {
            message.setPriority(null);
            fail("NullPointerException should have been thrown.");
        } catch (NullPointerException expected) {
            // Success.
        }
    }

    /**
     * Tests setPriority() using default headers.
     *
     * @throws Exception to JUnit
     */
    public void testSetPriorityUsingDefault() throws Exception {
        // Remove the namespace to prevent loading headers from configuration.
        ConfigManager.getInstance().removeNamespace(NAMESPACE);

        message.setPriority(PriorityLevel.HIGHEST);
        
        assertEquals("The priority is not valid.", PriorityLevel.HIGHEST, message.getPriority());
        assertEquals("The headers are not valid.", 3, message.getHeadersMap().size());
        assertEquals("The header is not valid.", "urgent", message.getHeader("Priority"));
        assertEquals("The header is not valid.", "1 (Highest)", message.getHeader("X-Priority"));
        assertEquals("The header is not valid.", "High", message.getHeader("X-MSMail-Priority"));
    }

    /**
     * Tests setPriority() using custom headers.
     */
    public void testSetPriorityCustom() {
        message.setPriority(PriorityLevel.NORMAL);
        assertEquals("The priority is not valid.", PriorityLevel.NORMAL, message.getPriority());
        assertEquals("The headers are not valid.", 2, message.getHeadersMap().size());
        assertEquals("The header is not valid.", "Super-Normal", message.getHeader("X-Priority2"));
        assertEquals("The header is not valid.", "Normal", message.getHeader("Z-Priority"));
    }

    /**
     * Tests setPriority() using custom headers for no priority.
     */
    public void testSetPriorityCustomNoPriority() {
        message.setPriority(PriorityLevel.NONE);
        assertEquals("The priority is not valid.", PriorityLevel.NONE, message.getPriority());
        assertEquals("The headers are not valid.", 1, message.getHeadersMap().size());
        assertEquals("The header is not valid.", "No Priority", message.getHeader("Priority"));
    }

    /**
     * Tests setPriority using custom headers to exclude headers for priority.
     */
    public void testSetPriorityCustomNoHeader() {
        message.setPriority(PriorityLevel.HIGH);
        assertEquals("The priority is not valid.", PriorityLevel.HIGH, message.getPriority());
        assertEquals("The headers are not valid.", 0, message.getHeadersMap().size());
    }

    /**
     * Tests setPriority with NoProperty configuration file.
     *
     * @throws Exception to JUnit
     */
    public void testSetPriorityCustomNoPropertyConfig() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();
        // Remove the good configuration.
        configManager.removeNamespace(NAMESPACE);
        // Add the bad one.
        configManager.add(NAMESPACE, "NoPropertyConfig.xml", ConfigManager.CONFIG_XML_FORMAT);

        message.setPriority(PriorityLevel.HIGH);
        assertEquals("The priority is not valid.", PriorityLevel.HIGH, message.getPriority());
        assertEquals("The headers are not valid.", 3, message.getHeadersMap().size());
        assertEquals("The header is not valid.", "urgent", message.getHeader("Priority"));
        assertEquals("The header is not valid.", "2 (High)", message.getHeader("X-Priority"));
        assertEquals("The header is not valid.", "High", message.getHeader("X-MSMail-Priority"));
    }

    /**
     * Tests setPriority with NoProperty configuration file. The file does not contain property for all the message
     * headers.
     *
     * @throws Exception to JUnit
     */
    public void testSetPriorityCustomBadConfig() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();
        // Remove the good configuration.
        configManager.removeNamespace(NAMESPACE);
        // Add the bad one.
        configManager.add(NAMESPACE, "BadConfig.xml", ConfigManager.CONFIG_XML_FORMAT);

        try {
            message.setPriority(PriorityLevel.HIGH);
            fail("IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException expected) {
            // Success.
        }

    }

    /**
     * Tests setPriority with configuration file that contains no configuration for the headers of the priority to set.
     */
    public void testSetPriorityNoConfigForNewPriority() {
        message.setPriority(PriorityLevel.LOW);
        assertEquals("The priority is not valid.", PriorityLevel.LOW, message.getPriority());
        assertEquals("The headers are not valid.", 3, message.getHeadersMap().size());
        assertEquals("The header is not valid.", "non-urgent", message.getHeader("Priority"));
        assertEquals("The header is not valid.", "4 (Low)", message.getHeader("X-Priority"));
        assertEquals("The header is not valid.", "Low", message.getHeader("X-MSMail-Priority"));
    }

    /**
     * Returns suite containing the tests.
     *
     * @return suite to run the test
     */
    public static Test suite() {
        return new TestSuite(TCSEmailMessageTestV3Priority.class);
    }

}
