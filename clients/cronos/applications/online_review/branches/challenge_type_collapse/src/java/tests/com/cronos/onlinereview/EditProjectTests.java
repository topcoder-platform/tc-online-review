/*
 * Copyright (C) 2010 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

/**
 * Edit project tests.
 *
 * @author TCSASSEMBLER
 * @version 2.0
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
        TestHelper.clickEditProjectLink(browser);
        browser.select("resources_role[0]", "label=Designer");
        browser.type("resources_name[0]", "super");
        browser.click("//img[@alt='Add']");
        browser.type("explanation", "Add designer");
        browser.click("//input[@alt='Save Changes']");
        browser.waitForPageToLoad(TIMEOUT);
        assertTrue("Resources should be shown", browser.isTextPresent("Resources"));
        assertTrue("designer should be added", browser.isTextPresent("super"));   
        assertNoErrorsOccurred();
    }
}
