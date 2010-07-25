/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import org.dbunit.dataset.IDataSet;
import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.Messages;
import com.cronos.onlinereview.project.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TCSTester
 * @version 1.0
 */
public class PerformAppealsResponseFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the sample project (as displayed by the list of projects) which is
     * used for testing purposes.</p>
     */
    public static final String PROJECT_NAME = "Ajax Timed Survey version 1.0";

    /**
     * <p>A <code>String</code> array providing the IDs of submissions with screening results which are used for testing
     * purposes.</p>
     */
    public static final String[] SUBMISSION_IDS = new String[] {"2", "3"};

    /**
     * <p>A <code>String</code> array representing the reviewers for the project which is used for testing.</p>
     */
    public static final String[] REVIEWERS
        = new String[] {UserSimulator.PRIMARY_REVIEWER, UserSimulator.REVIEWER1, UserSimulator.REVIEWER2};

    /**
     * <p>Scenario #118</p>
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
    public void testScenario118() throws Exception {
        String[] unauthorizedUsers;
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            for (int j = 0; j < REVIEWERS.length; j++) {
                unauthorizedUsers = getUnauthorizedUsers(SUBMISSION_IDS[i], REVIEWERS[j]);
                for (int k = 0; k < unauthorizedUsers.length; k++) {
                    // Review/Appeals
                    setUser(unauthorizedUsers[k]);
                    this.user.openReviewAppealsPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j]);
                    assertDisplayedMessage(Messages.getNoPermissionViewReviewerReview());
                }
            }
        }
    }

    /**
     * <p>Scenario #119</p>
     * <pre>
     * Note: User is logged-in as "Reviewer"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  User selects Review Scorecard to Perform Appeals Response
     * 5.  User selects submitters Appeal
     * 6.  User enters "Appeal Response" using sample data below:
     *
     * Field Name                                 Sample Data Value
     * Response Text                              test_sampleresponsetext01
     * Modified Response (drop-down)              Select
     *
     * 7.  User clicks on "Submit Response"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating a "Modified Response" MUST be selected. </p>
     */
    public void testScenario119() throws Exception {
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            for (int j = 0; j < REVIEWERS.length; j++) {
                // Review/Appeals
                setUser(REVIEWERS[j]);
                this.user.openReviewAppealsPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j]);
                List responseRows = this.user.getContent().getHtmlElementsByAttribute("tr", "class", "highlighted");
                if (!responseRows.isEmpty()) {
                    HtmlTableRow row = (HtmlTableRow) responseRows.get(0);
                    HtmlTextArea textArea = (HtmlTextArea) row.getCell(0).getHtmlElementsByTagName("textarea").get(0);
                    HtmlAnchor submitButton = (HtmlAnchor) row.getCell(1).getHtmlElementsByTagName("a").get(0);
                    textArea.setText("test_sampleresponsetext01");
                    this.user.click(submitButton);
                    assertDisplayedMessage(Messages.getInvalidInputModifiedResponseNotSelected());
                }
            }
        }
    }

    /**
     * <p>Scenario #120</p>
     * <pre>
     * Note: User is logged-in as "Reviewer"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  User selects Review Scorecard to Perform Appeals Response
     * 5.  User selects submitters Appeal
     * 6.  User enters "Appeal Response" using sample data below:
     *
     * Field Name                                 Sample Data Value
     * Response Text                              test_sampleresponsetext01
     * Modified Response (drop-down)              2 ­ Somewhat Agree
     *
     * 7.  User clicks on "Submit Response"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Appeal response is successfully submitted. </p>
     */
    public void testScenario120() throws Exception {
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            for (int j = 0; j < REVIEWERS.length; j++) {
                // Review/Appeals
                setUser(REVIEWERS[j]);
                this.user.openReviewAppealsPage(PROJECT_NAME, SUBMISSION_IDS[i], REVIEWERS[j]);
                List responseRows = this.user.getContent().getHtmlElementsByAttribute("tr", "class", "highlighted");
                if (!responseRows.isEmpty()) {
                    for (int k = 0; k < responseRows.size(); k++) {
                        HtmlTableRow row = (HtmlTableRow) responseRows.get(k);
                        HtmlTextArea textArea = (HtmlTextArea) row.getCell(0).getHtmlElementsByTagName("textarea").get(0);
                        HtmlAnchor submitButton = (HtmlAnchor) row.getCell(1).getHtmlElementsByTagName("a").get(0);
                        HtmlSelect select = (HtmlSelect) row.getCell(1).getHtmlElementsByTagName("select").get(0);
                        textArea.setText("test_sampleresponsetext01");
                        select.setSelectedAttribute("2", true);
                        this.user.click(submitButton);
                    }
                }
            }
        }
        assertData("data/expected/AppealsResponsesCommitted.xml", "The Appeal Responses are not saved correctly");
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
            getReviewPhaseData(), getAppealsPhaseData(), getAppealsResponsePhaseData()};
    }

    /**
     * <p>Gets the list of users which are not granted the permission for viewing the review scorecard performed by the
     * specified reviewer for the specified submission.</p>
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
}
