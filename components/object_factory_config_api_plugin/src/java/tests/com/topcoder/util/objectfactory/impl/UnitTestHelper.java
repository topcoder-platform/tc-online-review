/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

/**
 * Helper class for the unit test.
 *
 * @author sparemax
 * @version 1.0
 */
class UnitTestHelper {
    /**
     * Can not create an instance of this class.
     */
    private UnitTestHelper() {
        // empty
    }

    /**
     * Create a valid <code>ConfigurationObject</code> instance used in the unit
     * test.
     *
     * @throws Exception
     *             when error occurs
     * @return the created <code>ConfigurationObject</code> instance
     */
    public static ConfigurationObject createConfigurationObject() throws Exception {
        // the root ConfigurationObject
        ConfigurationObject root = new DefaultConfigurationObject("root");

        /*
         * create the TestClass configuration object
         */
        // specifying the identifier and the key
        ConfigurationObject testClassObject = new DefaultConfigurationObject("frac:default");
        // the type of the object to be created
        testClassObject.setPropertyValue("type", "com.topcoder.util.objectfactory.impl.TestClass");

        // create the params configuration object
        ConfigurationObject params = new DefaultConfigurationObject("params");

        // create one parameter
        ConfigurationObject param1 = new DefaultConfigurationObject("param1");
        param1.setPropertyValue("type", "int");
        param1.setPropertyValue("value", "1");
        // create another parameter
        ConfigurationObject param2 = new DefaultConfigurationObject("param2");
        param2.setPropertyValue("type", "String");
        param2.setPropertyValue("value", "Strong");
        // create another parameter
        ConfigurationObject param3 = new DefaultConfigurationObject("param3");
        param3.setPropertyValue("name", "bar");

        // add the children
        params.addChild(param1);
        params.addChild(param2);
        params.addChild(param3);
        testClassObject.addChild(params);

        /*
         * create the Bar configuration object
         */
        // specifying the identifier and the key
        ConfigurationObject barObject = new DefaultConfigurationObject("bar");
        // the type of the object to be created
        barObject.setPropertyValue("type", "com.topcoder.util.objectfactory.impl.Bar");

        // create the params configuration object
        params = new DefaultConfigurationObject("params");

        // create one parameter
        param1 = new DefaultConfigurationObject("param1");
        param1.setPropertyValue("type", "float");
        param1.setPropertyValue("value", "100.0");
        // create another parameter
        param2 = new DefaultConfigurationObject("param2");
        param2.setPropertyValue("name", "buffer");

        // add the children
        params.addChild(param1);
        params.addChild(param2);
        barObject.addChild(params);

        /*
         * create the StringBuffer configuration object
         */
        // specifying the identifier and the key
        ConfigurationObject bufferObject = new DefaultConfigurationObject("buffer");
        bufferObject.setPropertyValue("type", "java.lang.StringBuffer");

        // create the params configuration object
        params = new DefaultConfigurationObject("params");

        // create one parameter
        param1 = new DefaultConfigurationObject("param1");
        param1.setPropertyValue("type", "String");
        param1.setPropertyValue("value", "string buffer");

        // add the children
        params.addChild(param1);
        bufferObject.addChild(params);

        // add to the root
        root.addChild(testClassObject);
        root.addChild(barObject);
        root.addChild(bufferObject);

        return root;
    }

    /**
     * Create a <code>ConfigurationObject</code> instance with cyclical
     * definition.
     *
     * @throws Exception
     *             when error occurs
     * @return the created <code>ConfigurationObject</code> instance
     */
    public static ConfigurationObject createConfigurationObjectWithCyclical() throws Exception {
        // the root ConfigurationObject
        ConfigurationObject root = new DefaultConfigurationObject("root");

        /*
         * create the TestClass configuration object
         */
        // specifying the identifier and the key
        ConfigurationObject testClassObject = new DefaultConfigurationObject("frac:default");
        // the type of the object to be created
        testClassObject.setPropertyValue("type", "com.topcoder.util.objectfactory.impl.TestClass");

        // create the params configuration object
        ConfigurationObject params = new DefaultConfigurationObject("params");

        ConfigurationObject param1 = new DefaultConfigurationObject("param1");
        param1.setPropertyValue("name", "bar");

        // add the children
        params.addChild(param1);
        testClassObject.addChild(params);

        /*
         * create the Bar configuration object
         */
        // specifying the identifier and the key
        ConfigurationObject barObject = new DefaultConfigurationObject("bar");
        // the type of the object to be created
        barObject.setPropertyValue("type", "com.topcoder.util.objectfactory.impl.Bar");

        // create the params configuration object
        params = new DefaultConfigurationObject("params");

        // create one parameter
        param1 = new DefaultConfigurationObject("param1");
        param1.setPropertyValue("name", "frac:default");

        // add the children
        params.addChild(param1);
        barObject.addChild(params);

        // add to the root
        root.addChild(testClassObject);
        root.addChild(barObject);

        return root;
    }

    /**
     * Create a <code>ConfigurationObject</code> instance with invalid
     * reference.
     *
     * @throws Exception
     *             when error occurs
     * @return the created <code>ConfigurationObject</code> instance
     */
    public static ConfigurationObject createConfigurationObjectWithInvalidReference() throws Exception {
        // the root ConfigurationObject
        ConfigurationObject root = new DefaultConfigurationObject("root");

        /*
         * create the TestClass configuration object
         */
        // specifying the identifier and the key
        ConfigurationObject testClassObject = new DefaultConfigurationObject("frac:default");
        // the type of the object to be created
        testClassObject.setPropertyValue("type", "com.topcoder.util.objectfactory.impl.TestClass");

        // create the params configuration object
        ConfigurationObject params = new DefaultConfigurationObject("params");

        ConfigurationObject param1 = new DefaultConfigurationObject("param1");
        // the reference "barr" is invalid
        param1.setPropertyValue("name", "barr");

        // add the children
        params.addChild(param1);
        testClassObject.addChild(params);

        /*
         * create the Bar configuration object
         */
        // specifying the identifier and the key
        ConfigurationObject barObject = new DefaultConfigurationObject("bar");
        // the type of the object to be created
        barObject.setPropertyValue("type", "com.topcoder.util.objectfactory.impl.Bar");

        // create the params configuration object
        params = new DefaultConfigurationObject("params");

        // create one parameter
        param1 = new DefaultConfigurationObject("param1");
        param1.setPropertyValue("name", "frac:default");

        // add the children
        params.addChild(param1);
        barObject.addChild(params);

        // add to the root
        root.addChild(testClassObject);
        root.addChild(barObject);

        return root;
    }
}
