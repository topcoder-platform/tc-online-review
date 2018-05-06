/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

/**
 * <p>
 * Represents a submission that has been assigned an aggregated score, and a relative rank to other submissions in this
 * component contest.
 * </p>
 *
 * <p>
 * This class is immutable and therefore thread-safe.
 * </p>
 *
 * @author dplass, daiwb
 * @version 1.0
 */
public class RankedSubmission extends AggregatedSubmission {

    /**
     * <p>
     * The rank assigned to this submission. It is set in the constructor and accessed by getRank. Should always be
     * positive.
     * </p>
     */
    private final int rank;

    /**
     * <p>
     * Construct this object using the scored submission and relative rank.
     * </p>
     *
     * @param sub
     *            the scored submission that this ranked submission is based on
     * @param rank
     *            the relative rank of this submission, where 1 is the best
     * @throws IllegalArgumentException
     *             if sub is null or if rank is not positive.
     */
    public RankedSubmission(AggregatedSubmission sub, int rank) {
        super(sub, Util.getAggregatedScore(sub));
        if (rank <= 0) {
            throw new IllegalArgumentException("rank should be positive.");
        }
        this.rank = rank;
    }

    /**
     * Returns the rank as set in the constructor from the <code>rank</code> field.
     *
     * @return Returns the rank as set in the constructor
     */
    public int getRank() {
        return rank;
    }
}
