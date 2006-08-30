/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import junit.framework.TestCase;

/**
 * <p>
 * Scenarios involving "2.2.1	Display Active Contests Page Activity":
 * </p>
 * <ul>
 * <li>Scenario #69  (FTC 6) Display Active Contests Page</li>
 * <li>Scenario #70  (FTC 6) The images for the catalog category are missing</li>
 * <li>Scenario #71  (FTC 6) The component names are missing</li>
 * <li>Scenario #72  (FTC 6) There is no number of registrant displayed</li>
 * <li>Scenario #73  (FTC 6) There is no number of unrated registrant displayed</li>
 * <li>Scenario #74  (FTC 6) There is no number entry in the End date fro registration</li>
 * <li>Scenario #75  (FTC 6) There is a negative number or no number of submissions displayed</li>
 * <li>Scenario #76  (FTC 6) The $ is missing in the payment column</li>
 * <li>Scenario #77  (FTC 6) There are no entries in the Last day to submit column</li>
 * <li>Scenario #78  (FTC 6) User clicks on Component Name of one active contest</li>
 * <li>Scenario #79  (FTC 6) User clicks on Registrants of one active contest</li>
 * <li>Scenario #80  (FTC 6) Component Name is not blue.</li>
 * <li>Scenario #81  (FTC 6) Registrants are not shown blue</li>
 * </ul>
 * 
 * @author brain_cn
 * @version 1.0
 */
public class ScenarioDisplayActivePage extends TestCase {
	private OnlineReviewUser user = null;

    /**
     * <p>
     * Scenario #69  (FTC 6) Display Active Contests Page
     * 1.	Display Active Contests Page
     * Expected Outcome:
     * The active Contest Page will be shown correctly.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario69() throws Exception {    	
    	user = new OnlineReviewUser();
    	user.navigateToActiveContest();
    	assertTrue("The active Contest Page will be shown correctly", user.displayActiveContest());
    }

    /* 
    * Notes: Failure test cases Scenario #70 - #77 and #80 - #81 does not make sense since this task only will
    * update retrieve data sql statement.
    */
    
    /**
     * <p>
     * Scenario #78  (FTC 6) User clicks on Component Name of one active contest
     * 1.	Display Active Contests Page
     * 2.	User clicks on Component Name of one active contest
     * Expected Outcome:
     * The link will direct user to component overview page
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario78() throws Exception {    	
    	user = new OnlineReviewUser();
    	user.navigateToActiveContest();
    	assertTrue("Failed to direct user to Component overview page.", user.canNavigateComponentOverview()); 
    }
    
    /**
     * <p>
     * Scenario #79  (FTC 6) User clicks on Registrants of one active contest
     * 1.	Display Active Contests Page
     * 2.	User clicks on Component Name of one active contest
     * Expected Outcome:
     * The link will direct user to ViewRegistrant  page
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario79() throws Exception {    	
    	user = new OnlineReviewUser();
    	user.navigateToActiveContest();
    	assertTrue("Failed to direct user to ViewRegistrant page.", user.canNavigateViewRegistrantFromActiveContestPage()); 
    }
}
