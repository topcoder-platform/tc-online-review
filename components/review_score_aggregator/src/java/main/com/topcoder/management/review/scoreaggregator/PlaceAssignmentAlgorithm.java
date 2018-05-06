/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

/**
 * <p>
 * This interface defines an algorithm for assigning the final place ranking for a set of submissions based on their
 * initial placements. An implementation of this interface is used by the ReviewScoreAggregator class when calculating
 * final rankings of submissions after aggregation, sorting by score, and tie-breaking are applied.
 * </p>
 * <p>
 * Implementations of this interface can take the given array and apply various transformations on it to produce the
 * final placements. For example, if the initial array is 1,2,2,3, one implementation might assign their final places to
 * be 1,2,2,4. Another implementation might assign their final places as 1,3,3,4.
 * </p>
 * <p>
 * Implementations of this interface should be implemented in a thread-safe manner.
 * </p>
 *
 * @author dplass, daiwb
 * @version 1.0
 */
public interface PlaceAssignmentAlgorithm {
    /**
     * <p>
     * Determine the final placement ranking for each of the rankings in the given array. The input array will not
     * necessarily be in numeric order, but its minimum value must be 1, and every integer between 1 and the maximum
     * value in the array must be represented in the array.
     * </p>
     * <p>
     * The output array should represent the final rankings of the corresponding initial rankings. For example, if the
     * initial array is 1,2,2,3, one implementation might assign their final places to be 1,2,2,4.
     * </p>
     *
     * @param initialPlacements
     *            an array of initial placement (ranking) numbers, from 1 to some maximum number.
     * @return an array of integers, each of which is the final placement (ranking) of the corresponding entry in the
     *         initialPlacements array.
     * @throws IllegalArgumentException
     *             if initialPlacements is null or if any entry is not positive, or if there are any 'gaps' in the
     *             numbers in the array, or if the minimum value in the array is not 1.
     */
    public int[] assignPlacements(int[] initialPlacements);
}
