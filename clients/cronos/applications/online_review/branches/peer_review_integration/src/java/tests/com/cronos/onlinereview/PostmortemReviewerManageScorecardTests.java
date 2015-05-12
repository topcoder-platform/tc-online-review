/*
 * Copyright (C) 2011 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.sql.Connection;


/**
 * Online review functional tests 4,testing Post-mortem Reviewer can submit a scorecard.
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class PostmortemReviewerManageScorecardTests extends ProjectTests {

    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {
        super.setUp();
        Connection con = TestHelper.getConnection();
        try {
             //add post-moterm phase
            TestHelper.createPostMotermPhase(projectId, phaseIds, con);
            //set scorecard for post-mortem phase
            TestHelper.setPhaseScorecard(con, phaseIds.get("post_mortem"), TestHelper.POST_MORTEM_SCORECARD_ID);
            // close all the phase before review phase
            TestHelper.ClosePhase(phaseIds.get("spec_submission"), con);
            TestHelper.ClosePhase(phaseIds.get("spec_review"), con);
            TestHelper.ClosePhase(phaseIds.get("registration"), con);
            TestHelper.ClosePhase(phaseIds.get("submission"), con);
            // open review phase
            TestHelper.OpenPhase(phaseIds.get("post_mortem"), con);
            // add post-mortem reviewer.
            TestHelper.AddResource(projectId, 16, phaseIds.get("post_mortem"), Long.parseLong(TestHelper.TESTS_USER_ID), TestHelper.getUsername(), con);
            
        } finally {
            con.close();
        }
    }

    /**
     * Test Case Number: FTC131 RS8.2 Verify Post-mortem Reviewer can submit a scorecard
     *
     * @throws Exception if any error occurs
     */
    public void testPostmortemReviewerAddScorecard() throws Exception {
        // login as reviewer
        TestHelper.loginUser(browser);
        // open the project details page
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);

        //submit  Review Scorecard 
        TestHelper.submitScorecard(browser, "4 - Strongly Agree", "View Completed Scorecard", false);
        assertTrue("Comments should be added", browser.isTextPresent("This is my comments!"));

        //Check the score is reflected against the submission
        String score = browser.getText("//b[@id='scoreHere']");
        assertEquals("Score should be 100.0", "100.0", score);
        assertNoErrorsOccurred();

    }

}
