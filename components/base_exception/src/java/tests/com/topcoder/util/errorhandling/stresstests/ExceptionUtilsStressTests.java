/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling.stresstests;

import com.topcoder.util.errorhandling.ExceptionUtils;

import junit.framework.TestCase;

import java.util.ResourceBundle;


/**
 * Stress test for <code>ExceptionUtils</code>.
 *
 * @author kzhu
 * @version 2.0
 */
public class ExceptionUtilsStressTests extends TestCase {
    /** The time to do the stress test. */
    private static final int STRESS_TIME = 200;

    /** The bundle used in the unit test. */
    private ResourceBundle bundle = null;

    /**
     * Set up the unit test.
     */
    public void setUp() {
        bundle = ResourceBundle.getBundle(MockResourceBundle.class.getName());
    }

    /**
     * Stress test for <code>getMessage(ResourceBundle, String, String)</code>.
     */
    public void testGetMessageWithBundleKeyAndDefaultArg() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertEquals("The value should be return.", "xixi", ExceptionUtils.getMessage(bundle, "name", "default"));
        }

        System.out.println("Test getMessage(ResourceBundle, String, String) " + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>getMessage(ResourceBundle, String, String, String)</code>.
     */
    public void testGetMessageWithBundleAppModuleAndErrorArg() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            assertEquals("The value should be return.", "app-module-error: Item missing",
                ExceptionUtils.getMessage(bundle, "app", "module", "error"));
        }

        System.out.println("Test getMessage(ResourceBundle, String, String) " + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>checkNull(Object, ResourceBundle, String, String)</code>.
     */
    public void testCheckNull() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            try {
                ExceptionUtils.checkNull(null, bundle, "name", "default");
                fail("IllegalArgumentException should be thrown.");
            } catch (IllegalArgumentException iae) {
                // it's ok to be here.
            }
        }

        System.out.println("Test checkNull(Object, ResourceBundle, String, String) " + STRESS_TIME + " times costs: "
            + (System.currentTimeMillis() - start) + "ms.");
    }

    /**
     * Stress test for <code>checkNullOrEmpty(String, ResourceBundle, String, String)</code>.
     */
    public void testcheckNullOrEmpty() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < STRESS_TIME; i++) {
            try {
                ExceptionUtils.checkNullOrEmpty("      ", bundle, "name", "default");
                fail("IllegalArgumentException should be thrown.");
            } catch (IllegalArgumentException iae) {
                // it's ok to be here.
            }
        }

        System.out.println("Test checkNullOrEmpty(String, ResourceBundle, String, String) " + STRESS_TIME
            + " times costs: " + (System.currentTimeMillis() - start) + "ms.");
    }
}
