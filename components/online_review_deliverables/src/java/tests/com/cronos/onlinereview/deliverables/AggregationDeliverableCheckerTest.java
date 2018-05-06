/*
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved.
 */
package com.cronos.onlinereview.deliverables;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.topcoder.management.deliverable.Deliverable;

/**
 * <p>
 * Unit tests for the {@link com.cronos.onlinereview.deliverables.AggregationDeliverableChecker} class.
 * </p>
 *
 * @author kr00tki
 * @version 1.0
 */
public class AggregationDeliverableCheckerTest extends DbTestCase {

    /**
     * The AggregationDeliverableChecker instance to test on.
     */
    private AggregationDeliverableChecker checker = null;

    /**
     * Sets up the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        checker = new AggregationDeliverableChecker(getConnectionFactory(), CONNECTION_NAME);
        createReview(1, 1, 1, true);
        createReview(2, 1, 2, false);
    }

    /**
     * Tests the {@link AggregationDeliverableChecker#fillInQueryParameters(Deliverable, PreparedStatement)} method
     * accuracy. It uses a proxy to check what methods are called on the PreparedStatement with which arguments.
     *
     * @throws Exception to JUnit.
     */
    public void testFillInQueryParameters() throws Exception {
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(checker.getSqlQuery());
        TestProxy proxy = new TestProxy(pstmt);
        checker.fillInQueryParameters(new Deliverable(2, 1, 1, new Long(1), true), proxy.getProxy());

        assertEquals("Incorrect resource_id", new Long(1), proxy.getValue(0));
        assertEquals("Incorrect project_id", new Long(2), proxy.getValue(1));
    }

    /**
     * Tests the {@link AggregationDeliverableChecker#getSqlQuery()} method. Checks if it returns non-null and
     * non-empty String. Accuracy of the returned query is check in other tests.
     */
    public void testGetSqlQuery() {
        assertNotNull("Should return query.", checker.getSqlQuery());
        assertTrue("Should not be empty", checker.getSqlQuery().length() > 0);
    }

    /**
     * <p>
     * Tests the {@link AggregationDeliverableChecker#AggregationDeliverableChecker(DBConnectionFactory)}
     * constructor accuracy. Checks if internal fields are properly set.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testConstructor_Factory() throws Exception {
        checker = new AggregationDeliverableChecker(getConnectionFactory());
        assertSame("Incorrect connection factory.", getConnectionFactory(), getFieldFromBaseClass(checker,
                "connectionFactory"));
        assertNull("Name should be null.", getFieldFromBaseClass(checker, "connectionName"));
    }

    /**
     * <p>
     * Tests the {@link AggregationDeliverableChecker#AggregationDeliverableChecker(DBConnectionFactory)}
     * constructor failure. Checks if exception is thrown when the <code>connectionFactory</code> is
     * <code>null</code>.
     * </p>
     */
    public void testConstructor_Factory_Null() {
        try {
            new AggregationDeliverableChecker(null);
            fail("Null connection factor, IAE expected.");
        } catch (IllegalArgumentException ex) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the {@link AggregationDeliverableChecker#AggregationDeliverableChecker(DBConnectionFactory, String)}
     * constructor accuracy. Checks if internal fields are properly set.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testConstructor_FactoryString() throws Exception {
        checker = new AggregationDeliverableChecker(getConnectionFactory(), CONNECTION_NAME);
        assertSame("Incorrect connection factory.", getConnectionFactory(), getFieldFromBaseClass(checker,
                "connectionFactory"));
        assertEquals("Incorrect connection name.", CONNECTION_NAME, getFieldFromBaseClass(checker,
                "connectionName"));
    }

    /**
     * <p>
     * Tests the {@link AggregationDeliverableChecker#AggregationDeliverableChecker(DBConnectionFactory, String)}
     * constructor accuracy. Checks if internal fields are properly set and <code>null</code>
     * <code>connectionName</code>
     * is accepted.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testConstructor_FactoryString_NullName() throws Exception {
        checker = new AggregationDeliverableChecker(getConnectionFactory(), null);
        assertSame("Incorrect connection factory.", getConnectionFactory(), getFieldFromBaseClass(checker,
                "connectionFactory"));
        assertNull("Name should be null.", getFieldFromBaseClass(checker, "connectionName"));
    }

    /**
     * <p>
     * Tests the {@link AggregationDeliverableChecker#AggregationDeliverableChecker(DBConnectionFactory, String)}
     * constructor failure. Checks if exception is thrown when the <code>connectionFactory</code> is
     * <code>null</code>.
     * </p>
     */
    public void testConstructor_FactoryString_NullFactory() {
        try {
            new AggregationDeliverableChecker(null, CONNECTION_NAME);
            fail("Null connection factor, IAE expected.");
        } catch (IllegalArgumentException ex) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the {@link AggregationDeliverableChecker#AggregationDeliverableChecker(DBConnectionFactory, String)}
     * constructor failure. Checks if exception is thrown when the <code>connectionName</code> is
     * <code>empty</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testConstructor_FactoryString_EmptyName() throws Exception {
        try {
            new AggregationDeliverableChecker(getConnectionFactory(), " ");
            fail("Empty connection name, IAE expected.");
        } catch (IllegalArgumentException ex) {
            // ok
        }
    }

    /**
     * Tests the {@link SingleQuerySqlDeliverableChecker#check(Deliverable)} method accuracy.
     *
     * @throws Exception to JUnit.
     */
    public void testCheck_Commited() throws Exception {
        Deliverable deliverable = new Deliverable(1, 1, 1, new Long(1), true);
        checker.check(deliverable);
        assertNotNull(deliverable.getCompletionDate());
    }

    /**
     * Tests the {@link SingleQuerySqlDeliverableChecker#check(Deliverable)} method accuracy.
     *
     * @throws Exception to JUnit.
     */
    public void testCheck_Unommited() throws Exception {
        Deliverable deliverable = new Deliverable(2, 1, 1, new Long(1), true);

        checker.check(deliverable);
        assertNull(deliverable.getCompletionDate());
    }

    /**
     * Tests the {@link SingleQuerySqlDeliverableChecker#check(Deliverable)} method accuracy.
     *
     * @throws Exception to JUnit.
     */
    public void testCheck_ReviewForResourceNotExists() throws Exception {
        Deliverable deliverable = new Deliverable(5, 1, 2, new Long(1), true);
        checker.check(deliverable);
        assertNull(deliverable.getCompletionDate());
    }

    /**
     * <p>
     * Tests the {@link SingleQuerySqlDeliverableChecker#check(Deliverable)} method failure. Checks if
     * IllegalArgumentException is thrown on <code>null</code> deliverable.
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

}
