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

import com.cronos.onlinereview.deliverables.AggregationReviewDeliverableChecker;
import com.informix.jdbc.IfxInt8;
import com.informix.jdbc.IfxPreparedStatement;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.deliverable.Deliverable;

/**
 * Set of tests for AggregationReviewDeliverableChecker class.
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class AggregationReviewDeliverableCheckerTest extends TestCase {

    /**
     * SQL, required for successfull running.
     */
    private static final String SQL =
        "SELECT MAX(review_comment.modify_date) FROM review_comment INNER JOIN comment_type_lu "
            + "ON review_comment.comment_type_id = comment_type_lu.comment_type_id "
            + "INNER JOIN review ON review.review_id = review_comment.review_id "
            + "INNER JOIN resource ON resource.resource_id = review_comment.resource_id "
            + "WHERE review_comment.resource_id = ? AND comment_type_lu.name = 'Aggregation Review Comment' "
            + "AND (review_comment.extra_info = 'Approved' OR review_comment.extra_info = 'Rejected')";

    /**
     * Instance of AggregationReviewDeliverableChecker for testing.
     */
    private AggregationReviewDeliverableChecker aggregationReviewDeliverableChecker;

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
        connectionFactory =
            new DBConnectionFactoryImpl("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");
        connectionName = "informix";
        connection = connectionFactory.createConnection(connectionName);
        // clear database tables before running tests
        AccuracyTestHelper.clearTables(connection);
        // adding test data
        AccuracyTestHelper.addTestData(connection);
        aggregationReviewDeliverableChecker =
            new AggregationReviewDeliverableChecker(connectionFactory);
        // creating deliverable
        deliverable = new Deliverable(1, 2, 3, new Long(4), true);
    }

    /**
     * Testing for correctly assinged connection factory.
     * @throws Exception wraping all exceptions
     */
    public final void testAggregationReviewDeliverableCheckerDBConnectionFactory() throws Exception {
        assertSame("connection factories must be equal", AccuracyTestHelper.getDeclaredField(
            new AggregationReviewDeliverableChecker(connectionFactory), "connectionFactory"),
            connectionFactory);
    }

    /**
     * Testing that connection name was not assigned.
     * @throws Exception wraping all exceptions
     */
    public final void testAggregationReviewDeliverableCheckerDBConnectionFactoryNullConnectionName()
        throws Exception {
        assertNull("connection name must be null", AccuracyTestHelper.getDeclaredField(
            new AggregationReviewDeliverableChecker(connectionFactory), "connectionName"));
    }

    /**
     * Testing for correctly assinged connection factory.
     * @throws Exception wraping all exceptions
     */
    public final void testAggregationReviewDeliverableCheckerDBConnectionFactoryStringConnectionFactory()
        throws Exception {
        assertSame("connection factories must be equal", AccuracyTestHelper.getDeclaredField(
            new AggregationReviewDeliverableChecker(connectionFactory, connectionName),
            "connectionFactory"), connectionFactory);
    }

    /**
     * Testing for correctly assinged connection name.
     * @throws Exception wraping all exceptions
     */
    public final void testAggregationReviewDeliverableCheckerDBConnectionFactoryStringConnectionName()
        throws Exception {
        assertEquals("connection names must be equal", AccuracyTestHelper.getDeclaredField(
            new AggregationReviewDeliverableChecker(connectionFactory, connectionName),
            "connectionName"), connectionName);
    }

    /**
     * Testing for correctly set first sql parameter.
     * @throws Exception wraping all exceptions
     */
    public final void testFillInQueryParameters() throws Exception {
        assertEquals(deliverable.getResource(), ((IfxInt8) getParametersVector().get(0))
            .toLong());
    }

    /**
     * Testing for correctly set second sql parameter.
     * @throws Exception wraping all exceptions
     */
    public final void testGetSqlQuery() throws Exception {
        // getting method through invocation
        Method method =
            AggregationReviewDeliverableChecker.class
                .getDeclaredMethod("getSqlQuery", new Class[0]);
        method.setAccessible(true);
        // checking for correct sql statement
        assertNotNull("sql statement must be generated", method.invoke(
            aggregationReviewDeliverableChecker, new Object[0]));
    }

    /**
     * Testing that not null is returned as sql.
     * @throws Exception wraping all exceptions
     */
    public final void testCheckCompleted() throws Exception {
        aggregationReviewDeliverableChecker.check(deliverable);
        assertNotNull("completion date must be populated", deliverable.getCompletionDate());
    }

    /**
     * Testing that completed date were assigned.
     * @throws Exception wraping all exceptions
     */
    public final void testCheckNotCompleted() throws Exception {
        aggregationReviewDeliverableChecker.check(new Deliverable(2, 3, 4, new Long(5), false));
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
            AggregationReviewDeliverableChecker.class.getDeclaredMethod("fillInQueryParameters",
                new Class[] {Deliverable.class, PreparedStatement.class });
        method.setAccessible(true);
        Connection currentConnection = connectionFactory.createConnection();
        PreparedStatement preparedStatement = currentConnection.prepareStatement(SQL);
        method.invoke(aggregationReviewDeliverableChecker, new Object[] {deliverable,
            preparedStatement });
        // getting the vector of parameters
        Field field = IfxPreparedStatement.class.getDeclaredField("vector");
        field.setAccessible(true);
        return (Vector) field.get(preparedStatement);
    }
}