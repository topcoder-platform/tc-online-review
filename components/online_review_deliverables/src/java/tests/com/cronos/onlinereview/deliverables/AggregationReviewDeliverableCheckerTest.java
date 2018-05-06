/*
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved.
 */
package com.cronos.onlinereview.deliverables;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.topcoder.management.deliverable.Deliverable;

/**
 * <p>
 * Unit tests for the {@link com.cronos.onlinereview.deliverables.AggregationReviewDeliverableChecker} class.
 * </p>
 *
 * @author kr00tki
 * @version 1.0
 */
public class AggregationReviewDeliverableCheckerTest extends DbTestCase {

    /**
     * The AggregationReviewDeliverableChecker instance to test on.
     */
    private AggregationReviewDeliverableChecker checker = null;

    /**
     * Sets up the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        checker = new AggregationReviewDeliverableChecker(getConnectionFactory(), CONNECTION_NAME);
    }

    /**
     * <p>
     * Tests the
     * {@link AggregationReviewDeliverableChecker#AggregationReviewDeliverableChecker(DBConnectionFactory)}
     * constructor accuracy. Checks if internal fields are properly set.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testConstructor_Factory() throws Exception {
        checker = new AggregationReviewDeliverableChecker(getConnectionFactory());
        assertSame("Incorrect connection factory.", getConnectionFactory(), getFieldFromBaseClass(checker,
                "connectionFactory"));
        assertNull("Name should be null.", getFieldFromBaseClass(checker, "connectionName"));
    }

    /**
     * <p>
     * Tests the
     * {@link AggregationReviewDeliverableChecker#AggregationReviewDeliverableChecker(DBConnectionFactory)}
     * constructor failure. Checks if exception is thrown when the <code>connectionFactory</code> is
     * <code>null</code>.
     * </p>
     */
    public void testConstructor_Factory_Null() {
        try {
            new AggregationReviewDeliverableChecker(null);
            fail("Null connection factor, IAE expected.");
        } catch (IllegalArgumentException ex) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the
     * {@link AggregationReviewDeliverableChecker#AggregationReviewDeliverableChecker(DBConnectionFactory, String)}
     * constructor accuracy. Checks if internal fields are properly set.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testConstructor_FactoryString() throws Exception {
        checker = new AggregationReviewDeliverableChecker(getConnectionFactory(), CONNECTION_NAME);
        assertSame("Incorrect connection factory.", getConnectionFactory(), getFieldFromBaseClass(checker,
                "connectionFactory"));
        assertEquals("Incorrect connection name.", CONNECTION_NAME, getFieldFromBaseClass(checker,
                "connectionName"));
    }

    /**
     * <p>
     * Tests the
     * {@link AggregationReviewDeliverableChecker#AggregationReviewDeliverableChecker(DBConnectionFactory, String)}
     * constructor accuracy. Checks if internal fields are properly set and <code>null</code>
     * <code>connectionName</code>
     * is accepted.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testConstructor_FactoryString_NullName() throws Exception {
        checker = new AggregationReviewDeliverableChecker(getConnectionFactory(), null);
        assertSame("Incorrect connection factory.", getConnectionFactory(), getFieldFromBaseClass(checker,
                "connectionFactory"));
        assertNull("Name should be null.", getFieldFromBaseClass(checker, "connectionName"));
    }

    /**
     * <p>
     * Tests the
     * {@link AggregationReviewDeliverableChecker#AggregationReviewDeliverableChecker(DBConnectionFactory, String)}
     * constructor failure. Checks if exception is thrown when the <code>connectionFactory</code> is
     * <code>null</code>.
     * </p>
     */
    public void testConstructor_FactoryString_NullFactory() {
        try {
            new AggregationReviewDeliverableChecker(null, CONNECTION_NAME);
            fail("Null connection factor, IAE expected.");
        } catch (IllegalArgumentException ex) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the
     * {@link AggregationReviewDeliverableChecker#AggregationReviewDeliverableChecker(DBConnectionFactory, String)}
     * constructor failure. Checks if exception is thrown when the <code>connectionName</code> is
     * <code>empty</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testConstructor_FactoryString_EmptyName() throws Exception {
        try {
            new AggregationReviewDeliverableChecker(getConnectionFactory(), " ");
            fail("Empty connection name, IAE expected.");
        } catch (IllegalArgumentException ex) {
            // ok
        }
    }

    /**
     * Tests the {@link AggregationReviewDeliverableChecker#fillInQueryParameters(Deliverable, PreparedStatement)}
     * method accuracy. It uses a proxy to check what methods are called on the PreparedStatement with which
     * arguments.
     *
     * @throws Exception to JUnit.
     */
    public void testFillInQueryParameters() throws Exception {
        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(checker.getSqlQuery());
        TestProxy proxy = new TestProxy(pstmt);
        checker.fillInQueryParameters(new Deliverable(2, 1, 1, new Long(1), true), proxy.getProxy());

        assertEquals("Incorrect resource_id", new Long(1), proxy.getValue(0));
    }

    /**
     * Tests the {@link AggregationReviewDeliverableChecker#getSqlQuery()} method. Checks if it returns non-null
     * and non-empty String. Accuracy of the returned query is check in other tests.
     */
    public void testGetSqlQuery() {
        assertNotNull("Query should not be null.", checker.getSqlQuery());
        assertTrue("Query should not be empty.", checker.getSqlQuery().length() > 0);
    }

    /**
     * Tests the {@link SingleQuerySqlDeliverableChecker#check(Deliverable)} method accuracy.
     *
     * @throws Exception to Junit.
     */
    public void testCheck_Approved() throws Exception {
        createReview(10, 2, 1, true);
        createReviewComment(100, AGGREGATION_REVIEW_COMMENT, 2, APPROVED, 10);
        Deliverable deliverable = new Deliverable(1, 1, 2, null, false);
        checker.check(deliverable);
        assertNotNull("The deliverable should be completed.", deliverable.getCompletionDate());
    }

    /**
     * Tests the {@link SingleQuerySqlDeliverableChecker#check(Deliverable)} method accuracy.
     *
     * @throws Exception to Junit.
     */
    public void testCheck_Rejected() throws Exception {
        createReview(10, 2, 1, true);
        createReviewComment(100, AGGREGATION_REVIEW_COMMENT, 2, REJECTED, 10);
        createReviewComment(101, AGGREGATION_REVIEW_COMMENT, 2, APPROVED, 10);
        Deliverable deliverable = new Deliverable(1, 1, 2, null, false);
        checker.check(deliverable);
        assertNotNull("The deliverable should be completed.", deliverable.getCompletionDate());
    }

    /**
     * Tests the {@link SingleQuerySqlDeliverableChecker#check(Deliverable)} method accuracy.
     *
     * @throws Exception to Junit.
     */
    public void testCheck_EmptyExtraComment() throws Exception {
        createReview(10, 2, 1, true);
        createReviewComment(100, AGGREGATION_REVIEW_COMMENT, 2, "", 10);
        Deliverable deliverable = new Deliverable(1, 1, 2, null, false);
        checker.check(deliverable);
        assertNull("The deliverable should not be completed.", deliverable.getCompletionDate());
    }

    /**
     * Tests the {@link SingleQuerySqlDeliverableChecker#check(Deliverable)} method accuracy.
     *
     * @throws Exception to Junit.
     */
    public void testCheck_NoResource() throws Exception {
        createReview(10, 2, 1, true);
        createReviewComment(100, AGGREGATION_REVIEW_COMMENT, 2, "", 10);
        Deliverable deliverable = new Deliverable(1, 1, 2, null, false);
        checker.check(deliverable);
        assertNull("The deliverable should not be completed.", deliverable.getCompletionDate());
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
