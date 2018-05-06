/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import junit.framework.TestCase;

import com.topcoder.management.review.persistence.Helper.DataType;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.log.LogFactory;

/**
 * <p>
 * This class contains test cases for Helper.
 * </p>
 * @author TCSDEVELOPER
 * @version 1.2
 */
public class HelperTest extends TestCase {

    /**
     * <p>
     * Represents connection to database for testing.
     * </p>
     */
    private Connection connection;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     * @throws Exception if any error occurs
     */
    protected void setUp() throws Exception {
        connection = TestHelper.getDBConnectionFactory().createConnection();
        TestHelper.setUpTest();
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     * @throws Exception if any error occurs
     */
    protected void tearDown() throws Exception {
        TestHelper.tearDownTest();
        TestHelper.closeConnection(connection);
        connection = null;
    }

    /**
     * <p>
     * Tests {@link Helper#assertArrayNotNullNorHasNull(Object[], String)} with null array passed.
     * </p>
     * <p>
     * IllegalArgumentException exception is expected.
     * </p>
     */
    public void testAssertArrayNotNullNorHasNull1() {
        try {
            Helper.assertArrayNotNullNorHasNull(null, "array");
            fail("IllegalArgumentException exception is expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests {@link Helper#assertArrayNotNullNorHasNull(Object[], String)} with null object in array passed.
     * </p>
     * <p>
     * IllegalArgumentException exception is expected.
     * </p>
     */
    public void testAssertArrayNotNullNorHasNull2() {
        try {
            Helper.assertArrayNotNullNorHasNull(new Object[] {null}, "array");
            fail("IllegalArgumentException exception is expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests {@link Helper#assertLongPositive(long, String)} with not positive long passed.
     * </p>
     * <p>
     * IllegalArgumentException exception is expected.
     * </p>
     */
    public void testAssertLongPositive() {
        try {
            Helper.assertLongPositive(0, "val");
            fail("IllegalArgumentException exception is expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests {@link Helper#assertObjectNotNull(Object, String)} with null object passed.
     * </p>
     * <p>
     * IllegalArgumentException exception is expected.
     * </p>
     */
    public void testAssertObjectNotNull() {
        try {
            Helper.assertObjectNotNull(null, "obj");
            fail("IllegalArgumentException exception is expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests {@link Helper#assertStringNotNullNorEmpty(String, String)} with null object passed.
     * </p>
     * <p>
     * IllegalArgumentException exception is expected.
     * </p>
     */
    public void testAssertStringNotNullNorEmpty1() {
        try {
            Helper.assertStringNotNullNorEmpty(null, "obj");
            fail("IllegalArgumentException exception is expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests {@link Helper#assertStringNotNullNorEmpty(String, String)} with empty object passed.
     * </p>
     * <p>
     * IllegalArgumentException exception is expected.
     * </p>
     */
    public void testAssertStringNotNullNorEmpty2() {
        try {
            Helper.assertStringNotNullNorEmpty("", "obj");
            fail("IllegalArgumentException exception is expected.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests {@link Helper#closeConnection(Connection, com.topcoder.util.log.Log)} with valid arguments passed.
     * </p>
     * <p>
     * Connection should be closed.No exception is expected.
     * </p>
     * @throws Exception if any error occurs
     */
    public void testCloseConnection() throws Exception {
        Connection conn = TestHelper.getDBConnectionFactory().createConnection();
        Helper.closeConnection(conn, LogFactory.getLog());
        assertTrue("Connection should be closed.", conn.isClosed());
    }

    /**
     * <p>
     * Tests {@link Helper#closeStatement(PreparedStatement)} with valid arguments passed.
     * </p>
     * <p>
     * Statement should be closed.No exception is expected.
     * </p>
     * @throws Exception if any error occurs
     */
    public void testCloseStatement() throws Exception {
        PreparedStatement ps = connection.prepareStatement("select * from review");
        Helper.closeStatement(ps);
        try {
            ps.execute();
            fail("Statement should be closed.");
        } catch (SQLException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests {@link Helper#closeResultSet(ResultSet)} with valid arguments passed.
     * </p>
     * <p>
     * Result set should be closed.No exception is expected.
     * </p>
     * @throws Exception if any error occurs
     */
    public void testCloseResultSet() throws Exception {
        PreparedStatement ps = connection.prepareStatement("select * from review");
        ResultSet resultSet = ps.executeQuery();
        Helper.closeResultSet(resultSet);
        try {
            resultSet.next();
            fail("Result set should be closed.");
        } catch (SQLException e) {
            // expected
        }
    }

    /**
     * <p>
     * Tests {@link Helper#commitTransaction(Connection, com.topcoder.util.log.Log)} with valid arguments passed.
     * </p>
     * <p>
     * Transaction should be committed. No exception is expected.
     * </p>
     * @throws Exception if any error occurs
     */
    public void testCommitTransaction() throws Exception {
        connection.setAutoCommit(false);
        PreparedStatement ps = connection.prepareStatement("update review set review_id = -1 where review_id = -1");
        ps.executeUpdate();
        Helper.commitTransaction(connection, LogFactory.getLog());
    }

    /**
     * <p>
     * Tests {@link Helper#rollBackTransaction(Connection, com.topcoder.util.log.Log)} with valid arguments passed.
     * </p>
     * <p>
     * Transaction should be roll backed successfully. No exception is expected.
     * </p>
     * @throws Exception if any error occurs
     */
    public void testRollBackTransaction() throws Exception {
        connection.setAutoCommit(false);
        PreparedStatement ps = connection.prepareStatement("update review set review_id = -1 where review_id = -1");
        ps.executeUpdate();
        Helper.rollBackTransaction(connection, LogFactory.getLog());
    }

    /**
     * <p>
     * Tests {@link Helper#countEntities(String, String, long, Connection)} with valid arguments passed.
     * </p>
     * <p>
     * Count entities should be executed successfully. No exception is expected.
     * </p>
     * @throws Exception if any error occurs
     */
    public void testCountEntities() throws Exception {
        assertEquals("Count entities should be executed successfully.", 1,
                Helper.countEntities("upload", "upload_id", 1, connection));
    }

    /**
     * <p>
     * Tests {@link Helper#deleteEntities(String, String, java.util.Collection, Connection)} with valid arguments
     * passed.
     * </p>
     * <p>
     * Entities should be deleted successfully. No exception is expected.
     * </p>
     * @throws Exception if any error occurs
     */
    public void testDeleteEntities1() throws Exception {
        assertEquals("Entities should be deleted successfully.", 3,
                Helper.deleteEntities("upload", "upload_id", Arrays.asList(1L, 2L, 3L), connection));
    }

    /**
     * <p>
     * Tests {@link Helper#deleteEntities(String, String, long, Connection)} with valid arguments passed.
     * </p>
     * <p>
     * Entities should be deleted successfully. No exception is expected.
     * </p>
     * @throws Exception if any error occurs
     */
    public void testDeleteEntities2() throws Exception {
        assertEquals("Entities should be deleted successfully.", 1,
                Helper.deleteEntities("upload", "upload_id", 1L, connection));
    }

    /**
     * <p>
     * Tests {@link Helper#doDMLQuery(Connection, String, Object[])} with valid arguments passed.
     * </p>
     * <p>
     * Query should be executed successfully. No exception is expected.
     * </p>
     * @throws Exception if any error occurs
     */
    public void testDoDMLQuery() throws Exception {
        assertEquals("Query should be executed successfully.", 1,
                Helper.doDMLQuery(connection, "delete from upload where upload_id=?", new Object[] {1L}));
    }

    /**
     * <p>
     * Tests {@link Helper#doQuery(Connection, String, Object[], DataType[])} with valid arguments passed.
     * </p>
     * <p>
     * Query should be executed successfully. No exception is expected.
     * </p>
     * @throws Exception if any error occurs
     */
    public void testDoQuery() throws Exception {
        assertEquals("Query should be executed successfully.", 1, Helper.doQuery(connection,
                "select * from upload where upload_id=?", new Object[] {1L}, new DataType[] {Helper.LONG_TYPE}).length);
    }

    /**
     * <p>
     * Tests {@link Helper#doSingleValueQuery(Connection, String, Object[], DataType)} with valid arguments passed.
     * </p>
     * <p>
     * Query should be executed successfully. No exception is expected.
     * </p>
     * @throws Exception if any error occurs
     */
    public void testDoSingleValueQuery() throws Exception {
        assertEquals("Query should be executed successfully.", 1L, Helper.doSingleValueQuery(connection,
                "select count(*) from upload where upload_id=?", new Object[] {1L}, Helper.LONG_TYPE));
    }

    /**
     * <p>
     * Tests {@link Helper#getConfigurationParameterValue(ConfigManager, String, String, com.topcoder.util.log.Log)}
     * with valid arguments passed.
     * </p>
     * <p>
     * Configuration parameter should be retrieved successfully. No exception is expected.
     * </p>
     * @throws Exception if any error occurs
     */
    public void testGetConfigurationParameterValue() throws Exception {
        assertEquals("Configuration parameter should be retrieved successfully.",
                "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl", Helper.getConfigurationParameterValue(
                        ConfigManager.getInstance(),
                        "com.topcoder.management.review.persistence.InformixReviewPersistence",
                        "connection.factory_class", LogFactory.getLog()));
    }
}
