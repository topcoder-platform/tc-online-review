/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

/**
 * Online review functional tests 1, manage the project.
 *
 * @author TCSDEVELOPER
 * @version 1.1
 * @since 1.0
 */
public class ManageProjectTests extends ProjectTests {

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
            browser.isTextPresent("Manage Project"));
        browser.click("//img[@alt='Cancel']");
        browser.waitForPageToLoad(TIMEOUT);
        // check if the edit project links link works
        browser.click("//img[@alt='Edit Project Link']");
        browser.waitForPageToLoad(TIMEOUT);
        assertTrue("manage project links page should be shown",
            browser.isTextPresent("Manage Project Links"));
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
        assertTrue("registration period is 48 hrs at first", status.contains("Registration 48 h"));
        browser.click("//img[@alt='Manage Project']");
        browser.waitForPageToLoad(TIMEOUT);
        browser.type("registration_phase_extension", "2");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);
        status = browser.getText("//div[@id='mainMiddleContent']/div/table[3]/tbody/tr[2]/td[2]/div/table/tbody");
        assertTrue("registration period is 96 hrs after extending", status.contains("Registration 96 h"));
        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC16 RS3.1 Verify Manager can extend Submission phase.
     *
     * @throws Exception if any error occurs
     * @version 1.1
     */
    public void testExtensionOfSubmissionPhase() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        String status = browser.getText("//div[@id='mainMiddleContent']/div/table[3]/tbody/tr[2]/td[2]/div/table/tbody");
        assertTrue("submission period is 168 hrs at first", status.contains("Submission 168 hrs"));
        browser.click("//img[@alt='Manage Project']");
        browser.waitForPageToLoad(TIMEOUT);
        browser.type("submission_phase_extension", "2");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);
        status = browser.getText("//div[@id='mainMiddleContent']/div/table[3]/tbody/tr[2]/td[2]/div/table/tbody");
        assertTrue("submission period is 216 hrs after extending", status.contains("Submission 216 hrs"));
        assertNoErrorsOccurred();
    }

	/**
     * Test Case Number: FTC17 RS3.2 Verify Manager can add resources to the project (Designers).
     *
     * @throws Exception if any error occurs
     * @version 1.1
     * @since 1.0
     */
    public void testManagementConsoleAddDesigner() throws Exception {
    	openManagePage();
        browser.type("resource_handles[0]", "super");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);
        assertTrue("Resources should be shown", browser.isTextPresent("Resources"));
        String role = browser.getText("//div[@id='mainMiddleContent']/div/table[5]/tbody/tr[3]/td[1]");
        String added = browser.getText("//div[@id='mainMiddleContent']/div/table[5]/tbody/tr[3]/td[2]");
        assertEquals("designer should be added", "super", added);
        assertEquals("designer should be added", "Designer", role);
        assertNoErrorsOccurred();
    }

	/**
     * Test Case Number: FTC18 RS3.2 Verify Manager can add resources to the project (Observers).
     *
     * @throws Exception if any error occurs
     * @version 1.1
     */
    public void testManagementConsoleAddObserver() throws Exception {
    	openManagePage();
        browser.type("resource_handles[1]", "super");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);
        assertTrue("Resources should be shown", browser.isTextPresent("Resources"));
        String role = browser.getText("//div[@id='mainMiddleContent']/div/table[5]/tbody/tr[3]/td[1]");
        String added = browser.getText("//div[@id='mainMiddleContent']/div/table[5]/tbody/tr[3]/td[2]");
        assertEquals("Observer should be added", "super", added);
        assertEquals("Observer should be added", "Observer", role);
        assertNoErrorsOccurred();
    }

	/**
     * Test Case Number: FTC19 RS3.2 Verify Manager can add resources to the project (Copilots).
     *
     * @throws Exception if any error occurs
     * @version 1.1
     */
    public void testManagementConsoleAddCopilot() throws Exception {
    	openManagePage();
        browser.type("resource_handles[2]", "super");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);
        assertTrue("Resources should be shown", browser.isTextPresent("Resources"));
        String role = browser.getText("//div[@id='mainMiddleContent']/div/table[5]/tbody/tr[3]/td[1]");
        String added = browser.getText("//div[@id='mainMiddleContent']/div/table[5]/tbody/tr[3]/td[2]");
        assertEquals("Copilot should be added", "super", added);
        assertEquals("Copilot should be added", "Copilot", role);
        assertNoErrorsOccurred();
    }

	/**
     * Test Case Number: FTC20 RS3.2 Verify Manager can add resources to the project (Client Managers).
     *
     * @throws Exception if any error occurs
     * @version 1.1
     */
    public void testManagementConsoleAddClientManager() throws Exception {
    	openManagePage();
        browser.type("resource_handles[3]", "super");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);
        assertTrue("Resources should be shown", browser.isTextPresent("Resources"));
        String role = browser.getText("//div[@id='mainMiddleContent']/div/table[5]/tbody/tr[3]/td[1]");
        String added = browser.getText("//div[@id='mainMiddleContent']/div/table[5]/tbody/tr[3]/td[2]");
        assertEquals("Copilot should be added", "super", added);
        assertEquals("Copilot should be added", "Client Manager", role);
        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC21 RS3.3 Verify Manager can generate design distribution.
     *
     * @throws Exception if any error occurs
     * @version 1.1
     */
    public void testGenerateDesignDistribution() throws Exception {
    	openManagePage();
        browser.click("link=Distributions");
        browser.type("distribution_package_name", "mypackage");
		browser.type("distribution_rs", TestHelper.getRSLocation());
		browser.click("//table[@id='distribution_tbl']/tbody/tr[9]/td/input");
        Thread.sleep(new Long(TIMEOUT));
        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC22 RS3.3 Verify Manager can generate design distribution with additional documents.
     *
     * @throws Exception if any error occurs
     * @version 1.1
     */
    public void testGenerateAdditionalDocument() throws Exception {
    	openManagePage();
        browser.click("link=Distributions");
        browser.type("distribution_package_name", "mypackage");
		browser.type("distribution_rs", TestHelper.getRSLocation());
		browser.type("distribution_additional1", TestHelper.getDocument1());
		browser.type("distribution_additional2", TestHelper.getDocument2());
		browser.type("distribution_additional3", TestHelper.getDocument3());
		browser.click("//table[@id='distribution_tbl']/tbody/tr[9]/td/input");
        Thread.sleep(new Long(TIMEOUT));
        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC23 RS3.3 Verify Manager can upload design distribution.
     *
     * @throws Exception if any error occurs
     * @version 1.1
     */
    public void testUploadDesignDistribution() throws Exception {
    	openManagePage();
        browser.click("link=Distributions");
        browser.type("distribution_file", TestHelper.getDistributionLocation());
        browser.click("//table[@id='upload_distribution_tbl']/tbody/tr[2]/td[2]/input[2]");
        Thread.sleep(new Long(TIMEOUT));
        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC24 RS3.4 Verify Manager can upload development distribution.
     *
     * @throws Exception if any error occurs
     * @version 1.1
     * @since 1.0
     */
    public void testUploadDevelopmentDistribution() throws Exception {
    	openManagePage();
        browser.click("link=Distributions");
        browser.type("distribution_file", TestHelper.getDistributionLocation());
        browser.click("//table[@id='upload_distribution_tbl']/tbody/tr[2]/td[2]/input[2]");
        Thread.sleep(new Long(TIMEOUT));
        assertNoErrorsOccurred();
    }

    /**
     * Open the project management page.
     * @throws Exception if error
     */
    private void openManagePage() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        browser.click("//img[@alt='Manage Project']");
        browser.waitForPageToLoad(TIMEOUT);
    }
}
