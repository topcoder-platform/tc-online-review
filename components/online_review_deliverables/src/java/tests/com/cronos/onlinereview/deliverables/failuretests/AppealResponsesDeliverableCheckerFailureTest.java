/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.deliverables.failuretests;

import com.cronos.onlinereview.deliverables.AppealResponsesDeliverableChecker;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.persistence.DeliverableCheckingException;


/**
 * Failure tests for the class AppealResponsesDeliverableChecker.
 *
 * @author kinfkong
 * @version 1.0
 */
public class AppealResponsesDeliverableCheckerFailureTest extends FailureBaseTest {

    /**
     * An instance for AppealResponsesDeliverableChecker.
     */
    private AppealResponsesDeliverableChecker checker;

    /**
     * Sets up the enviorment.
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();
        checker = new AppealResponsesDeliverableChecker(getValidConnectionFactory());
    }

    /**
     * Tests method for AppealResponsesDeliverableChecker(DBConnectionFactory).
     *
     * With null factory, IllegalArgumentException should be thrown.
     */
    public void testAppealResponsesDeliverableCheckerDBConnectionFactory_NullFactory() {
        try {
            new AppealResponsesDeliverableChecker(null);
            fail("The factory cannot be null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Tests method for AppealResponsesDeliverableChecker(DBConnectionFactory, String).
     *
     * With null factory, IllegalArgumentException should be thrown.
     */
    public void testAppealResponsesDeliverableCheckerDBConnectionFactoryString_NullFactory() {

        try {
            new AppealResponsesDeliverableChecker(null, "informix");
            fail("The factory cannot be null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }

    }

    /**
     * Tests method for AppealResponsesDeliverableChecker(DBConnectionFactory, String).
     *
     * With emptry connection name, IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testAppealResponsesDeliverableCheckerDBConnectionFactoryString_EmptyName() throws Exception {

        try {
            new AppealResponsesDeliverableChecker(getValidConnectionFactory(), "  ");
            fail("The connection name cannot be an empty string, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }

    }

    /**
     * Tests method for check(Deliverable).
     *
     * With null deliverable, IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testCheck_NullDeliverable() throws Exception {
        try {
            checker.check(null);
            fail("The deliverable cannot be null.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Tests method for check(Deliverable).
     *
     * With a invalid connection factory to create an invalid connection, DeliverableCheckingException is expected.
     *
     * @throws Exception to JUnit
     */
    public void testCheck_FailConnect() throws Exception {
        checker = new AppealResponsesDeliverableChecker(getInvalidConnectionFactory());
        try {
            checker.check(new Deliverable(1, 1, 1, new Long(1), true));
            fail("Should throw DeliverableCheckingException.");
        } catch (DeliverableCheckingException e) {
            // success
        }
    }

}
