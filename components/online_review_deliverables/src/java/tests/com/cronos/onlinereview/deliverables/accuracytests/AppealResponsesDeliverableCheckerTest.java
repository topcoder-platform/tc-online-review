/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.deliverables.accuracytests;

import junit.framework.TestCase;

import com.cronos.onlinereview.deliverables.AppealResponsesDeliverableChecker;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;

/**
 * Set of tests for AppealResponsesDeliverableChecker class.
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class AppealResponsesDeliverableCheckerTest extends TestCase {

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
        // loading all configurations
        AccuracyTestHelper.loadAllConfig();
        // creating connection factory
        connectionFactory =
            new DBConnectionFactoryImpl("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");
        connectionName = "informix";
    }

    /**
     * Testing for correctly assinged connection factory.
     * @throws Exception wraping all exceptions
     */
    public final void testAppealResponsesDeliverableCheckerDBConnectionFactory() throws Exception {
        assertSame("connection factories must be equal", AccuracyTestHelper.getDeclaredField(
            new AppealResponsesDeliverableChecker(connectionFactory), "connectionFactory"),
            connectionFactory);
    }

    /**
     * Testing that connection name was not assigned.
     * @throws Exception wraping all exceptions
     */
    public final void testAppealResponsesDeliverableCheckerDBConnectionFactoryNullConnectionName()
        throws Exception {
        assertNull("connection name must be null", AccuracyTestHelper.getDeclaredField(
            new AppealResponsesDeliverableChecker(connectionFactory), "connectionName"));
    }

    /**
     * Testing for correctly assinged connection factory.
     * @throws Exception wraping all exceptions
     */
    public final void testAppealResponsesDeliverableCheckerDBConnectionFactoryStringConnectionFactory()
        throws Exception {
        assertSame("connection factories must be equal", AccuracyTestHelper.getDeclaredField(
            new AppealResponsesDeliverableChecker(connectionFactory, connectionName),
            "connectionFactory"), connectionFactory);
    }

    /**
     * Testing for correctly assinged connection name.
     * @throws Exception wraping all exceptions
     */
    public final void testAppealResponsesDeliverableCheckerDBConnectionFactoryStringConnectionName()
        throws Exception {
        assertEquals("connection names must be equal", AccuracyTestHelper.getDeclaredField(
            new AppealResponsesDeliverableChecker(connectionFactory, connectionName),
            "connectionName"), connectionName);
    }

    /**
     * tear down for each test.
     * @throws Exception wraping all exceptions
     */
    protected void tearDown() throws Exception {
        AccuracyTestHelper.clearTestConfig();
    }
}