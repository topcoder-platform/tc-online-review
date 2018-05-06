/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

/**
 * <p>
 * Tests the behavior of DefaultConfigManager.
 * </p>
 *
 * <p>
 * <em>Changes in 2.2:</em>
 * <ol>
 * <li>Moved related test cases from ConfigManagerTestCase.</li>
 * <li>Updated test cases.</li>
 * </ol>
 * </p>
 *
 * @author WishingBone, kr00tki, sparemax
 * @version 2.2
 * @since 2.2
 */
public class DefaultConfigManagerTestCase extends TestCase {
    /**
     * The DefaultConfigManager instance.
     */
    private DefaultConfigManager instance = null;

    /**
     * The list of created files.
     */
    private List<File> createdFiles = new ArrayList<File>();

    /**
     * Set up testing environment. Clear the config manager.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        instance = new DefaultConfigManager();
    }

    /**
     * Tear down testing environment. Clear the config manager and remove created files.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        instance = null;
        for (int i = 0; i < createdFiles.size(); ++i) {
            ((File) createdFiles.get(i)).delete();
        }
        createdFiles.clear();
    }

    /**
     * Prepares a properties config file.
     *
     * @return a properties config file.
     * @throws Exception
     *             to JUnit.
     */
    private File preparePropertiesFile() throws Exception {
        File file = File.createTempFile("unittest", ".properties", new File("test_files"));
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("prop1=value1");
        writer.println("prop1.prop2=value2;value3");
        writer.close();
        createdFiles.add(file);
        return file;
    }

    /**
     * Prepares an xml config file.
     *
     * @return an xml config file.
     * @throws Exception
     *             to JUnit.
     */
    private File prepareXMLFile() throws Exception {
        File file = File.createTempFile("unittest", ".xml", new File("test_files"));
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("<?xml version=\"1.0\"?>");
        writer.println("<CMConfig>");
        writer.println("    <Property name=\"prop1\">");
        writer.println("        <Value>value1</Value>");
        writer.println("    </Property>");
        writer.println("    <Property name=\"prop1.prop2\">");
        writer.println("        <Value>value2</Value>");
        writer.println("        <Value>value3</Value>");
        writer.println("    </Property>");
        writer.println("</CMConfig>");
        writer.close();
        createdFiles.add(file);
        return file;
    }

