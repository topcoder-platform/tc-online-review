/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import junit.framework.TestCase;

/**
 * Unit test for class <c>ConfigurationFileManager</c>.
 *
 * @author rainday
 * @version 1.0
 */
public class ConfigurationFileManagerUnitTests extends TestCase {

    /**
     * Responses the ConfigurationFileManager instance used for tests.
     */
    private ConfigurationFileManager manager = null;

    /**
     * Response the test properties file to load.
     */
    private String propertiesFile = "test_files/test.properties";

    /**
     * Response the test properties file to load.
     */
    private String propertiesFileWithSpace = "test_files/a b/test.properties";

    /**
     * config file for this component.
     */
    private String perloadFile = "test_files/preload.properties";

    /**
     * uncognized config file for this component.
     */
    private String unrecognizedFile = "test_files/preload";

    /**
     * temp file to write.
     */
    private String tempFile = "test_files/temp.properties";

    /**
     * conflict config file for this component.
     */
    private String conflictFile = "test_files/conflict.properties";

    /**
     * error per-load file for tests.
     */
    private String errorPreloadFile = "test_files/errorpreload.properties";

    /**
     * Response the test properties resource to load.
     */
    private String propertiesResource = "resource/test.properties";

    /**
     * Response the test properties resource to load.
     */
    private String propertiesResourceWithSpace = "resource/a b/test.properties";

    /**
     * config resource for this component.
     */
    private String perloadResource = "resource/preload.properties";

    /**
     * uncognized config resource for this component.
     */
    private String unrecognizedResource = "resource/preload";

    /**
     * temp resource to write.
     */
    private String tempResource = "resource/temp.properties";

    /**
     * conflict config resource for this component.
     */
    private String conflictResource = "resource/conflict.properties";

    /**
     * error per-load resource for tests.
     */
    private String errorPreloadResource = "resource/errorpreload.properties";

    /**
     * ConfigurationObject obj used to for constructor tests.
     */
    private ConfigurationObject ctorObj = new DefaultConfigurationObject("ctor");

    /**
     * Responses the persistence map used for tests.
     */
    private Map persistenceMap = new HashMap();

    /**
     * Response the files map used for tests.
     */
    private Map filesMap = new HashMap();

    /**
     * create ConfigurationFileManager instance for tests.
     *
     * @throws Exception
     *             any exception to junit
     */
    protected void setUp() throws Exception {
        persistenceMap.clear();
        persistenceMap.put(".xml", new XMLFilePersistence());
        persistenceMap.put(".properties", new PropertyFilePersistence());
        filesMap.clear();
        FileWriter writer = new FileWriter(propertiesFile);
        writer.write("ListDelimiter=;\n\n");

        writer.write("a:valuea\n");
        writer.write("f valuef1;valuef2\n");
        writer.write("b=valueb\n");
        writer.write("e = valuee1\n");
        writer.write("e= valuee2\n\n");
        writer.write("h.g=valueg\n");
        writer.write("b.c=valuec\n\n");
        writer.write("h.g.i=valuei\n");
        writer.close();
        filesMap.put("com.tc", new File(propertiesFile));
        ctorObj.clearChildren();
        ConfigurationObject filesObj = new DefaultConfigurationObject("files");
        filesObj.setPropertyValues("file1", new Object[] { "com.topcoder.abc", propertiesFile });
        ctorObj.addChild(filesObj);
        ConfigurationObject persistenceObj = new DefaultConfigurationObject("persistence");
        ctorObj.addChild(persistenceObj);

        persistenceObj.setPropertyValues(".xml", new Object[] { ".xml", new XMLFilePersistence() });
        persistenceObj.setPropertyValues(".properties", new Object[] { ".properties", new PropertyFilePersistence() });

        manager = new ConfigurationFileManager(perloadFile);
    }

    /**
     * delete generated temp files.
     *
     * @throws Exception
     *             any exception to junit
     */
    protected void tearDown() throws Exception {
        new File(tempFile).delete();
    }

