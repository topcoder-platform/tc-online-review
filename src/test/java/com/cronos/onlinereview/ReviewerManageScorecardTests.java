/*
 * Copyright (C) 2011 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.sql.Connection;


/**
 * Online review functional tests 4, testing reviewer manage the scorecard.
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ReviewerManageScorecardTests extends ProjectTests {

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
            long submitterId = TestHelper.AddResource(projectId, 1, phaseIds.get("registration") , Long.parseLong(TestHelper.getCompetitiorUserId()), TestHelper.getCompetitorUsername(), con);
            // add reviewer.
            TestHelper.AddResource(projectId, 4, phaseIds.get("review"), Long.parseLong(TestHelper.TESTS_USER_ID), TestHelper.getUsername(), con);
            // add contest submission.
            TestHelper.AddSubmission(projectId, phaseIds.get("submission"), submitterId, 1, con);
        } finally {
            con.close();
        }
    }

    
    /**
     * Test Case Number: FTC128 RS8.2 Verify Reviewer can submit a scorecard
     *
     * @throws Exception if any error occurs
     */
    public void testReviewerAddScorecard() throws Exception {
        // login as reviewer
        TestHelper.loginUser(browser);
        // open the project details page
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);

        //click the Review Scorecard link
        TestHelper.submitScorecard(browser, "4 - Strongly Agree", "View Completed Scorecard", false);
        assertTrue("Comments should be added", browser.isTextPresent("This is my comments!"));

        //Check the score is reflected against the submission
        String score = browser.getText("//b[@id='scoreHere']");
        assertEquals("Score should be 100.0", "100.0", score);
        assertNoErrorsOccurred();

    }



}
