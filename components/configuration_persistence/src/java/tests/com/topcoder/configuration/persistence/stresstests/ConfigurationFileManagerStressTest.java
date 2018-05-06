/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.configuration.persistence.stresstests;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;

/**
 * <p>
 * Stress test cases for class <code>ConfigurationFileManager</code>.
 * </p>
 * <p>
 * The class is not designed thread-safe, so only the performance is tested
 * here.
 * </p>
 * @author fuyun
 * @version 1.0
 */
public class ConfigurationFileManagerStressTest extends TestCase {

    /**
     * The ConfigurationFileManager instance used for test.
     */
    private ConfigurationFileManager manager = null;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     * <p>
     * The ConfigurationFileManager instance is created.
     * </p>
     * @throws Exception if there is any problem.
     */
    protected void setUp() throws Exception {
        manager = new ConfigurationFileManager(
                "test_files/Stress/PreLoad.properties");
    }

    /**
     * Stress test for constructor <code>ConfigurationFileManager(String)</code>.
     * @throws Exception if there is any problem.
     */
    public void testConfigurationFileManagerString() throws Exception {
        for (int i = 0; i < 10; i++) {
            new ConfigurationFileManager("test_files/Stress/PreLoad.properties");
        }
    }

    /**
     * Stress test for constructor <code>ConfigurationFileManager(Map)</code>.
     * @throws Exception if there is any problem.
     */
    public void testConfigurationFileManagerMap() throws Exception {
        Map map = new HashMap();

        map.put("namespace1", new File("test_files/Stress/Config.xml"));
        map.put("namespace2", new File("test_files/Stress/Config2.properties"));
        for (int i = 0; i < 10; i++) {
            manager = new ConfigurationFileManager(map);
        }

        assertNotNull("Fails to create ConfigurationFileManager instance.",
                manager.getConfiguration("namespace1"));
        assertNotNull("Fails to create ConfigurationFileManager instance.",
                manager.getConfiguration("namespace2"));

    }

    /**
     * Stress test for method <code>getConfiguration()</code>.
     */
    public void testGetConfiguration() {
        for (int i = 0; i < 10; i++) {
            assertEquals("Fails to get configuration.", 1, manager
                    .getConfiguration().size());
        }
    }

    /**
     * Stress test for method <code>createFile(String, String)</code>.
     * @throws Exception if there is any problem.
     */
    public void testCreateFile() throws Exception {
        ConfigurationObject config = new DefaultConfigurationObject("config");
        ConfigurationObject defaultCfg = new DefaultConfigurationObject(
                "default");
        config.addChild(defaultCfg);
        for (int i = 0; i < 10; i++) {
            defaultCfg.addChild(StressTestHelper.buildConfigurationObject(i));
        }
        try {
            for (int i = 0; i < 10; i++) {
                manager.createFile("namespace_createfile" + i,
                        "test_files/Stress/tmp/createfile.properties", config);
            }
        } finally {
            StressTestHelper.cleanFolder("test_files/Stress/tmp");
        }
    }

    /**
     * Stress test for method <code>getConfiguration(String)</code>.
     * @throws Exception if there is any problem.
     */
    public void testGetConfigurationString() throws Exception {
        for (int i = 0; i < 10; i++) {
            assertNotNull("Fails to get configuration.", manager
                    .getConfiguration("com.stress"));
        }
    }

    /**
     * Stress test for method <code>loadFile(String, String)</code>.
     * @throws Exception if there is any problem.
     */
    public void testLoadFile() throws Exception {
        for (int i = 0; i < 10; i++) {
            manager.loadFile("namespace_loadfile" + i,
                    "test_files/Stress/Config.xml");
        }
    }

    /**
     * Stress test for method <code>refresh</code>.
     * @throws Exception if there is any problem.
     */
    public void testRefresh() throws Exception {
        for (int i = 0; i < 10; i++) {
            manager.refresh();
        }
    }

    /**
     * Stress test for method <code>saveConfiguration</code>.
     * @throws Exception if there is any problem.
     */
    public void testSaveConfiguration() throws Exception {
        ConfigurationObject obj = new DefaultConfigurationObject(
                "namespace_savecfg");

        // build the configuration object.
        for (int i = 0; i < 10; i++) {
            obj.addChild(StressTestHelper.buildConfigurationObject(i));
        }
        // create the file first.
        manager.createFile("namespace_savecfg",
                "test_files/Stress/tmp/saveconfiguration.xml", obj);
        obj = new DefaultConfigurationObject("namespace_savecfg");
        try {
            for (int i = 10; i < 20; i++) {
                obj.addChild(StressTestHelper.buildConfigurationObject(i));
                // save the configuration.
                manager.saveConfiguration("namespace_savecfg", obj);
            }
        } finally {
            StressTestHelper.cleanFolder("test_files/Stress/tmp");
        }
    }

}
