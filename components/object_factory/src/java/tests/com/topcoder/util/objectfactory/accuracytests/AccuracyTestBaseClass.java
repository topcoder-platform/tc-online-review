/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.accuracytests;

import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

/**
 * The base class of accuracy test.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class AccuracyTestBaseClass extends TestCase {
    /** The value of the namespace of complex specification used for test. */
    protected static final String COMPLEX_NAMESPACE = "AccuracyTestConfigManagerSpecificationFactory.Complex";

    /** The value of the namespace of the type of int array used for test. */
    protected static final String INT_ARRAY_NAMESPACE = "AccuracyTestConfigManagerSpecificationFactory.intArray";

    /** The value of the namespace of the type of char array used for test. */
    protected static final String CHAR_ARRAY_NAMESPACE = "AccuracyTestConfigManagerSpecificationFactory.charArray";

    /** The value of the namespace of the type of String array used for test. */
    protected static final String STRING_ARRAY_NAMESPACE = "AccuracyTestConfigManagerSpecificationFactory.StringArray";

    /** The value of the namespace of the type of Integer array used for test. */
    protected static final String INTEGER_ARRAY_NAMESPACE = "AccuracyTestConfigManagerSpecificationFactory.IntegerArray";

    /** The name of the file of the complex specification used for test. */
    protected static final String COMPLEX_FILE_NAME = "accuracytests/complex.xml";

    /** The name of the file of the type of int array used for test. */
    protected static final String INT_ARRAY_FILE_NAME = "accuracytests/intArray.xml";

    /** The name of the file of the type of char array used for test. */
    protected static final String CHAR_ARRAY_FILE_NAME = "accuracytests/charArray.xml";

    /** The name of the file of the type of String array used for test. */
    protected static final String STRING_ARRAY_FILE_NAME = "accuracytests/StringArray.xml";

    /** The name of the file of the type of Integer array used for test. */
    protected static final String INTEGER_ARRAY_FILE_NAME = "accuracytests/IntegerArray.xml";

    /**
     * See javadoc for junit.framework.TestCase#setUp().
     *
     * @throws Exception exception that occurs during the setup process.
     *
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        addNamespace(cm, COMPLEX_NAMESPACE, COMPLEX_FILE_NAME);
        addNamespace(cm, INT_ARRAY_NAMESPACE, INT_ARRAY_FILE_NAME);
        addNamespace(cm, STRING_ARRAY_NAMESPACE, STRING_ARRAY_FILE_NAME);
        addNamespace(cm, INTEGER_ARRAY_NAMESPACE, INTEGER_ARRAY_FILE_NAME);
        addNamespace(cm, CHAR_ARRAY_NAMESPACE, CHAR_ARRAY_FILE_NAME);
    }

    /**
     * See javadoc for junit.framework.TestCase#tearDown().
     *
     * @throws Exception exception that occurs during the tear down process.
     *
     * @see junit.framework.TestCase#tearDown()
     */
    public void tearDown() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        removeNamespace(cm, COMPLEX_NAMESPACE);
        removeNamespace(cm, INT_ARRAY_NAMESPACE);
        removeNamespace(cm, CHAR_ARRAY_NAMESPACE);
        removeNamespace(cm, STRING_ARRAY_NAMESPACE);
        removeNamespace(cm, INTEGER_ARRAY_NAMESPACE);
    }

    /**
     * Adds the namespace if it does not exist.
     *
     * @param cm the instance of the config manager.
     * @param namespace the namespace to add.
     * @param filename the name of the xml file used to add the namespace.
     *
     * @throws Exception exception that occurs during the adding process.
     */
    private void addNamespace(ConfigManager cm, String namespace, String filename) throws Exception {
        if (!cm.existsNamespace(namespace)) {
            cm.add(namespace, filename, ConfigManager.CONFIG_XML_FORMAT);
        } else {
            cm.refresh(namespace);
        }
    }

    /**
     * Removes the namespace if it exists.
     *
     * @param cm the instance of the config manager.
     * @param namespace the namespace to remove.
     *
     * @throws Exception exception that occurs during the removing process.
     */
    private void removeNamespace(ConfigManager cm, String namespace) throws Exception {
        if (cm.existsNamespace(namespace)) {
            cm.removeNamespace(namespace);
        }
    }
}
