/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

/**
 * Online review functional tests 3, edit project fileds testing
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class EditProjectFiledsTests extends ProjectTests {
	
    /**
     * Test Case Number: FTC60 RS5.4 Verify Manager can Turn on AutoPilot
     *
     * @throws Exception if any error occurs
     */
    public void testTurnonAutoPilot() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        // AutoPilot should be off at the beginning
        String autoPilotStat = browser.getText("//div[@id='mainMiddleContent']/div/table[4]/tbody/tr[8]/td[2]");
        assertEquals("AutoPilot should be turned off", "Off", autoPilotStat);

        // Click the 'Edit Project' Link
        browser.click("//img[@alt='Edit Project']");
        browser.waitForPageToLoad(TIMEOUT);

        // Turn on AutoPilot, no error expected
        browser.check("//input[@id='autopilotOnRadioBox']");
        browser.type("explanation", "auto pilot turn on");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);

        autoPilotStat = browser.getText("//div[@id='mainMiddleContent']/div/table[4]/tbody/tr[8]/td[2]");
        assertEquals("AutoPilot should be turned on", "On", autoPilotStat);
        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC99 RS5.8 Verify Manager can Edit Name field
     *
     * @throws Exception if any error occurs
     */
    public void testEditNameField() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        // check the project name at the beginning
        String projectName = browser.getText("//table[@id='table12']/tbody/tr[1]/td[3]/span");
        assertFalse("Project Name should not be 'testEditNameField update name'", "testEditNameField update name".equalsIgnoreCase(projectName));

        // Click the 'Edit Project' Link
        browser.click("//img[@alt='Edit Project']");
        browser.waitForPageToLoad(TIMEOUT);

        // edit the project name
        browser.type("project_name", "testEditNameField update name");
        browser.type("explanation", "Name field edition");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);

        projectName = browser.getText("//table[@id='table12']/tbody/tr[1]/td[3]/span");
        assertEquals("Project Name should be changed to testEditNameField update name", "testEditNameField update name", projectName);

        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC100 RS5.8 Verify Manager can Edit Type field
     *
     * @throws Exception if any error occurs
     */
    public void testEditTypeField() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        // check the project type at the beginning 
        String projectType = browser.getText("//div[@id='mainMiddleContent']/div/table[4]/tbody/tr[3]/td[2]");
        assertFalse("Project Type Should not be Studio", "Studio".equalsIgnoreCase(projectType));

        // Click the 'Edit Project' Link
        browser.click("//img[@alt='Edit Project']");
        browser.waitForPageToLoad(TIMEOUT);

        // select the "studio" type, no error expected
        browser.select("project_type", "label=Studio");
        browser.type("explanation", "type field edition");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);

        projectType = browser.getText("//div[@id='mainMiddleContent']/div/table[4]/tbody/tr[3]/td[2]");
        assertEquals("Project Type should be changed to Studio", "Studio", projectType);

        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC101 RS5.8 Verify Manager can Edit Category field
     *
     * @throws Exception if any error occurs
     */
    public void testEditCategoryField() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        // check the project Category at the beginning
        String projectCategory = browser.getText("//div[@id='mainMiddleContent']/div/table[4]/tbody/tr[4]/td[2]");
        assertFalse("Project Category should not be Testing Competition", "Testing Competition".equalsIgnoreCase(projectCategory));

        // Click the 'Edit Project' Link
        browser.click("//img[@alt='Edit Project']");
        browser.waitForPageToLoad(TIMEOUT);

        // select the "Testing Competition" category, no error expected
        browser.select("project_category", "label=Testing Competition");
        browser.type("explanation", "Category field edition");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);

        projectCategory = browser.getText("//div[@id='mainMiddleContent']/div/table[4]/tbody/tr[4]/td[2]");
        assertEquals("Project Category should be changed to Testing Competition", "Testing Competition", projectCategory);

        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC102 RS5.8 Verify Manager can Edit Price field
     *
     * @throws Exception if any error occurs
     */
    public void testEditPriceField() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);

        String price = browser.getText("//div[@id='mainMiddleContent']/div/table[4]/tbody/tr[5]/td[2]");
        // check the price is not $100 at the beginning
        assertFalse("The price should not be $100", "$100".equals(price));

        // Click the 'Edit Project' Link
        browser.click("//img[@alt='Edit Project']");
        browser.waitForPageToLoad(TIMEOUT);

        // input 100$ as payments, no error expected
        browser.type("payments", "100");
        browser.type("explanation", "Price field edition");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);

        price = browser.getText("//div[@id='mainMiddleContent']/div/table[4]/tbody/tr[5]/td[2]");
        assertEquals("Price should be changed to $100", "$100", price);

        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC103 RS5.8 Verify Manager can Edit DR points field
     *
     * @throws Exception if any error occurs
     */
    public void testEditDRPointsField() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);

        String point = browser.getText("//div[@id='mainMiddleContent']/div/table[4]/tbody/tr[7]/td[2]");
        // check the point is not 100 at the beginning
        assertFalse("The point should not be 100", "100".equals(point));

        // Click the 'Edit Project' Link
        browser.click("//img[@alt='Edit Project']");
        browser.waitForPageToLoad(TIMEOUT);

        // input 100 as point, no error expected
        browser.type("dr_points", "100");
        browser.type("explanation", "DR Points field edition");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);

        point = browser.getText("//div[@id='mainMiddleContent']/div/table[4]/tbody/tr[7]/td[2]");
        assertEquals("Point should be changed to 100", "100", point);

        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC104 RS5.9 Verify Manager can Edit Notes field
     *
     * @throws Exception if any error occurs
     */
    public void testEditNotesField() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        // check the notes at the beginning
        String notes = browser.getText("//div[@id='mainMiddleContent']/div/table[2]/tbody/tr[2]/td[1]");
        assertFalse("Notes should not be 'testEditNotesField update note'", "testEditNotesField update note".equalsIgnoreCase(notes));

        // Click the 'Edit Project' Link
        browser.click("//img[@alt='Edit Project']");
        browser.waitForPageToLoad(TIMEOUT);


        // input notes, no error expected
        browser.type("notes", "testEditNotesField update note");
        browser.type("explanation", "Notes field edition");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);

        notes = browser.getText("//div[@id='mainMiddleContent']/div/table[2]/tbody/tr[2]/td[1]");
        assertEquals("Notes should be changed to testEditNotesField update note", "testEditNotesField update note", notes);

        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC105 RS5.10 Verify Manager can Edit Contest Status field
     *
     * @throws Exception if any error occurs
     */
    public void testEditContestStatusField() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        // check the status at the beginning
        String status = browser.getText("//div[@id='mainMiddleContent']/div/table[4]/tbody/tr[10]/td[2]");
        assertFalse("Status should not be Completed", "Completed".equalsIgnoreCase(status));

        // Click the 'Edit Project' Link
        browser.click("//img[@alt='Edit Project']");
        browser.waitForPageToLoad(TIMEOUT);

        // select 'Completed' as status, no error expected
        browser.select("//select[@name='status']", "label=Completed");
        browser.type("explanation", "Contest Status field edition");
        browser.type("status_explanation", "Contest Status changed to Completed");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);

        status = browser.getText("//div[@id='mainMiddleContent']/div/table[4]/tbody/tr[12]/td[2]");
        assertEquals("Status should be changed to Completed", "Completed", status);

        assertNoErrorsOccurred();
    }


    /**
     * Test Case Number: FTC107 RS6 Verify Manager can disable Recieving timeline change notifications
     *
     * @throws Exception if any error occurs
     */
    public void testDisableTimelineNotification() throws Exception {
        // login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);

        // disable Recieving timeline change notifications
        browser.check("//input[@id='notifCheckbox']");
        browser.uncheck("//input[@id='notifCheckbox']");

        // check the value
        String value = browser.getValue("//input[@id='notifCheckbox']");
        assertEquals("Recieve timeline change notifications should be disabled", "off", value);
        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC108 RS6 Verify Manager can Recieve timeline change notifications
     *
     * @throws Exception if any error occurs
     */
    public void testEnableTimelineNotification() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        
        // enable Recieving timeline change notifications
        browser.check("//input[@id='notifCheckbox']");

        // check the value
        String value = browser.getValue("//input[@id='notifCheckbox']");
        assertEquals("Recieve timeline change notifications should be enabled", "on", value);
        assertNoErrorsOccurred();
    }


}
