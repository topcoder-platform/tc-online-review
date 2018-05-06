/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.persistence.accuracy;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.configuration.persistence.PropertyFilePersistence;
import com.topcoder.configuration.persistence.XMLFilePersistence;

import junit.framework.TestCase;

/**
 * Accuracy test cases for class <code>ConfigurationFileManager </code>.
 *
 * @author Chenhong
 * @version 1.0
 */
public class TestConfigurationFileManagerAccuracy extends TestCase {

    /**
     * Represents the ConfigurationFileManager instance for testing.
     */
    private ConfigurationFileManager manager = null;

    /**
     * Test the constructor <code>ConfigurationFileManager() </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testConfigurationFileManager() throws Exception {
        manager = new ConfigurationFileManager();

        assertNotNull("Should not be null.", manager);
    }

    /**
     * Test constructor <code>ConfigurationFileManager(String fileName) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testConfigurationFileManagerString_1() throws Exception {
        manager = new ConfigurationFileManager("test_files/accuracy/test1.xml");
        assertNotNull("Should not be null.", manager);

        assertEquals("Equal is expected.", 0, manager.getConfiguration().size());
    }

    /**
     * Test constructor <code>ConfigurationFileManager(String fileName) </code>.
     * <p>
     * A property file will be loaded.
     * </p>
     *
     * @throws Exception
     *             to junit.
     */
    public void testConfigurationFileManagerString_2() throws Exception {
        manager = new ConfigurationFileManager("test_files/accuracy/manager1.properties");
        assertNotNull("Should not be null.", manager);

        assertEquals("Equal is expected.", 2, manager.getConfiguration().size());
    }

