/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.sql.Connection;

/**
 * Online review functional tests 2, close the open phase.
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class ClosePhaseTests extends ProjectTests {

    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {
    	super.setUp();
    	Connection con = TestHelper.getConnection();
    	try {
    		TestHelper.OpenPhase(phaseIds.get("spec_submission"), con);
    		// add spec submitter. 
    		long specSubmitter = TestHelper.AddResource(projectId, 17, -1, Long.parseLong(TestHelper.getCompetitiorUserId()), TestHelper.getCompetitorUsername(), con);
    		// add spec submission.
    		TestHelper.AddSubmission(projectId, specSubmitter, 2, con);
    	}
    	finally {
    		con.close();
    	}
    }
	
    /**
     * Test Case Number: FTC57 RS5.2 Verify Manager can close a phase manually
     *
     * @throws Exception if any error occurs
     */
    public void testCloseSpecSubmissionPhase() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        String status = browser.getText("//div[@id='mainMiddleContent']/div/table[3]/tbody/tr[2]/td[1]/table/tbody/tr[2]/td[2]");
        assertFalse("competition status is not correct", status.contains("Open"));
        // close the specification submission phase
        browser.click("//img[@alt='Edit Project']");
        browser.waitForPageToLoad(TIMEOUT);
        String id = browser.getValue("name=phase_js_id[1]");
        browser.type("explanation", "Close the spec submission");
        browser.click("//tr[@id='" + id + "']/td[1]/img[2]");
        browser.waitForPageToLoad(TIMEOUT);
        status = browser.getText("//div[@id='mainMiddleContent']/div/table[3]/tbody/tr[2]/td[1]/table/tbody/tr[2]/td[2]");
        assertEquals("spec submission is now closed", "Closed", status);
        assertNoErrorsOccurred();
    }
}
