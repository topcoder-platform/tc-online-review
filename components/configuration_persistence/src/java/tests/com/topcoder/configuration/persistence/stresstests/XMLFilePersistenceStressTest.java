/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.configuration.persistence.stresstests;

import java.io.File;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;

/**
 * <p>
 * Stress test for class <code>XMLFilePersistence</code>.
 * </p>
 * <p>
 * The class is immutable so thread-safe, so only the performance is tested
 * here.
 * </p>
 * @author fuyun
 * @version 1.0
 */
public class XMLFilePersistenceStressTest extends TestCase {

    /**
     * Stress test for the performance of saving and loading files.
     * @throws Exception if there is any problem.
     */
    public void testSaveAndLoadFile() throws Exception {

        File file = null;

        try {
            XMLFilePersistence persistence = new XMLFilePersistence();

            file = new File("test_files/Stress/tmp/testXML.xml");

            // create the default object.
            ConfigurationObject defaultObject = new DefaultConfigurationObject(
                    "default");
            for (int i = 0; i < 10; i++) {
                defaultObject.addChild(StressTestHelper
                        .buildConfigurationObject(i));
            }
            // create the root object.
            ConfigurationObject root = new DefaultConfigurationObject(
                    "namespace");
            root.addChild(defaultObject);

            // build the configuration object.
            for (int i = 0; i < 50; i++) {
                ConfigurationObject namespace = new DefaultConfigurationObject(
                        "namespace_" + i);
                for (int j = 0; j < 10; j++) {
                    namespace.addChild(StressTestHelper
                            .buildConfigurationObject(j));
                }
                root.addChild(namespace);
            }

            // save the file to file system.
            persistence.saveFile(file, root);

            // load the saved file.
            root = persistence.loadFile("namespace", file);

            assertEquals("Fails to load XML file.", 51, root.getAllChildren().length);
        } finally {
            StressTestHelper.cleanFolder("test_files/Stress/tmp");
        }

    }

}
