/*
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved.
 */
package com.cronos.onlinereview.deliverables;

import java.sql.PreparedStatement;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.management.deliverable.Deliverable;

/**
 * <p>
 * Unit tests for the {@link com.cronos.onlinereview.deliverables.SingleQuerySqlDeliverableChecker} class.
 * </p>
 *
 * @author kr00tki
 * @version 1.0
 */
public class SingleQuerySqlDeliverableCheckerTest extends DbTestCase {

    /**
     * The SingleQuerySqlDeliverableChecker instance to test on.
     */
    private SingleQuerySqlDeliverableChecker checker = null;

    /**
     * Sets up the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        checker = new DummyChecker(getConnectionFactory(), CONNECTION_NAME);
    }

    /**
     * <p>
     * Tests the {@link SingleQuerySqlDeliverableChecker#SingleQuerySqlDeliverableChecker(DBConnectionFactory)}
     * constructor accuracy. Checks if internal fields are properly set.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testConstructor_Factory() throws Exception {
        checker = new DummyChecker(getConnectionFactory());
        assertSame("Incorrect connection factory.", getConnectionFactory(), getFieldFromBaseClass(checker,
                "connectionFactory"));
        assertNull("Name should be null.", getFieldFromBaseClass(checker, "connectionName"));
    }

    /**
     * <p>
     * Tests the {@link SingleQuerySqlDeliverableChecker#SingleQuerySqlDeliverableChecker(DBConnectionFactory)}
     * constructor failure. Checks if exception is thrown when the <code>connectionFactory</code> is <code>null</code>.
     * </p>
     */
    public void testConstructor_Factory_Null() {
        try {
            new DummyChecker(null);
            fail("Null connection factor, IAE expected.");
        } catch (IllegalArgumentException ex) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the {@link SingleQuerySqlDeliverableChecker#SingleQuerySqlDeliverableChecker(DBConnectionFactory, String)}
     * constructor accuracy. Checks if internal fields are properly set.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testConstructor_FactoryString() throws Exception {
        checker = new DummyChecker(getConnectionFactory(), CONNECTION_NAME);
        assertSame("Incorrect connection factory.", getConnectionFactory(), getFieldFromBaseClass(checker,
                "connectionFactory"));
        assertEquals("Incorrect connection name.", CONNECTION_NAME, getFieldFromBaseClass(checker, "connectionName"));
    }

    /**
     * <p>
     * Tests the {@link SingleQuerySqlDeliverableChecker#SingleQuerySqlDeliverableChecker(DBConnectionFactory, String)}
     * constructor accuracy. Checks if internal fields are properly set and <code>null</code>
     * <code>connectionName</code> is accepted.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testConstructor_FactoryString_NullName() throws Exception {
        checker = new DummyChecker(getConnectionFactory(), null);
        assertSame("Incorrect connection factory.", getConnectionFactory(), getFieldFromBaseClass(checker,
                "connectionFactory"));
        assertNull("Name should be null.", getFieldFromBaseClass(checker, "connectionName"));
    }

    /**
     * <p>
     * Tests the {@link SingleQuerySqlDeliverableChecker#SingleQuerySqlDeliverableChecker(DBConnectionFactory, String)}
     * constructor failure. Checks if exception is thrown when the <code>connectionFactory</code> is <code>null</code>.
     * </p>
     */
    public void testConstructor_FactoryString_NullFactory() {
        try {
            new DummyChecker(null, CONNECTION_NAME);
            fail("Null connection factor, IAE expected.");
        } catch (IllegalArgumentException ex) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the {@link SingleQuerySqlDeliverableChecker#SingleQuerySqlDeliverableChecker(DBConnectionFactory, String)}
     * constructor failure. Checks if exception is thrown when the <code>connectionName</code> is <code>empty</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testConstructor_FactoryString_EmptyName() throws Exception {
        try {
            new DummyChecker(getConnectionFactory(), " ");
            fail("Empty connection name, IAE expected.");
        } catch (IllegalArgumentException ex) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the . method failure. Checks if IllegalArgumentException is thrown on <code>null</code>
     * deliverable.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testCheck_Null() throws Exception {
        try {
            checker.check(null);
            fail("The deliverable is null, IAE expected.");
        } catch (IllegalArgumentException ex) {
            // ok
        }
    }

    /**
     * The dummy single query checker.
     *
     * @author kr00tki
     * @version 1.0
     */
    private class DummyChecker extends SingleQuerySqlDeliverableChecker {

        /**
         * Constructor.
         *
         * @param connectionFactory factory.
         */
        protected DummyChecker(DBConnectionFactory connectionFactory) {
            super(connectionFactory);
        }

        /**
         * Constructor.
         *
         * @param connectionFactory factory
         * @param connectionName name
         */
        protected DummyChecker(DBConnectionFactory connectionFactory, String connectionName) {
            super(connectionFactory, connectionName);
        }

        /**
         * Not implemented.
         *
         * @param deliverable ignored
         * @param statement ignored
         */
        protected void fillInQueryParameters(Deliverable deliverable, PreparedStatement statement) {

        }

        /**
         * Returns null.
         *
         * @return null in this case.
         */
        protected String getSqlQuery() {
            return null;
        }

    }
}
