/*
 * Copyright (C) 2006-2014 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scoreaggregator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.topcoder.management.review.scoreaggregator.impl.AveragingAggregationAlgorithm;
import com.topcoder.management.review.scoreaggregator.impl.StandardPlaceAssignment;
import com.topcoder.management.review.scoreaggregator.impl.StandardTieBreaker;
import com.topcoder.management.review.scoreaggregator.impl.StandardTieDetector;
import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.impl.ConfigManagerSpecificationFactory;
import com.topcoder.util.objectfactory.impl.IllegalReferenceException;
import com.topcoder.util.objectfactory.impl.SpecificationConfigurationException;

/**
 * <p>
 * This is the main class of the Review Score Aggregator component. It provides pluggable implementations of the
 * following four features: 1. Score aggregation (currently just averages) 2. Tie detector (using an epsilon) 3. Tie
 * breaker (using "most wins" among tied submissions) 4. Final place assignment (e.g., to skip tied indicies)
 * </p>
 *
 * <p>
 * There are two main entry points in this class: aggregateScores, which calculates aggregated scores for a set of
 * submissions, and calcPlacements, which takes aggregated submissions and determines their final relative ranking.
 * </p>
 *
 * <p>
 * This class is thread-safe.
 * </p>
 *
 * @author dplass, daiwb
 * @version 1.0.1
 */
public class ReviewScoreAggregator {
    /**
     * <p>
     * This is the default namespace that external clients can use when configuring this component.
     * </p>
     */
    public static final String DEFAULT_NAMESPACE = "com.topcoder.management.review.scoreaggregator";

    /**
     * <p>
     * The current score aggregation algorithm. It is set in one of the constructors, or by the
     * setScoreAggregationAlgorithm method, and accessed by the getScoreAggregationAlgorithm method. It is used by the
     * aggregateScores methods, and will never be null.
     * </p>
     */
    private ScoreAggregationAlgorithm aggregationAlgorithm;

    /**
     * <p>
     * The current tie detection algorithm. It is set in one of the constructors, or by the setTieDetector method, and
     * accessed by the getTieDetector method. It is used by the calcPlacements methods, and will never be null.
     * </p>
     */
    private TieDetector tieDetector;

    /**
     * <p>
     * The current tie breaker algorithm. It is set in one of the constructors, or by the setTieBreaker method, and
     * accessed by the getTieBreaker method. It is used by the calcPlacements methods, and will never be null.
     * </p>
     */
    private TieBreaker tieBreaker;

    /**
     * <p>
     * The current place assignment algorithm. It is set in one of the constructors, or by the
     * setPlaceAssignmentAlgorithm method, and accessed by the getPlaceAssignmentAlgorithm method. It is used by the
     * calcPlacements methods, and will never be null.
     * </p>
     */
    private PlaceAssignmentAlgorithm placeAssignmentAlgorithm;

    /**
     * <p>
     * Construct this object with the given parameters.
     * </p>
     *
     * @param aggregator
     *            the score aggregation algorithm to use in aggregateScores
     * @param tieDetector
     *            the tie detection algorithm to use in calcPlacements
     * @param tieBreaker
     *            the tie breaker algorithm to use in calcPlacements
     * @param placeAssignmentAlgorithm
     *            the place assignment algorithm to use in calcPlacements
     * @throws IllegalArgumentException
     *             if any parameter is null
     */
    public ReviewScoreAggregator(ScoreAggregationAlgorithm aggregator, TieDetector tieDetector, TieBreaker tieBreaker,
            PlaceAssignmentAlgorithm placeAssignmentAlgorithm) {
        setScoreAggregationAlgorithm(aggregator);
        setTieDetector(tieDetector);
        setTieBreaker(tieBreaker);
        setPlaceAssignmentAlgorithm(placeAssignmentAlgorithm);
    }

