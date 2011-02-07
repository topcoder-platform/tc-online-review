/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

/**
 * Edit project tests.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class EditProjectTests extends ProjectTests {

    /**
     * Test Case Number: FTC69 RS5.5 Verify Manager can add Designer Role to a project
     *
     * @throws Exception if any error occurs
     */
    public void testAddRoleDesigner() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        browser.click("//img[@alt='Edit Project']");
        browser.waitForPageToLoad(TIMEOUT);
		browser.select("resources_role[0]", "label=Designer");
		browser.type("resources_name[0]", "super");
		browser.click("//img[@alt='Add']");
		browser.type("explanation", "Add designer");
		browser.click("//input[@name='']");
		browser.waitForPageToLoad(TIMEOUT);
        assertTrue("Resources should be shown", browser.isTextPresent("Resources"));
        assertTrue("designer should be added", browser.isTextPresent("super"));   
        assertNoErrorsOccurred();
    }
}
