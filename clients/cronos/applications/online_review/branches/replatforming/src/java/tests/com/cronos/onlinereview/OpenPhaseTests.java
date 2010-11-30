/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;



/**
 * Online review functional tests 1, open the closed phase.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class OpenPhaseTests extends ProjectTests {
    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {
        super.setUp();
        projectId = TestHelper.getProjectId();
    	// created the project with the given generated id
    	TestHelper.createProject(projectId, -1, false, false);

    }

    /**
     * Test Case Number: FTC56 RS5.2 Verify Manager can open a phase manually
     *
     * @throws Exception if any error occurs
     */
    public void testOpenRegistrationPhase() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        String status = browser.getText("//div[@id='mainMiddleContent']/div/table[3]/tbody/tr[2]/td[1]/table/tbody/tr[2]/td[2]");
        assertEquals("competition status is not correct", "Can't open", status);
        // open the registration phase first
        browser.click("//img[@alt='Edit Project']");
        browser.waitForPageToLoad(TIMEOUT);
        String id = browser.getValue("name=phase_js_id[1]");
        browser.type("explanation", "some reason");
        browser.click("//tr[@id='" + id + "']/td[1]/img[1]");
        browser.waitForPageToLoad(TIMEOUT);
        status = browser.getText("//div[@id='mainMiddleContent']/div/table[3]/tbody/tr[2]/td[1]/table/tbody/tr[3]/td[2]");
        assertEquals("competition is now open", "Open", status);
        assertNoErrorsOccurred();
    }
}
