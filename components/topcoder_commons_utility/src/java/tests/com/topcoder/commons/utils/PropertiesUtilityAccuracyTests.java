/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;

import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;

/**
 * <p>
 * Unit tests for {@link PropertiesUtility} class.
 * </p>
 *
 * @author dingying131
 * @version 1.0
 */
public class PropertiesUtilityAccuracyTests {

    /**
     * <p>
     * The test properties instance.
     * </p>
     */
    private static Properties properties;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(PropertiesUtilityAccuracyTests.class);
    }

    /**
     * <p>
     * Before class method that sets up the test properties instance.
     * </p>
     */
    @BeforeClass
    public static void beforeClass() {
        properties = new Properties();
        properties.put("string", "value");
        properties.put("strings", "value1,value2");
        properties.put("int", "2");
        properties.put("long", "100000000000");
        properties.put("double", "2.1");
        properties.put("date", "1970-1-1 00:00:00");
        properties.put("class", "java.lang.String");
        properties.put("sub.a", "a");
        properties.put("sub.b", "b");
    }

    /**
     * <p>
     * Accuracy test for {@link PropertiesUtility#getStringProperty(Properties, String, boolean, Class)}.
     * </p>
     *
     * <p>
     * The string property should be fetched correctly.
     * </p>
     */
    @Test
    public void testGetStringProperty() {
        Assert.assertEquals("The string property should be fetched correctly", "value", PropertiesUtility
                .getStringProperty(properties, "string", true, IllegalArgumentException.class));
    }

    /**
     * <p>
     * Accuracy test for {@link PropertiesUtility#getStringsProperty(Properties, String, String, boolean, Class)}.
     * </p>
     *
     * <p>
     * The strings property should be fetched correctly.
     * </p>
     */
    @Test
    public void testGetStringsProperty() {
        String[] values = PropertiesUtility.getStringsProperty(properties, "strings", ",", true,
                IllegalArgumentException.class);
        Assert.assertEquals("The string property should be fetched correctly", values.length, 2);
        Assert.assertEquals("The string property should be fetched correctly", values[0], "value1");
        Assert.assertEquals("The string property should be fetched correctly", values[1], "value2");
    }

    /**
     * <p>
     * Accuracy test for {@link PropertiesUtility#getIntegerProperty(Properties, String, boolean, Class)}.
     * </p>
     *
     * <p>
     * The Integer property should be fetched correctly.
     * </p>
     */
    @Test
    public void testGetIntegerProperty() {
        Assert.assertEquals("Int property should be fetched correctly.", Integer.valueOf(2), PropertiesUtility
                .getIntegerProperty(properties, "int", true, IllegalArgumentException.class));
    }

    /**
     * <p>
     * Accuracy test for {@link PropertiesUtility#getLongProperty(Properties, String, boolean, Class)}.
     * </p>
     *
     * <p>
     * The Long property should be fetched correctly.
     * </p>
     */
    @Test
    public void testGetLongProperty() {
        Assert.assertEquals("The Long property should be fetched correctly.", Long.valueOf("100000000000"),
                PropertiesUtility.getLongProperty(properties, "long", true, IllegalArgumentException.class));
    }

    /**
     * <p>
     * Accuracy test for {@link PropertiesUtility#getDoubleProperty(Properties, String, boolean, Class)}.
     * </p>
     *
     * <p>
     * The Double property should be fetched correctly.
     * </p>
     */
    @Test
    public void testGetDoubleProperty() {
        Assert.assertEquals("The Double property should be fetched correctly.", Double.valueOf("2.1"),
                PropertiesUtility.getDoubleProperty(properties, "double", true, IllegalArgumentException.class));
    }

    /**
     * <p>
     * Accuracy test for {@link PropertiesUtility#getDateProperty(Properties, String, String, boolean, Class)}.
     * </p>
     *
     * <p>
     * The Date property should be fetched correctly.
     * </p>
     * @throws to jUnit
     */
    @Test
    public void testGetDateProperty() throws Exception {
        Assert.assertEquals("The Date property should be fetched correctly.", new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.US).parse("1970-1-1 00:00:00"), PropertiesUtility.getDateProperty(
                properties, "date", "yyyy-MM-dd HH:mm:ss", true, IllegalArgumentException.class));
    }

    /**
     * <p>
     * Accuracy test for {@link PropertiesUtility#getSubConfiguration(Properties, String)}.
     * </p>
     *
     * <p>
     * The sub configuration property should be fetched correctly.
     * </p>
     */
    @Test
    public void testGetClassProperty() {
        Properties prop = new Properties();
        prop.put("a", "a");
        prop.put("b", "b");
        Assert.assertEquals("Sub configurations should be fetched properly.", prop, PropertiesUtility
                .getSubConfiguration(properties, "sub"));
    }
}
