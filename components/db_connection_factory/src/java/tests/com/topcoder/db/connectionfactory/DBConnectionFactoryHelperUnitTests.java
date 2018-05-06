/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import junit.framework.TestCase;

import java.io.Serializable;

/**
 * <p>
 * This is the tests for DBConnectionFactoryHelper class. All the public static helper methods are
 * tested.
 * </p>
 *
 * @author magicpig
 * @version 1.1
 * @since 1.1
 */
public class DBConnectionFactoryHelperUnitTests extends TestCase {
    /**
     * <p>
     * Tests checkNull with null paramValue. IllegalArgumentException is expected.
     * </p>
     */
    public void testCheckNullWithNullParamValue() {
        try {
            DBConnectionFactoryHelper.checkNull("test", null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * <p>
     * Tests checkNull will non-null paramValue. All work well.
     * </p>
     */
    public void testCheckNullWithNonNullParamValue() {
        DBConnectionFactoryHelper.checkNull("test", new Object());
    }

    /**
     * <p>
     * Tests checkNullOrEmpty with null paramValue. IllegalArgumentException is expected.
     * </p>
     */
    public void testCheckNullOrEmptyWithNullParamValue() {
        try {
            DBConnectionFactoryHelper.checkNullOrEmpty("test", null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * <p>
     * Tests checkNullOrEmpty with empty paramValue. IllegalArgumentException is expected.
     * </p>
     */
    public void testCheckNullOrEmptyWithEmptyParamValue() {
        try {
            DBConnectionFactoryHelper.checkNullOrEmpty("test", "");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * <p>
     * Tests checkNullOrEmpty with trimmed empty paramValue. IllegalArgumentException is expected.
     * </p>
     */
    public void testCheckNullOrEmptyWithTrimmedEmptyParamValue() {
        try {
            DBConnectionFactoryHelper.checkNullOrEmpty("test", "  ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * <p>
     * Tests checkNullOrEmpty with normal String paramValue. IllegalArgumentException is expected.
     * </p>
     */
    public void testCheckNullOrEmptyWithNormalParamValue() {
        DBConnectionFactoryHelper.checkNullOrEmpty("test", "normal");
    }

    /**
     * <p>
     * Tests getChildFromConfigurationObject with null configurationObject. IllegalArgumentException
     * is expected.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     */
    public void testGetChildFromConfigurationObjectWithNullParamValue() throws Exception {
        try {
            DBConnectionFactoryHelper.getChildFromConfigurationObject("test", null, "key");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * <p>
     * Tests getChildFromConfigurationObject when the ConfigurationObject instance for key is null.
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     */
    public void testGetChildFromConfigurationObjectWithNullValueForKey() throws Exception {
        try {
            DBConnectionFactoryHelper.getChildFromConfigurationObject("test", new DefaultConfigurationObject(
                "root"), "key");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * <p>
     * Tests getChildFromConfigurationObject with null key. IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     */
    public void testGetChildFromConfigurationObjectWithNullKey() throws Exception {
        try {
            DBConnectionFactoryHelper.getChildFromConfigurationObject("test", new DefaultConfigurationObject(
                "root"), null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * <p>
     * Tests getChildFromConfigurationObject with empty key. IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     */
    public void testGetChildFromConfigurationObjectWithEmptyKey() throws Exception {
        try {
            DBConnectionFactoryHelper.getChildFromConfigurationObject("test", new DefaultConfigurationObject(
                "root"), "");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * <p>
     * Tests getChildFromConfigurationObject with trimmed empty key. IllegalArgumentException is
     * expected.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     */
    public void testGetChildFromConfigurationObjectWithTrimmedEmptyKey() throws Exception {
        try {
            DBConnectionFactoryHelper.getChildFromConfigurationObject("test", new DefaultConfigurationObject(
                "root"), "  ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * <p>
     * Tests getChildFromConfigurationObject accuracy. The returned value is checked.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     */
    public void testGetChildFromConfigurationObjectAccuracy() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        ConfigurationObject child = new DefaultConfigurationObject("child");
        configurationObject.addChild(child);
        assertTrue("Failed to implement getChildFromConfigurationObject.", child == DBConnectionFactoryHelper
            .getChildFromConfigurationObject("test", configurationObject, "child"));
    }

    /**
     * <p>
     * Tests getNullOrStringValueFromConfigurationObject with null configurationObject.
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     */
    public void testGetNullOrStringValueFromConfigurationObjectWithNullParamValue() throws Exception {
        try {
            DBConnectionFactoryHelper.getNullOrStringValueFromConfigurationObject(null, "key");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * <p>
     * Tests getNullOrStringValueFromConfigurationObject with null key. IllegalArgumentException is
     * expected.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     */
    public void testGetNullOrStringValueFromConfigurationObjectWithNullKey() throws Exception {
        try {
            DBConnectionFactoryHelper.getNullOrStringValueFromConfigurationObject(new DefaultConfigurationObject(
                "root"), null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * <p>
     * Tests getNullOrStringValueFromConfigurationObject with empty key. IllegalArgumentException is
     * expected.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     */
    public void testGetNullOrStringValueFromConfigurationObjectWithEmptyKey() throws Exception {
        try {
            DBConnectionFactoryHelper.getNullOrStringValueFromConfigurationObject(new DefaultConfigurationObject(
                "root"), "");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * <p>
     * Tests getNullOrStringValueFromConfigurationObject with trimmed empty key.
     * IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     */
    public void testGetNullOrStringValueFromConfigurationObjectWithTrimmedEmptyKey() throws Exception {
        try {
            DBConnectionFactoryHelper.getNullOrStringValueFromConfigurationObject(new DefaultConfigurationObject(
                "root"), "  ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * <p>
     * Tests getNullOrStringValueFromConfigurationObject when the value for key is non-null
     * non-string object. IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     */
    public void testGetNullOrStringValueFromConfigurationObjectWithSerializableRetrieved() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        configurationObject.setPropertyValue("key", new Serializable() {
        });

        try {
            DBConnectionFactoryHelper.getNullOrStringValueFromConfigurationObject(configurationObject, "key");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * <p>
     * Tests getNullOrStringValueFromConfigurationObject accuracy. The returned String value is
     * checked.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     */
    public void testGetNullOrStringValueFromConfigurationObjectAccuracy1() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        configurationObject.setPropertyValue("key", "string");
        assertEquals("Failed to implement getNullOrStringValueFromConfigurationObject.", "string",
            DBConnectionFactoryHelper.getNullOrStringValueFromConfigurationObject(configurationObject, "key"));
    }

    /**
     * <p>
     * Tests getNullOrStringValueFromConfigurationObject accuracy. The returned null value is
     * checked.
     * </p>
     *
     * @throws Exception
     *             when error occurs
     */
    public void testGetNullOrStringValueFromConfigurationObjectAccuracy2() throws Exception {
        ConfigurationObject configurationObject = new DefaultConfigurationObject("root");
        assertNull("Failed to implement getNullOrStringValueFromConfigurationObject.", DBConnectionFactoryHelper
            .getNullOrStringValueFromConfigurationObject(configurationObject, "key"));
    }
}
