/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.Messages;
import com.cronos.onlinereview.project.AbstractTestCase;
import org.dbunit.dataset.IDataSet;

/**
 * <p>A test case for <code>Submit Scorecard Comment</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class SubmitScorecardCommentFunctionalTest extends AbstractTestCase {

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
    public static final String[] SUBMITTERS
        = new String[] {UserSimulator.WINNING_SUBMITTER, UserSimulator.SECOND_PLACE_SUBMITTER,
                        UserSimulator.THIRD_PLACE_SUBMITTER};

    /**
     * <p>A <code>UserSimulator</code> array representing the reviewers for the project which is used for testing.</p>
     */
    public static final String[] REVIEWERS
        = new String[] {UserSimulator.PRIMARY_REVIEWER, UserSimulator.REVIEWER1, UserSimulator.REVIEWER2};

    /**
     * <p>Scenario #152</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Final Review/Fix" tab
     * 4.  User clicks on "Submit Approval"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating user does NOT have permission to view "Aggregation Scorecard/Worksheet".
     * </p>
     */
    public void testScenario152() throws Exception {
    }

    /**
     * <p>Scenario #153</p>
     * <pre>
     * Note: User is logged-in as "Submitter"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Final Review/Fix" tab
     * 4.  User clicks on "Submit Approval"
     * 5.  User clicks on "Add Comment" for every response and inserts sample text below"
     * "test_sampleaddcomment01"
     * 6.  User checks the "Approve Aggregation Comments" box
     * 7.  User clicks on "Accept"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Aggregation Scorecard is submitted. </p>
     */
    public void testScenario153() throws Exception {
        setUser(UserSimulator.WINNING_SUBMITTER);
        this.user.openAggregationReviewResultsPage(PROJECT_NAME);
        this.user.submitAggregationReview("test_sampleaddcomment01", true, true);
        assertData("data/expected/SubmitterAggregationReviewCommitted.xml",
                   "The Aggregation Review comments are not saved as expected");
    }

    /**
     * <p>Scenario #154</p>
     * <pre>
     * Note: User is logged-in as "Submitter"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Final Review/Fix" tab
     * 4.  User clicks on "Submit Approval"
     * 5.  User clicks on "Add Comment" for the first response and inserts sample text : "test_sampleaddcomment01"
     * 6.  User checks the "Approve Aggregation Comments" box
     * 7.  User clicks on "Accept"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating Incomplete Aggregation Scorecard ­ required "Comments" to responses are
     * missing. </p>
     */
    public void testScenario154() throws Exception {
        setUser(UserSimulator.WINNING_SUBMITTER);
        this.user.openAggregationReviewResultsPage(PROJECT_NAME);
        this.user.submitAggregationReview("test_sampleaddcomment01", true, false);
        assertDisplayedMessage(Messages.getInvalidInputAggregationReviewIncomplete());
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
            getAggregationReviewPhaseData()};
    }
}
