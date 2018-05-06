/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.persistence.failuretests;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.configuration.persistence.ConfigurationParserException;
import com.topcoder.configuration.persistence.InvalidConfigurationUpdateException;
import com.topcoder.configuration.persistence.NamespaceConflictException;
import com.topcoder.configuration.persistence.PropertyFilePersistence;
import com.topcoder.configuration.persistence.UnrecognizedFileTypeException;
import com.topcoder.configuration.persistence.UnrecognizedNamespaceException;
import com.topcoder.configuration.persistence.XMLFilePersistence;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * Test the functionality of class <code>ConfigurationFileManager</code>.
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
public class ConfigurationFileManagerFailureTests extends TestCase {
    /**
     * <p>
     * Represents an instance of <code>String</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private static final String NAMESPACE = "com.topcoder.configuration.persistence.config1";

    /**
     * <p>
     * Represents an instance of <code>String</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private static final String tmpFile = "test_files/failuretests/temp.xml";

    /**
     * <p>
     * Represents an instance of <code>ConfigurationFileManager</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private static ConfigurationFileManager configurationFileManager;

    /**
     * <p>
     * Represents an instance of <code>String</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private String fileName = null;

    /**
     * <p>
     * Represents an instance of <code>Map</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private Map persistenceMap = null;

    /**
     * <p>
     * Represents an instance of <code>Map</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private Map filesMap = null;

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
        persistenceMap = new HashMap();
        persistenceMap.put(".xml", new XMLFilePersistence());
        persistenceMap.put(".properties", new PropertyFilePersistence());

        ConfigurationObject persistenceConfig = new DefaultConfigurationObject("persistence");
        fileConfig = new DefaultConfigurationObject("files");
        configurationObject.addChild(fileConfig);
        configurationObject.addChild(persistenceConfig);
        filesMap = new HashMap();

        if (configurationFileManager == null) {
            configurationFileManager = new ConfigurationFileManager("test_files/failuretests/load.properties");
        }
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
        filesMap = null;
        fileName = null;
        persistenceMap = null;
        new File(tmpFile).delete();
        super.tearDown();
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is invalid, <code>IOException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_String_IOException()
        throws Exception {
        try {
            new ConfigurationFileManager("not_existed_file_name.xml");
            fail("Test failure for ConfigurationFileManager() failed, IOException expected.");
        } catch (IOException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is invalid, <code>ConfigurationParserException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_String_ConfigurationParserException()
        throws Exception {
        try {
            new ConfigurationFileManager("test_files/failuretests/parser_error.properties");
            fail("Test failure for ConfigurationFileManager() failed, ConfigurationParserException expected.");
        } catch (ConfigurationParserException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is invalid, <code>NamespaceConflictException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_String_NamespaceConflictException()
        throws Exception {
        try {
            new ConfigurationFileManager("test_files/failuretests/namespace_conflict.properties");
            fail("Test failure for ConfigurationFileManager() failed, NamespaceConflictException expected.");
        } catch (NamespaceConflictException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is invalid, <code>UnrecognizedFileTypeException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_String_UnrecognizedFileTypeException()
        throws Exception {
        try {
            new ConfigurationFileManager("test_files/failuretests/UnrecognizedFileType.urf");
            fail("Test failure for ConfigurationFileManager() failed, UnrecognizedFileTypeException expected.");
        } catch (UnrecognizedFileTypeException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_String_Null1()
        throws Exception {
        try {
            new ConfigurationFileManager((String) null);
            fail("Test failure for ConfigurationFileManager() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is an empty <code>String</code>, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_String_Empty1()
        throws Exception {
        try {
            new ConfigurationFileManager("   ");
            fail("Test failure for ConfigurationFileManager() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is invalid, <code>IOException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_StringMap_IOException()
        throws Exception {
        try {
            new ConfigurationFileManager("no_existed_file.xml", persistenceMap);
            fail("Test failure for ConfigurationFileManager() failed, IOException expected.");
        } catch (IOException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is invalid, <code>ConfigurationParserException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_StringMap_ConfigurationParserException()
        throws Exception {
        try {
            new ConfigurationFileManager("test_files/failuretests/parser_error.properties", persistenceMap);
            fail("Test failure for ConfigurationFileManager() failed, ConfigurationParserException expected.");
        } catch (ConfigurationParserException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is invalid, <code>NamespaceConflictException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_StringMap_NamespaceConflictException()
        throws Exception {
        try {
            new ConfigurationFileManager("test_files/failuretests/namespace_conflict.properties", persistenceMap);
            fail("Test failure for ConfigurationFileManager() failed, NamespaceConflictException expected.");
        } catch (NamespaceConflictException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is invalid, <code>UnrecognizedFileTypeException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_StringMap_UnrecognizedFileTypeException()
        throws Exception {
        try {
            new ConfigurationFileManager("test_files/failuretests/UnrecognizedFileType.urf", persistenceMap);
            fail("Test failure for ConfigurationFileManager() failed, UnrecognizedFileTypeException expected.");
        } catch (UnrecognizedFileTypeException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_StringMap_Null1()
        throws Exception {
        try {
            new ConfigurationFileManager((String) null, persistenceMap);
            fail("Test failure for ConfigurationFileManager() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception
     */
    public void testConfigurationFileManager_StringMap_Null2()
        throws Exception {
        try {
            new ConfigurationFileManager("test_files/failuretests/demo_config.xml", null);
            fail("Test failure for ConfigurationFileManager() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is an empty <code>String</code>, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_StringMap_Empty1()
        throws Exception {
        try {
            new ConfigurationFileManager("  ", persistenceMap);
            fail("Test failure for ConfigurationFileManager() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is invalid, <code>IOException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_ConfigurationObject_IOException()
        throws Exception {
        try {
            fileConfig.setPropertyValues("file1",
                new Object[] {
                    "com.topcoder.configuration.persistence", "test_files/failuretests/not_exist_file.properties"
                });
            new ConfigurationFileManager(configurationObject);
            fail("Test failure for ConfigurationFileManager() failed, IOException expected.");
        } catch (IOException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is invalid, <code>ConfigurationParserException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_ConfigurationObject_ConfigurationParserException()
        throws Exception {
        try {
            fileConfig.setPropertyValues("config",
                new String[] {"com.topcoder.configuration.persistence", "test_files/failuretests/parser_error.properties"});
            new ConfigurationFileManager(configurationObject);
            fail("Test failure for ConfigurationFileManager() failed, ConfigurationParserException expected.");
        } catch (ConfigurationParserException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is invalid, <code>UnrecognizedFileTypeException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_ConfigurationObject_UnrecognizedFileTypeException()
        throws Exception {
        try {
            fileConfig.setPropertyValues("config",
                new String[] {
                    "com.topcoder.configuration.persistence", "test_files/failuretests/UnrecognizedFileType.urf"
                });
            new ConfigurationFileManager(configurationObject);
            fail("Test failure for ConfigurationFileManager() failed, UnrecognizedFileTypeException expected.");
        } catch (UnrecognizedFileTypeException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_ConfigurationObject_Null1()
        throws Exception, NamespaceConflictException, UnrecognizedFileTypeException, IOException {
        try {
            new ConfigurationFileManager((ConfigurationObject) null);
            fail("Test failure for ConfigurationFileManager() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is invalid, <code>IOException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_Map_IOException()
        throws Exception {
        File file = new File("test_files/failuretests/no_existed_files.xml");

        try {
            filesMap.put("com.topcoder.configuration.persistence", file);
            new ConfigurationFileManager(filesMap);
            fail("Test failure for ConfigurationFileManager() failed, IOException expected.");
        } catch (IOException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is invalid, <code>ConfigurationParserException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_Map_ConfigurationParserException()
        throws Exception {
        try {
            filesMap.put("com.topcoder.configuration.persistence", new File("test_files/failuretests/parser_error.xml"));
            new ConfigurationFileManager(filesMap);
            fail("Test failure for ConfigurationFileManager() failed, ConfigurationParserException expected.");
        } catch (ConfigurationParserException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is invalid, <code>UnrecognizedFileTypeException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_Map_UnrecognizedFileTypeException()
        throws Exception {
        try {
            filesMap.put("com.topcoder.configuration.persistence",
                new File("test_files/failuretests/UnrecognizedFileType.urf"));
            new ConfigurationFileManager(filesMap);
            fail("Test failure for ConfigurationFileManager() failed, UnrecognizedFileTypeException expected.");
        } catch (UnrecognizedFileTypeException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_Map_Null1()
        throws Exception {
        try {
            new ConfigurationFileManager((Map) null);
            fail("Test failure for ConfigurationFileManager() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_Map_NullValue()
        throws Exception {
        try {
            filesMap.put("com.topcoder.configuration.persistence", null);
            new ConfigurationFileManager(filesMap);
            fail("Test failure for ConfigurationFileManager() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_Map_EmptyKey()
        throws Exception {
        try {
            filesMap.put("   ", new File("test_files/failuretests/demo_config.xml"));
            new ConfigurationFileManager(filesMap);
            fail("Test failure for ConfigurationFileManager() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_Map_NotStringKey()
        throws Exception {
        try {
            filesMap.put(new Object(), new File("test_files/failuretests/demo_config.xml"));
            new ConfigurationFileManager(filesMap);
            fail("Test failure for ConfigurationFileManager() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_Map_NotFileValue()
        throws Exception {
        try {
            filesMap.put("   ", "test_files/failuretests/demo_config.xml");
            new ConfigurationFileManager(filesMap);
            fail("Test failure for ConfigurationFileManager() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is invalid, <code>IOException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_MapMap_IOException()
        throws Exception {
        File file = new File("test_files/failuretests/no_existed_files.xml");

        try {
            filesMap.put("com.topcoder.configuration.persistence", file);
            new ConfigurationFileManager(filesMap, persistenceMap);
            fail("Test failure for ConfigurationFileManager() failed, IOException expected.");
        } catch (IOException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is invalid, <code>ConfigurationParserException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_MapMap_ConfigurationParserException()
        throws Exception {
        try {
            filesMap.put("com.topcoder.configuration.persistence", new File("test_files/failuretests/parser_error.xml"));
            new ConfigurationFileManager(filesMap, persistenceMap);
            fail("Test failure for ConfigurationFileManager() failed, ConfigurationParserException expected.");
        } catch (ConfigurationParserException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is invalid, <code>UnrecognizedFileTypeException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_MapMap_UnrecognizedFileTypeException()
        throws Exception {
        try {
            filesMap.put("com.topcoder.configuration.persistence",
                new File("test_files/failuretests/UnrecognizedFileType.urf"));
            new ConfigurationFileManager(filesMap, persistenceMap);
            fail("Test failure for ConfigurationFileManager() failed, UnrecognizedFileTypeException expected.");
        } catch (UnrecognizedFileTypeException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_MapMap_Null1()
        throws Exception {
        try {
            new ConfigurationFileManager((Map) null, persistenceMap);
            fail("Test failure for ConfigurationFileManager() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_MapMap_Null2()
        throws Exception {
        try {
            new ConfigurationFileManager(filesMap, null);
            fail("Test failure for ConfigurationFileManager() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_MapMap_NullValue()
        throws Exception {
        try {
            filesMap.put("com.topcoder.configuration.persistence", null);
            new ConfigurationFileManager(filesMap, persistenceMap);
            fail("Test failure for ConfigurationFileManager() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_MapMap_EmptyKey()
        throws Exception {
        try {
            filesMap.put("   ", new File("test_files/failuretests/demo_config.xml"));
            new ConfigurationFileManager(filesMap, persistenceMap);
            fail("Test failure for ConfigurationFileManager() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_MapMap_NotStringKey()
        throws Exception {
        try {
            filesMap.put(new Object(), new File("test_files/failuretests/demo_config.xml"));
            new ConfigurationFileManager(filesMap, persistenceMap);
            fail("Test failure for ConfigurationFileManager() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'loadFile()'.<br>
     * The argument is invalid, <code>ConfigurationParserException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLoadFile_StringString_ConfigurationParserException()
        throws Exception {
        try {
            configurationFileManager.loadFile("namespace", "test_files/failuretests/parser_error.properties");
            fail("Test failure for loadFile() failed, ConfigurationParserException expected.");
        } catch (ConfigurationParserException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'loadFile()'.<br>
     * The argument is invalid, <code>UnrecognizedFileTypeException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLoadFile_StringString_UnrecognizedFileTypeException()
        throws Exception {
        try {
            configurationFileManager.loadFile("namespace", "test_files/failuretests/UnrecognizedFileType.urf");
            fail("Test failure for loadFile() failed, UnrecognizedFileTypeException expected.");
        } catch (UnrecognizedFileTypeException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'loadFile()'.<br>
     * The argument is invalid, <code>NamespaceConflictException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testLoadFile_StringString_NamespaceConflictException()
        throws Exception {
        try {
            configurationFileManager.loadFile(NAMESPACE, "test_files/failuretests/config1.xml");
            fail("Test failure for loadFile() failed, NamespaceConflictException expected.");
        } catch (NamespaceConflictException e) {
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
    public void testLoadFile_StringString_Null1() throws Exception {
        try {
            configurationFileManager.loadFile(null, "test_files/failuretests/demo_config.xml");
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
    public void testLoadFile_StringString_Null2() throws Exception {
        try {
            configurationFileManager.loadFile(NAMESPACE, null);
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
    public void testLoadFile_StringString_Empty1() throws Exception {
        try {
            configurationFileManager.loadFile("     ", "test_files/failuretests/demo_config.xml");
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
    public void testLoadFile_StringString_Empty2() throws Exception {
        try {
            configurationFileManager.loadFile(NAMESPACE, " ");
            fail("Test failure for loadFile() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'refresh()'.<br>
     * The argument is invalid, <code>UnrecognizedNamespaceException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testRefresh_String_UnrecognizedNamespaceException()
        throws Exception {
        try {
            configurationFileManager.refresh("Unrecognized Namespace");
            fail("Test failure for refresh() failed, UnrecognizedNamespaceException expected.");
        } catch (UnrecognizedNamespaceException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'refresh()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testRefresh_String_Null1() throws Exception {
        try {
            configurationFileManager.refresh(null);
            fail("Test failure for refresh() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'refresh()'.<br>
     * The argument is an empty <code>String</code>, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testRefresh_String_Empty1() throws Exception {
        try {
            configurationFileManager.refresh("      ");
            fail("Test failure for refresh() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'getConfiguration()'.<br>
     * The argument is invalid, <code>UnrecognizedNamespaceException</code> should be thrown.
     * </p>
     */
    public void testGetConfiguration_String_UnrecognizedNamespaceException() {
        try {
            configurationFileManager.getConfiguration("some_unknown_namespace");
            fail("Test failure for getConfiguration() failed, UnrecognizedNamespaceException expected.");
        } catch (UnrecognizedNamespaceException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'getConfiguration()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetConfiguration_String_Null1() throws Exception {
        try {
            configurationFileManager.getConfiguration(null);
            fail("Test failure for getConfiguration() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'getConfiguration()'.<br>
     * The argument is an empty <code>String</code>, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetConfiguration_String_Empty1() throws Exception {
        try {
            configurationFileManager.getConfiguration("       ");
            fail("Test failure for getConfiguration() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'ConfigurationFileManager()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConfigurationFileManager_MapMap_NotFileValue()
        throws Exception {
        try {
            filesMap.put("   ", "test_files/failuretests/demo_config.xml");
            new ConfigurationFileManager(filesMap, persistenceMap);
            fail("Test failure for ConfigurationFileManager() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'createFile()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCreateFile_StringStringConfigurationObject_Null1()
        throws Exception {
        try {
            configurationFileManager.createFile(null, tmpFile, configurationObject);
            fail("Test failure for createFile() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'createFile()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCreateFile_StringStringConfigurationObject_Null2()
        throws Exception {
        String namespace = "topcoder.config";

        try {
            configurationFileManager.createFile(namespace, null, configurationObject);
            fail("Test failure for createFile() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'createFile()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCreateFile_StringStringConfigurationObject_Null3()
        throws Exception {
        String namespace = "topcoder.config";

        try {
            configurationFileManager.createFile(namespace, tmpFile, null);
            fail("Test failure for createFile() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'createFile()'.<br>
     * The argument is an empty <code>String</code>, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testCreateFile_StringStringConfigurationObject_Empty1()
        throws Exception {
        try {
            configurationFileManager.createFile("       ", tmpFile, configurationObject);

            fail("Test failure for createFile() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'createFile()'.<br>
     * The argument is an empty <code>String</code>, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testCreateFile_StringStringConfigurationObject_Empty2()
        throws Exception {
        String namespace = "topcoder.config";

        try {
            configurationFileManager.createFile(namespace, "     ", configurationObject);

            fail("Test failure for createFile() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'saveConfiguration()'.<br>
     * The argument is invalid, <code>InvalidConfigurationUpdateException</code> should be thrown.
     * </p>
     */
    public void testSaveConfiguration_Map_InvalidConfigurationUpdateException()
        throws Exception {
        Map configMap = new HashMap();
        configMap.put("config", configurationObject);
        fileConfig.setPropertyValues("config", new Object[] {NAMESPACE, "test_files/failuretests/no_existed_files.xml"});

        try {
            configurationFileManager.saveConfiguration(configMap);
            fail("Test failure for saveConfiguration() failed, InvalidConfigurationUpdateException expected.");
        } catch (InvalidConfigurationUpdateException e) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'saveConfiguration()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testSaveConfiguration_Map_Null1() throws Exception {
        try {
            configurationFileManager.saveConfiguration(null);
            fail("Test failure for saveConfiguration() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'saveConfiguration()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testSaveConfiguration_Map_NullValue() throws Exception {
        try {
            Map configMap = new HashMap();
            configMap.put("config", null);
            configurationFileManager.saveConfiguration(configMap);
            fail("Test failure for saveConfiguration() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'saveConfiguration()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testSaveConfiguration_StringConfigurationObject_Null1()
        throws Exception {
        try {
            configurationFileManager.saveConfiguration(null, configurationObject);
            fail("Test failure for saveConfiguration() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'saveConfiguration()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testSaveConfiguration_StringConfigurationObject_Null2()
        throws Exception {
        try {
            configurationFileManager.saveConfiguration("namespace", null);
            fail("Test failure for saveConfiguration() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'saveConfiguration()'.<br>
     * The argument is an empty <code>String</code>, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testSaveConfiguration_StringConfigurationObject_Empty1()
        throws Exception {
        try {
            configurationFileManager.saveConfiguration("  ", configurationObject);
            fail("Test failure for saveConfiguration() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }
}
