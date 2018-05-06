/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.deliverables.failuretests;

import com.cronos.onlinereview.deliverables.FinalReviewDeliverableChecker;


/**
 * Failure tests for the class FinalReviewDeliverableChecker.
 *
 * @author kinfkong
 * @version 1.0
 */
public class FinalReviewDeliverableCheckerFailureTest extends FailureBaseTest {

    /**
     * Sets up the enviorment.
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Tests method for FinalReviewDeliverableChecker(DBConnectionFactory).
     *
     * With null factory, IllegalArgumentException should be thrown.
     */
    public void testFinalReviewDeliverableCheckerDBConnectionFactory_NullFactory() {
        try {
            new FinalReviewDeliverableChecker(null);
            fail("The factory cannot be null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Tests method for FinalReviewDeliverableChecker(DBConnectionFactory, String).
     *
     * With null factory, IllegalArgumentException should be thrown.
     */
    public void testFinalReviewDeliverableCheckerDBConnectionFactoryString_NullFactory() {

        try {
            new FinalReviewDeliverableChecker(null, "informix");
            fail("The factory cannot be null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }

    }

    /**
     * Tests method for FinalReviewDeliverableChecker(DBConnectionFactory, String).
     *
     * With emptry connection name, IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testFinalReviewDeliverableCheckerDBConnectionFactoryString_EmptyName() throws Exception {

        try {
            new FinalReviewDeliverableChecker(getValidConnectionFactory(), "  ");
            fail("The connection name cannot be an empty string, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }

    }

}