    /**
     * Test constructor <code>ConfigurationFileManager(String fileName, Map persistenceMap) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testConfigurationFileManagerStringMap_1() throws Exception {
        Map p = new HashMap();
        p.put(".xml", new XMLFilePersistence());
        p.put(".properties", new PropertyFilePersistence());

        manager = new ConfigurationFileManager("test_files/accuracy/manager1.properties", p);

        assertNotNull("Should not be null.", manager);
        assertEquals("Equal is expected.", 2, manager.getConfiguration().size());
    }

    /**
     * Test constructor <code>ConfigurationFileManager(String fileName, Map persistenceMap) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testConfigurationFileManagerStringMap_2() throws Exception {
        Map p = new HashMap();
        p.put(".xml", new XMLFilePersistence());
        p.put(".properties", new PropertyFilePersistence());

        manager = new ConfigurationFileManager("test_files/accuracy/manager_empty.properties", p);

        assertNotNull("Should not be null.", manager);
        assertEquals("Equal is expected.", 0, manager.getConfiguration().size());
    }

    /**
     * Test the constructor <code>ConfigurationFileManager(ConfigurationObject config) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testConfigurationFileManagerConfigurationObject_1() throws Exception {
        ConfigurationObject object = new DefaultConfigurationObject("config");
        ConfigurationObject file = new DefaultConfigurationObject("files");

        object.addChild(file);
        file.setPropertyValues("file1", new Object[] { "namespace1",
                "test_files/accuracy/DBConnectionFactory.xml" });

        manager = new ConfigurationFileManager(object);

        assertEquals("Equal is expected.", 1, manager.getConfiguration().size());

        XMLFilePersistence persistence = new XMLFilePersistence();

        File fileToCompare = new File("test_files/accuracy/file/restore1_DBConnectionFactory.xml");
        persistence.saveFile(fileToCompare, manager.getConfiguration("namespace1"));
    }

    /**
     * Test the constructor <code>ConfigurationFileManager(ConfigurationObject config) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testConfigurationFileManagerConfigurationObject_2() throws Exception {
        ConfigurationObject object = new DefaultConfigurationObject("config");
        ConfigurationObject file = new DefaultConfigurationObject("files");

        object.addChild(file);

        String namespace = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";
        file.setPropertyValues("file1", new Object[] { namespace,
                "test_files/accuracy/DBConnectionFactory.xml" });

        ConfigurationObject persistenceObjects = new DefaultConfigurationObject("persistence");

        object.addChild(persistenceObjects);

        persistenceObjects.setPropertyValues(".xml", new Object[] { ".xml", new XMLFilePersistence() });
        persistenceObjects.setPropertyValues(".properties", new Object[] { ".properties",
                new PropertyFilePersistence() });

        manager = new ConfigurationFileManager(object);

        assertEquals("Equal is expected.", 1, manager.getConfiguration().size());

        XMLFilePersistence persistence = new XMLFilePersistence();

        File fileToCompare = new File("test_files/accuracy/file/restore2_DBConnectionFactory.xml");
        persistence.saveFile(fileToCompare, manager.getConfiguration(namespace));
    }

    /**
     * Test the constructor <code>ConfigurationFileManager(ConfigurationObject config) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testConfigurationFileManagerConfigurationObject_3() throws Exception {
        ConfigurationObject object = new DefaultConfigurationObject("config");
        ConfigurationObject file = new DefaultConfigurationObject("files");

        object.addChild(file);

        String namespace = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";
        file.setPropertyValues("file1", new Object[] { namespace,
                "test_files/accuracy/DBConnectionFactory.xml" });
        file.setPropertyValues("file2", new Object[] { "ns1", "test_files/accuracy/test6.xml" });

        ConfigurationObject persistenceObjects = new DefaultConfigurationObject("persistence");

        object.addChild(persistenceObjects);

        persistenceObjects.setPropertyValues(".xml", new Object[] { ".xml", new XMLFilePersistence() });
        persistenceObjects.setPropertyValues(".properties", new Object[] { ".properties",
                new PropertyFilePersistence() });

        manager = new ConfigurationFileManager(object);

        assertEquals("Equal is expected.", 2, manager.getConfiguration().size());

        XMLFilePersistence persistence = new XMLFilePersistence();

        File fileToCompare = new File("test_files/accuracy/file/restore_test6.xml");
        persistence.saveFile(fileToCompare, manager.getConfiguration("ns1"));
    }

    /**
     * Test the constructor <code>ConfigurationFileManager(Map config) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testConfigurationFileManagerMap_1() throws Exception {
        Map map = new HashMap();
        map.put("namespace1", new File("test_files/accuracy/test1.properties"));
        map.put("namespace2", new File("test_files/accuracy/test4.properties"));

        manager = new ConfigurationFileManager(map);

        assertEquals("Equal is expected.", 2, manager.getConfiguration().size());
    }

    /**
     * Test the constructor <code>ConfigurationFileManager(Map config) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testConfigurationFileManagerMap_2() throws Exception {
        Map map = new HashMap();

        manager = new ConfigurationFileManager(map);

        assertEquals("Equal is expected.", 0, manager.getConfiguration().size());
    }

    /**
     * Test the constructor <code>ConfigurationFileManager(Map config) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testConfigurationFileManagerMap_3() throws Exception {
        Map map = new HashMap();
        map.put("ns", new File("test_files/accuracy/test7.xml"));

        manager = new ConfigurationFileManager(map);

        assertEquals("Equal is expected.", 1, manager.getConfiguration().size());
    }

    /**
     * Test the constructor <code>ConfigurationFileManager(Map config) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testConfigurationFileManagerMap_4() throws Exception {
        Map map = new HashMap();
        map.put("ns", new File("test_files/accuracy/test6.xml"));
        map.put("ns2", new File("test_files/accuracy/DBConnectionFactory.xml"));

        manager = new ConfigurationFileManager(map);

        assertEquals("Equal is expected.", 2, manager.getConfiguration().size());

        ConfigurationObject root = manager.getConfiguration("ns2");

        String namespace = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";
        root = root.getChild(namespace);

        assertNotNull("Should not be null.", root);

        root = root.getChild("connections");
        assertEquals("Equal is expected.", "sysuser", root.getPropertyValue("default"));
    }

    /**
     * Test constructor <code> ConfigurationFileManager(Map config, Map persistenceMap) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testConfigurationFileManagerMapMap_1() throws Exception {
        Map map = new HashMap();
        map.put("namespace1", new File("test_files/accuracy/test1.properties"));
        map.put("namespace2", new File("test_files/accuracy/test4.properties"));

        Map persistenceMap = new HashMap();

        persistenceMap.put(".xml", new XMLFilePersistence());
        persistenceMap.put(".properties", new PropertyFilePersistence());

        manager = new ConfigurationFileManager(map, persistenceMap);

        assertEquals("Equal is expected.", 2, manager.getConfiguration().size());
    }

    /**
     * Test constructor <code> ConfigurationFileManager(Map config, Map persistenceMap) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testConfigurationFileManagerMapMap_2() throws Exception {
        Map map = new HashMap();
        map.put("namespace1", new File("test_files/accuracy/test1.properties"));
        map.put("namespace2", new File("test_files/accuracy/test4.xml"));

        Map persistenceMap = new HashMap();

        persistenceMap.put(".xml", new XMLFilePersistence());
        persistenceMap.put(".properties", new PropertyFilePersistence());

        manager = new ConfigurationFileManager(map, persistenceMap);

        assertEquals("Equal is expected.", 2, manager.getConfiguration().size());
    }

    /**
     * Test method <code> Map getConfiguration() </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testGetConfiguration() throws Exception {
        Map map = new HashMap();
        map.put("namespace1", new File("test_files/accuracy/test1.properties"));

        Map persistenceMap = new HashMap();

        persistenceMap.put(".xml", new XMLFilePersistence());
        persistenceMap.put(".properties", new PropertyFilePersistence());

        manager = new ConfigurationFileManager(map, persistenceMap);

        Map config = manager.getConfiguration();
        assertEquals("Equal is expected.", 1, config.size());

        ConfigurationObject object = (ConfigurationObject) config.get("namespace1");
        object = object.getChild("default");
        assertEquals("Equal is expected.", 5, object.getPropertyValuesCount("a"));
    }

    /**
     * Test method <code>ConfigurationObject getConfiguration(String namespace) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testGetConfigurationString() throws Exception {
        Map map = new HashMap();
        map.put("namespace1", new File("test_files/accuracy/test1.properties"));

        Map persistenceMap = new HashMap();

        persistenceMap.put(".xml", new XMLFilePersistence());
        persistenceMap.put(".properties", new PropertyFilePersistence());

        manager = new ConfigurationFileManager(map, persistenceMap);

        ConfigurationObject object = manager.getConfiguration("namespace1");
        object = object.getChild("default");
        assertEquals("Equal is expected.", 5, object.getPropertyValuesCount("a"));
    }

    /**
     * Test method <code> void loadFile(String namespace, String fileName) </code>.
     *
     * <p>
     * In this test case, an xml file will be loaded.
     * </p>
     *
     * @throws Exception
     *             to junit.
     *
     */
    public void testLoadFile_1() throws Exception {
        Map map = new HashMap();
        map.put("namespace1", new File("test_files/accuracy/test1.properties"));

        Map persistenceMap = new HashMap();

        persistenceMap.put(".xml", new XMLFilePersistence());
        persistenceMap.put(".properties", new PropertyFilePersistence());

        manager = new ConfigurationFileManager(map, persistenceMap);

        assertEquals("Equal is expected.", 1, manager.getConfiguration().size());

        manager.loadFile("namespace2", "test_files/accuracy/test2.xml");

        assertEquals("Equal is expected.", 2, manager.getConfiguration().size());

        ConfigurationObject object = manager.getConfiguration("namespace2");

        object = object.getChild("namespace");

        assertEquals("Equal is expected.", 5, object.getPropertyValuesCount("test1"));
    }

