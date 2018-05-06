/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.impl;

import java.util.Arrays;

import com.topcoder.management.review.scoreaggregator.PlaceAssignmentAlgorithm;
import com.topcoder.management.review.scoreaggregator.Util;

/**
 * <p>
 * This is the standard implementation of the PlaceAssignmentAlgorithm interface. This implementation skips
 * intermediate rankings (e.g, 1,2,2,3 becomes 1,2,2,4) after ties are applied.
 * </p>
 *
 * <p>
 * Implementations of this interface should be implemented in a thread-safe manner.
 * </p>
 *
 * @author dplass, daiwb
 * @version 1.0
 */
public class StandardPlaceAssignment implements PlaceAssignmentAlgorithm {

    /**
     * <p>
     * Default constructor. Does nothing.
     * </p>
     */
    public StandardPlaceAssignment() {
    }

    /**
     * <p>
     * Determine the final placement of each entry in the initialPlacements array. This particular implementation skips
     * tied rankings (e.g., 1,2,2,3 becomes 1,2,2,4).
     * </p>
     *
     * @return an array of integers, each of which is the final placement (ranking) of the corresponding entry in the
     *         initialPlacements array.
     * @param initialPlacements
     *            an array of initial placement (ranking) numbers, from 1 to some maximum number (not necessarily in
     *            numeric order)
     * @throws IllegalArgumentException
     *             if initialPlacements is null or if any entry is not positive, or if there are any 'gaps' in the
     *             numbers in the array, or the minimum number is not 1
     */
    public int[] assignPlacements(int[] initialPlacements) {
        Util.checkNull(initialPlacements, "initialPlacements");

        // The length of the initialPlacements.
        int n = initialPlacements.length;

        // Return an empty int array if the input array is empty.
        if (n == 0) {
            return new int[0];
        }

        for (int i = 0; i < n; ++i) {
            if (initialPlacements[i] <= 0) {
                throw new IllegalArgumentException("initialPlacements should not contain non-positive element.");
            }
        }

        // Make a copy of the initialPlacements and sort it.
        int[] copy = (int[]) initialPlacements.clone();
        Arrays.sort(copy);

        if (copy[0] != 1) {
            throw new IllegalArgumentException("The highest rank should be 1.");
        }

        // Maps from input (1-based) to output.
        // The size is 'n+1' since the maximum number is n and the input ranks are 1-based.
        int mapping[] = new int[n + 1];

        // First place is always 1.
        mapping[1] = 1;

        // Skip the first one which is already mapped.
        for (int i = 1; i < n; ++i) {
            if (copy[i] - copy[i - 1] > 1) {
                throw new IllegalArgumentException("Gaps exist in the numbers of the initialPlacements");
            }
            if (copy[i] != copy[i - 1]) {
                // We have progressed a non-tied value, set it to the current index (1-based).
                mapping[copy[i]] = i + 1;
            }
        }

        // Remap inputs to their outputs
        int[] ret = new int[n];
        for (int i = 0; i < n; ++i) {
            ret[i] = mapping[initialPlacements[i]];
        }
        return ret;
    }
}
