/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.Messages;
import com.cronos.onlinereview.project.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;

import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

/**
 * <p>A test case for <code>Edit My Appeal Response</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class EditMyAppealResponseduringAppealsResponseFunctionalTest extends AbstractTestCase {

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
     * <p>Constructs new <code>EditMyAppealResponseduringAppealsResponseFunctionalTest</code> instance for running the
     * test matching the specified name.</p>
     *
     * @param name a <code>String</code> providing the name of the test.
     */
    public EditMyAppealResponseduringAppealsResponseFunctionalTest(String name) {
        super(name);
    }

    /**
     * <p>Scenario #123</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  User selects submission and clicks on score
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating user does NOT have permission to view appeals. </p>
     */
    public void testScenario123() throws Exception {
        String[] unauthorizedUsers;
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            for (int j = 0; j < REVIEWERS.length; j++) {
                unauthorizedUsers = getUnauthorizedUsers(SUBMISSION_IDS[i], REVIEWERS[j]);
                for (int k = 0; k < unauthorizedUsers.length; k++) {
                    setUser(unauthorizedUsers[k]);
                    this.user.openReviewAppealsPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j]);
                    assertDisplayedMessage(Messages.getNoPermissionViewAppealResponses());
                }
            }
        }
    }

    /**
     * <p>Scenario #124</p>
     * <pre>
     * Note: User is logged-in as "Reviewer"
     * Project phase is "Appeals"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  User selects submission and clicks on score
     * 5.  User clicks on "Edit" to edit original Appeal Response
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating "Appeals Response Phase" has ended and no additional edits may be made.
     * </p>
     */
    public void testScenario124() throws Exception {
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            for (int j = 0; j < REVIEWERS.length; j++) {
                setUser(REVIEWERS[j]);
//                System.out.println("ISV : " + REVIEWERS[j] + ", j = " + j);
                this.user.openReviewAppealsPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j]);
                HtmlAnchor editButon = this.user.findLinkWithImage("bttn_edit.gif");
                if (editButon != null) {
                    this.user.click(editButon);
                    assertDisplayedMessage(Messages.getNotificationAppealsResponsePhaseEnded());
                }
            }
        }
    }

    /**
     * <p>Scenario #125</p>
     * <pre>
     * Note: User is logged-in as "Reviewer"
     * Project phase is "Appeals"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  User selects submission and clicks on score
     * 5.  User clicks on "Edit" to edit original Appeal Response
     * 6.  User enters edited "Appeal Response" using sample data below:
     *
     * Field Name                                 Sample Data Value
     * Response Text                              test_editappealresponsetext01
     * Modified Response (drop-down)              1 ­ Strongly Agree
     *
     * 7.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Edited Appeals Response is saved. </p>
     */
    public void testScenario125() throws Exception {
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            for (int j = 0; j < REVIEWERS.length; j++) {
                setUser(REVIEWERS[j]);
                this.user.openReviewAppealsPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j]);
                this.user.updateAppealResponses("test_editappealresponsetext01", "4");
            }
        }
        assertData("data/expected/AppealsResponsesUpdated.xml",
                   "The updated appeals responses are not saved as expected");
    }

    /**
     * <p>Gets the data sets specific to test case which must be used to populate the database tables with initial data.
     * </p>
     *
     * @return an <code>IDataSet</code> array providing the data sets specific to test case.
     * @throws Exception if an unexpected error occurs.
     */
    protected IDataSet[] getDataSets() throws Exception {
        if ("testScenario124".equals(getName())) {
            return new IDataSet[] {getRegistrationPhaseData(), getSubmissionPhaseData(), getScreeningPhaseData(),
                getReviewPhaseData(), getAppealsPhaseData(), getAppealsResponsePhaseData(), getAggregationPhaseData()};
        } else {
            InputStream projectDataStream
                = AbstractTestCase.class.getClassLoader().getResourceAsStream("data/expected/"
                                                                              + "AppealsResponsesCommitted.xml");
            IDataSet appealsResponses = new FlatXmlDataSet(projectDataStream);

            return new IDataSet[] {getRegistrationPhaseData(), getSubmissionPhaseData(), getScreeningPhaseData(),
                getReviewPhaseData(), getAppealsPhaseData(), getAppealsResponsePhaseData(), appealsResponses};
        }
    }

    /**
     * <p>Gets the list of users which are not granted the permission for viewing the appeals response made by the
     * specified reviewer for the specified submission.</p>
     *
     * @param submissionId a <code>String</code> providing the ID for the submission.
     * @param reviewerHandle a <code>String</code> providing the handle for the reviewer.
     * @return a <code>String</code> array providing the usernames for users which are not granted the permission for
     *         viewing the appeals response for specified submission.
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
