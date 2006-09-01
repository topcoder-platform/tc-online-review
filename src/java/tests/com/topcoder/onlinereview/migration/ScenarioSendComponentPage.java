/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

/**
 * <p>
 * Scenarios involving "2.6.1	Send Component Information Activity":
 * </p>
 * <ul>
 * <li>Scenario #98 (FTC11,12) Online review must contain the component version ID.</li>
 * <li>Scenario #99 (FTC11,12) Send process did not work, user has not the correct rights.</li>
 * </ul>
 * 
 * @author brain_cn
 * @version 1.0
 */
public class ScenarioSendComponentPage extends BaseDataTestCases {
    /** The configuration interface. */
    private static Configuration config = new Configuration(ScenarioSendComponentPage.class.getName());
	private OnlineReviewUser user = null;

    /**
     * <p>
     * Scenario #98 (FTC11,12) Online review must contain the component version ID.
     * 1.	Capture Data
     * 2.	Send Data to Online Review
     * Expected Outcome:
     * Online review must contain the component version ID.
     * </p>
     * TODO prepare expected test data
     * @throws Exception to JUnit
     */
    public void testScenario98() throws Exception {    
    	// login as admin
    	user = new OnlineReviewUser("user_admin");
    	user.loginAdmin();
    	user.navigateToCreateComponent();
    	user.createComponent();

    	// Verify data is created
		String compVersId = config.getProperty("comp_vers_id");
		if (!propertyPending("comp_vers_id", compVersId)) {
			String projectTypeId = config.getProperty("project_type_id");
			String projectVersion = config.getProperty("project_version");
			long projectId = this.getProjectId(compVersId, projectTypeId, projectVersion);
			assertTrue("Failed to create project", projectId > 0);
		}
    }

    /**
     * <p>
     * Scenario #99 (FTC11,12) Send process did not work, user has not the correct rights.
     * 1.	Capture Data
     * 2.	Send Data to Online Review
     * Expected Outcome:
     * Send process did not work, user has not the correct rights.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario99() throws Exception {    
    	// user not login
    	user = new OnlineReviewUser();
    	user.navigateToCreateComponent();
    	
    	assertTrue("Send process did not work, user has not the correct rights", user.getPage().asText().indexOf(config.getProperty("no_correct_right_message")) >= 0);
    }
}
