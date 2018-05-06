/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorecalculator.accuracytests;

import com.topcoder.management.review.scorecalculator.ScorecardMatrix;
import com.topcoder.management.scorecard.data.Question;

import com.topcoder.util.weightedcalculator.LineItem;
import com.topcoder.util.weightedcalculator.MathMatrix;

import junit.framework.TestCase;


/**
 * Tests for ScorecardMatrix class.
 *
 * @author qiucx0161
 * @version 1.0
 */
public class TestScorecardMatrix extends TestCase {
    /** The MathMatrix instance used for testing. */
    private MathMatrix mm = null;

    /** The ScorecardMatrix instance used for testing. */
    private ScorecardMatrix sm = null;

    /** The LineItem instance used for testing. */
    private LineItem item = null;

    /** The Question instance used for testing. */
    private Question question = null;

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        mm = new MathMatrix("MathMatrix");
        sm = new ScorecardMatrix(mm);

        item = new LineItem("LineItem", 0.123, 100.00f);

        question = new Question(999990, "question", 100.00f);
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
     * Tests ScorecardMatrix(MathMatrix mathMatrix) method with accuracy state.
     */
    public void testScorecardMatrixAccuracy() {
        assertSame("creating scorecard matrix fails.", mm, sm.getMathMatrix());
        assertEquals("The number of questions is wrong.", 0, sm.getNumberOfQuestions());
    }

    /**
     * Tests addEntry(long questionId, LineItem item, Question question) method with
     * accuracy state.
     */
    public void testAddEntryAccuracy() {
        sm.addEntry(888888, item, question);
        sm.addEntry(888889, item, question);
        assertEquals("The LineItem is not added properly.", item, sm.getLineItem(888888));
        assertEquals("the count of question is wrong.", 2, sm.getNumberOfQuestions());
        assertEquals("The LineItem is not added properly.", question,
            sm.getQuestion(888888));
    }

    /**
     * Tests removeEntry(long questionId) method with accuracy state.
     */
    public void testRemoveEntryAccuracy() {
        sm.addEntry(888888, item, question);
        assertTrue("removing entry fails.", sm.removeEntry(888888));
    }

    /**
     * Tests clearEntries() method with accuracy state.
     */
    public void testClearEntriesAccuracy() {
        sm.addEntry(888888, item, question);
        sm.addEntry(888889, item, question);
        assertEquals("the count of question is wrong.", 2, sm.getNumberOfQuestions());

        sm.clearEntries();
        assertEquals("the count of question is wrong.", 0, sm.getNumberOfQuestions());
    }
}
