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
 * <li>Scenario #82 (FTC 7) Display Contests Status</li>
 * <li>Scenario #83 (FTC 7) User clicks on a contest name</li>
 * <li>Scenario #84 (FTC 7) User clicks on a component detail link</li>
 * <li>Scenario #85 (FTC 7) User clicks on a registrant of one contest</li>
 * <li>Scenario #86 (FTC 7) User clicks on Winner of one contest</li>
 * <li>Scenario #87 (FTC 7) User clicks on Second place of one contest</li>
 * <li>Scenario #88 (FTC 7) User clicks on Table header Catalog</li>
 * <li>Scenario #89 (FTC 7) User clicks on Table header Contest</li>
 * <li>Scenario #90 (FTC 7) User clicks on Table header Submission Due Date</li>
 * <li>Scenario #91 (FTC 7) User clicks on Table header Final Review Due Date</li>
 * <li>Scenario #92 (FTC 7) User clicks on Table header Winner</li>
 * <li>Scenario #93 (FTC 7) User clicks on Table header Second Place</li>
 * <li>Scenario #94 (FTC 7) User clicks on Table header Second Place</li>
 * </ul>
 * @author brain_cn
 * @version 1.0
 */
public class ScenarioDisplayContestStatusPage extends TestCase {
    private static final int CATALOG_INDEX = 0;
    private static final int CONTEST_INDEX = 1;
    private static final int FINAL_REVIEW_DUE_DATE_INDEX = 4;
    private static final int WINNER_INDEX = 6;
    private static final int SECOND_INDEX = 7;
	private OnlineReviewUser user = null;

