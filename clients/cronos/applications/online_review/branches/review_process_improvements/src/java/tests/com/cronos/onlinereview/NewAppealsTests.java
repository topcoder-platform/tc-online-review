/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.sql.Connection;
import java.util.Map;

/**
 * The unit tests for <code>New Appeals</code> phase.
 * 
 * @author TCSASSEMBER
 * @version 1.0
 */
public class NewAppealsTests extends ProjectTests {
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
     * Represents the second secondary review id.
     */
    private long secondaryReview2;

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
        secondaryReview1 = TestHelper.AddReview(secondaryReviewer1, submission, con)[0];
        // Add a review for secondary Reviewer2
        secondaryReview2 = TestHelper.AddReview(secondaryReviewer2, submission, con)[0];

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
        // open new appeals phase
        TestHelper.openPhase(phaseIds.get("new_appeals_phase"), con);
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
     * Test that the submitter can view all the reviews in New Appeals phase.
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
        assertTrue("The user should see the review score.", text.contains("100.0"));
    }

    /**
     * Test that the primary review evaluator can view all the reviews in New Appeals phase.
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
    }

    /**
     * Test that the secondary reviewer can only see his own review in New Appeals phase.
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
        assertTrue("The user should see his reviews.", text.contains("lightspeed"));
        assertFalse("The user can not see other review.", text.contains("Yoshi"));
        assertTrue("The user should see the review score.", text.contains("100.0"));
    }

    /**
     * Test that the submitter can submit appeal in New Appeals phase.
     * 
     * @throws Exception
     *             if any error occurs.
     */
    public void testUserAppeal() throws Exception {
        // login the user first
        TestHelper.loginAsCompetitor(browser, "user", "password");
        browser.open(TestHelper.getBaseURL() + "/actions/ViewSecondaryReview.do?method=viewSecondaryReview&rid="
                + secondaryReview1);
        browser.click("//a[@id='placeAppeal_0']");
        browser.type("appeal_text[0]", "user appeal review1");
        browser.click("//div[@id='appealText_0']/a/");
        browser.waitForCondition(
                "document.getElementById('appealText_0') == null || document.getElementById('appealText_0').className == 'hideText'",
                TestHelper.getTimeout());

        browser.open(TestHelper.getBaseURL() + "/actions/ViewSecondaryReview.do?method=viewSecondaryReview&rid="
                + secondaryReview2);
        browser.click("//a[@id='placeAppeal_0']");
        browser.type("appeal_text[0]", "user appeal review2");
        browser.click("//div[@id='appealText_0']/a/");
        browser.waitForCondition(
                "document.getElementById('appealText_0') == null || document.getElementById('appealText_0').className == 'hideText'",
                TestHelper.getTimeout());

        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        String text = browser.getText("//div[@id='sc4']/table/tbody/");
        // the appeals text should be submitted
        assertTrue("The user should see the appeal status.", text.contains("1 / 1"));

        assertNoErrorsOccurred();
    }

    /**
     * Test that the secodnary reviewer can submit appeal in New Appeals phase.
     * 
     * @throws Exception
     *             if any error occurs.
     */
    public void testSecondaryReviewerAppeal() throws Exception {
        // login the user first
        TestHelper.loginAsCompetitor(browser, "lightspeed", "password");
        browser.open(TestHelper.getBaseURL() + "/actions/ViewSecondaryReview.do?method=viewSecondaryReview&rid="
                + secondaryReview1);
        browser.click("//a[@id='placeAppeal_0']");
        browser.type("appeal_text[0]", "lightspeed appeal review1");
        browser.click("//div[@id='appealText_0']/a/");
        browser.waitForCondition(
                "document.getElementById('appealText_0') == null || document.getElementById('appealText_0').className == 'hideText'",
                TestHelper.getTimeout());

        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        String text = browser.getText("//div[@id='sc4']/table/tbody/");
        // the appeals text should be submitted
        assertTrue("The user should see the appeal status.", text.contains("1 / 1"));

        assertNoErrorsOccurred();
    }

    /**
     * Test the failure of the early close appeal phase. If there is someone didn't click the complete appeals button,
     * then the appeal phase can't be closed.
     * 
     * @throws Exception
     *             if any error occurs.
     */
    public void testEarylyAppealsFail() throws Exception {
        // login the user first
        TestHelper.loginUser(browser);
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        browser.click("//img[@alt='Edit Project']");
        browser.waitForPageToLoad(TIMEOUT);

        browser.click("//tr[@id='loaded_" + phaseIds.get("new_appeals_phase") + "']/td[1]/img[2]/");
        browser.waitForPageToLoad(TIMEOUT);

        assertTrue("Can close appeals phse early if there is a submitter didn't complete appeal",
                browser.isTextPresent("Cannot close the New Appeals phase"));
    }

    /**
     * The success of the early close appeal phase. If all members have clicked the complete appeals button, then the
     * appeal phase can be closed early.
     * 
     * @throws Exception
     *             if any error occurs.
     */
    public void earlyAppealsSucc() throws Exception {
        TestHelper.loginUser(browser);
        Connection con = TestHelper.getConnection();

        TestHelper.earylyCompleteApeal(submitter, con);
        TestHelper.earylyCompleteApeal(secondaryReviewer1, con);
        TestHelper.earylyCompleteApeal(secondaryReviewer2, con);

        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        browser.click("//img[@alt='Edit Project']");
        browser.waitForPageToLoad(TIMEOUT);

        browser.click("//tr[@id='loaded_" + phaseIds.get("new_appeals_phase") + "']/td[1]/img[2]/");
        browser.waitForPageToLoad(TIMEOUT);

        assertFalse("Can close appeals phse early if all submitter complete appeal",
                browser.isTextPresent("Cannot close the New Appeals phase"));
    }
}
