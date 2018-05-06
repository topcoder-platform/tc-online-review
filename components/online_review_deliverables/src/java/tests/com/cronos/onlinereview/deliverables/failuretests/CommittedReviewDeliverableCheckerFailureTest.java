/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.deliverables.failuretests;

import com.cronos.onlinereview.deliverables.CommittedReviewDeliverableChecker;


/**
 * Failure tests for the class CommittedReviewDeliverableChecker.
 *
 * @author kinfkong
 * @version 1.0
 */
public class CommittedReviewDeliverableCheckerFailureTest extends FailureBaseTest {

    /**
     * Sets up the enviorment.
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Tests method for CommittedReviewDeliverableChecker(DBConnectionFactory).
     *
     * With null factory, IllegalArgumentException should be thrown.
     */
    public void testCommittedReviewDeliverableCheckerDBConnectionFactory_NullFactory() {
        try {
            new CommittedReviewDeliverableChecker(null);
            fail("The factory cannot be null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Tests method for CommittedReviewDeliverableChecker(DBConnectionFactory, String).
     *
     * With null factory, IllegalArgumentException should be thrown.
     */
    public void testCommittedReviewDeliverableCheckerDBConnectionFactoryString_NullFactory() {

        try {
            new CommittedReviewDeliverableChecker(null, "informix");
            fail("The factory cannot be null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }

    }

    /**
     * Tests method for CommittedReviewDeliverableChecker(DBConnectionFactory, String).
     *
     * With emptry connection name, IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testCommittedReviewDeliverableCheckerDBConnectionFactoryString_EmptyName() throws Exception {

        try {
            new CommittedReviewDeliverableChecker(getValidConnectionFactory(), "  ");
            fail("The connection name cannot be an empty string, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }

    }

}
