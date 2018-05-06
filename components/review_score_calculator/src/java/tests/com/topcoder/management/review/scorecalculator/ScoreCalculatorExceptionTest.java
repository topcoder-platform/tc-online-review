/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import com.topcoder.management.review.data.Item;
import com.topcoder.management.scorecard.data.Question;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Contains the unit tests for the ScoreCalculatorException class.
 *
 * @author      UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class ScoreCalculatorExceptionTest extends TestCase {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Fields

    /**
     * The error message to be used in the unit tests.
     */
    protected static final String MESSAGE = "Error Message";

    /**
     * The root cause to be used in the unit tests.
     */
    protected static final Throwable CAUSE = new Exception("Cause Message");

    /**
     * The item to be used in the unit tests.
     */
    private static final Item ITEM = new Item();

    /**
     * The question to be used in the unit tests.
     */
    private static final Question QUESTION = new Question();

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Methods

    /**
     * Creates a test suite for the tests in this test case.
     *
     * @return  A TestSuite for this test case.
     */
    public static Test suite() {
        return new TestSuite(ScoreCalculatorExceptionTest.class);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor(String, Item, Question) Tests

    /**
     * Ensures that the three parameter constructor set the message attribute properly.
     */
    public void test3CtorSetsMessage() {
        assertEquals(
                "The message was improperly set.",
                MESSAGE, new ScoreCalculatorException(MESSAGE, ITEM, QUESTION).getMessage());
    }

    /**
     * Ensures that the three parameter constructor set the cause attribute properly.
     */
    public void test3CtorSetsCause() {
        assertNull(
                "The cause was improperly set.",
                new ScoreCalculatorException(MESSAGE, ITEM, QUESTION).getCause());
    }

    /**
     * Ensures that the three parameter constructor set the item attribute properly.
     */
    public void test3CtorSetsItem() {
        assertSame(
                "The item was improperly set.",
                ITEM, new ScoreCalculatorException(MESSAGE, ITEM, QUESTION).getItem());
    }

    /**
     * Ensures that the three parameter constructor set the question attribute properly.
     */
    public void test3CtorSetsQuestion() {
        assertSame(
                "The question was improperly set.",
                QUESTION, new ScoreCalculatorException(MESSAGE, ITEM, QUESTION).getQuestion());
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructor(String, Throwable, Item, Question) Tests

    /**
     * Ensures that the four parameter constructor set the message attribute properly.
     */
    public void test4CtorSetsMessage() {
        assertEquals(
                "The message was improperly set.",
                MESSAGE + ", caused by " + CAUSE.getMessage(),
                new ScoreCalculatorException(MESSAGE, CAUSE, ITEM, QUESTION).getMessage());
    }

    /**
     * Ensures that the four parameter constructor set the cause attribute properly.
     */
    public void test4CtorSetsCause() {
        assertSame(
                "The cause was improperly set.",
                CAUSE, new ScoreCalculatorException(MESSAGE, CAUSE, ITEM, QUESTION).getCause());
    }

    /**
     * Ensures that the four parameter constructor set the itemattribute properly.
     */
    public void test4CtorSetsItem() {
        assertSame(
                "The item was improperly set.",
                ITEM, new ScoreCalculatorException(MESSAGE, CAUSE, ITEM, QUESTION).getItem());
    }

    /**
     * Ensures that the four parameter constructor set the question attribute properly.
     */
    public void test4CtorSetsQuestion() {
        assertSame(
                "The question was improperly set.",
                QUESTION, new ScoreCalculatorException(MESSAGE, CAUSE, ITEM, QUESTION).getQuestion());
    }
}
