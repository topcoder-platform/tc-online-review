/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.Messages;
import com.cronos.onlinereview.project.Scorecard;
import com.cronos.onlinereview.project.Review;
import com.cronos.onlinereview.project.AbstractTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import org.dbunit.dataset.IDataSet;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Iterator;

import junit.framework.Assert;

/**
 * <p>A test case for <code>View Composite Review</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class ViewCompositeReviewFunctionalTest extends AbstractTestCase {

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
     * <p>Scenario #204</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  User clicks on # Score below "Review Score" for selected submission
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating user does not have permission to view selected scorecard. </p>
     */
    public void testScenario204() throws Exception {
        String[] unauthorizedUsers;
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            unauthorizedUsers = getUnauthorizedUsers(SUBMISSION_IDS[i]);
            for (int j = 0; j < unauthorizedUsers.length; j++) {
                setUser(unauthorizedUsers[j]);
                this.user.openCompositeReviewResultsPage(PROJECT_NAME, SUBMISSION_IDS[i]);
                assertDisplayedMessage(Messages.getNoPermissionViewCompositeReview());
            }
        }
    }

    /**
     * <p>Scenario #205</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  User clicks on # Score below "Review Score" for selected submission
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * User is able to view a Composite Review or Combined Review Scorecard </p>
     */
    public void testScenario205() throws Exception {
        String[] authorizedUsers;
        for (int i = 0; i < SUBMISSION_IDS.length; i++) {
            authorizedUsers = getAuthorizedUsers(SUBMISSION_IDS[i]);
            for (int j = 0; j < authorizedUsers.length; j++) {
                setUser(authorizedUsers[j]);
                this.user.openCompositeReviewResultsPage(PROJECT_NAME, SUBMISSION_IDS[i]);
                assertCompositeReviewResultsPage(PROJECT_NAME, SUBMISSION_IDS[i]);
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
            getReviewPhaseData(), getAppealsPhaseData()};
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

    /**
     * <p>Verifies that the current page displayed to user displays the composite review results for submission
     * identified by the specified ID in context of specified project correctly.</p>
     *
     * @param projectName a <code>String</code> providing the name of the project.
     * @param submissionId a <code>String</code> providing the ID of selected submission.
     * @throws Exception if an unexpected error occurs.
     */
    private void assertCompositeReviewResultsPage(String projectName, String submissionId) throws Exception {
        // Verify that the page header is "Combined Scorecards"
        Assert.assertEquals("Invalid page content header is displayed", "Combined Review Scorecard",
                            this.user.getText("h3"));

        // Verify that the submission ID, submitter handle, user role are displayed correctly
        Assert.assertTrue("Submission ID is not displayed correctly",
                          this.user.getCurrentPage().asText().indexOf("Submission: " + submissionId) > 0);

        // Verify the project name
        HtmlTable headerTable = (HtmlTable) this.user.getContent().getHtmlElementsByTagName("table").get(0);
        Assert.assertEquals("Incorrect project name is displayed",
                            projectName, headerTable.getCellAt(0, 0).asText().trim());

        // Verify that the composite review result is displayed correctly
        List groupTables = this.user.getContent().getHtmlElementsByAttribute("table", "class", "scorecard");
        Map[] scorecardGroups = Scorecard.DESIGN_REVIEW.getGroups();
        Assert.assertEquals("Incorrect number of groups is displayed", scorecardGroups.length, groupTables.size());
        for (int i = 0; i < scorecardGroups.length; i++) {
            assertCombinedReviewScorecardGroup(scorecardGroups[i], (HtmlTable) groupTables.get(i), submissionId);
        }
    }

    /**
     * <p>Verifies that the details for specified scorecard group are displayed correctly.</p>
     *
     * @param scorecardGroup a <code>Map</code> providing the details for scorecard group.
     * @param htmlTable an <code>HtmlTable</code> providing the details for displayed scorecard group.
     * @throws Exception if an unexpected error occurs.
     */
    private void assertCombinedReviewScorecardGroup(Map scorecardGroup, HtmlTable htmlTable, String submissionId)
        throws Exception {
        Review review1 = null, review2 = null, review3 = null;

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
                review1 = getReview(submissionId, parseReviewerHandle(htmlTable.getCellAt(i, 3)));
                review2 = getReview(submissionId, parseReviewerHandle(htmlTable.getCellAt(i, 4)));
                review3 = getReview(submissionId, parseReviewerHandle(htmlTable.getCellAt(i, 5)));
            } else {
                List divs = htmlTable.getCellAt(i, 0).getHtmlElementsByAttribute("div", "class", "showText");
                if (divs.size() > 0) {
                    HtmlDivision div = (HtmlDivision) divs.get(0);
                    if (div.asText().trim().startsWith("Question ")) {
                        // Verify section question
                        Map question = (Map) sectionQuestionsIterator.next();
                        Assert.assertTrue("Incorrect scorecard question is displayed",
                                          htmlTable.getCellAt(i, 0).asText().trim().
                                              endsWith((String) question.get("description")));
                        Assert.assertEquals("Incorrect scorecard question weight is displayed",
                                            question.get("weight"), htmlTable.getCellAt(i, 1).asText().trim());
                        Assert.assertEquals("Incorrect question score is displayed",
                                            review1.getQuestionScore((String) question.get("scorecard_question_id")),
                                            htmlTable.getCellAt(i, 3).asText().trim());
                        Assert.assertEquals("Incorrect question score is displayed",
                                            review2.getQuestionScore((String) question.get("scorecard_question_id")),
                                            htmlTable.getCellAt(i, 4).asText().trim());
                        Assert.assertEquals("Incorrect question score is displayed",
                                            review3.getQuestionScore((String) question.get("scorecard_question_id")),
                                            htmlTable.getCellAt(i, 5).asText().trim());
                    }
                }
            }
        }
        if (sectionQuestionsIterator != null) {
            Assert.assertFalse("Not all section questions are displayed", sectionQuestionsIterator.hasNext());
        }
        Assert.assertFalse("Not all group sections are displayed", groupSectionsIterator.hasNext());
    }

    /**
     * <p>Gets the reviewer handle provided by the specified cell.</p>
     *
     * @param cell an <code>HtmlTableCell</code> providing the details for table cell.
     * @return a <code>String</code> providing the reviewer handle name contained in the cell.
     */
    private String parseReviewerHandle(HtmlTableCell cell) {
        List links = cell.getHtmlElementsByTagName("a");
        assert (links != null) && (links.size() == 2) : "Two links must be provided";
        return ((HtmlAnchor) links.get(1)).asText();
    }
}