    /**
     * <p>
     * Construct this object using an Objectfactory and the given ConfigurationManager namespace. The four algorithm
     * objects are instantiated. If any of them cannot be instantiated, an exception is thrown.
     * </p>
     *
     * @param namespace
     *            the ConfigManager namespace that contains the ObjectFactory settings for the four algorithm objects.
     * @throws IllegalArgumentException
     *             if namespace is null or empty after trim.
     * @throws ReviewScoreAggregatorConfigException
     *             if any of the four required algorithm objects cannot be instantiated or the configuration is invalid
     */
    public ReviewScoreAggregator(String namespace) throws ReviewScoreAggregatorConfigException {
        Util.checkString(namespace, "namespace");

        ObjectFactory factory;
        try {
            factory = new ObjectFactory(new ConfigManagerSpecificationFactory(namespace));
            aggregationAlgorithm = (ScoreAggregationAlgorithm) factory.createObject("ScoreAggregationAlgorithm",
                    "default");
            tieDetector = (TieDetector) factory.createObject("TieDetector", "default");
            tieBreaker = (TieBreaker) factory.createObject("TieBreaker", "default");
            placeAssignmentAlgorithm = (PlaceAssignmentAlgorithm) factory.createObject("PlaceAssignmentAlgorithm",
                    "default");
        } catch (SpecificationConfigurationException sce) {
            throw new ReviewScoreAggregatorConfigException(
                    "Any of the four required algorithm objects cannot be instantiated.", sce);
        } catch (IllegalReferenceException ire) {
            throw new ReviewScoreAggregatorConfigException(
                    "Any of the four required algorithm objects cannot be instantiated.", ire);
        } catch (InvalidClassSpecificationException icse) {
            throw new ReviewScoreAggregatorConfigException(
                    "Any of the four required algorithm objects cannot be instantiated.", icse);
        } catch (ClassCastException cce) {
            throw new ReviewScoreAggregatorConfigException(
                    "Any of the four required algorithm objects cannot be instantiated.", cce);
        }
    }

    /**
     * <p>
     * Construct this object with all the standard algorithm implementations.
     * </p>
     */
    public ReviewScoreAggregator() {
        aggregationAlgorithm = new AveragingAggregationAlgorithm();
        tieDetector = new StandardTieDetector();
        tieBreaker = new StandardTieBreaker();
        placeAssignmentAlgorithm = new StandardPlaceAssignment();
    }

    /**
     * <p>
     * Calculate the aggregated scores for each of the submission scores. Then construct and return a
     * AggregatedSubmission for each one.
     * </p>
     *
     * @return an array of ScoredSubmission objects, one per row in the 'scores' array. There has to be a
     *         direct correspondance to the original array (in other words, the order must be the same).
     * @param scores
     *            the scores of the submissions for which we need aggreated scores
     * @throws InconsistentDataException
     *             if not all the entries in 'scores' have the same number of entries.
     * @throws IllegalArgumentException
     *             if scores is null, or if any entry in scores is null, or if any entry in scores is
     *             negative/NaN/Infinite
     */
    public synchronized AggregatedSubmission[] aggregateScores(double[][] scores) throws InconsistentDataException {
        Util.checkNull(scores, "scores");
        for (int i = 0; i < scores.length; ++i) {
            Util.checkNull(scores[i], "scores[" + i + "]");
            if (scores[i].length != scores[0].length) {
                throw new InconsistentDataException("Not all the entries in scores have the same number of entries.");
            }
            for (int j = 0; j < scores[i].length; ++j) {
                Util.checkDoubleValue(scores[i][j], "scores[" + i + "][" + j + "]");
            }
        }

        AggregatedSubmission[] subs = new AggregatedSubmission[scores.length];
        for (int i = 0; i < scores.length; ++i) {
            // Use the index into the array as the id.
            // Since the id of AggregatedSubmission is required to be positive, we should use the 1-based index.
            subs[i] = new AggregatedSubmission(i + 1, scores[i], aggregationAlgorithm
                    .calculateAggregateScore(scores[i]));
        }
        return subs;
    }

    /**
     * <p>
     * Calculate the aggregated scores for each of the submissions and construct and return a ScoredSubmission for each
     * one.
     * </p>
     *
     * @param submissions
     *            The Submission array to get AggregatedSubmission array from
     * @return an array of ScoredSubmission objects, one per entry in the 'subs' array. There has to be a
     *         direct correspondance to the original array (in other words, the order must be the same).
     * @throws InconsistentDataException
     *             if not all the entries in 'subs' have the same number of scores.
     * @throws IllegalArgumentException
     *             if subs is null, or if any entry in subs is null
     */
    public synchronized AggregatedSubmission[] aggregateScores(Submission[] submissions)
        throws InconsistentDataException {
        Util.checkNull(submissions, "submissions");
        for (int i = 0; i < submissions.length; ++i) {
            Util.checkNull(submissions[i], "submissions[" + i + "]");
            if (submissions[i].getScores().length != submissions[0].getScores().length) {
                throw new InconsistentDataException(
                        "Not all the entries in submissions have the same number of scores.");
            }
        }

        AggregatedSubmission[] subs = new AggregatedSubmission[submissions.length];
        for (int i = 0; i < submissions.length; ++i) {
            subs[i] = new AggregatedSubmission(submissions[i], aggregationAlgorithm
                    .calculateAggregateScore(submissions[i].getScores()));
        }
        return subs;
    }

