/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.util.ArrayList;
import java.util.List;

/**
 * Online review functional tests 2, manage the project without the permission.
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class ManageProjectWithNoPermissionTests extends ProjectTests {

    /**
     * Tears down the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void tearDown() throws Exception {
    	if (projectId != -1) {
    		browser.click("link=Logout");
            browser.waitForPageToLoad(TIMEOUT);
    		// login as manager to delete the project
    		TestHelper.loginUser(browser);
    		TestHelper.deleteProject(browser, projectId);
    	}
        projectId = -1;
        super.tearDown();
    }

    /**
     * Test Case Number: FTC9 RS2.1 Verify competitor can not access "Edit Project Links" using direct link.
     *
     * @throws Exception if any error occurs
     */
    public void testAccessEditWithoutPermission() throws Exception {
    	//login as competitor
    	TestHelper.loginAsCompetitor(browser);
    	browser.open(TestHelper.getBaseURL() + "/actions/EditProjectLinks.do?method=editProjectLinks&pid=" + projectId);
        assertTrue("error occurs, access is denied", browser.isTextPresent("Attention!"));
        assertTrue("error occurs, access is denied", browser.isTextPresent("The following error has occurred:"));
        assertTrue("error occurs, access is denied", browser.isTextPresent("You don't have permission to perform the operation you requested."));
        assertNoErrorsOccurred();
    }

	/**
     * Test Case Number: FTC10 RS2.1 Verify competitor can not access "Manage Project", "Edit Project Links" and "Edit Project" functionalities.
     *
     * @throws Exception if any error occurs
     */
    public void testManageFunctionsNotAvailable() throws Exception {
    	//login as competitor
    	TestHelper.loginAsCompetitor(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        assertFalse("Manage Project link must not be presented", browser.isElementPresent("//img[@alt='Manage Project']"));
        assertFalse("Edit Project Links link must not be presented",
            browser.isElementPresent("//img[@alt='Edit Project Link']"));
        assertFalse("Edit Project link must not be presented", browser.isElementPresent("//img[@alt='Edit Project']"));
        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC12 RS2.1 Verify competitor can not access "Manage Project" using direct link.
     *
     * @throws Exception if any error occurs
     */
    public void testAccessManageProjectWithoutPermission() throws Exception {
    	//login as competitor
    	TestHelper.loginAsCompetitor(browser);
    	browser.open(TestHelper.getBaseURL() + "/actions/ViewManagementConsole.do?method=viewConsole&pid=" + projectId);
        assertTrue("error occurs, access is denied", browser.isTextPresent("Attention!"));
        assertTrue("error occurs, access is denied", browser.isTextPresent("The following error has occurred:"));
        assertTrue("error occurs, access is denied", browser.isTextPresent("You don't have permission to perform the operation you requested."));
        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC14 RS2.1 Verify competitor can not access "Edit Project" using direct link.
     *
     * @throws Exception if any error occurs
     */
    public void testAccessEditProjectWithoutPermission() throws Exception {
    	//login as competitor
    	TestHelper.loginAsCompetitor(browser);
    	browser.open(TestHelper.getBaseURL() + "/actions/EditProject.do?method=editProject&pid=" + projectId);
        assertTrue("error occurs, access is denied", browser.isTextPresent("Attention!"));
        assertTrue("error occurs, access is denied", browser.isTextPresent("The following error has occurred:"));
        assertTrue("error occurs, access is denied", browser.isTextPresent("You don't have permission to perform the operation you requested."));
        assertNoErrorsOccurred();
    }
}
