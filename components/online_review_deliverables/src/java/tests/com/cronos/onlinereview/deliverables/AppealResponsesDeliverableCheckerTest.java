/*
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved.
 */
package com.cronos.onlinereview.deliverables;

import com.topcoder.management.deliverable.Deliverable;

/**
 * <p>
 * Unit tests for the {@link com.cronos.onlinereview.deliverables.AppealResponsesDeliverableChecker} class.
 * </p>
 *
 * @author kr00tki
 * @version 1.0
 */
public class AppealResponsesDeliverableCheckerTest extends DbTestCase {

    /**
     * The AppealResponsesDeliverableChecker instance to test on.
     */
    private AppealResponsesDeliverableChecker checker = null;

    /**
     * Sets up the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        checker = new AppealResponsesDeliverableChecker(getConnectionFactory(), CONNECTION_NAME);
    }

    /**
     * <p>
     * Tests the {@link AppealResponsesDeliverableChecker#AppealResponsesDeliverableChecker(DBConnectionFactory)}
     * constructor accuracy. Checks if internal fields are properly set.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testConstructor_Factory() throws Exception {
        checker = new AppealResponsesDeliverableChecker(getConnectionFactory());
        assertSame("Incorrect connection factory.", getConnectionFactory(), getFieldFromBaseClass(checker,
                "connectionFactory"));
        assertNull("Name should be null.", getFieldFromBaseClass(checker, "connectionName"));
    }

    /**
     * <p>
     * Tests the {@link AppealResponsesDeliverableChecker#AppealResponsesDeliverableChecker(DBConnectionFactory)}
     * constructor failure. Checks if exception is thrown when the <code>connectionFactory</code> is
     * <code>null</code>.
     * </p>
     */
    public void testConstructor_Factory_Null() {
        try {
            new AppealResponsesDeliverableChecker(null);
            fail("Null connection factor, IAE expected.");
        } catch (IllegalArgumentException ex) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the
     * {@link AppealResponsesDeliverableChecker#AppealResponsesDeliverableChecker(DBConnectionFactory, String)}
     * constructor accuracy. Checks if internal fields are properly set.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testConstructor_FactoryString() throws Exception {
        checker = new AppealResponsesDeliverableChecker(getConnectionFactory(), CONNECTION_NAME);
        assertSame("Incorrect connection factory.", getConnectionFactory(), getFieldFromBaseClass(checker,
                "connectionFactory"));
        assertEquals("Incorrect connection name.", CONNECTION_NAME, getFieldFromBaseClass(checker,
                "connectionName"));
    }

    /**
     * <p>
     * Tests the
     * {@link AppealResponsesDeliverableChecker#AppealResponsesDeliverableChecker(DBConnectionFactory, String)}
     * constructor accuracy. Checks if internal fields are properly set and <code>null</code>
     * <code>connectionName</code>
     * is accepted.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testConstructor_FactoryString_NullName() throws Exception {
        checker = new AppealResponsesDeliverableChecker(getConnectionFactory(), null);
        assertSame("Incorrect connection factory.", getConnectionFactory(), getFieldFromBaseClass(checker,
                "connectionFactory"));
        assertNull("Name should be null.", getFieldFromBaseClass(checker, "connectionName"));
    }

    /**
     * <p>
     * Tests the
     * {@link AppealResponsesDeliverableChecker#AppealResponsesDeliverableChecker(DBConnectionFactory, String)}
     * constructor failure. Checks if exception is thrown when the <code>connectionFactory</code> is
     * <code>null</code>.
     * </p>
     */
    public void testConstructor_FactoryString_NullFactory() {
        try {
            new AppealResponsesDeliverableChecker(null, CONNECTION_NAME);
            fail("Null connection factor, IAE expected.");
        } catch (IllegalArgumentException ex) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the
     * {@link AppealResponsesDeliverableChecker#AppealResponsesDeliverableChecker(DBConnectionFactory, String)}
     * constructor failure. Checks if exception is thrown when the <code>connectionName</code> is
     * <code>empty</code>.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testConstructor_FactoryString_EmptyName() throws Exception {
        try {
            new AppealResponsesDeliverableChecker(getConnectionFactory(), " ");
            fail("Empty connection name, IAE expected.");
        } catch (IllegalArgumentException ex) {
            // ok
        }
    }

    /**
     * Test the {@link AppealResponsesDeliverableChecker#check(Deliverable)} method accuracy.
     *
     * @throws Exception to JUnit.
     */
    public void testCheck_Response() throws Exception {
        createReview(10, 1, 1, true);
        createReviewItem(1, 10);
        createReviewItemComment(1, APPEAL, 1, "", 1);
        createReviewItemComment(2, APPEAL_RESPONSE, 1, "", 1);

        Deliverable deliverable = new Deliverable(1, 1, 1, new Long(1), true);
        checker.check(deliverable);
        assertNotNull("The deliverable should be completed.", deliverable.getCompletionDate());
    }

    /**
     * Test the {@link AppealResponsesDeliverableChecker#check(Deliverable)} method accuracy.
     *
     * @throws Exception to JUnit.
     */
    public void testCheck_NoResponse() throws Exception {
        createReview(10, 1, 1, true);
        createReviewItem(1, 10);
        createReviewItemComment(1, APPEAL, 1, "", 1);

        Deliverable deliverable = new Deliverable(1, 1, 1, new Long(1), true);
        checker.check(deliverable);
        assertNull("The deliverable should not be completed.", deliverable.getCompletionDate());
    }

    /**
     * Test the {@link AppealResponsesDeliverableChecker#check(Deliverable)} method accuracy.
     *
     * @throws Exception to JUnit.
     */
    public void testCheck_NoAppeal() throws Exception {
        createReview(10, 1, 1, true);
        createReviewItem(1, 10);
        createReviewItemComment(2, APPEAL_RESPONSE, 1, "", 1);

        Deliverable deliverable = new Deliverable(1, 1, 1, new Long(1), true);
        checker.check(deliverable);
        assertNull("The deliverable not should be completed.", deliverable.getCompletionDate());
    }

    /**
     * Test the {@link AppealResponsesDeliverableChecker#check(Deliverable)} method accuracy.
     *
     * @throws Exception to JUnit.
     */
    public void testCheck_Response2() throws Exception {
        createReview(10, 1, 1, true);
        createReviewItem(1, 10);
        createReviewItem(2, 10);
        createReviewItem(3, 10);
        createReviewItemComment(1, APPEAL, 1, "", 1);
        createReviewItemComment(2, APPEAL_RESPONSE, 1, "", 1);
        createReviewItemComment(3, APPEAL, 1, "", 2);
        createReviewItemComment(4, APPEAL_RESPONSE, 1, "", 2);
        createReviewItemComment(5, APPEAL, 1, "", 3);
        createReviewItemComment(6, APPEAL_RESPONSE, 1, "", 3);

        Deliverable deliverable = new Deliverable(1, 1, 1, new Long(1), true);
        checker.check(deliverable);
        assertNotNull("The deliverable should be completed.", deliverable.getCompletionDate());
    }

    /**
     * Test the {@link AppealResponsesDeliverableChecker#check(Deliverable)} method accuracy.
     *
     * @throws Exception to JUnit.
     */
    public void testCheck_NoResponse2() throws Exception {
        createReview(10, 1, 1, true);
        createReviewItem(1, 10);
        createReviewItem(2, 10);
        createReviewItem(3, 10);
        createReviewItemComment(1, APPEAL, 1, "", 1);
        createReviewItemComment(2, APPEAL_RESPONSE, 1, "", 1);
        createReviewItemComment(3, APPEAL, 1, "", 2);
        createReviewItemComment(5, APPEAL, 1, "", 3);
        createReviewItemComment(6, APPEAL_RESPONSE, 1, "", 3);

        Deliverable deliverable = new Deliverable(1, 1, 1, new Long(1), true);
        checker.check(deliverable);
        assertNull("The deliverable should not be completed.", deliverable.getCompletionDate());
    }

    /**
     * <p>
     * Tests the {@link AppealResponsesDeliverableChecker#check(Deliverable)} method failure. Checks if
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
