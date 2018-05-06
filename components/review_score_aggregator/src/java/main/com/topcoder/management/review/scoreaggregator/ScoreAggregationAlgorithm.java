/*
 * Copyright (C) 2006-2014 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

/**
 * <p>
 * This interface defines a score aggregation algorithm, whereby scores from multiple reviewers are aggregated into a
 * single combined score. Different implementations may aggregate scores differently, e.g., arithmetic average, or by
 * throwing out the highest and lowest, and averaging the rest.
 * </p>
 *
 * <p>
 * Implementations of this interface should be implemented in a thread-safe manner.
 * </p>
 *
 * @author dplass, daiwb
 * @version 1.0.1
 */
public interface ScoreAggregationAlgorithm {
    /**
     * <p>
     * Calculate the aggregate score of the given scores, using an implementation-specific algorithm.
     * </p>
     *
     * @return the aggregated score
     * @param scores
     *            the scores that a submission received from each reviewer
     * @throws IllegalArgumentException
     *             if scores is null or if any entry in the array is negative/NaN/Infinite
     */
    public double calculateAggregateScore(double[] scores);
}
