/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import com.topcoder.management.review.data.Item;
import com.topcoder.management.scorecard.data.Question;

/**
 * <p>
 * Thrown by ScoreCalculator to indicate any problems while processing a review item.
 * </p>
 *
 * <p>
 * <b>Thread Safety</b>: This class is thread safe.
 * </p>
 *
 * @author      nicka81, UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class ScoreCalculatorException extends CalculationException {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Fields

    /**
     * The review item instance that generated this exception.
     */
    private final Item item;

    /**
     * The scorecard question instance that generated this exception.
     */
    private final Question question;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors

    /**
     * Creates a new ScoreCalculatorException, with the specified details, item, and question.
     *
     * @param   details
     *          A message describing what caused the exception to be thrown.
     * @param   item
     *          The review item instance that caused the exception to be thrown.
     * @param   question
     *          The scorecard question instance that caused the exception to be thrown.
     */
    public ScoreCalculatorException(String details, Item item, Question question) {
        this(details, null, item, question);
    }

    /**
     * Creates a new ScoreCalculatorException, with the specified details, root cause, and review.
     *
     * @param   details
     *          A message describing what caused the exception to be thrown.
     * @param   cause
     *          The exception that prompted this exception to be thrown.
     * @param   item
     *          The review item instance that caused the exception to be thrown.
     * @param   question
     *          The scorecard question instance that caused the exception to be thrown.
     */
    public ScoreCalculatorException(String details, Throwable cause, Item item, Question question) {
        super(details, cause);
        this.item = item;
        this.question = question;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Accessors

    /**
     * Gets the review item instance that generated this exception.
     *
     * @return  The review item instance that generated this exception.
     */
    public Item getItem() {
        return item;
    }

    /**
     * Gets the scorecard question instance that generated this exception.
     *
     * @return  The scorecard question instance that generated this exception.
     */
    public Question getQuestion() {
        return question;
    }
}
