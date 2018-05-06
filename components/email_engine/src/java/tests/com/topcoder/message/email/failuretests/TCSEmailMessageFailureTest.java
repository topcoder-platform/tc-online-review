/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.message.email.failuretests;

import com.topcoder.message.email.AddressException;
import com.topcoder.message.email.PriorityLevel;
import com.topcoder.message.email.TCSEmailMessage;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.ByteArrayInputStream;


/**
 * Failure tests for the new version of TCSEmailMessage.
 *
 * @author mgmg
 * @version 3.0
 */
public class TCSEmailMessageFailureTest extends TestCase {
    /**
     * The namespace.
     */
    private final static String NAMESPACE = "com.topcoder.message.email.TCSEmailMessage";

    /**
     * Config file.
     */
    private final static String CONFIG_FILE = "failuretests/failuretests.xml";

    /**
     * The test instance.
     */
    private TCSEmailMessage instance = null;

    /**
     * Aggregate all the tests.
     *
     * @return
     */
    public static Test suite() {
        return new TestSuite(TCSEmailMessageFailureTest.class);
    }

    /**
     * Create the test instance.
     */
    public void setUp() throws Exception {
        FailureTests.cleanConfiguration();
        instance = new TCSEmailMessage();
        FailureTests.loadConfig(NAMESPACE, CONFIG_FILE);
    }

    /**
     * Clean the test environment.
     */
    public void tearDown() throws Exception {
        FailureTests.cleanConfiguration();
    }

