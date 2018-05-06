/*
 * Copyright (c) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.stresstests;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.config.UnknownNamespaceException;

import java.io.File;

import java.util.Iterator;


/**
 * <p>
 * A helper class used for testing, it provides some useful functionality such as loading
 * configuration and clear configuration.
 * </p>
 *
 * @author Wendell
 * @version 2.0
 */
class StressTestHelper {
    /** The configuration file used for testing. */
    public static final String CONFIG = "test_files/stresstests/config.xml";

    /** The namespace used for testing. */
    public static final String NAMESPACE = "com.topcoder.util.objectfactory.stresstests";

    /** The number of times each method will be run. */
    public static final int RUN_TIMES = 10000;

    /** The path of the jar file used for testing. */
    public static final String JAR_PATH = "file://test_files/stresstests/base_exception.jar";

    /**
     * <p>
     * Private constructor to prevent this class being instantiated.
     * </p>
     */
    private StressTestHelper() {
        // empty
    }

    /**
     * <p>
     * Loads configuration from the given file.
     * </p>
     *
     * @param file the file to load configuration from.
     *
     * @throws ConfigManagerException if failed to load configuration from file.
     */
    public static void loadConfig(String file) throws ConfigManagerException {
        ConfigManager cm = ConfigManager.getInstance();
        cm.add(new File(file).getAbsolutePath());
    }

    /**
     * <p>
     * Clears configuration from the configuration manager.
     * </p>
     */
    public static void clearConfig() {
        ConfigManager cm = ConfigManager.getInstance();
        Iterator iter = cm.getAllNamespaces();

        while (iter.hasNext()) {
            try {
                cm.removeNamespace((String) iter.next());
            } catch (UnknownNamespaceException e) {
                // just print the error message
                e.printStackTrace();
            }
        }
    }
}
