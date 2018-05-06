/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.cronos.onlinereview.deliverables.failuretests;

import com.cronos.onlinereview.deliverables.TestCasesDeliverableChecker;


/**
 * Failure tests for the class TestCasesDeliverableChecker.
 *
 * @author kinfkong
 * @version 1.0
 */
public class TestCasesDeliverableCheckerFailureTest extends FailureBaseTest {

    /**
     * Sets up the enviorment.
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Tests method for TestCasesDeliverableChecker(DBConnectionFactory).
     *
     * With null factory, IllegalArgumentException should be thrown.
     */
    public void testTestCasesDeliverableCheckerDBConnectionFactory_NullFactory() {
        try {
            new TestCasesDeliverableChecker(null);
            fail("The factory cannot be null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Tests method for TestCasesDeliverableChecker(DBConnectionFactory, String).
     *
     * With null factory, IllegalArgumentException should be thrown.
     */
    public void testTestCasesDeliverableCheckerDBConnectionFactoryString_NullFactory() {

        try {
            new TestCasesDeliverableChecker(null, "informix");
            fail("The factory cannot be null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }

    }

    /**
     * Tests method for TestCasesDeliverableChecker(DBConnectionFactory, String).
     *
     * With emptry connection name, IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testTestCasesDeliverableCheckerDBConnectionFactoryString_EmptyName() throws Exception {

        try {
            new TestCasesDeliverableChecker(getValidConnectionFactory(), "  ");
            fail("The connection name cannot be an empty string, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }

    }

}
