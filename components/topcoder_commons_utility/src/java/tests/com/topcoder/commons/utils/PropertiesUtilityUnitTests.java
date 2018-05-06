/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * Unit tests for {@link PropertiesUtility} class.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
public class PropertiesUtilityUnitTests {
    /**
     * <p>
     * Represents the properties.
     * </p>
     */
    private Properties properties;

    /**
     * <p>
     * Represents the key.
     * </p>
     */
    private String key = "userId";

    /**
     * <p>
     * Represents the flag indicates whether the property is required.
     * </p>
     */
    private boolean required = true;

    /**
     * <p>
     * Represents the exception class.
     * </p>
     */
    private Class<ConfigurationException> exceptionClass;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(PropertiesUtilityUnitTests.class);
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Before
    public void setUp() throws Exception {
        properties = TestsHelper.loadProperties(new File(TestsHelper.CONFIG_FILE));
        exceptionClass = ConfigurationException.class;
    }

    /**
     * <p>
     * Accuracy test for the method <code>getStringProperty(Properties properties, String key, boolean required,
     * Class&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getStringProperty_1() {
        String res = PropertiesUtility.getStringProperty(properties, key, required, exceptionClass);

        assertEquals("'getStringProperty' should be correct.", "101", res);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getStringProperty(Properties properties, String key, boolean required,
     * Class&lt;T&gt; exceptionClass)</code> with optional property does not exist.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getStringProperty_2() {
        key = "not_exist";
        required = false;
        String res = PropertiesUtility.getStringProperty(properties, key, required, exceptionClass);

        assertNull("'getStringProperty' should be correct.", res);
    }

    /**
     * <p>
     * Failure test for the method <code>getStringProperty(Properties properties, String key, boolean required,
     * Class&lt;T&gt; exceptionClass)</code> with required property is missing.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_getStringProperty_propertyMissing() {
        key = "not_exist";
        PropertiesUtility.getStringProperty(properties, key, required, exceptionClass);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getStringsProperty(Properties properties, String key, String delimiter,
     * boolean required, Class&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getStringsProperty_1() {
        String[] res = PropertiesUtility.getStringsProperty(properties, key, ";", required, exceptionClass);

        assertEquals("'getStringsProperty' should be correct.", 1, res.length);
        assertEquals("'getStringsProperty' should be correct.", "101", res[0]);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getStringsProperty(Properties properties, String key, String delimiter,
     * boolean required, Class&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getStringsProperty_2() {
        key = "numbers";
        String[] res = PropertiesUtility.getStringsProperty(properties, key, ";", required, exceptionClass);

        assertEquals("'getStringsProperty' should be correct.", 4, res.length);
        assertEquals("'getStringsProperty' should be correct.", "1", res[0]);
        assertEquals("'getStringsProperty' should be correct.", "2", res[1]);
        assertEquals("'getStringsProperty' should be correct.", "3", res[2]);
        assertEquals("'getStringsProperty' should be correct.", "", res[3]);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getStringsProperty(Properties properties, String key, String delimiter,
     * boolean required, Class&lt;T&gt; exceptionClass)</code> with optional property does not exist.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getStringsProperty_3() {
        key = "not_exist";
        required = false;
        String[] res = PropertiesUtility.getStringsProperty(properties, key, ";", required, exceptionClass);

        assertNull("'getStringsProperty' should be correct.", res);
    }

    /**
     * <p>
     * Failure test for the method <code>getStringsProperty(Properties properties, String key, String delimiter,
     * boolean required, Class&lt;T&gt; exceptionClass)</code> with required property is missing.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_getStringsProperty_propertyMissing() {
        key = "not_exist";
        PropertiesUtility.getStringsProperty(properties, key, ";", required, exceptionClass);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getIntegerProperty(Properties properties, String key, boolean required,
     * Class&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getIntegerProperty_1() {
        int res = PropertiesUtility.getIntegerProperty(properties, key, required, exceptionClass);

        assertEquals("'getIntegerProperty' should be correct.", 101, res);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getIntegerProperty(Properties properties, String key, boolean required,
     * Class&lt;T&gt; exceptionClass)</code> with optional property does not exist.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getIntegerProperty_2() {
        key = "not_exist";
        required = false;
        Integer res = PropertiesUtility.getIntegerProperty(properties, key, required, exceptionClass);

        assertNull("'getIntegerProperty' should be correct.", res);
    }

    /**
     * <p>
     * Failure test for the method <code>getIntegerProperty(Properties properties, String key, boolean required,
     * Class&lt;T&gt; exceptionClass)</code> with required property is missing.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_getIntegerProperty_propertyMissing() {
        key = "not_exist";
        PropertiesUtility.getIntegerProperty(properties, key, required, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>getIntegerProperty(Properties properties, String key, boolean required,
     * Class&lt;T&gt; exceptionClass)</code> with the property is not a valid integer.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_getIntegerProperty_propertyInvalid() {
        key = "retrieverClassName";
        PropertiesUtility.getIntegerProperty(properties, key, required, exceptionClass);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getLongProperty(Properties properties, String key, boolean required,
     * Class&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getLongProperty_1() {
        long res = PropertiesUtility.getLongProperty(properties, key, required, exceptionClass);

        assertEquals("'getLongProperty' should be correct.", 101L, res);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getLongProperty(Properties properties, String key, boolean required,
     * Class&lt;T&gt; exceptionClass)</code> with optional property does not exist.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getLongProperty_2() {
        key = "not_exist";
        required = false;
        Long res = PropertiesUtility.getLongProperty(properties, key, required, exceptionClass);

        assertNull("'getLongProperty' should be correct.", res);
    }

    /**
     * <p>
     * Failure test for the method <code>getLongProperty(Properties properties, String key, boolean required,
     * Class&lt;T&gt; exceptionClass)</code> with required property is missing.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_getLongProperty_propertyMissing() {
        key = "not_exist";
        PropertiesUtility.getLongProperty(properties, key, required, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>getLongProperty(Properties properties, String key, boolean required,
     * Class&lt;T&gt; exceptionClass)</code> with property is not a valid long integer.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_getLongProperty_propertyInvalid() {
        key = "retrieverClassName";
        PropertiesUtility.getLongProperty(properties, key, required, exceptionClass);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getDoubleProperty(Properties properties, String key, boolean required,
     * Class&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getDoubleProperty_1() {
        double res = PropertiesUtility.getDoubleProperty(properties, key, required, exceptionClass);

        assertEquals("'getDoubleProperty' should be correct.", 101, res, 0.001);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getDoubleProperty(Properties properties, String key, boolean required,
     * Class&lt;T&gt; exceptionClass)</code> with optional property does not exist.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getDoubleProperty_2() {
        key = "not_exist";
        required = false;
        Double res = PropertiesUtility.getDoubleProperty(properties, key, required, exceptionClass);

        assertNull("'getDoubleProperty' should be correct.", res);
    }

    /**
     * <p>
     * Failure test for the method <code>getDoubleProperty(Properties properties, String key, boolean required,
     * Class&lt;T&gt; exceptionClass)</code> with required property is missing.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_getDoubleProperty_propertyMissing() {
        key = "not_exist";
        PropertiesUtility.getDoubleProperty(properties, key, required, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>getDoubleProperty(Properties properties, String key, boolean required,
     * Class&lt;T&gt; exceptionClass)</code> with property is not a valid double.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_getDoubleProperty_propertyInvalid() {
        key = "retrieverClassName";
        PropertiesUtility.getDoubleProperty(properties, key, required, exceptionClass);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getDateProperty(Properties properties, String key, String format,
     * boolean required, Class&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getDateProperty_1() {
        key = "start_date";
        Date res = PropertiesUtility.getDateProperty(properties, key, "yyyy/MM/dd HH:mm:ss",
            required, exceptionClass);

        assertEquals("'getDateProperty' should be correct.",
            "2011/04/03 21:09:55", TestsHelper.TIMESTAMP_FORMAT.format(res));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getDateProperty(Properties properties, String key, String format,
     * boolean required, Class&lt;T&gt; exceptionClass)</code> with optional property does not exist.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getDateProperty_2() {
        key = "not_exist";
        required = false;
        Date res = PropertiesUtility.getDateProperty(properties, key, "yyyy/MM/dd HH:mm:ss",
            required, exceptionClass);

        assertNull("'getDateProperty' should be correct.", res);
    }

    /**
     * <p>
     * Failure test for the method <code>getDateProperty(Properties properties, String key, String format,
     * boolean required, Class&lt;T&gt; exceptionClass)</code> with required property is missing.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_getDateProperty_propertyMissing() {
        key = "not_exist";
        PropertiesUtility.getDateProperty(properties, key, "yyyy/MM/dd HH:mm:ss",
            required, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>getDateProperty(Properties properties, String key, String format,
     * boolean required, Class&lt;T&gt; exceptionClass)</code> with property has invalid format.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_getDateProperty_propertyInvalid1() {
        key = "retrieverClassName";
        PropertiesUtility.getDateProperty(properties, key, "yyyy/MM/dd HH:mm:ss",
            required, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>getDateProperty(Properties properties, String key, String format,
     * boolean required, Class&lt;T&gt; exceptionClass)</code> with property has invalid format.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_getDateProperty_propertyInvalid2() {
        key = "start_date";
        PropertiesUtility.getDateProperty(properties, key, "yyyy/MM/dd",
            required, exceptionClass);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getClassProperty(Properties properties, String key, boolean required,
     * Class&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getClassProperty_1() {
        key = "retrieverClassName";
        Class<?> res = PropertiesUtility.getClassProperty(properties, key, required, exceptionClass);

        assertEquals("'getClassProperty' should be correct.", CustomRetriever.class, res);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getClassProperty(Properties properties, String key, boolean required,
     * Class&lt;T&gt; exceptionClass)</code> with optional property does not exist.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getClassProperty_2() {
        key = "not_exist";
        required = false;
        Class<?> res = PropertiesUtility.getClassProperty(properties, key, required, exceptionClass);

        assertNull("'getClassProperty' should be correct.", res);
    }

    /**
     * <p>
     * Failure test for the method <code>getClassProperty(Properties properties, String key, boolean required,
     * Class&lt;T&gt; exceptionClass)</code> with required property is missing.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_getClassProperty_propertyMissing() {
        key = "not_exist";
        PropertiesUtility.getClassProperty(properties, key, required, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>getClassProperty(Properties properties, String key, boolean required,
     * Class&lt;T&gt; exceptionClass)</code> with the property is not a valid integer.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_getClassProperty_propertyInvalid() {
        key = "userId";
        PropertiesUtility.getClassProperty(properties, key, required, exceptionClass);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getSubConfiguration(Properties properties, String configName)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getSubConfiguration_1() {
        properties.put(new Object(), "value");

        String configName = "retriever";
        Properties res = PropertiesUtility.getSubConfiguration(properties, configName);

        assertEquals("'getSubConfiguration' should be correct.", 2, res.size());
        assertEquals("'getSubConfiguration' should be correct.", "12345", res.getProperty("param1"));
        assertEquals("'getSubConfiguration' should be correct.", "ABC", res.getProperty("param2"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getSubConfiguration(Properties properties, String configName)</code>
     * with the inner configuration does not exist.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_getSubConfiguration_2() {
        String configName = "not_exist";
        Properties res = PropertiesUtility.getSubConfiguration(properties, configName);

        assertEquals("'getSubConfiguration' should be correct.", 0, res.size());
    }
}
