/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.failuretests;

import junit.framework.TestCase;

import com.topcoder.management.review.scoreaggregator.PlaceAssignmentAlgorithm;
import com.topcoder.management.review.scoreaggregator.impl.StandardPlaceAssignment;


/**
 * <p>
 * Failure tests for <tt>StandardPlaceAssignment</tt>.
 * </p>
 *
 * @author GavinWang
 * @version 1.0
 */
public class StandardPlaceAssignmentFailureTests extends TestCase {
    /** An instance of StandardPlaceAssignment for testing. */
    private PlaceAssignmentAlgorithm algo;

    /**
     * <p>
     * Set up each test.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        algo = new StandardPlaceAssignment();
    }

    /**
     * Failure test for assignPlacements(int[]). Inputs: null placements. Expectation: IllegalArgumentException should
     * be thrown.
     */
    public void testAssignPlacementsNullPlacements() {
        try {
            algo.assignPlacements(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for assignPlacements(int[]). Inputs: placements array containing negative entries. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testAssignPlacementsNegativePlacements() {
        try {
            algo.assignPlacements(new int[] {1, 2, -5});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for assignPlacements(int[]). Inputs: placements array containing zero entries. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testAssignPlacementsZeroPlacements() {
        try {
            algo.assignPlacements(new int[] {1, 2, 0});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for assignPlacements(int[]). Inputs: placements array containing 'gaps' among entries. Expectation:
     * IllegalArgumentException should be thrown.
     */
    public void testAssignPlacementsPlacementsGaps() {
        try {
            algo.assignPlacements(new int[] {1, 2, 4, 5});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure test for assignPlacements(int[]). Inputs: placements array containing 'gaps' among entries, but another
     * condition. Expectation: IllegalArgumentException should be thrown.
     */
    public void testAssignPlacementsPlacementsGaps2() {
        try {
            algo.assignPlacements(new int[] {1, 2, 2, 4});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}
