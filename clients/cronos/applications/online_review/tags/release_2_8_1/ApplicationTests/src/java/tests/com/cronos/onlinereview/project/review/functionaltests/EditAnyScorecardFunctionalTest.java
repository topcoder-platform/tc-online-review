/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.review.functionaltests;

import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.AbstractTestCase;
import org.dbunit.dataset.IDataSet;

/**
 * @author TCSTester
 * @version 1.0
 */
public class EditAnyScorecardFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the sample project (as displayed by the list of projects) which is
     * used for testing purposes.</p>
     */
    public static final String PROJECT_NAME = "Ajax Timed Survey version 1.0";

    /**
     * <p>Constructs new <code>EditAnyScorecardFunctionalTest</code> instance.</p>
     *
     * @param name a <code>String</code> providing the name of the test.
     */
    public EditAnyScorecardFunctionalTest(String name) {
        super(name);
    }

    /**
     * <p>Scenario #161</p>
     * <pre>
     * Note: User is logged-in as a "Manager"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User selects a tab and clicks on a "Scorecard"
     * 4.  User clicks on "Edit Scorecard"
     * 5.  User enters sample text below in "Manager Comment" text box for one or moany
     * of the Scorecard responses:
     *
     * Field Name                                 Sample Data Value
     * Manager Comment                            Test_managercomment01
     * Fixed (Option Button)                          (Checked)
     * Not Fixed (Option Button)                      (Unchecked)
     *
     * 6.  User clicks on "Preview"
     * 7.  User clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Edited Scorecard is saved and is viewable to all users. </p>
     */
    public void testScenario161() throws Exception {
        setUser(UserSimulator.MANAGER);
        this.user.openFinalReviewScorecardPage(PROJECT_NAME);
        this.user.submitFinalReview("Fixed", "test_managercomment01", true, true, true);
        assertData("data/expected/ManagerFinalReviewCommentsCommitted.xml",
                   "The Manager comments are not saved as expected");
    }

    /**
     * <p>Scenario #162</p>
     * <pre>
     * Note: User is logged-in as a "Manager"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User selects a tab and clicks on a "Scorecard"
     * 4.  User clicks on "Edit Scorecard"
     * 5.  User enters sample text below in "Manager Comment" text box for one or moany
     * of the Scorecard responses:
     *
     * Field Name                                 Sample Data Value
     * Manager Comment                            Test_managercomment01
     * Fixed (Option Button)                          (Checked)
     * Not Fixed (Option Button)                      (Unchecked)
     *
     * 6.  User clicks on "Save and Mark Complete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Edited Scorecard is saved and is viewable to all users. </p>
     */
    public void testScenario162() throws Exception {
        setUser(UserSimulator.MANAGER);
        this.user.openFinalReviewScorecardPage(PROJECT_NAME);
        this.user.submitFinalReview("Fixed", "test_managercomment01", true, true, false);
        assertData("data/expected/ManagerFinalReviewCommentsCommitted.xml",
                   "The Manager comments are not saved as expected");
    }

    /**
     * <p>Scenario #163</p>
     * <pre>
     * Note: User is logged-in as a "Manager"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User selects a tab and clicks on a "Scorecard"
     * 4.  User clicks on "Edit Scorecard"
     * 5.  User enters sample text below in "Manager Comment" text box for one or moany
     * of the Scorecard responses:
     *
     * Field Name                                 Sample Data Value
     * Manager Comment                            Test_managercomment01
     * Fixed (Option Button)                          (Checked)
     * Not Fixed (Option Button)                      (Unchecked)
     *
     * 6.  User clicks on "Save and Finish Later"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Edited Scorecard is saved and is viewable to all users. </p>
     */
    public void testScenario163() throws Exception {
        setUser(UserSimulator.MANAGER);
        this.user.openFinalReviewScorecardPage(PROJECT_NAME);
        this.user.submitFinalReview("Fixed", "test_managercomment01", true, false, false);
        assertData("data/expected/ManagerFinalReviewCommentsCommitted.xml",
                   "The Manager comments are not saved as expected");
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
            getAggregationReviewPhaseData(), getFinalFixPhaseData(), getFinalReviewPhaseData()};
    }
}
