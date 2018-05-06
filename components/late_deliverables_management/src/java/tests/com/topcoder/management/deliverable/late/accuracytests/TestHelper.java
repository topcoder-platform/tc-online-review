/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.deliverable.late.accuracytests;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.deliverable.late.LateDeliverableManagementConfigurationException;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * This class contains the helper methods from submitters which can be used for test and other common methods.
 * </p>
 *
 * @author yuanyeyuanye, TCSDEVELOPER
 * @version 1.0
 */
final class TestHelper {

    /**
     * <p>
     * Represents the path of test files.
     * </p>
     */
    public static final String ACCURACY_TEST_FILES = "test_files" + File.separator + "accuracy" + File.separator;

    /**
     * <p>
     * Represents the configuration file used in tests.
     * </p>
     */
    public static final String CONFIG_FILE = ACCURACY_TEST_FILES + "LateDeliverableManagerImpl.xml";

    /**
     * <p>
     * Private constructor to prevent this class being instantiated.
     * </p>
     */
    private TestHelper() {
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
    public static void initializeConfigurationManager(String file) throws Exception {
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
    public static void resetConfigurationManager() throws Exception {
        final String logNameSpace = "com.topcoder.util.log";
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator<?> it = cm.getAllNamespaces(); it.hasNext();) {
            String ns = it.next().toString();

            if (!ns.equals(logNameSpace)) {
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
    public static Connection createConnection() throws Exception {
        ConfigurationObject config = TestHelper.getManagerConfig().getChild("persistenceConfig");

        // Get configuration of DB Connection Factory
        ConfigurationObject dbConnectionFactoryConfig = getChildConfig(config, "dbConnectionFactoryConfig");

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
    public static void clearTable(Connection connection) throws Exception {
        executeSQL(connection, ACCURACY_TEST_FILES + "clear.sql");
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
    public static void initializeData(Connection connection) throws Exception {
        executeSQL(connection, ACCURACY_TEST_FILES + "create.sql");
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
        String sql = null;
        try {
            stmt = connection.createStatement();

            String[] values = readFile(file).split(";");

            for (String value : values) {
                sql = value.trim();
                if ((sql.length() != 0) && (!sql.startsWith("#"))) {
                    stmt.executeUpdate(sql);
                }
            }
        } catch (Exception e) {
            throw new Exception("Error occurs when executing " + sql,e);
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
    public static ConfigurationObject getManagerConfig() throws Exception {
        XMLFilePersistence persistence = new XMLFilePersistence();

        // Get configuration
        ConfigurationObject obj = persistence.loadFile(
            "com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl", new File(CONFIG_FILE));

        return obj.getChild("com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl");
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
     * @throws java.io.IOException
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

    /**
     * <p>
     * Gets a child ConfigurationObject from given configuration object.
     * </p>
     *
     * @param config
     *            the given configuration object.
     * @param key
     *            the child name.
     *
     * @return the retrieved child ConfigurationObject.
     *
     * @throws com.topcoder.management.deliverable.late.LateDeliverableManagementConfigurationException
     *             if the child ConfigurationObject is missing or some error occurred.
     */
    private static ConfigurationObject getChildConfig(ConfigurationObject config, String key) {
        try {
            ConfigurationObject child = config.getChild(key);

            if (child == null) {
                throw new LateDeliverableManagementConfigurationException("The child '" + key + "' of '"
                    + config.getName() + "' is required.");
            }

            return child;
        } catch (ConfigurationAccessException e) {
            throw new LateDeliverableManagementConfigurationException(
                "An error occurred while accessing the configuration.", e);
        }
    }

    /**
     * <p> Gets the value of a private field in the given class. </p> <p> The value is retrieved from the given
     * instance. </p> <p> If the instance is null, the field is a static field. </p> <p> If any error occurs, null is
     * returned. </p>
     *
     * @param instance the instance which the private field belongs to.
     * @param name     the name of the private field to be retrieved.
     *
     * @return the value of the private field
     */
    public static Object getPrivateField(Object instance, String name) {
        Field field = null;
        Object obj = null;
        try {
            // get the reflection of the field
            field = getDeclaredField(instance.getClass(), name);

            // set the field accessible.
            field.setAccessible(true);

            // get the value
            obj = field.get(instance);
        } catch (NoSuchFieldException e) {
            // ignore
        } catch (IllegalAccessException e) {
            // ignore
        } finally {
            if (field != null) {
                // reset the accessibility
                field.setAccessible(false);
            }
        }
        return obj;
    }

    /**
     * <p> Sets the value of a private field in the given class. </p>
     *
     * @param instance the instance which the private field belongs to
     * @param name     the name of the private field to be set
     * @param value    the value to set
     */
    public static void setPrivateField(Object instance, String name, Object value) {
        Field field = null;
        try {
            // get the reflection of the field
            field = getDeclaredField(instance.getClass(), name);

            // set the field accessible
            field.setAccessible(true);

            // set the value
            field.set(instance, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (field != null) {
                // reset the accessibility
                field.setAccessible(false);
            }
        }
    }

    /**
     * Gets the field in the given classs.
     *
     * @param clazz     Class used to retrieve the field information.
     * @param fieldName Name of the field.
     *
     * @return field information.
     *
     * @throws NoSuchFieldException if there is no such field.
     */
    private static Field getDeclaredField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> c = clazz;

        while (c != null) {
            try {
                return c.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                c = c.getSuperclass();
            }
        }

        throw new NoSuchFieldException(String.format("%s do not exists in %s", fieldName, clazz));
    }

    /**
     * Gets the specific date.
     *
     * @param year year.
     * @param month month.
     * @param day day.
     * @param hour hour.
     * @param minute minute.
     * @param second second.
     * @return The date.
     */
    static Date getDate(int year, int month, int day, int hour, int minute, int second) {
        final Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, day, hour, minute, second);
        return calendar.getTime();
    }
}
