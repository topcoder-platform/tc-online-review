/*
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved.
 */
package com.cronos.onlinereview.deliverables;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.persistence.DeliverableCheckingException;

/**
 * <p>
 * Unit tests for the {@link com.cronos.onlinereview.deliverables.SqlDeliverableChecker} class.
 * </p>
 *
 * @author kr00tki
 * @version 1.0
 */
public class SqlDeliverableCheckerTest extends DbTestCase {

    /**
     * The SqlDeliverableChecker instance to test on.
     */
    private SqlDeliverableChecker checker = null;

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
     * Tests the {@link SqlDeliverableChecker#SqlDeliverableChecker(DBConnectionFactory)}
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
     * Tests the {@link SqlDeliverableChecker#SqlDeliverableChecker(DBConnectionFactory)}
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
     * Tests the {@link SqlDeliverableChecker#SqlDeliverableChecker(DBConnectionFactory, String)}
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
     * Tests the {@link SqlDeliverableChecker#SqlDeliverableChecker(DBConnectionFactory, String)}
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
     * Tests the {@link SqlDeliverableChecker#SqlDeliverableChecker(DBConnectionFactory, String)}
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
     * Tests the {@link SqlDeliverableChecker#SqlDeliverableChecker(DBConnectionFactory, String)}
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
     * Tests the {@link SqlDeliverableChecker#createDatabaseConnection()} method accuracy.
     *
     * @throws Exception to JUnit.
     */
    public void testCreateConnection() throws Exception {
        assertNotNull("Connection should be created.", checker.createDatabaseConnection());
    }

    /**
     * Dummy SqlDeliverableChecker.
     *
     * @author kr00tki
     * @version 1.0
     */
    private class DummyChecker extends SqlDeliverableChecker {

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
         * @param deliverable ignored.
         * @throws DeliverableCheckingException never.
         */
        public void check(Deliverable deliverable) throws DeliverableCheckingException {

        }

    }
}
