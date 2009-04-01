/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import org.dbunit.dataset.IDataSet;

import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.AbstractTestCase;
import junit.framework.Assert;

/**
 * <p>A test case for <code>View Aggregation Review</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class ViewAggregationReviewFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the sample project (as displayed by the list of projects) which is
     * used for testing purposes.</p>
     */
    public static final String PROJECT_NAME = "Ajax Timed Survey version 1.0";

    /**
     * <p>A <code>UserSimulator</code> representing a aggregator.</p>
     */
    private static final String AGGREGATOR = UserSimulator.PRIMARY_REVIEWER;

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
     * <p>Scenario #139</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation/Review" tab
     * 4.  User clicks "View Results"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * "Aggregation Scorecard" is displayed. </p>
     */
    public void testScenario139() throws Exception {
        for (int i = 0; i < AUTHORIZED_USERS.length; i++) {
            setUser(AUTHORIZED_USERS[i]);
            this.user.openAggregationReviewResultsPage(PROJECT_NAME);
            assertAggregationReviewResultsPage(PROJECT_NAME);
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
            getAggregationReviewPhaseData(), getFinalFixPhaseData()};
    }

    /**
     * <p>Verifies that the current page displayed to user displays the aggregation review results for specified project
     * correctly.</p>
     *
     * @param projectName a <code>String</code> providing the name of the project.
     */
    private void assertAggregationReviewResultsPage(String projectName) {
        // Verify that the page header is "Aggregation Review"
        Assert.assertEquals("Invalid page content header is displayed", "Aggregation Review", this.user.getText("h3"));

        // Verify that the final reviewer handle, submission ID, submitter handle, user role are displayed correctly
        Assert.assertTrue("Submission ID is not displayed correctly",
                          this.user.getCurrentPage().asText().indexOf("Submission: 1") > 0);
        Assert.assertTrue("Aggregator handle is not displayed correctly",
                          this.user.getCurrentPage().asText().
                              indexOf("Aggregator: " + AGGREGATOR) > 0);
    }
}
