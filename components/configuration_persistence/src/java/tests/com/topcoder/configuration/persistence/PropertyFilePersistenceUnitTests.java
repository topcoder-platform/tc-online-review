/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import junit.framework.TestCase;

/**
 * Unit test for class <c>PropertyFilePersistence</c>.
 *
 * @author rainday
 * @version 1.0
 */
public class PropertyFilePersistenceUnitTests extends TestCase {

    /**
     * Response the test properties file to load.
     */
    private static File file = new File("test_files/test.properties");

    /**
     * Response the error properties file to load.
     */
    private static File errFile = new File("test_files/error1.properties");

    /**
     * Response the error properties file which have forbidden delimiter.
     */
    private static File errFile2 = new File("test_files/error2.properties");

    /**
     * Response the test properties file which contains two line delimiter.
     */
    private static File errFile3 = new File("test_files/error3.properties");

    /**
     * Response the test properties file which error key formatting.
     */
    private static File errFile4 = new File("test_files/error4.properties");

    /**
     * Response the test properties file which error value formatting.
     */
    private static File errFile5 = new File("test_files/error5.properties");

    /**
     * Response the test properties file which error value formatting.
     */
    private static File errFile6 = new File("test_files/error6.properties");

    /**
     * Response the tmp properties file to write.
     */
    private static File tmpFile = new File("test_files/tmp_properties.properties");

    /**
     * Response the test properties resource to load.
     */
    private static File resource = new File("resource/test.properties");

    /**
     * Response the error properties resource to load.
     */
    private static File errResource = new File("resource/error1.properties");

    /**
     * Response the error properties resource which have forbidden delimiter.
     */
    private static File errResource2 = new File("resource/error2.properties");

    /**
     * Response the test properties resource which contains two line delimiter.
     */
    private static File errResource3 = new File("resource/error3.properties");

    /**
     * Response the test properties resource which error key formatting.
     */
    private static File errResource4 = new File("resource/error4.properties");

    /**
     * Response the test properties resource which error value formatting.
     */
    private static File errResource5 = new File("resource/error5.properties");

    /**
     * Response the test properties resource which error value formatting.
     */
    private static File errResource6 = new File("resource/error6.properties");

    /**
     * Response the tmp properties resource to write.
     */
    private static File tmpResource = new File("resource/tmp_properties.properties");

    /**
     * Response the PropertyFilePersistence used for tests.
     */
    private PropertyFilePersistence persistence;

    /**
     * create PropertyFilePersistence instance for tests.
     */
    protected void setUp() {
        persistence = new PropertyFilePersistence();
    }

    /**
     * Delete the generated files.
     */
    protected void tearDown() {
        if (tmpFile.exists()) {
            tmpFile.delete();
        }
    }

    /**
     * Accuracy test for the constructor, new instance should be created.
     *
     */
    public void testConstructor() {
        assertNotNull("Create PropertyFilePersistence instance incorrectly.", new PropertyFilePersistence());
    }

