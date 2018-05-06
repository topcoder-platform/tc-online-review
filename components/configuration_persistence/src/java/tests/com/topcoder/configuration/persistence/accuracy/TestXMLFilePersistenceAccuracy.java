/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.persistence.accuracy;

import java.io.File;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;

import junit.framework.TestCase;

/**
 * Accuracy test cases for class <code>XMLFilePersistence </code>.
 *
 * @author Chenhong
 * @version 1.0
 */
public class TestXMLFilePersistenceAccuracy extends TestCase {

    /**
     * Represents the XMLFilePersistence instance for testing.
     */
    private XMLFilePersistence persistence = new XMLFilePersistence();

    /**
     * Test method <code>saveFile(File file, ConfigurationObject config) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testSaveFile_1() throws Exception {
        File file = new File("test_files/accuracy/temp.xml");

        ConfigurationObject object = new DefaultConfigurationObject("name");

        persistence.saveFile(file, object);

        ConfigurationObject ret = persistence.loadFile("default", file);

        assertEquals("Equal is expected.", 0, ret.getAllChildren().length);
    }

    /**
     * Test method <code>saveFile(File file, ConfigurationObject config) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testSaveFile_2() throws Exception {
        File file = new File("test_files/accuracy/temp.xml");

        ConfigurationObject object = new DefaultConfigurationObject("name");

        ConfigurationObject object2 = new DefaultConfigurationObject("name2");

        object.addChild(object2);

        persistence.saveFile(file, object);

        ConfigurationObject ret = persistence.loadFile("default", file);

        assertEquals("Equal is expected.", 1, ret.getAllChildren().length);
    }

    /**
     * Test method <code>saveFile(File file, ConfigurationObject config) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testSaveFile_3() throws Exception {
        File file = new File("test_files/accuracy/temp.xml");

        ConfigurationObject object = new DefaultConfigurationObject("name");

        ConfigurationObject object2 = new DefaultConfigurationObject("name2");

        ConfigurationObject object3 = new DefaultConfigurationObject("name3");

        ConfigurationObject object4 = new DefaultConfigurationObject("name4");

        object.addChild(object2);
        object.addChild(object3);
        object.addChild(object4);

        persistence.saveFile(file, object);

        ConfigurationObject ret = persistence.loadFile("default", file);

        assertEquals("Equal is expected.", 3, ret.getAllChildren().length);
    }

    /**
     * Test method <code>saveFile(File file, ConfigurationObject config) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testSaveFile_4() throws Exception {
        File file = new File("test_files/accuracy/temp.xml");

        if (file.exists()) {
            file.createNewFile();
        }

        ConfigurationObject object = new DefaultConfigurationObject("name");

        ConfigurationObject object2 = new DefaultConfigurationObject("namespace");

        object2.setPropertyValues("p", new String[] { "1", "2", "3", "4", "5" });

        object.addChild(object2);

        persistence.saveFile(file, object);

        ConfigurationObject ret = persistence.loadFile("default", file);

        ConfigurationObject o = ret.getChild("namespace");

        assertEquals("Equal is expected.", 5, o.getPropertyValuesCount("p"));
    }

    /**
     * Test method <code>ConfigurationObject loadFile(String name, File file) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testLoadFile_1() throws Exception {
        File file = new File("test_files/accuracy/DBConnectionFactory.xml");

        ConfigurationObject ret = persistence.loadFile("connections", file);

        assertEquals("Equal is expected.", "connections", ret.getName());

        assertEquals("Equal is expected.", 1, ret.getAllChildren().length);
    }

    /**
     * Test method <code>ConfigurationObject loadFile(String name, File file) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testLoadFile_2() throws Exception {
        File file = new File("test_files/accuracy/test1.xml");

        ConfigurationObject ret = persistence.loadFile("default", file);

        assertEquals("Equal is expected.", 0, ret.getAllChildren().length);

        assertEquals("Equal is expected.", 0, ret.getAllPropertyKeys().length);
    }

    /**
     * Test method <code>ConfigurationObject loadFile(String name, File file) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testLoadFile_3() throws Exception {
        File file = new File("test_files/accuracy/test2.xml");

        ConfigurationObject ret = persistence.loadFile("default", file);

        assertEquals("Equal is expected.", 1, ret.getAllChildren().length);

        ConfigurationObject object = ret.getChild("namespace");

        assertNotNull("Should not be null.", object);

        assertEquals("Equal is expected.", 5, object.getPropertyValuesCount("test1"));

        for (int i = 1; i <= 5; i++) {
            assertEquals("Equal is expected.", String.valueOf(i), object.getPropertyValues("test1")[i - 1]);
        }
    }

    /**
     * Test method <code>ConfigurationObject loadFile(String name, File file) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testLoadFile_4() throws Exception {
        File file = new File("test_files/accuracy/test3.xml");

        ConfigurationObject ret = persistence.loadFile("default", file);

        assertEquals("Equal is expected.", 1, ret.getAllChildren().length);

        ConfigurationObject object = ret.getChild("namespace");

        assertNotNull("Should not be null.", object);

        assertEquals("Equal is expected.", 1, object.getPropertyValuesCount("test1"));
    }

    /**
     * Test method <code>ConfigurationObject loadFile(String name, File file) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testLoadFile_5() throws Exception {
        File file = new File("test_files/accuracy/test4.xml");

        ConfigurationObject ret = persistence.loadFile("default", file);

        assertEquals("Equal is expected.", 1, ret.getAllChildren().length);

        persistence.saveFile(new File("temp.xml"), ret);
        ConfigurationObject object = ret.getChild("namespace");

        assertEquals("Equal is expected.", 5, object.getChild("test1").getChild("test2").getPropertyValuesCount(
                "test3"));
    }

    /**
     * Test method <code>ConfigurationObject loadFile(String name, File file) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testLoadFile_6() throws Exception {
        File file = new File("test_files/accuracy/test5.xml");

        ConfigurationObject ret = persistence.loadFile("default", file);

        assertEquals("Equal is expected.", 2, ret.getAllChildren().length);

        ConfigurationObject object = ret.getChild("namespace");

        assertNotNull("Should not be null.", object);

        assertEquals("Equal is expected.", 5, object.getChild("test1").getChild("test2").getPropertyValuesCount(
                "test3"));

        ConfigurationObject object2 = ret.getChild("namespace2");

        assertEquals("Equal is expected.", 5, object2.getChild("test1").getChild("test2").getPropertyValuesCount(
                "test3"));
    }

    /**
     * Test method <code>ConfigurationObject loadFile(String name, File file) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testLoadFile_7() throws Exception {
        File file = new File("test_files/accuracy/test6.xml");

        ConfigurationObject ret = persistence.loadFile("default", file);

        assertEquals("Equal is expected.", 5, ret.getAllChildren().length);
    }

    /**
     * Test method <code>ConfigurationObject loadFile(String name, File file) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testLoadFile_8() throws Exception {
        File file = new File("test_files/accuracy/test_multiple.xml");

        ConfigurationObject ret = persistence.loadFile("default", file);

        assertNotNull("Should not be null.", ret);

        ConfigurationObject object = ret.getChild("default");

        assertEquals("Equal is expected.", 2, object.getPropertyValuesCount("t"));

        ConfigurationObject object2 = ret.getChild("namespace");

        assertEquals("Equal is expected.", 5, object2.getPropertyValuesCount("test1"));
    }
}