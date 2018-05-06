/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorecalculator.accuracytests;

import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.scorecalculator.AbstractScoreCalculator;
import com.topcoder.management.review.scorecalculator.ScoreCalculatorException;
import com.topcoder.management.scorecard.data.Question;

import junit.framework.TestCase;


/**
 * Tests for AbstractScoreCalculator class.
 *
 * @author qiucx0161
 * @version 1.0
 */
public class TestAbstractScoreCalculator extends TestCase {
    /** AbstractScoreCalculator instance used for testing. */
    private AbstractScoreCalculator calc = null;

    /** The Question instance used for testing. */
    private Question question = null;

    /** Item instance used for test. */
    private Item item = null;

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        question = new Question(999991, "1+2=3");
        item = new Item(999992, 999991, "1");

        calc = new AbstractScoreCalculatorImpl();
    }

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests evaluateItem(Item item, Question question) method with accuracy state. the
     * answer is 1 and the result is 1.0f.
     *
     * @throws ScoreCalculatorException to JUnit.
     */
    public void testEvaluateItemAccuracy1() throws ScoreCalculatorException {
        assertEquals("the result is wrong.", 1.0f, calc.evaluateItem(item, question), 0);
    }

    /**
     * Tests evaluateItem(Item item, Question question) method with accuracy state. the
     * answer is 0 and the result is 0f.
     *
     * @throws ScoreCalculatorException to JUnit.
     */
    public void testEvaluateItemAccuracy2() throws ScoreCalculatorException {
        item.setAnswer("0");
        assertEquals("the result is wrong.", 0f, calc.evaluateItem(item, question), 0);
    }

    /**
     * Tests evaluateItem(Item item, Question question) method with accuracy state. the
     * answer is specified and the result is 0.25f.
     *
     * @throws ScoreCalculatorException to JUnit.
     */
    public void testEvaluateItemAccuracy3() throws ScoreCalculatorException {
        item.setAnswer("0.3");
        assertEquals("the result is wrong.", 0.25f, calc.evaluateItem(item, question), 0);
    }

    /**
     * AbstractScoreCalculatorImpl instance used for testing.
     */
    final class AbstractScoreCalculatorImpl extends AbstractScoreCalculator {
        /**
         * Creates a new AbstractScoreCalculatorImpl.
         */
        public AbstractScoreCalculatorImpl() {
            // Do nothing.
        }

        /**
         * <p>
         * Returns a score between 0 and 1, inclusive, for the given answer to a
         * question.
         * </p>
         *
         * <p>
         * This method is expected to be thread safe.
         * </p>
         *
         * @param answer The answer to evaluate into a score between 0 and 1, inclusive.
         *
         * @return The score for the specified answer, in the range [0,1].
         *
         * @throws IllegalArgumentException The answer is a null reference or an empty
         *         string (after trimming).
         * @throws ScoreCalculatorException Evaluation cannot be performed successfully
         *         for the implementation.
         */
        public float evaluateAnswer(String answer) throws ScoreCalculatorException {
            if (answer.equals("1")) {
                return 1.0f;
            } else if (answer.equals("0")) {
                return 0;
            } else {
                return 0.25f;
            }
        }
    }
}