    /**
     * Prepares a multiple namespace xml config file.
     *
     * @return a multiple namespace xml config file.
     * @throws Exception
     *             to JUnit.
     */
    private File prepareMultipleXMLFile() throws Exception {
        File file = File.createTempFile("unittest", ".xml", new File("test_files"));
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("<?xml version=\"1.0\"?>");
        writer.println("<CMConfig>");
        writer.println("    <Config name=\"component a\">");
        writer.println("        <Property name=\"prop1\">");
        writer.println("            <Value>value1</Value>");
        writer.println("        </Property>");
        writer.println("    </Config>");
        writer.println("    <Config name=\"component b\">");
        writer.println("        <Property name=\"prop1.prop2\">");
        writer.println("            <Value>value2</Value>");
        writer.println("            <Value>value3</Value>");
        writer.println("        </Property>");
        writer.println("    </Config>");
        writer.println("</CMConfig>");
        writer.close();
        createdFiles.add(file);
        return file;
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>LateDeliverable()</code>.<br>
     * Instance should be correctly created.
     * </p>
     */
    public void testCtor() {
        instance = new DefaultConfigManager();

        assertNotNull("'namespaces' should be correct.", TestsHelper.getField(instance, "namespaces"));
        assertNotNull("'tempProperties' should be correct.", TestsHelper.getField(instance, "tempProperties"));
    }

    /**
     * Tests methods from ConfigManagerInterface.
     */
    public void testConfigManagerInterface() {
        assertEquals("'getNamespace' should be correct.",
            ConfigManager.CONFIG_MANAGER_NAMESPACE, instance.getNamespace());

        assertNotNull("'getConfigPropNames' should be correct.", instance.getConfigPropNames());
    }

    /**
     * Test add(namespace, filename, format, exceptionLevel).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddNamespaceFilenameFormatExceptionLevel() throws Exception {
        String filename = preparePropertiesFile().getAbsolutePath();
        instance.add("test.ns", filename, ConfigManager.CONFIG_PROPERTIES_FORMAT, ConfigManager.EXCEPTIONS_ALL);

        assertTrue("'add' should be correct.", instance.existsNamespace("test.ns"));
        assertTrue("'add' should be correct.", instance.getString("test.ns", "prop1").equals("value1"));
        assertTrue("'add' should be correct.", instance.getString("test.ns", "prop1.prop2").equals("value2;value3"));

        // namespace is null
        try {
            instance.add(null, filename, ConfigManager.CONFIG_PROPERTIES_FORMAT, ConfigManager.EXCEPTIONS_ALL);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace is empty
        try {
            instance.add("   ", filename, ConfigManager.CONFIG_PROPERTIES_FORMAT, ConfigManager.EXCEPTIONS_ALL);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iea) {
            // Good
        }
        // filename is null
        try {
            instance.add("test.ns", null, ConfigManager.CONFIG_PROPERTIES_FORMAT, ConfigManager.EXCEPTIONS_ALL);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // filename is empty
        try {
            instance.add("test.ns", "   ", ConfigManager.CONFIG_PROPERTIES_FORMAT, ConfigManager.EXCEPTIONS_ALL);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iea) {
            // Good
        }
        // format is null
        try {
            instance.add("test.ns", filename, null, ConfigManager.EXCEPTIONS_ALL);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // format is invalid
        try {
            instance.add("test.ns", filename, "UNKNOWN", ConfigManager.EXCEPTIONS_ALL);
            fail("Should have thrown UnknownConfigFormatException");
        } catch (UnknownConfigFormatException ucfe) {
            // Good
        }
        // exceptionLevel is invalid
        try {
            instance.add("test.ns", filename, ConfigManager.CONFIG_PROPERTIES_FORMAT, -101);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iea) {
            // Good
        }
        // can not locate file
        try {
            instance.add("test.ns", "nonexist.properties", ConfigManager.CONFIG_PROPERTIES_FORMAT,
                ConfigManager.EXCEPTIONS_ALL);
            fail("Should have thrown ConfigManagerException");
        } catch (ConfigManagerException instancee) {
            // Good
        }
        // namespace already exists
        try {
            instance.add("test.ns", filename, ConfigManager.CONFIG_PROPERTIES_FORMAT, ConfigManager.EXCEPTIONS_ALL);
            fail("Should have thrown NamespaceAlreadyExistsException");
        } catch (NamespaceAlreadyExistsException naee) {
            // Good
        }
    }

    /**
     * Tests add(namespace, format, exceptionLevel).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddNamespaceFormatExceptionLevel() throws Exception {
        String filename = preparePropertiesFile().getAbsolutePath();
        filename = filename.substring(filename.indexOf("test_files") + 11);
        String namespace = filename.substring(0, filename.lastIndexOf("."));
        instance.add(namespace, ConfigManager.CONFIG_PROPERTIES_FORMAT, ConfigManager.EXCEPTIONS_ALL);
        assertTrue("'add' should be correct.", instance.existsNamespace(namespace));
        assertTrue("'add' should be correct.", instance.getString(namespace, "prop1").equals("value1"));
        assertTrue("'add' should be correct.", instance.getString(namespace, "prop1.prop2").equals("value2;value3"));
        // namespace is null
        try {
            instance.add(null, ConfigManager.CONFIG_PROPERTIES_FORMAT, ConfigManager.EXCEPTIONS_ALL);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace is empty
        try {
            instance.add("   ", ConfigManager.CONFIG_PROPERTIES_FORMAT, ConfigManager.EXCEPTIONS_ALL);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
        // format is null
        try {
            instance.add(namespace, null, ConfigManager.EXCEPTIONS_ALL);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // format is invalid
        try {
            instance.add(namespace, "UNKNOWN", ConfigManager.EXCEPTIONS_ALL);
            fail("Should have thrown UnknownConfigFormatException");
        } catch (UnknownConfigFormatException ucfe) {
            // Good
        }
        // exceptionLevel is invalid
        try {
            instance.add(namespace, ConfigManager.CONFIG_PROPERTIES_FORMAT, -101);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iea) {
            // Good
        }
        // can not locate file
        try {
            instance.add("test.ns", ConfigManager.CONFIG_PROPERTIES_FORMAT, ConfigManager.EXCEPTIONS_ALL);
            fail("Should have thrown ConfigManagerException");
        } catch (ConfigManagerException instancee) {
            // Good
        }
        // namespace already exists
        try {
            instance.add(namespace, ConfigManager.CONFIG_PROPERTIES_FORMAT, ConfigManager.EXCEPTIONS_ALL);
            fail("Should have thrown NamespaceAlreadyExistsException");
        } catch (NamespaceAlreadyExistsException naee) {
            // Good
        }
    }

    /**
     * Tests add(filename, exceptionLevel).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddFilenameExceptionLevel() throws Exception {
        String filename = prepareMultipleXMLFile().getAbsolutePath();
        instance.add(filename, ConfigManager.EXCEPTIONS_ALL);
        assertTrue("'add' should be correct.", instance.existsNamespace("component a"));
        assertTrue("'add' should be correct.", instance.getString("component a", "prop1").equals("value1"));
        assertTrue("'add' should be correct.", instance.existsNamespace("component b"));
        assertTrue("'add' should be correct.",
            instance.getString("component b", "prop1.prop2").equals("value2;value3"));
        // filename is null
        try {
            instance.add((String) null, ConfigManager.EXCEPTIONS_ALL);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // filename is empty
        try {
            instance.add("   ", ConfigManager.EXCEPTIONS_ALL);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
        // exceptionLevel is invalid
        try {
            instance.add(filename, -101);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
        // can not locate file
        try {
            instance.add("nonexist.properties", ConfigManager.EXCEPTIONS_ALL);
            fail("Should have thrown ConfigManagerException");
        } catch (ConfigManagerException instancee) {
            // Good
        }
        // namespace already exists
        try {
            instance.add(filename, ConfigManager.EXCEPTIONS_ALL);
            fail("Should have thrown NamespaceAlreadyExistsException");
        } catch (NamespaceAlreadyExistsException naee) {
            // Good
        }
    }

    /**
     * Test add(namespace, filename, format).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddNamespaceFilenameFormat() throws Exception {
        String filename = preparePropertiesFile().getAbsolutePath();
        instance.add("test.ns", filename, ConfigManager.CONFIG_PROPERTIES_FORMAT);
        assertTrue("'add' should be correct.", instance.existsNamespace("test.ns"));
        assertTrue("'add' should be correct.", instance.getString("test.ns", "prop1").equals("value1"));
        assertTrue("'add' should be correct.", instance.getString("test.ns", "prop1.prop2").equals("value2;value3"));
        // namespace is null
        try {
            instance.add(null, filename, ConfigManager.CONFIG_PROPERTIES_FORMAT);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace is empty
        try {
            instance.add("   ", filename, ConfigManager.CONFIG_PROPERTIES_FORMAT);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iea) {
            // Good
        }
        // filename is null
        try {
            instance.add("test.ns", null, ConfigManager.CONFIG_PROPERTIES_FORMAT);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // filename is empty
        try {
            instance.add("test.ns", "   ", ConfigManager.CONFIG_PROPERTIES_FORMAT);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iea) {
            // Good
        }
        // format is null
        try {
            instance.add("test.ns", filename, null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // format is invalid
        try {
            instance.add("test.ns", filename, "UNKNOWN");
            fail("Should have thrown UnknownConfigFormatException");
        } catch (UnknownConfigFormatException ucfe) {
            // Good
        }
        // can not locate file
        try {
            instance.add("test.ns", "nonexist.properties", ConfigManager.CONFIG_PROPERTIES_FORMAT);
            fail("Should have thrown ConfigManagerException");
        } catch (ConfigManagerException instancee) {
            // Good
        }
        // namespace already exists
        try {
            instance.add("test.ns", filename, ConfigManager.CONFIG_PROPERTIES_FORMAT);
            fail("Should have thrown NamespaceAlreadyExistsException");
        } catch (NamespaceAlreadyExistsException naee) {
            // Good
        }
    }

    /**
     * Tests add(namespace, format).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddNamespaceFormat() throws Exception {
        String filename = preparePropertiesFile().getAbsolutePath();
        filename = filename.substring(filename.indexOf("test_files") + 11);
        String namespace = filename.substring(0, filename.lastIndexOf("."));
        instance.add(namespace, ConfigManager.CONFIG_PROPERTIES_FORMAT);
        assertTrue("'add' should be correct.", instance.existsNamespace(namespace));
        assertTrue("'add' should be correct.", instance.getString(namespace, "prop1").equals("value1"));
        assertTrue("'add' should be correct.", instance.getString(namespace, "prop1.prop2").equals("value2;value3"));
        // namespace is null
        try {
            instance.add(null, ConfigManager.CONFIG_PROPERTIES_FORMAT);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace is empty
        try {
            instance.add("   ", ConfigManager.CONFIG_PROPERTIES_FORMAT);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
        // format is null
        try {
            instance.add(namespace, null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // format is invalid
        try {
            instance.add(namespace, "UNKNOWN");
            fail("Should have thrown UnknownConfigFormatException");
        } catch (UnknownConfigFormatException ucfe) {
            // Good
        }
        // can not locate file
        try {
            instance.add("test.ns", ConfigManager.CONFIG_PROPERTIES_FORMAT);
            fail("Should have thrown ConfigManagerException");
        } catch (ConfigManagerException instancee) {
            // Good
        }
        // namespace already exists
        try {
            instance.add(namespace, ConfigManager.CONFIG_PROPERTIES_FORMAT);
            fail("Should have thrown NamespaceAlreadyExistsException");
        } catch (NamespaceAlreadyExistsException naee) {
            // Good
        }
    }

    /**
     * Tests add(filename).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddFilename() throws Exception {
        String filename = prepareMultipleXMLFile().getAbsolutePath();
        instance.add(filename);
        assertTrue("'add' should be correct.",
            instance.existsNamespace("component a"));
        assertTrue("'add' should be correct.",
            instance.getString("component a", "prop1").equals("value1"));
        assertTrue("'add' should be correct.",
            instance.existsNamespace("component b"));
        assertTrue("'add' should be correct.",
            instance.getString("component b", "prop1.prop2").equals("value2;value3"));
        // filename is null
        try {
            instance.add((String) null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // filename is empty
        try {
            instance.add("   ");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // Good
        }
        // can not locate file
        try {
            instance.add("nonexist.properties");
            fail("Should have thrown ConfigManagerException");
        } catch (ConfigManagerException instancee) {
            // Good
        }
        // namespace already exists
        try {
            instance.add(filename);
            fail("Should have thrown NamespaceAlreadyExistsException");
        } catch (NamespaceAlreadyExistsException naee) {
            // Good
        }
    }

    /**
     * Tests refresh().
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testRefresh() throws Exception {
        File file = preparePropertiesFile();
        instance.add("test.ns", file.getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        file.delete();
        createdFiles.clear();
        try {
            instance.refresh("test.ns");
            fail("Should have thrown ConfigManagerException");
        } catch (ConfigManagerException instancee) {
            // Good
        }
        // the namespace should be invalid
        assertFalse("'refresh' should be correct.", instance.existsNamespace("test.ns"));
        // namespace is null
        try {
            instance.refresh(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.refresh("unknown.ns");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests refreshAll().
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testRefreshAll() throws Exception {
        File file1 = preparePropertiesFile();
        instance.add("properties.ns", file1.getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        File file2 = prepareXMLFile();
        instance.add("xml.ns", file2.getAbsolutePath(), ConfigManager.CONFIG_XML_FORMAT);
        file1.delete();
        file2.delete();
        createdFiles.clear();
        try {
            instance.refreshAll();
            fail("Should have thrown ConfigManagerException");
        } catch (ConfigManagerException instancee) {
            // Good
        }
        // namespaces should all be invalid
        assertFalse("'refreshAll' should be correct.", instance.existsNamespace("properties.ns"));
        assertFalse("'refreshAll' should be correct.", instance.existsNamespace("xml.ns"));
    }

    /**
     * Tests getPropertyObject(namespace, key).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetPropertyObjectNamespaceKey() throws Exception {
        instance.add("test.ns", preparePropertiesFile().getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        Property property = instance.getPropertyObject("test.ns", "prop1");
        assertTrue("'getPropertyObject' should be correct.", property.getValue().equals("value1"));
        assertTrue("'getPropertyObject' should be correct.", property.list().size() == 1);
        assertTrue("'getPropertyObject' should be correct.",
            ((Property) property.list().get(0)).getName().equals("prop2"));
        assertNull("'getPropertyObject' should be correct.", instance.getPropertyObject("test.ns", "prop2"));
        // namespace is null
        try {
            instance.getPropertyObject(null, "prop1");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // key is null
        try {
            instance.getPropertyObject("test.ns", null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.getPropertyObject("nonexist.ns", "prop1");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests getString(namespace, key).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetStringNamespaceKey() throws Exception {
        instance.add("test.ns", preparePropertiesFile().getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        assertTrue("'getString' should be correct.",
            instance.getString("test.ns", "prop1").equals("value1"));
        assertTrue("'getString' should be correct.",
            instance.getString("test.ns", "prop1.prop2").equals("value2;value3"));
        assertNull("'getString' should be correct.",
            instance.getString("test.ns", "prop2"));
        // namespace is null
        try {
            instance.getString(null, "prop1");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // key is null
        try {
            instance.getString("test.ns", null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.getString("nonexist.ns", "prop1");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests getStringArray(namespace, key).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetStringArrayNamespaceKey() throws Exception {
        instance.add("test.ns", preparePropertiesFile().getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        assertTrue("'getStringArray' should be correct.",
            instance.getStringArray("test.ns", "prop1").length == 1);
        assertTrue("'getStringArray' should be correct.",
            instance.getStringArray("test.ns", "prop1")[0].equals("value1"));
        assertTrue("'getStringArray' should be correct.",
            instance.getStringArray("test.ns", "prop1.prop2").length == 2);
        assertTrue("'getStringArray' should be correct.",
            instance.getStringArray("test.ns", "prop1.prop2")[0].equals("value2"));
        assertTrue("'getStringArray' should be correct.",
            instance.getStringArray("test.ns", "prop1.prop2")[1].equals("value3"));
        assertNull("'getStringArray' should be correct.",
            instance.getStringArray("test.ns", "prop2"));
        // namespace is null
        try {
            instance.getStringArray(null, "prop1");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // key is null
        try {
            instance.getStringArray("test.ns", null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.getStringArray("nonexist.ns", "prop1");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests getProperty(namespace, key).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetPropertyNamespaceKey() throws Exception {
        instance.add("test.ns", preparePropertiesFile().getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        assertTrue("'getProperty' should be correct.",
            instance.getProperty("test.ns", "prop1").equals("value1"));
        assertTrue("'getProperty' should be correct.",
            instance.getProperty("test.ns", "prop1.prop2").equals("value2;value3"));
        assertNull("'getProperty' should be correct.",
            instance.getProperty("test.ns", "prop2"));
        // namespace is null
        try {
            instance.getProperty(null, "prop1");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // key is null
        try {
            instance.getProperty("test.ns", null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.getProperty("nonexist.ns", "prop1");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests getPropertyNames(namespace).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetPropertyNamesNamespace() throws Exception {
        instance.add("test.ns", preparePropertiesFile().getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        Enumeration<String> enu = instance.getPropertyNames("test.ns");
        assertTrue("'getPropertyNames' should be correct.", enu.hasMoreElements());
        assertTrue("'getPropertyNames' should be correct.", enu.nextElement().equals("prop1"));
        assertFalse("'getPropertyNames' should be correct.", enu.hasMoreElements());
        // namespace is null
        try {
            instance.getPropertyNames(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.getPropertyNames("nonexist.ns");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests existsNamespace(namespace).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testExistsNamespaceNamespace() throws Exception {
        instance.add("test.ns", preparePropertiesFile().getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        assertTrue("'existsNamespace' should be correct.", instance.existsNamespace("test.ns"));
        assertFalse("'existsNamespace' should be correct.", instance.existsNamespace("nonexist.ns"));
        // namespace is null
        try {
            instance.existsNamespace(null);
            fail("Should have thrwon NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
    }

    /**
     * Tests getConfigFormat(namespace).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetConfigFormatNamespace() throws Exception {
        instance.add("test.ns", preparePropertiesFile().getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        assertTrue("'getConfigFormat' should be correct.",
            instance.getConfigFormat("test.ns").equals(ConfigManager.CONFIG_PROPERTIES_FORMAT));
        // namespace is null
        try {
            instance.getConfigFormat(null);
            fail("Should have thrwon NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.getConfigFormat("nonexist.ns");
            fail("Should have thrwon UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests getConfigFilename(namespace).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetConfigFilenameNamespace() throws Exception {
        String filename = preparePropertiesFile().getAbsolutePath();
        instance.add("test.ns", filename, ConfigManager.CONFIG_PROPERTIES_FORMAT);
        assertTrue("'getConfigFilename' should be correct.",
            instance.getConfigFilename("test.ns").endsWith(
                filename.substring(filename.indexOf("test_files") + 11)));
        // namespace is null
        try {
            instance.getConfigFilename(null);
            fail("Should have thrwon NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.getConfigFilename("nonexist.ns");
            fail("Should have thrwon UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests getAllNamespaces().
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetAllNamespaces() throws Exception {
        instance.add("test.ns", preparePropertiesFile().getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        Iterator<String> itr = instance.getAllNamespaces();
        assertTrue("'getAllNamespaces' should be correct.", itr.hasNext());
        assertTrue("'getAllNamespaces' should be correct.", itr.next().equals("test.ns"));
        assertFalse("'getAllNamespaces' should be correct.", itr.hasNext());
    }

    /**
     * Tests canLock(namespace, user).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCanLockNamespaceUser() throws Exception {
        instance.add(prepareMultipleXMLFile().getAbsolutePath());
        assertTrue("'canLock' should be correct.", instance.canLock("component a", "foo"));
        assertTrue("'canLock' should be correct.", instance.canLock("component b", "foo"));
        instance.lock("component a", "foo");
        assertFalse("'canLock' should be correct.", instance.canLock("component a", "bar"));
        assertFalse("'canLock' should be correct.", instance.canLock("component b", "bar"));
        assertTrue("'canLock' should be correct.", instance.canLock("component a", "foo"));
        assertTrue("'canLock' should be correct.", instance.canLock("component b", "foo"));
        // namespace is null
        try {
            instance.canLock(null, "foo");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // user is null
        try {
            instance.canLock("component a", null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.canLock("nonexist.ns", "foo");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests lock(namespace, user).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testLockNamespaceUser() throws Exception {
        instance.add(prepareMultipleXMLFile().getAbsolutePath());
        instance.lock("component a", "foo");
        try {
            instance.lock("component a", "bar");
            fail("Should have thrown ConfigLockedException");
        } catch (ConfigLockedException cle) {
            // Good
        }
        instance.lock("component a", "foo");
        instance.lock("component b", "foo");
        // namespace is null
        try {
            instance.lock(null, "foo");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // user is null
        try {
            instance.lock("component a", null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.lock("nonexist.ns", "foo");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests forceUnlock(user).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testForceUnlockUser() throws Exception {
        instance.add(prepareMultipleXMLFile().getAbsolutePath());
        instance.lock("component a", "foo");
        assertFalse("'forceUnlock' should be correct.", instance.canLock("component b", "bar"));
        instance.forceUnlock("component b");
        assertTrue("'forceUnlock' should be correct.", instance.canLock("component a", "bar"));
        assertTrue("'forceUnlock' should be correct.", instance.canLock("component b", "bar"));
        // namespace is null
        try {
            instance.forceUnlock(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.forceUnlock("nonexist.ns");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests createTemporaryProperties(namespace).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateTemporaryPropertiesNamespace() throws Exception {
        instance.add("test.ns", preparePropertiesFile().getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        try {
            instance.commit("test.ns", "foo");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
        instance.createTemporaryProperties("test.ns");
        instance.commit("test.ns", "foo");
        // namespace is null
        try {
            instance.createTemporaryProperties(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.createTemporaryProperties("nonexist.ns");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests getTemporaryPropertyNames(namespace).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetTemporaryPropertyNames() throws Exception {
        instance.add("test.ns", preparePropertiesFile().getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        instance.createTemporaryProperties("test.ns");
        instance.setProperty("test.ns", "prop4", "value5");
        Enumeration<String> enu = instance.getTemporaryPropertyNames("test.ns");
        assertTrue("'getTemporaryPropertyNames' should be correct.", enu.hasMoreElements());
        assertTrue("'getTemporaryPropertyNames' should be correct.", enu.nextElement().equals("prop1"));
        assertTrue("'getTemporaryPropertyNames' should be correct.", enu.hasMoreElements());
        assertTrue("'getTemporaryPropertyNames' should be correct.", enu.nextElement().equals("prop4"));
        assertFalse("'getTemporaryPropertyNames' should be correct.", enu.hasMoreElements());
        // namespace is null
        try {
            instance.getTemporaryPropertyNames(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.getTemporaryPropertyNames("nonexist.ns");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests getTemporaryPropertyObject(namespace, key).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetTemporaryPropertyObjectNamespaceKey() throws Exception {
        instance.add("test.ns", preparePropertiesFile().getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        instance.createTemporaryProperties("test.ns");
        Property property = instance.getTemporaryPropertyObject("test.ns", "prop1");
        assertTrue("'getTemporaryPropertyObject' should be correct.",
            property.getValue().equals("value1"));
        assertTrue("'getTemporaryPropertyObject' should be correct.",
            property.list().size() == 1);
        assertTrue("'getTemporaryPropertyObject' should be correct.",
            ((Property) property.list().get(0)).getName().equals("prop2"));
        assertNull("'getTemporaryPropertyObject' should be correct.",
            instance.getPropertyObject("test.ns", "prop2"));
        // namespace is null
        try {
            instance.getTemporaryPropertyObject(null, "prop1");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // key is null
        try {
            instance.getTemporaryPropertyObject("test.ns", null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.getTemporaryPropertyObject("nonexist.ns", "prop1");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests getTemporaryString(namespace, key).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetTemporaryStringNamespaceKey() throws Exception {
        instance.add("test.ns", preparePropertiesFile().getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        instance.createTemporaryProperties("test.ns");
        assertTrue("'getTemporaryString' should be correct.",
            instance.getTemporaryString("test.ns", "prop1").equals("value1"));
        assertTrue("'getTemporaryString' should be correct.",
            instance.getTemporaryString("test.ns", "prop1.prop2").equals("value2;value3"));
        assertNull("'getTemporaryString' should be correct.",
            instance.getTemporaryString("test.ns", "prop2"));
        // namespace is null
        try {
            instance.getTemporaryString(null, "prop1");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // key is null
        try {
            instance.getTemporaryString("test.ns", null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.getTemporaryString("nonexist.ns", "prop1");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests getTemporatyStringArray(namespace, key).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetTemporaryStringArrayNamespaceKey() throws Exception {
        instance.add("test.ns", preparePropertiesFile().getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        instance.createTemporaryProperties("test.ns");
        assertTrue("'getTemporatyStringArray' should be correct.",
            instance.getTemporaryStringArray("test.ns", "prop1").length == 1);
        assertTrue("'getTemporatyStringArray' should be correct.",
            instance.getTemporaryStringArray("test.ns", "prop1")[0].equals("value1"));
        assertTrue("'getTemporatyStringArray' should be correct.",
            instance.getTemporaryStringArray("test.ns", "prop1.prop2").length == 2);
        assertTrue("'getTemporatyStringArray' should be correct.",
            instance.getTemporaryStringArray("test.ns", "prop1.prop2")[0].equals("value2"));
        assertTrue("'getTemporatyStringArray' should be correct.",
            instance.getTemporaryStringArray("test.ns", "prop1.prop2")[1].equals("value3"));
        assertNull("'getTemporatyStringArray' should be correct.",
            instance.getTemporaryStringArray("test.ns", "prop2"));
        // namespace is null
        try {
            instance.getTemporaryStringArray(null, "prop1");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // key is null
        try {
            instance.getTemporaryStringArray("test.ns", null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.getTemporaryStringArray("nonexist.ns", "prop1");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests setProperty(namespace, key, value).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetPropertyNamespaceKeyValue() throws Exception {
        instance.add("test.ns", preparePropertiesFile().getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        instance.createTemporaryProperties("test.ns");
        instance.setProperty("test.ns", "prop1.prop2.prop3", "value");
        assertTrue("'setProperty' should be correct.",
            instance.getTemporaryString("test.ns", "prop1.prop2.prop3").equals("value"));
        // namespace is null
        try {
            instance.setProperty(null, "prop", "value");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // key is null
        try {
            instance.setProperty("test.ns", null, "value");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // value is null
        try {
            instance.setProperty("test.ns", "prop", (String) null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.setProperty("nonexist.ns", "prop", "value");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests setProperty(namespace, key, values).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSetPropertyNamespaceKeyValues() throws Exception {
        instance.add("test.ns", preparePropertiesFile().getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        instance.createTemporaryProperties("test.ns");
        instance.setProperty("test.ns", "prop1.prop2.prop3", new String[] {"value1", "value2"});
        assertTrue("'setProperty' should be correct.",
            instance.getTemporaryStringArray("test.ns", "prop1.prop2.prop3").length == 2);
        assertTrue("'setProperty' should be correct.",
            instance.getTemporaryStringArray("test.ns", "prop1.prop2.prop3")[0].equals("value1"));
        assertTrue("'setProperty' should be correct.",
            instance.getTemporaryStringArray("test.ns", "prop1.prop2.prop3")[1].equals("value2"));
        // namespace is null
        try {
            instance.setProperty(null, "prop", new String[] {"value1", "value2"});
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // key is null
        try {
            instance.setProperty("test.ns", null, new String[] {"value1", "value2"});
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // values is null
        try {
            instance.setProperty("test.ns", "prop", (String[]) null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // values contains null entry
        try {
            instance.setProperty("test.ns", "prop", new String[] {"value", null});
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.setProperty("nonexist.ns", "prop", new String[] {"value1", "value2"});
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests addToProperty(namespace, key, value).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddToPropertyNamespaceKeyValue() throws Exception {
        instance.add("test.ns", preparePropertiesFile().getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        instance.createTemporaryProperties("test.ns");
        instance.addToProperty("test.ns", "prop1.prop2", "value4");
        assertTrue("'addToProperty' should be correct.",
            instance.getTemporaryStringArray("test.ns", "prop1.prop2").length == 3);
        assertTrue("'addToProperty' should be correct.",
            instance.getTemporaryStringArray("test.ns", "prop1.prop2")[0].equals("value2"));
        assertTrue("'addToProperty' should be correct.",
            instance.getTemporaryStringArray("test.ns", "prop1.prop2")[1].equals("value3"));
        assertTrue("'addToProperty' should be correct.",
            instance.getTemporaryStringArray("test.ns", "prop1.prop2")[2].equals("value4"));
        instance.addToProperty("test.ns", "prop3", "value5");
        assertTrue("'addToProperty' should be correct.",
            instance.getTemporaryString("test.ns", "prop3").equals("value5"));
        // namespace is null
        try {
            instance.addToProperty(null, "prop", "value");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // key is null
        try {
            instance.addToProperty("test.ns", null, "value");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // value is null
        try {
            instance.addToProperty("test.ns", "prop", null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.addToProperty("nonexist.ns", "prop", "value");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests removeProperty(namespace, key).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testRemovePropertyNamespaceKey() throws Exception {
        instance.add("test.ns", preparePropertiesFile().getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        instance.createTemporaryProperties("test.ns");
        instance.removeProperty("test.ns", "prop1");
        assertNull("'removeProperty' should be correct.",
            instance.getTemporaryString("test.ns", "prop1"));
        assertNull("'removeProperty' should be correct.",
            instance.getTemporaryString("test.ns", "prop1.prop2"));
        // namespace is null
        try {
            instance.removeProperty(null, "prop");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // key is null
        try {
            instance.removeProperty("test.ns", null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.removeProperty("nonexist.ns", "prop");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests removeValue(namespace, key, value).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testRemoveValueNamespaceKeyValue() throws Exception {
        instance.add("test.ns", preparePropertiesFile().getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        instance.createTemporaryProperties("test.ns");
        instance.removeValue("test.ns", "prop1.prop2", "value2");
        assertTrue("'removeValue' should be correct.",
            instance.getTemporaryString("test.ns", "prop1.prop2").equals("value3"));
        // namespace is null
        try {
            instance.removeValue(null, "prop", "value");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // key is null
        try {
            instance.removeValue("test.ns", null, "value");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // value is null
        try {
            instance.removeValue("test.ns", "prop", null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.removeValue("nonexist.ns", "prop", "value");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests commit(namespace, user).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCommitNamespaceUser() throws Exception {
        instance.add(prepareMultipleXMLFile().getAbsolutePath());
        instance.createTemporaryProperties("component b");
        instance.lock("component b", "foo");
        try {
            instance.commit("component b", "bar");
            fail("Should have thrown ConfigLockedException");
        } catch (ConfigLockedException cle) {
            // Good
        }
        instance.commit("component b", "foo");
        instance.forceUnlock("component a");
        // namespace is null
        try {
            instance.commit(null, "foo");
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // user is null
        try {
            instance.commit("component b", null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.commit("nonexist.ns", "foo");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests removeNamespace(namespace).
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testRemoveNamespaceNamespace() throws Exception {
        instance.add("test.ns", preparePropertiesFile().getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
        instance.removeNamespace("test.ns");
        assertFalse("'removeNamespace' should be correct.",
            instance.existsNamespace("test.ns"));
        // namespace is null
        try {
            instance.removeNamespace(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException npe) {
            // Good
        }
        // namespace does not exist
        try {
            instance.removeNamespace("nonexist.ns");
            fail("Should have thrown UnknownNamespaceException");
        } catch (UnknownNamespaceException une) {
            // Good
        }
    }

    /**
     * Tests the accuracy of the {@link ConfigManager#add(URL)} method.
     *
     * @throws Exception
     *             to JUnit.
     * @since 2.1.5
     */
    public void testAddURL() throws Exception {
        File file = new File("test_files/SampleMultipleConfig.xml");
        instance.add(file.toURL());
        assertTrue("Should load namespace.", instance.existsNamespace("Component A"));
        assertTrue("Should load namespace.", instance.existsNamespace("Component B"));

        assertEquals("Incorrect property value.", "Value", instance.getString("Component B", "Prop2"));
    }

    /**
     * Tests the accuracy of the {@link ConfigManager#add(URL, int)} method.
     *
     * @throws Exception
     *             to JUnit.
     * @since 2.1.5
     */
    public void testAddURLInt() throws Exception {
        File file = new File("test_files/SampleMultipleConfig.xml");
        instance.add(file.toURL(), ConfigManager.EXCEPTIONS_ALL);
        assertTrue("Should load namespace.", instance.existsNamespace("Component A"));
        assertTrue("Should load namespace.", instance.existsNamespace("Component B"));

        assertEquals("Incorrect property value.", "Value2", instance.getStringArray("Component A", "Prop2")[1]);
    }

    /**
     * Tests the failure of the {@link ConfigManager#add(URL)} method. The URL is null.
     *
     * @throws Exception
     *             to JUnit.
     * @since 2.1.5
     */
    public void testAddURL_Null() throws Exception {
        try {
            instance.add((URL) null);
            fail("Null URL, NPE expected.");
        } catch (NullPointerException ex) {
            // Good
        }
    }

    /**
     * Tests the failure of the {@link ConfigManager#add(URL)} method. The source file doesn't exists.
     *
     * @throws Exception
     *             to JUnit.
     * @since 2.1.5
     */
    public void testAddURL_InvalidURL() throws Exception {
        try {
            instance.add(new File("missing").toURL());
            fail("File not exists.");
        } catch (ConfigManagerException ex) {
            // Good
        }
    }
}
