/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.sql.Connection;


/**
 * Online review functional tests 3, competitor privilege testing.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class CompetitorPrivilegeTests extends ProjectTests {

    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {
    	super.setUp();
        Connection con = TestHelper.getConnection();
    	try {
    		// open register phase
            TestHelper.OpenPhase(phaseIds.get("registration"), con);
            // competitor register the project
            TestHelper.AddResource(projectId, 1, phaseIds.get("registration"), Long.parseLong(TestHelper.getCompetitiorUserId()), TestHelper.getCompetitorUsername(), con);

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
     * Test Case Number: FTC110 RS7.1 Verify Competitor can not access a project if he is not registered in it
     *
     * @throws Exception if any error occurs
     */
    public void testCompetitorCannotAcessProject() throws Exception {
        // competitor urregister the project
        TestHelper.unregisterProject(browser, projectId);

        // login as competitor
        TestHelper.loginAsCompetitor(browser);
        // open the project details page
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        // Verify Competitor can not access the project
        assertTrue("error occurs, access is denied", browser.isTextPresent("Attention!"));
        assertTrue("error occurs, access is denied", browser.isTextPresent("The following error has occurred:"));
        assertTrue("error occurs, access is denied", browser.isTextPresent("You don't have permission to perform the operation you requested."));
        assertNoErrorsOccurred();
    }

    




}
