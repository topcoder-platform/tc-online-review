/*
 * Copyright (C) 2007-2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.services.uploads;

import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.config.ConfigManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;

/**
 * Test helper class containing constants and some utility methods.
 *
 * @author cyberjag, TCSDEVELOPER
 * @version 1.1
 */
public final class TestHelper {
    /**
     * Represents the test_files folder.
     */
    public static final String TEST_FILES = System.getProperty("user.dir") + File.separator + "test_files"
            + File.separator;

    /**
     * Represents the exception message used in testing.
     */
    public static final String EXCEPTION_MESSAGE = "Message to test";

    /**
     * The project id used for testing.
     */
    public static final long PROJECT_ID = 10;

    /**
     * The project phase id used for testing.
     */
    public static final long PROJECT_PHASE_ID = 1;

    /**
     * The submission id used for testing.
     */
    public static final long SUBMISSION_ID = 1001;

    /**
     * The submission status id used for testing.
     */
    public static final long SUBMISSION_STATUS_ID = 1;

    /**
     * The user id used for testing.
     */
    public static final long USER_ID = 600;

    /**
     * The end point of the Axis UploadService.
     */
    public static final String END_POINT = "http://localhost:8080/axis/services/UploadService";

    /**
     * No instances allowed.
     */
    private TestHelper() {
        // empty
    }

    /**
     * <p>
     * Loads the configuration from the given configuration file.
     * </p>
     *
     * @param file the file to load
     *
     * @throws Exception exception to junit.
     */
    public static void loadConfigs(String file) throws Exception {
        releaseConfigs();
        ConfigManager cm = ConfigManager.getInstance();
        cm.add(TEST_FILES + file);

        // this will be required in all cases
        // - to prevent from throwing exception by logging wrapper 1.2 each time log is instated
        loadConfigs("com.topcoder.util.log", "logging.xml");
    }

    /**
     * <p>
     * Loads the configuration from the given configuration file with specified namespace.
     * </p>
     *
     * @param namespace namespace under which config will be loaded
     * @param file      the file to load
     *
     * @throws Exception exception to junit.
     */
    private static void loadConfigs(String namespace, String file) throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        cm.add(namespace, TEST_FILES + file, ConfigManager.CONFIG_XML_FORMAT);
    }

    /**
     * <p>
     * Releases the configurations.
     * </p>
     *
     * @throws Exception exception to junit.
     */
    public static void releaseConfigs() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        for (Iterator iterator = cm.getAllNamespaces(); iterator.hasNext();) {
            cm.removeNamespace((String) iterator.next());
        }
    }

    /**
     * <p>
     * Gets the field value of a given object.
     * </p>
     *
     * @param object    the object where to get the field value.
     * @param fieldName the name of the field.
     *
     * @return the field value
     *
     * @throws Exception any exception occurs.
     */
    public static Object getFieldValue(Object object, String fieldName) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

    /**
     * <p>Executes a sql batch from specified file.</p>
     *
     * @param file the file that contains the sql statements.
     *
     * @throws Exception if any exception occurs.
     */
    public static void executeBatch(String file) throws Exception {
        Connection connection = null;
        Statement statement = null;
        try {
            // Gets db connection
            connection = new DBConnectionFactoryImpl(DBConnectionFactoryImpl.class.getName()).createConnection();
            statement = connection.createStatement();

            // Gets sql statements and add to statement
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = null;

            while ((line = in.readLine()) != null) {
                if (line.trim().length() != 0) {
                    statement.addBatch(line);
                }
            }

            statement.executeBatch();
        } finally {
            if (statement != null) {
                statement.close();
            }

            if (connection != null) {
                connection.close();
            }
        }
    }
}
