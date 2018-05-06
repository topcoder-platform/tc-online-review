/*
 * Copyright (C) 2006-2014 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator.calculators;

import com.topcoder.management.review.scorecalculator.AbstractScoreCalculator;
import com.topcoder.management.review.scorecalculator.ConfigurationException;
import com.topcoder.management.review.scorecalculator.ScoreCalculatorException;
import com.topcoder.management.review.scorecalculator.Util;

/**
 * <p>
 * A simple score calculator for binary items (in other words, items that only accept two values; for example,
 * yes/no items).
 * </p>
 *
 * <p>
 * One of these items will always be scored as 1.0 (the positive answer) while another one will be scored as 0.0
 * (the negative answer), both of which are configurable. The default positive answer is "Yes" and the default
 * negative answer is "No". Please note that the comparison is case sensitive!
 * <p>
 *
 * <b>Thread Safety</b>: This class is thread safe.
 * </p>
 *
 * @author      nicka81, UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0.4
 */
public class BinaryScoreCalculator extends AbstractScoreCalculator {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Fields

    /**
     * The value returned by the evaluateAnswer method when a positive answer is encountered.
     */
    private static final double POSITIVE_ANSWER_VALUE = 1.0;

    /**
     * The value returned by the evaluateAnswer method when a negative answer is encountered.
     */
    private static final double NEGATIVE_ANSWER_VALUE = 0.0;

    /**
     * The default value used as the positive answer.
     */
    private static final String POSITIVE_ANSWER_DEFAULT = "Yes";

    /**
     * The default value used as the negative answer.
     */
    private static final String NEGATIVE_ANSWER_DEFAULT = "No";

    /**
     * The name of the configuration property containing the positive answer.
     */
    private static final String POSITIVE_ANSWER_PROPERTY_NAME = "positive_answer";

    /**
     * The name of the configuration property containing the negative answer.
     */
    private static final String NEGATIVE_ANSWER_PROPERTY_NAME = "negative_answer";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Fields

    /**
     * <p>
     * The positive answer constant.
     * </p>
     *
     * <p>
     * If the answer passed into the evaluateAnswer method equals this value exaclty. then POSITIVE_ANSWER_VALUE
     * will be returned by that method.
     * </p>
     *
     * <p>
     * <b>Invariant</b>: Cannot be null or an empty string (trimmed); cannot be equal to the negative answer
     * constant.
     * </p>
     */
    private final String positiveAnswer;

    /**
     * <p>
     * The negative answer constant.
     * </p>
     *
     * <p>
     * If the answer passed into the evaluateAnswer method equals this value exaclty. then NEGATIVE_ANSWER_VALUE
     * will be returned by that method.
     * </p>
     *
     * <p>
     * <b>Invariant</b>: Cannot be null or an empty string (trimmed); cannot be equal to the positive answer
     * constant.
     * </p>
     */
    private final String negativeAnswer;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors

    /**
     * Creates a new BinaryScoreCalculator, using the default values of "Yes" as the positive answer, and "No" as
     * the negative answer.
     */
    public BinaryScoreCalculator() {
        this(POSITIVE_ANSWER_DEFAULT, NEGATIVE_ANSWER_DEFAULT);
    }

    /**
     * <p>
     * Creates a new BinaryScoreCalculator with the specified configuration namespace.
     * </p>
     *
     * <p>
     * This constructor reads in the required 'positive_answer' and 'negative_answer' properties from the given
     * configuration namespace (which cannot be empty/blank strings and cannot equal one another).
     * </p>
     *
     * @param   namespace
     *          The configuration namespace to read the positive and negative answer settings from.
     *
     * @throws  ConfigurationException
     *          The namespace does not exist, the positve_answer property does not exist (or is an empty
     *          string), the negative_answer property does not exist (or is an empty string), or the
     *          positive_answer setting is equal to the negative_answer setting.
     * @throws  IllegalArgumentException
     *          The namespace is null or an empty string.
     */
    public BinaryScoreCalculator(String namespace) throws ConfigurationException {
        Util.checkNotNullOrEmpty(namespace, "namespace");

        this.positiveAnswer = Util.getRequiredProperty(namespace, POSITIVE_ANSWER_PROPERTY_NAME);
        this.negativeAnswer = Util.getRequiredProperty(namespace, NEGATIVE_ANSWER_PROPERTY_NAME);

        if (positiveAnswer.equals(negativeAnswer)) {
            throw new ConfigurationException("The positive answer cannot equal the negative answer.");
        }
    }

    /**
     * Creates a new BinaryScoreCalculator, using the given positive and negative answers.
     *
     * @param   positiveAnswer
     *          The positive answer to initialize with.
     * @param   negativeAnswer
     *          The negative answer to initialize with.
     *
     * @throws  IllegalArgumentException
     *          The positiveAnswer/negativeAnswer is null or an empty string, or the positiveAnswer is equal to the
     *          negativeAnswer.
     */
    public BinaryScoreCalculator(String positiveAnswer, String negativeAnswer) {
        // Sanity check arguments.
        Util.checkNotNullOrEmpty(positiveAnswer, "positiveAnswer");
        Util.checkNotNullOrEmpty(negativeAnswer, "negativeAnswer");

        if (positiveAnswer.equals(negativeAnswer)) {
            throw new IllegalArgumentException("The positive answer cannot equal the negative answer.");
        }

        // Initialize member fields.
        this.positiveAnswer = positiveAnswer;
        this.negativeAnswer = negativeAnswer;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // AbstractScoreCalculator Overridden Methods

    /**
     * <p>
     * Evalutes the given answer and returns a value between 0 and 1, inclusive.
     * </p>
     *
     * <p>
     * The answer must be equal to either the positive answer or the negative answer (as set by the constructor).
     * A positive answer will result in a 1.0, while a negative answer will result in a 0.0.
     * </p>
     *
     * @param   answer
     *          The answer to be evaluated into a score between 0 and 1, inclusive.
     *
     * @return  The score for the specified answer, which is 1.0 if the answer is equal to the positive answer,
     *          and 0.0 if the answer is equal to the negative answer.
     *
     * @throws  IllegalArgumentException
     *          The answer is a null reference or an empty string.
     * @throws  ScoreCalculatorException
     *          The answer is neither equal to the positive nor negative answer.
     */
    protected double evaluateAnswer(String answer) throws ScoreCalculatorException {
        Util.checkNotNullOrEmpty(answer, "answer");

        if (answer.equals(positiveAnswer)) {
            return POSITIVE_ANSWER_VALUE;
        } else if (answer.equals(negativeAnswer)) {
            return NEGATIVE_ANSWER_VALUE;
        } else {
            throw new ScoreCalculatorException(
                    "The answer '" + answer + "' must be '" + positiveAnswer + "' or '" + negativeAnswer + "'.",
                    null, null);
        }
    }
}
