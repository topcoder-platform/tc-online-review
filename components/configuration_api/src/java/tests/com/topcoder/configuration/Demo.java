/*
 * Copyright (C) 2007, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import com.topcoder.configuration.defaults.DefaultConfigurationObject;

/**
 * The demo of Configuration API Component.
 *
 * @author haozhangr, saarixx, sparemax
 * @version 1.1
 */
public class Demo extends TestCase {
    /**
     * The DefaultConfigurationObject instance for test.
     */
    private ConfigurationObject defaultCo;

    /**
     * Set up the DefaultConfigurationObject test case, create the DefaultConfigurationObject instance for test.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        defaultCo = new DefaultConfigurationObject("the name");
    }

    /**
     * The demo show how to create the ConfigurationObject.
     *
     */
    public void test_demo_create_ConfigurationObject() {
        // create a DefaultConfigurationObject
        defaultCo = new DefaultConfigurationObject("the name");
        // create a SynchronizedConfigurationObject with the inner object
        ConfigurationObject synchronizedCo = new SynchronizedConfigurationObject(defaultCo);
        // DefaultConfigurationObject is also can be used as TemplateConfigurationObject
        TemplateConfigurationObject templateCo = (TemplateConfigurationObject) defaultCo;
    }

    /**
     * The demo show how to manipulate the properties of a ConfigurationObject.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_demo_manipulate_properties() throws Exception {
        // set the value, can be null, and the old value will be returned
        Object[] values = defaultCo.setPropertyValue("key", "value");
        // set a array of values with the key
        values = defaultCo.setPropertyValues("key", new Object[] {"value1", "value2"});

        // check whether a ConfigurationObject contains a key
        boolean contained = defaultCo.containsProperty("key");

        // get all the values with the key
        values = defaultCo.getPropertyValues("key");
        // get the first value of the key
        Object value = defaultCo.getPropertyValue("key", Object.class);
        // get the count of values with the key
        int count = defaultCo.getPropertyValuesCount("key");

        // remove the values of the key
        defaultCo.removeProperty("key");
        defaultCo.clearChildren();
        // get all the keys of properties
        String[] keys = defaultCo.getAllPropertyKeys();
        // get the keys with the regex pattern
        keys = defaultCo.getPropertyKeys("[a\\*b]");
    }

    /**
     * The demo show how to manipulate the children of a ConfigurationObject.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_demo_manipulate_child() throws Exception {
        // add a child
        DefaultConfigurationObject child = new DefaultConfigurationObject("child");
        defaultCo.addChild(child);

        // check contains child
        boolean contained = defaultCo.containsChild("child");
        // get the child by name
        ConfigurationObject thechild = defaultCo.getChild("child");
        // remove the child by name
        thechild = defaultCo.removeChild("child");
        // clear the child
        defaultCo.clearChildren();

        // get all the children
        ConfigurationObject[] children = defaultCo.getAllChildren();
        // get all the children by a regex pattern
        children = defaultCo.getChildren("[abc]");
    }

    /**
     * The demo show how to manipulate the Descendants of a ConfigurationObject.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_demo_manipulate_Descendants() throws Exception {
        // get all the descendants
        ConfigurationObject[] descendants = defaultCo.getAllDescendants();
        // find the descendants by a path
        descendants = defaultCo.findDescendants("path");
        // delete the descendants by a path
        descendants = defaultCo.deleteDescendants("path");
        // get descendants with the regex pattern
        descendants = defaultCo.getDescendants("pattern");
    }

    /**
     * The demo show the clone of the ConfigurationObject.
     *
     */
    public void test_demo_clone() {
        // clone the ConfigurationObject
        ConfigurationObject clone = (ConfigurationObject) defaultCo.clone();
        ConfigurationObject synchronizedCo = new SynchronizedConfigurationObject(clone);
        SynchronizedConfigurationObject synchronizedClone = (SynchronizedConfigurationObject) synchronizedCo.clone();
    }

