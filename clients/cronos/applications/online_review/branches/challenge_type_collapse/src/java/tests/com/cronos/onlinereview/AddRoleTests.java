/*
 * Copyright (C) 2011 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

/**
 * Online review functional tests 3, add roles to a project testing.
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class AddRoleTests extends ProjectTests {
    
    /**
     * Test Case Number: FTC61 RS5.5 Verify Manager can add a Primary Screener Role to a project
     *
     * @throws Exception if any error occurs
     */
    public void testAddPrimaryScreenerRole() throws Exception {
        // login the user first
        TestHelper.loginUser(browser);
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        TestHelper.clickEditProjectLink(browser);

        // select the "Primary Screener" role, no error expected
        browser.select("resources_role[0]", "label=Primary Screener");
        // input the  competitor name
        browser.type("resources_name[0]", TestHelper.getCompetitorUsername());
        browser.click("//img[@alt='Add']");
        // add explanation
        browser.type("explanation", "add primary screener role");
        browser.click("//input[@alt='Save Changes']");
        browser.waitForPageToLoad(TIMEOUT);

        String role = browser.getText("//div[@id='mainMiddleContent']/div/table[7]/tbody/tr[3]/td[1]");
        String name = browser.getText("//div[@id='mainMiddleContent']/div/table[7]/tbody/tr[3]/td[2]");
        assertEquals("Primary Screener should be added", TestHelper.getCompetitorUsername(), name);
        assertEquals("Primary Screener should be added", "Primary Screener", role);

        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC71 RS5.5 Verify Manager can add Manager Role to a project
     *
     * @throws Exception if any error occurs
     */
    public void testAddManagerRole() throws Exception {
        // login the user first
        TestHelper.loginUser(browser);
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        // Click the 'Edit Project' Link
        TestHelper.clickEditProjectLink(browser);

        // select the "Manager" role, no error expected
        browser.select("resources_role[0]", "label=Manager");
        // input the name 'twight'
        browser.type("resources_name[0]", TestHelper.getCompetitorUsername());
        browser.click("//img[@alt='Add']");
        // add explanation
        browser.type("explanation", "add manager role");
        browser.click("//input[@alt='Save Changes']");
        browser.waitForPageToLoad(TIMEOUT);

        String role = browser.getText("//div[@id='mainMiddleContent']/div/table[7]/tbody/tr[3]/td[1]");
        String name = browser.getText("//div[@id='mainMiddleContent']/div/table[7]/tbody/tr[3]/td[2]");
        assertEquals("Manager should be added", TestHelper.getCompetitorUsername(), name);
        assertEquals("Manager should be added", "Manager", role);

        assertNoErrorsOccurred();
    }

}
