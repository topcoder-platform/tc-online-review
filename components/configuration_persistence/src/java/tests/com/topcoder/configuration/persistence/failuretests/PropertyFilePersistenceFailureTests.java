/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.persistence.failuretests;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationParserException;
import com.topcoder.configuration.persistence.PropertyFilePersistence;
import com.topcoder.configuration.persistence.XMLFilePersistence;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;


/**
 * <p>
 * Test the functionality of class <code>PropertyFilePersistence</code>.
 * </p>
 * 
 * <p>
 * This test suite contains multiple failure test cases that addressed different aspects for each public methods and constructors.<br>
 * Various real data is used to ensure that the invalid inputs are handled properly as defined in the
 * documentation.<br>
 * </p>
 *
 * @author lyt
 * @version 1.0
 */
public class PropertyFilePersistenceFailureTests extends TestCase {
    /**
     * <p>
     * Represents an instance of <code>PropertyFilePersistence</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private PropertyFilePersistence propertyFilePersistence = null;

	 /**
     * <p>
     * Represents an instance of <code>String</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private static final String NAMESPACE = "com.topcoder.config";

    /**
     * <p>
     * Represents an instance of <code>File</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private File testFile;

    /**
     * <p>
     * Represents an instance of <code>File</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private File tempFile;

    /**
     * <p>
     * Represents an instance of <code>ConfigurationObject</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private ConfigurationObject configurationObject = new DefaultConfigurationObject("config");

    /**
     * <p>
     * Represents an instance of <code>ConfigurationObject</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private ConfigurationObject fileConfig;


    /**
     * <p>
     * Sets up the test environment.
     * </p>
     * 
     * <p>
     * The test instance is initialized and all the need configuration are loaded.
     * </p>
     *
     * @throws Exception Any exception thrown by this method is propagated to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        propertyFilePersistence = new PropertyFilePersistence();

        ConfigurationObject persistenceConfig = new DefaultConfigurationObject("persistence");
        fileConfig = new DefaultConfigurationObject("files");
        configurationObject.addChild(fileConfig);
        configurationObject.addChild(persistenceConfig);
        
        testFile = new File("test_files/failuretests/config1.xml");
        tempFile = new File("test_files/failuretests/temp.xml");
    }

    /**
     * <p>
     * Tear down the test environment.
     * </p>
     * 
     * <p>
     * The test instance is released and the configuration is clear.
     * </p>
     *
     * @throws Exception Any exception thrown by this method is propagated to JUnit
     */
    protected void tearDown() throws Exception {
        configurationObject = null;
        propertyFilePersistence = null;
        tempFile.delete();
        super.tearDown();
    }

    /**
     * <p>
     * Failure test case for method 'loadFile()'.<br>
     * The argument is invalid, <code>IOException</code> should be thrown.
     * </p>
     * @throws Exception to JUnit
     */
    public void testLoadFile_StringFile_IOException() throws Exception {
        try {
            propertyFilePersistence.loadFile(NAMESPACE, new File("test_files/failuretests/not_existed_files.properties"));
            fail("Test failure for loadFile() failed, IOException expected.");
        } catch (IOException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'loadFile()'.<br>
     * The argument is invalid, <code>ConfigurationParserException</code> should be thrown.
     * </p>
     * @throws Exception to JUnit
     */
    public void testLoadFile_StringFile_ConfigurationParserException() throws Exception {
        try {
            propertyFilePersistence.loadFile(NAMESPACE, new File("test_files/failuretests/parser_error.properties"));
            fail("Test failure for loadFile() failed, ConfigurationParserException expected.");
        } catch (ConfigurationParserException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'loadFile()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLoadFile_StringFile_Null1() throws Exception {
        try {
            propertyFilePersistence.loadFile(null, testFile);
            fail("Test failure for loadFile() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'loadFile()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLoadFile_StringFile_Null2() throws Exception {
        try {
            propertyFilePersistence.loadFile("com.topcoder.configuration.persistence", null);
            fail("Test failure for loadFile() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'loadFile()'.<br>
     * The argument is an empty <code>String</code>, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLoadFile_StringFile_Empty1() throws Exception {
        try {
            propertyFilePersistence.loadFile("      ", testFile);
            fail("Test failure for loadFile() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }




    /**
     * <p>
     * Failure test case for method 'saveFile()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testSaveFile_FileConfigurationObject_Null1()
        throws Exception {
        try {
            propertyFilePersistence.saveFile(null, configurationObject);
            fail("Test failure for saveFile() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'saveFile()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p> 
     * @throws Exception to JUnit
     */
    public void testSaveFile_FileConfigurationObject_Null2() throws Exception{
        try {
            propertyFilePersistence.saveFile(tempFile, null);
            fail("Test failure for saveFile() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }
}
