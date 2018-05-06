/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.message.email.accuracytests;

import com.topcoder.message.email.PriorityLevel;
import com.topcoder.message.email.TCSEmailMessage;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;

import junit.framework.TestCase;

import java.util.Map;


/**
 * Test <code>TCSEmailMessage</code> class for accuracy.
 *
 * @author fairytale
 * @version 3.0
 */
public class AccuracyTCSEmailMessageTest extends TestCase {
    /** The empty config file used for testing. */
    private static String EMPTY_CONFIG_FILE =
        "test_files/accuracy/TCSEmailMessageEmpty.xml";

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
     * Initialization for all tests here.
     *
     * @throws Exception to Junit.
     */
    protected void setUp() throws Exception {
        AccuracyConfigHelper.copyFile(DEFAULT_CONFIG_FILE, TMP_DEFAULT_CONFIG_FILE);
        AccuracyConfigHelper.copyFile(EMPTY_CONFIG_FILE, DEFAULT_CONFIG_FILE);
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
     * Test getHeadersMap() method for accuracy.
     */
    public void testAccuracyGetHeadersMap45() {
        assertEquals("initially emtpy", 0, message.getHeadersMap().size());
    }

    /**
     * Test getHeadersMap() method for accuracy by set 3 headers.
     */
    public void testAccuracyGetHeadersMap46() {
        // set headers
        message.setHeader("Date", "2005-09-16");
        message.setHeader("Time", "23:00:00");
        message.setHeader("Language", "en-us");

        // get headers map and check
        Map headers = message.getHeadersMap();
        assertEquals("3 values", 3, headers.size());
        assertEquals("Date should exist", "2005-09-16", headers.get("Date"));
        assertEquals("Time should exist", "23:00:00", headers.get("Time"));
        assertEquals("Language should exist", "en-us", headers.get("Language"));
    }

    /**
     * Test getHeadersMap() method for accuracy by set duplicate headers.
     */
    public void testAccuracyGetHeadersMap47() {
        // set duplicate headers
        message.setHeader("Date", "2005-09-16");
        message.setHeader("Date", "23:00:00");
        message.setHeader("Date", "en-us");

        // get headers map and check
        Map headers = message.getHeadersMap();
        assertEquals("Only the last value", 1, headers.size());
        assertEquals("Date should exist", "en-us", headers.get("Date"));
    }

    /**
     * Test getHeadersMap() method to see if the return is modifiable.
     */
    public void testAccuracyGetHeadersMap48() {
        Map headers = message.getHeadersMap();

        try {
            headers.put("ABC", "ABC");
            fail("unmodifiable map should be returned");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    /**
     * Test getHeader(String) method for accuracy set 3 headers.
     */
    public void testAccuracyGetHeader51() {
        // set headers
        message.setHeader("Date", "2005-09-16");
        message.setHeader("Time", "23:00:00");
        message.setHeader("Language", "en-us");

        // get headers map and check
        assertEquals("Date should exist", "2005-09-16", message.getHeader("Date"));
        assertEquals("Time should exist", "23:00:00", message.getHeader("Time"));
        assertEquals("Language should exist", "en-us", message.getHeader("Language"));
    }

    /**
     * Test getHeader(String) method for accuracy by set duplicate headers.
     */
    public void testAccuracyGetHeader52() {
        // set duplicate headers
        message.setHeader("Date", "2005-09-16");
        message.setHeader("Date", "23:00:00");
        message.setHeader("Date", "en-us");

        // get headers map and check
        assertEquals("Date should exist", "en-us", message.getHeader("Date"));
    }

    /**
     * Test getHeader(String) method for accuracy by pass none-exist key.
     */
    public void testAccuracyGetHeader53() {
        assertNull(
            "header for Language has not been set.", message.getHeader("Language"));
    }

    /**
     * Test removeHeader(String) method for accuracy by remove time onece.
     */
    public void testAccuracyRemoveHeader54() {
        // set 3 headers
        message.setHeader("Date", "2005-09-16");
        message.setHeader("Time", "23:00:00");
        message.setHeader("Language", "en-us");
        assertEquals("3 values", 3, message.getHeadersMap().size());

        // remove Time
        assertEquals("time should be removed", "23:00:00", message.removeHeader("Time"));
        assertEquals("2 values", 2, message.getHeadersMap().size());
        assertNull("Time should not exist", message.getHeader("Time"));
        assertEquals("Date should exist", "2005-09-16", message.getHeader("Date"));
        assertEquals("Language should exist", "en-us", message.getHeader("Language"));
    }

    /**
     * Test removeHeader(String) method for accuracy by remove time twice.
     */
    public void testAccuracyRemoveHeader55() {
        // set 3 headers
        message.setHeader("Date", "2005-09-16");
        message.setHeader("Time", "23:00:00");
        message.setHeader("Language", "en-us");
        assertEquals("3 values", 3, message.getHeadersMap().size());

        // remove Time twice
        assertEquals("time should be removed", "23:00:00", message.removeHeader("Time"));
        assertNull("time does not exist", message.removeHeader("Time"));
        assertEquals("still 2 values", 2, message.getHeadersMap().size());
        assertNull("Time should not exist", message.getHeader("Time"));
        assertEquals("Date should exist", "2005-09-16", message.getHeader("Date"));
        assertEquals("Language should exist", "en-us", message.getHeader("Language"));
    }

    /**
     * Test removeHeader(String) method for accuracy by remove all.
     */
    public void testAccuracyRemoveHeader56() {
        // set 3 headers
        message.setHeader("Date", "2005-09-16");
        message.setHeader("Time", "23:00:00");
        message.setHeader("Language", "en-us");
        assertEquals("3 values", 3, message.getHeadersMap().size());

        // remove all
        assertEquals("Time should be removed", "23:00:00", message.removeHeader("Time"));
        assertEquals(
            "Date should be removed", "2005-09-16", message.removeHeader("Date"));
        assertEquals(
            "Language should be removed", "en-us", message.removeHeader("Language"));
        assertEquals("nothing exist", 0, message.getHeadersMap().size());
    }

    /**
     * Test getPriority() and setPriority() methods for accuracy. Initially should be set
     * to NONE.
     */
    public void testAccuracyGetPriority57() {
        assertEquals("NONE priority level.", PriorityLevel.NONE, message.getPriority());

        // check the headers map
        Map headers = message.getHeadersMap();
        assertEquals("Default entry of size 0", 0, headers.size());
    }

    /**
     * Test logic of default highest priority headers map.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracyDefaultHighestLevel() throws Exception {
        message.setPriority(PriorityLevel.HIGHEST);
        assertEquals(
            "HIGHEST priority level.", PriorityLevel.HIGHEST, message.getPriority());

        // check the headers map
        Map headers = message.getHeadersMap();
        assertEquals("Default entry of size 3", 3, headers.size());
        assertEquals("default priority is not valid", "urgent", headers.get("Priority"));
        assertEquals(
            "default X-Priority is not valid", "1 (Highest)", headers.get("X-Priority"));
        assertEquals(
            "default X-MSMail-Priority is not valid", "High",
            headers.get("X-MSMail-Priority"));
    }

    /**
     * Test logic of default high priority headers map.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracyDefaultHighLevel() throws Exception {
        message.setPriority(PriorityLevel.HIGH);
        assertEquals("HIGH priority level.", PriorityLevel.HIGH, message.getPriority());

        // check the headers map
        Map headers = message.getHeadersMap();
        assertEquals("Default entry of size 3", 3, headers.size());
        assertEquals("default priority is not valid", "urgent", headers.get("Priority"));
        assertEquals(
            "default X-Priority is not valid", "2 (High)", headers.get("X-Priority"));
        assertEquals(
            "default X-MSMail-Priority is not valid", "High",
            headers.get("X-MSMail-Priority"));
    }

    /**
     * Test logic of default normal priority headers map.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracyDefaultNormalLevel() throws Exception {
        message.setPriority(PriorityLevel.NORMAL);
        assertEquals(
            "NORMAL priority level.", PriorityLevel.NORMAL, message.getPriority());

        // check the headers map
        Map headers = message.getHeadersMap();
        assertEquals("Default entry of size 3", 3, headers.size());
        assertEquals("default priority is not valid", "normal", headers.get("Priority"));
        assertEquals(
            "default X-Priority is not valid", "3 (Normal)", headers.get("X-Priority"));
        assertEquals(
            "default X-MSMail-Priority is not valid", "Normal",
            headers.get("X-MSMail-Priority"));
    }

    /**
     * Test logic of default low priority headers map.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracyDefaultLowLevel() throws Exception {
        message.setPriority(PriorityLevel.LOW);
        assertEquals("Low priority level.", PriorityLevel.LOW, message.getPriority());

        // check the headers map
        Map headers = message.getHeadersMap();
        assertEquals("Default entry of size 3", 3, headers.size());
        assertEquals(
            "default priority is not valid", "non-urgent", headers.get("Priority"));
        assertEquals(
            "default X-Priority is not valid", "4 (Low)", headers.get("X-Priority"));
        assertEquals(
            "default X-MSMail-Priority is not valid", "Low",
            headers.get("X-MSMail-Priority"));
    }

    /**
     * Test logic of default lowest priority headers map.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracyDefaultLowestLevel() throws Exception {
        message.setPriority(PriorityLevel.LOWEST);
        assertEquals(
            "Lowest priority level.", PriorityLevel.LOWEST, message.getPriority());

        // check the headers map
        Map headers = message.getHeadersMap();
        assertEquals("Default entry of size 3", 3, headers.size());
        assertEquals(
            "default priority is not valid", "non-urgent", headers.get("Priority"));
        assertEquals(
            "default X-Priority is not valid", "5 (Lowest)", headers.get("X-Priority"));
        assertEquals(
            "default X-MSMail-Priority is not valid", "Low",
            headers.get("X-MSMail-Priority"));
    }

    /**
     * Test logic of default none priority headers map, should not exist.
     *
     * @throws Exception to Junit.
     */
    public void testAccuracyDefaultNoneLevel() throws Exception {
        message.setPriority(PriorityLevel.NONE);
        assertEquals("None priority level.", PriorityLevel.NONE, message.getPriority());

        // check the headers map
        Map headers = message.getHeadersMap();
        assertEquals("Default entry of size 0", 0, headers.size());
    }
}
