/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.impl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all test cases for <code>StandardPlaceAssignment</code>.
 * </p>
 *
 * @author daiwb
 * @version 1.0
 */
public class StandardPlaceAssignmentTest extends TestCase {
    /**
     * A <code>StandardPlaceAssignment</code> instance used for tests.
     */
    private StandardPlaceAssignment algo = null;

    /**
     * Returns the test suite of this test case.
     *
     * @return the test suite of this test case.
     */
    public static Test suite() {
        return new TestSuite(StandardPlaceAssignmentTest.class);
    }

    /**
     * Set up.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        algo = new StandardPlaceAssignment();
    }

    /**
     * Tear down.
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        algo = null;
    }

    /**
     * Test method <code>StandardPlaceAssignment()</code>.
     */
    public void testStandardPlaceAssignment_Accuracy() {
        assertNotNull("The instance should be created successfully.", algo);
    }

    /**
     * Test method <code>assignPlacements(int[])</code>.
     */
    public void testAssignPlacements_Accuracy_1() {
        int[] original = new int[] {1, 2, 2, 3};
        int[] expected = new int[] {1, 2, 2, 4};
        int[] received = algo.assignPlacements(original);
        assertEquals("Received array not match expected one.", expected.length, received.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Received array not match expected one.", expected[i], received[i]);
        }
    }

    /**
     * Test method <code>assignPlacements(int[])</code>.
     */
    public void testAssignPlacements_Accuracy_2() {
        int[] original = new int[] {2, 1, 2, 3};
        int[] expected = new int[] {2, 1, 2, 4};
        int[] received = algo.assignPlacements(original);
        assertEquals("Received array not match expected one.", expected.length, received.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Received array not match expected one.", expected[i], received[i]);
        }
    }

    /**
     * Test method <code>assignPlacements(int[])</code>.
     */
    public void testAssignPlacements_Accuracy_3() {
        int[] original = new int[] {2, 1, 2, 3, 6, 6, 4, 5, 6, 7};
        int[] expected = new int[] {2, 1, 2, 4, 7, 7, 5, 6, 7, 10};
        int[] received = algo.assignPlacements(original);
        assertEquals("Received array not match expected one.", expected.length, received.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Received array not match expected one.", expected[i], received[i]);
        }
    }

    /**
     * Test method <code>assignPlacements(int[])</code>.
     */
    public void testAssignPlacements_Accuracy_4() {
        int[] original = new int[] {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        int[] expected = new int[] {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        int[] received = algo.assignPlacements(original);
        assertEquals("Received array not match expected one.", expected.length, received.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Received array not match expected one.", expected[i], received[i]);
        }
    }

    /**
     * Test method <code>assignPlacements(int[])</code>.
     */
    public void testAssignPlacements_Accuracy_5() {
        int[] original = new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] expected = new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        int[] received = algo.assignPlacements(original);
        assertEquals("Received array not match expected one.", expected.length, received.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Received array not match expected one.", expected[i], received[i]);
        }
    }

    /**
     * Test method <code>assignPlacements(int[])</code>.
     */
    public void testAssignPlacements_Accuracy_6() {
        int[] original = new int[] {1, 2, 2, 3, 3, 4, 4, 5, 5, 6};
        int[] expected = new int[] {1, 2, 2, 4, 4, 6, 6, 8, 8, 10};
        int[] received = algo.assignPlacements(original);
        assertEquals("Received array not match expected one.", expected.length, received.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Received array not match expected one.", expected[i], received[i]);
        }
    }

    /**
     * Test method <code>assignPlacements(int[])</code>.
     */
    public void testAssignPlacements_Accuracy_7() {
        int[] original = new int[] {5, 9, 4, 4, 2, 3, 1, 7, 6, 8};
        int[] expected = new int[] {6, 10, 4, 4, 2, 3, 1, 8, 7, 9};
        int[] received = algo.assignPlacements(original);
        assertEquals("Received array not match expected one.", expected.length, received.length);
        for (int i = 0; i < expected.length; ++i) {
            assertEquals("Received array not match expected one.", expected[i], received[i]);
        }
    }

    /**
     * Test method <code>assignPlacements(int[])</code> with null <code>initialPlacements</code>.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testAssignPlacements_NullArg() {
        try {
            algo.assignPlacements(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>assignPlacements(int[])</code> with <code>initialPlacements</code> containing negative element.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testAssignPlacements_ArrayWithNegativeElement() {
        try {
            algo.assignPlacements(new int[] {1, -2});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>assignPlacements(int[])</code> with <code>initialPlacements</code> containing zero element.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testAssignPlacements_ArrayWithZeroElement() {
        try {
            algo.assignPlacements(new int[] {1, 0});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>assignPlacements(int[])</code> with invalid <code>initialPlacements</code>.
     * Gaps exist in the array.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testAssignPlacements_InvalidArray_2() {
        try {
            algo.assignPlacements(new int[] {1, 2, 2, 4});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>assignPlacements(int[])</code> with invalid <code>initialPlacements</code>.
     * The highest placement is not 1.
     * <code>IllegalArgumentException</code> is expected.
     */
    public void testAssignPlacements_InvalidArray_3() {
        try {
            algo.assignPlacements(new int[] {2, 2, 2, 3});
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * Test method <code>assignPlacements(int[])</code> with empty <code>initialPlacements</code>.
     */
    public void testAssignPlacements_EmptyArray() {
        int[] received = algo.assignPlacements(new int[0]);
        assertEquals("Empty array should be returned.", 0, received.length);
    }

}