    /**
     * <p>
     * Calculate the relative placements of the reviewed submissions with the given scores. The first index in the array
     * represents the submission number, and the second index represents the reviewer. The current
     * ScoreAggregationAlgorithm is used to aggregate the scores, then they are ranked, ties are detected using the
     * current TieDetector, then ties are broken by the current TieBreaker, and finally the placements are calculated by
     * the current PlaceAssignmentAlgorithm.
     * </p>
     *
     * @return an array of RankedSubmission objects, one per row in the original scores array. The Id of each ranked
     *         submission will be the index in the original array.
     * @param scores
     *            the scores of the submissions who we are ranking
     * @throws InconsistentDataException
     *             if not all the entries in 'scores' have the same number of entries.
     * @throws IllegalArgumentException
     *             if scores is null, or if any entry in scores is null, or if any entry in scores is
     *             negative/NaN/Infinite
     */
    public synchronized RankedSubmission[] calcPlacements(double[][] scores) throws InconsistentDataException {
        return calcPlacements(aggregateScores(scores));
    }

    /**
     * <p>
     * Calculate the relative placements of the given reviewed submissions. First, the submissions are sorted by
     * aggregated score, then ties are detected using the current TieDetector. Tied submissions are broken by the
     * current TieBreaker, and finally the placements are calculated by the current PlaceAssignmentAlgorithm.
     * </p>
     *
     * <p>
     * Note that the entries in the returned array will NOT necessarily correspond to the *entries* in the input array.
     * </p>
     *
     * @param submissions
     *            The AggregatedSubmission array to get RankedSubmission array from
     * @return an array of RankedSubmission objects, one per entry in the original submission array.
     * @throws InconsistentDataException
     *             if not all the entries in 'subs' have the same number of scores
     * @throws IllegalArgumentException
     *             if submissions is null, or if any entry in submissions is null
     */
    public synchronized RankedSubmission[] calcPlacements(AggregatedSubmission[] submissions)
        throws InconsistentDataException {
        Util.checkNull(submissions, "submissions");
        for (int i = 0; i < submissions.length; ++i) {
            Util.checkNull(submissions[i], "submissions[" + i + "]");
            if (submissions[i].getScores().length != submissions[0].getScores().length) {
                throw new InconsistentDataException("Not all entries in subs have the same number of scores.");
            }
        }

        // Return an empty array if the input array is empty.
        if (submissions.length == 0) {
            return new RankedSubmission[0];
        }

        // Make a copy of the original array.
        // The next manipulations will be on this one.
        AggregatedSubmission[] copy = (AggregatedSubmission[]) submissions.clone();

        // Sort and reverse the array.
        Arrays.sort(copy);
        for (int i = 0; i < copy.length / 2; ++i) {
            AggregatedSubmission tmp = copy[i];
            copy[i] = copy[copy.length - 1 - i];
            copy[copy.length - 1 - i] = tmp;
        }

        // Initial placements array (values are 1-based).
        int initialPlacements[] = new int[copy.length];

        // Current placement (starts from 1).
        int currentPlacement = 1;
        initialPlacements[0] = 1;

        // Index of the first submission that was tied.
        int firstTied = 0;

        // The current list of tied submission.
        List tied = new ArrayList();
        tied.add(copy[0]);

        // Note the end condition is "i <= copy.length" not "i < copy.length".
        // Since in the algorithm the designer provided, some tied submissions will be left un-processing.
        for (int i = 1; i <= copy.length; ++i) {
            if (i != copy.length && tieDetector.tied(copy[i].getAggregatedScore(), copy[i - 1].getAggregatedScore())) {
                // Tied with the previous one. Add to the list.
                tied.add(copy[i]);
            } else {
                if (tied.size() > 1) {
                    // Process the tied submissions.

                    // Break ties among these submissions.
                    // The array returned will have values starting at 1, which we will then add to each record.
                    AggregatedSubmission[] subs = new AggregatedSubmission[tied.size()];
                    for (int t = 0; t < tied.size(); ++t) {
                        subs[t] = (AggregatedSubmission) tied.get(t);
                    }
                    int relativePlacements[] = tieBreaker.breakTies(subs);

                    for (int j = 0; j < relativePlacements.length; ++j) {
                        // Update all entries starting from where we "first tied"
                        // (note, subtract 1 because both are 1-based).
                        initialPlacements[j + firstTied] = currentPlacement + relativePlacements[j] - 1;
                    }

                    // Set the currentPlacement value to the maximum, so we continue from the right number.
                    for (int j = firstTied; j < i; ++j) {
                        currentPlacement = Math.max(currentPlacement, initialPlacements[j]);
                    }
                }

                // Clear the tied.
                tied.clear();

                // Assign the next placement value to this (note, it may get overwritten in another iteration).
                if (i != copy.length) {
                    // Increase currentPlacement first before assignment.
                    initialPlacements[i] = ++currentPlacement;

                    // Remember this location and submission in case subsequent entries are also tied with this one.
                    firstTied = i;
                    tied.add(copy[i]);
                }
            }
        }

        // Convert initial placements into final placements.
        int[] finalPlacements = placeAssignmentAlgorithm.assignPlacements(initialPlacements);

        // Build rankerSubmission objects from the AggregatedSubmissions.
        RankedSubmission[] ret = new RankedSubmission[copy.length];
        for (int i = 0; i < copy.length; ++i) {
            ret[i] = new RankedSubmission(copy[i], finalPlacements[i]);
        }

        return ret;
    }

