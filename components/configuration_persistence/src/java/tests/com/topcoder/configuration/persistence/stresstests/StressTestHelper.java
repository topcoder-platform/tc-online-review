/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.configuration.persistence.stresstests;

import java.io.File;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

/**
 * The utility class used for the stress test to provide common methods.
 * @author fuyun
 * @version 1.0
 */
final class StressTestHelper {

    /**
     * private constructor to prevent the instantiation.
     *
     */
    private StressTestHelper() {

    }

    /**
     * Builds the ConfigurationObject instance used for testing.
     * @param postfix the postfix of name to distinguish each property.
     * @return the ConfigurationObject instance.
     * @throws Exception if fails to create the ConfigurationObject.
     */
    static ConfigurationObject buildConfigurationObject(int postfix)
        throws Exception {
        ConfigurationObject current = new DefaultConfigurationObject(
                "property_root_" + postfix);
        ConfigurationObject root = current;
        for (int i = 0; i < 10; i++) {
            ConfigurationObject obj = new DefaultConfigurationObject(
                    "property_level_" + i + "_" + postfix);
            current.addChild(obj);
            current = obj;
        }
        current.setPropertyValue("value_property_" + postfix, "value "
                + postfix);
        return root;
    }

    /**
     * Cleans the specified folder.
     * @param folderName the name of the folder to clean.
     */
    static void cleanFolder(String folderName) {
        File folder = new File(folderName);
        if (!folder.exists() || !folder.isDirectory()) {
            return;
        }
        File[] files = folder.listFiles();
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
    }
}
