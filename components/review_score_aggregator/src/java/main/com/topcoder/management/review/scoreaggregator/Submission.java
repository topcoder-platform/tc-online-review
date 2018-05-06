/*
 * Copyright (C) 2006-2014 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

/**
 * <p>
 * This class represents a component submission and currently only stores the scores received from each of the reviewers
 * as doubles.
 * <p>
 *
 * <p>
 * This class is thread-safe since it is immutable.
 * </p>
 *
 * @author dplass, daiwb
 * @version 1.0.1
 */
public class Submission {

    /**
     * <p>
     * A unique numeric identifier for this submission. Set in the constructor and retrieved by getId. Will always be
     * positive.
     * </p>
     */
    private final long id;

    /**
     * <p>
     * The scores received from each reviewer of this submission. Set in the constructor and retrieved by getScores.
     * Will never be null, and no entry will ever be negative/NaN/Infinite.
     * </p>
     */
    private final double[] scores;

    /**
     * <p>
     * Construct this Submission with the given id and review scores.
     * </p>
     *
     * @param id
     *            a unique numeric identifier for this submission
     * @param scores
     *            the scores that this submission received from each reviewer
     * @throws IllegalArgumentException
     *             if id is not positive, or if scores is null, or if any entry is negative/NaN/Infinite
     */
    public Submission(long id, double[] scores) {
        if (id <= 0) {
            throw new IllegalArgumentException("id should be positive.");
        }
        Util.checkNull(scores, "scores");
        for (int i = 0; i < scores.length; ++i) {
            Util.checkDoubleValue(scores[i], "scores[" + i + "]");
        }

        this.id = id;
        this.scores = scores.clone();
    }

    /**
     * <p>
     * Returns a clone of the scores array as set in the constructor from the <code>scores</code> field.
     * </p>
     *
     * @return a clone of the scores array as set in the constructor
     */
    public double[] getScores() {
        return (double[]) scores.clone();
    }

    /**
     * <p>
     * Return the id as set in the constructor, from the <code>id</code> field.
     * </p>
     *
     * @return the unique identifier as set in the constructor
     */
    public long getId() {
        return id;
    }
}
