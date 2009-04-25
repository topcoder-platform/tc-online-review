/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.Messages;
import com.cronos.onlinereview.project.AbstractTestCase;
import org.dbunit.dataset.IDataSet;

import junit.framework.Assert;

/**
 * <p>A test case for <code>Perform Final Review</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class PerformFinalReviewFunctionalTest extends AbstractTestCase {

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
        = new String[] {UserSimulator.MANAGER, UserSimulator.DESIGNER, UserSimulator.WINNING_SUBMITTER,
                        UserSimulator.SECOND_PLACE_SUBMITTER, UserSimulator.THIRD_PLACE_SUBMITTER,
                        UserSimulator.APPROVER, UserSimulator.REVIEWER1, UserSimulator.REVIEWER2,
                        UserSimulator.OBSERVER};

    /**
     * <p>Scenario #144</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Final Review/Fix" tab
     * 4.  User clicks on "Submit Review"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating User does NOT have permission to Perform Final Review </p>
     */
    public void testScenario144() throws Exception {
        for (int j = 0; j < UNAUTHORIZED_USERS.length; j++) {
            setUser(UNAUTHORIZED_USERS[j]);
            this.user.openFinalReviewScorecardPage(PROJECT_NAME);
            assertDisplayedMessage(Messages.getNoPermissionPerformFinalReview());
        }
    }

    /**
     * <p>Scenario #145</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Final Review/Fix" tab
     * 4.  User clicks on "Submit Review"
     * 5.  User clicks on "Fixed" option button for every reviewer response
     * 6.  User checks the "Approve Final Fixes" box
     * 7.  User clicks on "Preview"
     * 8.  User Previews "Final Review Scorecard" and clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Final Review Scorecard is saved, submitted, and marked as "Complete" </p>
     */
    public void testScenario145() throws Exception {
        setUser(UserSimulator.PRIMARY_REVIEWER);
        this.user.openFinalReviewScorecardPage(PROJECT_NAME);
        this.user.submitFinalReview("Fixed", true, true, true, true);
        assertData("data/expected/PerformFinalReviewCommitted.xml",
                   "The Final Review Scorecard is not committed correctly");
    }

    /**
     * <p>Scenario #146</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Final Review/Fix" tab
     * 4.  User clicks on "Submit Review"
     * 5.  User clicks on "Fixed" option button for every reviewer response
     * 6.  User checks the "Approve Final Fixes" box
     * 7.  User clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Final Review Scorecard is saved, submitted, and marked as "Complete". </p>
     */
    public void testScenario146() throws Exception {
        setUser(UserSimulator.PRIMARY_REVIEWER);
        this.user.openFinalReviewScorecardPage(PROJECT_NAME);
        this.user.submitFinalReview("Fixed", true, true, false, true);
        assertData("data/expected/PerformFinalReviewCommitted.xml",
                   "The Final Review Scorecard is not committed correctly");
    }

    /**
     * <p>Scenario #147</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Final Review/Fix" tab
     * 4.  User clicks on "Submit Review"
     * 5.  User clicks on "Fixed" option button for every reviewer response
     * 6.  User checks the "Approve Final Fixes" box
     * 7.  User clicks on "Save and Finish Later"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Final Review Scorecard is saved and marked as "Pending". </p>
     */
    public void testScenario147() throws Exception {
        setUser(UserSimulator.PRIMARY_REVIEWER);
        this.user.openFinalReviewScorecardPage(PROJECT_NAME);
        this.user.submitFinalReview("Fixed", false, true, false, true);
        assertData("data/expected/PerformFinalReviewPending.xml", "The Final Review Scorecard is not saved correctly");
    }

    /**
     * <p>Scenario #148</p>
     * <pre>
     * 1.  User clicks on "Submit Review"
     * 2.  User clicks on "Fixed" option button for every reviewer response
     * 3.  User clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Message is shown requesting confirmation from user that Final Review Scorecard will be submitted
     * without Approval of Final Fixes.  User confirms and Final Review Scorecard is saved, submitted, and marked as
     * "Complete". </p>
     */
    public void testScenario148() throws Exception {
        setUser(UserSimulator.PRIMARY_REVIEWER);
        this.user.setConfirmStatus(true);
        this.user.openFinalReviewScorecardPage(PROJECT_NAME);
        this.user.submitFinalReview("Fixed", true, true, false, false);
        assertData("data/expected/PerformFinalReviewCommitted.xml",
                   "The Final Review Scorecard is not committed correctly");
        Assert.assertEquals("The confirmation message is not displayed properly",
                            Messages.getFinalReviewNoApprovalConfirmation(), this.user.popConfirm());
    }

    /**
     * <p>Scenario #149</p>
     * <pre>
     * 1.  User clicks on "Submit Review"
     * 2.  User clicks on "Not Fixed" for first options button and inserts following test into
     * "Response Text" box"
     * "test_notfixed_reponsetext01"
     * 3.  User clicks on "Fixed" option button for every remaining reviewer response
     * 4.  User clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Message is shown indicating Final Review has Failed and a new Final Fix and Review Phase will be
     * created.  User is directed automatically to Newly created Final Fix Phase. </p>
     */
    public void testScenario149() throws Exception {
        setUser(UserSimulator.PRIMARY_REVIEWER);
        this.user.setConfirmStatus(true);
        this.user.openFinalReviewScorecardPage(PROJECT_NAME);
        this.user.submitFinalReview("Not Fixed", true, false, false, false);
        assertDisplayedMessage(Messages.getNotificationFinalReviewFailed());
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
            getAggregationReviewPhaseData(), getFinalFixPhaseData(), getFinalReviewPhaseData()};
    }
}
