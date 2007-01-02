/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.Messages;
import com.cronos.onlinereview.project.AbstractTestCase;
import org.dbunit.dataset.IDataSet;

/**
 * <p>A test case for <code>Perform Approval</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class PerformApprovalFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the sample project (as displayed by the list of projects) which is
     * used for testing purposes.</p>
     */
    public static final String PROJECT_NAME = "Ajax Timed Survey version 1.0";

    /**
     * <p>A <code>String</code> array providing the usernames for users who are granted the permission to view the
     * aggregation review results for selected project.</p>
     */
    public static final String[] UNAUTHORIZED_USERS
        = new String[] {UserSimulator.PRIMARY_REVIEWER, UserSimulator.DESIGNER, UserSimulator.WINNING_SUBMITTER,
                        UserSimulator.SECOND_PLACE_SUBMITTER, UserSimulator.THIRD_PLACE_SUBMITTER,
                        UserSimulator.REVIEWER1, UserSimulator.REVIEWER2, UserSimulator.OBSERVER,
                        UserSimulator.MANAGER};

    /**
     * <p>Scenario #155</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Approval" tab
     * 4.  User clicks on "Submit Approval"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating user does NOT have permission to view "Submit Approval". </p>
     */
    public void testScenario155() throws Exception {
        for (int j = 0; j < UNAUTHORIZED_USERS.length; j++) {
            setUser(UNAUTHORIZED_USERS[j]);
            this.user.openApprovalScorecardPage(PROJECT_NAME);
            assertDisplayedMessage(Messages.getNoPermissionPerformApproval());
        }
    }

    /**
     * <p>Scenario #156</p>
     * <pre>
     * Note: User is logged-in as "Approver"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Approval" tab
     * 4.  User clicks on "Submit Approval"
     * 5.  User enters sample data below for every "Approver Comments" section:
     * Field Name                                 Sample Data Value
     * Response 1: (drop-down list)               Recommended
     * Response (drop-down list)                  3 ­ Agree
     * "Response Text"                            test_approvalresponsetext01
     * 6.  User clicks on "Preview"
     * 7.  User clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Approval Scorecard is saved, submitted, and marked as "Complete". </p>
     */
    public void testScenario156() throws Exception {
        setUser(UserSimulator.APPROVER);
        this.user.openApprovalScorecardPage(PROJECT_NAME);
        this.user.submitApproval("2", "3", "test_approvalresponsetext01", true, true, true);
        assertData("data/expected/PerformApprovalCommitted.xml", "The Approval Scorecard is not committed correctly");
    }

    /**
     * <p>Scenario #157</p>
     * <pre>
     * Note: User is logged-in as "Approver"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Approval" tab
     * 4.  User clicks on "Submit Approval"
     * 5.  User enters sample data below for every "Approver Comments" section:
     * Field Name                                 Sample Data Value
     * Response 1: (drop-down list)               Recommended
     * Response (drop-down list)                  3 ­ Agree
     * "Response Text"                            test_approvalresponsetext01
     * 6.  User clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Approval Scorecard is saved, submitted, and marked as "Complete". </p>
     */
    public void testScenario157() throws Exception {
        setUser(UserSimulator.APPROVER);
        this.user.openApprovalScorecardPage(PROJECT_NAME);
        this.user.submitApproval("2", "3", "test_approvalresponsetext01", true, true, false);
        assertData("data/expected/PerformApprovalCommitted.xml", "The Approval Scorecard is not committed correctly");
    }

    /**
     * <p>Scenario #158</p>
     * <pre>
     * Note: User is logged-in as "Approver"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Approval" tab
     * 4.  User clicks on "Submit Approval"
     * 5.  User enters sample data below for every "Approver Comments" section:
     *
     * Field Name                                 Sample Data Value
     * Response 1: (drop-down list)               Recommended
     * Response (drop-down list)                  3 ­ Agree
     * "Response Text"                            test_approvalresponsetext01
     *
     * 6.  User enters sample text below in the "Approver Comments" text box in the
     * "Group" section
     * "test_approvalresponsetext01"
     * 7.  User clicks on "Save for Later"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Approval Scorecard is saved, and marked as "Pending". </p>
     */
    public void testScenario158() throws Exception {
        setUser(UserSimulator.APPROVER);
        this.user.openApprovalScorecardPage(PROJECT_NAME);
        this.user.submitApproval("2", "3", "test_approvalresponsetext01", false, true, false);
        assertData("data/expected/PerformApprovalPending.xml", "The Approval Scorecard is not committed correctly");
    }

    /**
     * <p>Scenario #159</p>
     * <pre>
     * Note: User is logged-in as "Approver"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Approval" tab
     * 4.  User clicks on "Submit Approval"
     * 5.  User enters sample data below for first "Approver Comments" section:
     * Field Name                                 Sample Data Value
     * Response 1: (drop-down list)               Recommended
     * Response (drop-down list)                  3 ­ Agree
     * "Response Text"                            test_approvalresponsetext01
     * 6.  User clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating "Approval Scorecard" is incomplete ­ required fields must be completed
     * before proceeding. </p>
     */
    public void testScenario159() throws Exception {
        setUser(UserSimulator.APPROVER);
        this.user.openApprovalScorecardPage(PROJECT_NAME);
        this.user.submitApproval("2", "3", "test_approvalresponsetext01", true, false, false);
        assertDisplayedMessage(Messages.getInvalidInputApprovalIncomplete());
    }

    /**
     * <p>Gets the data sets specific to test case which must be used to populate the database tables with initial data.
     * </p>
     *
     * @return an <code>IDataSet</code> array providing the data sets specific to test case.
     * @throws Exception if an unexpected error occurs.
     */
    protected IDataSet[] getDataSets() throws Exception {
        return new IDataSet[] {getRegistrationPhaseData(), getSubmissionPhaseData(), getScreeningPhaseData(),
            getReviewPhaseData(), getAppealsPhaseData(), getAppealsResponsePhaseData(), getAggregationPhaseData(),
            getAggregationReviewPhaseData(), getFinalFixPhaseData(), getFinalReviewPhaseData(), getApprovalPhaseData()};
    }
}
