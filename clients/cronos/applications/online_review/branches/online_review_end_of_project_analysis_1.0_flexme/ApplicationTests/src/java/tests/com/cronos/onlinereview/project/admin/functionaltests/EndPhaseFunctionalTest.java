/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import junit.framework.TestCase;

/**
 * @author TCSTester
 * @version 1.0
 */
public class EndPhaseFunctionalTest extends TestCase {
    /**
     * <p>Scenario #164</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Submission" tab
     * 4.  User confirms Phase Timeline Status for Submission shows "Open"
     * 5.  User confirms # of submissions "Passed" is greater than, or equal to, "# of
     * Required Passing Submissions"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Phase is ended and System advances project to next phase. </p>
     */
    public void testScenario164() throws Exception {
    }

    /**
     * <p>Scenario #165</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Submission" tab
     * 4.  User confirms Submission phase did not end prior to set Submission Phase end
     * date
     * 5.  User confirms # of submissions "Passed" is greater than, or equal to, # of
     * Required Passing Submissions
     * 6.  Within "Submission Phase", user confirms "Manual Screening" check box is
     * checked
     * 7.  User confirms "Screening Result" column for all submissions reads "Passed"
     * 8.  User clicks on score of each submission and confirms submissions have passed
     * manual screening
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Phase is ended and System advances project to next phase. </p>
     */
    public void testScenario165() throws Exception {
    }

    /**
     * <p>Scenario #166</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Submission" tab
     * 4.  # of submissions is less than # of required passing submissions
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Phase is not ended and System does not advance project of next phase. </p>
     */
    public void testScenario166() throws Exception {
    }

    /**
     * <p>Scenario #167</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Submission" tab
     * 4.  # of passing submissions is less than # of required passing submissions
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * System does not advance project o next phase. </p>
     */
    public void testScenario167() throws Exception {
    }

    /**
     * <p>Scenario #168</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Submission" tab
     * 4.  User changes Phase end date by shortening time frame by 2 weeks
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Submission Phase End Date CANNOT be shortened. </p>
     */
    public void testScenario168() throws Exception {
    }

    /**
     * <p>Scenario #169</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  User confirms Submission Phase within Project Timeline shows "Closed"
     * 5.  User confirms all test cases ­ Stress, Failure, and Accuracy ­ have been
     * uploaded
     * 6.  User confirms that Submissions have "Scores"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Criteria are met, Phase is ended, and System advances project to next phase. </p>
     */
    public void testScenario169() throws Exception {
    }

    /**
     * <p>Scenario #170</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  Submission phase has not ended
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Criteria are not met and System is not able advance project to next phase.
     * </p>
     */
    public void testScenario170() throws Exception {
    }

    /**
     * <p>Scenario #171</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  User notices at least one of the test cases is missing
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Criteria are not met and System is not able advance project to next phase.
     * </p>
     */
    public void testScenario171() throws Exception {
    }

    /**
     * <p>Scenario #172</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  User notices at least one of the reviews has not been completed
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Criteria are not met and System is not able advance project to next phase.
     * </p>
     */
    public void testScenario172() throws Exception {
    }

    /**
     * <p>Scenario #173</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  User confirms Review Phase within Project Timeline shows "Closed"
     * 5.  User confirms that "Appeals/Response" end date did not end early
     * 6.  User confirms that "Appeals/Response" end date is prior to current date
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Criteria are met, Phase is ended, and System advances project to next phase. </p>
     */
    public void testScenario173() throws Exception {
    }

    /**
     * <p>Scenario #174</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  Review phase has not ended
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Criteria are not met and System is not able advance project to next phase.
     * </p>
     */
    public void testScenario174() throws Exception {
    }

    /**
     * <p>Scenario #175</p>
     * <pre>
     * 1.  "User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  Appeals/Response" phase was ended prior to schedule end date.
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Criteria are not met and System is not able advance project to next phase.
     * </p>
     */
    public void testScenario175() throws Exception {
    }

    /**
     * <p>Scenario #176</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  User confirms Appeals Phase has ended
     * 5.  User confirms All Appeals have responses
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Criteria are met, Phase is ended, Winner is displayed, and System advances project to next phase. </p>
     */
    public void testScenario176() throws Exception {
    }

    /**
     * <p>Scenario #177</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  Appeals phase has not ended
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Criteria are not met and System is not able advance project to next phase.
     * </p>
     */
    public void testScenario177() throws Exception {
    }

    /**
     * <p>Scenario #178</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  All Appeals do not have responses.
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Criteria are not met and System is not able advance project to next phase.
     * </p>
     */
    public void testScenario178() throws Exception {
    }

