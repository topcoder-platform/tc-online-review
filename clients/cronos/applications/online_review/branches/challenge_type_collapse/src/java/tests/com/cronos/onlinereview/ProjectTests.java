/*
 * Copyright (C) 2011 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.util.HashMap;
import java.util.Map;

/**
 * Online review functional tests, project based tests.
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
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

        TestHelper.deleteTCDirectProject();
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
        try {
            if (projectId != -1) {
                TestHelper.deleteProject(browser, projectId);
            }
            TestHelper.deleteTCDirectProject();

            projectId = -1;
            // logout the user
            browser.click("link=Logout");
            browser.waitForPageToLoad(TIMEOUT);
            assertNoErrorsOccurred();
        } finally {
            super.tearDown();
        }
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