    /**
     * <p>
     * Scenario #82 (FTC 7) Display Contests Status
     * 1.	Display Contests Status
     * Expected Outcome:
     * The Contest Status page will be shown properly.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario82() throws Exception {    	
    	user = new OnlineReviewUser();
    	user.navigateToContestStatus();
    	assertTrue("Failed to display contest status", user.hasContestStatusList());
    }

    /**
     * <p>
     * Scenario #83 (FTC 7) User clicks on a contest name
     * 1.	Display Contests Status
     * 2. 	User clicks on a contest name
     * Expected Outcome:
     * The link will direct user to the Project Detail page.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario83() throws Exception {     	
    	user = new OnlineReviewUser();
    	user.navigateToContestStatus();
    	assertTrue("Failed to direct user to the Project Detail page.", user.canNavigateContest());
    }
    
    /**
     * <p>
     * Scenario #84 (FTC 7) User clicks on a component detail link
     * 1.	Display Contests Status
     * 2. 	User clicks on a component detail link
     * Expected Outcome:
     * The link will direct user to Component page.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario84() throws Exception {   
    	user = new OnlineReviewUser();
    	user.navigateToContestStatus();
    	assertTrue("Failed to direct user to Component page.", user.canNavigatorComponentDetail()); 	
    }

    /**
     * <p>
     * Scenario #85 (FTC 7) User clicks on a registrant of one contest
     * 1.	Display Contests Status
     * 2. 	User clicks on a registrant of one contest
     * Expected Outcome:
     * The link will direct user to the ViewRegistrant page.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario85() throws Exception {   
    	user = new OnlineReviewUser();
    	user.navigateToContestStatus();
    	assertTrue("Failed to direct user to the ViewRegistrant page.", user.canNavigateViewRegistrant()); 	 	
    }

    /**
     * <p>
     * Scenario #86 (FTC 7) User clicks on Winner of one contest
     * 1.	Display Contests Status
     * 2. 	User clicks on Winner of one contest
     * Expected Outcome:
     * The link will direct user to the MemberProfile page.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario86() throws Exception {   
    	user = new OnlineReviewUser();
    	user.navigateToContestStatus();
    	assertTrue("Failed to direct user to the MemberProfile page.", user.canNavigateMemberProfile(true)); 	  	
    }

    /**
     * <p>
     * Scenario #87 (FTC 7) User clicks on Second place of one contest
     * 1.	Display Contests Status
     * 2. 	User clicks on Second place of one contest
     * Expected Outcome:
     * The link will direct user to the MemberProfile page.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario87() throws Exception {  
    	user = new OnlineReviewUser();
    	user.navigateToContestStatus();
    	assertTrue("Failed to direct user to the MemberProfile page.", user.canNavigateMemberProfile(false)); 	  	
    }

    /**
     * <p>
     * Scenario #88 (FTC 7) User clicks on Table header Catalog
     * 1.	Display Contests Status
     * 2. 	User clicks on Table header Winner
     * Expected Outcome:
     * The Contest Status page will be sorted, in ascending order, by Catalog
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario88() throws Exception {    
    	user = new OnlineReviewUser();
    	user.navigateToContestStatus();
    	assertTrue("The Contest Status page will be sorted, in ascending order, by Catalog", user.isSort(true, CATALOG_INDEX)); 
    }

    /**
     * <p>
     * Scenario #89 (FTC 7) User clicks on Table header Contest
     * 1.	Display Contests Status
     * 2. 	User clicks on Table header Winner
     * Expected Outcome:
     * The Contest Status page will be sorted, in ascending order, by Contest
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario89() throws Exception {    
    	user = new OnlineReviewUser();
    	user.navigateToContestStatus();
    	assertTrue("The Contest Status page will be sorted, in ascending order, by Contest", user.isSort(true, CONTEST_INDEX)); 	
    }

    /**
     * <p>
     * Scenario #90 (FTC 7) User clicks on Table header Final Review Due Date
     * 1.	Display Contests Status
     * 2. 	User clicks on Table header Winner
     * Expected Outcome:
     * The Contest Status page will be sorted, in ascending order, by Final Review Due Date
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario90() throws Exception {    	
    	user = new OnlineReviewUser();
    	user.navigateToContestStatus();
    	assertTrue("The Contest Status page will be sorted, in ascending order, by Final Review Due Date", 
    			user.isSort(true, FINAL_REVIEW_DUE_DATE_INDEX)); 
    }

    /**
     * <p>
     * Scenario #91 (FTC 7) User clicks on Table header Winner
     * 1.	Display Contests Status
     * 2. 	User clicks on Table header Winner
     * Expected Outcome:
     * The Contest Status page will be sorted, in ascending order, by Winner
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario91() throws Exception {    	
    	user = new OnlineReviewUser();
    	user.navigateToContestStatus();
    	assertTrue("The Contest Status page will be sorted, in ascending order, by Winner", user.isSort(true, WINNER_INDEX)); 
 	
    }

    /**
     * <p>
     * Scenario #92 (FTC 7) User clicks on Table header Second Place
     * 1.	Display Contests Status
     * 2. 	User clicks on Table header Second Place
     * Expected Outcome:
     * The Contest Status page will be sorted, in ascending order, by Second Place
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario92() throws Exception {    	
    	user = new OnlineReviewUser();
    	user.navigateToContestStatus();
    	assertTrue("The Contest Status page will be sorted, in ascending order, by Second Place", user.isSort(true, SECOND_INDEX)); 
    }

    /**
     * <p>
     * Scenario #94 (FTC 7) User clicks on Table header Second Place
     * 1.	Display Contests Status
     * 2. 	User clicks on Table header Second Place
     * 3. 	User clicks on Table header Second Place
     * Expected Outcome:
     * The Contest Status page will be sorted, in descending order, by Second Place
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario93() throws Exception {    	
    	user = new OnlineReviewUser();
    	user.navigateToContestStatus();
    	user.sortBy(SECOND_INDEX);
    	assertTrue("The Contest Status page will be sorted, in ascending order, by Second Place", user.isSort(false, SECOND_INDEX)); 
    }

}
