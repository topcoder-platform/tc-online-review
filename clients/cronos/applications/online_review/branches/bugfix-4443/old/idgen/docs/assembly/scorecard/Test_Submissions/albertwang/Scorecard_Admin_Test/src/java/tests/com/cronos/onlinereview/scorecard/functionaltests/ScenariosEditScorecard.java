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
 * Scenarios involving "2.2.1. Edit Scorecard Activity":
 * </p>
 * <ul>
 * <li>Scenario #9(FTC 55)</li>
 * <li>Scenario #10(FTC 55)</li>
 * <li>Scenario #11(FTC 55)</li>
 * <li>Scenario #12(FTC 55, 56, 57, 59, 60)</li>
 * <li>Scenario #13(FTC 58)</li>
 * <li>Scenario #14(FTC 58)</li>
 * </ul>
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ScenariosEditScorecard extends TestCase {
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
     * Scenario #9 (FTC 55):
     * 1. User selects scorecard to edit
	 * 2. User clicks on Edit Scorecard
	 * Expected Outcome:
	 * Validation Error is show indicating user does not have permission to edit a scorecard.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario9() throws Exception {
/*
    	// initialize a user, can not edit scorecard
        user = new OnlineReviewUser(OnlineReviewUser.USER_CANNOT_EDIT_SCORECARD);
        // login
        user.login();
        // click scorecard "Development Review Scorecard v1.2"
        user.navigateTo("Development Review Scorecard v1.2");
        
        // click "Edit" button
        user.clickEditScorecard();

        String page = user.getPageAsText();
        // the user should get permission warning
        assertTrue("User does not see the permission warning.",
        		page.indexOf(Messages.getNoPermissionEditScorecard()) > 0);
*/
    }
    
    /**
     * <p>
     * Scenario #10 (FTC 55):
     * Note: User is logged-in as a "Manager"
	 * 1. User clicks on scorecard to edit
	 * 2. User clicks on Edit Scorecard
	 * Expected Outcome:
	 * Validation Error is show indicating user scorecard is currently linked to a project and
	 * cannot be edited.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario10() throws Exception {
/*
    	// initialize a user, manager
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        // click scorecard "Design Screening Scorecard v1.1"
        user.navigateTo("Design Screening Scorecard v1.1");
        
        // click "Edit" button
        user.clickEditScorecard();

        String page = user.getPageAsText();
        // the user should get permission warning
        assertTrue("User does not see the warning.",
        		page.indexOf(Messages.getActiveScorecardEditError()) > 0);
*/
    }
    
    /**
     * <p>
     * Scenario #11 (FTC 55):
     * Note: User is logged-in as a "Manager"
	 * 1. User clicks on scorecard to edit
	 * 2. User clicks on Edit Scorecard
	 * 3. User attempts to edit scorecard name and version number
	 * Expected Outcome:
	 * Scorecard name and version number are in read-only format and cannot be edited.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario11() throws Exception {

    	// initialize a user, manager
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        // click scorecard "Development Review Scorecard v1.2"
        user.navigateTo("Development Review Scorecard v1.2");
        
        // click "Edit" button
        user.clickEditScorecard();
        
        // verify the scorecard name & version are disabled
        assertTrue("The scorecard name is not disabled.", user.isScorecardNameInputDisabled());
        assertTrue("The scorecard version is not disabled.", user.isScorecardVersionInputDisabled());

    }
    
    /**
     * <p>
     * Scenario #12 (FTC 55, 56, 57, 59, 60)):
     * Note: User is logged-in as a "Manager"
	 * 1. User clicks on scorecard to edit
	 * 2. User clicks on Edit Scorecard
	 * 3. User changes scorecard using sample data below:
	 * Expected Outcome:
	 * Scorecard has been updated and will be saved to the database. Minor version number
	 * will be incremented.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario12() throws Exception {

    	// initialize a user, manager
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        // click scorecard "Development Review Scorecard v1.2"
        user.navigateTo("Development Review Scorecard v1.2");
        
        // click "Edit" button
        user.clickEditScorecard();
        
        // edit group name
        user.fillGroup(0, "Group#1_edited", 30);

        // save the changes
        user.clickSaveChanges();
        
        ScorecardManager mgr = DatabaseUtility.getInstance().getScorecardManager();
        Filter filter = ScorecardSearchBundle.buildVersionEqualFilter("1.2");
        filter = ScorecardSearchBundle.buildNotFilter(filter);
        filter = ScorecardSearchBundle.buildAndFilter(filter, ScorecardSearchBundle.buildNameEqualFilter("Development Review Scorecard"));
        
        Scorecard[] scorecards = mgr.searchScorecards(filter, true);
        assertTrue("The version should be updated.", scorecards.length > 0);

    }
    
    /**
     * <p>
     * Scenario #13 (FTC 58):
     * Note: User is logged-in as a "Manager"
	 * 1. User clicks on scorecard to edit
	 * 2. User clicks on Edit Scorecard
	 * 3. User changes scorecard using sample data below:
	 * Expected Outcome:
	 * Validation error is shown indicating Required Fields are MISSING.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario13() throws Exception {

    	// initialize a user, manager
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        // click scorecard "Development Review Scorecard v1.2"
        user.navigateTo("Development Review Scorecard v1.2");
        
        // click "Edit" button
        user.clickEditScorecard();
        
        // edit group name
        user.fillGroup(0, "", 30); // the group is missing

        // save the changes
        user.clickSaveChanges();
        
        String page = user.getPageAsText();
        // the user should get warning
        assertTrue("User does not see the warning.",
        		page.indexOf(Messages.getValidationErrorFieldMissing()) > 0);
    }
    
    /**
     * <p>
     * Scenario #14 (FTC 58):
     * Note: User is logged-in as a "Manager"
	 * 1. User clicks on scorecard to edit
	 * 2. User clicks on Edit Scorecard
	 * 3. User changes scorecard using sample data below:
	 * Expected Outcome:
	 * Validation error is shown indicating WEIGHTS MUST SUM to 100.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario14() throws Exception {
    	// initialize a user, manager
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        // click scorecard "Development Review Scorecard v1.2"
        user.navigateTo("Development Review Scorecard v1.2");
        
        // click "Edit" button
        user.clickEditScorecard();
        
        // edit group name
        user.fillGroup(0, "Group#1_edited", 10); // weights don't sum up to 100

        // save the changes
        user.clickSaveChanges();
        
        String page = user.getPageAsText();
        // the user should get warning
        assertTrue("User does not see the warning.",
        		page.indexOf(Messages.getValidationErrorIncorrectGroupWeights()) > 0);
    }
}
