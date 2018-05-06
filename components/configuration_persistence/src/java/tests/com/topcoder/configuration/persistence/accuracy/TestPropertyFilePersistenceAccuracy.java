/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.persistence.accuracy;

import java.io.File;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.PropertyFilePersistence;

import junit.framework.TestCase;

/**
 * Accuracy test cases for class <code>PropertyFilePersistence </code>.
 *
 *
 * @author Chenhong
 * @version 1.0
 */
public class TestPropertyFilePersistenceAccuracy extends TestCase {

    /**
     * Represents the PropertyFilePersistence instance for testing.
     */
    private PropertyFilePersistence persistence = new PropertyFilePersistence();

    /**
     * Test method <code>saveFile(File file, ConfigurationObject config) </code>.
     *
     * @throws Exception
     *             to junit.
     *
     */
    public void testSaveFile_1() throws Exception {
        File file = new File("test_files/accuracy/temp.properties");

        if (file.exists()) {
            file.createNewFile();
        }

        ConfigurationObject object = persistence.loadFile("a", new File("test_files/accuracy/test1.properties"));

        persistence.saveFile(file, object);

        ConfigurationObject ret = persistence.loadFile("a", file);

        ret = ret.getChild("default");

        assertEquals("Equal is expected.", 5, ret.getPropertyValuesCount("a"));
    }

    /**
     * Test method <code>saveFile(File file, ConfigurationObject config) </code>.
     *
     * @throws Exception
     *             to junit.
     *
     */
    public void testSaveFile_2() throws Exception {
        File file = new File("test_files/accuracy/temp.properties");

        if (file.exists()) {
            file.createNewFile();
        }

        ConfigurationObject object = persistence.loadFile("a", new File("test_files/accuracy/test2.properties"));

        persistence.saveFile(file, object);

        ConfigurationObject ret = persistence.loadFile("a", file);

        ret = ret.getChild("default");

        assertEquals("Equal is expected.", 0, ret.getAllPropertyKeys().length);
    }

    /**
     * Test method <code>saveFile(File file, ConfigurationObject config) </code>.
     *
     * @throws Exception
     *             to junit.
     *
     */
    public void testSaveFile_3() throws Exception {
        File file = new File("test_files/accuracy/temp.properties");

        if (file.exists()) {
            file.createNewFile();
        }

        ConfigurationObject object = persistence.loadFile("a", new File("test_files/accuracy/test3.properties"));

        persistence.saveFile(file, object);

        ConfigurationObject ret = persistence.loadFile("a", file);

        ret = ret.getChild("default");

        assertEquals("Equal is expected.", 10, ret.getPropertyValuesCount("a"));
    }

    /**
     * Test method <code>saveFile(File file, ConfigurationObject config) </code>.
     *
     * @throws Exception
     *             to junit.
     *
     */
    public void testSaveFile_4() throws Exception {
        File file = new File("test_files/accuracy/temp.properties");

        if (file.exists()) {
            file.createNewFile();
        }

        ConfigurationObject object = persistence.loadFile("a", new File("test_files/accuracy/test4.properties"));

        persistence.saveFile(file, object);

        ConfigurationObject ret = persistence.loadFile("a", file);

        object = ret.getChild("default");
        object = object.getChild("a").getChild("b").getChild("c").getChild("e");

        assertEquals("Equal is expected.", 7, object.getPropertyValuesCount("f"));

    }

    /**
     * Test method <code>saveFile(File file, ConfigurationObject config) </code>.
     *
     * @throws Exception
     *             to junit.
     *
     */
    public void testSaveFile_5() throws Exception {
        File file = new File("test_files/accuracy/temp.properties");

        if (file.exists()) {
            file.createNewFile();
        }

        ConfigurationObject object = persistence.loadFile("a", new File("test_files/accuracy/test5.properties"));

        persistence.saveFile(file, object);

        ConfigurationObject ret = persistence.loadFile("a", file);

        object = ret.getChild("default");
        assertEquals("Equal is expected.", 11, object.getPropertyValuesCount("a"));
    }

