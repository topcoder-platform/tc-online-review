/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

/**
 * <p>
 * Scenarios involving "2.8.1	Send Reviewer Registration Activity":
 * </p>
 * <ul>
 * <li>Scenario #102 (FTC15,16) Open an active contest and register as a reviewer.</li>
 * </ul>
 * 
 * @author brain_cn
 * @version 1.0
 */
public class ScenarioReviewerRegistrationPage extends BaseDataTestCases {
    /** The configuration interface. */
    private static Configuration config = new Configuration(ScenarioReviewerRegistrationPage.class.getName());
	private OnlineReviewUser user = null;

    /**
     * <p>
     * Scenario #102 (FTC15,16) Open an active contest and register as a reviewer.
     * 1.	Capture Reviewer Registration Data
     * 2.	Send Data to Online Review
     * Expected Outcome:
     * The Competitor information is displayed in the online review including the design/development 
     * rating and reliability.
     * </p>
     * TODO prepare test expected data
     * @throws Exception to JUnit
     */
    public void testScenario102() throws Exception {    	
    	user = new OnlineReviewUser("user_reviewer");
    	user.login();
    	user.navigateToReviewerRegistration();
    	user.applyReviewer();

    	// Verify data is created
    	// one resource record, project_result record
    	String projectId = config.getProperty("project_id");
    	String userId = config.getProperty("user_id");
    	String phaseId = config.getProperty("phase_id");
    	String file = config.getProperty("test_file");

    	// Verify resource
    	String tableName = "resource";
    	String sql = "select * from resource r " +
					 " inner join resource_info ri on r.resource_id = ri.resource_id " +
					 "  	and ri.resource_info_type_id = 1 and ri.value= " + userId +
					 " where project_id = " + projectId;  	
    	this.assertData(tableName, file, sql, "Incorrect resource");
    	
    	// Verify rboard_application
    	tableName = "rboard_application";
    	sql = "select * from rboard_application " +
			 " where project_id = " + projectId + " " +
			 "	and phase_id = " + phaseId + " " +
			 "	and user_id = " + userId + " "; 
    	this.assertData(tableName, file, sql, "Incorrect rboard_application");
    	
    	// Verify resource_info
    	// resource_info (resource_info_type_id is 1: External Reference ID)
    	// resource_info (resource_info_type_id is 6: Registration Date)
    	tableName = "resource_info";
    	sql = 	"select * from resource_info " +
				"	where resource_info_type_id in {1, 6} " +
				"	and resource_id in " +
				"	(select resource_id from resource r" +
				" inner join resource_info ri on r.resource_id = ri.resource_id " +
				"  	and ri.resource_info_type_id = 1 and ri.value= " + userId +
				" where project_id = " + projectId + ")";
    	this.assertData(tableName, file, sql, "Incorrect resource_info");
    }
}
