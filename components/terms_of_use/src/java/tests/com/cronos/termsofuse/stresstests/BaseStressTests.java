/**
 * Copyright (c) 2011, TopCoder, Inc. All rights reserved
 */
package com.cronos.termsofuse.stresstests;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.sql.Connection;

/**
 * <p>This is base class for all stress tests.</p>
 *
 * @author jmn
 * @version 1.0
 */
public abstract class BaseStressTests {

    /**
     * <p>Represents the path to directory with configuration files.</p>
     */
    private static final String CONFIG_DIRECTORY = "test_files/stresstests";

    /**
     * <p>Represents the name of the file with configuration.</p>
     */
    private static final String CONFIG_NAME = "config.xml";

    /**
     * <p>Sets up the test environment.</p>
     *
     * @throws Exception if any error occurs
     */
    @Before
    public void setUp() throws Exception {
        StressTestHelper.clearDatabase(getConnection());
        StressTestHelper.initDatabase(getConnection());
    }

    /**
     * <p>Tears down the test environment.</p>
     *
     * @throws Exception if any error occurs
     */
    @After
    public void tearDown() throws Exception {

        StressTestHelper.clearDatabase(getConnection());
    }

    /**
     * Retrieves the configuration name for this test.
     *
     * @return the configuration name for this test
     */
    protected abstract String getConfigName();

    /**
     * Retrieves the {@link Connection} object.
     *
     * @return the {@link Connection} object
     *
     * @throws Exception if any error occurs when loading the configuration
     */
    protected Connection getConnection() throws Exception {

        ConfigurationObject configurationObject = loadConfiguration();

        DBConnectionFactoryImpl dbConnectionFactory = new DBConnectionFactoryImpl(
                configurationObject.getChild("dbConnectionFactoryConfig"));

        return dbConnectionFactory.createConnection();
    }

    /**
     * Loads the configuration from the default file.
     *
     * @return the loaded configuration object
     *
     * @throws Exception if any error occurs when loading the configuration
     */
    protected ConfigurationObject loadConfiguration() throws Exception {

        XMLFilePersistence persistence = new XMLFilePersistence();
        ConfigurationObject configurationObject = persistence.loadFile(getConfigName(),
                new File(CONFIG_DIRECTORY, CONFIG_NAME));

        return configurationObject.getChild(getConfigName());
    }
}
