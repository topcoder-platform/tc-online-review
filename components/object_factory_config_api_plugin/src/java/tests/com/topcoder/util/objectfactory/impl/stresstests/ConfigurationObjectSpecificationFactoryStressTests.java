/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl.stresstests;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.SpecificationFactory;
import com.topcoder.util.objectfactory.impl.ConfigurationObjectSpecificationFactory;

import junit.framework.TestCase;

/**
 * Stress tests of ConfigurationObjectSpecificationFactory class.
 *
 * @author King_Bette
 * @version 1.0
 */
public class ConfigurationObjectSpecificationFactoryStressTests extends TestCase {
    /**
     * test constructor 100 times.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testConstructor1() throws Exception {
        SpecificationFactory specificationFactory = null;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            specificationFactory = new ConfigurationObjectSpecificationFactory(createConfigurationObject());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("test constructor 100 times cost " + (endTime - startTime) + "ms.");
        ObjectFactory objectFactory = new ObjectFactory(specificationFactory);

        // validate results.
        Bar bar = (Bar) objectFactory.createObject("bar");
        TestClass fracDefault = (TestClass) objectFactory.createObject("frac", "default");

        // check the objects
        assertEquals("Bad data retrieved.", 100.0, bar.getF(), 0.000001);
        assertEquals("Bad data retrieved.", "string buffer", bar.getBuffer().toString());

        assertEquals("Bad data retrieved.", 1, fracDefault.getIntValue());
        assertEquals("Bad data retrieved.", "Strong", fracDefault.getStr());
        assertEquals("Bad data retrieved.", 100.0, fracDefault.getBar().getF(), 0.000001);
        assertEquals("Bad data retrieved.", "string buffer", fracDefault.getBar().getBuffer().toString());
    }

    /**
     * Run getObjectSpecification 1000 times.
     *
     * @throws Exception
     *             to Junit.
     */
    public void testConstructor2() throws Exception {
        SpecificationFactory specificationFactory = null;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            specificationFactory = new ConfigurationObjectSpecificationFactory(createConfigurationObject());
        }
        long endTime = System.currentTimeMillis();
        System.out.println("test constructor 1000 times cost " + (endTime - startTime) + "ms.");
        ObjectFactory objectFactory = new ObjectFactory(specificationFactory);

        // validate results.
        Bar bar = (Bar) objectFactory.createObject("bar");
        TestClass fracDefault = (TestClass) objectFactory.createObject("frac", "default");

        // check the objects
        assertEquals("Bad data retrieved.", 100.0, bar.getF(), 0.000001);
        assertEquals("Bad data retrieved.", "string buffer", bar.getBuffer().toString());

        assertEquals("Bad data retrieved.", 1, fracDefault.getIntValue());
        assertEquals("Bad data retrieved.", "Strong", fracDefault.getStr());
        assertEquals("Bad data retrieved.", 100.0, fracDefault.getBar().getF(), 0.000001);
        assertEquals("Bad data retrieved.", "string buffer", fracDefault.getBar().getBuffer().toString());
    }

    /**
     * Create a valid <code>ConfigurationObject</code> instance used in the
     * stress test.
     *
     * @throws Exception
     *             when error occurs
     * @return the created <code>ConfigurationObject</code> instance
     */
    private static ConfigurationObject createConfigurationObject() throws Exception {
        // the root ConfigurationObject
        ConfigurationObject root = new DefaultConfigurationObject("root");

        /*
         * create the TestClass configuration object
         */
        // specifying the identifier and the key
        ConfigurationObject testClassObject = new DefaultConfigurationObject("frac:default");
        // the type of the object to be created
        testClassObject.setPropertyValue("type", "com.topcoder.util.objectfactory.impl.stresstests.TestClass");

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
        barObject.setPropertyValue("type", "com.topcoder.util.objectfactory.impl.stresstests.Bar");

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
}
