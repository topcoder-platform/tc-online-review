/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl.accuracytests;

import java.io.File;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;

import junit.framework.TestCase;

/**
 * The base class for accuracy test.
 * 
 * @author 80x86
 * @version 1.0
 */
public class AccuracyTestBaseClass extends TestCase {
    /**
     * <p>
     * Represents the <code>XMLFilePersistence</code> used to load object specifications from config file.
     * </p>
     * 
     * <p>
     * Instantiated when declared, and never changed later. Used in <tt>loadConfigurationObjectFromFile</tt> method.
     * </p>
     */
    private static XMLFilePersistence xmlFilePersistence = new XMLFilePersistence();

    /**
     * The value of the namespace of complex specification used for test.
     */
    protected static final String COMPLEX_NAMESPACE = "Complex";

    /**
     * The value of the namespace of the type of int array used for test.
     */
    protected static final String INT_ARRAY_NAMESPACE = "intArray";

    /**
     * The value of the namespace of the type of char array used for test.
     */
    protected static final String CHAR_ARRAY_NAMESPACE = "charArray";

    /**
     * The value of the namespace of the type of String array used for test.
     */
    protected static final String STRING_ARRAY_NAMESPACE = "StringArray";

    /**
     * The value of the namespace of the type of Integer array used for test.
     */
    protected static final String INTEGER_ARRAY_NAMESPACE = "IntegerArray";

    /**
     * The name of the file of the complex specification used for test.
     */
    protected static final String COMPLEX_FILE_NAME = "test_files/accuracytests/Complex.xml";

    /**
     * The name of the file of the type of int array used for test.
     */
    protected static final String INT_ARRAY_FILE_NAME = "test_files/accuracytests/intArray.xml";

    /**
     * The name of the file of the type of char array used for test.
     */
    protected static final String CHAR_ARRAY_FILE_NAME = "test_files/accuracytests/charArray.xml";

    /**
     * The name of the file of the type of String array used for test.
     */
    protected static final String STRING_ARRAY_FILE_NAME = "test_files/accuracytests/StringArray.xml";

    /**
     * The name of the file of the type of Integer array used for test.
     */
    protected static final String INTEGER_ARRAY_FILE_NAME = "test_files/accuracytests/IntegerArray.xml";

    /**
     * <p>
     * Loads the object specifications from the config file specified by namespace and file path.
     * </p>
     * 
     * @param namespace
     *            the namespace
     * @param file
     *            the file path specified the config file
     * 
     * @return the ConfigurationObject that provides the ObjectSpecifications
     * 
     * @throws Exception
     *             if any error occurred
     */
    static ConfigurationObject loadConfigurationObjectFromFile(String namespace, String file) throws Exception {
        ConfigurationObject configObj = xmlFilePersistence.loadFile(namespace, new File(file));
        return configObj.getChild(namespace);
    }

    /**
     * <p>
     * Creates the sample <code>ConfigurationObject</code> manually.
     * </p>
     * 
     * @return the ConfiguraitonObject instance
     * 
     * @throws Exception
     *             if any error occurred
     */
    static ConfigurationObject createSampleConfigurationObject() throws Exception {
        // create the root configuration object
        ConfigurationObject root = new DefaultConfigurationObject("Configuration");

        // specifying the identifier and the key
        ConfigurationObject configurationObject = new DefaultConfigurationObject("frac:default");

        // the type of the object to be created
        configurationObject.setPropertyValue("type", "java.lang.StringBuffer");

        // create the params configuration object
        ConfigurationObject params = new DefaultConfigurationObject("params");

        // create one parameter
        ConfigurationObject param1 = new DefaultConfigurationObject("param1");
        param1.setPropertyValue("type", "String");
        param1.setPropertyValue("value", "test buffer");

        // add the children
        params.addChild(param1);
        configurationObject.addChild(params);
        root.addChild(configurationObject);

        return root;
    }
}
