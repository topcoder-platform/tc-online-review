/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 *
 * @(#)TCSEmailMessageTestV3Normal.java
 */
package com.topcoder.message.email;

import java.util.Map;

import javax.mail.Address;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates the tests for normal methods of <code>TCSEmailMessage</code> of version 3.0. Only the
 * functionalities added or affected in version 3.0 are tested in the test case.</p>
 *
 * @author  smell
 * @version 3.0
 */
public class TCSEmailMessageTestV3Normal extends TestCase {

    /**
     * A TCSEmailMessage instance for test.
     */
    private TCSEmailMessage message = null;

    /**
     * Sets up the fixtures. After setting up, message is a blank TCSEmailMessage instance.
     */
    protected void setUp() {
        message = new TCSEmailMessage();
    }

    /**
     * Tears down the fixtures.
     */
    protected void tearDown() {
        message = null;
    }

    /**
     * Tests setFromAddress() with null input, NullPointerException should be caught.
     *
     * @throws Exception to JUnit
     */
    public void testSetFromAddressNull() throws Exception {
        try {
            message.setFromAddress(null);
            fail("NullPointerException should have been thrown.");
        } catch (NullPointerException expected) {
            // Success.
        }
    }

    /**
     * Tests setFromAddress() with invalid address, AddressException should be caught.
     */
    public void testSetFromAddressInvalid() {
        try {
            message.setFromAddress("Invalid!!$tnh3\n**");
            fail("AddressException should have been thrown.");
        } catch (AddressException expected) {
            // Success.
        }
    }

    /**
     * Tests setFromAddress(String, String) with the first argument null. NullPointerException should be caught.
     *
     * @throws Exception to JUnit
     */
    public void testSetFromAddressStringNullString() throws Exception {
        try {
            message.setFromAddress(null, "smell");
            fail("NullPointerException should have been thrown.");
        } catch (NullPointerException expected) {
            // Success.
        }
    }

    /**
     * Tests setFromAddress(String, String) with the second argument null. NullPointerException should be caught.
     *
     * @throws Exception to JUnit
     */
    public void testSetFromAddressStringStringNull() throws Exception {
        try {
            message.setFromAddress("smell@topcoder.com", null);
            fail("NullPointerException should have been thrown.");
        } catch (NullPointerException expected) {
            // Success.
        }
    }

    /**
     * Tests setFromAddress(String, String) with invalid address. AddressException should be caught.
     */
    public void testSetFromAddressStringStringInvalid() {
        try {
            message.setFromAddress("Invalid!!$tnh3\n**", "Bad");
            fail("AddressException should have been thrown.");
        } catch (AddressException expected) {
            // Success.
        }
    }

    /**
     * Tests setFromAddress(String, String) with valid address.
     *
     * @throws Exception to JUnit
     */
    public void testSetFromAddressStringStringValid() throws Exception {
        message.setFromAddress("smell@topcoder.com", "TCSDEVELOPER");
        Address address = message.getFromAddress();
        assertEquals("The type of from address is not valid", "rfc822", address.getType());
    }

    /**
     * Tests setToAddress() with null address, NullPointerException should be caught.
     *
     * @throws Exception to JUnit
     */
    public void testSetToAddressNull() throws Exception {
        try {
            message.setToAddress(null, TCSEmailMessage.TO);
            fail("NullPointerException should have been thrown.");
        } catch (NullPointerException expected) {
            // Success.
        }
    }

    /**
     * Tests setToAddress() with invalid to address type, IllegalArgumentException should be caught.
     *
     * @throws Exception to JUnit
     */
    public void testSetToAddressInvalidType() throws Exception {
        try {
            message.setToAddress("smell@topcoder.com", 10);
            fail("IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException expected) {
            // Success.
        }
    }

    /**
     * Tests setToAddress() with invalid to address, AddressException should be caught.
     */
    public void testSetToAddressInvalidAddress() {
        try {
            message.setToAddress("Invalid!!$tnh3\n**", TCSEmailMessage.CC);
            fail("AddressException should have been thrown.");
        } catch (AddressException expected) {
            // Success.
        }
    }

    /**
     * Tests setToAddress(String, String, int) with the first argument null. NullPointerException should be caught.
     *
     * @throws Exception to JUnit
     */
    public void testSetToAddressStringNullStringInt() throws Exception {
        try {
            message.setToAddress(null, "smell", TCSEmailMessage.TO);
            fail("NullPointerException should have been thrown.");
        } catch (NullPointerException expected) {
            // Success.
        }
    }

    /**
     * Tests setToAddress(String, String, int) with the first argument null. NullPointerException should be caught.
     *
     * @throws Exception to JUnit
     */
    public void testSetToAddressStringStringNullInt() throws Exception {
        try {
            message.setToAddress("smell@topcoder.com", null, TCSEmailMessage.BCC);
            fail("NullPointerException should have been thrown.");
        } catch (NullPointerException expected) {
            // Success.
        }
    }

    /**
     * Tests setToAddress(String, String, int) with invalid type. IllegalArgumentException should be caught.
     *
     * @throws Exception
     */
    public void testSetToAddressStringStringIntInvalidType() throws Exception {
        try {
            message.setToAddress("smell@topcoder.com", "TCSDEVELOPER", -1);
            fail("IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException expected) {
            // Success.
        }
    }

    /**
     * Tests setToAddress(String, String, int) with invalid address. AddressException should be caught.
     */
    public void testSetToAddressStringStringIntInvalidAddress() {
        try {
            message.setToAddress("invalid address", "smell", TCSEmailMessage.BCC);
            fail("AddressException should have been thrown.");
        } catch (AddressException expected) {
            // Success.
        }
    }

    /**
     * Tests setToAddress(String, String, int) with valid address.
     *
     * @throws Exception to JUnit
     */
    public void testSetToAddressStringStringIntValidAddress() throws Exception {
        message.setToAddress("TCSEVELOPER@topcoder.com", "smell", TCSEmailMessage.BCC);
        Address[] address = message.getToAddress(TCSEmailMessage.BCC);
        assertEquals("The address is not valid", 1, address.length);
        assertEquals("The address is not valid", "rfc822", address[0].getType());
    }

    /**
     * Tests getToAddress with invalid address type. IllegalArgumentException should be caught.
     */
    public void testGetToAddressInvalidType() {
        try {
            message.getToAddress(10);
        } catch (IllegalArgumentException expected) {
            // Success.
        }
    }

    /**
     * Tests addAttachment() with null attachment, NullPointerException should be caught.
     */
    public void testAddAttachmentsNullAttachment() {
        try {
            message.addAttachment(null, "null");
            fail("NullPointerException should have been thrown.");
        } catch (NullPointerException expected) {
            // Success.
        }
    }

    /**
     * Tests addAttachment() with null attachment name, NullPointerException should be caught.
     */
    public void testAddAttachmentsNullName() {
        try {
            message.addAttachment(System.in, null);
            fail("NullPointerException should have been thrown.");
        } catch (NullPointerException expected) {
            // Success.
        }
    }

    /**
     * Adds some headers into the message. It helps some tests to get an email message with some headers.
     */
    private void initHeaders() {
        message.setHeader("key1", "value1");
        message.setHeader("key2", "value2");
    }

    /**
     * Tests getHeadersMap().
     */
    public void testGetHeadersMap() {
        initHeaders();
        Map headers = message.getHeadersMap();
        assertEquals("The size of returned Map is not valid.", 2, headers.size());
        assertEquals("The value for key1 is not valid.", "value1", headers.get("key1"));
        assertEquals("The value for key2 is not valid.", "value2", headers.get("key2"));
    }

    /**
     * Tests setHeader() with null key. NullPointerException should be caught.
     */
    public void testSetHeaderNullKey() {
        try {
            message.setHeader(null, "value");
            fail("NullPointerException should have been thrown.");
        } catch (NullPointerException expected) {
            // Success.
        }
    }

    /**
     * Tests setHeader() with null value. NullPointerException should be caught.
     */
    public void testSetHeaderNullValue() {
        try {
            message.setHeader("key", null);
            fail("NullPointerException should have been thrown.");
        } catch (NullPointerException expected) {
            // Success.
        }
    }

    /**
     * Tests setHeader() with empty key. IllegalArgumentException should be caught.
     */
    public void testSetHeaderEmptyKey() {
        try {
            message.setHeader(" ", "key is empty");
            fail("IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException expected) {
            // Success.
        }
    }

    /**
     * Tests setHeader() with new header that does not present in the message.
     */
    public void testSetHeaderNotExist() {
        assertNull("The header should have not been set", message.getHeader("Key"));
        message.setHeader("Key", "Value");
        assertEquals("Returned value is not valid", "Value", message.getHeader("Key"));
    }

    /**
     * Tests setHeader() with existing header, the old header should be overwritten.
     */
    public void testSetHeaderExist() {
        initHeaders();
        assertEquals("The header should have been set", "value1", message.getHeader("key1"));
        message.setHeader("key1", "new value");
        assertEquals("Returned value is not valid", "new value", message.getHeader("key1"));
    }

    /**
     * Tests getHeader() with null key. NullPointerException should be caught.
     */
    public void testGetHeaderNull() {
        try {
            message.getHeader(null);
            fail("NullPointerException should have been thrown.");
        } catch (NullPointerException expected) {
            // Success.
        }
    }

    /**
     * Tests getHeader() with empty key. IllegalArgumentException should be caught.
     */
    public void testGetHeaderEmptyKey() {
        try {
            message.getHeader(" ");
            fail("IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException expected) {
            // Success.
        }
    }

    /**
     * Tests getHeader() with key specifying existing header.
     */
    public void testGetHeaderExist() {
        initHeaders();
        assertEquals("Returned value is not valid.", "value2", message.getHeader("key2"));
    }

    /**
     * Tests getHeader() with key specifying non-existing header, null should be returned.
     */
    public void testGetHeaderNotExist() {
        assertNull("The header should not exist", message.getHeader("no such key"));
    }

    /**
     * Tests removeHeader() with null key. NullPointerException should be caught.
     */
    public void testRemoveHeaderNull() {
        try {
            message.removeHeader(null);
            fail("NullPointerException should have been thrown.");
        } catch (NullPointerException expected) {
            // Success.
        }
    }

    /**
     * Tests removeHeader() key specifying existing header, the old header should be returned.
     */
    public void testRemoveHeaderExist() {
        initHeaders();
        assertEquals("Removed header is not valid", "value1", message.removeHeader("key1"));
        assertNull("The header is not removed.", message.getHeader("key1"));
    }

    /**
     * Tests removeHeader() key specifying non-existing header, this should not affect the headers and null should be
     * returned.
     */
    public void testRemoveHeaderNotExist() {
        assertNull("Returned value is not valid", message.removeHeader("No Such Header"));
        assertNull("The header should not exist", message.getHeader("No Such Header"));
    }

    /**
     * Returns suite containing the tests.
     *
     * @return suite to run the test
     */
    public static Test suite() {
        return new TestSuite(TCSEmailMessageTestV3Normal.class);
    }

}
