/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import java.io.FileReader;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;

/**
 * <p>
 * A helper class to perform those common operations which are helpful for the test.
 * </p>
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class TestHelper {
    /**
     *  The base directory for testing.
     */
    public static final String TEST_FILES_DIR = "test_files/";

    /**
     * <p>
     * This private constructor prevents to create a new instance.
     * </p>
     */
    private TestHelper() {
    }

    /**
     * <p>
     * Uses the given file to create the ConfigurationObject.
     * </p>
     * @param fileName
     *            name of configuration file
     * @param namespace
     *            the namespace of configuration object
     * @return the created the ConfigurationObject.
     * @throws Exception
     *             when any exception occurs
     */
    public static ConfigurationObject createConfigurationObject(String fileName, String namespace)
        throws Exception {
        ConfigurationFileManager cfManager = new ConfigurationFileManager(fileName);
        return cfManager.getConfiguration(namespace).getChild(namespace);
    }

    /**
     * read the given file content.
     * @param fileName
     *            name of file to read
     * @return the file content
     * @throws Exception
     *             to junit
     */
    public static String readFile(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        try {
            return Util.readString(reader);
        } finally {
            reader.close();
        }
    }
}
