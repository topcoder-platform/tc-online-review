/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.sql.Connection;

/**
 * Online review functional tests 3, delete reviewer testing.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class DeleteReviewerTests extends ProjectTests {
	
    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {
    	super.setUp();
    	Connection con = TestHelper.getConnection();
    	try {
    		// add reviewer.
    		TestHelper.AddResource(projectId, 4, phaseIds.get("review"), Long.parseLong(TestHelper.getCompetitiorUserId()), TestHelper.getCompetitorUsername(), con);
    	}
    	finally {
    		con.close();
    	}
    }

    /**
     * Tears down the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void tearDown() throws Exception {
    	TestHelper.reloginAndDeleteProject(browser, projectId);
        projectId = -1;
        super.tearDown();
    }

    /**
     * Test Case Number: FTC80 RS5.6 Verify Manager can Delete a reviewer Role to a project
     *
     * @throws Exception if any error occurs
     */
    public void testDeleteReviewerRole() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        // Click the 'Edit Project' Link
        browser.click("//img[@alt='Edit Project']");
        browser.waitForPageToLoad(TIMEOUT);

        // Delete the reviewer
        browser.click("//table[@id='resources_tbl']/tbody/tr[4]/td[5]/img[1]");
        // add explanation
        browser.type("explanation", "delete reviewer role");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);

        // logout the user
        browser.click("link=Logout");
        browser.waitForPageToLoad(TIMEOUT);

        //login as competitor
        TestHelper.loginAsCompetitor(browser);
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        //Competitor have no permission to this project
        assertTrue("error occurs, access is denied", browser.isTextPresent("Attention!"));
        assertTrue("error occurs, access is denied", browser.isTextPresent("The following error has occurred:"));
        assertTrue("error occurs, access is denied", browser.isTextPresent("You don't have permission to perform the operation you requested."));
        assertNoErrorsOccurred();
    }
}
