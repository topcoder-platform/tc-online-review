/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.deliverables.accuracytests;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.cronos.onlinereview.deliverables.SqlDeliverableChecker;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.persistence.DeliverableCheckingException;

/**
 * Set of tests for SingleQuerySqlDeliverableChecker class.
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class SingleQuerySqlDeliverableCheckerTest extends TestCase {

    /**
     * Instance of connection factory for testing.
     */
    private DBConnectionFactoryImpl connectionFactory;
    /**
     * Connection name for testing.
     */
    private String connectionName;

    /**
     * Set up for each test.
     * @throws Exception wraps all exceptions
     */
    protected void setUp() throws Exception {
        AccuracyTestHelper.loadAllConfig();
        connectionFactory = new DBConnectionFactoryImpl("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");
        connectionName = "informix";
    }

    /**
     * Testing for correctly assinged connection factory.
     * @throws Exception wraping all exceptions
     */
    public final void testSingleQuerySqlDeliverableCheckerDBConnectionFactory() throws Exception {
        assertSame("connection factories must be equal", AccuracyTestHelper.getDeclaredField(
            new SingleQuerySqlDeliverableCheckerMock(connectionFactory), "connectionFactory"), connectionFactory);
    }

    /**
     * Testing that connection name was not assigned.
     * @throws Exception wraping all exceptions
     */
    public final void testSingleQuerySqlDeliverableCheckerDBConnectionFactoryNullConnectionName() throws Exception {
        assertNull("connection name must be null", AccuracyTestHelper.getDeclaredField(
            new SingleQuerySqlDeliverableCheckerMock(connectionFactory), "connectionName"));
    }

    /**
     * Testing for correctly assinged connection factory.
     * @throws Exception wraping all exceptions
     */
    public final void testSingleQuerySqlDeliverableCheckerDBConnectionFactoryStringConnectionFactory()
        throws Exception {
        assertSame("connection factories must be equal", AccuracyTestHelper.getDeclaredField(
            new SingleQuerySqlDeliverableCheckerMock(connectionFactory, connectionName), "connectionFactory"),
            connectionFactory);
    }

    /**
     * Testing for correctly assinged connection name.
     * @throws Exception wraping all exceptions
     */
    public final void testSingleQuerySqlDeliverableCheckerDBConnectionFactoryStringConnectionName() throws Exception {
        assertEquals("connection names must be equal", AccuracyTestHelper.getDeclaredField(
            new SingleQuerySqlDeliverableCheckerMock(connectionFactory, connectionName), "connectionName"),
            connectionName);
    }

    /**
     * Testing connection creation..
     * @throws Exception wraping all exceptions
     */
    public final void testCreateDatabaseConnection() throws Exception {
        SingleQuerySqlDeliverableCheckerMock singleQuerySqlDeliverableCheckerMock =
            new SingleQuerySqlDeliverableCheckerMock(connectionFactory, connectionName);
        Class clazz = singleQuerySqlDeliverableCheckerMock.getClass();
        while (clazz != null) {
            if (clazz.getSuperclass() == Object.class) {
                break;
            }
            clazz = clazz.getSuperclass();
        }
        Method createConnectionMethod = clazz.getDeclaredMethod("createDatabaseConnection", new Class[0]);
        createConnectionMethod.setAccessible(true);
        // testing that connection was created
        assertNotNull("connection must be created", createConnectionMethod.invoke(
            singleQuerySqlDeliverableCheckerMock, new Object[0]));
    }

    private class SingleQuerySqlDeliverableCheckerMock extends SqlDeliverableChecker {

        /**
         * SqlDeliverableChecker constructor: Creates a new
         * SqlDeliverableChecker. The connectionName field is set to null.
         * @param connectionFactory The connection factory to use for getting
         *            connections to the database.
         * @throws IllegalArgumentException If connectionFactory is null.
         */
        public SingleQuerySqlDeliverableCheckerMock(DBConnectionFactory connectionFactory) {
            super(connectionFactory);
        }

        /**
         * SqlDeliverableChecker constructor: Creates a new
         * SqlDeliverableChecker. All fields are set to the given values.
         * @param connectionFactory The connection factory to use for getting
         *            connections to the database.
         * @param connectionName The name of the connection to use. Can be null.
         * @throws IllegalArgumentException If connectionFactory is null.
         * @throws IllegalArgumentException If connectionName is the empty
         *             string
         */
        public SingleQuerySqlDeliverableCheckerMock(DBConnectionFactory connectionFactory, String connectionName) {
            super(connectionFactory, connectionName);
        }

        /**
         * Checks the given deliverable to see if it is complete. If it is
         * complete, the checker marks the deliverable as complete by calling
         * the setCompletionDate method.
         * @param deliverable The deliverable to check
         * @throws IllegalArgumentException If deliverable is null
         * @throws DeliverableCheckingException exception
         */
        public void check(Deliverable deliverable) throws DeliverableCheckingException {
            throw new DeliverableCheckingException("message");
        }
    }

    /**
     * tear down for each test.
     * @throws Exception wraping all exceptions
     */
    protected void tearDown() throws Exception {
        AccuracyTestHelper.clearTestConfig();
    }
}