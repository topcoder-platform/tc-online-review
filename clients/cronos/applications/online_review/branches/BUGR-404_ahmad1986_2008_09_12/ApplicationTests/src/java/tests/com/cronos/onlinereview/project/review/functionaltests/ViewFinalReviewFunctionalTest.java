/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import junit.framework.Assert;
import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.Messages;
import com.cronos.onlinereview.project.Review;
import com.cronos.onlinereview.project.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import org.dbunit.dataset.IDataSet;

import java.util.Map;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * <p>A test case for <code>View Final Review</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class ViewFinalReviewFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the sample project (as displayed by the list of projects) which is
     * used for testing purposes.</p>
     */
    public static final String PROJECT_NAME = "Ajax Timed Survey version 1.0";

    /**
     * <p>A <code>UserSimulator</code> representing a final reviewer.</p>
     */
    private static final UserSimulator FINAL_REVIEWER = new UserSimulator(UserSimulator.PRIMARY_REVIEWER);

    /**
     * <p>A <code>String</code> array providing the usernames for users who are granted the permission to view the
     * final review results for selected project.</p>
     */
    public static final String[] AUTHORIZED_USERS
        = new String[] {UserSimulator.MANAGER, UserSimulator.PRIMARY_REVIEWER, UserSimulator.APPROVER,
                        UserSimulator.DESIGNER, UserSimulator.WINNING_SUBMITTER, UserSimulator.SECOND_PLACE_SUBMITTER,
                        UserSimulator.THIRD_PLACE_SUBMITTER, UserSimulator.REVIEWER1, UserSimulator.REVIEWER2,
                        UserSimulator.OBSERVER};

    /**
     * <p>A <code>String</code> array providing the usernames for users who are not granted the permission to view the
     * final review results for selected project.</p>
     */
    public static final String[] UNAUTHORIZED_USERS = new String[] {};

    /**
     * <p>Scenario #150</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Final Review/Fix" tab User clicks on "Final Review"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating user does NOT have permission to view "Final Review". </p>
     */
    public void testScenario150() throws Exception {
        for (int j = 0; j < UNAUTHORIZED_USERS.length; j++) {
            setUser(UNAUTHORIZED_USERS[j]);
            this.user.openFinalReviewResultsPage(PROJECT_NAME);
            assertDisplayedMessage(Messages.getNoPermissionViewFinalReview());
        }
    }

    /**
     * <p>Scenario #151</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Final Review/Fix" tab User clicks on "Final Review"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Final Review is displayed. </p>
     */
    public void testScenario151() throws Exception {
        for (int i = 0; i < AUTHORIZED_USERS.length; i++) {
            setUser(AUTHORIZED_USERS[i]);
            this.user.openFinalReviewResultsPage(PROJECT_NAME);
            assertFinalReviewResultsPage();
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
     * <p>Verifies that the current page displayed to user displays the final review results for specified project
     * correctly.</p>
     */
    private void assertFinalReviewResultsPage() throws Exception {
        // Verify that the page header is "Final Review Scorecard"
        Assert.assertEquals("Invalid page content header is displayed", "Final Review Scorecard",
                            this.user.getText("h3"));

        // Verify that the final reviewer handle, submission ID, submitter handle, user role are displayed correctly
        Assert.assertTrue("Submission ID is not displayed correctly",
                          this.user.getCurrentPage().asText().indexOf("Submission: 1") > 0);
        Assert.assertTrue("Aggregator handle is not displayed correctly",
                          this.user.getCurrentPage().asText().
                              indexOf("Aggregator: " + FINAL_REVIEWER.getUsername()) > 0);

        // Verify that the final review result is displayed correctly
        // TODO : Revise this
/*
        Review finalReview = getFinalReview();
        List groupTables = this.user.getContent().getHtmlElementsByAttribute("table", "class", "scorecard");
        Map[] scorecardGroups = Scorecard.DESIGN_REVIEW.getGroups();
        Assert.assertEquals("Incorrect number of groups is displayed", scorecardGroups.length, groupTables.size());
        for (int i = 0; i < scorecardGroups.length; i++) {
            assertFinalReviewScorecardGroup(scorecardGroups[i], (HtmlTable) groupTables.get(i), finalReview);
        }
*/
    }

    /**
     * <p>Verifies that the details for specified scorecard group are displayed correctly.</p>
     *
     * @param scorecardGroup a <code>Map</code> providing the details for scorecard group.
     * @param htmlTable an <code>HtmlTable</code> providing the details for displayed scorecard group.
     * @param review a <code>Review</code> providing the details for the screening review.
     */
    private void assertFinalReviewScorecardGroup(Map scorecardGroup, HtmlTable htmlTable, Review review) {
        Assert.assertEquals("Invalid scorecard group name is displayed",
                            scorecardGroup.get("name"), htmlTable.getCellAt(0, 0).asText().trim());
        LinkedHashMap groupSections = (LinkedHashMap) scorecardGroup.get("sections");
        Iterator groupSectionsIterator = groupSections.entrySet().iterator();
        Map scorecardSection = null;
        LinkedHashMap scorecardSectionQuestions = null;
        Iterator sectionQuestionsIterator = null;
        for (int i = 1; i < htmlTable.getRowCount() - 1; i++) {
            if (htmlTable.getCellAt(i, 0).getAttributeValue("class").equals("subheader")) {
                if (sectionQuestionsIterator != null) {
                    Assert.assertFalse("Not all section questions are displayed", sectionQuestionsIterator.hasNext());
                }
                scorecardSection = (Map) groupSectionsIterator.next();
                scorecardSectionQuestions = (LinkedHashMap) scorecardSection.get("questions");
                sectionQuestionsIterator = scorecardSectionQuestions.entrySet().iterator();
                Assert.assertEquals("Invalid section name is displayed",
                                    scorecardSection.get("name"), htmlTable.getCellAt(i, 0).asText().trim());
            } else {
                // Verify section question
                Map question = (Map) sectionQuestionsIterator.next();
                Assert.assertTrue("Incorrect scorecard question is displayed",
                                  htmlTable.getCellAt(i, 0).asText().trim().
                                      endsWith((String) question.get("description")));
                Assert.assertEquals("Incorrect scorecard question weight is displayed",
                                    question.get("weight"), htmlTable.getCellAt(i, 1).asText().trim());
                Assert.assertEquals("Incorrect question score is displayed",
                                    review.getQuestionScore((String) question.get("scorecard_question_id")),
                                    htmlTable.getCellAt(i, 2).asText().trim());
            }
        }
        if (sectionQuestionsIterator != null) {
            Assert.assertFalse("Not all section questions are displayed", sectionQuestionsIterator.hasNext());
        }
        Assert.assertFalse("Not all group sections are displayed", groupSectionsIterator.hasNext());
    }
}
