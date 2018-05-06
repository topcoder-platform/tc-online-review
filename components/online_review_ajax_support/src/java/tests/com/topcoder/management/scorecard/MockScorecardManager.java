/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.scorecard;

import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.MockScorecardStatus;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.QuestionType;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.ScorecardStatus;
import com.topcoder.management.scorecard.data.ScorecardType;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.search.builder.filter.Filter;

/**
 * A mock implementation for <code>ScorecardManager</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class MockScorecardManager implements ScorecardManager {

    /**
     * <p>A <code>Throwable</code> representing the exception to be thrown from any method of the mock class.</p>
     */
    private static Throwable globalException = null;

    /**
     * Create scorecard.
     * @param scorecard the scorecard
     * @param operator the operator
     */
    public void createScorecard(Scorecard scorecard, String operator) {
    }

    /**
     * Get all question type.
     * @return all question type
     */
    public QuestionType[] getAllQuestionTypes() {
        return null;
    }

    /**
     * Get all scorecard status.
     * @return all the scorecard status.
     */
    public ScorecardStatus[] getAllScorecardStatuses() {
        ScorecardStatus active = new MockScorecardStatus();
        active.setId(1);
        active.setName("Active");

        ScorecardStatus inactive = new MockScorecardStatus();
        inactive.setId(1);
        inactive.setName("Inactive");

        return new ScorecardStatus[] {active, inactive};
    }

    /**
     * Get all question type.
     * @return all question type.
     */
    public ScorecardType[] getAllScorecardTypes() {
        return null;
    }

    /**
     * Get scorecard by id.
     * @param id the score card id
     * @return the score card
     */
    public Scorecard getScorecard(long id) throws PersistenceException {
        if (MockScorecardManager.globalException != null) {
            if (MockScorecardManager.globalException instanceof PersistenceException) {
                throw (PersistenceException) MockScorecardManager.globalException;
            } else {
                throw new RuntimeException("The test may not be configured properly",
                                           MockScorecardManager.globalException);
            }
        }
        if (id == 1) {
            Scorecard card = new Scorecard();
            card.setId(1);
            card.setName("Scorecard");
            Group group1 = new Group(1, "Group1");
            Section section1 = new Section(1, "Section1");
            Question question1 = new Question(1,"Question1");
            question1.setDescription("Desc");
            question1.setGuideline("Guide");
            question1.setWeight(50);
            question1.setQuestionType(new QuestionType(1, "Q1"));
            section1.setWeight(50);
            group1.setWeight(50);
            section1.addQuestion(question1);
            group1.addSection(section1);

            Group group2 = new Group(2, "Group2");
            Section section2 = new Section(2, "Section2");
            Question question2 = new Question(2,"Question2");
            question2.setDescription("Desc");
            question2.setGuideline("Guide");
            question2.setWeight(50);
            question2.setQuestionType(new QuestionType(2, "Q2"));
            section2.setWeight(50);
            group2.setWeight(50);
            section2.addQuestion(question2);
            group2.addSection(section2);

            card.addGroup(group1);
            card.addGroup(group2);

            return card;
        }
        return null;
    }

    /**
     * Search scorecards.
     * @param filter the filter
     * @param complete whether complete
     * @return scorecards found
     */
    public Scorecard[] searchScorecards(Filter filter, boolean complete) {
        return null;
    }

    /**
     * Update scorecard.
     * @param scorecard the scorecard
     * @param operator the operator
     */
    public void updateScorecard(Scorecard scorecard, String operator) {
    }

    public Scorecard[] getScorecards(long[] arg0, boolean arg1) throws PersistenceException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * <p>Sets the exception to be thrown when the specified method is called.</p>
     *
     * @param exception a <code>Throwable</code> representing the exception to be thrown whenever any method is called.
     * If this argument is <code>null</code> then no exception will be thrown.
     */
    public static void throwGlobalException(Throwable exception) {
        MockScorecardManager.globalException = exception;
    }

    /**
     * <p>Releases the state of <code>MockLog</code> so all collected method arguments, configured method results and
     * exceptions are lost.</p>
     */
    public static void releaseState() {
        MockScorecardManager.globalException = null;
    }

    /**
     * <p>Initializes the initial state for all created instances of <code>MockScorecardManager</code> class.</p>
     */
    public static void init() {
    }
}
