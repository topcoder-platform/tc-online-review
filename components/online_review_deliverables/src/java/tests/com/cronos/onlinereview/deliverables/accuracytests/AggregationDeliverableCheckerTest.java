/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.deliverables.accuracytests;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Vector;

import junit.framework.TestCase;

import com.cronos.onlinereview.deliverables.AggregationDeliverableChecker;
import com.informix.jdbc.IfxInt8;
import com.informix.jdbc.IfxPreparedStatement;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.deliverable.Deliverable;

/**
 * Set of tests for AggregationDeliverableChecker class.
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class AggregationDeliverableCheckerTest extends TestCase {

    /**
     * SQL, required for successfull running.
     */
    private static final String SQL =
        "SELECT MAX(review.modify_date) FROM review INNER JOIN resource ON review.resource_id = resource.resource_id "
            + "WHERE committed = 1 AND review.resource_id = ? AND resource.project_id = ?";

    /**
     * Instance of AggregationDeliverableChecker for testing.
     */
    private AggregationDeliverableChecker aggregationDeliverableChecker;

    /**
     * Instance of connection factory for testing.
     */
    private DBConnectionFactoryImpl connectionFactory;

    /**
     * Instance of Deliverable for testing.
     */
    private Deliverable deliverable;

    /**
     * Connection name for testing.
     */
    private String connectionName;

    /**
     * Instance of Connection for testing.
     */
    private Connection connection;

    /**
     * Set up for each test.
     * @throws Exception wraps all exceptions
     */
    protected void setUp() throws Exception {
        // loading all configurations
        AccuracyTestHelper.loadAllConfig();
        // initializing connection to database
        connectionFactory = new DBConnectionFactoryImpl("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");
        connectionName = "informix";
        connection = connectionFactory.createConnection(connectionName);
        // clear database tables before running tests
        AccuracyTestHelper.clearTables(connection);
        // adding test data
        AccuracyTestHelper.addTestData(connection);
        aggregationDeliverableChecker = new AggregationDeliverableChecker(connectionFactory);
        // creating deliverable
        deliverable = new Deliverable(1, 2, 3, new Long(4), true);
    }

    /**
     * Testing for correctly assinged connection factory.
     * @throws Exception wraping all exceptions
     */
    public final void testAggregationDeliverableCheckerDBConnectionFactory() throws Exception {
        assertSame("connection factories must be equal", AccuracyTestHelper.getDeclaredField(
            new AggregationDeliverableChecker(connectionFactory), "connectionFactory"), connectionFactory);
    }

    /**
     * Testing that connection name was not assigned.
     * @throws Exception wraping all exceptions
     */
    public final void testAggregationDeliverableCheckerDBConnectionFactoryNullConnectionName() throws Exception {
        assertNull("connection name must be null", AccuracyTestHelper.getDeclaredField(
            new AggregationDeliverableChecker(connectionFactory), "connectionName"));
    }

    /**
     * Testing for correctly assinged connection factory.
     * @throws Exception wraping all exceptions
     */
    public final void testAggregationDeliverableCheckerDBConnectionFactoryStringConnectionFactory() throws Exception {
        assertSame("connection factories must be equal", AccuracyTestHelper.getDeclaredField(
            new AggregationDeliverableChecker(connectionFactory, connectionName), "connectionFactory"),
            connectionFactory);
    }

    /**
     * Testing for correctly assinged connection name.
     * @throws Exception wraping all exceptions
     */
    public final void testAggregationDeliverableCheckerDBConnectionFactoryStringConnectionName() throws Exception {
        assertEquals("connection names must be equal", AccuracyTestHelper.getDeclaredField(
            new AggregationDeliverableChecker(connectionFactory, connectionName), "connectionName"), connectionName);
    }

    /**
     * Testing for correctly set first sql parameter.
     * @throws Exception wraping all exceptions
     */
    public final void testFillInQueryParameters1() throws Exception {
        assertEquals(deliverable.getResource(), ((IfxInt8) getParametersVector().get(0)).toLong());
    }

    /**
     * Testing for correctly set second sql parameter.
     * @throws Exception wraping all exceptions
     */
    public final void testFillInQueryParameters2() throws Exception {
        assertEquals(deliverable.getProject(), ((IfxInt8) getParametersVector().get(1)).toLong());
    }

    /**
     * Testing that not null is returned as sql.
     * @throws Exception wraping all exceptions
     */
    public final void testGetSqlQuery() throws Exception {
        // getting method through invocation
        Method method = AggregationDeliverableChecker.class.getDeclaredMethod("getSqlQuery", new Class[0]);
        method.setAccessible(true);
        // checking for correct sql statement
        assertNotNull("sql statement must be generated", method.invoke(aggregationDeliverableChecker, new Object[0]));
    }

    /**
     * Testing that completed date were assigned.
     * @throws Exception wraping all exceptions
     */
    public final void testCheckCompleted() throws Exception {
        aggregationDeliverableChecker.check(deliverable);
        assertNotNull("completion date must be populated", deliverable.getCompletionDate());
    }

    /**
     * Testing that completed date were not assigned.
     * @throws Exception wraping all exceptions
     */
    public final void testCheckNotCompleted() throws Exception {
        aggregationDeliverableChecker.check(new Deliverable(2, 3, 4, new Long(5), false));
        assertNull("completion date must be not populated", deliverable.getCompletionDate());
    }

    /**
     * tear down for each test.
     * @throws Exception wraping all exceptions
     */
    protected void tearDown() throws Exception {
        AccuracyTestHelper.clearTestConfig();
        AccuracyTestHelper.clearTables(connection);
        connection.close();
    }

    /**
     * Getting the vector of parameters from prepared statement.
     * @return vector of parameters
     * @throws Exception wrap all exceptions
     */
    private Vector getParametersVector() throws Exception {
        // invoking fillInQueryParameters method
        Method method =
            AggregationDeliverableChecker.class.getDeclaredMethod("fillInQueryParameters", new Class[] {
                Deliverable.class, PreparedStatement.class });
        method.setAccessible(true);
        Connection currentConnection = connectionFactory.createConnection();
        PreparedStatement preparedStatement = currentConnection.prepareStatement(SQL);
        method.invoke(aggregationDeliverableChecker, new Object[] {deliverable, preparedStatement });
        // getting the vector of parameters
        Field field = IfxPreparedStatement.class.getDeclaredField("vector");
        field.setAccessible(true);
        return (Vector) field.get(preparedStatement);
    }
}