/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils.stresstests;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.commons.utils.JDBCUtility;

/**
 * <p>
 * Stress test case of {@link JDBCUtility}.
 * </p>
 * 
 * @author mumujava
 * @version 1.0
 */
public class JDBCUtilityStressTest extends BaseStressTest {

    private Connection connection;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     * 
     * @throws Exception
     *             to jUnit.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        connection = getConnection();
        init();

    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     * 
     * @throws Exception
     *             to jUnit.
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        clear();
        connection.close();
    }

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     * 
     * @return a TestSuite for this test case.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(
                JDBCUtilityStressTest.class);
        return suite;
    }

    /**
     * <p>
     * Stress test for method JDBCUtility#executeQuery().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_executeQuery() throws Throwable {
        for (int i = 0; i < testCount; i++) {
            int[] argumentTypes = new int[]{};
            Object[] argumentValues = new Object[]{};
            Class<?>[] columnTypes = new Class<?>[]{Integer.class, String.class};
            Class<IllegalArgumentException> exceptionClass = IllegalArgumentException.class;
            Object[][] res = JDBCUtility.executeQuery(connection, "select * from issue_types", argumentTypes, argumentValues, columnTypes, exceptionClass );
            assertEquals(1000, res.length);
            assertEquals(2, res[0].length);
        }
        
        System.out.println("Run executeQuery for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }
    
    /**
     * <p>
     * Stress test for method JDBCUtility#executeUpdate().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_executeUpdate() throws Throwable {
        for (int i = 0; i < 10; i++) {
            clear();
            init();
            int[] argumentTypes = new int[]{};
            Object[] argumentValues = new Object[]{};
            Class<IllegalArgumentException> exceptionClass = IllegalArgumentException.class;
            long ret = JDBCUtility.executeUpdate(connection, "delete from issue_types", argumentTypes, argumentValues, exceptionClass );
            assertEquals(1000, ret);
        }
        
        System.out.println("Run executeUpdate for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }

    /**
     * <p>
     * Stress test for method JDBCUtility#rollbackTransaction().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_rollbackTransaction() throws Throwable {
        connection.setAutoCommit(false);
        for (int i = 0; i < testCount; i++) {
            JDBCUtility.rollbackTransaction(connection, IllegalArgumentException.class);
        }
        
        System.out.println("Run rollbackTransaction for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }

    /**
     * <p>
     * Stress test for method JDBCUtility#commitTransaction().
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_commitTransaction() throws Throwable {
        connection.setAutoCommit(false);
        for (int i = 0; i < testCount; i++) {
            JDBCUtility.commitTransaction(connection, IllegalArgumentException.class);
        }
        
        System.out.println("Run commitTransaction for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }
    
    /**
     * Clear the db.
     *
     * @throws Exception
     *             to Junit
     */
    public static void clear() throws Exception {
        Connection conn = getConnection();
        try {
            executeSqlFile(conn, "test_files/stress/clear.sql");
        } finally {
            conn.close();
        }
    }

    /**
     * Init the db.
     *
     * @throws Exception
     *             to JUnit
     */
    public static void init() throws Exception {
        Connection conn = getConnection();
        try {
            executeSqlFile(conn, "test_files/stress/init.sql");
        } finally {
            conn.close();
        }
    }
    /**
     * <p>
     * Gets a connection.
     * </p>
     *
     * @return the connection.
     * @throws Exception
     *             to JUnit.
     */
    private static Connection getConnection() throws Exception {
        Properties config = new Properties();

        FileInputStream fio = new FileInputStream("test_files/stress/config.properties");
        try {
            config.load(fio);
        } finally {
            fio.close();
        }
        String url = config.getProperty("dbConnectionUrl");
        String driver = config.getProperty("driverClassName");
        String user = config.getProperty("dbUserId");
        String password = config.getProperty("dbPassword");

        Class.forName(driver);

        return DriverManager.getConnection(url, user, password);

    }
    /**
     * Executes the sql file.
     *
     * @param connection
     *            the db connection
     * @param file
     *            the sql file
     * @throws Exception
     *             to JUnit
     */
    private static void executeSqlFile(Connection connection, String file) throws Exception {
        String sql = getSql(file);

        StringTokenizer st = new StringTokenizer(sql, ";");

        PreparedStatement preparedStatement = null;

        try {
            for (int count = 1; st.hasMoreTokens(); count++) {
                String statement = st.nextToken().trim();

                if (statement.length() > 0) {
                    // An empty statement is technically
                    // erroneous, but ignore them silently
                    preparedStatement = connection.prepareStatement(statement);
                    preparedStatement.executeUpdate();
                }
            }
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }
    /**
     * <p>
     * Gets the sql file content.
     * </p>
     *
     * @param file
     *            The sql file to get its content.
     * @return The content of sql file.
     * @throws Exception
     *             to JUnit
     */
    private static String getSql(String file) throws Exception {
        StringBuilder sql = new StringBuilder();

        BufferedReader in = new BufferedReader(new FileReader(file));

        try {
            for (String s = in.readLine(); s != null; s = in.readLine()) {
                int commentIndex = s.indexOf("--");

                if (commentIndex >= 0) { // Remove any SQL comment
                    s = s.substring(0, commentIndex);
                }

                sql.append(s).append(' '); // The BufferedReader drops newlines so insert whitespace
            }
        } finally {
            in.close();
        }

        return sql.toString();
    }
}
