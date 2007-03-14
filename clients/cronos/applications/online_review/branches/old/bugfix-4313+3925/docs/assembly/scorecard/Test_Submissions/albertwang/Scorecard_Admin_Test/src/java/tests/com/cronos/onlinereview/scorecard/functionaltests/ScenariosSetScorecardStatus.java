/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.scorecard.functionaltests;


import junit.framework.TestCase;

/**
 * <p>
 * Scenarios involving "2.2.2. Edit Scorecard Activity - Set Status":
 * </p>
 * <ul>
 * <li>Scenario #15(FTC 61)</li>
 * <li>Scenario #16(FTC 63)</li>
 * <li>Scenario #17(FTC 62)</li>
 * </ul>
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ScenariosSetScorecardStatus extends TestCase {
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
     * Scenario #15 (FTC 61)
     * 1. User navigates to scorecard list
     * Expected Outcome
     * Validation error is shown indicating user does NOT permission to View scorecard list.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario15() throws Exception {
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
     * Scenario #16 (FTC 63)
     * 1. User navigates to scorecard list
     * 2. User changes status "Design Screening Scorecard v1.1" from Active to Inactive
     * Expected Outcome
     * Scorecard status is changed from Active to Inactive.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario16() throws Exception {
    	// initialize a user, manager
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        
        // change scorecard "Design Screening Scorecard v1.1" from active to inactive
        user.setScorecardStatus("Design Screening Scorecard v1.1", false);
        
        // verify the status
        assertFalse("The scorecard has not been set to inactive.", user.getScorecardStatus("Design Screening Scorecard v1.1"));
    }
    
    /**
     * <p>
     * Scenario #17 (FTC 62)
     * 1. User navigates to scorecard list
     * 2. User changes status of "Development Review Scorecard v1.2" from Inactive to Active
     * Expected Outcome
     * Scorecard status is changes from Inactive to Active.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario17() throws Exception {
    	// initialize a user, manager
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        
        // change scorecard "Development Review Scorecard v1.2" from inactive to active
        user.setScorecardStatus("Development Review Scorecard v1.2", true);
        
        // verify the status
        assertTrue("The scorecard has not been set to active.", user.getScorecardStatus("Development Review Scorecard v1.2"));
    }
}
