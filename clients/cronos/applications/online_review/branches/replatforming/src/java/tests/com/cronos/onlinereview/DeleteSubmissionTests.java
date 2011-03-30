/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.sql.Connection;

/**
 * Online review functional tests 3, manager can delete a submission testing.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class DeleteSubmissionTests extends ProjectTests {

    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {
    	super.setUp();
        Connection con = TestHelper.getConnection();
        try {
    	    // close all the phase before submission
            TestHelper.ClosePhase(phaseIds.get("spec_submission"), con);
            TestHelper.ClosePhase(phaseIds.get("spec_review"), con);
            TestHelper.ClosePhase(phaseIds.get("registration"), con);
            // open submission phase
            TestHelper.OpenPhase(phaseIds.get("submission"), con);
    		// add  submitter.
    		long Submitter = TestHelper.AddResource(projectId, 1, phaseIds.get("registration"), Long.parseLong(TestHelper.getCompetitiorUserId()), TestHelper.getCompetitorUsername(), con);
    		// add contest submission.
    		TestHelper.AddSubmission(projectId, Submitter, 1, con);
        } finally {
    		con.close();
    	}
    }
	
    /**
     * Test Case Number: FTC106 RS5.11 Verify Manager can delete a submission
     *
     * @throws Exception if any error occurs
     */
    public void testDeleteSubmission() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);

        //verify submission is uploaded
        browser.click("//div[@id='sc2']/ul/li[2]/a");
        String submissionId = browser.getText("//table[@id='Submissions0']/tbody/tr[3]/td[1]/a[1]");
        assertNotNull("Submission should be uploaded",submissionId);

        // Click the 'Edit Project' Link
        browser.click("//img[@alt='Edit Project']");
        browser.waitForPageToLoad(TIMEOUT);
        // open Submission/Screeen
        browser.click("//div[@id='sc3']/ul/li[2]/a");
        // delete the submission
        browser.click("//img[@alt='Remove this Submission']");
        browser.waitForPageToLoad(TIMEOUT);
        browser.click("//img[@alt='Delete Submission']");
        browser.waitForPageToLoad(TIMEOUT);

        //verify submission is deleted
        String deliverableString = browser.getText("//table[@id='myRolesTable']/tbody/tr[2]/td[3]/b[2]");
        assertEquals("Submission should be deleted","Submission" ,deliverableString);
        assertNoErrorsOccurred();
    }


}
