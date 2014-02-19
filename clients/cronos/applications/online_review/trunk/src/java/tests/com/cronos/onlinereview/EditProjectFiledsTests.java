/*
 * Copyright (C) 2011 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

/**
 * Online review functional tests 3, edit project fields testing.
 *
 * @author TCSASSEMBLER
 * @version 2.0
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
        String autoPilotStat = browser.getText("//div[@id='mainMiddleContent']/div/table[4]/tbody/tr[7]/td[2]");
        assertEquals("AutoPilot should be turned off", "Off", autoPilotStat);

        // Click the 'Edit Project' Link
        TestHelper.clickEditProjectLink(browser);

        // Turn on AutoPilot, no error expected
        browser.check("//input[@id='autopilotOnRadioBox']");
        browser.type("explanation", "auto pilot turn on");
        browser.click("//input[@alt='Save Changes']");
        browser.waitForPageToLoad(TIMEOUT);

        autoPilotStat = browser.getText("//div[@id='mainMiddleContent']/div/table[4]/tbody/tr[7]/td[2]");
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
        TestHelper.clickEditProjectLink(browser);

        // edit the project name
        browser.type("project_name", "testEditNameField update name");
        browser.type("explanation", "Name field edition");
        browser.click("//input[@alt='Save Changes']");
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
        TestHelper.clickEditProjectLink(browser);

        // select the "studio" type, no error expected
        browser.select("project_type", "label=Studio");
        browser.type("explanation", "type field edition");
        browser.click("//input[@alt='Save Changes']");
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
        TestHelper.clickEditProjectLink(browser);

        // select the "Testing Competition" category, no error expected
        browser.select("project_category", "label=Testing Competition");
        browser.type("explanation", "Category field edition");
        browser.click("//input[@alt='Save Changes']");
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

        String price = browser.getText("//div[@id='mainMiddleContent']/div/table[5]/tbody/tr[3]/td[1]");
        // check the price is not $100 at the beginning
        assertTrue("These should be no contest prizes", "There are no prizes.".equals(price));

        price = browser.getText("//div[@id='mainMiddleContent']/div/table[6]/tbody/tr[3]/td[1]");
        // check the price is not $100 at the beginning
        assertTrue("These should be no checkpoint prizes", "There are no prizes.".equals(price));

        // Click the 'Edit Project' Link
        TestHelper.clickEditProjectLink(browser);

        // Add two contest prizes
        browser.click("//table[@id='contest-prizes-table']//img[@alt='Add Prize']");
        browser.type("contest_prizes_amount[0]", "1000");
        browser.click("//table[@id='contest-prizes-table']//img[@alt='Add Prize']");
        browser.type("contest_prizes_amount[1]", "600");

        // Add a checkpoint prize
        browser.click("//table[@id='checkpoint-prizes-table']//img[@alt='Add Prize']");
        browser.type("checkpoint_prizes_amount[0]", "300");
        browser.select("//select[@name='checkpoint_prizes_num[0]']", "label=3");

        browser.click("//input[@alt='Save Changes']");
        browser.waitForPageToLoad(TIMEOUT);

        // check first contest prize
        String place = browser.getText("//div[@id='mainMiddleContent']/div/table[5]/tbody/tr[3]/td[1]");
        price = browser.getText("//div[@id='mainMiddleContent']/div/table[5]/tbody/tr[3]/td[2]");
        String noOfPrizes = browser.getText("//div[@id='mainMiddleContent']/div/table[5]/tbody/tr[3]/td[3]");
        assertEquals("Place of first contest prize should be 1", "1", place);
        assertEquals("Amount of first contest prize should be $1,000", "$1,000", price);
        assertEquals("# of Prizes of first contest prize should be 1", "1", noOfPrizes);

        // check second contest prize
        place = browser.getText("//div[@id='mainMiddleContent']/div/table[5]/tbody/tr[4]/td[1]");
        price = browser.getText("//div[@id='mainMiddleContent']/div/table[5]/tbody/tr[4]/td[2]");
        noOfPrizes = browser.getText("//div[@id='mainMiddleContent']/div/table[5]/tbody/tr[4]/td[3]");
        assertEquals("Place of second contest prize should be 2", "2", place);
        assertEquals("Amount of second contest prize should be $600", "$600", price);
        assertEquals("# of Prizes of second contest prize should be 1", "1", noOfPrizes);

        // check first checkpoint prize
        place = browser.getText("//div[@id='mainMiddleContent']/div/table[6]/tbody/tr[3]/td[1]");
        price = browser.getText("//div[@id='mainMiddleContent']/div/table[6]/tbody/tr[3]/td[2]");
        noOfPrizes = browser.getText("//div[@id='mainMiddleContent']/div/table[6]/tbody/tr[3]/td[3]");
        assertEquals("Place of first checkpoint prize should be 1", "1", place);
        assertEquals("Amount of first checkpoint prize should be $300", "$300", price);
        assertEquals("# of Prizes of first checkpoint prize should be 3", "3", noOfPrizes);

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
        TestHelper.clickEditProjectLink(browser);

        // input 100 as point, no error expected
        browser.type("dr_points", "100");
        browser.type("explanation", "DR Points field edition");
        browser.click("//input[@alt='Save Changes']");
        browser.waitForPageToLoad(TIMEOUT);

        point = browser.getText("//div[@id='mainMiddleContent']/div/table[4]/tbody/tr[6]/td[2]");
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
        TestHelper.clickEditProjectLink(browser);


        // input notes, no error expected
        browser.type("notes", "testEditNotesField update note");
        browser.type("explanation", "Notes field edition");
        browser.click("//input[@alt='Save Changes']");
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
        String status = browser.getText("//div[@id='mainMiddleContent']/div/table[4]/tbody/tr[9]/td[2]");
        assertFalse("Status should not be Completed", "Completed".equalsIgnoreCase(status));

        // Click the 'Edit Project' Link
        TestHelper.clickEditProjectLink(browser);

        // select 'Cancelled - Client Request' as status, no error expected
        browser.select("//select[@name='status']", "label=Cancelled - Client Request");
        browser.type("explanation", "Contest Status field edition");
        browser.type("status_explanation", "Contest Status changed to Cancelled - Client Request");
        browser.click("//input[@alt='Save Changes']");
        browser.waitForPageToLoad(TIMEOUT);

        status = browser.getText("//div[@id='mainMiddleContent']/div/table[4]/tbody/tr[14]/td[2]");
        assertEquals("Status should be changed to Cancelled - Client Request", "Cancelled - Client Request", status);

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
