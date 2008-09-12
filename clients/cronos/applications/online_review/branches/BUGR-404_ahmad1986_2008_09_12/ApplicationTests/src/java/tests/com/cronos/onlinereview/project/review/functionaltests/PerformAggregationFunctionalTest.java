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
 * @author TCSTester
 * @version 1.0
 */
public class PerformAggregationFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the sample project (as displayed by the list of projects) which is
     * used for testing purposes.</p>
     */
    public static final String PROJECT_NAME = "Ajax Timed Survey version 1.0";

    /**
     * <p>A <code>String</code> representing a aggregator.</p>
     */
    private static final String AGGREGATOR = UserSimulator.PRIMARY_REVIEWER;

    /**
     * <p>Scenario #126</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation/Review" tab
     * 4.  User clicks on "Aggregation Scorecard" for selected submission
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating User does NOT have permission to perform an Aggregation Review. </p>
     */
    public void testScenario126() throws Exception {
        String[] unauthorizedUsers;
        unauthorizedUsers = getUnauthorizedUsers();
        for (int j = 0; j < unauthorizedUsers.length; j++) {
            setUser(unauthorizedUsers[j]);
            this.user.openAggregationResultsPage(PROJECT_NAME);
            assertDisplayedMessage(Messages.getNoPermissionViewAggregation());
        }
    }

    /**
     * <p>Scenario #127</p>
     * <pre>
     * Note: User is logged-in as an "Aggregator"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation/Review" tab
     * 4.  User clicks on "Aggregation Scorecard" for selected submission
     * 5.  User clicks on "Add Response"
     * 6.  User enters sample data below for all Reviewer Responses:
     * Field Name                                 Sample Data Value
     * Response Text                              test_performaggregationtext01
     * Type Required                              Rejected Accepted Duplicate
     * 7.  User clicks on "Preview" and previews a read-only version of the scorecard
     * 8.  User completes preview of scorecard and clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Scorecard is saved, submitted, and marked as "Complete" in the submission list. </p>
     */
    public void testScenario127() throws Exception {
        setUser(AGGREGATOR);
        this.user.openAggregationResultsPage(PROJECT_NAME);
        this.user.submitAggregation("test_performaggregationtext01", "Accept", "Required", true, true, true);
        assertData("data/expected/AggregationCommitted.xml", "The Aggregation Results are not saved as expected");
    }

    /**
     * <p>Scenario #128</p>
     * <pre>
     * Note: User is logged-in as an "Aggregator"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation/Review" tab
     * 4.  User clicks on "Aggregation Scorecard" for selected submission
     * 5.  User clicks on "Add Response"
     * 6.  User enters sample data below for all Reviewer Responses:
     * Field Name                                 Sample Data Value
     * Response Text                              test_performaggregationtext01
     * Type Required                              Rejected Accepted Duplicate
     * 7.  User clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Scorecard is saved, submitted, and marked as "Complete" in the submission list. </p>
     */
    public void testScenario128() throws Exception {
        setUser(AGGREGATOR);
        this.user.openAggregationResultsPage(PROJECT_NAME);
        this.user.submitAggregation("test_performaggregationtext01", "Accept", "Required", true, true, false);
        assertData("data/expected/AggregationCommitted.xml", "The Aggregation Results are not saved as expected");
    }

    /**
     * <p>Scenario #129</p>
     * <pre>
     * Note: User is logged-in as an "Aggregator"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation/Review" tab
     * 4.  User clicks on "Aggregation Scorecard" for selected submission
     * 5.  User clicks on "Add Response"
     * 6.  User enters sample data below for first Reviewer Responses:
     *
     * Field Name                                 Sample Data Value
     * Response Text                              test_performaggregationtext01
     * Type Required
     * Rejected
     * Accepted
     * Duplicate
     *
     * 7.  User clicks on "Save and Finish Later"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Scorecard is saved and marked as "Pending" in the submission list. </p>
     */
    public void testScenario129() throws Exception {
        setUser(AGGREGATOR);
        this.user.openAggregationResultsPage(PROJECT_NAME);
        this.user.submitAggregation("test_performaggregationtext01", "Accept", "Required", false, true, false);
        assertData("data/expected/AggregationPending.xml", "The Aggregation Results are not saved as expected");
    }

    /**
     * <p>Scenario #130</p>
     * <pre>
     * Note: User is logged-in as an "Aggregator"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation/Review" tab
     * 4.  User clicks on "Aggregation Scorecard" for selected submission
     * 5.  User clicks on "Add Response"
     * 6.  User enters sample data below for first Reviewer Responses:
     *
     * Field Name                                 Sample Data Value
     * Rejected
     * Accepted
     * Duplicate
     *
     * 7.  User clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating all required fields within Scorecard MUST be completed before scorecard can
     * be saved. </p>
     */
    public void testScenario130() throws Exception {
        setUser(AGGREGATOR);
        this.user.openAggregationResultsPage(PROJECT_NAME);
        this.user.submitAggregation("test_performaggregationtext01", "Accept", "Required", true, false, false);
        assertDisplayedMessage(Messages.getInvalidInputAggregationIncomplete());
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
            getReviewPhaseData(), getAppealsPhaseData(), getAppealsResponsePhaseData(), getAggregationPhaseData()};
    }

    /**
     * <p>Gets the list of users which are not granted the permission for viewing the aggregation results for the
     * winning submission.</p>
     *
     * @return a <code>String</code> array providing the usernames for users which are not granted the permission for
     *         viewing the aggregation results for winning submission.
     */
    private String[] getUnauthorizedUsers() {
        List result = new ArrayList();
        result.add(UserSimulator.REVIEWER1);
        result.add(UserSimulator.REVIEWER2);
        result.add(UserSimulator.SECOND_PLACE_SUBMITTER);
        result.add(UserSimulator.THIRD_PLACE_SUBMITTER);
        result.add(UserSimulator.DESIGNER);
        result.add(UserSimulator.OBSERVER);
        return (String[]) result.toArray(new String[result.size()]);
    }
}
