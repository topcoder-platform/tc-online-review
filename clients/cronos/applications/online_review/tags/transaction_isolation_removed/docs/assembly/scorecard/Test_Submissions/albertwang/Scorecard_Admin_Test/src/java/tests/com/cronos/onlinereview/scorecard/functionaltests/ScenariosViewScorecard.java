/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.scorecard.functionaltests;

import junit.framework.TestCase;

/**
 * <p>
 * Scenarios involving "2.3.1 View Scorecard Activity":
 * </p>
 * <ul>
 * <li>Scenario #24(FTC 72)</li>
 * <li>Scenario #25(FTC 72)</li>
 * <li>Scenario #26(FTC 73)</li>
 * <li>Scenario #27(FTC 74, 75)</li>
 * </ul>
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ScenariosViewScorecard extends TestCase {
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
     * Scenario #24 (FTC 72):
     * PC. The user has no permission to view the scorecard list.
     * 1. User navigates to scorecard list
     * Expected Outcome:
     * Validation error is shown indicating user has NOT permission to view scorecard list.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario24() throws Exception {
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
     * Scenario #25 (FTC 72)
     * PC. The user has no permission to view the scorecard.
     * 1. User navigates to scorecard list 
     * 2. User clicks on scorecard
     * Expected Outcome:
     * Validation error is shown indicating user has NOT permission to view scorecard.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario25() throws Exception {
/*
    	// initialize a user, can not view scorecard
        user = new OnlineReviewUser(OnlineReviewUser.USER_CANNOT_VIEW_SCORECARD);
        // login
        user.login();
        // click scorecard "Design Screening Scorecard v1.1"
        user.navigateTo("Design Screening Scorecard v1.1");
        
        String page = user.getPageAsText();
        // the user should get permission warning
        assertTrue("User does not see the permission warning.",
        		page.indexOf(Messages.getNoPermissionScorecard()) > 0);
*/
    }
    
    /**
     * <p>
     * Scenario #26 (FTC 73)
     * PC. User is logged-in as a "Manager"
     * 1. User navigates to scorecard list
     * 2. User filters scorecard by "Active Only"
     * Expected Outcome
     * Only active scorecards are displayed in scorecard list.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario26() throws Exception {

    	// initialize a user, a manager
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();

        // filter the scorecard by "Active Only"
        user.filterScorecardListByStatus("Active Only");
        // extract the scorecard list
        String[][] list = user.getScorecardList();
        // verify the scorecard list, in the test DB, there's only one "Active" scorecard
        // Design Screening Scorecard v1.1
        assertEquals("The user sees more than one scorecard.", 1, list.length);
        assertEquals("The user doesn't see the correct scorecard name.", "Design Screening Scorecard v 1.1", list[0][0]);
        assertEquals("The user doesn't see the correct scorecard type.", "Screening", list[0][1]);
        assertEquals("The user doesn't see the correct scorecard status.", "checked", list[0][2]);
    }
    
    /**
     * <p>
     * Scenario #27 (FTC 74, 75)
     * PC. User is logged-in as a "Manager"
     * 1. User navigates to scorecard list
     * 2. User clicks on scorecard
     * Expected Outcome
     * User is able to view Scorecard in Read-Only Format
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario27() throws Exception {
    	// initialize a user, a manager
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        // click scorecard "Design Screening Scorecard v1.1"
        user.navigateTo("Design Screening Scorecard v1.1");
        
        // extract scorecard details(summary section)
        String[] summary = user.getScorecardSummary();
        // verify the scorecard details
        assertEquals("The user sees incorrect scorecard id.", "888", summary[0]);
        assertEquals("The user sees incorrect scorecard name.", "Design Screening Scorecard v 1.1", summary[1]);
        assertEquals("The user sees incorrect scorecard project type.", "Design", summary[2]);
        assertEquals("The user sees incorrect scorecard category.", "Component", summary[3]);
        assertEquals("The user sees incorrect scorecard type.", "Screening", summary[4]);
        assertEquals("The user sees incorrect scorecard Min. Score.", "75", summary[5]);
        assertEquals("The user sees incorrect scorecard Max. Score.", "100", summary[6]);
        assertEquals("The user sees incorrect scorecard Status.", "Active", summary[7]);
    }
}
