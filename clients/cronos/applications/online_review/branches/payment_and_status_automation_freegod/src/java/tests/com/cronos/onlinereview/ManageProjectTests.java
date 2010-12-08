/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

/**
 * Online review functional tests 1, manage the project.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ManageProjectTests extends ProjectTests {

    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {
        super.setUp();
    	projectId = TestHelper.getProjectId();
    	// created the project with the given generated id
    	TestHelper.createProject(projectId, -1, false, true);
    }

    /**
     * Test Case Number: FTC13 RS2.1 Verify Manager can access "Manage Project", "Edit Project Links" and "Edit
     * Project" functionalities.
     *
     * @throws Exception if any error occurs
     */
    public void testManagerPrivileges() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        assertTrue("Manage Project link must be presented", browser.isElementPresent("//img[@alt='Manage Project']"));
        assertTrue("Edit Project Links link must be presented",
            browser.isElementPresent("//img[@alt='Edit Project Link']"));
        assertTrue("Edit Project link must be presented", browser.isElementPresent("//img[@alt='Edit Project']"));
        // check if the manage project link works
        browser.click("//img[@alt='Manage Project']");
        browser.waitForPageToLoad(TIMEOUT);
        assertTrue("manage project page should be shown",
            browser.isTextPresent("Integration Test Case 1 (project 1)"));
        browser.click("//img[@alt='Cancel']");
        browser.waitForPageToLoad(TIMEOUT);
        // check if the edit project links link works
        browser.click("//img[@alt='Edit Project Link']");
        browser.waitForPageToLoad(TIMEOUT);
        assertTrue("manage project links page should be shown",
            browser.isTextPresent("Integration Test Case 1 (project 1)"));
        browser.click("//img[@alt='Cancel']");
        browser.waitForPageToLoad(TIMEOUT);
        // check if the edit project link works
        browser.click("//img[@alt='Edit Project']");
        browser.waitForPageToLoad(TIMEOUT);
        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC15 RS3.1 Verify Manager can extend Registration phase.
     *
     * @throws Exception if any error occurs
     */
    public void testExtensionOfRegistrationPhase() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        String status = browser.getText("//div[@id='mainMiddleContent']/div/table[3]/tbody/tr[2]/td[2]/div/table/tbody");
        assertTrue("registration period is 72 hrs at first", status.contains("Registration 72 hrs"));
        browser.click("//img[@alt='Manage Project']");
        browser.waitForPageToLoad(TIMEOUT);
        browser.type("registration_phase_extension", "2");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);
        status = browser.getText("//div[@id='mainMiddleContent']/div/table[3]/tbody/tr[2]/td[2]/div/table/tbody");
        assertTrue("registration period is 96 hrs after extending", status.contains("Registration 120 hrs"));
        assertNoErrorsOccurred();
    }

	/**
     * Test Case Number: FTC17 RS3.2 Verify Manager can add resources to the project (Designers).
     *
     * @throws Exception if any error occurs
     */
    public void testManagementConsoleAddDesigner() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        browser.click("//img[@alt='Manage Project']");
        browser.waitForPageToLoad(TIMEOUT);
        browser.type("resource_handles[0]", "super");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);
        assertTrue("Resources should be shown", browser.isTextPresent("Resources"));
        assertTrue("designer should be added", browser.isTextPresent("super"));
        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC24 RS3.4 Verify Manager can upload development distribution.
     *
     * @throws Exception if any error occurs
     */
//    public void testUploadDevelopmentDistribution() throws Exception {
//    	// login the user first
//    	TestHelper.loginUser(browser);
//    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
//        browser.click("//img[@alt='Manage Project']");
//        browser.waitForPageToLoad(TIMEOUT);
//        browser.click("link=Distributions");
//        browser.type("distribution_file", TestHelper.getDistributionLocation());
//        browser.click("//table[@id='upload_distribution_tbl']/tbody/tr[2]/td[2]/input[2]");
//        browser.waitForPageToLoad(TIMEOUT);
//        assertNoErrorsOccurred();
//    }
}
