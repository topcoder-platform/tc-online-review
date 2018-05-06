/*
 * Copyright (C) 2006-2014 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator.impl;

import com.topcoder.management.review.scoreaggregator.AggregatedSubmission;
import com.topcoder.management.review.scoreaggregator.InconsistentDataException;
import com.topcoder.management.review.scoreaggregator.TieBreaker;
import com.topcoder.management.review.scoreaggregator.Util;

/**
 * <p>
 * This class is the standard implementation of the TieBreaker interface. It implements the tie-breaking algorithm
 * described in the Requirements Specification, namely submissions with more 'wins' are ranked higher than other
 * submissions with the same tied score.
 * </p>
 *
 * <p>
 * This class is thread-safe, since it has no instance variables.
 * </p>
 *
 * @author dplass, daiwb
 * @version 1.0.1
 */
public class StandardTieBreaker implements TieBreaker {

    /**
     * <p>
     * Default constructor. Does nothing.
     * </p>
     */
    public StandardTieBreaker() {
    }

    /**
     * <p>
     * Determine the relative placement of the given submissions using a tie-breaking algorithm. The submissions are
     * assumed to already be 'tied' for some reason or another. This version implements the "standard" tie-brekaing
     * algorithm. Namely, for each unranked submission, the submission with the most "wins" will be ranked first. This
     * process is repeated until no more unranked submissions exist.
     * </p>
     *
     * @return an array of numbers representing the relative placements of the entries in the submissions array.
     * @param submissions
     *            the aggregated submissions currently 'tied'
     * @throws IllegalArgumentException
     *             if submissions is null, or if any entry in submissions is null
     * @throws InconsistentDataException
     *             if the entries in submissions have 'scores' arrays of different size
     */
    public int[] breakTies(AggregatedSubmission[] submissions) throws InconsistentDataException {
        Util.checkNull(submissions, "submissions");

        // The length of the submissions.
        int len = submissions.length;

        // Return an empty array if the input array is empty.
        if (len == 0) {
            return new int[0];
        }

        for (int i = 0; i < len; ++i) {
            Util.checkNull(submissions[i], "submission[" + i + "]");
            if (submissions[i].getScores().length != submissions[0].getScores().length) {
                throw new InconsistentDataException(
                        "Not all the entries in submissions have the same number of scores.");
            }
        }

        // Note : since the algorithm designer provided use sort every time, the time complexity is O(nm^2logm)
        // where n is the number of submissions and m is the number of reviewers.
        // I use some extra memory to record whether a submission has been assigned rank.
        // To get the winning submissions, I think a O(m) traverse is enough, there's no need to sort.
        // So the time complexity is O(nm^2) and space complexity is O(n) which is a improvement to the algorithm.

        // The rank of each submission.
        int[] rank = new int[len];

        // The number of scores for each submission.
        int n = submissions[0].getScores().length;

        // First initialize the nextrank to be 1.
        int nextrank = 1;

        // Representing whether a submission has been assigned a rank.
        boolean[] visit = new boolean[len];

        // The number of submission which has not been assigned a rank.
        // Initialized to the number of submissions.
        int left = submissions.length;

        // These array stores the the score of the i-th submission, j-th reviewer.
        // It is for fast access in the next step.
        double[][] scores = new double[len][n];
        for (int i = 0; i < len; ++i) {
            double[] ss = submissions[i].getScores();
            for (int j = 0; j < n; ++j) {
                scores[i][j] = ss[j];
            }
        }

        while (left > 0) {
            // The number of win's for each submission.
            // Initialized to be all 0.
            int[] wins = new int[len];

            // For each kind of score (e.g. from the same reviewer)
            // get the winning submissions (there may be several ones).
            for (int i = 0; i < n; ++i) {
                // Store the winning submissions.
                int[] win = new int[len];

                // The number of "EXACT" equal winning submissions.
                int num = 0;

                // The highest score for this specified reviewer score.
                double highest = -1;

                for (int j = 0; j < len; ++j) {
                    // Has been assigned a rank, skip.
                    if (visit[j]) {
                        continue;
                    }

                    // Score for specified submission (j) and reviewer (i).
                    double score = scores[j][i];
                    if (score > highest) {
                        highest = score;
                        num = 0;
                        win[num++] = j;
                    } else if (score == highest) {
                        // Here we use "==" to determine "EXACT" equal to winning submission.
                        win[num++] = j;
                    }
                }

                for (int j = 0; j < num; ++j) {
                    ++wins[win[j]];
                }
            }

            // Find the most number of win's for all the non-rank-assigned submissions.
            int mostwin = 0;
            for (int i = 0; i < len; ++i) {
                // Has been assigned a rank, skip.
                if (visit[i]) {
                    continue;
                }

                if (wins[i] > mostwin) {
                    mostwin = wins[i];
                }
            }

            // Assign rank to the winning submissions.
            for (int i = 0; i < len; ++i) {
                // Has been assigned a rank, skip.
                if (visit[i]) {
                    continue;
                }

                if (wins[i] == mostwin) {
                    rank[i] = nextrank;
                    --left;
                    visit[i] = true;
                }
            }

            // Increase the rank number.
            ++nextrank;
        }
        return rank;
    }
}
