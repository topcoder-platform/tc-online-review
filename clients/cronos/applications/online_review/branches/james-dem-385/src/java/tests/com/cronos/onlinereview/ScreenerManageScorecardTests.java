/*
 * Copyright (C) 2011 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.sql.Connection;


/**
 * Online review functional tests 4, testing screener manage the scorecard.
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ScreenerManageScorecardTests extends ProjectTests {

    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {
        super.setUp();
        Connection con = TestHelper.getConnection();
        try {
            // close all the phase before screen phase
            TestHelper.ClosePhase(phaseIds.get("spec_submission"), con);
            TestHelper.ClosePhase(phaseIds.get("spec_review"), con);
            TestHelper.ClosePhase(phaseIds.get("registration"), con);
            TestHelper.ClosePhase(phaseIds.get("submission"), con);
            // open screen phase
            TestHelper.OpenPhase(phaseIds.get("screening"), con);
            // add  submitter.
            long Submitter = TestHelper.AddResource(projectId, 1, phaseIds.get("registration") , Long.parseLong(TestHelper.getCompetitiorUserId()), TestHelper.getCompetitorUsername(), con);
            // add screener.
            TestHelper.AddResource(projectId, 2, phaseIds.get("screening") , Long.parseLong(TestHelper.TESTS_USER_ID), TestHelper.getUsername(), con);
            // add reviewer.
            TestHelper.AddResource(projectId, 4, phaseIds.get("review"), Long.parseLong(TestHelper.TESTS_USER_ID), TestHelper.getUsername(), con);
            // add contest submission.
            TestHelper.AddSubmission(projectId, phaseIds.get("submission"), Submitter, 1, con);
        } finally {
            con.close();
        }
    }
   
    
    /**
     * Test Case Number: FTC130 RS8.2 Verify Screener can submit a scorecard
     *
     * @throws Exception if any error occurs
     */
    public void testScreenerAddScorecard() throws Exception {
        // login as reviewer
        TestHelper.loginUser(browser);
        // open the project details page
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);

        //submit the Screen Scorecard 
        TestHelper.submitScorecard(browser, "4 - Strongly Agree", "View Completed Scorecard", false);
        assertTrue("Comments should be added", browser.isTextPresent("This is my comments!"));

        //Check the score is reflected against the submission
        String score = browser.getText("//b[@id='scoreHere']");
        assertEquals("Score should be 100.0", "100.0", score);
        assertNoErrorsOccurred();
    }



    /**
     * Test Case Number: FTC134 RS8.3 Verify Screener can save a scorecard for later
     *
     * @throws Exception if any error occurs
     */
    public void testScreenerLaterSaveScorecard() throws Exception {
        // login as reviewer
        TestHelper.loginUser(browser);
        // open the project details page
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);

        //click the Screen Scorecard link
        browser.click("//table[@id='myRolesTable']/tbody/tr[2]/td[2]/a/b");
        browser.waitForPageToLoad(TIMEOUT);

        // add the screen comments
        browser.select("answer[0]","label=4 - Strongly Agree");
        browser.type("comment(0.1)", "This is my screen comments!");
        browser.click("//input[@alt='Save for Later']");
        browser.waitForPageToLoad(TIMEOUT);

        //Check the scorecard is the same before clcik 'Save for Later'
        browser.click("//table[@id='myRolesTable']/tbody/tr[2]/td[2]/a/b");
        browser.waitForPageToLoad(TIMEOUT);
        String answer = browser.getText("//select[@name='answer[0]']/option[@selected='selected']");
        assertEquals("Answer should be 4 - Strongly Agree", "4 - Strongly Agree", answer);
        assertTrue("Comments should not be added", browser.isTextPresent("This is my screen comments!"));
        assertNoErrorsOccurred();
    }


}
