/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.failuretests;

import junit.framework.TestCase;

import com.topcoder.configuration.PropertyNotFoundException;
import com.topcoder.configuration.PropertyTypeMismatchException;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

/**
 * Failure tests for the DefaultConfigurationObject class of 1.1.
 *
 * @author gjw99
 * @version 1.1
 */
public class DefaultConfigurationObjectV11Tests extends TestCase {

    /** The test DefaultConfigurationObject instance. */
    private DefaultConfigurationObject instance = null;

    /**
     * Load test namespace.
     *
     * @throws ConfigManagerException to JUnit
     */
    public void setUp() {
        instance = new DefaultConfigurationObject("test");
    }

    /**
     * Remove test namespace.
     *
     * @throws ConfigManagerException to JUnit
     */
    public void tearDown() {
        instance = null;
    }

    /**
     * Failure tests for the method getIntegerProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getIntegerProperty1() throws Exception {
        try {
            instance.getIntegerProperty(null, true);
            fail("expected IAE");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Failure tests for the method getIntegerProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getIntegerProperty2() throws Exception {
        try {
            instance.getIntegerProperty(" ", true);
            fail("expected IAE");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Failure tests for the method getIntegerProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getIntegerProperty3() throws Exception {
        try {
            instance.getIntegerProperty("notfound", true);
            fail("expected PropertyNotFoundException");
        } catch (PropertyNotFoundException e) {
        }
    }

    /**
     * Failure tests for the method getIntegerProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getIntegerProperty4() throws Exception {
        try {
        	instance.setPropertyValue("test", "test");
            instance.getIntegerProperty("test", true);
            fail("expected PropertyTypeMismatchException");
        } catch (PropertyTypeMismatchException e) {
        }
    }

    /**
     * Failure tests for the method getLongProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getLongProperty1() throws Exception {
        try {
            instance.getLongProperty(null, true);
            fail("expected IAE");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Failure tests for the method getLongProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getLongProperty2() throws Exception {
        try {
            instance.getLongProperty(" ", true);
            fail("expected IAE");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Failure tests for the method getLongProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getLongProperty3() throws Exception {
        try {
            instance.getLongProperty("notfound", true);
            fail("expected PropertyNotFoundException");
        } catch (PropertyNotFoundException e) {
        }
    }

    /**
     * Failure tests for the method getLongProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getLongProperty4() throws Exception {
        try {
        	instance.setPropertyValue("test", "test");
            instance.getLongProperty("test", true);
            fail("expected PropertyTypeMismatchException");
        } catch (PropertyTypeMismatchException e) {
        }
    }

    /**
     * Failure tests for the method getDoubleProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getDoubleProperty1() throws Exception {
        try {
            instance.getDoubleProperty(null, true);
            fail("expected IAE");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Failure tests for the method getDoubleProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getDoubleProperty2() throws Exception {
        try {
            instance.getDoubleProperty(" ", true);
            fail("expected IAE");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Failure tests for the method getDoubleProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getDoubleProperty3() throws Exception {
        try {
            instance.getDoubleProperty("notfound", true);
            fail("expected PropertyNotFoundException");
        } catch (PropertyNotFoundException e) {
        }
    }

    /**
     * Failure tests for the method getDoubleProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getDoubleProperty4() throws Exception {
        try {
        	instance.setPropertyValue("test", "test");
            instance.getDoubleProperty("test", true);
            fail("expected PropertyTypeMismatchException");
        } catch (PropertyTypeMismatchException e) {
        }
    }

    /**
     * Failure tests for the method getDateProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getDateProperty1() throws Exception {
        try {
            instance.getDateProperty(null, "dd-mm-yyyy", true);
            fail("expected IAE");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Failure tests for the method getDateProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getDateProperty2() throws Exception {
        try {
            instance.getDateProperty(" ", "dd-mm-yyyy", true);
            fail("expected IAE");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Failure tests for the method getDateProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getDateProperty3() throws Exception {
        try {
            instance.getDateProperty("notfound", "dd-mm-yyyy", true);
            fail("expected PropertyNotFoundException");
        } catch (PropertyNotFoundException e) {
        }
    }

    /**
     * Failure tests for the method getDateProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getDateProperty4() throws Exception {
        try {
        	instance.setPropertyValue("test", "test");
            instance.getDateProperty("test", "dd-mm-yyyy", true);
            fail("expected PropertyTypeMismatchException");
        } catch (PropertyTypeMismatchException e) {
        }
    }

    /**
     * Failure tests for the method getDateProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getDateProperty5() throws Exception {
        try {
            instance.getDateProperty("test", null, true);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Failure tests for the method getDateProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getDateProperty6() throws Exception {
        try {
            instance.getDateProperty("test", "  ", true);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Failure tests for the method getClassProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getClassProperty1() throws Exception {
        try {
            instance.getClassProperty(null, true);
            fail("expected IAE");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Failure tests for the method getClassProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getClassProperty2() throws Exception {
        try {
            instance.getClassProperty(" ", true);
            fail("expected IAE");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Failure tests for the method getClassProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getClassProperty3() throws Exception {
        try {
            instance.getClassProperty("notfound", true);
            fail("expected PropertyNotFoundException");
        } catch (PropertyNotFoundException e) {
        }
    }

    /**
     * Failure tests for the method getClassProperty.
     *
     * @exception Exception propagate exceptions
     */
    public void test_getClassProperty4() throws Exception {
        try {
        	instance.setPropertyValue("test", "test");
            instance.getClassProperty("test", true);
            fail("expected PropertyTypeMismatchException");
        } catch (PropertyTypeMismatchException e) {
        }
    }
}
