/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.accuracytests;

import com.topcoder.management.review.scoreaggregator.impl.StandardPlaceAssignment;

import junit.framework.TestCase;


/**
 * <p>
 * Accuracy tests of the StandardPlaceAssignment.
 * </p>
 *
 * @author magicpig
 * @version 1.0
 */
public class StandardPlaceAssignmentAccuracyTests extends TestCase {
    /**
     * Test of constructor.
     */
    public void testCtor() {
        assertNotNull("Unable to create StandardPlaceAssignment.", new StandardPlaceAssignment());
    }

    /**
     * Test of assignPlacements.
     */
    public void testAssignPlacements1() {
        int[] initialPlacements = new int[55];
        int[] expectedPlacements = new int[55];
        int k = 0;

        // generate data : 1, 2,2, 3,3,3, 4,4,4,4, ...... 10
        for (int i = 1; i <= 10; i++) {
            for (int j = 0; j < i; j++) {
                initialPlacements[k] = i;
                expectedPlacements[k] = (k + 1) - j;
                k++;
            }
        }

        // get the result
        int[] gotPlacements = new StandardPlaceAssignment().assignPlacements(initialPlacements);

        // compare them
        for (int i = 0; i < 55; i++) {
            assertEquals("Fails to invoke assignPlacements", expectedPlacements[i], gotPlacements[i]);
        }
    }

    /**
     * Test of assignPlacements.
     */
    public void testAssignPlacements2() {
        // 9 data
        int[] initialPlacements = new int[] {3, 2, 6, 6, 1, 5, 4, 4, 5};
        int[] expectedPlacements = new int[] {3, 2, 8, 8, 1, 6, 4, 4, 6};

        // get the result
        int[] gotPlacements = new StandardPlaceAssignment().assignPlacements(initialPlacements);

        assertEquals("Fails to invoke assignPlacements", 9, gotPlacements.length);

        // compare them
        for (int i = 0; i < 9; i++) {
            assertEquals("Fails to invoke assignPlacements", expectedPlacements[i], gotPlacements[i]);
        }
    }

    /**
     * Test of assignPlacements.
     */
    public void testAssignPlacements3() {
        int[] initialPlacements = new int[100];

        // generate data : 1,2,3,100
        for (int i = 1; i <= 100; i++) {
            initialPlacements[i - 1] = i;
        }

        // get the result
        int[] gotPlacements = new StandardPlaceAssignment().assignPlacements(initialPlacements);

        // compare them
        for (int i = 1; i <= 100; i++) {
            assertEquals("Fails to invoke assignPlacements", i, gotPlacements[i - 1]);
        }
    }

    /**
     * Test of assignPlacements.
     */
    public void testAssignPlacements4() {
        // get the result for zero-leng parameter
        int[] gotPlacements = new StandardPlaceAssignment().assignPlacements(new int[0]);

        // check result
        assertEquals("Fails to invoke assignPlacements", 0, gotPlacements.length);
    }
}