    /**
     * Test method <code> void loadFile(String namespace, String fileName) </code>.
     *
     * <p>
     * In this test case, a properties file will be loaded.
     * </p>
     *
     * @throws Exception
     *             to junit.
     *
     */
    public void testLoadFile_2() throws Exception {
        Map map = new HashMap();
        map.put("namespace1", new File("test_files/accuracy/test1.properties"));

        Map persistenceMap = new HashMap();

        persistenceMap.put(".xml", new XMLFilePersistence());
        persistenceMap.put(".properties", new PropertyFilePersistence());

        manager = new ConfigurationFileManager(map, persistenceMap);

        assertEquals("Equal is expected.", 1, manager.getConfiguration().size());

        manager.loadFile("test7", "test_files/accuracy/test7.properties");

        assertEquals("Equal is expected.", 2, manager.getConfiguration().size());

        ConfigurationObject object = manager.getConfiguration("test7");

        object = object.getChild("default");

        assertEquals("Equal is expected.", 2, object.getPropertyValuesCount("a"));
    }

    /**
     * Test method <code>void createFile(String namespace, String fileName,
     *       ConfigurationObject config) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testCreateFile() throws Exception {
        Map map = new HashMap();
        map.put("namespace1", new File("test_files/accuracy/test1.properties"));

        Map persistenceMap = new HashMap();

        persistenceMap.put(".xml", new XMLFilePersistence());
        persistenceMap.put(".properties", new PropertyFilePersistence());

        manager = new ConfigurationFileManager(map, persistenceMap);

        assertEquals("Equal is expected.", 1, manager.getConfiguration().size());

        manager.loadFile("namespace2", "test_files/accuracy/test2.xml");

        assertEquals("Equal is expected.", 2, manager.getConfiguration().size());

        ConfigurationObject object = manager.getConfiguration("namespace2");

        manager.createFile("newNamespace", "test_files/accuracy/file/create.xml", object);

        XMLFilePersistence xmlFilePersistence = new XMLFilePersistence();

        ConfigurationObject ret = xmlFilePersistence.loadFile("namespace", new File(
                "test_files/accuracy/file/create.xml"));

        ret = ret.getChild("namespace");

        assertEquals("Equal is expected.", 5, ret.getPropertyValuesCount("test1"));
    }

    /**
     * Test method <code> void refresh() </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testRefresh() throws Exception {
        try {
            Map map = new HashMap();
            map.put("namespace1", new File("test_files/accuracy/test.properties"));

            Map persistenceMap = new HashMap();

            persistenceMap.put(".xml", new XMLFilePersistence());
            persistenceMap.put(".properties", new PropertyFilePersistence());

            manager = new ConfigurationFileManager(map, persistenceMap);

            assertEquals("Equal is expected.", 1, manager.getConfiguration().size());

            ConfigurationObject object = manager.getConfiguration("namespace1");

            ConfigurationObject o = object;

            ConfigurationObject original = (ConfigurationObject) object.clone();

            object = object.getChild("default");

            // the original setting is a=1;2;3;4;5. There are 5 values for key "a";
            assertEquals("Equal is expected.", 5, object.getPropertyValuesCount("a"));

            object.clearProperties();
            object.setPropertyValues("a", new String[] { "1", "2", "3" });

            // now we use a PropertyFilePersistence to change the setting for test.properties.
            // now the setting is changed to be a=1;2;3
            PropertyFilePersistence persistence = new PropertyFilePersistence();
            persistence.saveFile(new File("test_files/accuracy/test.properties"), o);

            // reflesh the setting.
            manager.refresh();

            object = manager.getConfiguration("namespace1");

            object = object.getChild("default");

            // after refleshing, the configuration will be reloaded.And this time the
            // count for key "a" should be changed to 3 now.
            assertEquals("Equal is expected.", 3, object.getPropertyValuesCount("a"));
        } finally {

            FileWriter writer = new FileWriter(new File("test_files/accuracy/test.properties"));
            writer.write("a=1\n");
            writer.write("a=2\n");
            writer.write("a=3\n");
            writer.write("a=4\n");
            writer.write("a=5\n");

            writer.close();
        }

    }

    /**
     * Test method <code>void refresh(String namespace)  </code>.
     * <p>
     * In this test case, an ConfigurationPersistence is used for change the file to be loaded. After refreshing, the
     * newly modified file should be loaded.
     * </p>
     *
     * @throws Exception
     *             to junit.
     */
    public void testRefreshString() throws Exception {
        try {
            Map map = new HashMap();
            map.put("namespace1", new File("test_files/accuracy/test.properties"));

            Map persistenceMap = new HashMap();

            persistenceMap.put(".xml", new XMLFilePersistence());
            persistenceMap.put(".properties", new PropertyFilePersistence());

            manager = new ConfigurationFileManager(map, persistenceMap);

            assertEquals("Equal is expected.", 1, manager.getConfiguration().size());

            ConfigurationObject object = manager.getConfiguration("namespace1");

            ConfigurationObject o = object;

            ConfigurationObject original = (ConfigurationObject) object.clone();

            object = object.getChild("default");

            // the original setting is a=1;2;3;4;5. There are 5 values for key "a";
            assertEquals("Equal is expected.", 5, object.getPropertyValuesCount("a"));

            object.clearProperties();
            object.setPropertyValues("a", new String[] { "1", "2", "3" });

            // now we use a PropertyFilePersistence to change the setting for test.properties.
            // now the setting is changed to be a=1;2;3
            PropertyFilePersistence persistence = new PropertyFilePersistence();
            persistence.saveFile(new File("test_files/accuracy/test.properties"), o);

            // reflesh the setting.
            manager.refresh("namespace1");

            object = manager.getConfiguration("namespace1");

            object = object.getChild("default");

            // after refleshing, the configuration will be reloaded.And this time the
            // count for key "a" should be changed to 3 now.
            assertEquals("Equal is expected.", 3, object.getPropertyValuesCount("a"));

        } finally {
            FileWriter writer = new FileWriter(new File("test_files/accuracy/test.properties"));
            writer.write("a=1\n");
            writer.write("a=2\n");
            writer.write("a=3\n");
            writer.write("a=4\n");
            writer.write("a=5\n");

            writer.close();

        }
    }

