/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.deliverables.failuretests;

import java.sql.PreparedStatement;

import com.cronos.onlinereview.deliverables.AggregationDeliverableChecker;
import com.cronos.onlinereview.deliverables.CommittedReviewDeliverableChecker;
import com.cronos.onlinereview.deliverables.FinalReviewDeliverableChecker;
import com.cronos.onlinereview.deliverables.SingleQuerySqlDeliverableChecker;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.persistence.DeliverableCheckingException;

/**
 * Failure tests for the class SingleQuerySqlDeliverableChecker.
 *
 * @author kinfkong
 * @version 1.0
 */
public class SingleQuerySqlDeliverableCheckerFailureTest extends FailureBaseTest {

    /**
     * Tests method check(Deliverable).
     *
     * With null deliverable, IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testCheck_NullDeliverable() throws Exception {

        // create a mock-up instance of checker
        SingleQuerySqlDeliverableChecker checker = new SingleQuerySqlDeliverableChecker(
                getValidConnectionFactory()) {
            public void fillInQueryParameters(Deliverable deliverable, PreparedStatement statement) {
                // empty and mock
            }
            public String getSqlQuery() {
                return null;
            }
        };

        try {
            checker.check(null);
            fail("The delivery cannot be null, should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // success
        }

    }

    /**
     * Tests method check(Deliverable).
     *
     * With an invalid connnection factory to fail the connection, DeliverableCheckingException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testCheck_FailConnection() throws Exception {

        // we use a sub-class of SingleQuerySqlDeliverableChecker: AggregationDeliverableChecker for the test.
        AggregationDeliverableChecker checker = new AggregationDeliverableChecker(getInvalidConnectionFactory());
        // check it
        try {
            checker.check(new Deliverable(1, 1, 1, new Long(123), true));
            fail("The connection is mock and incorrect, DeliverableCheckingException is expected.");
        } catch (DeliverableCheckingException e) {
            // success
        }
    }

    /**
     * Tests method check(Deliverable).
     *
     * With the sub-class: CommittedReviewDeliverableChecker for test.
     *
     * And the parameter: deliverable's submission is null, i.e, deliverable.getSubmission() == null.
     *
     * fillInQueryParameters() should throw DeliverableCheckingException and then pass the exception
     *
     * to the method: check.
     *
     * @throws Exception to JUnit
     */
    public void testCheck_With_CommittedReviewDeliverableChecker() throws Exception {
        CommittedReviewDeliverableChecker checker = new CommittedReviewDeliverableChecker(
                getValidConnectionFactory());
        try {
            checker.check(new Deliverable(1, 1, 1, null, true));
            fail("Deliverable's submission is null, DeliverableCheckingException should be thrown.");
        } catch (DeliverableCheckingException e) {
            // success
        }
    }

    /**
     * Tests method check(Deliverable).
     *
     * With the sub-class: FinalReviewDeliverableChecker for test.
     *
     * And the parameter: deliverable's submission is null, i.e, deliverable.getSubmission() == null.
     *
     * fillInQueryParameters() should throw DeliverableCheckingException and then pass the exception
     *
     * to the method: check.
     *
     * @throws Exception to JUnit
     */
    public void testCheck_With_FinalReviewDeliverableChecker() throws Exception {
        FinalReviewDeliverableChecker checker = new FinalReviewDeliverableChecker(
                getValidConnectionFactory());
        try {
            checker.check(new Deliverable(1, 1, 1, null, true));
            fail("Deliverable's submission is null, DeliverableCheckingException should be thrown.");
        } catch (DeliverableCheckingException e) {
            // success
        }
    }

    /**
     * Tests method check(Deliverable).
     *
     * With the sub-class: SubmitterCommentDeliverableChecker for test.
     *
     * And the parameter: deliverable's submission is null, i.e, deliverable.getSubmission() == null.
     *
     * fillInQueryParameters() should throw DeliverableCheckingException and then pass the exception
     *
     * to the method: check.
     *
     * @throws Exception to JUnit
     */
    public void testCheck_With_SubmitterCommentDeliverableChecker() throws Exception {
        FinalReviewDeliverableChecker checker = new FinalReviewDeliverableChecker(
                getValidConnectionFactory());
        try {
            checker.check(new Deliverable(1, 1, 1, null, true));
            fail("Deliverable's submission is null, DeliverableCheckingException should be thrown.");
        } catch (DeliverableCheckingException e) {
            // success
        }
    }


}
