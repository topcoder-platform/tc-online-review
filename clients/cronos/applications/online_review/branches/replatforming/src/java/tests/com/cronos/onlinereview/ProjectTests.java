package com.cronos.onlinereview;

public class ProjectTests extends BaseTests {
	
    /** 
     * Represents the project id to be tested. 
     */
    protected long projectId = -1;

    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {
    	super.setUp();
    	projectId = TestHelper.getProjectId();
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
        projectId = -1;
        super.tearDown();
    }

}
