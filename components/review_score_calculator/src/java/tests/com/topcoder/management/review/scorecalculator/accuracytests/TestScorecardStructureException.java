/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorecalculator.accuracytests;
import com.topcoder.management.review.scorecalculator.ScorecardStructureException;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.ScorecardStatus;
import com.topcoder.management.scorecard.data.ScorecardType;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.util.errorhandling.BaseException;

import junit.framework.TestCase;
/**
 * Tests for ScorecardStructureException class.
 * @author qiucx0161
 * @version 1.0
 */
public class TestScorecardStructureException extends TestCase {

    /**
     * A Scorecard used for test.
     */
    private Scorecard scorecard = null;

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

        scorecard = new Scorecard(999990, "validator test");

        Question question = new Question(999991, "Is elegant APIs?", 100.0f);
        question.setDescription("here is the first des.");
        question.setGuideline("here is the first guide");

        Section section = new Section(999992, "structure design party.", 100.0f);
        section.addQuestion(question);
        Group group = new Group(999993, "Method.", 100.0f);
        group.addSection(section);

        scorecard.addGroup(group);
        scorecard.setCategory(999994);
        scorecard.setVersion("2.0");
        scorecard.setMinScore(0);
        scorecard.setMaxScore(100.0f);
        scorecard.setScorecardStatus(new ScorecardStatus(999995, "incompleted."));
        scorecard.setScorecardType(new ScorecardType(999996, "design"));
    }

    /**
     * Setup the test environment.
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests ScorecardStructureException(String details, Scorecard scorecard) method with accuracy state.
     */
    public void testScorecardStructureException1Accuracy() {

        ScorecardStructureException e = new ScorecardStructureException(ERROR_MESSAGE, scorecard);

        assertNotNull("Unable to instantiate CalculationException", e);
        assertTrue("Exception should be extension of BaseException class", e instanceof BaseException);
        assertEquals("Error message is not properly set", ERROR_MESSAGE, e.getMessage());
        assertEquals("Error scorecard.", scorecard, e.getScorecard());
    }

    /**
     * Tests ScorecardStructureException(String details, Throwable cause, Scorecard scorecard) method with accuracy state.
     */
    public void testScorecardStructureExceptionAccuracy() {
        ScorecardStructureException e = new ScorecardStructureException(ERROR_MESSAGE, CAUSE, scorecard);

        assertNotNull("Unable to instantiate CalculationException", e);
        assertEquals("Cause is not properly set", CAUSE, e.getCause());
        assertEquals("Error scorecard.", scorecard, e.getScorecard());
    }
}
