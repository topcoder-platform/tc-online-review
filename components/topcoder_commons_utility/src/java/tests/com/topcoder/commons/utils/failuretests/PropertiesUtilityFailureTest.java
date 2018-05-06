/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils.failuretests;

import java.util.Properties;

import junit.framework.TestCase;

import com.topcoder.commons.utils.PropertiesUtility;

/**
 * <p>
 * Failure tests for PropertiesUtility.
 * </p>
 * 
 * @author Beijing2008
 * @version 1.0
 */
public class PropertiesUtilityFailureTest extends TestCase {

    /**
     * <p>
     * Setup the environment.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
    }

    /**
     * <p>
     * Clean up the environment.
     * </p>
     * 
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
    }
    /**
     * Tests for getStringProperty() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_getStringProperty() throws Exception {
        Properties properties = new Properties();
        try {
            PropertiesUtility.getStringProperty(properties, "name", true, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
    }
    /**
     * Tests for getStringsProperty() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_getStringsProperty() throws Exception {
        Properties properties = new Properties();
        try {
            PropertiesUtility.getStringsProperty(properties, "name", ",",true, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
    }
    /**
     * Tests for getIntegerProperty() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_getIntegerProperty() throws Exception {
        Properties properties = new Properties();
        try {
            PropertiesUtility.getIntegerProperty(properties, "key", true, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        properties.setProperty("key", "4444444444444444");
        try {
            PropertiesUtility.getIntegerProperty(properties, "key", true, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        properties.setProperty("key", "xxxx");
        try {
            PropertiesUtility.getLongProperty(properties, "key", true, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        properties.setProperty("key", "3.0");
        try {
            PropertiesUtility.getLongProperty(properties, "key", true, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
    }
    /**
     * Tests for getLongProperty() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_getLongProperty() throws Exception {
        Properties properties = new Properties();
        try {
            PropertiesUtility.getLongProperty(properties, "key", true, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        properties.setProperty("key", "xxxx");
        try {
            PropertiesUtility.getLongProperty(properties, "key", true, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        properties.setProperty("key", "3.0");
        try {
            PropertiesUtility.getLongProperty(properties, "key", true, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
    }
    /**
     * Tests for getDoubleProperty() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_getDoubleProperty() throws Exception {
        Properties properties = new Properties();
        try {
            PropertiesUtility.getDoubleProperty(properties, "key", true, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        properties.setProperty("key", "xxxx");
        try {
            PropertiesUtility.getDoubleProperty(properties, "key", true, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
    }
    

    /**
     * Tests for getDateProperty() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_getDateProperty() throws Exception {
        Properties properties = new Properties();
        try {
            PropertiesUtility.getDateProperty(properties, "key", "yyyy",true, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        properties.setProperty("key", "xxxx");
        try {
            PropertiesUtility.getDateProperty(properties, "key", "yyyy", true, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
    }

    /**
     * Tests for getClassProperty() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_getClassProperty() throws Exception {
        Properties properties = new Properties();
        try {
            PropertiesUtility.getClassProperty(properties, "key", true, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        properties.setProperty("key", "xxxx");
        try {
            PropertiesUtility.getClassProperty(properties, "key", true, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
    }
}
