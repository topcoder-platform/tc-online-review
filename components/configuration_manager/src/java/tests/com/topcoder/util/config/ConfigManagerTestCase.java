/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

/**
 * Tests the behavior of ConfigManager.
 *
 * @author sparemax
 * @version 2.2
 * @since 2.2
 */
public class ConfigManagerTestCase extends TestCase {
    /**
     * <p>
     * Represents a copy of the configuration file.
     * </p>
     */
    private static final String CONFIG_FILE_COPY = TestsHelper.TEST_FILES + "ConfigManager.properties";

    /**
     * <p>
     * Represents the path of the configuration file.
     * </p>
     */
    private static final String CONFIG_FILE = TestsHelper.TEST_FILES + "com" + File.separator + "topcoder"
        + File.separator + "util" + File.separator + "config" + File.separator + "ConfigManager.properties";

    /**
     * The singleton ConfigManager instance.
     */
    private ConfigManager instance = null;

    /**
     * The list of created files.
     */
    private List<File> createdFiles = new ArrayList<File>();

    /**
     * Set up testing environment.
     * Clear the config manager.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        // Restore the default configuration file
        TestsHelper.copyFile(CONFIG_FILE_COPY, CONFIG_FILE);

        instance = ConfigManager.getInstance();
        for (Iterator<String> itr = instance.getAllNamespaces(); itr.hasNext();) {
            instance.removeNamespace((String) itr.next());
        }
    }

    /**
     * Tear down testing environment.
     * Clear the config manager and remove created files.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        for (Iterator<String> itr = instance.getAllNamespaces(); itr.hasNext();) {
            instance.removeNamespace((String) itr.next());
        }
        instance = null;
        for (int i = 0; i < createdFiles.size(); ++i) {
            ((File) createdFiles.get(i)).delete();
        }
        createdFiles.clear();

        // Restore the default configuration file
        TestsHelper.copyFile(CONFIG_FILE_COPY, CONFIG_FILE);
    }

    /**
     * Prepares a properties config file.
     *
     * @return a properties config file.
     * @throws Exception to JUnit.
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
     * <p>
     * Accuracy test for the constructor <code>LateDeliverable()</code>.<br>
     * Instance should be correctly created.
     * </p>
     */
    public void testCtor() {
        instance = new DefaultConfigManager();

        assertTrue("'configRefreshableByDefault' should be correct.",
            (Boolean) TestsHelper.getField(instance, "configRefreshableByDefault"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getURL(String filename)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getURL_1() throws Exception {
        assertNotNull("'getURL' should be correct.", ConfigManager.getURL(TestsHelper.TEST_FILES));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getURL(String filename)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getURL_2() throws Exception {
        assertNotNull("'getURL' should be correct.", ConfigManager.getURL(preparePropertiesFile().getPath()));
    }

    /**
     * <p>
     * Failure test for the method <code>getURL(String filename)</code> with filename does not exist.<br>
     * <code>ConfigManagerException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getURL_filenameNotExist() throws Exception {
        try {
            ConfigManager.getURL(TestsHelper.TEST_FILES + "not_exist");

            fail("ConfigManagerException is expected.");
        } catch (ConfigManagerException cme) {
            // Good
        }
    }

    /**
     * <p>
     * Accuracy test for the method <code>getInstance()</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getInstance_1() throws Exception {
        assertNotNull("'getInstance' should be correct.", instance);
        assertFalse("'getInstance' should be correct.", instance.isConfigRefreshableByDefault());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getInstance()</code> with 'refreshableByDefault' is not specified.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getInstance_2() throws Exception {
        TestsHelper.copyFile(TestsHelper.TEST_FILES + "ConfigManager2.properties", CONFIG_FILE);

        // Set configManagerInstance to null
        setConfigManagerInstanceNull();
        instance = ConfigManager.getInstance();

        assertNotNull("'getInstance' should be correct.", instance);
        assertTrue("'getInstance' should be correct.", instance.isConfigRefreshableByDefault());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getInstance()</code> with 'refreshableByDefault' is not specified.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getInstance_3() throws Exception {
        TestsHelper.copyFile(TestsHelper.TEST_FILES + "ConfigManager3.properties", CONFIG_FILE);

        // Set configManagerInstance to null
        setConfigManagerInstanceNull();
        instance = ConfigManager.getInstance();

        assertNotNull("'getInstance' should be correct.", instance);
        assertFalse("'getInstance' should be correct.", instance.isConfigRefreshableByDefault());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getInstance()</code> with 'implementor' is invalid.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getInstance_implementorInvalid() throws Exception {
        TestsHelper.copyFile(TestsHelper.INVALID_FILES + "ConfigManagerImplementorInvalid.properties", CONFIG_FILE);

        // Set configManagerInstance to null
        setConfigManagerInstanceNull();
        instance = ConfigManager.getInstance();

        assertNotNull("'getInstance' should be correct.", instance);

        assertFalse("'getInstance' should be correct.", instance.isConfigRefreshableByDefault());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getInstance()</code> with file is invalid.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getInstance_FileInvalid() throws Exception {
        TestsHelper.copyFile(TestsHelper.INVALID_FILES + "ConfigManagerFileInvalid.properties", CONFIG_FILE);

        // Set configManagerInstance to null
        setConfigManagerInstanceNull();
        instance = ConfigManager.getInstance();

        assertNotNull("'getInstance' should be correct.", instance);

        assertFalse("'getInstance' should be correct.", instance.isConfigRefreshableByDefault());
    }

    /**
     * <p>
     * Accuracy test for the method <code>isConfigRefreshableByDefault()</code>.<br>
     * The value should be properly retrieved.
     * </p>
     */
    public void test_isConfigRefreshableByDefault() {
        instance = new DefaultConfigManager();

        boolean value = false;
        instance.setConfigRefreshableByDefault(value);

        assertEquals("'isConfigRefreshableByDefault' should be correct.",
            value, instance.isConfigRefreshableByDefault());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setConfigRefreshableByDefault(boolean configRefreshableByDefault)</code>.<br>
     * The value should be properly set.
     * </p>
     */
    public void test_setConfigRefreshableByDefault() {
        instance = new DefaultConfigManager();

        boolean value = false;
        instance.setConfigRefreshableByDefault(value);

        assertEquals("'setConfigRefreshableByDefault' should be correct.",
            value, TestsHelper.getField(instance, "configRefreshableByDefault"));
    }

    /**
     * <p>
     * Sets the 'configManagerInstance' field to <code>null</code>.
     * </p>
     */
    private static void setConfigManagerInstanceNull() {
        try {
            Field fieldObj = ConfigManager.class.getDeclaredField("configManagerInstance");
            fieldObj.setAccessible(true);

            fieldObj.set(null, null);

            fieldObj.setAccessible(false);
        } catch (IllegalAccessException e) {
            // Ignore
        } catch (NoSuchFieldException e) {
            // Ignore
        }
    }
}
