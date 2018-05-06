/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

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
     * Represents the configuration file.
     * </p>
     */
    public static final String CONFIG_FILE = TEST_FILES + "config.properties";

    /**
     * <p>
     * Represents the file that does not exist.
     * </p>
     */
    public static final String NOT_EXIST_FILE = TEST_FILES + "not_exist";

    /**
     * <p>
     * Represents the timestamp format.
     * </p>
     */
    public static final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * <p>
     * Represents the log file.
     * </p>
     */
    public static final String LOG_FILE = TEST_FILES + "log.txt";

    /**
     * <p>
     * Represents the connection string.
     * </p>
     */
    private static final String CONN_STRING;

    /**
     * <p>
     * Represents the driver class name.
     * </p>
     */
    private static final String DRIVER_CLASS;

    /**
     * <p>
     * Represents the username.
     * </p>
     */
    private static final String USERNAME;

    /**
     * <p>
     * Represents the password.
     * </p>
     */
    private static final String PASSWORD;

    /**
     * <p>
     * Initialization.
     * </p>
     */
    static {
        Properties config = null;
        try {
            config = loadProperties(new File(CONFIG_FILE));
        } catch (IOException e) {
            // Ignore
        }

        CONN_STRING = config.getProperty("connectionString");
        DRIVER_CLASS = config.getProperty("driverClassName");
        USERNAME = config.getProperty("username");
        PASSWORD = config.getProperty("password");
    }

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
     * Gets a connection.
     * </p>
     *
     * @return the connection.
     *
     * @throws ClassNotFoundException
     *             the driver class cannot be loaded.
     * @throws SQLException
     *             if a database access error occurs.
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER_CLASS);

        return DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
    }

    /**
     * <p>
     * Closes the given connection.
     * </p>
     *
     * @param connection
     *            the given connection.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Ignore
            }
        }
    }

    /**
     * <p>
     * Loads data into the database.
     * </p>
     *
     * @param connection
     *            the connection.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void loadDB(Connection connection) throws Exception {
        executeSQL(connection, TEST_FILES + "DBData.sql");
    }

    /**
     * <p>
     * Clears the database.
     * </p>
     *
     * @param connection
     *            the connection.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void clearDB(Connection connection) throws Exception {
        executeSQL(connection, TEST_FILES + "DBClear.sql");
    }

    /**
     * <p>
     * Loads the properties.
     * </p>
     *
     * @param configFile
     *            the properties file.
     *
     * @return the properties.
     *
     * @throws IOException
     *             if any error occurs.
     */
    public static Properties loadProperties(File configFile) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(configFile);
            properties.load(inputStream);
            return properties;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    /**
     * <p>
     * Checks the logging content.
     * </p>
     *
     * @param method
     *            the method.
     * @param fields
     *            the fields.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void checkLog(String method, String... fields) throws Exception {
        String content = readFile(LOG_FILE);

        for (String field : fields) {
            assertTrue("'" + method + "' should be correct, the following content is missing: " + field,
                content.contains(field));
        }
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
    public static String readFile(String fileName) throws IOException {
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

            // Return  content
            return sb.toString().trim();
        } finally {
            reader.close();
        }
    }

    /**
     * <p>
     * Executes the SQL statements in the file. Empty lines will be ignore.
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
    private static void executeSQL(Connection connection, String file) throws Exception {
        String[] values = readFile(file).split(";");

        Statement statement = connection.createStatement();
        try {

            for (int i = 0; i < values.length; i++) {
                String sql = values[i].trim();
                if (sql.length() != 0) {
                    statement.executeUpdate(sql);
                }
            }
        } finally {
            statement.close();
        }
    }
}
