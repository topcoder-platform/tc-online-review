/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import com.cronos.onlinereview.project.Messages;
import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.AbstractTestCase;
import org.dbunit.dataset.IDataSet;

/**
 * <p></p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class PerformAggregationReviewFunctionalTest extends AbstractTestCase {

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
        = new String[] {UserSimulator.SECOND_PLACE_SUBMITTER, UserSimulator.THIRD_PLACE_SUBMITTER};

    /**
     * <p>A <code>String</code> array representing the reviewers for the project which is used for testing.</p>
     */
    public static final String[] REVIEWERS = new String[] {UserSimulator.REVIEWER1, UserSimulator.REVIEWER2};

    /**
     * <p>A <code>String</code> representing a aggregator.</p>
     */
    private static final String AGGREGATOR = UserSimulator.PRIMARY_REVIEWER;

    /**
     * <p>Scenario #132</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation/Review" tab
     * 4.  User clicks on "Aggregation Scorecard/Worksheet"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating User does NOT have permission to view or edit the Aggregation
     * Scorecard/Worksheet </p>
     */
    public void testScenario132() throws Exception {
        for (int i = 0; i < SUBMITTERS.length; i++) {
            setUser(SUBMITTERS[i]);
            this.user.openAggregationReviewResultsPage(PROJECT_NAME);
            assertDisplayedMessage(Messages.getNoPermissionPerformAggregationReview());
        }
    }

    /**
     * <p>Scenario #133</p>
     * <pre>
     * Note: User is both an "Aggregator" and a "Reviewer"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation/Review" tab
     * 4.  User clicks on "Aggregation Scorecard/Worksheet"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating user is NOT required to complete Aggregation Scorecard/Worksheet </p>
     */
    public void testScenario133() throws Exception {
        setUser(AGGREGATOR);
        this.user.openAggregationReviewResultsPage(PROJECT_NAME);
        assertDisplayedMessage(Messages.getNotRequiredPerformAggregationReview());
    }

    /**
     * <p>Scenario #134</p>
     * <pre>
     * Note: User is logged-in as a "Reviewer"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation/Review" tab
     * 4.  User clicks on "Aggregation Scorecard/Worksheet"
     * 5.  User selects/highlights every "Accept" option buttons on the "Aggregation
     * Scorecard/Worksheet"
     * 6.  User clicks on `Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * "Aggregation Scorecard/Worksheet" is saved, submitted, and marked as "Complete" </p>
     */
    public void testScenario134() throws Exception {
        for (int i = 0; i < REVIEWERS.length; i++) {
            setUser(REVIEWERS[i]);
            this.user.openAggregationReviewResultsPage(PROJECT_NAME);
            this.user.submitAggregationReview("test_aggreationreviewtext01", "Accept", true,
                                              "test_aggreationreviewapproval", true, true, false);
        }
        assertData("data/expected/AggregationReviewCommitted.xml",
                   "The Aggregation Review comments are not saved as expected");
    }

    /**
     * <p>Scenario #135</p>
     * <pre>
     * Note: User is logged-in as a "Reviewer"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation/Review" tab
     * 4.  User clicks on "Aggregation Scorecard/Worksheet"
     * 5.  User selects/highlights every "Accept" option buttons on the "Aggregation
     * Scorecard/Worksheet"
     * 6.  User clicks on "Preview"
     * 7.  User Previews the "Aggregation Scorecard/Worksheet"
     * 8.  User clicks on `Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * "Aggregation Scorecard/Worksheet" is saved, submitted, and marked as "Complete" </p>
     */
    public void testScenario135() throws Exception {
        for (int i = 0; i < REVIEWERS.length; i++) {
            setUser(REVIEWERS[i]);
            this.user.openAggregationReviewResultsPage(PROJECT_NAME);
            this.user.submitAggregationReview("test_aggreationreviewtext01", "Accept", true,
                                              "test_aggreationreviewapproval", true, true, true);
        }
        assertData("data/expected/AggregationReviewCommitted.xml",
                   "The Aggregation Review comments are not saved as expected");
    }

    /**
     * <p>Scenario #136</p>
     * <pre>
     * Note: User is logged-in as a "Reviewer"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation/Review" tab
     * 4.  User clicks on "Aggregation Scorecard/Worksheet"
     * 5.  User selects/highlights 2 of the "Accept" option buttons on the "Aggregation
     * Scorecard/Worksheet"
     * 6.  User clicks on `Save and Finish Later"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * "Aggregation Scorecard/Worksheet" is saved and marked as "Pending" </p>
     */
    public void testScenario136() throws Exception {
        for (int i = 0; i < REVIEWERS.length; i++) {
            setUser(REVIEWERS[i]);
            this.user.openAggregationReviewResultsPage(PROJECT_NAME);
            this.user.submitAggregationReview("test_aggreationreviewtext01", "Accept", true,
                                              "test_aggreationreviewapproval", false, true, false);
        }
        assertData("data/expected/AggregationReviewPending.xml",
                   "The Aggregation Review comments are not saved as expected");
    }

    /**
     * <p>Scenario #137</p>
     * <pre>
     * Note: User is logged-in as a "Reviewer"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation/Review" tab
     * 4.  User clicks on "Aggregation Scorecard/Worksheet"
     * 5.  User selects/highlights 2 of the "Accept" option buttons on the "Aggregation
     * Scorecard/Worksheet"
     * 6.  User clicks on `Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating all required fields must be completed before proceeding. </p>
     */
    public void testScenario137() throws Exception {
        for (int i = 0; i < REVIEWERS.length; i++) {
            setUser(REVIEWERS[i]);
            this.user.openAggregationReviewResultsPage(PROJECT_NAME);
            this.user.submitAggregationReview("test_aggreationreviewtext01", "Accept", true,
                                              "test_aggreationreviewapproval", true, false, false);
            assertDisplayedMessage(Messages.getInvalidInputAggregationReviewIncomplete());
        }
    }

    /**
     * <p>Scenario #138</p>
     * <pre>
     * Note: User is logged-in as a "Reviewer"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation/Review" tab
     * 4.  User clicks on "Aggregation Scorecard/Worksheet"
     * 5.  User selects/highlights first "Reject" option buttons on the "Aggregation
     * Scorecard/Worksheet"
     * 6.  User selects/highlights all remaining "Accept" option buttons on the "Aggregation
     * Scorecard/Worksheet"
     * 7.  User clicks on `Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * "Aggregation Scorecard/Worksheet" is rejected and an email notification is sent to reviewer.  A new Aggregation
     * is generated automatically. </p>
     */
    public void testScenario138() throws Exception {
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
