/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.sql.Connection;
import java.util.Map;

/**
 * The unit tests for <code>Primary Review Appeals Response</code> phase.
 * 
 * @author TCSASSEMBER
 * @version 1.0
 */
public class PrimaryAppealsResponseTests extends ProjectTests {
    /**
     * Represents the submitter role id.
     */
    private static String SUBMITTER_ROLE_ID = "1";

    /**
     * Represents the primary review evaluator role id.
     */
    private static String PRIMARY_REVIEW_EVALUATOR_ROLE_ID = "20";

    /**
     * Represents the secondary reviewer role id.
     */
    private static String SECONDARY_REVIEWER_ROLE_ID = "19";

    /**
     * Represents the first secondary review id.
     */
    private long secondaryReview1;

    /**
     * Represents the submitter resource id.
     */
    private long submitter;

    /**
     * Represents the first secondary reviewer resource id.
     */
    private long secondaryReviewer1;

    /**
     * Represents the second secondary reviewer resource id.
     */
    private long secondaryReviewer2;

    /**
     * Represents the project phsae ids.
     */
    private Map<String, Long> phaseIds;

    /**
     * Sets up the testing environment.
     * 
     * @throws Exception
     *             if any error occurs.
     */
    public void setUp() throws Exception {
        super.setUp();
        phaseIds = TestHelper.createProjectWithNewReviewSystem(projectId, true, true);

        Connection con = TestHelper.getConnection();
        // Add a Submitter resource
        submitter = TestHelper.AddResource(projectId, SUBMITTER_ROLE_ID, -1, 132458, "user", con);
        // Add a Primary Review Evaluator resource
        TestHelper.AddResource(projectId, PRIMARY_REVIEW_EVALUATOR_ROLE_ID,
                phaseIds.get("primary_review_evaluation_phase"), 124766, "twight", con);
        // Add a Secondary Reviewer resource
        secondaryReviewer1 = TestHelper.AddResource(projectId, SECONDARY_REVIEWER_ROLE_ID,
                phaseIds.get("secondary_reviewer_review_phase"), 124834, "lightspeed", con);
        // Add a Secondary Reviewer resource
        secondaryReviewer2 = TestHelper.AddResource(projectId, SECONDARY_REVIEWER_ROLE_ID,
                phaseIds.get("secondary_reviewer_review_phase"), 124916, "Yoshi", con);

        // Add a submission
        long submission = TestHelper.addSubmission(projectId, submitter, 1, con);

        // Add a review for secondary Reviewer1
        long[] ids = TestHelper.AddReview(secondaryReviewer1, submission, con);
        secondaryReview1 = ids[0];
        long secondaryReview1Item = ids[1];
        // Add a review for secondary Reviewer2
        TestHelper.AddReview(secondaryReviewer2, submission, con);

        // close specification submission phase
        TestHelper.closePhase(phaseIds.get("specific_submission_phase"), con);
        // close specification review phase
        TestHelper.closePhase(phaseIds.get("specific_review_phase"), con);
        // close submission phase
        TestHelper.closePhase(phaseIds.get("submission_phase"), con);
        // close screening phase
        TestHelper.closePhase(phaseIds.get("screening_phase"), con);
        // close secondary reviewer review phase
        TestHelper.closePhase(phaseIds.get("secondary_reviewer_review_phase"), con);
        // close primary review evaluation phase
        TestHelper.closePhase(phaseIds.get("primary_review_evaluation_phase"), con);
        // close new appeals phase
        TestHelper.closePhase(phaseIds.get("new_appeals_phase"), con);
        // open primary review appeals response phase
        TestHelper.openPhase(phaseIds.get("primary_review_appeals_response_phase"), con);

        // Add submitter's appeal to first review board
        TestHelper.AddAppeal(submitter, secondaryReview1Item, con);
        // Add secondary reviewer's appeal to first review board
        TestHelper.AddAppeal(secondaryReviewer1, secondaryReview1Item, con);
    }

