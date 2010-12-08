/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

/**
 * Online review functional tests 1, link different projects.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class LinkProjectsTests extends ProjectTests {

	/** 
     * Represents the project id to be tested. 
     */
    protected long projectId2 = -1;
    
    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {
    	super.setUp();
    	projectId2 = TestHelper.getProjectId();
    	// created the project with the given generated id
    	TestHelper.createProject(projectId, projectId2, false, true);
    }

    /**
     * Tears down the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void tearDown() throws Exception {
    	if (projectId2 != -1) {
    		TestHelper.deleteProject(browser, projectId2);
    	}
    	projectId2 = -1;
        super.tearDown();
    }

    /**
     * Test Case Number: FTC26 RS4 Verify Manager can add to the project a "Is Related To" link.
     *
     * @throws Exception if any error occurs
     */
    public void testAddProjectLink() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        browser.click("//img[@alt='Edit Project Link']");
        browser.waitForPageToLoad(TIMEOUT);
        // select the "Is Related To" option, no error expected
        browser.select("link_type_id[0]", "label=Is Related To");
		browser.select("link_dest_id[0]", "value=" + (projectId2));
		browser.click("//img[@alt='add']");
		browser.click("//input[@name='']");
		browser.waitForPageToLoad(TIMEOUT);
		assertTrue("link is added", browser.isTextPresent("Integration Test Case 1 (project 2)-"));
    }
}
