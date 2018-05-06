/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;

/**
 * <p>
 * Unit tests for {@link JDBCUtility} class.
 * </p>
 *
 * @author dingying131
 * @version 1.0
 */
public class JDBCUtilityAccuracyTests {

    /**
     * <p>
     * The tested databse connection.
     * </p>
     */
    private Connection connection;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(JDBCUtilityAccuracyTests.class);
    }

    /**
     * <p>
     * Before method for this test suite that loads the test configuration and prepare the test data.
     * </p>
     *
     * @throws Exception
     *             to jUnit
     */
    @Before
    public void before() throws Exception {
        // load the database configuration
        Properties prop = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File("test_files/accuracy.properties"));
            prop.load(fis);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        // create the database
        Class.forName(prop.getProperty("db.class"));
        connection = DriverManager.getConnection(prop.getProperty("db.url"), prop.getProperty("db.user"), prop
                .getProperty("db.password"));

        // prepare the test data
        execute("CREATE TABLE TEST(ID INT PRIMARY KEY, NAME VARCHAR(8), L BIGINT, F FLOAT, D DOUBLE, T TIMESTAMP)");
        execute("INSERT INTO TEST VALUES(1, 'Hello', 500000000000, 1.0, 2.0, '2000-01-01 12:00:00')");
        execute("INSERT INTO TEST VALUES(2, 'World', 600000000000, 1.0, 2.0, '2000-01-01 12:00:00')");
        execute("INSERT INTO TEST VALUES(3, null, null, null, null, null)");
    }

    /**
     * <p>
     * After method for this test suite that deletes the test data and drop the database.
     * </p>
     *
     * @throws Exception
     *             to jUnit
     */
    @After
    public void after() throws Exception {
        execute("DROP TABLE TEST");
        connection.close();
    }

    /**
     * <p>
     * Accuracy test for @ JDBCUtility#executeQuery(Connection, String, int[], Object[], Class[], Class)}.
     * </p>
     *
     * <p>
     * Test test data has been loaded and this method should return correct result.
     * </p>
     * @throws to jUnit
     */
    @Test
    public void testExecuteQuery1() throws Exception {

        // Query the first 2 rows
        Object[][] result = JDBCUtility.executeQuery(connection,
                "select ID , NAME, L, F, D, T from TEST where F = ? and D = ?",
                new int[] { Types.FLOAT, Types.DOUBLE }, new Object[] { 1.0, 2.0 }, new Class[] { Integer.class,
                        String.class, Long.class, Float.class, Double.class, Date.class }, Exception.class);

        // Create date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = sdf.parse("2000-01-01 12:00:00");

        // Check the return data of first 2 rows
        Assert.assertEquals("Query result should be correct.", 1, result[0][0]);
        Assert.assertEquals("Query result should be correct.", "Hello", result[0][1]);
        Assert.assertEquals("Query result should be correct.", 500000000000L, result[0][2]);
        Assert.assertEquals("Query result should be correct.", 1.0F, result[0][3]);
        Assert.assertEquals("Query result should be correct.", 2.0D, result[0][4]);
        Assert.assertEquals("Query result should be correct.", date, result[0][5]);

        Assert.assertEquals("Query result should be correct.", 2, result[1][0]);
        Assert.assertEquals("Query result should be correct.", "World", result[1][1]);
        Assert.assertEquals("Query result should be correct.", 600000000000L, result[1][2]);
        Assert.assertEquals("Query result should be correct.", 1.0F, result[1][3]);
        Assert.assertEquals("Query result should be correct.", 2.0D, result[1][4]);
        Assert.assertEquals("Query result should be correct.", date, result[1][5]);

        // Get the last row which contains null values
        result = JDBCUtility.executeQuery(connection, "select ID , NAME, L, F, D, T from TEST where ID = 3",
                new int[] {}, new Object[] {}, new Class[] { Integer.class, String.class, Long.class, Float.class,
                        Double.class, Date.class }, Exception.class);

        // Check the last row value
        Assert.assertEquals("Query result should be correct.", 3, result[0][0]);
        Assert.assertEquals("Query result should be correct.", null, result[0][1]);
        Assert.assertEquals("Query result should be correct.", null, result[0][2]);
        Assert.assertEquals("Query result should be correct.", null, result[0][3]);
        Assert.assertEquals("Query result should be correct.", null, result[0][4]);
        Assert.assertEquals("Query result should be correct.", null, result[0][5]);
    }

    /**
     * <p>
     * Accuracy test for @ JDBCUtility#executeQuery(Connection, String, int[], Object[], Class[], Class)}.
     * </p>
     *
     * <p>
     * The update execution should be executed properly.
     * </p>
     * @throws to jUnit
     */
    @Test
    public void testExecuteUpdate() throws Exception {
        // update two rows data in data base
        int rows = JDBCUtility.executeUpdate(connection, "update TEST set NAME = 'TEST' where F = ? and D = ?",
                new int[] { Types.FLOAT, Types.DOUBLE }, new Object[] { 1.0, 2.0 }, Exception.class);

        // Check the returned result
        Assert.assertEquals("2 rows should be updated.", 2, rows);

        // get the data from data base
        Object[][] result = JDBCUtility.executeQuery(connection, "select count(1) from TEST where NAME = ?",
                new int[] { Types.VARCHAR }, new Object[] { "TEST" }, new Class[] { Integer.class }, Exception.class);

        // Check the updated rows
        Assert.assertEquals("2 rows should be updated.", 2, result[0][0]);
    }

    /**
     * <p>
     * Accuracy test for @ JDBCUtility#executeQuery(Connection, String, int[], Object[], Class[], Class)}.
     * </p>
     *
     * <p>
     * The transaction should be committed manually.
     * </p>
     *
     * @throws Exception to jUnit
     */
    @Test
    public void testExecuteCommit() throws Exception {
        connection.setAutoCommit(false);
        JDBCUtility.executeUpdate(connection, "delete from TEST", new int[] {}, new Object[] {}, Exception.class);
        connection.commit();
        Object[][] result = JDBCUtility.executeQuery(connection, "select count(1) from TEST", new int[] {},
                new Object[] {}, new Class[] { Integer.class }, IllegalArgumentException.class);
        Assert.assertEquals("Transaction should be commited and data should be cleared.", Integer.valueOf(0),
                (Integer) result[0][0]);
    }

    /**
     * <p>
     * Accuracy test for @ JDBCUtility#executeQuery(Connection, String, int[], Object[], Class[], Class)}.
     * </p>
     *
     * <p>
     * The transaction should be rollbacked properly.
     * </p>
     * @throws to jUnit
     */
    @Test
    public void testExecuteRollback() throws Exception {
        connection.setAutoCommit(false);
        JDBCUtility.executeUpdate(connection, "delete from TEST", new int[] {}, new Object[] {}, Exception.class);
        connection.rollback();
        connection.setAutoCommit(true);
        Object[][] result = JDBCUtility.executeQuery(connection, "select count(*) from TEST", new int[] {},
                new Object[] {}, new Class[] { Integer.class }, IllegalArgumentException.class);
        Assert.assertEquals("Transaction should be commited and data should be cleared.", Integer.valueOf(3),
                (Integer) result[0][0]);
    }

    /**
     * <p>
     * Helper method that executes the given SQL string.
     * </p>
     *
     * @param sql
     *            SQL string to be executed
     */
    private void execute(String sql) {
        Statement s = null;
        try {
            s = connection.createStatement();
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (SQLException e) {
                    // ignore
                }
            }
        }

    }
}
