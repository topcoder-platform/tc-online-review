/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.sql.Connection;


/**
 * Online review functional tests 3,  competitor upload a submission while in final fix testing
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class FinalFixSubmissionUploadTests extends ProjectTests {

    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {
    	super.setUp();
        Connection con = TestHelper.getConnection();
        try {
            // close all the phase before final fix
            TestHelper.ClosePhase(phaseIds.get("spec_submission"), con);
            TestHelper.ClosePhase(phaseIds.get("spec_review"), con);
            TestHelper.ClosePhase(phaseIds.get("registration"), con);
            TestHelper.ClosePhase(phaseIds.get("submission"), con);
            TestHelper.ClosePhase(phaseIds.get("screening"), con);
            TestHelper.ClosePhase(phaseIds.get("review"), con);
            TestHelper.ClosePhase(phaseIds.get("appeals"), con);
            TestHelper.ClosePhase(phaseIds.get("appeals_response"), con);
            TestHelper.ClosePhase(phaseIds.get("aggregation"), con);
            TestHelper.ClosePhase(phaseIds.get("aggregation_review"), con);
            // open final fix phase
            TestHelper.OpenPhase(phaseIds.get("final_fix"), con);
            // competitor register the project
            TestHelper.AddResource(projectId, 1, phaseIds.get("registration"), Long.parseLong(TestHelper.getCompetitiorUserId()), TestHelper.getCompetitorUsername(), con);
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
     * Test Case Number: FTC113 RS7.2 Verify Competitor can upload a submission while in final fix
     *
     * @throws Exception if any error occurs
     */
    public void testCompetitorCanUploadSubmissionInFinalFix() throws Exception {


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
        String phaseString = browser.getText("//table[@id='myRolesTable']/tbody/tr[2]/td[2]/a");
        assertEquals("Submission should be uploaded","Final Fix" ,phaseString);
        assertEquals("Submission should be uploaded","There are no outstanding deliverables." ,deliverableString);
        assertNoErrorsOccurred();

    }


}
