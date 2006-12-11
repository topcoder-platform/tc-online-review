/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.Messages;
import com.cronos.onlinereview.project.AbstractTestCase;
import org.dbunit.dataset.IDataSet;

import java.util.List;
import java.util.ArrayList;

/**
 * <p>A test case for <code>View Reviews</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class ViewReviewsFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test.</p>
     */
    public static final String PROJECT_TEST_DATA_FILE_NAME = "data/split/ApprovalPhase.xml";

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
     * <p>A <code>UserSimulator</code> array representing the reviewers for the project which is used for testing.</p>
     */
    public static final UserSimulator[] REVIEWERS
        = new UserSimulator[] {new UserSimulator(UserSimulator.PRIMARY_REVIEWER),
                               new UserSimulator(UserSimulator.REVIEWER1),
                               new UserSimulator(UserSimulator.REVIEWER2)};

    /**
     * <p>Scenario #108</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User selects "Scorecard Review based on one of the follow paths:
     * a.  Click on "Review/Appeals" tab > click on one of the "Scores" for the
     * selected "Submission"
     * b.  Click on "Aggregation/Review" tab > click on "View Results"
     * c.  Click on "Final Fix/Review" tab > click on "View Results"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating user does NOT have permission to view Scorecard Reviews </p>
     */
    public void testScenario108() throws Exception {
        String[] unauthorizedUsers;
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            for (int j = 0; j < REVIEWERS.length; j++) {
                unauthorizedUsers = getUnauthorizedUsers(SUBMISSION_IDS[i], REVIEWERS[j].getUsername());
                for (int k = 0; k < unauthorizedUsers.length; k++) {
                    setUser(unauthorizedUsers[k]);
                    // Review/Appeals
                    this.user.openReviewAppealsPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j].getUsername());
                    assertDisplayedMessage(Messages.getNoPermissionViewReviewerReview());
                    // Aggregation/Review
                    this.user.openAggregationReviewResultsPage(PROJECT_NAME);
                    this.user.openReviewScorecard(REVIEWERS[j].getUsername());
                    assertDisplayedMessage(Messages.getNoPermissionViewReviewerReview());
                    // Final Fix/Review
                    this.user.openFinalReviewResultsPage(PROJECT_NAME);
                    this.user.openReviewScorecard(REVIEWERS[j].getUsername());
                    assertDisplayedMessage(Messages.getNoPermissionViewReviewerReview());
                }
            }
        }
    }

    /**
     * <p>Scenario #109</p>
     * <pre>
     * Note: User is logged-in as one of the following: Manager, Observer, Submitter, Screener,
     * Aggregator, Final Reviewer, Approver, Public or Designer.
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User selects "Scorecard Review based on one of the follow paths:
     * a.  Click on "Review/Appeals" tab > click on one of the "Scores" for the
     * selected "Submission"
     * b.  Click on "Aggregation/Review" tab > click on "View Results"
     * c.  Click on "Final Fix/Review" tab > click on "View Results"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Scorecard Review is displayed based on user selection. </p>
     */
    public void testScenario109() throws Exception {
        String[] authorizedUsers;
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            for (int j = 0; j < REVIEWERS.length; j++) {
                authorizedUsers = getAuthorizedUsers(SUBMISSION_IDS[i], REVIEWERS[j].getUsername());
                for (int k = 0; k < authorizedUsers.length; k++) {
                    setUser(authorizedUsers[k]);
                    // Review/Appeals
                    this.user.openReviewAppealsPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j].getUsername());
                    assertReviewScorecardPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j].getUsername());
                    // Aggregation/Review
                    this.user.openAggregationReviewResultsPage(PROJECT_NAME);
                    this.user.openReviewScorecard(REVIEWERS[j].getUsername());
                    assertReviewScorecardPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j].getUsername());
                    // Final Fix/Review
                    this.user.openFinalReviewResultsPage(PROJECT_NAME);
                    this.user.openReviewScorecard(REVIEWERS[j].getUsername());
                    assertReviewScorecardPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j].getUsername());
                }
            }
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
        return new IDataSet[] {getRegistrationPhaseData(), getSubmissionPhaseData(), getScreeningPhaseData(),
            getReviewPhaseData(), getAppealsPhaseData(), getAppealsResponsePhaseData(), getAggregationPhaseData(),
            getAggregationReviewPhaseData(), getFinalFixPhaseData(), getFinalReviewPhaseData(), getApprovalPhaseData()};
    }

    /**
     * <p>Gets the list of users which are not granted the permission for viewing the screening results for the
     * specified submission.</p>
     *
     * @param submissionId a <code>String</code> providing the ID for the target submission.
     * @param reviewerHandle a <code>String</code> providing the handle for the reviewer who has produced the review.
     * @return a <code>String</code> array providing the usernames for users which are not granted the permission for
     *         viewing the screening results for specified submission.
     */
    private String[] getUnauthorizedUsers(String submissionId, String reviewerHandle) {
        List result = new ArrayList();
        if (reviewerHandle.equals(UserSimulator.PRIMARY_REVIEWER)) {
            result.add(UserSimulator.REVIEWER1);
            result.add(UserSimulator.REVIEWER2);
        } else if (reviewerHandle.equals(UserSimulator.REVIEWER1)) {
            result.add(UserSimulator.PRIMARY_REVIEWER);
            result.add(UserSimulator.REVIEWER2);
        } else if (reviewerHandle.equals(UserSimulator.REVIEWER2)) {
            result.add(UserSimulator.PRIMARY_REVIEWER);
            result.add(UserSimulator.REVIEWER1);
        }
        if (submissionId.equals("1")) {
            result.add(UserSimulator.SECOND_PLACE_SUBMITTER);
            result.add(UserSimulator.THIRD_PLACE_SUBMITTER);
        } else if (submissionId.equals("2")) {
            result.add(UserSimulator.WINNING_SUBMITTER);
            result.add(UserSimulator.THIRD_PLACE_SUBMITTER);
        } else if (submissionId.equals("3")) {
            result.add(UserSimulator.WINNING_SUBMITTER);
            result.add(UserSimulator.SECOND_PLACE_SUBMITTER);
        }
        return (String[]) result.toArray(new String[result.size()]);
    }

    /**
     * <p>Gets the list of users which are granted the permission for viewing the screening results for the specified
     * submission.</p>
     *
     * @param submissionId a <code>String</code> providing the ID for the submission.
     * @return a <code>String</code> array providing the usernames for users which are granted the permission for
     *         viewing the screening results for specified submission.
     */
    private String[] getAuthorizedUsers(String submissionId, String reviewerHandle) {
        List result = new ArrayList();
        result.add(UserSimulator.MANAGER);
        result.add(UserSimulator.APPROVER);
        result.add(UserSimulator.OBSERVER);
        if (submissionId.equals("1")) {
            result.add(UserSimulator.WINNING_SUBMITTER);
        } else if (submissionId.equals("2")) {
            result.add(UserSimulator.SECOND_PLACE_SUBMITTER);
        } else if (submissionId.equals("3")) {
            result.add(UserSimulator.THIRD_PLACE_SUBMITTER);
        }
        if (reviewerHandle.equals(UserSimulator.PRIMARY_REVIEWER)) {
            result.add(UserSimulator.PRIMARY_REVIEWER);
        } else if (reviewerHandle.equals(UserSimulator.REVIEWER1)) {
            result.add(UserSimulator.REVIEWER1);
        } else if (reviewerHandle.equals(UserSimulator.REVIEWER2)) {
            result.add(UserSimulator.REVIEWER2);
        }
        return (String[]) result.toArray(new String[result.size()]);
    }
}
