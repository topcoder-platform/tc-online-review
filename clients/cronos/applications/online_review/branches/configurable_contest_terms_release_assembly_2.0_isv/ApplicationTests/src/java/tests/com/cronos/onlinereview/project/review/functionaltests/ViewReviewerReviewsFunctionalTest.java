/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import org.dbunit.dataset.IDataSet;

import java.util.ArrayList;
import java.util.List;

import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.Messages;
import com.cronos.onlinereview.project.AbstractTestCase;

/**
 * <p>A test case for <code>View Reviewer Reviews</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class ViewReviewerReviewsFunctionalTest extends AbstractTestCase {

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
     * <p>A <code>String</code> array representing the reviewers for the project which is used for testing.</p>
     */
    public static final String[] REVIEWERS
        = new String[] {new String(UserSimulator.PRIMARY_REVIEWER),
                               new String(UserSimulator.REVIEWER1),
                               new String(UserSimulator.REVIEWER2)};

    /**
     * <p>Scenario #110</p>
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
    public void testScenario110() throws Exception {
        String[] unauthorizedReviewers;
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            for (int j = 0; j < REVIEWERS.length; j++) {
                unauthorizedReviewers = getUnauthorizedReviewers(REVIEWERS[j]);
                for (int k = 0; k < unauthorizedReviewers.length; k++) {
                    setUser(unauthorizedReviewers[k]);
                    // Review/Appeals
                    this.user.openReviewAppealsPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j]);
                    assertDisplayedMessage(Messages.getNoPermissionViewReviewerReview());
                    // Aggregation/Review
                    this.user.openAggregationReviewResultsPage(PROJECT_NAME);
                    this.user.openReviewScorecard(REVIEWERS[j]);
                    assertDisplayedMessage(Messages.getNoPermissionViewReviewerReview());
                    // Final Fix/Review
                    this.user.openFinalReviewResultsPage(PROJECT_NAME);
                    this.user.openReviewScorecard(REVIEWERS[j]);
                    assertDisplayedMessage(Messages.getNoPermissionViewReviewerReview());
                }
            }
        }
    }

    /**
     * <p>Scenario #111</p>
     * <pre>
     * Note: User is logged-in as a "Reviewer"
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
    public void testScenario111() throws Exception {
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            for (int j = 0; j < REVIEWERS.length; j++) {
                setUser(REVIEWERS[j]);
                // Review/Appeals
                this.user.openReviewAppealsPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j]);
                assertReviewScorecardPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j]);
                // Aggregation/Review
                this.user.openAggregationReviewResultsPage(PROJECT_NAME);
                this.user.openReviewScorecard(REVIEWERS[j]);
                assertReviewScorecardPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j]);
                // Final Fix/Review
                this.user.openFinalReviewResultsPage(PROJECT_NAME);
                this.user.openReviewScorecard(REVIEWERS[j]);
                assertReviewScorecardPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j]);
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
     * @param reviewerHandle a <code>String</code> providing the ID for the submission.
     * @return a <code>String</code> array providing the usernames for users which are not granted the permission for
     *         viewing the screening results for specified submission.
     */
    private String[] getUnauthorizedReviewers(String reviewerHandle) {
        List result = new ArrayList();
        result.add(UserSimulator.DESIGNER);
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
        return (String[]) result.toArray(new String[result.size()]);
    }
}
