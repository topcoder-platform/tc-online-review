/*
 * Copyright (C) 2006-2014 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

/**
 * <p>
 * This interface defines the "tie detector" contract. Two scores, represented as doubles, are tested to determine if
 * they are in fact tied. An implementation of this class is used by the ReviewScoreAggregator class when determining if
 * two or more submissions are tied (and need to have their ranking tie broken by a TieBreaker implementation.)
 * </p>
 *
 * <p>
 * Implementations of this interface should be implemented in a thread-safe manner.
 * </p>
 *
 * @author dplass, daiwb
 * @version 1.0.1
 */
public interface TieDetector {
    /**
     * Determine if the two scores are tied. Different implementations may have different definitions of what 'tied'
     * means.
     *
     * @return true if a and b are considered 'tied', false else.
     * @param a
     *            the first score
     * @param b
     *            the second score
     * @throws IllegalArgumentException
     *             if a or b is negative/NaN/Infinite
     */
    public boolean tied(double a, double b);
}
