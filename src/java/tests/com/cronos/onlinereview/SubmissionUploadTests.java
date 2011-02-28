/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.sql.Connection;


/**
 * Online review functional tests 3,competitor upload a submission  testing
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class SubmissionUploadTests extends ProjectTests {

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
            // competitor register the project
            TestHelper.AddResource(projectId, 1, -1, Long.parseLong(TestHelper.getCompetitiorUserId()), TestHelper.getCompetitorUsername(), con);

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
     * Test Case Number: FTC112 RS7.1 Verify Competitor can upload a submission
     *
     * @throws Exception if any error occurs
     */
    public void testCompetitorCanUploadSubmission() throws Exception {


        // login as competitor
        TestHelper.loginAsCompetitor(browser);
        // open the project details page
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);

        //click submission
        browser.click("//table[@id='myRolesTable']/tbody/tr[2]/td[2]/a");
        browser.waitForPageToLoad(TIMEOUT);
        browser.type("file", TestHelper.getUploadFilePath());
        browser.click("//input[@name='']");
        Thread.sleep(2*(new Long(TIMEOUT)));
        assertNoErrorsOccurred();

        //verify submission is uploaded
        String deliverableString = browser.getText("//table[@id='myRolesTable']/tbody/tr[2]/td[3]");
        String submissionString = browser.getText("//table[@id='myRolesTable']/tbody/tr[2]/td[2]/a");
        assertEquals("Submission should be uploaded","Submission" ,submissionString);
        assertEquals("Submission should be uploaded","There are no outstanding deliverables." ,deliverableString);

        browser.click("//div[@id='sc2']/ul/li[2]/a");
        String submissionId = browser.getText("//table[@id='Submissions0']/tbody/tr[3]/td[1]/a[1]");
        assertNotNull("Submission should be uploaded",submissionId);
        assertNoErrorsOccurred();

    }


}
