/*
 * Copyright (C) 2011 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.sql.Connection;


/**
 * Online review functional tests 4, testing competitor managing the appeals.
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ManageAppealTests extends ProjectTests {

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
            // close all the phase before appeal phase
            TestHelper.ClosePhase(phaseIds.get("spec_submission"), con);
            TestHelper.ClosePhase(phaseIds.get("spec_review"), con);
            TestHelper.ClosePhase(phaseIds.get("registration"), con);
            TestHelper.ClosePhase(phaseIds.get("submission"), con);
            TestHelper.ClosePhase(phaseIds.get("screening"), con);
            TestHelper.ClosePhase(phaseIds.get("review"), con);
            // open appeal phase
            TestHelper.OpenPhase(phaseIds.get("appeals"), con);
            // add  submitter.
            long submitterId = TestHelper.AddResource(projectId, 1, -1 , Long.parseLong(TestHelper.getCompetitiorUserId()), TestHelper.getCompetitorUsername(), con);
            // add reviewer.
            long reviewerId = TestHelper.AddResource(projectId, 4, phaseIds.get("review"), Long.parseLong(TestHelper.TESTS_USER_ID), TestHelper.getUsername(), con);
            // add contest submission.
            long submissionId = TestHelper.AddSubmission(projectId, phaseIds.get("submission"), submitterId, 1, con);

            reviewId = TestHelper.AddReview (reviewerId, submissionId, phaseIds.get("review"), con);

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
     * Test Case Number: FTC116 RS7.4 Verify Competitor can write appeals
     *
     * @throws Exception if any error occurs
     */
    public void testWriteAppeal() throws Exception {
        // login as competitor
        TestHelper.loginAsCompetitor(browser);
        // open the project details page
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);

        // click the score to open the review socrecard
        browser.click("//div[@id='sc3']/table/tbody/tr[4]/td[4]/a");
        browser.waitForPageToLoad(TIMEOUT);

        // click the one appeal to add appeals
        browser.click("//a[@id='placeAppeal_0']/img");
        browser.type("appeal_text[0]","This is my appeal text!");
        // submit the appeal
        browser.click("//img[@alt='Submit Appeal']");
        Thread.sleep(new Long(TIMEOUT));
        
        // open the view review page
        browser.open(TestHelper.getBaseURL() + TestHelper.getReviewURL() + reviewId);
        // Verify the appeal have been added
        assertTrue("Appeal text should have been added", browser.isTextPresent("This is my appeal text!"));
        assertNoErrorsOccurred();

    }


     /**
     * Test Case Number: FTC119 RS7.7 Verify Competitor can stop writing appeals
     *
     * @throws Exception if any error occurs
     */
    public void testStopAppealPhase() throws Exception {
        // login as competitor
        TestHelper.loginAsCompetitor(browser);
        // open the project details page
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);

        // Check if  "Complete Appeal" link is present
        String appealLink = browser.getText("//table[@id='table12']/tbody/tr/td[4]/a[1]");
        assertEquals("'Complete Appeals' link must be present", "Complete Appeals", appealLink);

        // Click the "Complete Appeal" link
        browser.click("//table[@id='table12']/tbody/tr/td[4]/a[1]");
        browser.waitForPageToLoad(TIMEOUT);
        // click confirm button
        browser.click("//img[@alt='Confirm']");
        browser.waitForPageToLoad(TIMEOUT);

        appealLink = browser.getText("//table[@id='table12']/tbody/tr/td[4]/a[1]");
        // Check if "Resume Appeal" link is present
        assertEquals("'Resume Appeals' link must be present", "Resume Appeals", appealLink);
        assertNoErrorsOccurred();

    }




}