    /**
     * <p>
     * Returns the current score aggregation algorithm.
     * </p>
     *
     * @return Returns the current score aggregation algorithm.
     */
    public synchronized ScoreAggregationAlgorithm getScoreAggregationAlgorithm() {
        return aggregationAlgorithm;
    }

    /**
     * <p>
     * Change the current score aggregation algorithm to the given value.
     * </p>
     *
     * @param aggregationAlgorithm
     *            The new score aggregation algorithm.
     * @throws IllegalArgumentException
     *             if aggregationAlgorithm is null.
     */
    public synchronized void setScoreAggregationAlgorithm(ScoreAggregationAlgorithm aggregationAlgorithm) {
        Util.checkNull(aggregationAlgorithm, "aggregationAlgorithm");
        this.aggregationAlgorithm = aggregationAlgorithm;
    }

    /**
     * <p>
     * Returns the current place assignment algorithm.
     * </p>
     *
     * @return Returns the current place assignment algorithm
     */
    public synchronized PlaceAssignmentAlgorithm getPlaceAssignmentAlgorithm() {
        return placeAssignmentAlgorithm;
    }

    /**
     * Change the current placement assignment algorithm to the given value.
     *
     * @param placeAssignmentAlgorithm
     *            The new place assignment algorithm to use
     * @throws IllegalArgumentException
     *             if placeAssignmentAlgorithm is null
     */
    public synchronized void setPlaceAssignmentAlgorithm(PlaceAssignmentAlgorithm placeAssignmentAlgorithm) {
        Util.checkNull(placeAssignmentAlgorithm, "placeAssignmentAlgorithm");
        this.placeAssignmentAlgorithm = placeAssignmentAlgorithm;
    }

    /**
     * <code>
     * Returns the current tie breaker algorithm.
     * </code>
     *
     * @return Returns the current tie breaker algorithm.
     */
    public synchronized TieBreaker getTieBreaker() {
        return tieBreaker;
    }

    /**
     * <p>
     * Change the current tie breaker algorithm to the given value.
     * </p>
     *
     * @param tieBreaker
     *            The new tie breaker algorithm to use
     * @throws IllegalArgumentException
     *             If tieBreaker is null
     */
    public synchronized void setTieBreaker(TieBreaker tieBreaker) {
        Util.checkNull(tieBreaker, "tieBreaker");
        this.tieBreaker = tieBreaker;
    }

    /**
     * <p>
     * Returns the current tie detector algorithm.
     * </p>
     *
     * @return Returns the current tie detector algorithm.
     */
    public synchronized TieDetector getTieDetector() {
        return tieDetector;
    }

    /**
     * <p>
     * Change the current tie detector algorithm to the given value.
     * </p>
     *
     * @param tieDetector
     *            The new tie detector algorithm to use
     * @throws IllegalArgumentException
     *             If tieDetector is null
     */
    public synchronized void setTieDetector(TieDetector tieDetector) {
        Util.checkNull(tieDetector, "tieDetector");
        this.tieDetector = tieDetector;
    }
}
