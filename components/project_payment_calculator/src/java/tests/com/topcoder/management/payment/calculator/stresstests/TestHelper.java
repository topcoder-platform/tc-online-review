/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator.stresstests;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.Statement;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.payment.calculator.impl.DefaultProjectPaymentCalculator;


/**
 * <p>
 * Helper methods used for the stress tests.
 * </p>
 *
 * @author gjw99
 * @version 1.0
 */
public final class TestHelper {
    /**
     * Private constructor.
     */
    private TestHelper() {
    }

    /**
     * Get the jdbc connection.
     *
     * @return the jdbc connection
     * @throws Exception
     *             if any error
     */
    public static Connection getConnection() throws Exception {
        ConfigurationFileManager configFileManager = new ConfigurationFileManager("stress/stress.properties");
        ConfigurationObject configObject =
            configFileManager.getConfiguration(DefaultProjectPaymentCalculator.class.getName());
        ConfigurationObject config = configObject.getChild(DefaultProjectPaymentCalculator.class.getName());
        ConfigurationObject dbConnFactoryConfig = config.getChild("db_connection_factory_config");
        DBConnectionFactoryImpl dbConnectionFactory = new DBConnectionFactoryImpl(dbConnFactoryConfig);
        return dbConnectionFactory.createConnection();
    }

    /**
     * <p>
     * Closes the given connection.
     * </p>
     *
     * @param connection
     *            the given connection.
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
     * Loads data into the database.
     * </p>
     *
     * @param connection
     *            the connection.
     * @throws Exception
     *             to JUnit.
     */
    public static void loadDB(Connection connection) throws Exception {
        executeSQL(connection, "test_files/stress/insert.sql");
    }

    /**
     * <p>
     * Clears the database.
     * </p>
     *
     * @param connection
     *            the connection.
     * @throws Exception
     *             to JUnit.
     */
    public static void clearDB(Connection connection) throws Exception {
        executeSQL(connection, "test_files/stress/delete.sql");
    }

    /**
     * <p>
     * Executes the SQL statements in the file. Empty lines will be ignored.
     * </p>
     *
     * @param connection
     *            the connection.
     * @param file
     *            the file.
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

    /**
     * <p>
     * Reads the content of a given file.
     * </p>
     *
     * @param fileName
     *            the name of the file to read.
     * @return a string represents the content.
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
            return sb.toString().replace("\r\n", "\n");
        } finally {
            reader.close();
        }
    }
}
