/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;

import java.io.Serializable;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * <p>
 * Test the functionality of class <code>BundleInfo</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public class BundleInfoTestCases extends TestCase {
    /**
     * <p>
     * An instance of <code>BundleInfo</code> for testing.<br>
     * </p>
     */
    private BundleInfo bundleInfo = null;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        bundleInfo = new BundleInfo();
    }

    /**
     * <p>
     * Tear down the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        bundleInfo = null;
        super.tearDown();
    }

    /**
     * <p>
     * Accuracy test case for method 'BundleInfo()'.
     * </p>
     */
    public void testBundleInfo() {
        assertTrue("Test accuracy for method 'BundleInfo()' failed.", bundleInfo instanceof Serializable);
    }

    /**
     * <p>
     * Accuracy test case for method 'getLocale()'.<br>The value of field 'locale' should be return properly.
     * </p>
     */
    public void testGetLocale_Accuracy() {
        Locale locale = Locale.CANADA;
        TestHelper.setPrivateField(BundleInfo.class, bundleInfo, "bundleLocale", locale);

        Locale value = bundleInfo.getLocale();
        assertEquals("Test accuracy for method getLocale() failed, the value should be return properly.",
            locale, value);
    }

    /**
     * <p>
     * Accuracy test case for method 'getBundleName()'.<br>The value of field 'bundleName' should be return properly.
     * </p>
     */
    public void testGetBundleName_Accuracy() {
        String bundleName = new String("bundleName");
        TestHelper.setPrivateField(BundleInfo.class, bundleInfo, "bundleName", bundleName);

        String value = bundleInfo.getBundleName();
        assertEquals("Test accuracy for method getBundleName() failed, the value should be return properly.",
            bundleName, value);
    }

    /**
     * <p>
     * Accuracy test case for method 'getDefaultMessage()'.<br>
     * The value of field 'defaultMessage' should be return properly.
     * </p>
     */
    public void testGetDefaultMessage_Accuracy() {
        String defaultMessage = new String("defaultMessage");
        TestHelper.setPrivateField(BundleInfo.class, bundleInfo, "defaultMessage", defaultMessage);

        String value = bundleInfo.getDefaultMessage();
        assertEquals("Test accuracy for method getDefaultMessage() failed, the value should be return properly.",
            defaultMessage, value);
    }

    /**
     * <p>
     * Accuracy test case for method 'getMessageKey()'.<br>The value of field 'messageKey' should be return properly.
     * </p>
     */
    public void testGetMessageKey_Accuracy() {
        String messageKey = new String("messageKey");
        TestHelper.setPrivateField(BundleInfo.class, bundleInfo, "messageKey", messageKey);

        String value = bundleInfo.getMessageKey();
        assertEquals("Test accuracy for method getMessageKey() failed, the value should be return properly.",
            messageKey, value);
    }

    /**
     * <p>
     * Failure test case for method 'setMessageKey()'.<br><code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testSetMessageKey_String_Null1() {
        try {
            bundleInfo.setMessageKey(null);
            fail("Test failure for setMessageKey() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'setMessageKey()'.<br><code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testSetMessageKey_String_Empty1() {
        try {
            bundleInfo.setMessageKey("     ");
            fail("Test failure for method setMessageKey() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'setMessageKey()'.<br>The field 'messageKey' should be set properly.
     * </p>
     */
    public void testSetMessageKey_String_Accuracy() {
        String messageKey = new String("messageKey");
        bundleInfo.setMessageKey(messageKey);

        String value = (String) TestHelper.getPrivateField(BundleInfo.class, bundleInfo, "messageKey");
        assertEquals("Test accuracy for method setMessageKey() failed, the value should be set properly.", messageKey,
            value);
    }

    /**
     * <p>
     * Failure test case for method 'setDefaultMessage()'.<br><code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testSetDefaultMessage_String_Null1() {
        try {
            bundleInfo.setDefaultMessage(null);
            fail("Test failure for setDefaultMessage() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'setDefaultMessage()'.<br><code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testSetDefaultMessage_String_Empty1() {
        try {
            bundleInfo.setDefaultMessage("    ");

            fail("Test failure for method setDefaultMessage() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'setDefaultMessage()'.<br>The field 'defaultMessage' should be set properly.
     * </p>
     */
    public void testSetDefaultMessage_String_Accuracy() {
        String defaultMessage = new String("defaultMessage");
        bundleInfo.setDefaultMessage(defaultMessage);

        String value = (String) TestHelper.getPrivateField(BundleInfo.class, bundleInfo, "defaultMessage");
        assertEquals("Test accuracy for method setDefaultMessage() failed, the value should be set properly.",
            defaultMessage, value);
    }

    /**
     * <p>
     * Failure test case for method 'setBundle()'.<br><code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testSetBundle_StringLocale_Null1() {
        try {
            bundleInfo.setBundle(null, Locale.CANADA);

            fail("Test failure for setBundle() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'setBundle()'.<br><code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testSetBundle_StringLocale_Null2() {
        try {
            bundleInfo.setBundle("bundle", null);

            fail("Test failure for setBundle() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'setBundle()'.<br><code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testSetBundle_StringLocale_Empty1() {
        try {
            bundleInfo.setBundle("  ", Locale.CANADA);
            fail("Test failure for method setBundle() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'setBundle()'.<br><code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testSetBundle_String_Null1() {
        try {
            bundleInfo.setBundle(null);

            fail("Test failure for setBundle() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'setBundle()'.<br><code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testSetBundle_String_Empty1() {
        try {
            bundleInfo.setBundle("   ");

            fail("Test failure for method setBundle() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'getCurrentLocale()'.<br>
     * The value of field 'currentLocale' should be return properly.
     * </p>
     */
    public void testGetCurrentLocale_Accuracy() {
        boolean currentLocale = true;
        TestHelper.setPrivateField(BundleInfo.class, bundleInfo, "currentLocale", Boolean.TRUE);

        boolean value = bundleInfo.getCurrentLocale();
        assertEquals("Test accuracy for method getCurrentLocale() failed, the value should be return properly.",
            currentLocale, value);
    }

    /**
     * <p>
     * Accuracy test case for method 'getResourceBundle()'.<br>
     * The value of field 'resourceBundle' should be return properly.
     * </p>
     */
    public void testGetResourceBundle_Accuracy() {
        ResourceBundle resourceBundle = new MockResourcesBundle();
        TestHelper.setPrivateField(BundleInfo.class, bundleInfo, "resourceBundle", resourceBundle);

        ResourceBundle value = bundleInfo.getResourceBundle();
        assertEquals("Test accuracy for method getResourceBundle() failed, the value should be return properly.",
            resourceBundle, value);
    }
}
