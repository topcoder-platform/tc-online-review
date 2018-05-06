/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils.failuretests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Types;
import java.util.Properties;

import junit.framework.TestCase;

import com.topcoder.commons.utils.JDBCUtility;

/**
 * <p>
 * Failure tests for JDBCUtility.
 * </p>
 * 
 * @author Beijing2008
 * @version 1.0
 */
public class JDBCUtilityFailureTest extends TestCase {

    private Connection connection;
    /**
     * <p>
     * Setup the environment.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        Properties config = loadConfig();
        Class.forName(config.getProperty("driverClassName"));
        connection =  DriverManager.getConnection(config.getProperty("url"), 
                config.getProperty("username"), config.getProperty("password"));
    }

    /**
     * <p>
     * Clean up the environment.
     * </p>
     * 
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        connection.close();
    }


    private Properties loadConfig() throws FileNotFoundException, IOException {
        Properties config = new Properties();
        FileInputStream fio = new FileInputStream("test_files/failure/config.properties");
        try {
            config.load(fio);
        } finally {
            fio.close();
        }
        return config;
    }
    /**
     * Tests for executeQuery() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_executeQuery1() throws Exception {
        try {
            int[] argumentTypes = new int[]{};
            Object[] argumentValues = new Object[]{};
            Class<?>[] columnTypes = new Class[]{Integer.class, String.class};
            JDBCUtility.executeQuery(connection, "wrong sql", argumentTypes, argumentValues, columnTypes, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
    }
    /**
     * Tests for executeQuery() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_executeQuery2() throws Exception {
        try {
            int[] argumentTypes = new int[]{Types.INTEGER};
            Object[] argumentValues = new Object[]{1233};
            Class<?>[] columnTypes = new Class[]{Integer.class, String.class};
            JDBCUtility.executeQuery(connection, "select * from failurereviewer", argumentTypes, argumentValues, columnTypes, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
    }
    /**
     * Tests for executeQuery() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_executeQuery3() throws Exception {
        try {
            int[] argumentTypes = new int[]{};
            Object[] argumentValues = new Object[]{};
            Class<?>[] columnTypes = new Class[]{Properties.class, Integer.class};
            JDBCUtility.executeQuery(connection, "select * from failurereviewer", argumentTypes, argumentValues, columnTypes, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
    }
    /**
     * Tests for executeQuery() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_executeQuery4() throws Exception {
        try {
            int[] argumentTypes = new int[]{};
            Object[] argumentValues = new Object[]{};
            Class<?>[] columnTypes = new Class[]{};
            JDBCUtility.executeQuery(connection, "select * from failurereviewer", argumentTypes, argumentValues, columnTypes, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
    }
    
    

    /**
     * Tests for executeUpdate() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_executeUpdate1() throws Exception {
        try {
            int[] argumentTypes = new int[]{};
            Object[] argumentValues = new Object[]{};
            JDBCUtility.executeUpdate(connection, "wrong sql", argumentTypes, argumentValues, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
    }
    /**
     * Tests for executeUpdate() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_executeUpdate2() throws Exception {
        try {
            int[] argumentTypes = new int[]{Types.INTEGER};
            Object[] argumentValues = new Object[]{1233};
            JDBCUtility.executeUpdate(connection, "delete * from failurereviewer", argumentTypes, argumentValues, FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
    }
}
