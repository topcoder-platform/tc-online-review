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
 * Unit test for class <c>XMLFilePersistence</c>.
 *
 * @author rainday
 * @version 1.0
 */
public class XMLFilePersistenceUnitTests extends TestCase {

    /**
     * Response the test xml file to load.
     */
    private static File file = new File("test_files/test.xml");

    /**
     * Response the error xml file to load.
     */
    private static File errFile = new File("test_files/error1.xml");

    /**
     * Response the xml file which contain empty namespace.
     */
    private static File errFile2 = new File("test_files/error2.xml");

    /**
     * Response the xml file which contain non-name property.
     */
    private static File errFile3 = new File("test_files/error3.xml");

    /**
     * Response the xml file which contain non-name property.
     */
    private static File testFile2 = new File("test_files/test2.xml");

    /**
     * Response the temp xml file to write.
     */
    private static File tmpFile = new File("test_files/tmp_xml.xml");

    /**
     * Response the test xml resource to load.
     */
    private static File resource = new File("resource/test.xml");

    /**
     * Response the error xml resource to load.
     */
    private static File errResource = new File("resource/error1.xml");

    /**
     * Response the xml resource which contain empty namespace.
     */
    private static File errResource2 = new File("resource/error2.xml");

    /**
     * Response the xml resource which contain non-name property.
     */
    private static File errResource3 = new File("resource/error3.xml");

    /**
     * Response the xml resource which contain non-name property.
     */
    private static File testResource2 = new File("resource/test2.xml");

    /**
     * Response the temp xml resource to write.
     */
    private static File tmpResource = new File("resource/tmp_xml.xml");

    /**
     * Response the XMLFilePersistence used for tests.
     */
    private XMLFilePersistence persistence;

    /**
     * create XMLFilePersistence instance for tests.
     */
    protected void setUp() {
        persistence = new XMLFilePersistence();
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
        assertNotNull("Create XMLFilePersistence instance incorrectly.", new XMLFilePersistence());
    }

