/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * 
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.topcoder.management.scorecard.PersistenceException;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.data.QuestionType;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.ScorecardStatus;
import com.topcoder.management.scorecard.data.ScorecardType;
import com.topcoder.search.builder.filter.Filter;


/**
 * Mock class.
 * 
 * @author assistant
 * @version 1.0
 */
public class MockScorecardManager implements ScorecardManager {

    public static void main(String[] args) {
    }

    public void createScorecard(Scorecard scorecard, String operator) {
    }

    public void updateScorecard(Scorecard scorecard, String operator) {
    }

    public Scorecard getScorecard(long id) {
        return null;
    }

    public Scorecard[] searchScorecards(Filter filter, boolean complete) {
        return null;
    }

    public ScorecardType[] getAllScorecardTypes() {
        return null;
    }

    public QuestionType[] getAllQuestionTypes() {
        return null;
    }

    public ScorecardStatus[] getAllScorecardStatuses() {
        return new ScorecardStatus[]{new MockScorecardStatus(1), new MockScorecardStatus(2)};
    }

    public Scorecard[] getScorecards(long[] arg0, boolean arg1) throws PersistenceException {
        // TODO Auto-generated method stub
        return null;
    }
}
