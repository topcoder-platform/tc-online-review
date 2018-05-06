/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.services.uploads.failuretests;

import com.topcoder.util.config.ConfigManager;

import java.io.File;

import java.util.Iterator;


/**
 * <p>A helper class to perform those common operations which are helpful for the test.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class FailureTestHelper {

    /**
     * Represents the test_files folder.
     */
    public static final String TEST_FILES = "test_files" + File.separator + "failuretests" + File.separator;
    
    /**
     * <p>The sample configuration file for this component.</p>
     */
    public static final String CONFIG_FILE = "test_files" + File.separator + "failuretests" + File.separator +
        "config.xml";

    /**
     * <p>
     * This private constructor prevents to create a new instance.
     * </p>
     */
    private FailureTestHelper() {
    }

    /**
     * <p>Uses the given file to config the configuration manager.</p>
     *
     * @param fileName config file to set up environment
     *
     * @throws Exception when any exception occurs
     */
    public static void loadXMLConfig(String fileName) throws Exception {
        //set up environment
        ConfigManager config = ConfigManager.getInstance();
        File file = new File(fileName);

        config.add(file.getCanonicalPath());
    }

    /**
     * <p>
     * Loads the configuration from the given configuration file with specified namespace.
     * </p>
     *
     * @param namespace namespace under which config will be loaded
     * @param file      the file to load
     *
     * @throws Exception exception to junit.
     */
    public static void loadXMLConfig(String namespace, String file) throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        cm.add(namespace, new File(file).getCanonicalPath(), ConfigManager.CONFIG_XML_FORMAT);
    }

    /**
     * <p>Clears all the namespaces from the configuration manager.</p>
     *
     * @throws Exception to JUnit.
     */
    public static void clearConfig() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator i = cm.getAllNamespaces(); i.hasNext();) {
            cm.removeNamespace((String) i.next());
        }
    }
}
