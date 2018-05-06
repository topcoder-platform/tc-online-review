/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.accuracytests;

import com.topcoder.util.datavalidator.BundleInfo;

import junit.framework.TestCase;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Accuracy tests for the BundleInfo class.
 *
 * @author KLW
 * @version 1.1
 */
public class BundleInfoAccuracyTests extends TestCase {
    /**
     * <p>
     * BundleInfo case used to test.
     * </p>
     */
    private BundleInfo bundleInfo = null;

    /**
     * <p>
     * set up the test environment.
     * </p>
     */
    protected void setUp() {
        bundleInfo = new BundleInfo();
        bundleInfo.setBundle("accuracy.test", new Locale("en"));
        bundleInfo.setDefaultMessage("accuracy test");
        bundleInfo.setMessageKey("StartsWith_Bundle_Key");
    }

    /**
     * <p>
     * tear down the test environment.
     * </p>
     */
    protected void tearDown() {
        bundleInfo = null;
    }

    /**
     * <p>
     * test default constructor.
     * </p>
     *
     */
    public void testCtor() {
        assertNotNull(bundleInfo);
    }

    /**
     * <p>
     * test method  getBundleName().
     * </p>
     *
     */
    public void testGetBundleName() {
        assertEquals("accuracy.test", bundleInfo.getBundleName());
    }

    /**
     * <p>
     * test method getLocale().
     * </p>
     *
     */
    public void testGetLocale() {
        assertEquals(new Locale("en"), bundleInfo.getLocale());
    }

    /**
     * <p>
     * test method getDefaultMessage().
     * </p>
     *
     */
    public void testGetDefaultMessage() {
        assertEquals("accuracy test", bundleInfo.getDefaultMessage());
    }

    /**
     * <p>
     * test method getMessageKey().
     * </p>
     *
     */
    public void testGetMessageKey() {
        assertEquals("StartsWith_Bundle_Key", bundleInfo.getMessageKey());
    }

    /**
     * <p>
     * test method setMessageKey().
     * </p>
     *
     */
    public void testSetMessageKey() {
        bundleInfo.setMessageKey("StartsWith_Bundle_Key");
        assertEquals("StartsWith_Bundle_Key", bundleInfo.getMessageKey());
    }

    /**
     * <p>
     * test method SetDefaultMessage().
     * </p>
     *
     */
    public void testSetDefaultMessage() {
        bundleInfo.setDefaultMessage("default message");
        assertEquals("default message", bundleInfo.getDefaultMessage());
    }

    /**
     * <p>
     * test method setBundle(String,locale).
     * </p>
     *
     */
    public void testSetBundleLocale() {
        bundleInfo.setBundle("accuracy.test", new Locale("zh"));
        assertEquals(new Locale("zh"), bundleInfo.getLocale());
    }

    /**
     * <p>
     * test method setBundle().
     * </p>
     *
     */
    public void testSetBundle() {
        bundleInfo.setBundle("accuracy.test");
        assertEquals("accuracy.test", bundleInfo.getBundleName());
    }

    /**
     * <p>
     * test method getCurrentLocale().
     * </p>
     *
     */
    public void testGetCurrentLocale() {
        assertEquals(false, bundleInfo.getCurrentLocale());
    }

    /**
     * <p>
     * test method getResourceBundle().
     * </p>
     *
     */
    public void testGetResourceBundle() {
        bundleInfo.setBundle("accuracy.test", new Locale("en"));
        assertEquals(bundleInfo.getResourceBundle(),
            ResourceBundle.getBundle("accuracy.test", new Locale("en")));
    }
}
