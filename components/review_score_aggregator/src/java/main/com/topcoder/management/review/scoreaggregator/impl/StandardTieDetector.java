/*
 * Copyright (C) 2006-2014 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.impl;

import com.topcoder.management.review.scoreaggregator.TieDetector;
import com.topcoder.management.review.scoreaggregator.Util;

/**
 * <p>
 * Standard implementation of the TieDetector interface, which uses an epsilon to detect relative or absolute
 * differences between two scores.
 * </p>
 * <p>
 * This class is thread-safe since it is immutable.
 * </p>
 *
 * @author dplass, daiwb
 * @version 1.0.1
 */
public class StandardTieDetector implements TieDetector {

    /**
     * <p>
     * The epsilon used for relative or absolute comparisons. Set in the constructor and used by the <code>tied</code>
     * method. Will never be negative.
     * </p>
     */
    private final double epsilon;

    /**
     * <p>
     * Construct this object with the given epsilon.
     * </p>
     *
     * @param epsilon
     *            the epsilon to use in the 'tied' method
     * @throws IllegalArgumentException
     *             if epsilon is negative/NaN (POSITIVE_INFINITY is valid)
     */
    public StandardTieDetector(double epsilon) {
        // Note that the epsilon could be POSITIVE_INFINITY here, which means the user want all
        // submissions to be tied.
        if (Double.isNaN(epsilon)) {
            throw new IllegalArgumentException("epsilon should not be NaN.");
        }
        if (epsilon < 0) {
            throw new IllegalArgumentException("epsilon should not be negative.");
        }

        this.epsilon = epsilon;
    }

    /**
     * <p>
     * Construct this object with an epsilon of 0 (i.e., exact match).
     * </p>
     */
    public StandardTieDetector() {
        this(0);
    }

    /**
     * <p>
     * Use the <code>epsilon</code> as set in the constructor to detect if a and b are 'tied' using either relative or
     * absolute difference.
     * </p>
     *
     * @return true if a and b are within epsilon, either absolutely or relatively of each other.
     * @param a
     *            the first score to test
     * @param b
     *            the second score to test
     * @throws IllegalArgumentException
     *             if a or b is negative/NaN/Infinite
     */
    public boolean tied(double a, double b) {
        Util.checkDoubleValue(a, "a");
        Util.checkDoubleValue(b, "b");

        // "<=" should be used here.
        if (Math.abs(a - b) <= epsilon) {
            return true;
        }

        // "<=" and ">=" should be used here.
        if (a >= b * (1 - epsilon) && a <= b * (1 + epsilon)) {
            return true;
        }
        if (b >= a * (1 - epsilon) && b <= a * (1 + epsilon)) {
            return true;
        }

        return false;
    }
}
