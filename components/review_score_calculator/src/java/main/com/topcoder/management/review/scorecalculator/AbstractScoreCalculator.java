/*
 * Copyright (C) 2006-2014 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import com.topcoder.management.review.data.Item;
import com.topcoder.management.scorecard.data.Question;

/**
 * <p>
 * Provides a simple template for the evaluation of review items.
 * </p>
 *
 * <p>
 * This class replaces the evaluateItem method by a simpler evaluateAnswer method which introduces an additional
 * limitation in that all answers must be String instances.
 * </p>
 *
 * <p>
 * This class also ensures appropriate weighting of the score.
 * </p>
 *
 * <p>
 * <b>Thread Safety</b>: All implementations of this base class are expected to be thread safe.
 * </p>
 *
 * @author      nicka81, UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0.4
 */
public abstract class AbstractScoreCalculator implements ScoreCalculator {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors

    /**
     * Creates a new AbstractScoreCalculator.
     */
    protected AbstractScoreCalculator() {
        // Do nothing.
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ScoreCalculator Interface Methods

    /**
     * <p>
     * Returns a score in the range [0, 1] for the given review item and scorecard question.
     * </p>
     *
     * <p>
     * This method actually relies on the evaluateAnswer method, which will return a value between 0 and 1,
     * inclusive, to get this method's result.
     * </p>
     *
     * @param   item
     *          The review item being evaluated.
     * @param   question
     *          The scorecard question for the item being evaluated.
     *
     * @return  The item's score which is in the range [0, 1].
     *
     * @throws  IllegalArgumentException
     *          The item or question is a null reference.
     * @throws  ScoreCalculatorException
     *          The item's question does not match the question's id, the question id was not set yet, the item's
     *          answer is not a String, the item's instance is an empty string (after trimming), or the answer
     *          could not be evaluted by the implementation's evaluateAnswer method.
     */
    public double evaluateItem(Item item, Question question) throws ScoreCalculatorException {
        // Check parameters against null.
        Util.checkNotNull(item, "item");
        Util.checkNotNull(question, "question");

        // Check that item's question and question match.
        try {
            if (item.getQuestion() != question.getId()) {
                throw new ScoreCalculatorException(
                        "The item's question id ("
                            + item.getQuestion()
                            + ") does not match the question's id ("
                            + question.getId()
                            + ")",
                        item,
                        question);
            }
        } catch (IllegalStateException ex) {
            throw new ScoreCalculatorException("The question's id was not set yet.", ex, item, question);
        }

        // Check that item's answer ia a String.
        if (!(item.getAnswer() instanceof String)) {
            throw new ScoreCalculatorException(
                    "The item's answer must be a String instance.", item, question);
        }

        // Check that item's answer is not empty (no need to check null as instanceof takes care of it).
        String answer = (String) item.getAnswer();

        if (answer.trim().length() == 0) {
            throw new ScoreCalculatorException(
                    "The item's answer must not be a blank or empty String instance.", item, question);
        }

        // Delegate to the abstract method to calculate a score between 0 and 1, and return that result.
        try {
            return evaluateAnswer(answer);
        } catch (IllegalArgumentException ex) {
            throw new ScoreCalculatorException(
                    "The answer was null or empty, though this should never happen.", ex, item, question);
        } catch (ScoreCalculatorException ex) {
            throw new ScoreCalculatorException(ex.getMessage(), ex.getCause(), item, question);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Abstract Methods

    /**
     * <p>
     * Returns a score between 0 and 1, inclusive, for the given answer to a question.
     * </p>
     *
     * <p>
     * This method is expected to be thread safe.
     * </p>
     *
     * @param   answer
     *          The answer to evaluate into a score between 0 and 1, inclusive.
     *
     * @return  The score for the specified answer, in the range [0,1].
     *
     * @throws  IllegalArgumentException
     *          The answer is a null reference or an empty string (after trimming).
     * @throws  ScoreCalculatorException
     *          Evaluation cannot be performed successfully for the implementation.
     */
    protected abstract double evaluateAnswer(String answer) throws ScoreCalculatorException;
}
