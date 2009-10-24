/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.Messages;
import com.cronos.onlinereview.project.AbstractTestCase;

import java.util.List;
import java.util.ArrayList;

import org.dbunit.dataset.IDataSet;

/**
 * <p>A test case for <code>View Appeal Response</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class ViewAppealsResponseFunctionalTest extends AbstractTestCase {

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
        = new String[] {UserSimulator.PRIMARY_REVIEWER, UserSimulator.REVIEWER1, UserSimulator.REVIEWER2};

    /**
     * <p>Scenario #121</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  User selects "Review Scorecard"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating user does NOT have permission to view appeals. </p>
     */
    public void testScenario121() throws Exception {
        String[] unauthorizedUsers;
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            unauthorizedUsers = getUnauthorizedUsers(SUBMISSION_IDS[i]);
            for (int j = 0; j < unauthorizedUsers.length; j++) {
                setUser(unauthorizedUsers[j]);
                for (int k = 0; k < REVIEWERS.length; k++) {
                    this.user.openReviewAppealsPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[k]);
                    assertDisplayedMessage(Messages.getNoPermissionViewAppealResponses());
                }
            }
        }
    }

    /**
     * <p>Scenario #122</p>
     * <pre>
     * Note: User has permission to view Review Scorecard Appeal Reponses
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  User selects Review Scorecard
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Scorecard Review with Appeals Responses are displayed. </p>
     */
    public void testScenario122() throws Exception {
        String[] authorizedUsers;
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            authorizedUsers = getAuthorizedUsers(SUBMISSION_IDS[i]);
            for (int j = 0; j < authorizedUsers.length; j++) {
                setUser(authorizedUsers[j]);
                for (int k = 0; k < REVIEWERS.length; k++) {
                    this.user.openReviewAppealsPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[k]);
                    assertReviewScorecardPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[k]);
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
     * @param submissionId a <code>String</code> providing the ID for the submission.
     * @return a <code>String</code> array providing the usernames for users which are not granted the permission for
     *         viewing the screening results for specified submission.
     */
    private String[] getUnauthorizedUsers(String submissionId) {
        List result = new ArrayList();
        result.add(UserSimulator.DESIGNER);
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
    private String[] getAuthorizedUsers(String submissionId) {
        List result = new ArrayList();
        result.add(UserSimulator.MANAGER);
        result.add(UserSimulator.APPROVER);
        result.add(UserSimulator.PRIMARY_REVIEWER);
        result.add(UserSimulator.OBSERVER);
        if (submissionId.equals("1")) {
            result.add(UserSimulator.WINNING_SUBMITTER);
        } else if (submissionId.equals("2")) {
            result.add(UserSimulator.SECOND_PLACE_SUBMITTER);
        } else if (submissionId.equals("3")) {
            result.add(UserSimulator.THIRD_PLACE_SUBMITTER);
        }
        return (String[]) result.toArray(new String[result.size()]);
    }
}
