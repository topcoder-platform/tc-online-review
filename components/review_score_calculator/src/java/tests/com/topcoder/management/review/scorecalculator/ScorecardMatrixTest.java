/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import com.topcoder.management.scorecard.data.Question;
import com.topcoder.util.weightedcalculator.LineItem;
import com.topcoder.util.weightedcalculator.MathMatrix;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Contains the unit tests for the ScorecardMatrix class.
 *
 * @author      UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class ScorecardMatrixTest extends TestCase {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Fields

    /**
     * The math matrix to use in unit testing.
     */
    private static final MathMatrix MATH_MATRIX = new MathMatrix("A matrix.");

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Fields

    /**
     * The scorecard matrix to be used in unit testing.
     */
    private ScorecardMatrix instance = null;

    /**
     * The item to use in unit testing.
     */
    private LineItem item = null;

    /**
     * The question to use in unit testing.
     */
    private Question question = null;


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Methods

    /**
     * Creates a test suite for the tests in this test case.
     *
     * @return  A TestSuite for this test case.
     */
    public static Test suite() {
        return new TestSuite(ScorecardMatrixTest.class);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SetUp

    /**
     * Recreates the member fields before each unit test.
     */
    protected void setUp() {
        instance = new ScorecardMatrix(MATH_MATRIX);

        item = new LineItem("A line item.", 0.5, 100.0);

        question = new Question();
        question.setId(1);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor(MathMatrix) Tests

    /**
     * Ensures that the constructor throws an IllegalArgumentException when given a null math matrix.
     */
    public void testCtorThrowsOnNullMatrix() {
        try {
            new ScorecardMatrix(null);
            fail("An IllegalArgumentException is expected when given a null scorecard matrix.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the constructor sets the mathMatrix field properly, by checking that the getMathMatrix method
     * returns the original math matrix.
     */
    public void testCtorSetsMathMatrix() {
        assertSame(
                "The mathMatrix field was not set properly.",
                MATH_MATRIX, instance.getMathMatrix());
    }

    /**
     * Ensures that the constructor sets the entryMap field properly, by checking that the getNumberOfQuestions
     * method returns zero.
     */
    public void testCtorSetsEntryMap() {
        assertEquals(
                "The number of questions should initially be zero.",
                0, instance.getNumberOfQuestions());
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // addEntry(long, LineItem, Question) Tests

    /**
     * Ensures that the addEntry method throws an IllegalArgumentException when given a negative question
     * identifier.
     */
    public void testAddEntryThrowsOnNegativeQuestionId() {
        checkAddEntryThrowsIAE(-1, item, question, "a negative question identifier.");
    }

    /**
     * Ensures that the addEntry method throws an IllegalArgumentException when given a zero question
     * identifier.
     */
    public void testAddEntryThrowsOnZeroQuestionId() {
        checkAddEntryThrowsIAE(0, item, question, "a negative question identifier.");
    }

    /**
     * Ensures that the addEntry method throws an IllegalArgumentException when given a null line item.
     */
    public void testAddEntryThrowsOnNullItem() {
        checkAddEntryThrowsIAE(question.getId(), null, question, "a null item.");
    }

    /**
     * Ensures that the addEntry method throws an IllegalArgumentException when given a null question.
     */
    public void testAddEntryThrowsOnNullQuestion() {
        checkAddEntryThrowsIAE(question.getId(), item, null, "a null question.");
    }

    /**
     * Ensures that the addEntry method properly associated the line item with the question identifier.
     */
    public void testAddEntryAddedItem() {
        instance.addEntry(question.getId(), item, question);

        assertSame(
                "The line item was not properly associated with the question id.",
                item, instance.getLineItem(question.getId()));
    }

    /**
     * Ensures that the addEntry method properly associated the question with the question identifier.
     */
    public void testAddEntryAddedQuestion() {
        instance.addEntry(question.getId(), item, question);

        assertSame(
                "The question was not properly associated with the question id.",
                question, instance.getQuestion(question.getId()));
    }

    /**
     * Ensures that the addEntry method properly incremented the number of associated questions.
     */
    public void testAddEntryIncrementedCount() {
        instance.addEntry(question.getId(), item, question);

        assertEquals(
                "The number of questions should have increased to one.",
                1, instance.getNumberOfQuestions());
    }

    /**
     * Helper method that ensures the addEntry method throws an IllegalArgumentException for the given arguments.
     *
     * @param   questionId
     *          The question identifier to use.
     * @param   item
     *          The line item instance to use.
     * @param   question
     *          The question instance to use.
     * @param   message
     *          The message to be used in the error message if the unit test fails.
     */
    private void checkAddEntryThrowsIAE(long questionId, LineItem item, Question question, String message) {
        try {
            instance.addEntry(questionId, item, question);
            fail("An IllegalArgumentException is expected when " + message);
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // removeEntry(long) Tests

    /**
     * Ensures that the removeEntry method returns true when an entry was actually removed.
     */
    public void testRemoveEntryReturnsTrue() {
        instance.addEntry(question.getId(), item, question);

        assertTrue(
                "The entries associated with the question identifier should have been removed.",
                instance.removeEntry(question.getId()));
    }

    /**
     * Ensures that the removeEntry method returns false when an entry was not actually removed.
     */
    public void testRemoveEntryReturnsFalse() {
        instance.addEntry(question.getId(), item, question);

        assertFalse(
                "The entries associated with the question identifier shouldn't have been removed.",
                instance.removeEntry(-1));
    }

    /**
     * Ensures that the removeEntry method properly removed the line item associated with a given question
     * identifier.
     */
    public void testRemoveEntryRemovedLineItem() {
        instance.addEntry(question.getId(), item, question);
        instance.removeEntry(question.getId());

        assertNull("The line item entry should have been removed.", instance.getLineItem(question.getId()));
    }

    /**
     * Ensures that the removeEntry method properly removed the question associated with a given question
     * identifier.
     */
    public void testRemoveEntryRemovedQuestion() {
        instance.addEntry(question.getId(), item, question);
        instance.removeEntry(question.getId());

        assertNull("The question entry should have been removed.", instance.getQuestion(question.getId()));
    }

    /**
     * Ensures that the removeEntry method properly decremented the number of associated questions.
     */
    public void testRemoveEntryDecrementedCount() {
        instance.addEntry(question.getId(), item, question);
        instance.removeEntry(question.getId());

        assertEquals(
                "The number of questions should have decreased to zero.",
                0, instance.getNumberOfQuestions());
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // clearEntries() Tests

    /**
     * Ensures that the clearEntries method properly removed the line item.
     */
    public void testClearEntriesRemovedLineItem() {
        instance.addEntry(question.getId(), item, question);
        instance.clearEntries();

        assertNull("The line item entry should have been removed.", instance.getLineItem(question.getId()));
    }

    /**
     * Ensures that the clearEntries method properly removed the question.
     */
    public void testClearEntriesRemovedQuestion() {
        instance.addEntry(question.getId(), item, question);
        instance.clearEntries();

        assertNull("The question entry should have been removed.", instance.getQuestion(question.getId()));
    }

    /**
     * Ensures that the clearEntries method properly decremented the number of associated questions.
     */
    public void testClearEntriesDecrementedCount() {
        instance.addEntry(question.getId(), item, question);
        instance.clearEntries();

        assertEquals(
                "The number of questions should have decreased to zero.",
                0, instance.getNumberOfQuestions());
    }
}