    /**
     * Accuracy test for the method <c>loadFile(String, File)</c>. The test file should be load
     * correctly, the generated ConfigurationObject should contains all properties in the xml file.
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

        ConfigurationObject xxxObj = obj.getChild("com.topcoder.xxx");
        // test multi-values
        Object[] evalues = xxxObj.getPropertyValues("e");
        String values = evalues[0].toString() + evalues[1].toString();
        assertTrue("LoadFile error", values.indexOf("valuee1") > -1);
        assertTrue("LoadFile error", values.indexOf("valuee2") > -1);
        Object[] nvalues = xxxObj.getChild("m").getPropertyValues("n");
        values = nvalues[0].toString() + nvalues[1].toString();
        assertTrue("LoadFile error", values.indexOf("valuen1") > -1);
        assertTrue("LoadFile error", values.indexOf("valuen2") > -1);
        assertEquals("LoadFile error", "valuef", xxxObj.getPropertyValue("f"));
        assertEquals("LoadFile error", "valueg", xxxObj.getChild("f").getPropertyValue("g"));

        // test multi-namespace
        ConfigurationObject yyyObj = obj.getChild("com.topcoder.yyy");
        assertNotNull("LoadFile error", yyyObj);
        assertEquals("LoadFile error", "valueh", yyyObj.getPropertyValue("h"));
        // test nested property in custom namespace
        assertEquals("LoadFile error", "valuej", yyyObj.getChild("i").getPropertyValue("j"));
    }
    /**
     * Test the method <c>loadFile(String, File)</c>.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileEmptyValues() throws Exception {
        ConfigurationObject obj = persistence.loadFile("com.topcoder", testFile2);
        ConfigurationObject cfgObj = obj.getChild("config");
        assertEquals("LoadFile error", "", cfgObj.getChild("a_property").getPropertyValue("property_name"));
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
     * Test the method <c>loadFile(String, File)</c> with error xml file,
     * ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileErrorXmlFile() throws Exception {
        try {
            persistence.loadFile("com.topcoder", errFile);
            fail("ConfigurationParserException should be thrown when the xml file is error.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with empty namespace in the xml file
     * ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileEmptyNamespaceInXmlFile() throws Exception {
        try {
            persistence.loadFile("com.topcoder", errFile2);
            fail("ConfigurationParserException should be thrown when empty namespace in the xml file.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with no name attribute property in the xml
     * file ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadFileNoNamePropertyInXmlFile() throws Exception {
        try {
            persistence.loadFile("com.topcoder", errFile3);
            fail("ConfigurationParserException should be thrown when no name attribute property in the xml file.");
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
            new ConfigurationFileManager("non-exists.xml");
            fail("IOException should be thrown when the file doesn't exist.");
        } catch (IOException ioe) {
            // ok
        }
    }

    /**
     * Accuracy test for the method <c>saveFile(File, ConfigurationObject)</c>. an xml for the
     * configuration object should be saved.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testSaveFile() throws Exception {
        ConfigurationObject obj = persistence.loadFile("com.topcoder", file);
        persistence.saveFile(tmpFile, obj);
        BufferedReader readerA = new BufferedReader(new FileReader(file));
        BufferedReader readerB = new BufferedReader(new FileReader(tmpFile));
        String lineA = readerA.readLine();
        String lineB = readerB.readLine();
        String content = "";
        while (lineB != null) {
            content += lineB;
            lineB = readerB.readLine();
        }
        while (lineA != null) {
            if (lineA.trim().length() > 0) {
                assertTrue("Test saveFile failed", content.indexOf(lineA.trim()) != -1);
            }
            lineA = readerA.readLine();
        }
        readerA.close();
        readerB.close();
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
     * Accuracy test for the method <c>loadFile(String, File)</c>. The test resource should be load
     * correctly, the generated ConfigurationObject should contains all properties in the xml resource.
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

        ConfigurationObject xxxObj = obj.getChild("com.topcoder.xxx");
        // test multi-values
        Object[] evalues = xxxObj.getPropertyValues("e");
        String values = evalues[0].toString() + evalues[1].toString();
        assertTrue("LoadResource error", values.indexOf("valuee1") > -1);
        assertTrue("LoadResource error", values.indexOf("valuee2") > -1);
        Object[] nvalues = xxxObj.getChild("m").getPropertyValues("n");
        values = nvalues[0].toString() + nvalues[1].toString();
        assertTrue("LoadResource error", values.indexOf("valuen1") > -1);
        assertTrue("LoadResource error", values.indexOf("valuen2") > -1);
        assertEquals("LoadResource error", "valuef", xxxObj.getPropertyValue("f"));
        assertEquals("LoadResource error", "valueg", xxxObj.getChild("f").getPropertyValue("g"));

        // test multi-namespace
        ConfigurationObject yyyObj = obj.getChild("com.topcoder.yyy");
        assertNotNull("LoadResource error", yyyObj);
        assertEquals("LoadResource error", "valueh", yyyObj.getPropertyValue("h"));
        // test nested property in custom namespace
        assertEquals("LoadResource error", "valuej", yyyObj.getChild("i").getPropertyValue("j"));
    }
    /**
     * Test the method <c>loadFile(String, File)</c>.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResourceEmptyValues() throws Exception {
        ConfigurationObject obj = persistence.loadFile("com.topcoder", testResource2);
        ConfigurationObject cfgObj = obj.getChild("config");
        assertEquals("LoadResource error", "", cfgObj.getChild("a_property").getPropertyValue("property_name"));
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
     * Test the method <c>loadFile(String, File)</c> with error xml resource,
     * ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResourceErrorXmlResource() throws Exception {
        try {
            persistence.loadFile("com.topcoder", errResource);
            fail("ConfigurationParserException should be thrown when the xml resource is error.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with empty namespace in the xml resource
     * ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResourceEmptyNamespaceInXmlResource() throws Exception {
        try {
            persistence.loadFile("com.topcoder", errResource2);
            fail("ConfigurationParserException should be thrown when empty namespace in the xml resource.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }

    /**
     * Test the method <c>loadFile(String, File)</c> with no name attribute property in the xml
     * resource ConfigurationParserException should be thrown.
     *
     * @throws Exception
     *             any exception to junit
     */
    public void testLoadResourceNoNamePropertyInXmlResource() throws Exception {
        try {
            persistence.loadFile("com.topcoder", errResource3);
            fail("ConfigurationParserException should be thrown when no name attribute property in the xml resource.");
        } catch (ConfigurationParserException cpe) {
            // ok
        }
    }
}
