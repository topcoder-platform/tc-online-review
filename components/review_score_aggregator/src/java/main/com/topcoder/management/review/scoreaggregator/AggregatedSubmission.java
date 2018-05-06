/*
 * Copyright (C) 2006-2014 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

/**
 * <p>
 * Represents a submission whose scores have been aggregated. This class implements Comparable so it can be used to sort
 * submissions by their aggregated scores.
 * </p>
 *
 * <p>
 * This class is thread-safe since it is immutable.
 * </p>
 *
 * @author dplass, daiwb
 * @version 1.0.1
 */
public class AggregatedSubmission extends Submission implements Comparable {

    /**
     * <p>
     * The aggregated score of this submission. It is set in the constructor and retrieved by the getAggregatedScore
     * method. Will never be negative/NaN/Infinite.
     * </p>
     */
    private final double aggregatedScore;

    /**
     * <p>
     * Construct this object using the given id and submission scores and aggregated score.
     * </p>
     *
     * @param id
     *            a unique numeric identifier for this submission
     * @param scores
     *            the reviewed scores of the submission that this aggregated submission is based on
     * @param score
     *            the aggregated score of this submission
     * @throws IllegalArgumentException
     *             if id is not positive, or if scores is null, or if any entry in scores is negative/Nan/Infinite
     */
    public AggregatedSubmission(long id, double[] scores, double score) {
        super(id, scores);

        Util.checkDoubleValue(score, "score");

        aggregatedScore = score;
    }

    /**
     * <p>
     * Construct this object using the given submission and aggregated score.
     * </p>
     *
     * @param sub
     *            the Submission that this aggregated submission is based on
     * @param score
     *            the aggregated score of this submission
     * @throws IllegalArgumentException
     *             if sub is null, or if score is negative/NaN/Infinite
     */
    public AggregatedSubmission(Submission sub, double score) {
        super(Util.getId(sub), sub.getScores());

        Util.checkDoubleValue(score, "score");

        aggregatedScore = score;
    }

    /**
     * <p>
     * Returns the aggregated score as set in the constructor, from the <code>aggregatedScore</code> field.
     * </p>
     *
     * @return Returns the aggregated score as set in the constructor
     */
    public double getAggregatedScore() {
        return aggregatedScore;
    }

    /**
     * <p>
     * Implementation of the Comparable interface. Compare this object to the parameter, as an AggregatedSubmission
     * object, by comparing their aggregated scores.
     * </p>
     *
     * @return -1 if this.aggregatedScore is less than obj.aggregatedScore, 0 if they are equal, and 1 if
     *         this.aggregatedScore is greater than obj.aggregatedScore
     * @param obj
     *            the other AggregatedSubmission to compare with
     * @throws IllegalArgumentException
     *             if obj is null
     * @throws ClassCastException
     *             if obj is not of AggregatedSubmission type
     */
    public int compareTo(Object obj) {
        Util.checkNull(obj, "obj");

        if (!(obj instanceof AggregatedSubmission)) {
            throw new ClassCastException("obj is not of AggregatedSubmission type.");
        }

        AggregatedSubmission sub = (AggregatedSubmission) obj;
        if (aggregatedScore < sub.aggregatedScore) {
            return -1;
        } else if (aggregatedScore > sub.aggregatedScore) {
            return 1;
        } else {
            return 0;
        }
    }
}
