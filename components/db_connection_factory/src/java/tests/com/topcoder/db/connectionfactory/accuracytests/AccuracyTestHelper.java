/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.accuracytests;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.Property;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import java.sql.Connection;
import java.sql.DriverManager;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;


/**
 * <p>
 * Utility class. Provides utilities used in this accuracy test.
 * </p>
 *
 * @author cosherx
 * @version 1.0
 */
class AccuracyTestHelper {
    /**
     * <p>
     * Configuration namespace used in this test.
     * </p>
     */
    static final String CONFIG_NAMESPACE = "accuracy_test_namespace";

    /** The jdbc property type. */
    final static String JDBC = "jdbc";

    /** The jndi property type. */
    final static String JNDI = "jndi";

    /** The reflecting property type. */
    final static String REFLECTING = "reflecting";

    /** The the configuration file to load the necessary configuration values to create a connection. */
    private static final String DB_CONFIG_FILE = "test_files/accuracy/db.properties";

    /** The the configuration file to load the necessary configuration values to create a connection. */
    static final String CONFIG_FILE = "test_files/accuracy/dbconnectionfactory.xml";

    /** The connection instance for testing. */
    private static Connection conn = null;

    /** The dbdriver for testing. */
    private static String dbdriver = null;

    /** The dburl for testing. */
    private static String dburl = null;

    /** The dbuser for testing. */
    private static String dbuser = null;

    /** The dbpwd for testing. */
    private static String dbpwd = null;

    /** The dbjndi for testing. */
    private static String dbjndi = null;

    /** The jdbcProperty for testing. */
    private static Property jdbcProperty = null;

    /** The jndiProperty for testing. */
    private static Property jndiProperty = null;

    /** The reflectingProperty for testing. */
    private static Property reflectingProperty = null;

    static {
        try {
            // load the properties from configuration file
            Properties prop = new Properties();
            prop.load(new BufferedInputStream(new FileInputStream(DB_CONFIG_FILE)));

            dbdriver = prop.getProperty("dbdriver");
            dburl = prop.getProperty("dburl");
            dbuser = prop.getProperty("dbuser");
            dbpwd = prop.getProperty("dbpassword");
            dbjndi = prop.getProperty("dbjndi");


            // load the driver class
            Class.forName(dbdriver);

            conn = DriverManager.getConnection(dburl, dbuser, dbpwd);

            ConfigManager cm = ConfigManager.getInstance();

            if (cm.existsNamespace(CONFIG_NAMESPACE)) {
                cm.removeNamespace(CONFIG_NAMESPACE);
            }
            cm.add(CONFIG_NAMESPACE, new File(CONFIG_FILE).getAbsolutePath(), ConfigManager.CONFIG_XML_FORMAT);

            Property property = cm.getPropertyObject(CONFIG_NAMESPACE, "connections");
            List connections = property.list();

            // Enumerate each sub-property
            Iterator connectionIter = connections.iterator();

            while (connectionIter.hasNext()) {
                Property connectionProperty = (Property) connectionIter.next();
                String name = connectionProperty.getName();

                if (name.equals(JDBC)) {
                    jdbcProperty = connectionProperty.getProperty("parameters");
                } else if (name.equals(JNDI)) {
                    jndiProperty = connectionProperty.getProperty("parameters");
                } else if (name.equals(REFLECTING)) {
                    reflectingProperty = connectionProperty.getProperty("parameters");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * Prevent a new <code>AccuracyTestHelper</code> instance to be created outside.
     * </p>
     */
    private AccuracyTestHelper() {
    }

    /**
     * Return a mock DataSource instance.
     *
     * @return the mock dataSource instance
     */
    static DataSource getDataSource() {
        return new MockDataSource();
    }

    static Connection getConnection() {
        return conn;
    }

    /**
     * Return the test db driver info.
     *
     * @return the test db driver
     */
    static String getDBDriver() {
        return dbdriver;
    }

    /**
     * Return the test db url info.
     *
     * @return the test db url
     */
    static String getDBUrl() {
        return dburl;
    }

    /**
     * Return the test db user info.
     *
     * @return the test db user
     */
    static String getDBUserName() {
        return dbuser;
    }

    /**
     * Return the test db password info.
     *
     * @return the test db password
     */
    static String getDBPassword() {
        return dbpwd;
    }

    /**
     * Return the test db jndi info.
     *
     * @return the test db jndi
     */
    static String getDBJNDI() {
        return dbjndi;
    }

    /**
     * @param type the type of property, can be JDBC, JNDI, Reflection.
     *
     * @return the property with the given type
     */
    static Property getProperty(String type) {
        if (type.equals(JDBC)) {
            return jdbcProperty;
        } else if (type.equals(JNDI)) {
            return jndiProperty;
        } else if (type.equals(REFLECTING)) {
            return reflectingProperty;
        }

        System.out.println("return type: " + type);
        return null;
    }

    /**
     * Return the test reflecting class name
     * 
     * @return the test reflecting class name
     */
    static String getReflectingClass() {
        return "com.topcoder.db.connectionfactory.accuracytests.MockDataSource";
    }

    /**
     * Return the test mock initial context factory class name
     * 
     * @return the test mock initial context factory class name
     */
    static String getContextFactory() {
        return "com.topcoder.db.connectionfactory.accuracytests.MockInitialContextFactory";
    }
}
