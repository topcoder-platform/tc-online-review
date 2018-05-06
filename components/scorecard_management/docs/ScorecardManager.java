package com.topcoder.management.scorecard;

import com.topcoder.search.builder.filter.Filter;

public interface ScorecardManager {
	void createScorecard(Scorecard scorecard, String operator);
	void updateScorecard(Scorecard scorecard, String operator);
	Scorecard getScorecard(long id);
	Scorecard[] searchScorecards(Filter filter, boolean complete);
	ScorecardType[] getAllScorecardTypes();
	QuestionType[] getAllQuestionTypes();
	ScorecardStatus[] getAllScorecardStatuses();
}