    /**
     * Test method <code>saveFile(File file, ConfigurationObject config) </code>.
     *
     * @throws Exception
     *             to junit.
     *
     */
    public void testSaveFile_6() throws Exception {
        File file = new File("test_files/accuracy/temp.properties");

        if (file.exists()) {
            file.createNewFile();
        }

        ConfigurationObject object = persistence.loadFile("a", new File("test_files/accuracy/test6.properties"));

        persistence.saveFile(file, object);

        ConfigurationObject ret = persistence.loadFile("a", file);

        object = ret.getChild("default");
        assertEquals("Equal is expected.", 10, object.getPropertyValuesCount("a"));
    }

    /**
     * Test the method <code>ConfigurationObject loadFile(String name, File file) </code>.
     *
     * @throws Exception
     *             to junit.
     *
     */
    public void testLoadFile_1() throws Exception {

        File file = new File("test_files/accuracy/test1.properties");

        ConfigurationObject object = persistence.loadFile("default", file);

        object = object.getChild("default");

        assertEquals("Equal is expected.", 5, object.getPropertyValuesCount("a"));
    }

    /**
     * Test the method <code>ConfigurationObject loadFile(String name, File file) </code>.
     *
     * @throws Exception
     *             to junit.
     *
     */
    public void testLoadFile_2() throws Exception {

        File file = new File("test_files/accuracy/test2.properties");

        ConfigurationObject object = persistence.loadFile("default", file);

        object = object.getChild("default");

        assertEquals("Euqal is expected.", 0, object.getAllChildren().length);

        assertEquals("Equal is expected.", 0, object.getAllPropertyKeys().length);
    }

    /**
     * Test the method <code>ConfigurationObject loadFile(String name, File file) </code>.
     *
     * @throws Exception
     *             to junit.
     *
     */
    public void testLoadFile_3() throws Exception {

        File file = new File("test_files/accuracy/test3.properties");

        ConfigurationObject object = persistence.loadFile("default", file);

        object = object.getChild("default");

        assertEquals("Equal is expected.", 10, object.getPropertyValuesCount("a"));
    }

    /**
     * Test the method <code>ConfigurationObject loadFile(String name, File file) </code>.
     *
     * @throws Exception
     *             to junit.
     *
     */
    public void testLoadFile_4() throws Exception {

        File file = new File("test_files/accuracy/test4.properties");

        ConfigurationObject object = persistence.loadFile("default", file);

        object = object.getChild("default");

        object = object.getChild("a").getChild("b").getChild("c").getChild("e");

        assertEquals("Equal is expected.", 7, object.getPropertyValuesCount("f"));
    }

    /**
     * Test the method <code>ConfigurationObject loadFile(String name, File file) </code>.
     *
     * @throws Exception
     *             to junit.
     *
     */
    public void testLoadFile_5() throws Exception {

        File file = new File("test_files/accuracy/test5.properties");

        ConfigurationObject object = persistence.loadFile("default", file);

        object = object.getChild("default");

        assertEquals("Equal is expected.", 11, object.getPropertyValuesCount("a"));
    }

    /**
     * Test the method <code>ConfigurationObject loadFile(String name, File file) </code>.
     *
     * @throws Exception
     *             to junit.
     *
     */
    public void testLoadFile_6() throws Exception {

        File file = new File("test_files/accuracy/test6.properties");

        ConfigurationObject object = persistence.loadFile("default", file);

        object = object.getChild("default");

        assertEquals("Equal is expected.", 10, object.getPropertyValuesCount("a"));
    }

    /**
     * Test the method <code>ConfigurationObject loadFile(String name, File file) </code>.
     *
     * @throws Exception
     *             to junit.
     *
     */
    public void testLoadFile_7() throws Exception {

        File file = new File("test_files/accuracy/test7.properties");

        ConfigurationObject object = persistence.loadFile("default", file);

        object = object.getChild("default");

        assertEquals("Equal is expected.", 2, object.getPropertyValuesCount("a"));
    }

    /**
     * Test the method <code>ConfigurationObject loadFile(String name, File file) </code>.
     *
     * @throws Exception
     *             to junit.
     *
     */
    public void testLoadFile_8() throws Exception {

        File file = new File("test_files/accuracy/test8.properties");

        ConfigurationObject object = persistence.loadFile("default", file);

        object = object.getChild("default");

        assertEquals("Equal is expected.", 4, object.getPropertyValuesCount("a"));
    }
}
