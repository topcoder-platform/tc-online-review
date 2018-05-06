/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.deliverables.failuretests;

import java.io.File;
import java.util.Iterator;

import junit.framework.TestCase;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.config.ConfigManager;

/**
 * A base class for failure tests.
 *
 * @author kinfkong
 * @version 1.0
 */
public class FailureBaseTest extends TestCase {

    /**
     * The configuration file for the DBConnectionFactory.
     */
    public static final String DB_CONFIG =
        "test_files" + File.separator  + "failuretests" + File.separator + "dbconfig.xml";

    /**
     * The invalid DBConnectionFactory for failure tests.
     */
    public static final String INVALID_DB_CONFIG =
        "test_files" + File.separator  + "failuretests" + File.separator + "invalid_dbconfig.xml";

    /**
     * The namespace of a valid DBConnectionFactory.
     */
    public static final String DBFACTORY_NAMESPACE =
        "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /**
     * The invalid config namespace for the failure tests.
     */
    public static final String INVALID_DBFACTORY_NAMESPACE = "com.topcoder.db.connectionfactory.invalid";

    /**
     * An instance of ConfigManager to load the configurations.
     */
    private ConfigManager cm = ConfigManager.getInstance();

    /**
     * Sets up the environment for failure tests.
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        loadConfig();
    }

    /**
     * Clears the test environment.
     *
     * @throws Exception to JUnit
     */
    public void tearDown() throws Exception {
        clearNamespaces();
    }

    /**
     * Loads the config files to the ConfigManager.
     *
     * @throws Exception to JUnit
     */
    private void loadConfig() throws Exception {
        // load the db config file
        File file = new File(DB_CONFIG);
        cm.add(file.getCanonicalPath());

        // load the invalid db config file
        file = new File(INVALID_DB_CONFIG);
        cm.add(file.getCanonicalPath());
    }

    /**
     * Clears the namespaces of the ConfigManager.
     *
     * @throws Exception to JUnit
     */
    private void clearNamespaces() throws Exception {
        // find all the existing namespaces.
        for (Iterator it = cm.getAllNamespaces(); it.hasNext();) {
            String namespace = (String) it.next();
            if (cm.existsNamespace(namespace)) {
                cm.removeNamespace(namespace);
            }
        }
    }

    /**
     * Returns the valid connection factory that can connect to database.
     *
     * @return the DBConnectionFactory the valid connection factory that can connect to database.
     *
     * @throws Exception to JUnit
     */
    protected DBConnectionFactory getValidConnectionFactory() throws Exception {
        DBConnectionFactory factory = new DBConnectionFactoryImpl(DBFACTORY_NAMESPACE);
        return factory;
    }

    /**
     * Returns the invalid connection factory that can connect to database.
     *
     * @return the DBConnectionFactory the invalid connection factory that can connect to database.
     *
     * @throws Exception to JUnit
     */
    protected DBConnectionFactory getInvalidConnectionFactory() throws Exception {
        DBConnectionFactory factory = new DBConnectionFactoryImpl(INVALID_DBFACTORY_NAMESPACE);
        return factory;
    }
}
