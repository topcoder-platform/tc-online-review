/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import junit.framework.TestCase;

import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.scorecalculator.calculators.BinaryScoreCalculator;
import com.topcoder.management.scorecard.data.Question;

/**
 * Abstract base class for testing AbstractScoreCalculator implementations.
 *
 * @author      UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public abstract class AbstractScoreCalculatorTest extends TestCase {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Fields

    /**
     * The score calculator instance to use in unit testing.
     */
    private ScoreCalculator instance = null;

    /**
     * The item to use in unit testing.
     */
    private Item item = null;

    /**
     * The question to use in unit testing.
     */
    private Question question = null;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Abstract Methods

    /**
     * Creates a new AbstractScoreCalculator of the type currently being unit tested.
     *
     * @return  A new AbstractScoreCalculator of the type currently being unit tested.
     */
    protected abstract AbstractScoreCalculator createInstance();

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SetUp

    /**
     * Recreates the member fields before each unit test.
     *
     * @throws  Exception
     *          An unknown error occured during setup.
     */
    protected void setUp() throws Exception {
        instance = createInstance();

        item = new Item();
        item.setId(1);
        item.setQuestion(1);
        item.setAnswer("Foo");

        question = new Question();
        question.setId(1);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Accessors and Mutators

    /**
     * Gets the instance that is currently being used for unit testing.
     *
     * @return  The instance that is currently being used for unit testing.
     */
    protected ScoreCalculator getInstance() {
        return instance;
    }

    /**
     * Sets the instance that should be used for unit testing.
     *
     * @param   instance
     *          The instance that
     */
    protected void setInstance(ScoreCalculator instance) {
        this.instance = instance;
    }

    /**
     * Gets the item that is currently being used for unit testing.
     *
     * @return  The item that is currently being used for unit testing.
     */
    protected Item getItem() {
        return item;
    }

    /**
     * Gets the question that is currently being used for unit testing.
     *
     * @return  The question that is currently being used for unit testing.
     */
    protected Question getQuestion() {
        return question;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // evaluateItem Tests

    /**
     * Ensures that the evaluateItem method throws an IllegalArgumentException when given a null item.
     *
     * @throws  ScoreCalculatorException
     *          An unexpected error occurred.
     */
    public void testEvaluateItemThrowsOnNullItem() throws ScoreCalculatorException {
        ScoreCalculator calculator = new BinaryScoreCalculator();

        try {
            calculator.evaluateItem(null, question);
            fail("An IllegalArgumentException is expected when given a null item.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the evaluateItem method throws an IllegalArgumentException when given a null question.
     *
     * @throws  ScoreCalculatorException
     *          An unexpected error occurred.
     */
    public void testEvaluateItemThrowsOnNullQuestion() throws ScoreCalculatorException {
        ScoreCalculator calculator = new BinaryScoreCalculator();

        try {
            calculator.evaluateItem(item, null);
            fail("An IllegalArgumentException is expected when given a null question.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the evaluateItem method throws an ScoreCalculatorException when given a question that has no
     * identifier initialized.
     */
    public void testEvaluateItemThrowsOnUninitializedQuestionId() {
        question.resetId();
        checkEvaulateItemThrowsSCE("a question that has an uninitialized id.");
    }

    /**
     * Ensures that the evaluateItem method throws an ScoreCalculatorException when given an item and question with
     * differing question identifiers.
     */
    public void testEvaluateItemThrowsOnDifferentQuestionId() {
        question.setId(question.getId() + 1);
        checkEvaulateItemThrowsSCE("a question that is different from the item's question id.");
    }

    /**
     * Ensures that the evaluateItem method throws an ScoreCalculatorException when given an item whose answer is
     * not a String.
     */
    public void testEvaluateItemThrowsOnNonStringAnswer() {
        item.setAnswer(new Exception());
        checkEvaulateItemThrowsSCE("an item's answer that is not a String.");
    }

    /**
     * Ensures that the evaluateItem method throws an ScoreCalculatorException when given an item whose answer is
     * null.
     */
    public void testEvaluateItemThrowsOnNullAnswer() {
        item.setAnswer(null);
        checkEvaulateItemThrowsSCE("an item's answer that is null.");
    }

    /**
     * Ensures that the evaluateItem method throws an ScoreCalculatorException when given an item whose answer is
     * an empty string.
     */
    public void testEvaluateItemThrowsOnEmptyAnswer() {
        item.setAnswer("");
        checkEvaulateItemThrowsSCE("an item's answer that is an empty string.");
    }

    /**
     * Ensures that the evaluateItem method throws an ScoreCalculatorException when given an item whose answer is
     * a blank string.
     */
    public void testEvaluateItemThrowsOnBlankAnswer() {
        item.setAnswer(" ");
        checkEvaulateItemThrowsSCE("an item's answer that is a blank string.");
    }

    /**
     * Helper method to check that the evaluateItem throws a ScoreCalculatorException with the current arguments
     * (which are member fields).
     *
     * @param   message
     *          The message to be used in an error message if the unit test fails.
     */
    protected void checkEvaulateItemThrowsSCE(String message) {
        try {
            instance.evaluateItem(item, question);
            fail("A ScorecardCalculatorException is expected when given " + message);
        } catch (ScoreCalculatorException ex) {
            assertSame("The item in the exception was not set properly.", item, ex.getItem());
            assertSame("The question in the exception was not set properly.", question, ex.getQuestion());
        }
    }
}
