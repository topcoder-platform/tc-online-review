/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

import java.util.ResourceBundle;

import junit.framework.TestCase;

/**
 * Unit test for the <code>ExceptionUtils</code> class.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class ExceptionUtilsTests extends TestCase {

    /**
     * Represents the default <code>ResourceBundle</code> instance used to
     * test against.
     */
    private ResourceBundle bundle;

    /**
     * Set up the test environment.
     */
    protected void setUp() {
        bundle = ResourceBundle.getBundle("com.topcoder.util.errorhandling.MyResourceBundle");
    }

    /**
     * Test the protected constructor <code>ExceptionUtils()</code>.
     *
     */
    public void testCtor() {
        assertNotNull("ExceptionUtils instance created incorrect.", new ExceptionUtils());
    }

    /**
     * Accuracy test for the method
     * <code>getMessage(ResourceBundle, String, String)</code> with null
     * bundle/key. The value of defaultMessage should be returned.
     */
    public void testGetMessage_NullBundleOrKeyAccuracy() {
        // bundle is null
        assertEquals("test should be returned", "test", ExceptionUtils.getMessage(null, "key", "test"));
        // bundle is null and defaultMessage is null, null should be returned
        assertNull("null should be returned", ExceptionUtils.getMessage(null, "key", null));
        // bundle is null and defaultMessage is an empty string, an empty string
        // should be returned
        assertEquals("An empty string should be returned", "", ExceptionUtils.getMessage(null, "key", ""));

        // key is null
        assertEquals("test should be returned", "test", ExceptionUtils.getMessage(bundle, null, "test"));
        // key is null and defaultMessage is null, null should be returned
        assertNull("null should be returned", ExceptionUtils.getMessage(bundle, null, null));
        // key is null and defaultMessage is an empty string, an empty string
        // should be returned
        assertEquals("An empty string should be returned", "", ExceptionUtils.getMessage(bundle, null, ""));
    }

    /**
     * Accuracy test for the method
     * <code>getMessage(ResourceBundle, String, String)</code> with non-null
     * bundle and key. Correct value of message should be returned.
     */
    public void testGetMessage() {
        // key is "key", "value" should be returned
        assertEquals("value should be returned", "value", ExceptionUtils.getMessage(bundle, "key", "default"));
        // key is "doubleKey", so default message should be returned
        assertEquals("default should be returned", "default", ExceptionUtils.
                                                    getMessage(bundle, "doubleKey", "default"));
        // key is "missing", so default message should be returned
        assertEquals("default should be returned", "default", ExceptionUtils.getMessage(bundle, "missing", "default"));
        // key is "emptyKey", so an empty string should be returned
        assertEquals("An empty string should be returned", "", ExceptionUtils.
                                                    getMessage(bundle, "emptyKey", "default"));
        // key is "trimemptyKey", so a string containing two blank spaces should
        // be returned
        assertEquals("An string containing two blank spaces should be returned", "  ", ExceptionUtils.getMessage(
                bundle, "trimemptyKey", "default"));
        // key is an empty string which is not an existing key, so "default"
        // should be
        // returned
        assertEquals("default should be returned", "default", ExceptionUtils.getMessage(bundle, "", "default"));
    }

    /**
     * Accuracy test for the method
     * <code>getMessage(ResourceBundle,String,String,String)</code> with null
     * bundle. Correct value should be returned.
     */
    public void testGetMessage1_NullBundleAccuracy() {
        // appCode = "app", moduleCode = "module", errorCode = "error",
        // "app-module-error" should be returned
        assertEquals("Return value should be 'app-module-error'", "app-module-error", ExceptionUtils.getMessage(null,
                "app", "module", "error"));
        // appCode = null, "module-error" should be returned
        assertEquals("Return value should be 'module-error'", "module-error", ExceptionUtils.getMessage(null, null,
                "module", "error"));
        // moduleCode = null, "app-error" should be returned
        assertEquals("Return value should be 'app-error'", "app-error", ExceptionUtils.getMessage(null, "app", null,
                "error"));
        // errorCode = null, "app-module" should be returned
        assertEquals("Return value should be 'app-module'", "app-module", ExceptionUtils.getMessage(null, "app",
                "module", null));
        // appCode = null, moduleCode = null, "error" should be returned
        assertEquals("Return value should be 'error'", "error", ExceptionUtils.getMessage(null, null, null, "error"));
        // appCode = null, errorCode = null, "module" should be returned
        assertEquals("Return value should be 'module'", "module",
                                ExceptionUtils.getMessage(null, null, "module", null));
        // moduleCode = null, errorCode = null, "app" should be returned
        assertEquals("Return value should be 'app'", "app", ExceptionUtils.getMessage(null, "app", null, null));
        // all null, "" should be returned
        assertEquals("Return value should be an empty string", "", ExceptionUtils.getMessage(null, null, null, null));
        // all codes containing only blank spaces, " - " should be returned
        assertEquals("Return value should be an empty string", "  -    ", ExceptionUtils.getMessage(null, "", "  ",
                "    "));
    }

    /**
     * Accuracy test for the method
     * <code>getMessage(ResourceBundle,String,String,String)</code> with the
     * default bundle. Correct value should be returned.
     */
    public void testGetMessage1() {
        // key is "", an empty string should be returned
        assertEquals("Return value should be an empty string", "", ExceptionUtils.getMessage(bundle, null, null, null));
        // key is "sameKey", "sameKey" should be returned
        assertEquals("Return value should be 'sameKey", "sameKey", ExceptionUtils.getMessage(bundle, "sameKey", null,
                null));
        // key is "app-module-error", "app-module-error: value1" should be
        // returned
        assertEquals("Return value should be 'app-module-error: value1", "app-module-error: value1", ExceptionUtils
                .getMessage(bundle, "app", "module", "error"));
        // key is "invalid", "invalid" should be returned
        assertEquals("Return value should be 'invalid'", "invalid", ExceptionUtils.getMessage(bundle, "invalid", null,
                null));
        // key is "emptyKey", "emptyKey" should be returned
        assertEquals("Return value should be 'emptyKey'", "emptyKey", ExceptionUtils.getMessage(bundle, "emptyKey",
                null, null));
        // key is "trimemptyKey", "trimemptyKey: " should be returned
        assertEquals("Return value should be 'trimemptyKey:   '", "trimemptyKey:   ", ExceptionUtils.getMessage(bundle,
                "trimemptyKey", null, null));
    }

    /**
     * Accuracy test for the method
     * <code>getMessage(ResourceBundle,String,String,String)</code> with the
     * empty bundle. Correct value should be returned.
     */
    public void testGetMessage1_EmptyKeyBundle() {
        ResourceBundle emptyBundle = ResourceBundle.getBundle("com.topcoder.util.errorhandling.EmptyKeyResourceBundle");
        // key is "" and empty bundle is used, so "value" should be returned
        assertEquals("Return value should be 'value'", "value", ExceptionUtils
                .getMessage(emptyBundle, null, null, null));
    }

    /**
     * Accuracy test for the method
     * <code>getMessage(ResourceBundle,String,String,String)</code> with the
     * null value bundle. Correct value should be returned.
     */
    public void testGetMessage1_NullValueBundle() {
        // key is "nullKey" and null bundle is used
        ResourceBundle nullBundle = ResourceBundle.getBundle("com.topcoder.util.errorhandling.NullValueResourceBundle");
        assertEquals("Return value should be 'nullKey'", "nullKey", ExceptionUtils.getMessage(nullBundle, "nullKey",
                null, null));
        assertEquals("Return value should be 'value'", "value", ExceptionUtils.getMessage(nullBundle, "value", null,
                null));
    }

    /**
     * Accuracy test for the method
     * <code>checkNull(Object,ResourceBundle,String,String)</code>. No
     * Exception should be thrown.
     */
    public void testCheckNull() {
        ExceptionUtils.checkNull(new Object(), null, null, "test");
    }

    /**
     * Failure test for the method
     * <code>checkNull(Object,ResourceBundle,String,String)</code> with null
     * Object. <code>IllegalArgumentException</code> should be thrown.
     */
    public void testCheckNull_NullObject() {
        try {
            ExceptionUtils.checkNull(null, null, null, "test");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * Accuracy test for the method
     * <code>checkNullOrEmpty(String,ResourceBundle,String,String)</code>. No
     * Exception should be thrown.
     */
    public void testCheckNullOrEmpty() {
        ExceptionUtils.checkNullOrEmpty("test", null, null, "test");
    }

    /**
     * Failure test for the method
     * <code>checkNullOrEmpty(String,ResourceBundle,String,String)</code> with
     * null String. <code>IllegalArgumentException</code> should be thrown.
     */
    public void testCheckNullOrEmpty_NullString() {
        try {
            ExceptionUtils.checkNullOrEmpty(null, null, null, "test");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

    /**
     * Failure test for the method
     * <code>checkNullOrEmpty(String,ResourceBundle,String,String)</code> with
     * empty String. <code>IllegalArgumentException</code> should be thrown.
     */
    public void testCheckNullOrEmpty_EmptyString() {
        try {
            ExceptionUtils.checkNullOrEmpty("  ", null, null, "test");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException iae) {
            // good
        }
    }

}