    /**
     * Test method <code>void saveConfiguration(Map config) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testSaveConfigurationMap() throws Exception {
        try {
            Map map = new HashMap();
            map.put("namespace1", new File("test_files/accuracy/test_save.properties"));

            Map persistenceMap = new HashMap();

            persistenceMap.put(".xml", new XMLFilePersistence());
            persistenceMap.put(".properties", new PropertyFilePersistence());

            manager = new ConfigurationFileManager(map, persistenceMap);

            assertEquals("Equal is expected.", 1, manager.getConfiguration().size());

            Map configuration = manager.getConfiguration();

            ConfigurationObject object = (ConfigurationObject) configuration.get("namespace1");

            object = object.getChild("default");

            // add a new Property into the Configuration.
            object.setPropertyValue("user", "userName");

            // save the Configuration.
            manager.saveConfiguration(configuration);

            // refresh, loading the currently updated configuration settings.
            manager.refresh();

            ConfigurationObject ret = manager.getConfiguration("namespace1");

            ret = ret.getChild("default");

            // check the "user", "userName" must be in the configuration.
            assertEquals("Equal is expected.", "userName", ret.getPropertyValue("user"));

        } finally {
            FileWriter writer = new FileWriter(new File("test_files/accuracy/test_save.properties"));
            writer.write("a=1000;100;10;1\n");
            writer.close();
        }
    }

    /**
     * Test method <code>void saveConfiguration(String namespace, ConfigurationObject config) </code>.
     * <p>
     * In this test case, a new property will be added and checked if the saving is correct.
     * </p>
     *
     * @throws Exception
     *             to junit.
     */
    public void testSaveConfigurationStringConfigurationObject_1() throws Exception {
        try {
            Map map = new HashMap();
            map.put("namespace1", new File("test_files/accuracy/test_save_2_1.properties"));

            Map persistenceMap = new HashMap();

            persistenceMap.put(".properties", new PropertyFilePersistence());

            manager = new ConfigurationFileManager(map, persistenceMap);

            assertEquals("Equal is expected.", 1, manager.getConfiguration().size());

            ConfigurationObject object = manager.getConfiguration("namespace1");

            // keep a reference for the ConfigurationObject.
            ConfigurationObject obj = object;

            object = object.getChild("default");

            assertNull("No property for 'user'", object.getPropertyValue("user"));

            // set a Property values "user" and "userName";
            object.setPropertyValue("user", "userName");

            // save the current configuration.
            manager.saveConfiguration("namespace1", obj);

            // reload the configuration setting.
            manager.refresh("namespace1");

            object = manager.getConfiguration("namespace1");

            object = object.getChild("default");

            // check if the ConfigurationObject is saved correctly.
            assertEquals("Equal is expected.", "userName", object.getPropertyValue("user"));
        } finally {
            FileWriter writer = new FileWriter(new File("test_files/accuracy/test_save_2_1.properties"));
            writer.write("a=1000;100;10;1\n");
            writer.close();
        }
    }

