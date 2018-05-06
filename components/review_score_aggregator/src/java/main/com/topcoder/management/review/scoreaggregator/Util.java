/*
 * Copyright (C) 2006-2014 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

/**
 * <p>
 * Helper class for the Status Tracker Component.
 * </p>
 *
 * <p>
 * Note, since this is a multi-package component, this helper class is defined as public instead of package-private to
 * avoid defining such class in every package. This is allowed by the TopCoder rule. All the static methods are shared
 * between multiple packages are defined as public, otherwise will be defined as package-private.
 * </p>
 *
 * @author daiwb
 * @version 1.0.1
 */
public final class Util {
    /**
     * <p>
     * This private constructor prevents to create a new instance.
     * </p>
     */
    private Util() {
    }

    /**
     * <p>
     * Checks whether the given Object is null.
     * </p>
     *
     * @param arg
     *            the argument to check
     * @param name
     *            the name of the argument to check
     *
     * @throws IllegalArgumentException
     *             if the given Object is null
     */
    public static void checkNull(Object arg, String name) {
        if (arg == null) {
            throw new IllegalArgumentException(name + " should not be null.");
        }
    }

    /**
     * <p>
     * Checks whether the given String is null or empty. Here empty means the length of the given string is zero after
     * trimmed.
     * </p>
     *
     * @param arg
     *            the String to check
     * @param name
     *            the name of the String argument to check
     *
     * @throws IllegalArgumentException
     *             if the given string is null or empty
     */
    public static void checkString(String arg, String name) {
        checkNull(arg, name);

        if (arg.trim().length() == 0) {
            throw new IllegalArgumentException(name + " should not be empty String.");
        }
    }

    /**
     * <p>
     * Checks whether the given double value is valid.
     * </p>
     * <p>
     * If score is negative/NaN/Infinite, IllegalArgumentException will be thrown.
     * </p>
     *
     * @param score
     *            The double value to check
     * @param name
     *            The name of the double value
     * @throws IllegalArgumentException
     *             if score is negative/Nan/Infinite
     */
    public static void checkDoubleValue(double score, String name) {
        if (Double.isNaN(score)) {
            throw new IllegalArgumentException(name + " should not be NaN.");
        }
        if (Double.isInfinite(score)) {
            throw new IllegalArgumentException(name + " should not be Infinite.");
        }
        if (score < 0) {
            throw new IllegalArgumentException(name + " should not be negative.");
        }
    }

    /**
     * <p>
     * Helper method to return the id of the given Submission.
     * </p>
     *
     * @param sub
     *            the submission to get id from
     * @return the id of the submission
     * @throws IllegalArgumentException
     *             if sub is null
     */
    public static long getId(Submission sub) {
        if (sub == null) {
            throw new IllegalArgumentException("sub should not be null.");
        }
        return sub.getId();
    }

    /**
     * <p>
     * Returns the aggregatedScore of the given AggregatedSubmission.
     * </p>
     *
     * @param sub
     *            the submission to get aggregatedScore from
     * @return the aggregatedScore of the submission
     * @throws IllegalArgumentException
     *             if sub is null
     */
    public static double getAggregatedScore(AggregatedSubmission sub) {
        if (sub == null) {
            throw new IllegalArgumentException("sub should not be null.");
        }
        return sub.getAggregatedScore();
    }
}