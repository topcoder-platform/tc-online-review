/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.Messages;
import com.cronos.onlinereview.project.AbstractTestCase;
import org.dbunit.dataset.IDataSet;

/**
 * <p>A test case for <code>Perform Appeals</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class PerformAppealsFunctionalTest extends AbstractTestCase {

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
     * <p>A <code>String</code> array representing the reviewers for the project which is used for testing.</p>
     */
    public static final String[] REVIEWERS
        = new String[] {UserSimulator.PRIMARY_REVIEWER, UserSimulator.REVIEWER1, UserSimulator.REVIEWER2};

    /**
     * <p>Scenario #114</p>
     * <pre>
     * Note: User is logged-in as a "Submitter"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User selects review scorecard
     * 4.  User selects a question within scorecard
     * 5.  User clicks on "Appeal" - users may elect to Appeal any or all of the questions
     * 6.  User enters appeal data listed in sample below:
     *
     * Field Name                                  Sample Data Value
     * Appeal Text                                 test_sampleappealtext01
     *
     * 7.  User clicks on "Submit Appeal"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * User successfully submits scorecard appeal. </p>
     */
    public void testScenario114() throws Exception {
        for (int i = 0; i < SUBMITTERS.length; i++) {
            setUser(SUBMITTERS[i]);
            for (int j = 0; j < REVIEWERS.length; j++) {
                this.user.openFinalFixReviewTab(PROJECT_NAME);
                this.user.openReviewScorecard(REVIEWERS[j]);
                this.user.submitAppeal("test_sampleappealtext01", null);
            }
        }
        assertData("data/expected/AppealsSubmitted.xml", "The appeals are not saved as expected");
    }

    /**
     * <p>Scenario #115</p>
     * <pre>
     * Note: User is logged-in as a "Submitter"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User selects review scorecard
     * 4.  User selects a question within scorecard
     * 5.  User clicks on "Appeal" - users may elect to Appeal any or all of the questions
     * 6.  User enters appeal data listed in sample below:
     *
     * Field Name                                  Sample Data Value
     * Appeal Text
     *
     * 7.  User clicks on "Submit Appeal"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating "Appeal Text" cannot be blank. </p>
     */
    public void testScenario115() throws Exception {
        for (int i = 0; i < SUBMITTERS.length; i++) {
            setUser(SUBMITTERS[i]);
            for (int j = 0; j < REVIEWERS.length; j++) {
                this.user.openFinalFixReviewTab(PROJECT_NAME);
                this.user.openReviewScorecard(REVIEWERS[j]);
                this.user.submitAppeal("", Messages.getInvalidInputAppealTextEmpty());
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
}