    /**
     * Failure test for setFromAddress.
     * NullPointerException should be thrown because of the null parameter.
     *
     */
    public void testSetFromAddress1_NullParam() throws Exception {
        try {
            instance.setFromAddress(null);
            fail("NullPointerException should be thrown because of the null parameter.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Failure test for setFromAddress.
     * AddressException should be thrown because of the invalid parameter.
     *
     */
    public void testSetFromAddress1_InvalidAddress() throws Exception {
        try {
            instance.setFromAddress("abcd$&*()<?#^&$");
            fail("AddressException should be thrown because of the invalid parameter.");
        } catch (AddressException e) {
            // success.
        }
    }

    /**
     * Failure test for setFromAddress.
     * NullPointerException should be thrown because of the null parameter.
     */
    public void testSetFromAddress2_NullParam1() throws Exception {
        try {
            instance.setFromAddress(null, "mgmg");
            fail("NullPointerException should be thrown because of the null parameter.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Failure test for setFromAddress.
     * NullPointerException should be thrown because of the null parameter.
     */
    public void testSetFromAddress2_NullParam2() throws Exception {
        try {
            instance.setFromAddress("mgmg@topcoder.com", null);
            fail("NullPointerException should be thrown because of the null parameter.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Failure test for setFromAddress.
     * AddressException should be thrown because of the invalid parameter.
     *
     */
    public void testSetFromAddress2_InvalidAddress() throws Exception {
        try {
            instance.setFromAddress("abcd$&*()<?#^&$", "mgmg");
            fail("AddressException should be thrown because of the invalid parameter.");
        } catch (AddressException e) {
            // success.
        }
    }

    /**
     * Failure test for setToAddress.
     * NullPointerException should be thrown because of the null parameter.
     */
    public void testSetToAddress1_NullParam() throws Exception {
        try {
            instance.setToAddress(null, TCSEmailMessage.TO);
            fail("NullPointerException should be thrown because of the null parameter.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Failure test for setToAddress.
     * IllegalArgumentException should be thrown because of the invalid type.
     */
    public void testSetToAddress1_InvalidType() throws Exception {
        try {
            instance.setToAddress("mgmg@topcoder.com", 5);
            fail("IllegalArgumentException should be thrown because of the invalid type.");
        } catch (IllegalArgumentException e) {
            // success.
        }
    }

    /**
     * Failure test for setToAddress.
     * AddressException should be thrown because of the invalid parameter.
     *
     */
    public void testSetToAddress1_InvalidAddress() throws Exception {
        try {
            instance.setToAddress("abcd$&*()<?#^&$", TCSEmailMessage.BCC);
            fail("AddressException should be thrown because of the invalid parameter.");
        } catch (AddressException e) {
            // success.
        }
    }

    /**
     * Failure test for setToAddress.
     * NullPointerException should be thrown because of the null parameter.
     */
    public void testSetToAddress2_NullParam1() throws Exception {
        try {
            instance.setToAddress(null, "mgmg", TCSEmailMessage.TO);
            fail("NullPointerException should be thrown because of the null parameter.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Failure test for setToAddress.
     * NullPointerException should be thrown because of the null parameter.
     */
    public void testSetToAddress2_NullParam2() throws Exception {
        try {
            instance.setToAddress("mgmg@topcoder.com", null, TCSEmailMessage.TO);
            fail("NullPointerException should be thrown because of the null parameter.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Failure test for setToAddress.
     * IllegalArgumentException should be thrown because of the invalid type.
     */
    public void testSetToAddress2_InvalidType() throws Exception {
        try {
            instance.setToAddress("mgmg@topcoder.com", "mgmg", 5);
            fail("IllegalArgumentException should be thrown because of the invalid type.");
        } catch (IllegalArgumentException e) {
            // success.
        }
    }

    /**
     * Failure test for setToAddress.
     * AddressException should be thrown because of the invalid parameter.
     *
     */
    public void testSetToAddress2_InvalidAddress() throws Exception {
        try {
            instance.setToAddress("abcd$&*()<?#^&$", "mgmg", TCSEmailMessage.BCC);
            fail("AddressException should be thrown because of the invalid parameter.");
        } catch (AddressException e) {
            // success.
        }
    }

    /**
     * Failure test for addToAddress.
     * NullPointerException should be thrown because of the null parameter.
     */
    public void testAddToAddress1_NullParam() throws Exception {
        try {
            instance.addToAddress(null, TCSEmailMessage.TO);
            fail("NullPointerException should be thrown because of the null parameter.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Failure test for addToAddress.
     * IllegalArgumentException should be thrown because of the invalid type.
     */
    public void testAddToAddress1_InvalidType() throws Exception {
        try {
            instance.addToAddress("mgmg@topcoder.com", 5);
            fail("IllegalArgumentException should be thrown because of the invalid type.");
        } catch (IllegalArgumentException e) {
            // success.
        }
    }

    /**
     * Failure test for addToAddress.
     * AddressException should be thrown because of the invalid parameter.
     *
     */
    public void testAddToAddress1_InvalidAddress() throws Exception {
        try {
            instance.addToAddress("abcd$&*()<?#^&$", TCSEmailMessage.BCC);
            fail("AddressException should be thrown because of the invalid parameter.");
        } catch (AddressException e) {
            // success.
        }
    }

    /**
     * Failure test for addToAddress.
     * NullPointerException should be thrown because of the null parameter.
     */
    public void testAddToAddress2_NullParam1() throws Exception {
        try {
            instance.addToAddress(null, "mgmg", TCSEmailMessage.TO);
            fail("NullPointerException should be thrown because of the null parameter.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Failure test for addToAddress.
     * NullPointerException should be thrown because of the null parameter.
     */
    public void testAddtToAddress2_NullParam2() throws Exception {
        try {
            instance.addToAddress("mgmg@topcoder.com", null, TCSEmailMessage.TO);
            fail("NullPointerException should be thrown because of the null parameter.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Failure test for addToAddress.
     * IllegalArgumentException should be thrown because of the invalid type.
     */
    public void testAddToAddress2_InvalidType() throws Exception {
        try {
            instance.addToAddress("mgmg@topcoder.com", "mgmg", 5);
            fail("IllegalArgumentException should be thrown because of the invalid type.");
        } catch (IllegalArgumentException e) {
            // success.
        }
    }

    /**
     * Failure test for addToAddress.
     * AddressException should be thrown because of the invalid parameter.
     *
     */
    public void testAddToAddress2_InvalidAddress() throws Exception {
        try {
            instance.addToAddress("abcd$&*()<?#^&$", "mgmg", TCSEmailMessage.BCC);
            fail("AddressException should be thrown because of the invalid parameter.");
        } catch (AddressException e) {
            // success.
        }
    }

    /**
     * Failure test for getToAddress.
     * IllegalArgumentException should be thrown because of the invalid type.
     */
    public void testGetToAddress_InvalidType() throws Exception {
        try {
            instance.getToAddress(-5);
            fail("IllegalArgumentException should be thrown because of the invalid type.");
        } catch (IllegalArgumentException e) {
            // success.
        }
    }

    /**
     * Failure test for addAttachment.
     * NullPointerException should be thrown because of the null parameter.
     */
    public void testAddAttachment_NullParam1() throws Exception {
        try {
            instance.addAttachment(null, "test");
            fail("NullPointerException should be thrown because of the null parameter.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Failure test for addAttachment.
     * NullPointerException should be thrown because of the null parameter.
     */
    public void testAddAttachment_NullParam2() throws Exception {
        try {
            instance.addAttachment(new ByteArrayInputStream(("test").getBytes()), null);
            fail("NullPointerException should be thrown because of the null parameter.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Failure test for setHeader.
     * NullPointerException should be thrown because of the null parameter.
     */
    public void testSetHeader_NullParam1() {
        try {
            instance.setHeader(null, "value");
            fail("NullPointerException should be thrown because of the null parameter.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Failure test for setHeader.
     * NullPointerException should be thrown because of the null parameter.
     */
    public void testSetHeader_NullParam2() {
        try {
            instance.setHeader("key", null);
            fail("NullPointerException should be thrown because of the null parameter.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Failure test for setHeader.
     * IllegalArgumentException should be thrown because of the empty string.
     */
    public void testSetHeader_EmptyKey() {
        try {
            instance.setHeader(" ", "value");
            fail("IllegalArgumentException should be thrown because of the empty string.");
        } catch (IllegalArgumentException e) {
            // success.
        }
    }

    /**
     * Failure test for getHeader.
     * NullPointerException should be thrown because of the null parameter.
     */
    public void testGetHeader_NullParam() {
        try {
            instance.getHeader(null);
            fail("NullPointerException should be thrown because of the null parameter.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Failure test for getHeader.
     * IllegalArgumentException should be thrown because of the empty string.
     */
    public void testGetHeader_EmptyKey() {
        try {
            instance.getHeader(" ");
            fail("IllegalArgumentException should be thrown because of the empty string.");
        } catch (IllegalArgumentException e) {
            // success.
        }
    }

    /**
     * Failure test for removeHeader.
     * NullPointerException should be thrown because of the null parameter.
     */
    public void testRemoveHeader_NullParam() {
        try {
            instance.removeHeader(null);
            fail("NullPointerException should be thrown because of the null parameter.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Failure test for removeHeader.
     * IllegalArgumentException should be thrown because of the empty string.
     */
    public void testRemoveHeader_EmptyKey() {
        try {
            instance.removeHeader(" ");
            fail("IllegalArgumentException should be thrown because of the empty string.");
        } catch (IllegalArgumentException e) {
            // success.
        }
    }

    /**
     * Failure test for setPriority.
     * NullPointerException should be thrown because of the null parameter.
     */
    public void testSetPriority_NullParam() throws Exception {
        try {
            instance.setPriority(null);
            fail("NullPointerException should be thrown because of the null parameter.");
        } catch (NullPointerException e) {
            // success.
        }
    }

    /**
     * Failure test for setPriority.
     * The config file is invalid so IllegalArgumentException should be thrown.
     */
    public void testSetPriority_InvalidConfig1() throws Exception {
        try {
            instance.setPriority(PriorityLevel.NORMAL);
            fail("The config file is invalid so IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // success.
        }
    }

    /**
     * Failure test for setPriority.
     * The config file is invalid so IllegalArgumentException should be thrown.
     */
    public void testSetPriority_InvalidConfig2() throws Exception {
        try {
            instance.setPriority(PriorityLevel.HIGH);
            fail("The config file is invalid so IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // success.
        }
    }

    /**
     * Failure test for setPriority.
     * The config file is invalid so IllegalArgumentException should be thrown.
     */
    public void testSetPriority_InvalidConfig3() throws Exception {
        try {
            instance.setPriority(PriorityLevel.HIGHEST);
            fail("The config file is invalid so IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // success.
        }
    }
}
