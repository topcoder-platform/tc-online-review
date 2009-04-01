/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import junit.framework.Assert;
import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.Review;
import com.cronos.onlinereview.project.Scorecard;
import com.cronos.onlinereview.project.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import org.dbunit.dataset.IDataSet;

import java.util.Map;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Iterator;

/**
 * <p>A test case for <code>View Approval</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class ViewApprovalFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the sample project (as displayed by the list of projects) which is
     * used for testing purposes.</p>
     */
    public static final String PROJECT_NAME = "Ajax Timed Survey version 1.0";

    /**
     * <p>A <code>String</code> array providing the usernames for users who are granted the permission to view the
     * aggregation review results for selected project.</p>
     */
    public static final String[] AUTHORIZED_USERS
        = new String[] {UserSimulator.MANAGER, UserSimulator.PRIMARY_REVIEWER, UserSimulator.APPROVER,
                        UserSimulator.DESIGNER, UserSimulator.WINNING_SUBMITTER, UserSimulator.SECOND_PLACE_SUBMITTER,
                        UserSimulator.THIRD_PLACE_SUBMITTER, UserSimulator.REVIEWER1, UserSimulator.REVIEWER2,
                        UserSimulator.OBSERVER};

    /**
     * <p>Scenario #160</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Approval" tab
     * 4.  User clicks on "View Results"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Approval Scorecard is displayed. </p>
     */
    public void testScenario160() throws Exception {
        for (int i = 0; i < AUTHORIZED_USERS.length; i++) {
            setUser(AUTHORIZED_USERS[i]);
            this.user.openApprovalResultsPage(PROJECT_NAME);
            assertApprovalResultsPage(PROJECT_NAME);
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
            getAggregationReviewPhaseData(), getFinalFixPhaseData(), getFinalReviewPhaseData(), getApprovalPhaseData(),
            getCompletePhaseData()};
    }

    /**
     * <p>Verifies that the current page displayed to user displays the approval results for specified project
     * correctly.</p>
     *
     * @param projectName a <code>String</code> providing the name of the project.
     */
    private void assertApprovalResultsPage(String projectName) throws Exception {
        // Verify that the page header is "Approval Scorecard"
        Assert.assertEquals("Invalid page content header is displayed", "Approval Scorecard", this.user.getText("h3"));

        // Verify that the submission ID, submitter handle, user role are displayed correctly
        Assert.assertTrue("Submission ID is not displayed correctly",
                          this.user.getCurrentPage().asText().indexOf("Submission: 1") > 0);

        // Verify the project name
        HtmlTable headerTable = (HtmlTable) this.user.getContent().getHtmlElementsByTagName("table").get(0);
        Assert.assertEquals("Incorrect project name is displayed",
                            projectName, headerTable.getCellAt(0, 0).asText().trim());

        // Verify that the approval result is displayed correctly
        Review review = getApprovalReview();
        List groupTables = this.user.getContent().getHtmlElementsByAttribute("table", "class", "scorecard");
        Map[] scorecardGroups = Scorecard.DESIGN_APPROVAL.getGroups();
        Assert.assertEquals("Incorrect number of groups is displayed", scorecardGroups.length, groupTables.size());
        for (int i = 0; i < scorecardGroups.length; i++) {
            assertApprovalScorecardGroup(scorecardGroups[i], (HtmlTable) groupTables.get(i), review);
        }
    }

    /**
     * <p>Verifies that the details for specified scorecard group are displayed correctly.</p>
     *
     * @param scorecardGroup a <code>Map</code> providing the details for scorecard group.
     * @param htmlTable an <code>HtmlTable</code> providing the details for displayed scorecard group.
     * @param review a <code>Review</code> providing the details for the approval review.
     */
    private void assertApprovalScorecardGroup(Map scorecardGroup, HtmlTable htmlTable, Review review) {
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
