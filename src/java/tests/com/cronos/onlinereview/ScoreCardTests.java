/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.sql.Connection;

/**
 * Online review functional tests 3, scordcard managment testing.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ScoreCardTests extends ProjectTests {

    /**
     * <p>Represents review id</p>
     */
    private long reviewId;
    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {
    	super.setUp();
        Connection con = TestHelper.getConnection();
        try {
    	    // close all the phase before review phase
            TestHelper.ClosePhase(phaseIds.get("spec_submission"), con);
            TestHelper.ClosePhase(phaseIds.get("spec_review"), con);
            TestHelper.ClosePhase(phaseIds.get("registration"), con);
            TestHelper.ClosePhase(phaseIds.get("submission"), con);
            TestHelper.ClosePhase(phaseIds.get("screening"), con);
            // open review phase
            TestHelper.OpenPhase(phaseIds.get("review"), con);
            // add  submitter.
    		long Submitter = TestHelper.AddResource(projectId, 1, phaseIds.get("registration"), Long.parseLong(TestHelper.getCompetitiorUserId()), TestHelper.getCompetitorUsername(), con);
            // add reviewer.
    		long resourceId = TestHelper.AddResource(projectId, 4, phaseIds.get("review"), Long.parseLong(TestHelper.TESTS_USER_ID), TestHelper.getUsername(), con);
    		// add contest submission.
    		long submissionId = TestHelper.AddSubmission(projectId, Submitter, 1, con);

            reviewId = TestHelper.AddReview (resourceId, submissionId, con);

        } finally {
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
     * Test Case Number: FTC97 RS5.7 Verify Manager can Edit a scorecard
     *
     * @throws Exception if any error occurs
     */
    public void testEditScoreCard() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);

        //click Reivew Scorecard
        browser.click("//table[@id='myRolesTable']/tbody/tr[2]/td[2]/a");
        browser.waitForPageToLoad(TIMEOUT);
        // click Edit Scorecard
        browser.click("//div[@id='mainMiddleContent']/div[1]/div[1]/table[1]/tbody/tr[1]/td[2]/a[1]");
        browser.waitForPageToLoad(TIMEOUT);
        browser.select("answer[0]","label=1 - Strongly Disagree");
        browser.click("//input[@alt='Save Changes']");
        browser.waitForPageToLoad(TIMEOUT);

        //Verify scorecard is saved
        browser.click("//table[@id='myRolesTable']/tbody/tr[2]/td[2]/a");
        browser.waitForPageToLoad(TIMEOUT);
        assertTrue("scorecard should be saved", browser.isTextPresent("1 - Strongly Disagree"));
        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC111 RS7.1 Verify Competitor can not edit a project scorecard using a direct link
     *
     * @throws Exception if any error occurs
     */
    public void testAccessScorecard() throws Exception {
    	// login the user first
    	TestHelper.loginAsCompetitor(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getReviewURL() + reviewId);

        //Competitor have no permission to access the scorecard
        assertTrue("error occurs, access is denied", browser.isTextPresent("Attention!"));
        assertTrue("error occurs, access is denied", browser.isTextPresent("The following error has occurred:"));
        assertTrue("error occurs, access is denied", browser.isTextPresent("You don't have permission to perform the operation you requested."));

        assertNoErrorsOccurred();
    }


}
