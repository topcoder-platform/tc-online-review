/*
 * Copyright (C) 2007-2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.services.uploads.stresstests;

import com.topcoder.util.config.ConfigManager;

import java.io.File;
import java.util.Iterator;

/**
 * <p>
 * Helper class for stress tests.
 * </p>
 *
 * @author Thinfox, moon.river
 * @version 1.1
 * @since 1.0
 */
public class StressTestHelper {

    /**
     * Represents the test_files folder.
     */
    public static final String TEST_FILES = "stresstests" + File.separator;

    /**
     * The total running times.
     */
    public static final long TOTOL = 100;
    /**
     * The project id used for testing.
     */
    public static final long PROJECT_ID = 10;

    /**
     * The project phase id used for testing.
     */
    public static final long PROJECT_PHASE_ID = 1;

    /**
     * The submission id used for testing.
     */
    public static final long SUBMISSION_ID = 1001;

    /**
     * The submission status id used for testing.
     */
    public static final long SUBMISSION_STATUS_ID = 1;

    /**
     * The user id used for testing.
     */
    public static final long USER_ID = 600;

    /**
     * The end point of the Axis UploadService.
     */
    public static final String END_POINT = "http://localhost:8080/axis/services/UploadService";

    /**
     * The test file name.
     */
    public static final String FILE = "test.jar";

    /**
     * <p>
     * Empty constructor for utility class.
     * </p>
     */
    private StressTestHelper() {
        // empty
    }

    /**
     * <p>
     * Use the given file to config the configuration manager.
     * </p>
     *
     * @param fileName config file to set up environment
     *
     * @throws Exception to JUnit.
     */
    public static void loadConfig(String fileName) throws Exception {
        ConfigManager config = ConfigManager.getInstance();
        config.add(TEST_FILES + fileName);
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
    public static void loadConfig(String namespace, String file) throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        cm.add(namespace, TEST_FILES + file, ConfigManager.CONFIG_XML_FORMAT);
    }

    /**
     * <p>
     * Clear all the name spaces from the configuration manager.
     * </p>
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