    /**
     * Test method <code>void saveConfiguration(String namespace, ConfigurationObject config) </code>.
     * <p>
     * After loading the configuration, modify some properties, save the configuration.
     * </p>
     *
     * <p>
     * Andcheck if the changed is done.
     * </p>
     *
     * @throws Exception
     *             to junit.
     */
    public void testSaveConfigurationStringConfigurationObject_2() throws Exception {
        try {
            Map map = new HashMap();
            map.put("namespace1", new File("test_files/accuracy/test_save_2_2.properties"));

            Map persistenceMap = new HashMap();

            persistenceMap.put(".properties", new PropertyFilePersistence());

            manager = new ConfigurationFileManager(map, persistenceMap);

            assertEquals("Equal is expected.", 1, manager.getConfiguration().size());

            ConfigurationObject object = manager.getConfiguration("namespace1");

            // keep a reference for the ConfigurationObject.
            ConfigurationObject obj = object;

            object = object.getChild("default");

            assertNull("No property for 'user'", object.getPropertyValue("user"));

            // clear all the Properties.
            object.clearProperties();

            object.setPropertyValue("a", "abc");

            // save the current configuration.
            manager.saveConfiguration("namespace1", obj);

            // reload the configuration setting.
            manager.refresh("namespace1");

            object = manager.getConfiguration("namespace1");

            object = object.getChild("default");

            assertEquals("Equal is expected.", "abc", object.getPropertyValue("a"));
        } finally {
            FileWriter writer = new FileWriter(new File("test_files/accuracy/test_save_2_2.properties"));
            writer.write("a=1000;100;10;1\n");
            writer.close();
        }
    }

