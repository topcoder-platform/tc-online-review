/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorecalculator.failuretests;

import com.topcoder.management.review.scorecalculator.ConfigurationException;
import com.topcoder.management.review.scorecalculator.ScoreCalculatorException;
import com.topcoder.management.review.scorecalculator.calculators.BinaryScoreCalculator;

/**
 * This class is an simple mocked subclass of BinaryScoreCalculator. In this class, the method evaluateAnswer is public.
 * It is created for testing.
 *
 * @author Chenhong
 * @version 1.0
 */
public class MyBinaryScoreCalculator extends BinaryScoreCalculator {
    /**
     * Default constructor.
     *
     */
    public MyBinaryScoreCalculator() {
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
    public MyBinaryScoreCalculator(String namespace) throws ConfigurationException {
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
