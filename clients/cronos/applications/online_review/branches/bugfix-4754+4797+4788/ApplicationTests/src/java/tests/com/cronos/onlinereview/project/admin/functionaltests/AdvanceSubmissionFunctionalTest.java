/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import junit.framework.TestCase;

/**
 * @author TCSTester
 * @version 1.0
 */
public class AdvanceSubmissionFunctionalTest extends TestCase {
    
    /**
     * <p>Scenario #195</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Submission/Screening" tab
     * 4.  There are no outstanding deliverables for Submitters
     * 5.  Screening Result for all Submissions are labeled "Passed"
     * 6.  The next phase in the Timelines is labeled "Open"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * System advances submission to next phase. </p>
     */
    public void testScenario195() throws Exception {
    }

    /**
     * <p>Scenario #196</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Submission/Screening" tab
     * 4.  There are outstanding deliverables for Submitters
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Subsequent phase in not open and System is not able to advance submission to next phase. </p>
     */
    public void testScenario196() throws Exception {
    }

    /**
     * <p>Scenario #197</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  Subsequent phase is not labeled "Open"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * System is not able to advance submission to next phase. </p>
     */
    public void testScenario197() throws Exception {
    }
}
