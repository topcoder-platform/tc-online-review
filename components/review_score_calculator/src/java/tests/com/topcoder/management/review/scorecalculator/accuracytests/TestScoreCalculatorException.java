/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorecalculator.accuracytests;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.scorecalculator.ScoreCalculatorException;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.util.errorhandling.BaseException;

import junit.framework.TestCase;
/**
 * Tests for ScoreCalculatorException class.
 * @author qiucx0161
 * @version 1.0
 */
public class TestScoreCalculatorException extends TestCase {

    /**
     * Question instance for testing.
     */
    private Question question = null;

    /**
     * Item instance for testing.
     */
    private Item item = null;

    /**
     * <p>
     * Error message used for testing.
     * </p>
     */
    private static final String ERROR_MESSAGE = "error message";

    /**
     * <p>
     * Cause used for testing.
     * </p>
     */
    private static final Exception CAUSE = new Exception();

    /**
     * Setup the test environment.
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        question = new Question(999991, "Is elegant APIs?", 100.0f);
        question.setDescription("here is the first des.");
        question.setGuideline("here is the first guide");

        item = new Item();
    }

    /**
     * Setup the test environment.
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests ScoreCalculatorException(String details, Item item, Question question) method with accuracy state.
     */
    public void testScoreCalculatorException1Accuracy() {
        ScoreCalculatorException e = new ScoreCalculatorException(ERROR_MESSAGE, item, question);

        assertNotNull("Unable to instantiate CalculationException", e);
        assertTrue("Exception should be extension of BaseException class", e instanceof BaseException);
        assertEquals("Error message is not properly set", ERROR_MESSAGE, e.getMessage());
        assertEquals("Error scorecard.", item, e.getItem());
        assertEquals("Error scorecard.", question, e.getQuestion());
    }

    /**
     * Tests ScoreCalculatorException(String details, Throwable cause, Item item, Question question) method with accuracy state.
     */
    public void testScoreCalculatorExceptionAccuracy() {
        ScoreCalculatorException e = new ScoreCalculatorException(ERROR_MESSAGE, CAUSE, item, question);

        assertNotNull("Unable to instantiate CalculationException", e);
        assertEquals("Error scorecard.", item, e.getItem());
        assertEquals("Error scorecard.", question, e.getQuestion());
    }
}
