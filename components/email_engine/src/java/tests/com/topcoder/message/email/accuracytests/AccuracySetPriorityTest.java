/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.message.email.accuracytests;

import com.topcoder.message.email.PriorityLevel;
import com.topcoder.message.email.TCSEmailMessage;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;

import junit.framework.TestCase;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Main test the setPriority method with custom configuration file.
 *
 * @author fairytale
 * @version 3.0
 */
public class AccuracySetPriorityTest extends TestCase {
    /** The accuracy config file used for testing. */
    private static String ACCURACY_CONFIG_FILE =
        "test_files/accuracy/TCSEmailMessageAccuracy.xml";

    /** The default loacation of config file. */
    private static String DEFAULT_CONFIG_FILE =
        "conf/com/topcoder/message/email/TCSEmailMessage.xml";

    /** The temporary config file. */
    private static String TMP_DEFAULT_CONFIG_FILE =
        "test_files/accuracy/TCSEmailMessage.xml";

    /**
     * The ConfigManager namespace for the configuration properties for the email
     * messasge class.
     */
    private static String PROPERTIES_NAMESPACE =
        "com.topcoder.message.email.TCSEmailMessage";

    /** The main <code>TCSEmailMessage</code> instance used for testing. */
    private TCSEmailMessage message;

    /**
     * Help method, print the content of a map to console.
     *
     * @param headers the map to print out.
     */
    private static void printMap(Map headers) {
        for (Iterator iter = headers.entrySet().iterator(); iter.hasNext();) {
            Map.Entry entry = (Entry) iter.next();
            System.out.println(
                "assertEquals(\"header map invalid\", \"" + entry.getValue()
                + "\", headers.get(\"" + entry.getKey() + "\"));");
        }
    }

    /**
     * Initialization for all tests here.
     *
     * @throws Exception to Junit.
     */
    protected void setUp() throws Exception {
        AccuracyConfigHelper.copyFile(DEFAULT_CONFIG_FILE, TMP_DEFAULT_CONFIG_FILE);
        AccuracyConfigHelper.copyFile(ACCURACY_CONFIG_FILE, DEFAULT_CONFIG_FILE);
        message = new TCSEmailMessage();
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
     * Test getPriority() and setPriority() methods for accuracy. Set Priority to high.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracySetPriorityHIGH() throws Exception {
        message.setPriority(PriorityLevel.HIGH);
        assertEquals("HIGH priority level.", PriorityLevel.HIGH, message.getPriority());

        // check the headers map
        Map headers = message.getHeadersMap();
        assertEquals("Override entry of size 3", 3, headers.size());
        assertEquals("header map invalid", "High", headers.get("X-MYMail-Priority"));
        assertEquals("header map invalid", "2 (High)", headers.get("X-Priority"));
        assertEquals("header map invalid", "very urgent", headers.get("Y-Priority"));
    }

    /**
     * Test getPriority() and setPriority() methods for accuracy. Set Priority to normal.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracySetPriorityNormal() throws Exception {
        message.setPriority(PriorityLevel.NORMAL);
        assertEquals(
            "NORMAL priority level.", PriorityLevel.NORMAL, message.getPriority());

        // check the headers map
        Map headers = message.getHeadersMap();
        assertEquals("Override entry of size 3", 3, headers.size());
        assertEquals("header map invalid", "normal", headers.get("Priority"));
        assertEquals("header map invalid", "", headers.get("X-Priority"));
        assertEquals("header map invalid", "Normal", headers.get("X-MSMail-Priority"));
    }

    /**
     * Test getPriority() and setPriority() methods for accuracy. Set Priority to
     * HIGHEST.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracySetPriorityHIGHEST() throws Exception {
        message.setPriority(PriorityLevel.HIGHEST);
        assertEquals(
            "HIGHEST priority level.", PriorityLevel.HIGHEST, message.getPriority());

        // check the headers map
        Map headers = message.getHeadersMap();
        assertEquals("Override entry of size 3", 3, headers.size());
        assertEquals("header map invalid", "urgent", headers.get("Priority"));
        assertEquals("header map invalid", "1 (Highest)", headers.get("X-Priority"));
        assertEquals("header map invalid", "Highest", headers.get("X-MSMail-Priority"));
    }

    /**
     * Test getPriority() and setPriority() methods for accuracy. Set Priority to LOW.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracySetPriorityLOW() throws Exception {
        message.setPriority(PriorityLevel.LOW);
        assertEquals("LOW priority level.", PriorityLevel.LOW, message.getPriority());

        // check the headers map
        Map headers = message.getHeadersMap();
        assertEquals("Override entry of size 0", 0, headers.size());
    }

    /**
     * Test getPriority() and setPriority() methods for accuracy. Set Priority to LOWEST.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracySetPriorityLOWEST() throws Exception {
        message.setPriority(PriorityLevel.LOWEST);
        assertEquals(
            "LOWEST priority level.", PriorityLevel.LOWEST, message.getPriority());

        // check the headers map
        Map headers = message.getHeadersMap();
        assertEquals("Override entry of size 2", 2, headers.size());
        assertEquals("header map invalid", "The lowest", headers.get("Z-Priority"));
        assertEquals("header map invalid", "non-urgent", headers.get("Priority"));
    }

    /**
     * Test getPriority() and setPriority() methods for accuracy. Set Priority to NONE.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracySetPriorityNONE() throws Exception {
        message.setPriority(PriorityLevel.NONE);
        assertEquals("NONE priority level.", PriorityLevel.NONE, message.getPriority());

        // check the headers map
        Map headers = message.getHeadersMap();
        assertEquals("Override entry of size 2", 2, headers.size());
        assertEquals("header map invalid", "None Priority", headers.get("X-Priority"));
        assertEquals("header map invalid", "None Priority", headers.get("Y-Priority"));
    }

    /**
     * Test getPriority() and setPriority() methods for accuracy. Set additional header,
     * then set Priority to LOWEST and then to HIGH. The settings for LOWEST should be
     * removed, others should remain.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracySetPriorityLOWESTAndHIGH()
        throws Exception {
        message.setHeader("Date", "2005-09-16");
        message.setHeader("Time", "23:00:00");
        message.setHeader("Language", "en-us");
        message.setPriority(PriorityLevel.LOWEST);
        message.setPriority(PriorityLevel.HIGH);
        assertEquals("HIGH priority level.", PriorityLevel.HIGH, message.getPriority());

        // check the headers map
        Map headers = message.getHeadersMap();
        assertEquals("Override entry of size 6", 6, headers.size());
        assertEquals("header map invalid", "High", headers.get("X-MYMail-Priority"));
        assertEquals("header map invalid", "2 (High)", headers.get("X-Priority"));
        assertEquals("header map invalid", "very urgent", headers.get("Y-Priority"));
        assertEquals("header map invalid", "2005-09-16", headers.get("Date"));
        assertEquals("header map invalid", "23:00:00", headers.get("Time"));
        assertEquals("header map invalid", "en-us", headers.get("Language"));
    }
}
