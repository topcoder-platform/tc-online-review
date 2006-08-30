/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import junit.framework.TestCase;

/**
 * <p>
 * Scenarios involving "2.4.1	Display Scorecards from Warehouse Activity":
 * </p>
 * <ul>
 * <li>Scenario #95 (FTC9) Display Contests Detail Page</li>
 * <li>Scenario #96 (FTC9) User clicks on the active scorecard link</li>
 * </ul>
 * 
 * @author brain_cn
 * @version 1.0
 */
public class ScenarioDisplayScorecardPage extends TestCase {
	private OnlineReviewUser user = null;

    /**
     * <p>
     * Scenario #95 (FTC9) Display Contests Detail Page
     * 1.	Display Contests Detail Page
     * Expected Outcome:
     * The active scorecards are linkable
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario95() throws Exception {    	
    	user = new OnlineReviewUser();
    	user.navigateToContestDetail();
    	assertTrue("The active scorecards are linkable", user.activeScorecardLinkable());
    }

    /**
     * <p>
     * Scenario #96 (FTC9) User clicks on the active scorecard link
     * 1.	Display Contests Detail Page
     * 2.	User clicks on the active scorecard link
     * Expected Outcome:
     * The scorecard contains the following information
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario96() throws Exception {    	
    	user = new OnlineReviewUser();
    	user.navigateToContestDetail();
    	user.navigateActiveScorecard();
    	assertTrue("The scorecard contains the full information", user.containsScorecardFullInfo());
    }
}
