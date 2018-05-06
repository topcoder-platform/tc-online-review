/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl;

import java.io.File;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;
import com.topcoder.util.objectfactory.ObjectFactory;

/**
 * Show the usage for the component.
 *
 * @author sparemax
 * @version 1.0
 */
public class Demo extends TestCase {
    /** The test files path. */
    private static final String TEST_FILES = "test_files" + File.separator;

    /**
     * Create the needed object using <code>ObjectFactory</code> created from a
     * <code>ConfigurationObject</code> (built manually).
     *
     * @throws Exception
     *             when error occurs
     */
    public void testDemo1() throws Exception {
        // Create the ConfigurationObject instance
        ConfigurationObject root = UnitTestHelper.createConfigurationObject();

        // create ConfigurationObjectSpecificationFactory
        // from the given configuration object
        ConfigurationObjectSpecificationFactory cosf = new ConfigurationObjectSpecificationFactory(root);

        // create the object factory
        ObjectFactory objFactory = new ObjectFactory(cosf);

        // create the needed StringBuffer object using the object factory
        StringBuffer buffer = (StringBuffer) objFactory.createObject("buffer");

        // create the needed Bar object using the object factory
        Bar bar = (Bar) objFactory.createObject("bar");

        // create the needed TestClass object using the object factory
        TestClass testClass = (TestClass) objFactory.createObject("frac", "default");

        // check the objects
        assertEquals("Bad data retrieved.", "string buffer", buffer.toString());

        assertEquals("Bad data retrieved.", 100.0, bar.getF(), 0.000001);
        assertEquals("Bad data retrieved.", "string buffer", bar.getBuffer().toString());

        assertEquals("Bad data retrieved.", 1, testClass.getIntValue());
        assertEquals("Bad data retrieved.", "Strong", testClass.getStr());
        assertEquals("Bad data retrieved.", 100.0, testClass.getBar().getF(), 0.000001);
        assertEquals("Bad data retrieved.", "string buffer", testClass.getBar().getBuffer().toString());
    }

    /**
     * Create the needed object using <code>ObjectFactory</code> created from a
     * <code>ConfigurationObject</code> (built using Configuration Persistence
     * component and configurations stored in a xml configuration file).
     *
     * @throws Exception
     *             when error occurs
     */
    public void testDemo2() throws Exception {
        // build the XMLFilePersistence
        XMLFilePersistence xmlFilePersistence = new XMLFilePersistence();

        // load the ConfigurationObject from the input file
        ConfigurationObject cfgObject = xmlFilePersistence.loadFile("test", new File(TEST_FILES + "config.xml"));

        // create ConfigurationObjectSpecificationFactory
        // from the given configuration object
        ConfigurationObjectSpecificationFactory cosf = new ConfigurationObjectSpecificationFactory(
                cfgObject.getChild("valid_config"));

        // create the object factory
        ObjectFactory objFactory = new ObjectFactory(cosf);

        // create the needed Bar object using the object factory
        Bar bar = (Bar) objFactory.createObject("bar");

        // create the needed TestClass object using the object factory
        TestClass testClass = (TestClass) objFactory.createObject("frac", "default");

        // check the objects
        assertEquals("Bad data retrieved.", 2.5F, bar.getF(), 0.000001);

        assertEquals("Bad data retrieved.", 2, testClass.getIntValue());
        assertEquals("Bad data retrieved.", "Strong", testClass.getStr());
        assertEquals("Bad data retrieved.", 2.5F, testClass.getBar().getF(), 0.000001);
    }
}
