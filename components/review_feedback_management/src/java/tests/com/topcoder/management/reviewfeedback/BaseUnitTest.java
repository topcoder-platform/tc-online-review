/*
 * Copyright (C) 2012, 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;

/**
 * <p>
 * This class provides methods for testing this component.
 * </p>
 *
 * @author amazingpig, sparemax
 * @version 2.0
 */
public class BaseUnitTest extends TestCase {
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
    public static final String CONFIG_FILE = TEST_FILES + "config.xml";

    /**
     * <p>
     * Represents the connection used in tests.
     * </p>
     */
    private Connection connection;

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Override
    public void setUp() throws Exception {
        connection = createConnection();
        clearDB(connection);
        loadDB(connection);
    }

    /**
     * <p>
     * Cleans up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Override
    public void tearDown() throws Exception {
        clearDB(connection);
        closeConnection(connection);
        connection = null;
    }

    /**
     * Executes the SQL.
     *
     * @param connection
     *            the connection
     * @param columnsCount
     *            the count of columns
     * @param sql
     *            the SQL string
     * @param params
     *            the parameters
     *
     * @return the result
     *
     * @throws Exception
     *             if any error occurs.
     */
    public static List<List<Object>> executeQuery(Connection connection, int columnsCount, String sql, Object... params)
        throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        try {
            // Set parameters
            setParameters(preparedStatement, params);

            // Execute
            ResultSet resultSet = preparedStatement.executeQuery();

            List<List<Object>> result = new ArrayList<List<Object>>();
            while (resultSet.next()) {
                List<Object> row = new ArrayList<Object>();
                for (int i = 0; i < columnsCount; i++) {
                    row.add(resultSet.getObject(i + 1));
                }

                result.add(row);
            }

            return result;
        } finally {
            preparedStatement.close();
        }
    }

    /**
     * Checks the result.
     *
     * @param method
     *            the method
     * @param result
     *            the result
     * @param values
     *            the expected values
     */
    public static void checkResult(String method, List<Object> result, Object... values) {
        int index = 0;
        for (Object value : result) {
            if (value instanceof String) {
                value = value.toString().trim();
            }

            assertEquals("'" + method + "' should be correct.", "" + values[index++], "" + value);
        }
    }

    /**
     * <p>
     * Gets a connection.
     * </p>
     *
     * @return the connection.
     *
     * @since 2.0
     */
    protected Connection getConnection() {
        return connection;
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
        executeSQL(connection, TEST_FILES + "/sqls/clear.sql");
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
        executeSQL(connection, TEST_FILES + "/sqls/prepare.sql");
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
        ConfigurationObject obj = persistence.loadFile("com.topcoder.management.reviewfeedback", new File(
                CONFIG_FILE));

        return obj;
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
     * Gets a connection.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Changed to private.</li>
     * </ol>
     * </p>
     *
     * @return the connection.
     *
     * @throws Exception
     *             to JUnit.
     */
    private static Connection createConnection() throws Exception {
        ConfigurationObject config = getConfig();

        // Get configuration of DB Connection Factory
        ConfigurationObject dbConnectionFactoryConfig = config.getChild("dbConnectionFactoryConfiguration");

        // Create database connection factory using the extracted configuration
        DBConnectionFactoryImpl dbConnectionFactory = new DBConnectionFactoryImpl(dbConnectionFactoryConfig);
        return dbConnectionFactory.createConnection();
    }

    /**
     * Sets the parameters.
     *
     * @param preparedStatement
     *            the prepared statement
     * @param params
     *            the parameters
     *
     * @throws SQLException
     *             if any error occurs
     *
     * @since 2.0
     */
    private static void setParameters(PreparedStatement preparedStatement, Object... params) throws SQLException {
        // Set parameters
        for (int i = 0; i < params.length; i++) {
            int index = i + 1;
            Object paramValue = params[i];

            preparedStatement.setObject(index, paramValue);
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
