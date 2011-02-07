/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

/**
 * Online review functional tests 1, link different projects.
 *
 * @author TCSDEVELOPER
 * @version 1.1
 * @since 1.0
 */
public class LinkProjectsTests extends ProjectTests {

	/** 
     * Represents the project id to be tested. 
     */
    protected long project2Id = -1;
    
    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {
    	super.setUp();
    	
    	// create project to link.
    	project2Id = TestHelper.getNextProjectId();
    	TestHelper.createProject(project2Id, getName()+"2");
    }

    /**
     * Tears down the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void tearDown() throws Exception {
    	if (project2Id != -1) {
    		TestHelper.deleteProject(browser, project2Id);
    	}
    	project2Id = -1;
        super.tearDown();
    }

    /**
     * Test Case Number: FTC25 RS4 Verify Manager can add to the project a "Depends On" link.
     *
     * @throws Exception if any error occurs
     * @version 1.1
     */
    public void testAddProjectLinkDependsOn() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        browser.click("//img[@alt='Edit Project Link']");
        browser.waitForPageToLoad(TIMEOUT);
        // select the "Depends On" option, no error expected
        browser.select("link_type_id[0]", "label=Depends On");
		browser.select("link_dest_id[0]", "value=" + (project2Id));
		browser.click("//img[@alt='add']");
		browser.click("//input[@name='']");
		browser.waitForPageToLoad(TIMEOUT);
        String type = browser.getText("//div[@id='mainMiddleContent']/div/table[6]/tbody/tr[3]/td[1]");
        String linkName = browser.getText("//div[@id='mainMiddleContent']/div/table[6]/tbody/tr[3]/td[2]");
        assertTrue("link is added", linkName.startsWith("Test Case " + getName()));
        assertEquals("link is added", "Depends On", type);
    }

    /**
     * Test Case Number: FTC26 RS4 Verify Manager can add to the project a "Is Related To" link.
     *
     * @throws Exception if any error occurs
     */
    public void testAddProjectLinkIsRelatedTo() throws Exception {
    	// login the user first
    	TestHelper.loginUser(browser);
    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        browser.click("//img[@alt='Edit Project Link']");
        browser.waitForPageToLoad(TIMEOUT);
        // select the "Is Related To" option, no error expected
        browser.select("link_type_id[0]", "label=Is Related To");
		browser.select("link_dest_id[0]", "value=" + (project2Id));
		browser.click("//img[@alt='add']");
		browser.click("//input[@name='']");
		browser.waitForPageToLoad(TIMEOUT);
		String type = browser.getText("//div[@id='mainMiddleContent']/div/table[6]/tbody/tr[3]/td[1]");
        String linkName = browser.getText("//div[@id='mainMiddleContent']/div/table[6]/tbody/tr[3]/td[2]");
		assertTrue("link is added", linkName.startsWith("Test Case " + getName()));
		assertEquals("link is added", "Is Related To", type);
    }

    /**
     * Test Case Number: FTC29 RS4 Verify Manager can add to the project more than one link.
     *
     * @throws Exception if any error occurs
     * @version 1.1
     */
    public void testAddProjectLinks() throws Exception {
    	long project3Id = TestHelper.getNextProjectId();
    	TestHelper.createProject(project3Id, getName());
    	try {
	    	// login the user first
	    	TestHelper.loginUser(browser);
	    	browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
	        browser.click("//img[@alt='Edit Project Link']");
	        browser.waitForPageToLoad(TIMEOUT);
	        // select the "Repost For" option, no error expected
	        browser.select("link_type_id[0]", "label=Repost For");
			browser.select("link_dest_id[0]", "value=" + project2Id);
			browser.click("//img[@alt='add']");
	        browser.select("link_type_id[0]", "label=Repost For");
			browser.select("link_dest_id[0]", "value=" + project3Id);
			browser.click("//img[@alt='add']");
			browser.click("//input[@name='']");
			browser.waitForPageToLoad(TIMEOUT);
	        String type1 = browser.getText("//div[@id='mainMiddleContent']/div/table[6]/tbody/tr[3]/td[1]");
	        String linkName1 = browser.getText("//div[@id='mainMiddleContent']/div/table[6]/tbody/tr[3]/td[2]");
	        assertTrue("link is added", linkName1.startsWith("Test Case " + getName()));
	        assertEquals("link is added", "Repost For", type1);
	        String type2 = browser.getText("//div[@id='mainMiddleContent']/div/table[6]/tbody/tr[4]/td[1]");
	        String linkName2 = browser.getText("//div[@id='mainMiddleContent']/div/table[6]/tbody/tr[4]/td[2]");
	        assertTrue("link is added", linkName2.startsWith("Test Case " + getName()));
	        assertEquals("link is added", "Repost For", type2);
    	} 
    	finally {
    		TestHelper.deleteProject(browser, project3Id);
    	}
    }
}
