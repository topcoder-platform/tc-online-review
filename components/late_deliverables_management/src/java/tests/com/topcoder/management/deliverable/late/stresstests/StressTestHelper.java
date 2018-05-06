/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late.stresstests;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationPersistence;
import com.topcoder.configuration.persistence.XMLFilePersistence;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * This class provides methods for testing this component.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class StressTestHelper {
    /**
     * The singleton ConfigManager instance.
     */
    private static ConfigManager CM = ConfigManager.getInstance();

    /**
     * Represents the DB connection.
     */
    private static Connection connection;

    /**
     * Represents the Configuration Object.
     */
    private static ConfigurationObject config;

    /**
     * Prevent from being instantiated.
     */
    private StressTestHelper() {
        // empty
    }

    /**
     * Loads config file.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void addConfig() throws Exception {
        removeConfig();
        CM.add("stress/SearchBundleManager.xml");
    }

    /**
     * Removes all namespaces.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void removeConfig() throws Exception {
        for (Iterator<?> it = CM.getAllNamespaces(); it.hasNext();) {
            String ns = it.next().toString();
            CM.removeNamespace(ns);
        }
    }

    /**
     * Gets the configuration object used for tests.
     *
     * @return the configuration object.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static ConfigurationObject getLateDeliverableManagerImplConfig() throws Exception {
        if (config == null) {
            ConfigurationPersistence persistence = new XMLFilePersistence();
            ConfigurationObject obj = persistence.loadFile("com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl", new File(
                "test_files/stress/LateDeliverableManagerImpl.xml"));
            config = obj.getChild("com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl");
        }

        return config;
    }

    /**
     * Gets the DB connection.
     *
     * @return the DB connection.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static Connection getDBConnection() throws Exception {
        if (connection == null) {
            ConfigurationObject config = getLateDeliverableManagerImplConfig().getChild("persistenceConfig");
            DBConnectionFactoryImpl dbFactory = new DBConnectionFactoryImpl(config
                .getChild("dbConnectionFactoryConfig"));
            connection = dbFactory.createConnection();
        }

        return connection;
    }

    /**
     * Closes the given connection.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void closeConnection() throws Exception {
        if ((connection != null) && (!connection.isClosed())) {
            connection.close();
            connection = null;
        }
    }

    /**
     * Add test data into the database.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void addTestData() throws Exception {
        clearTestData();
        execute("test_files/stress/add.sql");
    }

    /**
     * Clears the database.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void clearTestData() throws Exception {
        execute("test_files/stress/clear.sql");
    }

    /**
     * Executes the SQL statements in the file.
     *
     * @param fileName
     *            the name of the file.
     *
     * @throws Exception
     *             to JUnit.
     */
    private static void execute(String fileName) throws Exception {
        Statement stmt = null;
        try {
            stmt = getDBConnection().createStatement();
            String[] split = readFile(fileName).split(";");
			for (String sql : split) {
                sql = sql.trim();
                if (sql.length() != 0) {
                    stmt.executeUpdate(sql);
                }
            }
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }

    /**
     * Counts the records in database.
     *
     * @param sql
     *            the sql statement.
     * @return the record count.
     * @throws Exception
     *             to jUnit.
     */
    public static int count(String sql) throws Exception {
        Statement stmt = null;
        Connection conn = null;
        try {
            conn = getDBConnection();
            conn.setAutoCommit(false);
            stmt = getDBConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } finally {
            conn.commit();
            stmt.close();
        }
    }

    /**
     * Reads the file content.
     *
     * @param fileName
     *            the name of the file.
     *
     * @return the file content.
     *
     * @throws IOException
     *             to jUnit.
     */
    private static String readFile(String fileName) throws IOException {
        Reader reader = null;

        try {
            reader = new FileReader(fileName);

            StringBuilder sb = new StringBuilder();
            char[] buf = new char[1024];
            int i = 0;
            while ((i = reader.read(buf)) != -1) {
                sb.append(buf, 0, i);
            }
            return sb.toString();
        } finally {
            try {
                reader.close();
            } catch (IOException ioe) {
                // ignore
            }
        }
    }
}