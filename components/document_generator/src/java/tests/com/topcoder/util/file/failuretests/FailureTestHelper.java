/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.failuretests;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;

/**
 * Failure test helper.
 * @author extra
 * @version 3.0
 */
class FailureTestHelper {

    /**
     * Default ctor.
     */
    private FailureTestHelper() {
        // do nothing
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
    static ConfigurationObject createConfigurationObject(String fileName, String namespace)
        throws Exception {
        ConfigurationFileManager cfManager = new ConfigurationFileManager(fileName);
        return cfManager.getConfiguration(namespace).getChild(namespace);
    }
}
