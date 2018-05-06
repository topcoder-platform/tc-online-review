/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.deliverables.failuretests;

import com.cronos.onlinereview.deliverables.SubmitterCommentDeliverableChecker;


/**
 * Failure tests for the class SubmitterCommentDeliverableChecker.
 *
 * @author kinfkong
 * @version 1.0
 */
public class SubmitterCommentDeliverableCheckerFailureTest extends FailureBaseTest {

    /**
     * Sets up the enviorment.
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Tests method for SubmitterCommentDeliverableChecker(DBConnectionFactory).
     *
     * With null factory, IllegalArgumentException should be thrown.
     */
    public void testSubmitterCommentDeliverableCheckerDBConnectionFactory_NullFactory() {
        try {
            new SubmitterCommentDeliverableChecker(null);
            fail("The factory cannot be null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Tests method for SubmitterCommentDeliverableChecker(DBConnectionFactory, String).
     *
     * With null factory, IllegalArgumentException should be thrown.
     */
    public void testSubmitterCommentDeliverableCheckerDBConnectionFactoryString_NullFactory() {

        try {
            new SubmitterCommentDeliverableChecker(null, "informix");
            fail("The factory cannot be null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }

    }

    /**
     * Tests method for SubmitterCommentDeliverableChecker(DBConnectionFactory, String).
     *
     * With emptry connection name, IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testSubmitterCommentDeliverableCheckerDBConnectionFactoryString_EmptyName() throws Exception {

        try {
            new SubmitterCommentDeliverableChecker(getValidConnectionFactory(), "  ");
            fail("The connection name cannot be an empty string, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }

    }

}
