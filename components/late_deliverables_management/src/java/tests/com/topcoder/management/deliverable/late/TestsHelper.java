/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * This class provides methods for testing this component.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
public class TestsHelper {
    /**
     * <p>
     * Represents the empty string.
     * </p>
     */
    public static final String EMPTY_STRING = " \t ";

    /**
     * <p>
     * Represents the path of test files.
     * </p>
     */
    public static final String TEST_FILES = "test_files" + File.separator;

    /**
     * <p>
     * Represents the configuration file used in tests.
     * </p>
     */
    public static final String CONFIG_FILE = TEST_FILES + "LateDeliverableManagerImpl.xml";

    /**
     * <p>
     * Private constructor to prevent this class being instantiated.
     * </p>
     */
    private TestsHelper() {
        // empty
    }

    /**
     * <p>
     * Loads necessary configurations into ConfigManager.
     * </p>
     *
     * @param file
     *            the file contains configurations.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void loadConfig(String file) throws Exception {
        ConfigManager.getInstance().add(file);
    }

    /**
     * <p>
     * Unloads all configurations.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void unloadConfig() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator<?> it = cm.getAllNamespaces(); it.hasNext();) {
            String ns = it.next().toString();

            if (!"com.topcoder.util.log".equals(ns)) {
                cm.removeNamespace(ns);
            }
        }
    }

    /**
     * <p>
     * Gets a connection.
     * </p>
     *
     * @return the connection.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static Connection getConnection() throws Exception {
        ConfigurationObject config = TestsHelper.getConfig().getChild("persistenceConfig");

        // Get configuration of DB Connection Factory
        ConfigurationObject dbConnectionFactoryConfig = Helper.getChildConfig(config, "dbConnectionFactoryConfig");

        // Create database connection factory using the extracted configuration
        DBConnectionFactoryImpl dbConnectionFactory = new DBConnectionFactoryImpl(dbConnectionFactoryConfig);
        return dbConnectionFactory.createConnection();
    }

    /**
     * <p>
     * Closes the given connection.
     * </p>
     *
     * @param connection
     *            the given connection.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void closeConnection(Connection connection) throws Exception {
        if ((connection != null) && (!connection.isClosed())) {
            connection.close();
        }
    }

    /**
     * <p>
     * Clears the database.
     * </p>
     *
     * @param connection
     *            the given connection.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void clearDB(Connection connection) throws Exception {
        executeSQL(connection, TEST_FILES + "DBClear.sql");
    }

    /**
     * <p>
     * Loads data into the database.
     * </p>
     *
     * @param connection
     *            the given connection.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void loadDB(Connection connection) throws Exception {
        executeSQL(connection, TEST_FILES + "DBData.sql");
    }

    /**
     * <p>
     * Executes the SQL statements in the file. Lines that are empty or starts with '#' will be ignore.
     * </p>
     *
     * @param connection
     *            the connection.
     * @param file
     *            the file.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void executeSQL(Connection connection, String file) throws Exception {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();

            String[] values = readFile(file).split(";");

            for (int i = 0; i < values.length; i++) {
                String sql = values[i].trim();
                if ((sql.length() != 0) && (!sql.startsWith("#"))) {
                    stmt.executeUpdate(sql);
                }
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    /**
     * <p>
     * Gets the configuration object used for tests.
     * </p>
     *
     * @return the configuration object.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static ConfigurationObject getConfig() throws Exception {
        XMLFilePersistence persistence = new XMLFilePersistence();

        // Get configuration
        ConfigurationObject obj = persistence.loadFile(
            "com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl", new File(CONFIG_FILE));

        return obj.getChild("com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl");
    }

    /**
     * <p>
     * Gets value for field of given object.
     * </p>
     *
     * @param obj
     *            the given object.
     * @param field
     *            the field name.
     *
     * @return the field value.
     */
    public static Object getField(Object obj, String field) {
        Object value = null;
        try {
            Field declaredField = null;
            try {
                declaredField = obj.getClass().getDeclaredField(field);
            } catch (NoSuchFieldException e) {
                // Ignore
            }
            if (declaredField == null) {
                // From the superclass
                declaredField = obj.getClass().getSuperclass().getDeclaredField(field);
            }

            declaredField.setAccessible(true);

            value = declaredField.get(obj);

            declaredField.setAccessible(false);
        } catch (IllegalArgumentException e) {
            // Ignore
        } catch (SecurityException e) {
            // Ignore
        } catch (IllegalAccessException e) {
            // Ignore
        } catch (NoSuchFieldException e) {
            // Ignore
        }

        return value;
    }

    /**
     * <p>
     * Reads the content of a given file.
     * </p>
     *
     * @param fileName
     *            the name of the file to read.
     *
     * @return a string represents the content.
     *
     * @throws IOException
     *             if any error occurs during reading.
     */
    private static String readFile(String fileName) throws IOException {
        Reader reader = new FileReader(fileName);

        try {
            // Create a StringBuilder instance
            StringBuilder sb = new StringBuilder();

            // Buffer for reading
            char[] buffer = new char[1024];

            // Number of read chars
            int k = 0;

            // Read characters and append to string builder
            while ((k = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, k);
            }

            // Return read content
            return sb.toString();
        } finally {
            try {
                reader.close();
            } catch (IOException ioe) {
                // Ignore
            }
        }
    }
}
