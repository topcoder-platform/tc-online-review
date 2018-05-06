/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorecalculator.failuretests;

import com.topcoder.management.review.scorecalculator.ScorecardMatrix;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.util.weightedcalculator.LineItem;
import com.topcoder.util.weightedcalculator.MathMatrix;

import junit.framework.TestCase;

/**
 * Failure test cases for class <code>ScorecardMatrix </code>.
 *
 * @author Chenhong
 * @version 1.0
 */
public class FailureTestForScorecardMatrix extends TestCase {

    /**
     * Test constructor <code>ScorecardMatrix(MathMatrix mathMatrix) </code>. If the parameter mathMathix is null,
     * IllegalArgumentException should be thrown.
     *
     */
    public void testScorecardMatrix() {
        try {
            new ScorecardMatrix(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>void addEntry(long questionId, LineItem item, Question question)</code>. if the questionId
     * is not a positive long, item is a null reference, or question is a null reference, IllegalArgumentException
     * should be thrown.
     */
    public void testAddEntry_1() throws Exception {
        ScorecardMatrix mathrix = new ScorecardMatrix(new MathMatrix("math mathix"));
        try {
            mathrix.addEntry(-1, new LineItem("line item", 0.5, 100), new Question(1));
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>void addEntry(long questionId, LineItem item, Question question)</code>. if the questionId
     * is not a positive long, item is a null reference, or question is a null reference, IllegalArgumentException
     * should be thrown.
     */
    public void testAddEntry_2() throws Exception {
        ScorecardMatrix mathrix = new ScorecardMatrix(new MathMatrix("math mathix"));
        try {
            mathrix.addEntry(1, null, new Question(1));
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>void addEntry(long questionId, LineItem item, Question question)</code>. if the questionId
     * is not a positive long, item is a null reference, or question is a null reference, IllegalArgumentException
     * should be thrown.
     */
    public void testAddEntry_3() throws Exception {
        ScorecardMatrix mathrix = new ScorecardMatrix(new MathMatrix("math mathix"));
        try {
            mathrix.addEntry(1, new LineItem("line item", 0.5, 100), null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }
}