    /**
     * Accuracy test for the constructor ConfigurationFileManager(), new instance should be created.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct1() throws Exception {
        assertNotNull("Create ConfigurationFileManager error", new ConfigurationFileManager());
    }

    /**
     * Accuracy test for the constructor ConfigurationFileManager(String), new instance should be created.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct2() throws Exception {
        assertNotNull("Create ConfigurationFileManager error", new ConfigurationFileManager(perloadFile));
    }

    /**
     * Test the constructor ConfigurationFileManager(String) with null file, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct2WithNull() throws Exception {
        try {
            new ConfigurationFileManager((String) null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(String) with empty file, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct2WithEmptyFile() throws Exception {
        try {
            new ConfigurationFileManager("\t \n \r");
            fail("IllegalArgumentException should be thrown when file is empty");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(String) with error file, ConfigurationParserException should be
     * thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct2WithErrorFile() throws Exception {
        try {
            new ConfigurationFileManager(errorPreloadFile);
            fail("ConfigurationParserException should be thrown");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(String) with non-exist file, IOException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct2WithNonExistFile() throws Exception {
        try {
            new ConfigurationFileManager("non-exists.xml");
            fail("IOException should be thrown when the file doesn't exist.");
        } catch (IOException ioe) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(String) with unrecognized file, UnrecognizedFileTypeException
     * should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct2WithunrecognizedFile() throws Exception {
        try {
            new ConfigurationFileManager(unrecognizedFile);
            fail("UnrecognizedFileTypeException should be thrown when the fils is unrecognized");
        } catch (UnrecognizedFileTypeException ufte) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(String) with conflict file, NamespaceConflictException should be
     * thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct2WithConflictFile() throws Exception {
        try {
            new ConfigurationFileManager(conflictFile);
            fail("NamespaceConflictException should be thrown when the fils has conflict namespace");
        } catch (NamespaceConflictException nce) {
            // ok
        }
    }

    /**
     * Accuracy test for the constructor ConfigurationFileManager(String, Map), new instance should be created.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct3() throws Exception {
        assertNotNull("Create ConfigurationFileManager error",
            new ConfigurationFileManager(perloadFile, persistenceMap));
    }

    /**
     * Test the constructor ConfigurationFileManager(String, Map) with null file, IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct3WithNull() throws Exception {
        try {
            new ConfigurationFileManager((String) null, persistenceMap);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(String, Map) with empty file, IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct3WithEmptyFile() throws Exception {
        try {
            new ConfigurationFileManager("\t \n \r", persistenceMap);
            fail("IllegalArgumentException should be thrown when file is empty");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(String, Map) with error file, ConfigurationParserException should
     * be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct3WithErrorFile() throws Exception {
        try {
            new ConfigurationFileManager(errorPreloadFile, persistenceMap);
            fail("ConfigurationParserException should be thrown");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(String, Map) with non-exist file, IOException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct3WithNonExistFile() throws Exception {
        try {
            new ConfigurationFileManager("non-exists.xml", persistenceMap);
            fail("IOException should be thrown when the file doesn't exist.");
        } catch (IOException ioe) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(String, Map) with unrecognized file, UnrecognizedFileTypeException
     * should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct3WithunrecognizedFile() throws Exception {
        try {
            new ConfigurationFileManager(unrecognizedFile, persistenceMap);
            fail("UnrecognizedFileTypeException should be thrown when the fils is unrecognized");
        } catch (UnrecognizedFileTypeException ufte) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(String, Map) with conflict file, NamespaceConflictException should
     * be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct3WithConflictFile() throws Exception {
        try {
            new ConfigurationFileManager(conflictFile, persistenceMap);
            fail("NamespaceConflictException should be thrown when the fils has conflict namespace");
        } catch (NamespaceConflictException nce) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(String, Map) when the Map is null. IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct3WithNullMap() throws Exception {
        try {
            new ConfigurationFileManager(conflictFile, null);
            fail("IllegalArgumentException should be thrown when the map is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(String, Map) when the Map is empty. IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct3WithEmptyMap() throws Exception {
        try {
            persistenceMap.clear();
            new ConfigurationFileManager(conflictFile, persistenceMap);
            fail("IllegalArgumentException should be thrown when the map is empty.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(String, Map) when the Map is contain non-string key.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct3WithErrorMapKey() throws Exception {
        try {
            persistenceMap.put(new Integer(1), new Integer(1));
            new ConfigurationFileManager(conflictFile, persistenceMap);
            fail("IllegalArgumentException should be thrown when the map has non-string key.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(String, Map) when value in the map is not ConfigurationPersistence
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct3WithErrorMapValue() throws Exception {
        try {
            persistenceMap.put(".xml", new Integer(1));
            new ConfigurationFileManager(conflictFile, persistenceMap);
            fail("IllegalArgumentException should be thrown when value in the map is error.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(ConfigurationObject), new instance should be created.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct4() throws Exception {
        assertNotNull("Create ConfigurationFileManager incorrectly", new ConfigurationFileManager(ctorObj));
    }

    /**
     * Test the constructor ConfigurationFileManager(ConfigurationObject), new instance should be created.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct4NullPersistence() throws Exception {
        ctorObj.removeChild("persistence");
        assertNotNull("Create ConfigurationFileManager incorrectly", new ConfigurationFileManager(ctorObj));
    }

    /**
     * Test the constructor ConfigurationFileManager(ConfigurationObject) with error persistence key.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct4ErrorPersistenceKey() throws Exception {
        ConfigurationObject obj = ctorObj.getChild("persistence");
        obj.setPropertyValues("x", new Object[] { new Integer(1), new XMLFilePersistence() });
        try {
            new ConfigurationFileManager(ctorObj);
            fail("IllegalArgumentException should be thrown when the persistence key is not string");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(ConfigurationObject) with error persistence value.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct4ErrorPersistenceValue() throws Exception {
        ConfigurationObject obj = ctorObj.getChild("persistence");
        obj.setPropertyValues("x", new Object[] { ".xml", new Integer(1) });
        try {
            new ConfigurationFileManager(ctorObj);
            fail("IllegalArgumentException should be thrown when the persistence value is not Persistence");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(ConfigurationObject) when the ConfigurationObject is null.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct4WithNullObj() throws Exception {
        try {
            new ConfigurationFileManager((ConfigurationObject) null);
            fail("IllegalArgumentException should be thrown when the ConfigurationObject is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(ConfigurationObject) when the persistence has error format.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct4WithErrorPersistenceFormat() throws Exception {
        try {
            ctorObj.getChild("persistence").setPropertyValue(".xml", new Integer(1));
            new ConfigurationFileManager(ctorObj);
            fail("IllegalArgumentException should be thrown when the format of persistence is erro.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(ConfigurationObject) when the persistence has error value.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct4WithErrorPersistenceValue() throws Exception {
        try {
            ctorObj.getChild("persistence").setPropertyValue(".xml", new Object[] { new Integer(1), new Integer(1) });
            new ConfigurationFileManager(ctorObj);
            fail("IllegalArgumentException should be thrown when the value of persistence is error.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(ConfigurationObject) when the persistence has error value.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct4WithErrorPersistenceValue2() throws Exception {
        try {
            ctorObj.getChild("persistence").setPropertyValue(".xml", new Object[] { ".xml", new Integer(1) });
            new ConfigurationFileManager(ctorObj);
            fail("IllegalArgumentException should be thrown when the value of persistence is error.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(ConfigurationObject) when the config doesn't have files child
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct4WithNoneFilesChild() throws Exception {
        try {
            ctorObj.removeChild("files");
            new ConfigurationFileManager(ctorObj);
            fail("IllegalArgumentException should be thrown when the files child doesn't exist.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(ConfigurationObject) with error file, ConfigurationParserException
     * should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct4WithErrorFile() throws Exception {
        try {
            ctorObj.getChild("files").setPropertyValues("file1", new Object[] { "namespace1", "test_files/error.xml" });
            new ConfigurationFileManager(ctorObj);
            fail("ConfigurationParserException should be thrown when the file is error format.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(ConfigurationObject) with non-exist file, IOException should be
     * thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct4WithNonExistFile() throws Exception {
        try {
            ctorObj.getChild("files").setPropertyValues("file1", new Object[] { "namespace1", "non-exists.xml" });
            new ConfigurationFileManager(ctorObj);
            fail("ConfigurationParserException should be thrown when the file doesn't exist.");
        } catch (IOException ioe) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(ConfigurationObject) with unrecognized file,
     * UnrecognizedFileTypeException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct4WithunrecognizedFile() throws Exception {
        try {
            ctorObj.getChild("files").setPropertyValues("file1", new Object[] { "namespace1", unrecognizedFile });
            new ConfigurationFileManager(ctorObj);
            fail("UnrecognizedFileTypeException should be thrown when the file is unrecognized.");
        } catch (UnrecognizedFileTypeException ufte) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(Map), new instance should be created.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct5() throws Exception {
        assertNotNull("Create ConfigurationFileManager incorrectly", new ConfigurationFileManager(filesMap));
    }

    /**
     * Test the constructor ConfigurationFileManager(Map) when the Map is null. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct5WithNullMap() throws Exception {
        try {
            new ConfigurationFileManager((Map) null);
            fail("IllegalArgumentException should be thrown when the Map is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(Map) with empty namespace. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct5WithEmptyNamespace() throws Exception {
        try {
            filesMap.put("  ", new File("test_files/test5.properties"));
            new ConfigurationFileManager(filesMap);
            fail("IllegalArgumentException should be thrown when the namespace is empty.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(Map) with incorrect namespace. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct5WithIncorrectNamespace() throws Exception {
        try {
            filesMap.put(new Integer(1), new File(propertiesFile));
            new ConfigurationFileManager(filesMap);
            fail("IllegalArgumentException should be thrown when the namespace is empty.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(Map) with incorrect File value. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct5WithIncorrectFileValue() throws Exception {
        try {
            filesMap.put("com.tc", new Integer(1));
            new ConfigurationFileManager(filesMap);
            fail("IllegalArgumentException should be thrown when the file value is incorrect.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(Map) with error file, ConfigurationParserException should be
     * thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct5WithErrorFile() throws Exception {
        try {
            filesMap.put("com.tc", new File("test_files/error.xml"));
            new ConfigurationFileManager(filesMap);
            fail("ConfigurationParserException should be thrown when the file is incorrect formatting.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(Map) with non-exist file, IOException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct5WithNonExistFile() throws Exception {
        try {
            filesMap.put("com.tc", new File("test_files/non-exist.xml"));
            new ConfigurationFileManager(filesMap);
            fail("ConfigurationParserException should be thrown when the file doesn't exist.");
        } catch (IOException ioe) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(Map) with unrecognized file, UnrecognizedFileTypeException should
     * be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct5WithunrecognizedFile() throws Exception {
        try {
            filesMap.put("com.tc", new File(unrecognizedFile));
            new ConfigurationFileManager(filesMap);
            fail("UnrecognizedFileTypeException should be thrown when the file is unrecognized.");
        } catch (UnrecognizedFileTypeException ufte) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(Map, Map), new instance should be created.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct6() throws Exception {
        assertNotNull("Create ConfigurationFileManager incorrectly", new ConfigurationFileManager(filesMap,
            persistenceMap));
    }

    /**
     * Test the constructor ConfigurationFileManager(Map, Map) when the Map is null. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct6WithNullMap() throws Exception {
        try {
            new ConfigurationFileManager((Map) null, persistenceMap);
            fail("IllegalArgumentException should be thrown when the Map is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(Map, Map) with empty namespace. IllegalArgumentException should be
     * thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct6WithEmptyNamespace() throws Exception {
        try {
            filesMap.put("  ", new File(propertiesFile));
            new ConfigurationFileManager(filesMap, persistenceMap);
            fail("IllegalArgumentException should be thrown when the namespace is empty.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(Map, Map) with incorrect namespace. IllegalArgumentException should
     * be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct6WithIncorrectNamespace() throws Exception {
        try {
            filesMap.put(new Integer(1), new File(propertiesFile));
            new ConfigurationFileManager(filesMap, persistenceMap);
            fail("IllegalArgumentException should be thrown when the namespace is empty.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(Map, Map) with incorrect File value. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct6WithIncorrectFileValue() throws Exception {
        try {
            filesMap.put("com.tc", new Integer(1));
            new ConfigurationFileManager(filesMap, persistenceMap);
            fail("IllegalArgumentException should be thrown when the file value is incorrect.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(Map, Map) with error file, ConfigurationParserException should be
     * thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct6WithErrorFile() throws Exception {
        try {
            filesMap.put("com.tc", new File("test_files/error.xml"));
            new ConfigurationFileManager(filesMap, persistenceMap);
            fail("ConfigurationParserException should be thrown when the file is incorrect formatting.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(Map, Map) with non-exist file, IOException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct6WithNonExistFile() throws Exception {
        try {
            filesMap.put("com.tc", new File("test_files/non-exist.xml"));
            new ConfigurationFileManager(filesMap, persistenceMap);
            fail("ConfigurationParserException should be thrown when the file doesn't exist.");
        } catch (IOException ioe) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(Map, Map) with unrecognized file, UnrecognizedFileTypeException
     * should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct6WithunrecognizedFile() throws Exception {
        try {
            filesMap.put("com.tc", new File(unrecognizedFile));
            new ConfigurationFileManager(filesMap, persistenceMap);
            fail("UnrecognizedFileTypeException should be thrown when the file is unrecognized.");
        } catch (UnrecognizedFileTypeException ufte) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(Map, Map) when the persistence Map is null.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct6WithNullPersistenceMap() throws Exception {
        try {
            new ConfigurationFileManager(filesMap, null);
            fail("IllegalArgumentException should be thrown when the map is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(String, Map) when the persistence Map is empty.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct6WithEmptyPersistenceMap() throws Exception {
        try {
            persistenceMap.clear();
            new ConfigurationFileManager(filesMap, persistenceMap);
            fail("IllegalArgumentException should be thrown when the map is empty.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(String, Map) when the persistence Map is contain non-string key.
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct6WithErrorPersistenceMapKey() throws Exception {
        try {
            persistenceMap.put(new Integer(1), new Integer(1));
            new ConfigurationFileManager(conflictFile, persistenceMap);
            fail("IllegalArgumentException should be thrown when the map has non-string key.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the constructor ConfigurationFileManager(String, Map) when value in the persistence map is not
     * ConfigurationPersistence IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testConstruct6WithErrorPersistenceMapValue() throws Exception {
        try {
            persistenceMap.put(".xml", new Integer(1));
            new ConfigurationFileManager(filesMap, persistenceMap);
            fail("IllegalArgumentException should be thrown when value in the map is error.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Accuracy test for the method getConfiguration(), all loaded files should be returned.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testGetConfiguration() throws Exception {
        ConfigurationFileManager mgr = new ConfigurationFileManager(filesMap);
        Map configs = mgr.getConfiguration();
        for (Iterator iter = configs.keySet().iterator(); iter.hasNext();) {
            assertTrue("GetConfiguration incorrectly.", filesMap.containsKey(iter.next()));
        }
    }

    /**
     * Accuracy test for the method getConfiguration(String), all loaded files should be returned.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testGetConfigurationString() throws Exception {
        ConfigurationFileManager mgr = new ConfigurationFileManager(filesMap);
        for (Iterator iter = filesMap.keySet().iterator(); iter.hasNext();) {
            String namespace = (String) iter.next();
            assertNotNull("GetConfiguration incorrectly.", mgr.getConfiguration(namespace));
        }
    }

    /**
     * Test for the method getConfiguration(String) with null namespace. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testGetConfigurationNullNamespace() throws Exception {
        try {
            manager.getConfiguration(null);
            fail("IllegalArgumentException should be thrown when the namespace is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test for the method getConfiguration(String) with empty namespace. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testGetConfigurationEmptyNamespace() throws Exception {
        try {
            manager.getConfiguration("\t \n \r");
            fail("IllegalArgumentException should be thrown when the namespace is empty.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test for the method getConfiguration(String) with unrecognized namespace. UnrecognizedNamespaceException should
     * be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testGetConfigurationunrecognizedNamespace() throws Exception {
        try {
            manager.getConfiguration("abc.def.ghi");
            fail("IllegalArgumentException should be thrown when the namespace is unrecognized.");
        } catch (UnrecognizedNamespaceException ufte) {
            // ok
        }
    }

    /**
     * Accuracy test for the method loadFile(String, String), the namespace should be save in configurationMap.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFile() throws Exception {
        String namespace = "com.topcoder.xxx";
        manager.loadFile(namespace, propertiesFile);
        ConfigurationObject obj = manager.getConfiguration(namespace);
        assertEquals("LoadFile error", namespace, obj.getName());
        ConfigurationObject defaultObj = obj.getChild("default");
        // test property in default namespace
        assertEquals("LoadFile error", "valuea", defaultObj.getPropertyValue("a"));
        assertEquals("LoadFile error", "valueb", defaultObj.getPropertyValue("b"));
        // test nested property in default namespace
        assertEquals("LoadFile error", "valuec", defaultObj.getChild("b").getPropertyValue("c"));

        // test multi-values
        Object[] evalues = defaultObj.getPropertyValues("e");
        assertEquals("LoadFile error", "valuee1", evalues[0]);
        assertEquals("LoadFile error", "valuee2", evalues[1]);
        Object[] fvalues = defaultObj.getPropertyValues("f");
        assertEquals("LoadFile error", "valuef1", fvalues[0]);
        assertEquals("LoadFile error", "valuef2", fvalues[1]);

        // test nested property in custom namespace
        assertEquals("LoadFile error", "valueg", defaultObj.getChild("h").getPropertyValue("g"));
        assertEquals("LoadFile error", "valuei", defaultObj.getChild("h").getChild("g").getPropertyValue("i"));
    }

    /**
     * Test the method <c>loadFile(String, String)</c> with null file, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileNullFile() throws Exception {
        try {
            manager.loadFile("a", null);
            fail("IllegalArgumentException should be thrown when the file is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, String)</c> with null namespace, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileNullNamespace() throws Exception {
        try {
            manager.loadFile(null, perloadFile);
            fail("IllegalArgumentException should be thrown when the namespace is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, String)</c> with empty namespace, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileEmptyNamespace() throws Exception {
        try {
            manager.loadFile("\t \r \n", perloadFile);
            fail("IllegalArgumentException should be thrown when the namespace is empty.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, String)</c> with empty file, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileEmptyFile() throws Exception {
        try {
            manager.loadFile("com.topcoder", "\t \r \n");
            fail("IllegalArgumentException should be thrown when the file is empty.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, String)</c> with conflict namespace, NamespaceConflictException should be
     * thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileConfilictNamespace() throws Exception {
        try {
            manager.loadFile("com.topcoder", perloadFile);
            manager.loadFile("com.topcoder", perloadFile);
            fail("NamespaceConflictException should be thrown when the file is empty.");
        } catch (NamespaceConflictException e) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, String)</c> with uncoginzed file, UnrecognizedFileTypeException should be
     * thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileunrecognizedFile() throws Exception {
        try {
            manager.loadFile("c", unrecognizedFile);
            fail("UnrecognizedFileTypeException should be thrown when the file is unrecognized.");
        } catch (UnrecognizedFileTypeException e) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, String)</c> with uncoginzed file, ConfigurationParserException should be
     * thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileErrorFile() throws Exception {
        try {
            manager.loadFile("error", "test_files/error.xml");
            fail("ConfigurationParserException should be thrown when the file is empty.");
        } catch (ConfigurationParserException e) {
            // ok
        }
    }

    /**
     * Accuracy test for the method createFile(String, String, ConfiguraitonObject). all properties in the config should
     * be written to the fill
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testCreateFile() throws Exception {
        String namespace = "newNamespace";
        ConfigurationObject obj = new PropertyFilePersistence().loadFile(namespace, new File(perloadFile));
        manager.createFile(namespace, tempFile, obj);
        assertNotNull("create file error", manager.getConfiguration(namespace));
        BufferedReader reader = new BufferedReader(new FileReader(tempFile));
        String line = reader.readLine();
        String content = "";
        while (line != null) {
            content += line;
            line = reader.readLine();
        }
        reader.close();
        System.out.print(content);
        assertTrue("Create file error", content.indexOf("com.topcoder.abc") > -1);
        assertTrue("Create file error", content.indexOf("com.topcoder.def") > -1);
    }

    /**
     * Test for the method createFile(String, String, ConfiguraitonObject). when namespace is null,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testCreateFileNullNamespace() throws Exception {
        try {
            manager.createFile(null, tempFile, new DefaultConfigurationObject("t"));
            fail("IllegalArgumentException should be thrown when the namespace is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test for the method createFile(String, String, ConfiguraitonObject). when namespace is empty,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testCreateFileEmptyNamespace() throws Exception {
        try {
            manager.createFile("\t \n \r", tempFile, new DefaultConfigurationObject("t"));
            fail("IllegalArgumentException should be thrown when the namespace is empty.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test for the method createFile(String, String, ConfiguraitonObject). when namespace is conflict,
     * NamespaceConflictException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testCreateFileConflictNamespace() throws Exception {
        try {
            manager.createFile("com.topcoder.abc", tempFile, new DefaultConfigurationObject("t"));
            fail("NamespaceConflictException should be thrown when the namespace is conflict.");
        } catch (NamespaceConflictException e) {
            // ok
        }
    }

    /**
     * Test for the method createFile(String, String, ConfiguraitonObject). when file is null, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testCreateFileNullFile() throws Exception {
        try {
            manager.createFile("x", null, new DefaultConfigurationObject("t"));
            fail("IllegalArgumentException should be thrown when the file is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test for the method createFile(String, String, ConfiguraitonObject). when file is empty, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testCreateFileEmptyFile() throws Exception {
        try {
            manager.createFile("x", "\t \n \r", new DefaultConfigurationObject("t"));
            fail("IllegalArgumentException should be thrown when the file is empty.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test for the method createFile(String, String, ConfiguraitonObject). when file is unrecognized,
     * UnrecognizedFileTypeException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testCreateFileUnrecognizedFile() throws Exception {
        try {
            manager.createFile("x", "x.abc", new DefaultConfigurationObject("t"));
            fail("UnrecognizedFileTypeException should be thrown when the file is null.");
        } catch (UnrecognizedFileTypeException e) {
            // ok
        }
    }

    /**
     * Test for the method createFile(String, String, ConfiguraitonObject). when config is null,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testCreateFileNullConfig() throws Exception {
        try {
            manager.createFile("x", tempFile, null);
            fail("IllegalArgumentException should be thrown when the config is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Accuracy test for the method refresh(). all properties in the config should be reloaded.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testRefresh() throws Exception {
        FileWriter writer = new FileWriter(propertiesFile);
        writer.write("tt=cc");
        writer.close();
        manager.refresh("com.topcoder.abc");
        ConfigurationObject defaultObj = manager.getConfiguration("com.topcoder.abc").getChild("default");
        assertNotNull("Refresh configuration manager error.", defaultObj);
        // new value should be set
        assertEquals("Refresh error.", "cc", defaultObj.getPropertyValue("tt"));
        // elder value should be removed
        assertNull("Refresh error", defaultObj.getPropertyValue("a"));
    }

    /**
     * Accuracy test for the method refresh(String). file corresponding to the namespace should be reloaded.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testRefreshString() throws Exception {
        FileWriter writer = new FileWriter(propertiesFile);
        writer.write("fresh=abc");
        writer.close();
        manager.refresh();
        ConfigurationObject defaultObj = manager.getConfiguration("com.topcoder.abc").getChild("default");
        assertNotNull("Refresh configuration manager error.", defaultObj);
        // new value should be set
        assertEquals("Refresh error.", "abc", defaultObj.getPropertyValue("fresh"));
        // elder value should be removed
        assertNull("Refresh error", defaultObj.getPropertyValue("a"));
        manager.refresh("com.topcoder.abc");
    }

    /**
     * Test for the method refresh(String) with null namespace, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testRefreshStringNull() throws Exception {
        try {
            manager.refresh(null);
            fail("IllegalArgumentException should be thrown when the namespace is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test for the method refresh(String) with empty namespace, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testRefreshStringEmpty() throws Exception {
        try {
            manager.refresh("\t \r \n");
            fail("IllegalArgumentException should be thrown when the namespace is empty.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test for the method refresh(String) with unrecongnized namespace, UnrecognizedNamespaceException should be
     * thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testRefreshStringUnrecongnized() throws Exception {
        try {
            manager.refresh("unrecongnized");
            fail("UnrecognizedNamespaceException should be thrown when the namespace is unrecongnized.");
        } catch (UnrecognizedNamespaceException e) {
            // ok
        }
    }

    /**
     * Accuracy test for the method saveConfiguration(Map). file corresponding to the namespace should be reloaded.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testSaveConfiguration() throws Exception {
        Map map = new HashMap();
        ConfigurationObject obj = manager.getConfiguration("com.topcoder.abc");
        obj.getChild("default").clearProperties();
        obj.getChild("default").setPropertyValues("xxx", new Object[] { "value1", "value2" });
        map.put("com.topcoder.abc", obj);
        manager.saveConfiguration(map);
        BufferedReader reader = new BufferedReader(new FileReader(propertiesFile));
        assertEquals("Save configuration incorrectly", "xxx=value1;value2", reader.readLine());
        reader.close();
    }

    /**
     * Test for the method saveConfiguration(Map) with null map. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testSaveConfigurationNull() throws Exception {
        try {
            manager.saveConfiguration(null);
            fail("IllegalArgumentException should be thrown when the map is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test for the method saveConfiguration(Map) with error keys. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testSaveConfigurationErrorKeys() throws Exception {
        try {
            Map errorKeys = new HashMap();
            errorKeys.put(new Integer(1), new DefaultConfigurationObject("a"));
            manager.saveConfiguration(errorKeys);
            fail("IllegalArgumentException should be thrown when the key is error.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test for the method saveConfiguration(Map) with error value. IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testSaveConfigurationErrorValues() throws Exception {
        try {
            Map errorValues = new HashMap();
            errorValues.put("abc", null);
            manager.saveConfiguration(errorValues);
            fail("IllegalArgumentException should be thrown when the values is error.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Accuracy test for the method saveConfiguration(String, ConfigurationObject).
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testSaveConfiguration2() throws Exception {
        ConfigurationObject obj = manager.getConfiguration("com.topcoder.abc");
        obj.getChild("default").setPropertyValue("newKey", "abc");
        manager.saveConfiguration("com.topcoder.abc", obj);
        BufferedReader reader = new BufferedReader(new FileReader(propertiesFile));
        String line = reader.readLine();
        while (line != null) {
            if (line.startsWith("newKey")) {
                assertEquals("Save configuration incorrectly", "newKey=abc", line);
            }
            line = reader.readLine();
        }
        reader.close();
    }

    /**
     * Test for the method saveConfiguration(String, ConfigurationObject) with null namespace. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testSaveConfiguration2NullNamespace() throws Exception {
        try {
            manager.saveConfiguration(null, new DefaultConfigurationObject("a"));
            fail("IllegalArgumentException should be thrown when the namespace is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test for the method saveConfiguration(String, ConfigurationObject) with empty namespace. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testSaveConfiguration2EmptyNamespace() throws Exception {
        try {
            manager.saveConfiguration("\t \n \r", new DefaultConfigurationObject("a"));
            fail("IllegalArgumentException should be thrown when the namespace is empty.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test for the method saveConfiguration(String, ConfigurationObject) with unrecognized namespace.
     * InvalidConfigurationUpdateException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testSaveConfiguration2UnrecognizedNamespace() throws Exception {
        try {
            manager.saveConfiguration("non-exist", new DefaultConfigurationObject("a"));
            fail("InvalidConfigurationUpdateException should be thrown when the namespace doesn't exist.");
        } catch (InvalidConfigurationUpdateException e) {
            // ok
        }
    }

    /**
     * Test for the method saveConfiguration(String, ConfigurationObject) when the namespace is out of data namespace.
     * ConfigurationUpdateConflictException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testSaveConfiguration2OutofDate() throws Exception {
        try {
            FileWriter writer = new FileWriter(propertiesFile);
            writer.write("fresh=abc");
            writer.close();
            manager.saveConfiguration("com.topcoder.abc", manager.getConfiguration("com.topcoder.abc"));
            fail("ConfigurationUpdateConflictException should be thrown when the config file is out of date");
        } catch (ConfigurationUpdateConflictException e) {
            // ok
        }
    }

    /**
     * Test for the method saveConfiguration(String, ConfigurationObject) when the config file is error formatting
     * ConfigurationParserException( should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testSaveConfiguration2ErrorFile() throws Exception {
        try {
            FileWriter writer = new FileWriter(propertiesFile);
            writer.write("xxx...");
            writer.close();
            manager.saveConfiguration("com.topcoder.abc", manager.getConfiguration("com.topcoder.abc"));
            fail("ConfigurationParserException( should be thrown when the config file is out of date");
        } catch (ConfigurationParserException e) {
            // ok
        }
    }

    /**
     * Test for the method saveConfiguration(String, ConfigurationObject) with null config. IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     *
     */
    public void testSaveConfiguration2NullConfig() throws Exception {
        try {
            manager.saveConfiguration("com.topcoder.abc", null);
            fail("InvalidConfigurationUpdateException should be thrown when the config is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Accuracy test for the method loadFile(String, String), the namespace should be save in configurationMap. And in
     * this case, the path contains whitespaces.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileWhenSpacesInPath() throws Exception {
        String namespace = "com.topcoder.xxx";
        manager.loadFile(namespace, propertiesFileWithSpace);
        ConfigurationObject obj = manager.getConfiguration(namespace);
        assertEquals("LoadFile error", namespace, obj.getName());
        ConfigurationObject defaultObj = obj.getChild("default");
        // test property in default namespace
        assertEquals("LoadFile error", "valuea", defaultObj.getPropertyValue("a"));
        assertEquals("LoadFile error", "valueb", defaultObj.getPropertyValue("b"));
        // test nested property in default namespace
        assertEquals("LoadFile error", "valuec", defaultObj.getChild("b").getPropertyValue("c"));

        // test multi-values
        Object[] evalues = defaultObj.getPropertyValues("e");
        assertEquals("LoadFile error", "valuee1", evalues[0]);
        assertEquals("LoadFile error", "valuee2", evalues[1]);
        Object[] fvalues = defaultObj.getPropertyValues("f");
        assertEquals("LoadFile error", "valuef1", fvalues[0]);
        assertEquals("LoadFile error", "valuef2", fvalues[1]);

        // test nested property in custom namespace
        assertEquals("LoadFile error", "valueg", defaultObj.getChild("h").getPropertyValue("g"));
        assertEquals("LoadFile error", "valuei", defaultObj.getChild("h").getChild("g").getPropertyValue("i"));
    }

    /**
     * Accuracy test for the method loadFile(String, String), the namespace should be save in
     * configurationMap.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResource() throws Exception {
        String namespace = "com.topcoder.xxx";
        manager.loadFile(namespace, propertiesResource);
        ConfigurationObject obj = manager.getConfiguration(namespace);
        assertEquals("LoadResource error", namespace, obj.getName());
        ConfigurationObject defaultObj = obj.getChild("default");
        // test property in default namespace
        assertEquals("LoadResource error", "valuea", defaultObj.getPropertyValue("a"));
        assertEquals("LoadResource error", "valueb", defaultObj.getPropertyValue("b"));
        // test nested property in default namespace
        assertEquals("LoadResource error", "valuec", defaultObj.getChild("b").getPropertyValue("c"));

        // test multi-values
        Object[] evalues = defaultObj.getPropertyValues("e");
        assertEquals("LoadResource error", "valuee1", evalues[0]);
        assertEquals("LoadResource error", "valuee2", evalues[1]);
        Object[] fvalues = defaultObj.getPropertyValues("f");
        assertEquals("LoadResource error", "valuef1", fvalues[0]);
        assertEquals("LoadResource error", "valuef2", fvalues[1]);

        // test nested property in custom namespace
        assertEquals("LoadResource error", "valueg", defaultObj.getChild("h").getPropertyValue("g"));
        assertEquals("LoadResource error", "valuei", defaultObj.getChild("h").getChild("g").getPropertyValue("i"));
    }

    /**
     * Test the method <c>loadFile(String, String)</c> with null resource, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResourceNullResource() throws Exception {
        try {
            manager.loadFile("a", null);
            fail("IllegalArgumentException should be thrown when the resource is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, String)</c> with null namespace,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResourceNullNamespace() throws Exception {
        try {
            manager.loadFile(null, perloadResource);
            fail("IllegalArgumentException should be thrown when the namespace is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, String)</c> with empty namespace,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResourceEmptyNamespace() throws Exception {
        try {
            manager.loadFile("\t \r \n", perloadResource);
            fail("IllegalArgumentException should be thrown when the namespace is empty.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, String)</c> with empty resource, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResourceEmptyResource() throws Exception {
        try {
            manager.loadFile("com.topcoder", "\t \r \n");
            fail("IllegalArgumentException should be thrown when the resource is empty.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, String)</c> with conflict namespace,
     * NamespaceConflictException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResourceConfilictNamespace() throws Exception {
        try {
            manager.loadFile("com.topcoder", perloadResource);
            manager.loadFile("com.topcoder", perloadResource);
            fail("NamespaceConflictException should be thrown when the resource is empty.");
        } catch (NamespaceConflictException e) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, String)</c> with uncoginzed resource,
     * ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResourceErrorResource() throws Exception {
        try {
            manager.loadFile("error", "resource/error.xml");
            fail("ConfigurationParserException should be thrown when the resource is empty.");
        } catch (ConfigurationParserException e) {
            // ok
        }
    }
}
