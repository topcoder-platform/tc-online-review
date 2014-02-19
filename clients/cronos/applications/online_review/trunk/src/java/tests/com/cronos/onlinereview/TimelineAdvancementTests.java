/*
 * Copyright (C) 2011-2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.sql.Connection;


/**
 * Online review functional tests 4, testing timeline advancement.
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class TimelineAdvancementTests extends ProjectTests {

    /**
     * Sets up the testing environment.
     *
     * @throws Exception if any error occurs.
     */
    public void setUp() throws Exception {
        super.setUp();
        Connection con = TestHelper.getConnection();
        try {
            // add  submitter.
            long Submitter = TestHelper.AddResource(projectId, 1, phaseIds.get("registration") , Long.parseLong(TestHelper.getCompetitiorUserId()), TestHelper.getCompetitorUsername(), con);
            // add screener.
            TestHelper.AddResource(projectId, 2, phaseIds.get("screening") , Long.parseLong(TestHelper.TESTS_USER_ID), TestHelper.getUsername(), con);
            // add reviewer.
            TestHelper.AddResource(projectId, 4, phaseIds.get("review") , Long.parseLong(TestHelper.TESTS_USER_ID), TestHelper.getUsername(), con);
            // add aggregator.
            TestHelper.AddResource(projectId, 8, phaseIds.get("aggregation") , Long.parseLong(TestHelper.TESTS_USER_ID), TestHelper.getUsername(), con);
            // add final reviewer.
            TestHelper.AddResource(projectId, 9, phaseIds.get("final_review") , Long.parseLong(TestHelper.TESTS_USER_ID), TestHelper.getUsername(), con);
            // add approver.
            TestHelper.AddResource(projectId, 10, phaseIds.get("approval") , Long.parseLong(TestHelper.TESTS_USER_ID), TestHelper.getUsername(), con);
            // add contest submission.
            TestHelper.AddSubmission(projectId, phaseIds.get("submission"), Submitter, 1, con);
        } finally {
            con.close();
        }
    }

    /**
     * Test Case Number: FTC148 RS10.3 Verify timeline advancement for a Contest with submissions which all failed screening
     *
     * @throws Exception if any error occurs
     */
    public void testReviewPhaseCannotOpen() throws Exception {
        // login as manager
        TestHelper.loginUser(browser);
        // open the project details page
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);

        // open spec submission phase
        TestHelper.OpenPhaseByUI(browser, 1, "Specification Submission");
        // Add spec submitter role
        TestHelper.addRoles(browser, "Specification Submitter", TestHelper.getUsername());
        // Add spec reviewer role
        TestHelper.addRoles(browser, "Specification Reviewer", TestHelper.getUsername());
        // Upload specification
        browser.click("//table[@id='myRolesTable']/tbody/tr[2]/td[2]/a/b");
        browser.waitForPageToLoad(TIMEOUT);
        browser.type("file", TestHelper.getUploadFilePath());
        browser.click("//input[@alt='Upload']");
        browser.waitForPageToLoad(TIMEOUT);
        // close spec submission phase
        TestHelper.ClosePhaseByUI(browser, 1, "Specification Submission");

        // open spec review phase
        TestHelper.OpenPhaseByUI(browser, 2, "Specification Review");
        // submit specification review
        TestHelper.submitScorecard(browser, "4 - Strongly Agree", "Return to Project Details", true);
        // close spec review phase
        TestHelper.ClosePhaseByUI(browser, 2, "Specification Review");

        // Open registration phase
        TestHelper.OpenPhaseByUI(browser, 3, "registration");
        // Open submission phase
        TestHelper.OpenPhaseByUI(browser, 4, "submission");

        // Close registration phase
        TestHelper.ClosePhaseByUI(browser, 3, "registration");
        // Close submission phase
        TestHelper.ClosePhaseByUI(browser, 4, "submission");


        // open screening phase
        TestHelper.OpenPhaseByUI(browser, 5, "screening");
        //submit  Screen Scorecard 
        TestHelper.submitScorecard(browser, "2 - Somewhat Agree", "Return to Project Details", false);
        // Check Post-Mortem have not open
        assertFalse("Post-Mortem Should not be opened!", browser.isTextPresent("Post-Mortem"));
        // Close screening phase
        TestHelper.ClosePhaseByUI(browser, 5, "screening");

        // Check Post-Mortem be opened
        assertTrue("Post-Mortem Should be opened!", browser.isTextPresent("Post-Mortem"));

         // open review phase
        TestHelper.OpenPhaseByUI(browser, 7, "review");

        //Check the review phase can not be opened
        assertTrue("error occurs!", browser.isTextPresent("There were validation errors. See below."));
        assertTrue("error occurs!", browser.isTextPresent("Cannot open the Review phase"));

        // Click 'cancel'
        browser.click("//img[@alt='Cancel']");
        browser.waitForPageToLoad(TIMEOUT);
        assertNoErrorsOccurred();
    }


    /**
     * Test Case Number: FTC149 RS10.4 Verify timeline advancement for a Contest with submissions which all failed review
     *
     * @throws Exception if any error occurs
     */
    public void testAggregationPhaseCannotOpen() throws Exception {
        // login as manager
        TestHelper.loginUser(browser);
        // open the project details page
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);

        // open spec submission phase
        TestHelper.OpenPhaseByUI(browser, 1, "Specification Submission");
        // Add spec submitter role
        TestHelper.addRoles(browser, "Specification Submitter", TestHelper.getUsername());
        // Add spec reviewer role
        TestHelper.addRoles(browser, "Specification Reviewer", TestHelper.getUsername());
        // Upload specification
        browser.click("//table[@id='myRolesTable']/tbody/tr[2]/td[2]/a/b");
        browser.waitForPageToLoad(TIMEOUT);
        browser.type("file", TestHelper.getUploadFilePath());
        browser.click("//input[@alt='Upload']");
        browser.waitForPageToLoad(TIMEOUT);
        // close spec submission phase
        TestHelper.ClosePhaseByUI(browser, 1, "Specification Submission");

        // open spec review phase
        TestHelper.OpenPhaseByUI(browser, 2, "Specification Review");
        // submit specification review
        TestHelper.submitScorecard(browser, "4 - Strongly Agree", "Return to Project Details", true);
        // close spec review phase
        TestHelper.ClosePhaseByUI(browser, 2, "Specification Review");
        // Open registration phase
        TestHelper.OpenPhaseByUI(browser, 3, "registration");
        // Open submission phase
        TestHelper.OpenPhaseByUI(browser, 4, "submission");

        // Close registration phase
        TestHelper.ClosePhaseByUI(browser, 3, "registration");
        // Close submission phase
        TestHelper.ClosePhaseByUI(browser, 4, "submission");


        // open screening phase
        TestHelper.OpenPhaseByUI(browser, 5, "screening");
        //submit Screen Scorecard
        TestHelper.submitScorecard(browser, "4 - Strongly Agree", "Return to Project Details", false);
        // Close screening phase
        TestHelper.ClosePhaseByUI(browser, 5, "screening");

       // open review phase
        TestHelper.OpenPhaseByUI(browser, 6, "review");
        //submit Review Scorecard 
        TestHelper.submitScorecard(browser, "2 - Somewhat Agree", "Return to Project Details", false);
        // Close review phase
        TestHelper.ClosePhaseByUI(browser, 6, "review");

        // open appeal phase
        TestHelper.OpenPhaseByUI(browser, 7, "appeal");
        // Close appeal phase
        TestHelper.ClosePhaseByUI(browser, 7, "appeal");

        // open appeal response phase
        TestHelper.OpenPhaseByUI(browser, 8, "appeal response");
        // Check Post-Mortem have not open
        assertFalse("Post-Mortem Should not be opened!", browser.isTextPresent("Post-Mortem"));
       // Close appeal response phase
        TestHelper.ClosePhaseByUI(browser, 8, "appeal response");
        // Check Post-Mortem be opened
        assertTrue("Post-Mortem Should be opened!", browser.isTextPresent("Post-Mortem"));

        // open aggregation phase
        TestHelper.OpenPhaseByUI(browser, 10, "aggregation");


        //Check the aggregation phase can not be opened
        assertTrue("error occurs!", browser.isTextPresent("There were validation errors. See below."));
        assertTrue("error occurs!", browser.isTextPresent("Cannot open the Aggregation phase"));

        // Click 'cancel'
        browser.click("//img[@alt='Cancel']");
        browser.waitForPageToLoad(TIMEOUT);
        assertNoErrorsOccurred();
    }

    /**
     * Test Case Number: FTC146 RS10.1 Verify timeline advancement for a successful contest
     *
     * @throws Exception if any error occurs
     */
    public void testCompleteProjectSuccessfully() throws Exception {

        CompleteProject();
        // open approval phase
        TestHelper.OpenPhaseByUI(browser, 13, "approval");
        //approval the approval
        SubmitApproval(true,1);
        // close approval phase
        TestHelper.ClosePhaseByUI(browser, 13, "approval");
        //check the project have completed successfully
        String approvalStatus = browser.getText("//div[@id='mainMiddleContent']/div/table[3]/tbody/tr[2]/td[1]/table/tbody/tr[14]/td[2]");
        assertEquals("Approval phase should be closed", "Closed", approvalStatus);
        assertNoErrorsOccurred();
    }



    // upload the final fix
    private void UploadFinalFix() throws Exception{
        // logout the user
        browser.click("link=Logout");
        browser.waitForPageToLoad(TIMEOUT);
        
        // login as submitter
        TestHelper.loginAsCompetitor(browser);
        // open the project details page
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        
        //click submission
        browser.click("//table[@id='myRolesTable']/tbody/tr[2]/td[2]/a");
        browser.waitForPageToLoad(TIMEOUT);
        browser.type("file", TestHelper.getUploadFilePath());
        browser.click("//input[@alt='Upload']");
        Thread.sleep(2*(new Long(TIMEOUT)));
        assertNoErrorsOccurred();

        // logout the user
        browser.click("link=Logout");
        browser.waitForPageToLoad(TIMEOUT);
        // open login page
        browser.open(TestHelper.getBaseURL());
        // login as manager
        TestHelper.loginUser(browser);
        // open the project details page
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
    }

    /*  submit the final review
     *  @param round , final review round
     */
    private void SubmitFinalReview(int round) throws Exception{
        //click submission
        browser.click("//table[@id='myRolesTable']/tbody/tr[2]/td[2]/a");
        browser.waitForPageToLoad(TIMEOUT);
        //add comment
        browser.click("//div[@id='mainMiddleContent']/div/form/table/tbody/tr[5]/td[5]/input");
        browser.type("final_comment[0]","OK");
        if(round == 1) {
            browser.click("//input[@name='approve_fixes']");
        }
        browser.click("//input[@alt='Save and Mark Complete']");
        browser.waitForPageToLoad(TIMEOUT);
    }

    /*
    * submit the approval
    *
    *@param approval indicate approval or not
    *@param round , approval round
    */
    private void SubmitApproval(Boolean approval, int round) throws Exception{
        //click submission
        browser.click("//div[@id='sc" + (7+(round-1)*2) +"']/table/tbody/tr[3]/td[6]/a");
        browser.waitForPageToLoad(TIMEOUT);
        browser.select("answer[0]","label=4 - Strongly Agree");
        if(!approval) {
            browser.type("comment(0.1)", "Reject");
            browser.click("name=approve_fixes value=false");
        } else {
            browser.type("comment(0.1)", "Approval");
            browser.click("name=approve_fixes value=true");
        }
        browser.click("//input[@alt='Save and Mark Complete']");
        if(browser.isConfirmationPresent()) {
            browser.getConfirmation();
            browser.chooseOkOnNextConfirmation();
        }
        browser.waitForPageToLoad(TIMEOUT);
        browser.click("//img[@alt='Return to Project Details']");
        browser.waitForPageToLoad(TIMEOUT);
    }


    /*
    * set final reviewer
    *
    * @param round , final review round
    */
    private void SetFinalReviewer(int round) throws Exception{
         // Click the 'Edit Project' Link
        TestHelper.clickEditProjectLink(browser);
        
        // select the "Final Reviewer" role, no error expected
        browser.select("resources_role[0]", "label=Final Reviewer");
        browser.select("resources_phase[0]", "label=" + round);

        // input the  competitor name
        browser.type("resources_name[0]", TestHelper.getUsername());
        browser.click("//img[@alt='Add']");
        // add explanation
        browser.type("explanation", "add Final Reviewer role");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(TIMEOUT);

    }

    /*
    * Complete the project in one final fix
    *
    */
    private void CompleteProject() throws Exception{
        // login as manager
        TestHelper.loginUser(browser);
        // open the project details page
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        // open spec submission phase
        TestHelper.OpenPhaseByUI(browser, 1, "Specification Submission");
        // Add spec submitter role
        TestHelper.addRoles(browser, "Specification Submitter", TestHelper.getUsername());
        // Add spec reviewer role
        TestHelper.addRoles(browser, "Specification Reviewer", TestHelper.getUsername());
        // Upload specification
        browser.click("//table[@id='myRolesTable']/tbody/tr[2]/td[2]/a/b");
        browser.waitForPageToLoad(TIMEOUT);
        browser.type("file", TestHelper.getUploadFilePath());
        browser.click("//input[@alt='Upload']");
        browser.waitForPageToLoad(TIMEOUT);
        // close spec submission phase
        TestHelper.ClosePhaseByUI(browser, 1, "Specification Submission");

        // open spec review phase
        TestHelper.OpenPhaseByUI(browser, 2, "Specification Review");
        // submit specification review
        TestHelper.submitScorecard(browser, "4 - Strongly Agree", "Return to Project Details", true);
        // close spec review phase
        TestHelper.ClosePhaseByUI(browser, 2, "Specification Review");

        // Open registration phase
        TestHelper.OpenPhaseByUI(browser, 3, "registration");
        // Open submission phase
        TestHelper.OpenPhaseByUI(browser, 4, "submission");


        // Close registration phase
        TestHelper.ClosePhaseByUI(browser, 3, "registration");
        // Close submission phase
        TestHelper.ClosePhaseByUI(browser, 4, "submission");

        // open screening phase
        TestHelper.OpenPhaseByUI(browser, 5, "screening");
        //submit the Screen Scorecard
        TestHelper.submitScorecard(browser,"4 - Strongly Agree", "Return to Project Details", false);

        // Close screening phase
        TestHelper.ClosePhaseByUI(browser, 5, "screening");


        // open review phase
        TestHelper.OpenPhaseByUI(browser, 6, "review");
        //submit  Review Scorecard 
        TestHelper.submitScorecard(browser, "4 - Strongly Agree", "Return to Project Details", false);
        // Close review phase
        TestHelper.ClosePhaseByUI(browser, 6, "review");

        // open appeal phase
        TestHelper.OpenPhaseByUI(browser, 7, "appeal");
        // Close appeal phase
        TestHelper.ClosePhaseByUI(browser, 7, "appeal");

        // open appeal response phase
        TestHelper.OpenPhaseByUI(browser, 8, "appeal response");
        // Close appeal response phase
        TestHelper.ClosePhaseByUI(browser, 8, "appeal response");

        // open aggregation phase
        TestHelper.OpenPhaseByUI(browser, 9, "aggregation");
        //click the Aggregation link
        browser.click("//table[@id='myRolesTable']/tbody/tr[2]/td[2]/a/b");
        browser.waitForPageToLoad(TIMEOUT);
        // add comments
        browser.click("//div[@id='mainMiddleContent']/div/form/table/tbody/tr[5]/td[6]/input");
        browser.type("aggregator_response[0]", "OK");
        browser.click("//input[@alt='Save and Mark Complete']");
        browser.waitForPageToLoad(TIMEOUT);
        // Close aggregation phase
        TestHelper.ClosePhaseByUI(browser, 9, "aggregation");

        // open aggregation review phase
        TestHelper.OpenPhaseByUI(browser, 10, "aggregation review ");
        // Close aggregation review phase
        TestHelper.ClosePhaseByUI(browser, 10, "aggregation review ");

        // open final fix  phase
        TestHelper.OpenPhaseByUI(browser, 11, "final fix");
        // upload final fix
        UploadFinalFix();
        // Close final fix phase
        TestHelper.ClosePhaseByUI(browser, 11, "final fix");

        // open final review phase
        TestHelper.OpenPhaseByUI(browser, 12, "final review");
        // submit final review
        SubmitFinalReview(1);
        // close final review phase
        TestHelper.ClosePhaseByUI(browser, 12, "final review");


    }







}
