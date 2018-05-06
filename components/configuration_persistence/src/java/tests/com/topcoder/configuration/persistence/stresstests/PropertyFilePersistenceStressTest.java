/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.configuration.persistence.stresstests;

import java.io.File;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.configuration.persistence.PropertyFilePersistence;

/**
 * <p>
 * Stress test for class <code>PropertyFilePersistence</code>.
 * </p>
 * <p>
 * The class is immutable so thread-safe, so only the performance is tested
 * here.
 * </p>
 * @author fuyun
 * @version 1.0
 */
public class PropertyFilePersistenceStressTest extends TestCase {

    /**
     * Stress test for the performance of saving and loading files.
     * @throws Exception if there is any problem.
     */
    public void testSaveAndLoadFile() throws Exception {

        File file = null;

        try {

            // create new instance.
            PropertyFilePersistence persistence = new PropertyFilePersistence();

            file = new File("test_files/Stress/tmp/testProperty.properties");

            // create the root object.
            ConfigurationObject root = new DefaultConfigurationObject(
                    "namespace");
            // create the default object.
            ConfigurationObject defaultObj = new DefaultConfigurationObject(
                    "default");
            root.addChild(defaultObj);
            for (int i = 0; i < 10; i++) {
                // build the object.
                defaultObj.addChild(StressTestHelper
                        .buildConfigurationObject(i));
            }

            // save the configuration object to file system.
            persistence.saveFile(file, root);

            // then load it.
            root = persistence.loadFile("namespace", file);

            assertEquals("Fails to load property file.", 10, root.getChild(
                    "default").getAllChildren().length);
        } finally {
            StressTestHelper.cleanFolder("test_files/Stress/tmp");
        }

    }

}
