/*
 * Copyright (C) 2011-2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.sql.Connection;


/**
 * Online review functional tests 4, testing reviewer upload test case.
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ReviewerUploadTestCaseTests extends ProjectTests {

    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {
        super.setUp();
        Connection con = TestHelper.getConnection();
        try {
            // update to dev project
            TestHelper.updateProjectCategory(projectId, 2, con);
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
            // add Accuracy reviewer.
            TestHelper.AddResource(projectId, 5, phaseIds.get("review"), Long.parseLong(TestHelper.TESTS_USER_ID), TestHelper.getUsername(), con);
            // add contest submission.
            TestHelper.AddSubmission(projectId, phaseIds.get("submission"), submitterId, 1, con);
        } finally {
            con.close();
        }
    }


     /**
     * Test Case Number: FTC140 RS8.5 Verify Reviewer can Upload test cases
     *
     * @throws Exception if any error occurs 
     */
    public void testReviewerUploadTestCases() throws Exception {
        // login as reviewer
        TestHelper.loginUser(browser);
        // open the project details page
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);

        //click the Upload Test Case link
        browser.click("//table[@id='myRolesTable']/tbody/tr[2]/td[2]/a/b");
        browser.waitForPageToLoad(TIMEOUT);

        //upload test cases
        browser.type("file", TestHelper.getUploadFilePath()); 
        browser.click("//input[@alt='Upload']");
        browser.waitForPageToLoad(TIMEOUT);

        //Check test cases have been uploaded
        String completeImg = browser.getAttribute("//table[@id='myRolesTable']/tbody/tr[2]/td[2]/img@alt");
        assertEquals("test cases should have been uploaded", "Completed", completeImg);
        assertNoErrorsOccurred();

    }


}
