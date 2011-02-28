/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.sql.Connection;

/**
 * Online review functional tests 3, change score cored testing
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ChangeScorecardTests extends ProjectTests {

    /**
     * <p>Represents scorecard id</p>
     */
    private long scorecardId;

    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {
    	super.setUp();
        Connection con = TestHelper.getConnection();
        try {
            // add new spec review scorecard for FTC98 testing  
            scorecardId = TestHelper.addSpecReviewScoreCard(con);
            TestHelper.setPhaseScorecard(con, phaseIds.get("spec_review"), scorecardId);
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
        Connection con = TestHelper.getConnection();
        try {
            // delete the added spec review scorecard 
            TestHelper.deleteSpecReviewScoreCard(con,scorecardId);
        } finally {
            con.close();
        }
        super.tearDown();
    }



    /**
     * Test Case Number: FTC98 RS5.7 Verify Manager can Add a scorecard
     *
     * @throws Exception if any error occurs
     */
    public void testAddScorecard() throws Exception {
        // login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        // Click the 'Edit Project' Link
        browser.click("//img[@alt='Edit Project']");
        browser.waitForPageToLoad(TIMEOUT);
        // Check the created scorecard is used
        String scorecard = browser.getText("//select[@name='phase_scorecard[2]']/option[1]");
        assertEquals("New Created Scorecard should be used", "Default Spec Review Scorecard 2.0", scorecard);
        
        // select 'Default Spec Review Scorecard 1.0' as scorecard, no error expected
        browser.select("//select[@name='phase_scorecard[2]']", "label=Default Spec Review Scorecard 1.0");
        browser.type("explanation", "Scorecard add");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);

        scorecard = browser.getText("//div[@id='mainMiddleContent']/div/table[4]/tbody/tr[9]/td[2]/a");
        assertEquals("Scorecard should be added", "Default Spec Review Scorecard v1.0", scorecard);

        assertNoErrorsOccurred();
    }

   
}
