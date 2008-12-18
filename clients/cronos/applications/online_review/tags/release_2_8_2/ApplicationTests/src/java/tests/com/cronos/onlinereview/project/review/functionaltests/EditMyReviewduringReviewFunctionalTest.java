/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.Messages;
import com.cronos.onlinereview.project.AbstractTestCase;

import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

/**
 * <p>A test case for <code>Edit My Review</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class EditMyReviewduringReviewFunctionalTest extends AbstractTestCase {

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
     * <p>Scenario #112</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User selects scorecard to edit
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating user does NOT have permission to edit selected Scorecard Review. </p>
     */
    public void testScenario112() throws Exception {
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
     * <p>Scenario #113</p>
     * <pre>
     * Note: User is logged-in as "Reviewer"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User selects scorecard to edit
     * 4.  User edits review scorecard
     * 5.  User saves edited scorecard and re-submits scorecard
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Edited scorecard is validated according to procedures described in test scenario 2.26.1. Perform Review Activity
     * (Functional Test Case Numbers: <193 through 199>). </p>
     */
    public void testScenario113() throws Exception {
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            for (int j = 0; j < REVIEWERS.length; j++) {
                setUser(REVIEWERS[j]);
                this.user.openReviewAppealsPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j]);
                this.user.updateReview("test_editreviewresponsetext01", "4", "1");
            }
        }
        assertData("data/expected/ReviewsUpdated.xml",
                   "The updated reviews are not saved as expected");
    }

    /**
     * <p>Gets the data sets specific to test case which must be used to populate the database tables with initial data.
     * </p>
     *
     * @return an <code>IDataSet</code> array providing the data sets specific to test case.
     * @throws Exception if an unexpected error occurs.
     */
    protected IDataSet[] getDataSets() throws Exception {
        InputStream projectDataStream
            = AbstractTestCase.class.getClassLoader().getResourceAsStream("data/expected/ReviewsCommitted.xml");
        IDataSet reviews = new FlatXmlDataSet(projectDataStream);
        return new IDataSet[]{getRegistrationPhaseData(), getSubmissionPhaseData(), getScreeningPhaseData(),
            getReviewPhaseData(), reviews};
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