    /**
     * Tear down the testing environment.
     * 
     * @throws Exception
     *             if any error occurs.
     */
    public void tearDown() throws Exception {
        // logout and relogin as manager
        browser.open(TestHelper.getBaseURL() + "/actions/Logout.do?method=logout");
        TestHelper.loginUser(browser);

        super.tearDown();
    }

    /**
     * Test that the submitter can view all the reviews in Primary Review Appeals Response phase.
     * 
     * @throws Exceptionif
     *             any error occurs.
     */
    public void testUserCanSeeAllReviews() throws Exception {
        // login the user first
        TestHelper.loginAsCompetitor(browser, "user", "password");
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        String text = browser.getText("//div[@id='sc4']/table/tbody/");
        // the user can see all the reviews
        assertTrue("The user should see all the reviews.", text.contains("lightspeed"));
        assertTrue("The user should see all the reviews.", text.contains("Yoshi"));
        assertTrue("The user should see all the reviews.", text.contains("100.0"));
        assertTrue("The user should see the appeal status.", text.contains("0 / 1"));
        assertTrue("The user should see the appeal status.", text.contains("0 / 0"));
    }

    /**
     * Test that the secondary reviewer can only see his own review in Primary Review Appeals Response phase.
     * 
     * @throws Exception
     *             if any error occurs.
     */
    public void testSecondaryReviewerCanOnlySeeOwnReview() throws Exception {
        // login the user first
        TestHelper.loginAsCompetitor(browser, "lightspeed", "password");
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        String text = browser.getText("//div[@id='sc4']/table/tbody/");
        // the user can see all the reviews
        assertTrue("The user should see his review.", text.contains("lightspeed"));
        assertFalse("The user can not see other review.", text.contains("Yoshi"));
        assertTrue("The user should see the review score.", text.contains("100.0"));
        assertTrue("The user should see the appeal status.", text.contains("0 / 1"));
    }

    /**
     * Test that the primary review evaluator can view all the reviews in Primary Review Appeals Response phase.
     * 
     * @throws Exception
     *             if any error occurs.
     */
    public void testPrimaryReviewerCanSeeAllReviews() throws Exception {
        // login the user first
        TestHelper.loginAsCompetitor(browser, "twight", "password");
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        String text = browser.getText("//div[@id='sc4']/table/tbody/");
        // the user can see all the reviews
        assertTrue("The user should see all the reviews.", text.contains("lightspeed"));
        assertTrue("The user should see all the reviews.", text.contains("Yoshi"));
        assertTrue("The user should see the review score.", text.contains("100.0"));
        assertTrue("The user should see the appeal status.", text.contains("0 / 2"));
        assertTrue("The user should see the appeal status.", text.contains("0 / 0"));
    }

    /**
     * Test that the primary evaluator can submit appeal response in Primary Review Appeals Response phase.
     * 
     * @throws Exception
     *             if any error occurs.
     */
    public void testPrimaryReviewEvaluatorResponse() throws Exception {
        // login the user first
        TestHelper.loginAsCompetitor(browser, "twight", "password");
        browser.open(TestHelper.getBaseURL() + "/actions/ViewSecondaryReview.do?method=viewSecondaryReview&rid="
                + secondaryReview1);
        browser.type("//tr[@name='placeAppealResponse_0']/td/textarea/", "my response");
        browser.select("//select[@name='answer[0]']", "label=3 - Agree");
        browser.click("//img[@alt='Submit Response']");
        browser.waitForCondition(
                "document.getElementsByName('placeAppealResponse_0').length == 0 || document.getElementsByName('placeAppealResponse_0')[0].style.display == 'none'",
                TestHelper.getTimeout());

        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        String text = browser.getText("//div[@id='sc4']/table/tbody/");
        // all the appeals should have response
        assertTrue("The user should see the appeal status.", text.contains("2 / 2"));
        // the score was changed from 100.0 to 75.0
        assertTrue("The review score should be changed.", text.contains("75.0"));

        assertNoErrorsOccurred();
    }
}
