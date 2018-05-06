/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils.stresstests;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.commons.utils.PropertiesUtility;

/**
 * <p>
 * Stress test case of {@link PropertiesUtility}.
 * </p>
 * 
 * @author mumujava
 * @version 1.0
 */
public class PropertiesUtilityStressTest extends BaseStressTest {

    private static final int PROPERTIES_SIZE = 10000;
    private static Properties properties;
    static {
        properties = new Properties();
        for (int i = 0; i < PROPERTIES_SIZE; i++) {
            properties.setProperty("strkey"+i, ""+i);
        }
        for (int i = 0; i < PROPERTIES_SIZE; i++) {
            properties.setProperty("strskey"+i, i + ";" + (i+1));
        }
        for (int i = 0; i < PROPERTIES_SIZE; i++) {
            properties.setProperty("doublekey"+i, Double.toString(i));
        }
        for (int i = 0; i < PROPERTIES_SIZE; i++) {
            properties.setProperty("datekey"+i, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }
        for (int i = 0; i < PROPERTIES_SIZE; i++) {
            properties.setProperty("classkey"+i, String.class.getName());
        }
        for (int i = 0; i < PROPERTIES_SIZE; i++) {
            properties.setProperty("prefix"+i+".0", String.valueOf(i));
            properties.setProperty("prefix"+i+".1", String.valueOf(i+1));
        }
    }
    /**
     * <p>
     * Sets up test environment.
     * </p>
     * 
     * @throws Exception
     *             to jUnit.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     * 
     * @throws Exception
     *             to jUnit.
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     * 
     * @return a TestSuite for this test case.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(
                PropertiesUtilityStressTest.class);
        return suite;
    }

    /**
     * <p>
     * Stress test for method PropertiesUtility#getStringProperty().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_getStringProperty() throws Throwable {
        for (int i = 0; i < testCount; i++) {
            assertEquals("100", PropertiesUtility.getStringProperty(properties, "strkey100", true, IllegalArgumentException.class));
        }
        
        System.out.println("Run getStringProperty for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }
    /**
     * <p>
     * Stress test for method PropertiesUtility#getStringsProperty().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_getStringsProperty() throws Throwable {
        for (int i = 0; i < testCount; i++) {
            String[] stringsProperty = PropertiesUtility.getStringsProperty(properties, "strskey100", ";",true, IllegalArgumentException.class);
            assertEquals("100", stringsProperty[0]);
            assertEquals("101", stringsProperty[1]);
            assertEquals(2, stringsProperty.length);
        }
        
        System.out.println("Run getStringsProperty for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }
    /**
     * <p>
     * Stress test for method PropertiesUtility#getIntegerProperty().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_getIntegerProperty() throws Throwable {
        for (int i = 0; i < testCount; i++) {
            assertEquals((Integer)100, PropertiesUtility.getIntegerProperty(properties, "strkey100", true, IllegalArgumentException.class));
        }
        
        System.out.println("Run getIntegerProperty for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }
    /**
     * <p>
     * Stress test for method PropertiesUtility#getLongProperty().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_getLongProperty() throws Throwable {
        for (int i = 0; i < testCount; i++) {
            assertEquals((Long)100L, PropertiesUtility.getLongProperty(properties, "strkey100", true, IllegalArgumentException.class));
        }
        
        System.out.println("Run getLongProperty for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }
    /**
     * <p>
     * Stress test for method PropertiesUtility#getDoubleProperty().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_getDoubleProperty() throws Throwable {
        for (int i = 0; i < testCount; i++) {
            assertEquals((Double)100.0, PropertiesUtility.getDoubleProperty(properties, "doublekey100", true, IllegalArgumentException.class));
        }
        
        System.out.println("Run getDoubleProperty for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }
    /**
     * <p>
     * Stress test for method PropertiesUtility#getDateProperty().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_getDateProperty() throws Throwable {
        for (int i = 0; i < testCount; i++) {
            assertEquals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), new SimpleDateFormat("yyyy-MM-dd").format(PropertiesUtility.getDateProperty(properties, "datekey100", "yyyy-MM-dd", true, IllegalArgumentException.class)));
        }
        
        System.out.println("Run getDateProperty for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }
    /**
     * <p>
     * Stress test for method PropertiesUtility#getClassProperty().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_getClassProperty() throws Throwable {
        for (int i = 0; i < testCount; i++) {
            assertEquals(String.class, PropertiesUtility.getClassProperty(properties, "classkey100", true, IllegalArgumentException.class));
        }
        
        System.out.println("Run getClassProperty for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }

    /**
     * <p>
     * Stress test for method PropertiesUtility#getSubConfiguration().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_getSubConfiguration() throws Throwable {
        for (int i = 0; i < testCount; i++) {
            Properties stringsProperty = PropertiesUtility.getSubConfiguration(properties, "prefix100");
            assertEquals("100", stringsProperty.getProperty("0"));
            assertEquals("101", stringsProperty.getProperty("1"));
            assertEquals(2, stringsProperty.size());
        }
        
        System.out.println("Run getSubConfiguration for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }
}