    /**
     * <p>Scenario #179</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation" tab
     * 4.  User confirms Appeals Response Phase has ended ­ "Appeals Phase" in the
     * Project Timeline should read "Closed"
     * 5.  User clicks on "View Results" to view Aggregation
     * 6.  User confirms Aggregation is complete
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Criteria are met, Phase is ended, and System advances project to next phase. </p>
     */
    public void testScenario179() throws Exception {
    }

    /**
     * <p>Scenario #180</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation" tab
     * 4.  Appeals Response Phase has not ended
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Criteria are not met and System is not able advance project to next phase.
     * </p>
     */
    public void testScenario180() throws Exception {
    }

    /**
     * <p>Scenario #181</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation" tab
     * 4.  User notes that Aggregation is not complete
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Criteria are not met and System is not able advance project to next phase.
     * </p>
     */
    public void testScenario181() throws Exception {
    }

    /**
     * <p>Scenario #182</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation" tab
     * 4.  User confirms Aggregation Phase has ended ­ Aggregation worksheet is
     * complete
     * 5.  User clicks on "View Results" to view Review
     * 6.  User confirms an Aggregation Review exists and is complete
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Criteria are met, Phase is ended, and System advances project to next phase. </p>
     */
    public void testScenario182() throws Exception {
    }

    /**
     * <p>Scenario #183</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation" tab
     * 4.  Aggregation Phase has not ended
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Criteria are not met and System is not able advance project to next phase.
     * </p>
     */
    public void testScenario183() throws Exception {
    }

    /**
     * <p>Scenario #184</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation" tab
     * 4.  User notes that Aggregation Review is not complete
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Criteria are not met and System is not able advance project to next phase.
     * </p>
     */
    public void testScenario184() throws Exception {
    }

    /**
     * <p>Scenario #185</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Final Fix/Review" tab
     * 4.  User confirms Aggregation Review Phase has ended ­ Aggregation Review
     * within Timeline shows "Complete"
     * 5.  User confirms "Final Fixes" have been uploaded
     * 6.  User clicks on "View Results" to ensure submitter has approved aggregation
     * scorecard comments
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Criteria are met, Phase is ended, and System advances project to next phase. </p>
     */
    public void testScenario185() throws Exception {
    }

    /**
     * <p>Scenario #186</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Final Fix/Review" tab
     * 4.  Aggregation Review Phase has not ended
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Criteria are not met and System is not able advance project to next phase.
     * </p>
     */
    public void testScenario186() throws Exception {
    }

    /**
     * <p>Scenario #187</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Final Fix/Review" tab
     * 4.  User notes that Final Fixes have not been uploaded
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Criteria are not met and System is not able advance project to next phase.
     * </p>
     */
    public void testScenario187() throws Exception {
    }

    /**
     * <p>Scenario #188</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Final Fix/Review" tab
     * 4.  User notes that aggregation scorecard comments have not been approved by
     * submitter
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Criteria are not met and System is not able advance project to next phase.
     * </p>
     */
    public void testScenario188() throws Exception {
    }

    /**
     * <p>Scenario #189</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Final Fix/Review" tab
     * 4.  User confirms Final Fix Phase has ended ­ Final Fixes Phase within Timeline
     * shows "Complete"
     * 5.  User clicks on "View Results" and confirms Final Review is complete
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Criteria are met, Phase is ended, and System advances project to next phase. </p>
     */
    public void testScenario189() throws Exception {
    }

    /**
     * <p>Scenario #190</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Final Fix/Review" tab
     * 4.  Final Fixes Phase has not ended
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Criteria are not met and System is not able advance project to next phase.
     * </p>
     */
    public void testScenario190() throws Exception {
    }

    /**
     * <p>Scenario #191</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Final Fix/Review" tab
     * 4.  User notes that Final Review has not been completed
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Criteria are not met and System is not able advance project to next phase.
     * </p>
     */
    public void testScenario191() throws Exception {
    }

    /**
     * <p>Scenario #192</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Approval" tab
     * 4.  Final Review Phase has ended ­ Final Review Phase within Timeline shows
     * "Complete"
     * 5.  User clicks on "View Results"
     * 6.  Approval Scorecard is complete
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Criteria are met, Phase is ended, and System advances project to next phase. </p>
     */
    public void testScenario192() throws Exception {
    }

    /**
     * <p>Scenario #193</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Approval" tab
     * 4.  Final Review Phase has not ended
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Criteria are not met and System is not able advance project to next phase.
     * </p>
     */
    public void testScenario193() throws Exception {
    }

    /**
     * <p>Scenario #194</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Approval" tab
     * 4.  User notes that Approval Scorecard has not been completed
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Criteria are not met and System is not able advance project to next phase.
     * </p>
     */
    public void testScenario194() throws Exception {
    }
}
