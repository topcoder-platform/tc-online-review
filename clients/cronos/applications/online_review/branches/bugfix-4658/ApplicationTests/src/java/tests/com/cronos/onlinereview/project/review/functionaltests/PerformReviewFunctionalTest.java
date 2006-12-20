/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.Messages;
import com.cronos.onlinereview.project.AbstractTestCase;
import org.dbunit.dataset.IDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>A test case for <code>Preform Review</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class PerformReviewFunctionalTest extends AbstractTestCase {

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
    public static final String[] REVIEWERS
        = new String[] {UserSimulator.PRIMARY_REVIEWER, UserSimulator.REVIEWER1, UserSimulator.REVIEWER2};

    /**
     * <p>Scenario #98</p>
     * <pre>
     * 1.  User logs in
     * 2.  User clicks on "All Open Projects" tab
     * 3.  User views project list and selects a project
     * 4.  User clicks on "Review & Appeals" tab
     * 5.  User clicks on "Submit Review" for selected submission
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating User does NOT have permission to Submit a Review. </p>
     */
    public void testScenario98() throws Exception {
        String[] unauthorizedUsers;
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            for (int j = 0; j < REVIEWERS.length; j++) {
                unauthorizedUsers = getUnauthorizedUsers(SUBMISSION_IDS[i], REVIEWERS[j]);
                for (int k = 0; k < unauthorizedUsers.length; k++) {
                    setUser(unauthorizedUsers[k]);
                    this.user.openReviewAppealsPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j]);
                    assertDisplayedMessage(Messages.getNoPermissionViewReviewerReview());
                }
            }
        }
    }

    /**
     * <p>Scenario #99</p>
     * <pre>
     * Note: User is logged-in as a "Reviewer"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review & Appeals" tab
     * 4.  User clicks on "Submit Review" for selected submission
     * 5.  User completes all drop-downs and text boxes contained in "Review Scorecard"
     * ­ all drop-downs and test boxes are required to be completed
     * 6.  User clicks on "Preview"
     * 7.  User completes preview of scorecard and clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Scorecard Score is computed; Scorecard is saved, submitted, and marked as "Complete" in the submission list.
     * </p>
     */
    public void testScenario99() throws Exception {
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            for (int j = 0; j < REVIEWERS.length; j++) {
                setReviewerUser(REVIEWERS[j]);
                this.user.openReviewAppealsPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j]);
                this.user.submitReview("test_reviewresponsetext01", "3", "2", true, true);
            }
        }
        assertData("data/expected/ReviewsCommitted.xml",
                   "The reviews are not saved as expected");
    }

    /**
     * <p>Scenario #100</p>
     * <pre>
     * Note: User is logged-in as a "Reviewer"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review & Appeals" tab
     * 4.  User clicks on "Submit Review" for selected submission
     * 5.  User completes all drop-downs and text boxes contained in "Review Scorecard"
     * ­ all drop-downs and test boxes are required to be completed
     * 6.  User completes clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Scorecard Score is computed; Scorecard is saved, submitted, and marked as "Complete" in the submission list.
     * </p>
     */
    public void testScenario100() throws Exception {
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            for (int j = 0; j < REVIEWERS.length; j++) {
                setReviewerUser(REVIEWERS[j]);
                this.user.openReviewAppealsPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j]);
                this.user.submitReview("test_reviewresponsetext01", "3", "2", true, true);
            }
        }
        assertData("data/expected/ReviewsCommitted.xml",
                   "The reviews are not saved as expected");
    }

    /**
     * <p>Scenario #101</p>
     * <pre>
     * Note: User is logged-in as a "Reviewer"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review & Appeals" tab
     * 4.  User clicks on "Submit Review" for selected submission
     * 5.  User completes 5 of required drop-down lists and 3 of required text boxes
     * 6.  User clicks on "Save for Later"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Scorecard is saved and marked as "Pending" in the submission list. </p>
     */
    public void testScenario101() throws Exception {
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            for (int j = 0; j < REVIEWERS.length; j++) {
                setReviewerUser(REVIEWERS[j]);
                this.user.openReviewAppealsPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j]);
                this.user.submitReview("test_reviewresponsetext01", "3", "2", false, true);
            }
        }
        assertData("data/expected/ReviewsPending.xml",
                   "The reviews are not saved as expected");
    }

    /**
     * <p>Scenario #102</p>
     * <pre>
     * Note: User is logged-in as a "Reviewer"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review & Appeals" tab
     * 4.  User clicks on "Submit Review" for selected submission
     * 5.  User completes 5 of required drop-down lists and 3 of required text boxes
     * 6.  User clicks on "Preview"
     * 7.  User completes preview of scorecard and clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating all required fields within Scorecard MUST be completed. </p>
     */
    public void testScenario102() throws Exception {
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            for (int j = 0; j < REVIEWERS.length; j++) {
                setReviewerUser(REVIEWERS[j]);
                this.user.openReviewAppealsPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j]);
                this.user.submitReview("test_reviewresponsetext01", "3", "2", true, false);
                assertDisplayedMessage(Messages.getInvalidInputReviewIncomplete());
            }
        }
    }

    /**
     * <p>Scenario #103</p>
     * <pre>
     * Note: User is logged-in as a "Reviewer"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review & Appeals" tab
     * 4.  User clicks on "Submit Review" for selected submission
     * 5.  User completes 5 of required drop-down lists and 3 of required text boxes
     * 6.  User clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating all required fields within Scorecard MUST be completed. </p>
     */
    public void testScenario103() throws Exception {
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            for (int j = 0; j < REVIEWERS.length; j++) {
                setReviewerUser(REVIEWERS[j]);
                this.user.openReviewAppealsPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j]);
                this.user.submitReview("test_reviewresponsetext01", "3", "2", true, false);
                assertDisplayedMessage(Messages.getInvalidInputReviewIncomplete());
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
        return new IDataSet[]{getRegistrationPhaseData(), getSubmissionPhaseData(), getScreeningPhaseData(),
            getReviewPhaseData()};
    }

    /**
     * <p>Gets the list of users which are not granted the permission for viewing the review made by the
     * specified reviewer for the specified submission.</p>
     *
     * @param submissionId a <code>String</code> providing the ID for the submission.
     * @param reviewerHandle a <code>String</code> providing the handle for the reviewer.
     * @return a <code>String</code> array providing the usernames for users which are not granted the permission for
     *         viewing the review for specified submission.
     */
    private String[] getUnauthorizedUsers(String submissionId, String reviewerHandle) {
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
}
