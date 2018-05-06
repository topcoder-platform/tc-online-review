/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.deliverables.failuretests;

import com.cronos.onlinereview.deliverables.SubmissionDeliverableChecker;


/**
 * Failure tests for the class SubmissionDeliverableChecker.
 *
 * @author kinfkong
 * @version 1.0
 */
public class SubmissionDeliverableCheckerFailureTest extends FailureBaseTest {

    /**
     * Sets up the enviorment.
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Tests method for SubmissionDeliverableChecker(DBConnectionFactory).
     *
     * With null factory, IllegalArgumentException should be thrown.
     */
    public void testSubmissionDeliverableCheckerDBConnectionFactory_NullFactory() {
        try {
            new SubmissionDeliverableChecker(null);
            fail("The factory cannot be null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Tests method for SubmissionDeliverableChecker(DBConnectionFactory, String).
     *
     * With null factory, IllegalArgumentException should be thrown.
     */
    public void testSubmissionDeliverableCheckerDBConnectionFactoryString_NullFactory() {

        try {
            new SubmissionDeliverableChecker(null, "informix");
            fail("The factory cannot be null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }

    }

    /**
     * Tests method for SubmissionDeliverableChecker(DBConnectionFactory, String).
     *
     * With emptry connection name, IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testSubmissionDeliverableCheckerDBConnectionFactoryString_EmptyName() throws Exception {

        try {
            new SubmissionDeliverableChecker(getValidConnectionFactory(), "  ");
            fail("The connection name cannot be an empty string, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }

    }

}
