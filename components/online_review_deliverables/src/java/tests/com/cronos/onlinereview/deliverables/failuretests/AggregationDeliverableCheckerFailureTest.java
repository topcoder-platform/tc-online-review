/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.deliverables.failuretests;

import com.cronos.onlinereview.deliverables.AggregationDeliverableChecker;


/**
 * Failure tests for the class AggregationDeliverableChecker.
 *
 * @author kinfkong
 * @version 1.0
 */
public class AggregationDeliverableCheckerFailureTest extends FailureBaseTest {

    /**
     * Sets up the enviorment.
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Tests method for AggregationDeliverableChecker(DBConnectionFactory).
     *
     * With null factory, IllegalArgumentException should be thrown.
     */
    public void testAggregationDeliverableCheckerDBConnectionFactory_NullFactory() {
        try {
            new AggregationDeliverableChecker(null);
            fail("The factory cannot be null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Tests method for AggregationDeliverableChecker(DBConnectionFactory, String).
     *
     * With null factory, IllegalArgumentException should be thrown.
     */
    public void testAggregationDeliverableCheckerDBConnectionFactoryString_NullFactory() {

        try {
            new AggregationDeliverableChecker(null, "informix");
            fail("The factory cannot be null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }

    }

    /**
     * Tests method for AggregationDeliverableChecker(DBConnectionFactory, String).
     *
     * With emptry connection name, IllegalArgumentException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testAggregationDeliverableCheckerDBConnectionFactoryString_EmptyName() throws Exception {

        try {
            new AggregationDeliverableChecker(getValidConnectionFactory(), "  ");
            fail("The connection name cannot be an empty string, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // success
        }

    }

}
