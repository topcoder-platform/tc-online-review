/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction.accuracytests;

import com.topcoder.util.sql.databaseabstraction.CustomResultSetMetaData;

import junit.framework.Assert;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;


/**
 * <p>
 * Defines helper methods used in tests.
 * </p>
 *
 * @author PE
 * @version 1.1
 */
public final class AccuracyTestHelper {
    /** Represents the CustomResultSetMetaData instance for testing. */
    private static CustomResultSetMetaData crmd = null;

    /**
     * <p>
     * Creates a new instance of <code>AccuracyTestHelper</code> class. The private constructor prevents the creation
     * of a new instance.
     * </p>
     */
    private AccuracyTestHelper() {
    }

    /**
     * <p>
     * Creates the CustomResultMetaData instance from Access database present in version 1.0.
     * </p>
     *
     * @return the CustomResultMetaData instance from Access database present in version 1.0.
     *
     * @throws Exception any exception to JUnit.
     */
    public static CustomResultSetMetaData getCRMD() throws Exception {
        if (crmd == null) {
            Connection connection = null;

            try {
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                connection = DriverManager.getConnection("jdbc:odbc:AccuracyTester");

                ResultSetMetaData rmd = connection.createStatement().executeQuery("select * from tests").getMetaData();
                crmd = new CustomResultSetMetaData(rmd);
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        }

        return crmd;
    }

    /**
     * Gets the connection to access the Access database present in version 1.0.
     *
     * @return the connection to access the Access database present in version 1.0.
     *
     * @throws Exception any exception to JUnit.
     */
    public static Connection getConnection() throws Exception {
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

        return DriverManager.getConnection("jdbc:odbc:AccuracyTester");
    }

    /**
     * Gets the result set from the Access database whose table name is tests.
     *
     * @param conn the connection to access the Access database present in version 1.0.
     *
     * @return the result set from the Access database whose table name is tests.
     *
     * @throws Exception any exception to JUnit.
     */
    public static ResultSet getResultSet(Connection conn)
        throws Exception {
        return conn.createStatement().executeQuery("select * from tests");
    }

    /**
     * <p>
     * Asserts the two given byte arrays to be equals. The two byte arrays will be regarded to be equals only if both
     * the length and the content are equals.
     * </p>
     *
     * @param errorMessage the error message which will be thrown when the two byte arrays are not equals.
     * @param expected the expected byte array.
     * @param actual the actual byte array.
     */
    public static void assertEquals(String errorMessage, byte[] expected, byte[] actual) {
        Assert.assertEquals(errorMessage, expected.length, actual.length);

        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(errorMessage, expected[i], actual[i]);
        }
    }

    /**
     * <p>
     * Gets the byte content from the given input stream.
     * </p>
     *
     * @param inputStream the given input stream to get the content.
     *
     * @return the byte content from the given input stream.
     *
     * @throws Exception any exception when try to get the byte content from the given input stream.
     */
    public static byte[] readContent(InputStream inputStream)
        throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer buffer = new StringBuffer();
        String line = null;

        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        return buffer.toString().getBytes();
    }
}
