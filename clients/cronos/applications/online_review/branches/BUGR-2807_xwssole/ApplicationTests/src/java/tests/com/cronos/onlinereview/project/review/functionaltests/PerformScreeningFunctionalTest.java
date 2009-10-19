/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.Messages;
import com.cronos.onlinereview.project.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import org.dbunit.dataset.IDataSet;

import junit.framework.Assert;

/**
 * <p>A test case for <code>Perform Screening</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class PerformScreeningFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the sample project (as displayed by the list of projects) which is
     * used for testing purposes.</p>
     */
    public static final String PROJECT_NAME = "Ajax Timed Survey version 1.0";

    /**
     * <p>A <code>String</code> array providing the IDs of submissions with screening results which are used for testing
     * purposes.</p>
     */
    public static final String[] SUBMISSION_IDS = new String[] {"1", "2", "3"};

    /**
     * <p>A <code>String</code> array providing the usernames for users who are granted the permission to view the
     * aggregation review results for selected project.</p>
     */
    public static final String[] UNAUTHORIZED_USERS
        = new String[] {UserSimulator.APPROVER, UserSimulator.DESIGNER, UserSimulator.WINNING_SUBMITTER,
                        UserSimulator.SECOND_PLACE_SUBMITTER, UserSimulator.THIRD_PLACE_SUBMITTER,
                        UserSimulator.REVIEWER1, UserSimulator.REVIEWER2, UserSimulator.OBSERVER,
                        UserSimulator.MANAGER};

    /**
     * <p>Scenario #90</p>
     * <pre>
     * 1.  User logs in
     * 2.  User clicks on "All Open Projects" tab
     * 3.  User views project list and selects a project
     * 4.  User clicks on "Submission/Screening" tab
     * 5.  User selects submission to screen and clicks on "Submit Scorecard"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating User does NOT have permission to Submit a Scorecard </p>
     */
    public void testScenario90() throws Exception {
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            for (int j = 0; j < UNAUTHORIZED_USERS.length; j++) {
                setUser(UNAUTHORIZED_USERS[j]);
                this.user.openScreeningScorecardPage(PROJECT_NAME, SUBMISSION_IDS[i]);
                assertDisplayedMessage(Messages.getNoPermissionPerformScreening());
            }
        }
    }

    /**
     * <p>Scenario #91</p>
     * <pre>
     * Note: User is logged-in as a "Screener" or "Primary Screener"
     * 1.  User logs in
     * 2.  User clicks on "All Open Projects" tab
     * 3.  User views project list and selects a project
     * 4.  User clicks on "Submission/Screening" tab
     * 5.  User selects submission to screen and clicks on "Submit Scorecard"
     * 6.  User completes all drop-downs and text boxes contained in Screening Scorecard
     * ­ all drop-downs and test boxes are required to be completed
     * 7.  User clicks on "Preview"
     * 8.  User completes preview of scorecard and clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Scorecard Score is computed; Scorecard is saved, submitted, and marked as "Complete" in the submission list.
     * </p>
     */
    public void testScenario91() throws Exception {
        setUser(UserSimulator.PRIMARY_REVIEWER);
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            this.user.openScreeningScorecardPage(PROJECT_NAME, SUBMISSION_IDS[i]);
            this.user.submitScreening("1", "3", "test_screeningresponsetext01", true, true, true);
            assertData("data/expected/PerformScreeningCommitted" + SUBMISSION_IDS[i] + ".xml",
                       "The Screening Scorecard is not committed correctly");
            assertScreeningResult(SUBMISSION_IDS[i], true);
        }
    }

    /**
     * <p>Scenario #92</p>
     * <pre>
     * Note: User is logged-in as a "Screener" or "Primary Screener"
     * 1.  User logs in
     * 2.  User clicks on "All Open Projects" tab
     * 3.  User views project list and selects a project
     * 4.  User clicks on "Submission/Screening" tab
     * 5.  User selects submission to screen and clicks on "Submit Scorecard"
     * 6.  User completes all drop-downs and text boxes contained in Screening Scorecard
     * ­ all drop-downs and test boxes are required to be completed
     * 7.  User clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Scorecard Score is computed; Scorecard is saved, submitted, and marked as "Complete" in the submission list.
     * </p>
     */
    public void testScenario92() throws Exception {
        setUser(UserSimulator.PRIMARY_REVIEWER);
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            this.user.openScreeningScorecardPage(PROJECT_NAME, SUBMISSION_IDS[i]);
            this.user.submitScreening("1", "3", "test_screeningresponsetext01", true, true, false);
            assertData("data/expected/PerformScreeningCommitted" + SUBMISSION_IDS[i] + ".xml",
                       "The Screening Scorecard is not committed correctly");
            assertScreeningResult(SUBMISSION_IDS[i], true);
        }
    }

    /**
     * <p>Scenario #93</p>
     * <pre>
     * Note: User is logged-in as a "Screener" or "Primary Screener"
     * 1.  User logs in
     * 2.  User clicks on "All Open Projects" tab
     * 3.  User views project list and selects a project
     * 4.  User clicks on "Submission/Screening" tab
     * 5.  User selects submission to screen and clicks on "Submit Scorecard"
     * 6.  User completes 5 of required drop-down lists and 3 of required text boxes
     * 7.  User clicks on "Save for Later"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Scorecard is saved and marked as "Pending" in the submission list. </p>
     */
    public void testScenario93() throws Exception {
        setUser(UserSimulator.PRIMARY_REVIEWER);
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            this.user.openScreeningScorecardPage(PROJECT_NAME, SUBMISSION_IDS[i]);
            this.user.submitScreening("1", "3", "test_screeningresponsetext01", false, true, false);
            assertData("data/expected/PerformScreeningPending" + SUBMISSION_IDS[i] + ".xml",
                       "The Screening Scorecard is not saved correctly");
            assertScreeningResult(SUBMISSION_IDS[i], false);
        }
    }

    /**
     * <p>Scenario #94</p>
     * <pre>
     * Note: User is logged-in as a "Screener" or "Primary Screener"
     * 1.  User logs in
     * 2.  User clicks on "All Open Projects" tab
     * 3.  User views project list and selects a project
     * 4.  User clicks on "Submission/Screening" tab
     * 5.  User selects submission to screen and clicks on "Submit Scorecard"
     * 6.  User completes 5 of required drop-down lists and 3 of required text boxes
     * 7.  User clicks on "Preview"
     * 8.  User completes preview of scorecard and clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating all required fields within Scorecard MUST be completed. </p>
     */
    public void testScenario94() throws Exception {
        setUser(UserSimulator.PRIMARY_REVIEWER);
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            this.user.openScreeningScorecardPage(PROJECT_NAME, SUBMISSION_IDS[i]);
            this.user.submitScreening("1", "3", "", true, true, true);
            assertDisplayedMessage(Messages.getInvalidInputScreeningIncomplete());
        }
    }

    /**
     * <p>Scenario #95</p>
     * <pre>
     * Note: User is logged-in as a "Screener" or "Primary Screener"
     * 1.  User logs in
     * 2.  User clicks on "All Open Projects" tab
     * 3.  User views project list and selects a project
     * 4.  User clicks on "Submission/Screening" tab
     * 5.  User selects submission to screen and clicks on "Submit Scorecard"
     * 6.  User completes 5 of required drop-down lists and 3 of required text boxes
     * 7.  User clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating all required fields within Scorecard MUST be completed. </p>
     */
    public void testScenario95() throws Exception {
        setUser(UserSimulator.PRIMARY_REVIEWER);
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            this.user.openScreeningScorecardPage(PROJECT_NAME, SUBMISSION_IDS[i]);
            this.user.submitScreening("1", "3", "", true, true, false);
            assertDisplayedMessage(Messages.getInvalidInputScreeningIncomplete());
        }
    }

    /**
     * <p>Gets the data sets specific to test case which must be used to populate the database tables with initial data.
     * </p>
     *
     * @return an <code>IDataSet</code> array providing the data sets specific to test case.
     * @throws Exception if an unexpected error occurs.
     */
    protected IDataSet[] getDataSets() throws Exception {
        return new IDataSet[] {getRegistrationPhaseData(), getSubmissionPhaseData(), getScreeningPhaseData()};
    }

    /**
     * <p>Verifies that the status of screening scorecard for specified submission is displayed correctly.</p>
     *
     * @param submissionId a <code>String</code> providing the ID of requested submission.
     * @param committed <code>true</code> if the scorecard is expected to be committed; <code>false</code> otherwise.
     * @throws Exception if an unexpected error occurs.
     */
    private void assertScreeningResult(String submissionId, boolean committed) throws Exception {
        this.user.openSubmissionScreeningTab(PROJECT_NAME);
        String screeningResult = this.user.getScreeningResult(submissionId);
        HtmlAnchor screeningLink = this.user.getScreeningScorecardLink(submissionId);
        if (committed) {
            Assert.assertEquals("The screening result is not correct", "Passed", screeningResult);
            Assert.assertEquals("The screening score link is not correct", "75.00", screeningLink.asText());
        } else {
            Assert.assertEquals("The screening result is not correct", "N/A", screeningResult);
            Assert.assertEquals("The screening score link is not correct", "Submit Scorecard", screeningLink.asText());
        }
    }
}
