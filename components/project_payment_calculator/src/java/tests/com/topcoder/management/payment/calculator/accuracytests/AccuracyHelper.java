/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator.accuracytests;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.payment.calculator.impl.DefaultProjectPaymentCalculator;


/**
 * <p>
 * This test case aggregates all accuracy unit test cases.
 * </p>
 *
 * @author lqz
 * @version 1.0
 */
public class AccuracyHelper extends TestCase {
    /**
     * <p>
     * All accuracy unit test cases.
     * </p>
     *
     * @return The test suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(DefaultProjectPaymentCalculatorAccuracyTests.suite());
        suite.addTest(ProjectPaymentAdjustmentCalculatorAccuracyTests.suite());

        return suite;
    }

    /**
     * Clear the database.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected static void clearDB() throws Exception {
        executeSQL("test_files/accuracy/clear.sql");
    }

    /**
     * Clear the database.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected static void prepareDB() throws Exception {
        executeSQL("test_files/accuracy/data.sql");
    }

    /**
     * Create connection to the database.
     *
     * @return connection
     * @throws Exception
     *             to JUnit.
     */
    protected static Connection getConnection() throws Exception {
        ConfigurationFileManager configFileManager =
            new ConfigurationFileManager("accuracy/DefaultProjectPaymentCalculator.properties");
        ConfigurationObject configObject =
            configFileManager.getConfiguration(DefaultProjectPaymentCalculator.class.getName());
        ConfigurationObject config = configObject.getChild(DefaultProjectPaymentCalculator.class.getName());
        ConfigurationObject dbConnFactoryConfig = config.getChild("db_connection_factory_config");
        DBConnectionFactoryImpl dbConnectionFactory = new DBConnectionFactoryImpl(dbConnFactoryConfig);
        return dbConnectionFactory.createConnection();
    }

    /**
     * Executes the SQL statements from file.
     *
     * @param file
     *            the file.
     * @throws Exception
     *             to JUnit.
     */
    private static void executeSQL(String file) throws Exception {
        String[] values = readFile(file).split(";");

        Connection conn = null;

        try {
            conn = getConnection();
            Statement statement = conn.createStatement();
            for (int i = 0; i < values.length; i++) {
                String sql = values[i].trim();
                if ((sql.length() != 0) && (!sql.startsWith("#"))) {
                    statement.executeUpdate(sql);
                }
            }
            statement.close();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    // ignore
                }
            }
            e.printStackTrace();
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Reads file to a string.
     *
     * @param fileName
     *            the name of the file to read.
     * @return a string represents the content.
     * @throws IOException
     *             if any IO error occurs.
     */
    private static String readFile(String fileName) throws IOException {
        Reader reader = new FileReader(fileName);

        try {
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[1024];
            int k = 0;
            while ((k = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, k);
            }
            return sb.toString().replace("\r\n", "\n");
        } finally {
            try {
                reader.close();
            } catch (IOException ioe) {
                // Ignore
            }
        }
    }
}
