/*
 * Copyright (C) 2011 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.sql.Connection;


/**
 * Online review functional tests 4, Competitor can not Unregister from the Contest after registration phase ending.
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class UnregisterProjectTests extends ProjectTests {

    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {//need to merge with SubmissionUploadTests.setUp()
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
     * Test Case Number: FTC114 RS7.3 Verify Competitor can not Unregister from the Contest after registration phase ending
     *
     * @throws Exception if any error occurs
     */
    public void testCannotUnregisterAfterRegistrationPhase() throws Exception {
        // login as competitor
        TestHelper.loginAsCompetitor(browser);
        // open the project details page
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);


        // check the unregister link
        String unregisterLink = browser.getText("//table[@id='table12']/tbody/tr/td[4]/a[1]");
        assertFalse("This should not be the unregister link", "Unregister".equals(unregisterLink));

        // access the unregister link directly
        browser.open(TestHelper.getBaseURL() + TestHelper.getUnregisterURL() + projectId);
        // Verify Competitor can not access the unregister the project after registration phase ending
        assertTrue("error occurs, access is denied", browser.isTextPresent("Attention!"));
        assertTrue("error occurs, access is denied", browser.isTextPresent("The following error has occurred:"));
        assertTrue("error occurs, access is denied", browser.isTextPresent("You cannot unregister since registration phase is closed."));
        assertNoErrorsOccurred();

    }


}
