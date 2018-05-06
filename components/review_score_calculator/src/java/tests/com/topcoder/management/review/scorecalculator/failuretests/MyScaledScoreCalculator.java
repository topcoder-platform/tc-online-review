/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorecalculator.failuretests;

import com.topcoder.management.review.scorecalculator.ConfigurationException;
import com.topcoder.management.review.scorecalculator.ScoreCalculatorException;
import com.topcoder.management.review.scorecalculator.calculators.ScaledScoreCalculator;

/**
 * This class is an simple mocked subclass of ScaledScoreCalculator. In this class, the method evaluateAnswer is public.
 * It is created for testing.
 *
 * @author Chenhong
 * @version 1.0
 */
public class MyScaledScoreCalculator extends ScaledScoreCalculator {
    /**
     * Default constructor.
     *
     */
    public MyScaledScoreCalculator() {
        super();
    }

    /**
     * Constructor with namespace.
     *
     * @param namespace
     *            the namespace to create MyScaledScoreCalculator instance
     * @throws ConfigurationException
     *             if config information is not correct.
     */
    public MyScaledScoreCalculator(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * Evaluate the answer.
     *
     * @param answer
     *            the answer to evaluate.
     * @throws ScoreCalculatorException
     *             if errors happened.
     */
    public float evaluateAnswer(String answer) throws ScoreCalculatorException {
        return super.evaluateAnswer(answer);
    }
}
