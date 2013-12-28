package com.cronos.onlinereview;

import java.util.HashMap;
import java.util.Map;

public class ProjectTests extends BaseTests {
	
    /** 
     * Represents the project id to be tested. 
     */
    protected long projectId = -1;
    
    /**
     * Represents map with phase ids.
     */
    protected Map<String, Long> phaseIds = new HashMap<String, Long>();

    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {
    	super.setUp();

        TestHelper.createTCDirectProject();

    	projectId = TestHelper.getNextProjectId();
    	createProject();
    }
    
    /**
     * Tears down the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void tearDown() throws Exception {
    	if (projectId != -1) {
    		TestHelper.deleteProject(browser, projectId);
    	}
        TestHelper.deleteTCDirectProject();

        projectId = -1;
        // logout the user
        browser.click("link=Logout");
        browser.waitForPageToLoad(TIMEOUT);
        assertNoErrorsOccurred();
        super.tearDown();
    }

    /**
     * Create a new project.
     * 
     * @throws Exception if any error occurs.
     */
    protected void createProject() throws Exception {
        TestHelper.createProject(projectId, getName(), phaseIds);
    }
}