    /**
     * Test method <code>void saveConfiguration(String namespace, ConfigurationObject config) </code>.
     *
     * <p>
     * In this test case, first load the config from file, second clear all properties and save the current config.
     * </p>
     *
     * <p>
     * then check if the properties are cleared.
     * </p>
     *
     * @throws Exception
     *             to junit.
     */
    public void testSaveConfigurationStringConfigurationObject_3() throws Exception {
        try {
            Map map = new HashMap();
            map.put("namespace1", new File("test_files/accuracy/test_save_2_3.properties"));

            Map persistenceMap = new HashMap();

            persistenceMap.put(".properties", new PropertyFilePersistence());

            manager = new ConfigurationFileManager(map, persistenceMap);

            assertEquals("Equal is expected.", 1, manager.getConfiguration().size());

            ConfigurationObject object = manager.getConfiguration("namespace1");

            // keep a reference for the ConfigurationObject.
            ConfigurationObject obj = object;

            object = object.getChild("default");

            object.clearProperties();

            // save the current configuration.
            manager.saveConfiguration("namespace1", obj);

            // reload the configuration setting.
            manager.refresh("namespace1");

            object = manager.getConfiguration("namespace1");

            object = object.getChild("default");

            assertEquals("Equal is expected.", -1, object.getPropertyValuesCount("a"));
            assertEquals("Equal is expected.", -1, object.getPropertyValuesCount("b"));
        } finally {
            FileWriter writer = new FileWriter(new File("test_files/accuracy/test_save_2_3.properties"));
            writer.write("a=1000;100;10;1\n");
            writer.write("b=1000");
            writer.close();
        }
    }
}
