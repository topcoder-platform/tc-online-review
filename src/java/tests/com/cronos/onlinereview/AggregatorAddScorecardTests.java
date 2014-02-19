/*
 * Copyright (C) 2011 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.sql.Connection;


/**
 * Online review functional tests 4, verify aggregator can submit a scorecard.
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class AggregatorAddScorecardTests extends ProjectTests {

    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {
        super.setUp();
        Connection con = TestHelper.getConnection();
        try {
            // close all the phase before aggregation phase
            TestHelper.ClosePhase(phaseIds.get("spec_submission"), con);
            TestHelper.ClosePhase(phaseIds.get("spec_review"), con);

            // add  submitter.
            long submitterId = TestHelper.AddResource(projectId, 1, phaseIds.get("registration") , Long.parseLong(TestHelper.getCompetitiorUserId()), TestHelper.getCompetitorUsername(), con);
            // add screener.
            TestHelper.AddResource(projectId, 2, phaseIds.get("screening") , Long.parseLong(TestHelper.TESTS_USER_ID), TestHelper.getUsername(), con);
            // add reviewer.
            long reviewerId = TestHelper.AddResource(projectId, 4, phaseIds.get("review"), Long.parseLong(TestHelper.TESTS_USER_ID), TestHelper.getUsername(), con);
            // add aggregator.
            TestHelper.AddResource(projectId, 8, phaseIds.get("aggregation") , Long.parseLong(TestHelper.TESTS_USER_ID), TestHelper.getUsername(), con);
            // add contest submission.
            long submissionId = TestHelper.AddSubmission(projectId, phaseIds.get("submission"), submitterId, 1, con);
            /*// add review
            TestHelper.AddReview (reviewerId, submissionId, con);*/

        } finally {
            con.close();
        }
    }

    
    /**
     * Test Case Number: FTC129 RS8.2 Verify Aggregator can submit a scorecard
     *
     * @throws Exception if any error occurs
     */
    public void testAggregatorAddScorecard() throws Exception {
        // login as reviewer
        TestHelper.loginUser(browser);
        // open the project details page
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);

         // Open registration phase
        TestHelper.OpenPhaseByUI(browser, 3, "registration");
        // Open submission phase
        TestHelper.OpenPhaseByUI(browser, 4, "submission");


        // Close registration phase
        TestHelper.ClosePhaseByUI(browser, 3, "registration");
        // Close submission phase
        TestHelper.ClosePhaseByUI(browser, 4, "submission");

        // open screening phase
        TestHelper.OpenPhaseByUI(browser, 5, "screening");
        //submit Screen Scorecard
        TestHelper.submitScorecard(browser, "4 - Strongly Agree", "Return to Project Details", false);
        // Close screening phase
        TestHelper.ClosePhaseByUI(browser, 5, "screening");


        // open review phase
        TestHelper.OpenPhaseByUI(browser, 6, "review");
        //submit Review Scorecard
        TestHelper.submitScorecard(browser, "4 - Strongly Agree", "Return to Project Details", false);
        // Close review phase
        TestHelper.ClosePhaseByUI(browser, 6, "review");

        // open appeal phase
        TestHelper.OpenPhaseByUI(browser, 7, "appeal");
        // Close appeal phase
        TestHelper.ClosePhaseByUI(browser, 7, "appeal");

        // open appeal response phase
        TestHelper.OpenPhaseByUI(browser, 8, "appeal response");
        // Close appeal response phase
        TestHelper.ClosePhaseByUI(browser, 8, "appeal response");

        // open aggregation phase
        TestHelper.OpenPhaseByUI(browser, 9, "aggregation");

        //click the Aggregation link
        browser.click("//table[@id='myRolesTable']/tbody/tr[2]/td[2]/a/b");
        browser.waitForPageToLoad(TIMEOUT);
       // add comments
        browser.click("//div[@id='mainMiddleContent']/div/form/table/tbody/tr[5]/td[6]/input");
        browser.type("aggregator_response[0]", "This is Aggregation comment!");
        browser.click("//input[@alt='Save and Mark Complete']");
        browser.waitForPageToLoad(TIMEOUT);


        //click the Aggregation link
        browser.click("//table[@id='myRolesTable']/tbody/tr[2]/td[2]/a/b");
        browser.waitForPageToLoad(TIMEOUT);
        //Check the comments is added
        assertTrue("Comments should be added", browser.isTextPresent("This is Aggregation comment!"));
        String status = browser.getText("//div[@id='mainMiddleContent']/div/table/tbody/tr[5]/td[5]");
        assertEquals("Staus should be Accepted", "Accepted", status);

        assertNoErrorsOccurred();
    }


}
