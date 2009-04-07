/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.AbstractTestCase;
import org.dbunit.dataset.IDataSet;

import java.util.List;
import java.util.ArrayList;

import junit.framework.Assert;

/**
 * <p></p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ViewAggregationFunctionalTest extends AbstractTestCase {

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
     * <p>A <code>String</code> representing a aggregator.</p>
     */
    private static final String AGGREGATOR = UserSimulator.PRIMARY_REVIEWER;

    /**
     * <p>Scenario #131</p>
     * <pre>
     * Note: User has permission to view Aggregation Scorecard
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Aggregation/Review" tab
     * 4.  User clicks on "View Results"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Aggregation Review is displayed. </p>
     */
    public void testScenario131() throws Exception {
        String[] authorizedUsers;
        authorizedUsers = getAuthorizedUsers();
        for (int j = 0; j < authorizedUsers.length; j++) {
            setUser(authorizedUsers[j]);
            this.user.openAggregationResultsPage(PROJECT_NAME);
            assertAggregationResultsPage();
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
            getAggregationReviewPhaseData()};
    }

    /**
     * <p>Gets the list of users which are granted the permission for viewing the aggregation results for the winning
     * submission.</p>
     *
     * @return a <code>String</code> array providing the usernames for users which are granted the permission for
     *         viewing the aggregation results for winning submission.
     */
    private String[] getAuthorizedUsers() {
        List result = new ArrayList();
        result.add(UserSimulator.MANAGER);
        result.add(UserSimulator.PRIMARY_REVIEWER);
        return (String[]) result.toArray(new String[result.size()]);
    }

    /**
     * <p>Verifies that <code>Aggregation Results</code> page is displayed.</p>
     */
    private void assertAggregationResultsPage() {
        // Verify that the page header is "Screening Scorecard"
        Assert.assertEquals("Invalid page content header is displayed", "Aggregation Worksheet",
                            this.user.getText("h3"));

        // Verify that the screener, submission ID, user role are displayed correctly
        Assert.assertTrue("Submission ID is not displayed correctly",
                          this.user.getCurrentPage().asText().indexOf("Submission: 1") > 0);
        Assert.assertTrue("Aggregation handle is not displayed correctly",
                           this.user.getCurrentPage().asText().indexOf("Aggregatior: " + AGGREGATOR) > 0);
    }
}