    /**
     * Accuracy test for the method <c>loadFile(String, File)</c>. The test file should be load
     * correctly, the generated ConfigurationObject should contains all properties in the properties
     * file.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFile() throws Exception {
        ConfigurationObject obj = persistence.loadFile("com.topcoder", file);
        assertEquals("LoadFile error", "com.topcoder", obj.getName());
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
     * Test the method <c>loadFile(String, File)</c> with null file, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileNullFile() throws Exception {
        try {
            persistence.loadFile("com.topcoder", null);
            fail("IllegalArgumentException should be thrown when the file is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with null namespace, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileNullNamespace() throws Exception {
        try {
            persistence.loadFile(null, file);
            fail("IllegalArgumentException should be thrown when the namespace is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with empty namespace, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileEmptyNamespace() throws Exception {
        try {
            persistence.loadFile("\t \r \n", file);
            fail("IllegalArgumentException should be thrown when the namespace is empty.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with error list delimiter,
     * ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileErrorListDelimiterFile() throws Exception {
        try {
            persistence.loadFile("com.topcoder", errFile);
            fail("ConfigurationParserException should be thrown when the properties file is error.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with error properties file,
     * ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileErrorPropertyFile() throws Exception {
        try {
            persistence.loadFile("com.topcoder", errFile2);
            fail("ConfigurationParserException should be thrown when the properties file is error.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with two different list delimiters in file,
     * ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileTwoListDelimitersFile() throws Exception {
        try {
            persistence.loadFile("com.topcoder", errFile3);
            fail("ConfigurationParserException should be thrown when the properties file has two delimiters.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with error key format.
     * ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileErrorKeyFormat() throws Exception {
        try {
            persistence.loadFile("com.topcoder", errFile4);
            fail("ConfigurationParserException should be thrown when the properties file has incorrect key format.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with empty key format.
     * ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileEmptyKeyFormat() throws Exception {
        try {
            persistence.loadFile("com.topcoder", errFile6);
            fail("ConfigurationParserException should be thrown when the properties file has empty key format.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with error value format.
     * ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileErrorValueFormat() throws Exception {
        try {
            persistence.loadFile("com.topcoder", errFile5);
            fail("ConfigurationParserException should be thrown when the properties file has incorrect value format.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the constructor <c>loadFile(String, File)</c> with non-exist file, IOException should
     * be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileNonExistFile() throws Exception {
        try {
            new ConfigurationFileManager("non-exits.properties");
            fail("IOException should be thrown when the file doesn't exist.");
        } catch (IOException ioe) {
            // ok
        }
    }

    /**
     * Accuracy test for the method <c>saveFile(File, ConfigurationObject)</c>. an properties file
     * for the configuration object should be saved.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testSaveFile() throws Exception {
        ConfigurationObject obj = persistence.loadFile("com.topcoder", file);
        persistence.saveFile(tmpFile, obj);
        BufferedReader reader = new BufferedReader(new FileReader(tmpFile));
        String content = "";
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            content += line;
        }
        assertTrue("Test saveFile error", content.indexOf("a=valuea") != -1);
        assertTrue("Test saveFile error", content.indexOf("b.c=valuec") != -1);
        assertTrue("Test saveFile error", content.indexOf("h.g.i=valuei") != -1);
        assertTrue("Test saveFile error", content.indexOf("e=valuee1;valuee2") != -1);
        reader.close();
    }

    /**
     * Test the method <c>saveFile(File, ConfigurationObject)</c> with null file,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testSaveFileNullFile() throws Exception {
        try {
            persistence.saveFile(null, new DefaultConfigurationObject("T"));
            fail("IllegalArgumentException should be thrown when the file is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the method <c>saveFile(File, ConfigurationObject)</c> with null configuration object,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testSaveFileNullConfig() throws Exception {
        try {
            persistence.saveFile(tmpFile, null);
            fail("IllegalArgumentException should be thrown when the config object is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the method <c>saveFile(File, ConfigurationObject)</c> when the config object doesn't
     * have default child. ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testSaveFileNoDefaultChild() throws Exception {
        try {
            persistence.saveFile(tmpFile, new DefaultConfigurationObject("No default child"));
            fail("ConfigurationParserException should be thrown when the config object doesn't have default child.");
        } catch (ConfigurationParserException e) {
            // ok
        }
    }

    /**
     * Accuracy test for the method <c>loadFile(String, File)</c>. The test resource should be load
     * correctly, the generated ConfigurationObject should contains all properties in the properties
     * resource.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResource() throws Exception {
        ConfigurationObject obj = persistence.loadFile("com.topcoder", resource);
        assertEquals("LoadResource error", "com.topcoder", obj.getName());
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
     * Test the method <c>loadFile(String, File)</c> with null resource, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResourceNullResource() throws Exception {
        try {
            persistence.loadFile("com.topcoder", null);
            fail("IllegalArgumentException should be thrown when the resource is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with null namespace, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResourceNullNamespace() throws Exception {
        try {
            persistence.loadFile(null, resource);
            fail("IllegalArgumentException should be thrown when the namespace is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with empty namespace, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResourceEmptyNamespace() throws Exception {
        try {
            persistence.loadFile("\t \r \n", resource);
            fail("IllegalArgumentException should be thrown when the namespace is empty.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with error list delimiter,
     * ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResourceErrorListDelimiterResource() throws Exception {
        try {
            persistence.loadFile("com.topcoder", errResource);
            fail("ConfigurationParserException should be thrown when the properties resource is error.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with error properties resource,
     * ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResourceErrorPropertyResource() throws Exception {
        try {
            persistence.loadFile("com.topcoder", errResource2);
            fail("ConfigurationParserException should be thrown when the properties resource is error.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with two different list delimiters in resource,
     * ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResourceTwoListDelimitersResource() throws Exception {
        try {
            persistence.loadFile("com.topcoder", errResource3);
            fail("ConfigurationParserException should be thrown when the properties resource has two delimiters.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with error key format.
     * ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResourceErrorKeyFormat() throws Exception {
        try {
            persistence.loadFile("com.topcoder", errResource4);
            fail("ConfigurationParserException should be thrown when the properties resource has incorrect key format.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with empty key format.
     * ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResourceEmptyKeyFormat() throws Exception {
        try {
            persistence.loadFile("com.topcoder", errResource6);
            fail("ConfigurationParserException should be thrown when the properties resource has empty key format.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with error value format.
     * ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResourceErrorValueFormat() throws Exception {
        try {
            persistence.loadFile("com.topcoder", errResource5);
            fail("ConfigurationParserException should be thrown when the properties resource has incorrect value format.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }
}
