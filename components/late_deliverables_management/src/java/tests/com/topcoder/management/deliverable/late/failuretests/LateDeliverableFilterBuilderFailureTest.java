/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.management.deliverable.late.search.LateDeliverableFilterBuilder;

/**
 * <p>
 * Failure test for {@link LateDeliverableFilterBuilder}.
 * </p>
 *
 * @author mumujava
 * @version 1.0
 */
public class LateDeliverableFilterBuilderFailureTest extends TestCase {
    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a TestSuite for this test case.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(LateDeliverableFilterBuilderFailureTest.class);
        return suite;
    }
    /**
     * Failure test for method createProjectIdFilter() with illegal input.
     * Expects IllegalArgumentException.
     */
    public void test_createProjectIdFilter_1() throws Exception {
        try {
        	LateDeliverableFilterBuilder.createProjectIdFilter(-1);
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method createProjectStatusIdFilter() with illegal input.
     * Expects IllegalArgumentException.
     */
    public void test_createProjectStatusIdFilter_1() throws Exception {
        try {
        	LateDeliverableFilterBuilder.createProjectStatusIdFilter(-1);
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method createProjectCategoryIdFilter() with illegal input.
     * Expects IllegalArgumentException.
     */
    public void test_createProjectCategoryIdFilter_1() throws Exception {
        try {
        	LateDeliverableFilterBuilder.createProjectCategoryIdFilter(-1);
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method createDeliverableIdFilter() with illegal input.
     * Expects IllegalArgumentException.
     */
    public void test_createDeliverableIdFilter_1() throws Exception {
        try {
        	LateDeliverableFilterBuilder.createDeliverableIdFilter(-1);
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method createLateDeliverableTypeIdFilter() with illegal input.
     * Expects IllegalArgumentException.
     */
    public void test_createLateDeliverableTypeIdFilter_1() throws Exception {
        try {
            LateDeliverableFilterBuilder.createLateDeliverableTypeIdFilter(-1);
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
        try {
            LateDeliverableFilterBuilder.createLateDeliverableTypeIdFilter(0);
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
}