    /**
     * The demo show how to use as TemplateConfigurationObject.
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_demo_useas_TemplateConfigurationObject() throws Exception {
        DefaultConfigurationObject child = new DefaultConfigurationObject("child");
        TemplateConfigurationObject templateCo = (TemplateConfigurationObject) defaultCo;
        // set the property value with a path
        templateCo.setPropertyValue("a", "key", "value");

        // set the property values with a path
        templateCo.setPropertyValues("a/b", "key", new Object[] {"value"});

        // remove the property values with a path
        templateCo.removeProperty("a*\\/b", "key");

        // clear property with a path
        templateCo.clearProperties("path/*c");
        // add a child with a path
        templateCo.addChild("path", child);
        // remove child with a path and child name
        templateCo.removeChild("path", child.getName());
        // clear children with a path
        templateCo.clearChildren("b");

        // processDescendants with a path
        templateCo.processDescendants("path", new ProcessorMock());
    }

    /**
     * The demo retrieval of property values with casting and parsing.
     *
     * @throws Exception
     *             to JUnit
     *
     * @since 1.1
     */
    public void test_demo_retrieve_property_values() throws Exception {
        // Create an instance of DefaultConfigurationObject
        ConfigurationObject config = new DefaultConfigurationObject("default");

        // Initialize sample properties
        config.setPropertyValues("ints", new Object[] {1, 2, 3});
        config.setPropertyValues("strings", new Object[] {"abc", "def"});
        config.setPropertyValue("intValue1", 5);
        config.setPropertyValue("intValue2", "5");
        config.setPropertyValue("longValue", "12345");
        config.setPropertyValue("doubleValue", "1.23");
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(2011, 0, 1);
        config.setPropertyValue("dateValue1", calendar.getTime());
        config.setPropertyValue("dateValue2", "2011-01-01");
        config.setPropertyValue("class", "java.lang.Integer");

        // Retrieve the property values as integer array
        Integer[] intValues = config.getPropertyValues("ints", Integer.class);
        // intValues must contain {1, 2, 3}

        // Retrieve the property values as string array
        String[] stringValues = config.getPropertyValues("strings", String.class);
        // stringValues must contain {"abc", "def"}

        // Retrieve the integer property value (without parsing support)
        Integer intValue = config.getPropertyValue("intValue1", Integer.class);
        // intValue must be equal to 5

        // Retrieve the integer property value by parsing it from string
        intValue = config.getIntegerProperty("intValue2", true);
        // intValue must be equal to 5

        // Retrieve the long property value by parsing it from string
        Long longValue = config.getLongProperty("longValue", true);
        // longValue must be equal to 12345

        // Retrieve the double property value by parsing it from string
        Double doubleValue = config.getDoubleProperty("doubleValue", true);
        // doubleValue must be equal to 1.23

        // Retrieve the date property stored as Date
        Date dateValue1 = config.getDateProperty("dateValue1", "yyyy-MM-dd", true);

        // Retrieve the date property stored as String
        Date dateValue2 = config.getDateProperty("dateValue2", "yyyy-MM-dd", true);

        // dateValue1.getTime() must be equal to dateValue2.getTime()
        // Both dates must represent 2011-01-01

        // Retrieve the class property value
        Class<?> clazz = config.getClassProperty("class", true);
        // clazz must be equal to Integer.class
    }

    /**
     * The demo retrieval of required property values.
     *
     * @throws Exception
     *             to JUnit
     *
     * @since 1.1
     */
    public void test_demo_retrieve_required_property_values() throws Exception {
        // Create an instance of DefaultConfigurationObject
        ConfigurationObject config = new DefaultConfigurationObject("default");

        // Retrieve optional not existing property
        Object value = config.getPropertyValue("key1", Object.class, false);
        // value must be equal to null

        try {
            // Retrieve required, but not existing property
            value = config.getPropertyValue("key2", Object.class, true);
            // PropertyNotFoundException must be thrown here
        } catch (PropertyNotFoundException e) {
            // Ignore
        }
    }
}
