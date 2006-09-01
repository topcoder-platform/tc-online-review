/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * <p>
 * Scenarios involving "2.7.1	Send Competitor Registration Activity":
 * </p>
 * <ul>
 * <li>Scenario #100 (FTC13,14) Open an active contest and register as a competitor.</li>
 * <li>Scenario #101 (FTC13,14) User is not logged in, due of time out.</li>
 * </ul>
 * 
 * @author brain_cn
 * @version 1.0
 */
public class ScenarioCompetitorRegistrationPage extends BaseDataTestCases {
    /** The configuration interface. */
    private static Configuration config = new Configuration(ScenarioCompetitorRegistrationPage.class.getName());
	private OnlineReviewUser user = null;

    /**
     * <p>
     * Scenario #100 (FTC13,14) Open an active contest and register as a competitor.
     * 1.	Capture Competitor Registration
     * 2.	Send Information to Online Review
     * Expected Outcome:
     * The Competitor information, design/development rating and reliability are correct.
     * </p>
     * TODO prepare expected test data
     * @throws Exception to JUnit
     */
    public void testScenario100() throws Exception {    
    	// login as competitor
    	user = new OnlineReviewUser("user_registrant");
    	user.login();
    	user.navigateToCompetitorRegistration();
    	user.registratorAsCompetitor();
    	
    	// Verify data is created
    	// one resource record, project_result record
    	String projectId = config.getProperty("project_id");
    	String userId = config.getProperty("user_id");
    	String file = config.getProperty("test_file");

    	// Verify resource
    	String tableName = "resource";
    	String sql = "select * from resource r " +
					 " inner join resource_info ri on r.resource_id = ri.resource_id " +
					 "  	and ri.resource_info_type_id = 1 and ri.value= " + userId +
					 " where project_id = " + projectId;     	
    	this.assertData(tableName, file, sql, "Incorrect resource");
    	
    	// Verify project_result
    	tableName = "project_result";
    	sql = "select * from project_result pr " +
			  " where project_id = " + projectId + " and user_id = " + userId;
    	this.assertData(tableName, file, sql, "Incorrect project_result");
    	
    	// Verify resource_info
    	// resource_info (resource_info_type_id is 1: External Reference ID)
    	// resource_info (resource_info_type_id is 4: Rating)
    	// resource_info (resource_info_type_id is 5: Reliability)
    	// resource_info (resource_info_type_id is 6: Registration Date)
    	tableName = "resource_info";
    	sql = 	"select * from resource_info " +
				"	where resource_info_type_id in {1, 4, 5, 6} " +
				"	and resource_id = " +
				"	(select resource_id from resource r" +
				" inner join resource_info ri on r.resource_id = ri.resource_id " +
				"  	and ri.resource_info_type_id = 1 and ri.value= " + userId +
				" where project_id = " + projectId + ")";
    	this.assertData(tableName, file, sql, "Incorrect resource_info");
    }

    /**
     * <p>
     * Scenario #101 (FTC13,14) User is not logged in, due of time out.
     * 1.	Capture Competitor Registration
     * 2.	Send Information to Online Review
     * Expected Outcome:
     * Error is shown indicating user must be logged in.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario101() throws Exception {    
    	// user not login
    	user = new OnlineReviewUser();
    	user.navigateToCompetitorRegistration();

    	HtmlAnchor link = this.user.getPage().getFirstAnchorByText("Register");

    	// Click Register link
    	HtmlPage page = (HtmlPage) link.click();
    	assertTrue("Error is shown indicating user must be logged in.", page.asText().indexOf("In order to continue, you must provide your user name and password.") >= 0);
    }
}
