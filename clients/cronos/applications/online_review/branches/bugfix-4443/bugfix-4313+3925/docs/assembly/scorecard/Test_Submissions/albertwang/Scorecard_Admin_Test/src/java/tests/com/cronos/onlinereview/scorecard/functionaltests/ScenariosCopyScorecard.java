/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.scorecard.functionaltests;

import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.ScorecardSearchBundle;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.search.builder.filter.Filter;

import junit.framework.TestCase;

/**
 * <p>
 * Scenarios involving "2.2.3. Edit Scorecard Activity - Copy Scorecard":
 * </p>
 * <ul>
 * <li>Scenario #18(FTC 64)</li>
 * <li>Scenario #19(FTC 68)</li>
 * <li>Scenario #20(FTC 64, 65, 66, 70, 71)</li>
 * <li>Scenario #21(FTC 64, 65, 67, 70)</li>
 * <li>Scenario #22(FTC 64, 65, 67, 68, 69)</li>
 * <li>Scenario #23(FTC 64, 65, 67, 68, 69)</li>
 * </ul>
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ScenariosCopyScorecard extends TestCase {
    /** The user to test with. */
    private OnlineReviewUser user = null;
    /**
     * Clear the DB.
     * @throws Exception to JUnit
     */
    public void tearDown() throws Exception {
    	DatabaseUtility.getInstance().clearTables();
    }
    /**
     * <p>
     * Scenario #18 (FTC 64):
     * 1. User navigates to scorecard list
     * Expected Outcome
	 * Validation error is shown indicating user does NOT permission to View scorecard list.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario18() throws Exception {
/*
    	// initialize a user, cannot view scorecard list
        user = new OnlineReviewUser(OnlineReviewUser.USER_CANNOT_VIEW_SCORECARD_LIST);
        // login
        user.login();
        
        String page = user.getPageAsText();
        // the user should get permission warning
        assertTrue("User does not see the permission warning.",
        		page.indexOf(Messages.getNoPermissionScorecardList()) > 0);
*/
    }
    
    /**
     * <p>
     * Scenario #19 (FTC 68):
     * 1. User navigates to scorecard list
	 * 2. User clicks on scorecard
	 * 3. User clicks on "Copy"
	 * 4. User clicks on "Save New Scorecard"
	 * Expected Outcome:
	 * Validation error is shown indicating scorecard is currently linked to a project and name
	 * must be changed.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario19() throws Exception {
    	// initialize a user, manager
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        // click scorecard "Design Screening Scorecard v1.1"
        user.navigateTo("Design Screening Scorecard v1.1");
        
        // click "Copy" button
        user.clickCopyScorecard();
        
        // click "Save New Scorecard" button
        user.clickSaveNewScorecard();

        String page = user.getPageAsText();
        // the user should get warning
        assertTrue("User does not see the warning.",
        		page.indexOf(Messages.getScorecardInUseCopyError()) > 0);
    }
    
    /**
     * <p>
     * Scenario #20 (FTC 64, 65, 66, 70, 71):
     * Note: User is logged-in as a "Manager"
	 * 1. User navigates to scorecard list
	 * 2. User clicks on scorecard
	 * 3. User clicks on "Copy"
	 * 5. User clicks on "Save New Scorecard"
	 * A new Scorecard is created and saved to the database. Major and minor version
	 * numbers is incremented.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario20() throws Exception {
    	// initialize a user, manager
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        // click scorecard "Development Review Scorecard v1.2"
        user.navigateTo("Development Review Scorecard v1.2");
        
        // click "Copy" button
        user.clickCopyScorecard();
        
        // click "Save New Scorecard" button
        user.clickSaveNewScorecard();
        
        ScorecardManager mgr = DatabaseUtility.getInstance().getScorecardManager();
        Filter filter = ScorecardSearchBundle.buildVersionEqualFilter("1.2");
        filter = ScorecardSearchBundle.buildNotFilter(filter);
        filter = ScorecardSearchBundle.buildAndFilter(filter, ScorecardSearchBundle.buildNameEqualFilter("Development Review Scorecard"));
        
        Scorecard[] scorecards = mgr.searchScorecards(filter, true);
        assertTrue("There should be one copy.", scorecards.length > 0);
    }
    
    /**
     * <p>
     * Scenario #21 (FTC 64, 65, 67, 70):
     * Note: User is logged-in as a "Manager"
	 * 1. User navigates to scorecard list
	 * 2. User clicks on scorecard
	 * 3. User clicks on "Copy"
	 * 4. User changes Scorecard Name and Version Number using Sample Data be:
	 * 5. User clicks on "Save New Scorecard"
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario21() throws Exception {
    	// initialize a user, manager
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        // click scorecard "Development Review Scorecard v1.2"
        user.navigateTo("Development Review Scorecard v1.2");
        
        // click "Copy" button
        user.clickCopyScorecard();
        
        // edit scorecard name and version
        user.fillScorecardNameAndVersion("test_scorecardcopy01", "1.0");
        // click "Save New Scorecard" button
        user.clickSaveNewScorecard();
        
        ScorecardManager mgr = DatabaseUtility.getInstance().getScorecardManager();
        Filter filter = ScorecardSearchBundle.buildNameEqualFilter("test_scorecardcopy01");
        
        Scorecard[] scorecards = mgr.searchScorecards(filter, true);
        assertTrue("There should be one copy.", scorecards.length > 0);
    }
    
    /**
     * <p>
     * Scenario #22 (FTC 64, 65, 67, 68, 69):
     * Note: User is logged-in as a "Manager"
	 * 1. User navigates to scorecard list
	 * 2. User clicks on scorecard
	 * 3. User clicks on "Copy"
	 * 4. User completes Scorecard Details using sample data below
	 * 5. In the Group Section, Click "Add New"
	 * 6. Enter sample data listed below in "New Group Section"
	 * 7. Click on "Save New Scorecard"
	 * Expected Outcome:
	 * Validation error is shown indicating Group Weights DO NOT Sum to 100.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario22() throws Exception {
    	// initialize a user, manager
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        // click scorecard "Development Review Scorecard v1.2"
        user.navigateTo("Development Review Scorecard v1.2");
        
        // click "Copy" button
        user.clickCopyScorecard();
        
        // edit group name and weight
        user.fillGroup(0, "Group#1_edited", 10); // weights don't sum up to 100
        // click "Save New Scorecard" button
        user.clickSaveNewScorecard();
        
        String page = user.getPageAsText();
        // the user should get warning
        assertTrue("User does not see the warning.",
        		page.indexOf(Messages.getValidationErrorIncorrectGroupWeights()) > 0);
    }
    
    /**
     * <p>
     * Scenario #23 (FTC 64, 65, 67, 68, 69):
     * Note: User is logged-in as a "Manager"
	 * 1. User navigates to scorecard list
	 * 2. User clicks on scorecard
	 * 3. User clicks on "Copy"
	 * 4. User completes Scorecard Details using sample data below
	 * 5. In the Group Section, Click "Add New"
	 * 6. Enter sample data listed below in "New Group Section"
	 * 7. Click on "Save New Scorecard"
	 * Expected Outcome:
	 * Validation error is shown indicating Section Weights DO NOT Sum to 100
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario23() throws Exception {
    	// initialize a user, manager
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        // click scorecard "Development Review Scorecard v1.2"
        user.navigateTo("Development Review Scorecard v1.2");
        
        // click "Copy" button
        user.clickCopyScorecard();
        
        // edit section name and weight
        user.fillSection(0, 0, "Section#1.1_edited", 20); // weights don't sum up to 100
        // click "Save New Scorecard" button
        user.clickSaveNewScorecard();
        
        String page = user.getPageAsText();
        // the user should get warning
        assertTrue("User does not see the warning.",
        		page.indexOf(Messages.getValidationErrorIncorrectSectionWeights()) > 0);
    }
}
