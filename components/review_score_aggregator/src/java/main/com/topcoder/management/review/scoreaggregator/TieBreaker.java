/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

/**
 * <p>
 * This interface determines the relative placement of a set of Submissions whose aggregated scores have already been
 * determined to be 'tied'. An implementation of this interface is used by the ReviewScoreAggregator class to break ties
 * between tied submissions. Implementations may use various methods to break ties (including not breaking ties at all).
 * </p>
 *
 * <p>
 * Implementations of this interface should be implemented in a thread-safe manner.
 * </p>
 *
 * @author dplass, daiwb
 * @version 1.0
 */
public interface TieBreaker {
    /**
     * <p>
     * Determine the relative placement of the given submissions using a tie-breaking algorithm. The submissions are
     * assumed to already be tied for some reason or another. The returned array must correspond to the given
     * submissions in size and semantics. That is, the 0th element of the returned array corresponds to the relative
     * placement of submissions[0], and so on.
     * </p>
     * <p>
     * The relative placements must start from 1. When looking at the entire array of returned numbers, there must not
     * be any 'gaps' in the numbers. That is, each integer between 1 (inclusive) and the maximum value in the array
     * (inclusive) must be represented in the array at least once. It is acceptable for all the entries in the array to
     * be identical (and therefore equal to 1).
     * </p>
     * <p>
     * Implementations of this interface should be implemented in a thread-safe manner.
     * </p>
     *
     * @return an array of numbers representing the relative placements of the entries in the submissions array.
     * @param submissions
     *            the aggregated submissions who are currently 'tied'.
     * @throws IllegalArgumentException
     *             if submissions is null, or if any entry in submissions is null
     * @throws InconsistentDataException
     *             if the entries in submissions have 'scores' arrays of different 'scores'
     */
    public int[] breakTies(AggregatedSubmission[] submissions) throws InconsistentDataException;
}
