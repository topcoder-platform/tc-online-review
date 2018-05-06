/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.failuretests;

import com.topcoder.file.transfer.registry.FileSystemRegistry;
import com.topcoder.file.transfer.registry.FileSystemXmlRegistry;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import java.util.Iterator;


/**
 * Helper class provides some utilities for testing purpose.
 *
 * @author fairytale
 * @version 1.0
 */
final class FailureHelper {
    /** Configurations for DB connection. */
    private static final String DB_CONFIG_FILE = "DBConnectionFactoryImpl.xml";

    /** Configurations for DB connection. */
    private static final String MF_CONFIG_FILE = "failure/MessageFactoryConfig.xml";

    /** The name of the id generator used in test. */
    private static final String IDGEN_NAME = "failureIdGen";

    /** The files file persistence location. */
    private static final String FILES_FILE_LOCATION = "test_files/failure/filesfile.xml";

    /** The groups file persistence location. */
    private static final String GROUPS_FILE_LOCATION = "test_files/failure/groupsfile.xml";

    /**
     * Private constructor.
     */
    private FailureHelper() {
    }

    /**
     * Load configurations.
     *
     * @throws Exception to Junit.
     */
    public static void loadConfigs() throws Exception {
        clearConfigs();

        ConfigManager cm = ConfigManager.getInstance();
        cm.add(DB_CONFIG_FILE);
        cm.add(MF_CONFIG_FILE);
    }

    /**
     * Clear configurations.
     *
     * @throws Exception to Junit.
     */
    public static void clearConfigs() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
            cm.removeNamespace((String) iter.next());
        }
    }

    /**
     * Get IDGenerator.
     *
     * @return the idgenerator.
     *
     * @throws Exception to Junit.
     */
    public static IDGenerator getIDGenerator() throws Exception {
        return IDGeneratorFactory.getIDGenerator(IDGEN_NAME);
    }

    /**
     * Get an instance of FileSystemXmlRegistry.
     *
     * @return an instance of FileSystemXmlRegistry.
     *
     * @throws Exception to Junit.
     */
    public static FileSystemRegistry getFileSystemXMLRegistry()
        throws Exception {
        return new FileSystemXmlRegistry(FILES_FILE_LOCATION, GROUPS_FILE_LOCATION, getIDGenerator());
    }
}
