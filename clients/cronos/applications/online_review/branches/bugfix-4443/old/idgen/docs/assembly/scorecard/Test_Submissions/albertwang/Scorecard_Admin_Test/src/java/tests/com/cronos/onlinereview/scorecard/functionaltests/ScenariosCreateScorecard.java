/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.scorecard.functionaltests;

import junit.framework.TestCase;

import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.ScorecardSearchBundle;
import com.topcoder.management.scorecard.data.*;
import com.topcoder.search.builder.filter.Filter;
/**
 * <p>
 * Scenarios involving "2.1.1. Create Scorecard Activity":
 * </p>
 * <ul>
 * <li>Scenario #1(FTC 42)</li>
 * <li>Scenario #2(FTC 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 54)</li>
 * <li>Scenario #3(FTC 53)</li>
 * <li>Scenario #4(FTC 53)</li>
 * <li>Scenario #5(FTC 53)</li>
 * <li>Scenario #6(FTC 53)</li>
 * <li>Scenario #7(FTC 53)</li>
 * <li>Scenario #8(FTC 53)</li>
 * </ul>
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ScenariosCreateScorecard extends TestCase {
    /** The user to test with. */
    private OnlineReviewUser user = null;
    
    /**
     * Clear the DB.
     * @throws Exception to JUnit
     */
    public void tearDown() throws Exception {
    	DatabaseUtility.getInstance().clearTables();
    }
    /**
     * <p>
     * Scenario #1 (FTC 42):
     * 1. User clicks on "Create New Scorecard"
     * Expected Outcome:
	 * Validation Error is show indicating user does not have permission to create a scorecard.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario1() throws Exception {
/*
    	// initialize a user, can not create scorecard
        user = new OnlineReviewUser(OnlineReviewUser.USER_CANNOT_CREATE_SCORECARD);
        // login
        user.login();
        // click button "Create New Scorecard"
        user.clickCreateNewScorecard();
        
        String page = user.getPageAsText();
        // the user should get permission warning
        assertTrue("User does not see the permission warning.",
        		page.indexOf(Messages.getNoPermissionCreateScorecard()) > 0);
*/
    }
    
    /**
     * <p>
     * Scenario #2 (FTC FTC 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 54):
     * PC : User is logged-in as a "Manager"
     * 1. User clicks on "Create New Scorecard"
	 * 2. User completes Scorecard Details using sample data below
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario2() throws Exception {
    	// initialize a user, can not create scorecard
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        // click button "Create New Scorecard"
        user.clickCreateNewScorecard();
        // create a scorecard
        Scorecard scorecard = new Scorecard();
        scorecard.setName("test_scorecard01");
        scorecard.setVersion("1.0");
        scorecard.setCategory(1); // Component Design
        scorecard.setScorecardType(new ScorecardType(1, "Screening")); // Screening
        scorecard.setMinScore(0);
        scorecard.setMaxScore(100);
        scorecard.setScorecardStatus(new ScorecardStatus(1, "Active")); // Active
        // add one group with one section with one question
        Group group = new Group();
        group.setName("test_group01");
        group.setWeight(100);
        Section section = new Section();
        section.setName("Test_section01");
        section.setWeight(100);
        Question question = new Question();
        question.setDescription("test_question01");
        question.setGuideline("test_questionguideline01");
        question.setQuestionType(new QuestionType(1, "Scale (1-4)"));
        question.setWeight(100);
        question.setUploadDocument(true);
        question.setUploadRequired(false);
        section.addQuestion(question);
        group.addSection(section);
        scorecard.addGroup(group);
        
        // fill the web form to create the scorecard
        user.createScorecard(scorecard);
        
        ScorecardManager mgr = DatabaseUtility.getInstance().getScorecardManager();
        Filter filter = ScorecardSearchBundle.buildNameEqualFilter("test_scorecard01");
        
        Scorecard[] scorecards = mgr.searchScorecards(filter, true);
        assertTrue("There should be one scorecard with name test_scorecard01.", scorecards.length > 0);
    }
    
    /**
     * <p>
     * Scenario #3 (FTC 53):
     * PC: User is logged-in as a "Manager"
	 * 1. User clicks on "Create New Scorecard"
	 * 2. User completes Scorecard Details using sample data below
	 * Expected Outcome:
	 * Validation error is shown indicating Scorecard Name is currently in use and an alternate
	 * name needs to be selected.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario3() throws Exception {
    	// initialize a user, can not create scorecard
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        // click button "Create New Scorecard"
        user.clickCreateNewScorecard();
        // create a scorecard
        Scorecard scorecard = new Scorecard();
        scorecard.setName("Design Screening Scorecard"); // "Design Screening Scorecard" exists in the system
        scorecard.setVersion("1.0");
        scorecard.setCategory(1); // Component Design
        scorecard.setScorecardType(new ScorecardType(1, "Screening")); // Screening
        scorecard.setMinScore(0);
        scorecard.setMaxScore(100);
        scorecard.setScorecardStatus(new ScorecardStatus(1, "Active")); // Active
        // add one group with one section with one question
        Group group = new Group();
        group.setName("test_group01");
        group.setWeight(100);
        Section section = new Section();
        section.setName("Test_section01");
        section.setWeight(100);
        Question question = new Question();
        question.setDescription("test_question01");
        question.setGuideline("test_questionguideline01");
        question.setQuestionType(new QuestionType(1, "Scale (1-4)"));
        question.setWeight(100);
        question.setUploadDocument(true);
        question.setUploadRequired(false);
        section.addQuestion(question);
        group.addSection(section);
        scorecard.addGroup(group);
        
        // fill the web form to create the scorecard
        user.createScorecard(scorecard);
        
        String page = user.getPageAsText();
        // the user should get warning
        assertTrue("User does not see the warning.",
        		page.indexOf(Messages.getValidationErrorScorecardNameExists()) > 0);
    }
    
    /**
     * <p>
     * Scenario #4 (FTC 53):
     * PC: User is logged-in as a "Manager"
	 * 1. User clicks on "Create New Scorecard"
	 * 2. User completes Scorecard Details using sample data below
	 * Expected Outcome:
	 * Validation error is shown indicating Group Weights DO NOT Sum to 100.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario4() throws Exception {
    	// initialize a user, can not create scorecard
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        // click button "Create New Scorecard"
        user.clickCreateNewScorecard();
        // create a scorecard
        Scorecard scorecard = new Scorecard();
        scorecard.setName("test_scorecard04");
        scorecard.setVersion("1.0");
        scorecard.setCategory(1); // Component Design
        scorecard.setScorecardType(new ScorecardType(1, "Screening")); // Screening
        scorecard.setMinScore(0);
        scorecard.setMaxScore(100);
        scorecard.setScorecardStatus(new ScorecardStatus(1, "Active")); // Active
        // add two groups, each with one section with one question
        Group group = new Group();
        group.setName("test_group01");
        group.setWeight(50);
        Section section = new Section();
        section.setName("Test_section01");
        section.setWeight(100);
        Question question = new Question();
        question.setDescription("test_question01");
        question.setGuideline("test_questionguideline01");
        question.setQuestionType(new QuestionType(1, "Scale (1-4)"));
        question.setWeight(100);
        question.setUploadDocument(true);
        question.setUploadRequired(false);
        section.addQuestion(question);
        group.addSection(section);
        scorecard.addGroup(group);
        Group group2 = new Group();
        group2.setName("test_group02");
        group2.setWeight(25); // here the weight is 25, then the weights of groups don't sum to 100
        Section section2 = new Section();
        section2.setName("Test_section02");
        section2.setWeight(100);
        Question question2 = new Question();
        question2.setDescription("test_question02");
        question2.setGuideline("test_questionguideline02");
        question2.setQuestionType(new QuestionType(1, "Scale (1-4)"));
        question2.setWeight(100);
        question2.setUploadDocument(true);
        question2.setUploadRequired(false);
        section2.addQuestion(question2);
        group2.addSection(section2);
        scorecard.addGroup(group2);
        
        // fill the web form to create the scorecard
        user.createScorecard(scorecard);
        
        String page = user.getPageAsText();
        // the user should get warning
        assertTrue("User does not see the warning.",
        		page.indexOf(Messages.getValidationErrorIncorrectGroupWeights()) > 0);
    }
    
    /**
     * <p>
     * Scenario #5 (FTC 53):
     * PC: User is logged-in as a "Manager"
	 * 1. User clicks on "Create New Scorecard"
	 * 2. User completes Scorecard Details using sample data below
	 * Expected Outcome:
	 * Validation error is shown indicating Section Weights DO NOT Sum to 100
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario5() throws Exception {
    	// initialize a user, can not create scorecard
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        // click button "Create New Scorecard"
        user.clickCreateNewScorecard();
        // create a scorecard
        Scorecard scorecard = new Scorecard();
        scorecard.setName("test_scorecard05");
        scorecard.setVersion("1.0");
        scorecard.setCategory(1); // Component Design
        scorecard.setScorecardType(new ScorecardType(1, "Screening")); // Screening
        scorecard.setMinScore(0);
        scorecard.setMaxScore(100);
        scorecard.setScorecardStatus(new ScorecardStatus(1, "Active")); // Active
        // add two groups, each with one section with one question
        Group group = new Group();
        group.setName("test_group01");
        group.setWeight(50);
        Section section = new Section();
        section.setName("Test_section01");
        section.setWeight(100);
        Question question = new Question();
        question.setDescription("test_question01");
        question.setGuideline("test_questionguideline01");
        question.setQuestionType(new QuestionType(1, "Scale (1-4)"));
        question.setWeight(100);
        question.setUploadDocument(true);
        question.setUploadRequired(false);
        section.addQuestion(question);
        group.addSection(section);
        scorecard.addGroup(group);
        Group group2 = new Group();
        group2.setName("test_group02");
        group2.setWeight(50);
        Section section2 = new Section();
        section2.setName("Test_section02");
        section2.setWeight(20); // here the weight is 20, then the weights of the sections don't sum to 100
        Question question2 = new Question();
        question2.setDescription("test_question02");
        question2.setGuideline("test_questionguideline02");
        question2.setQuestionType(new QuestionType(1, "Scale (1-4)"));
        question2.setWeight(100);
        question2.setUploadDocument(true);
        question2.setUploadRequired(false);
        section2.addQuestion(question2);
        group2.addSection(section2);
        scorecard.addGroup(group2);
        
        // fill the web form to create the scorecard
        user.createScorecard(scorecard);
        
        String page = user.getPageAsText();
        // the user should get warning
        assertTrue("User does not see the warning.",
        		page.indexOf(Messages.getValidationErrorIncorrectSectionWeights()) > 0);
    }
    
    /**
     * <p>
     * Scenario #6 (FTC 53):
     * Note: User is logged-in as a "Manager"
	 * 1. User clicks on "Create New Scorecard"
	 * 2. User completes Scorecard Details using sample data below
	 * Expected Outcome:
	 * Validation error is shown indicating Question Weights DO NOT Sum to 100
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario6() throws Exception {
    	// initialize a user, can not create scorecard
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        // click button "Create New Scorecard"
        user.clickCreateNewScorecard();
        // create a scorecard
        Scorecard scorecard = new Scorecard();
        scorecard.setName("test_scorecard06");
        scorecard.setVersion("1.0");
        scorecard.setCategory(1); // Component Design
        scorecard.setScorecardType(new ScorecardType(1, "Screening")); // Screening
        scorecard.setMinScore(0);
        scorecard.setMaxScore(100);
        scorecard.setScorecardStatus(new ScorecardStatus(1, "Active")); // Active
        // add two groups, each with one section with one question
        Group group = new Group();
        group.setName("test_group01");
        group.setWeight(50);
        Section section = new Section();
        section.setName("Test_section01");
        section.setWeight(100);
        Question question = new Question();
        question.setDescription("test_question01");
        question.setGuideline("test_questionguideline01");
        question.setQuestionType(new QuestionType(1, "Scale (1-4)"));
        question.setWeight(100);
        question.setUploadDocument(true);
        question.setUploadRequired(false);
        section.addQuestion(question);
        group.addSection(section);
        scorecard.addGroup(group);
        Group group2 = new Group();
        group2.setName("test_group02");
        group2.setWeight(50);
        Section section2 = new Section();
        section2.setName("Test_section02");
        section2.setWeight(100);
        Question question2 = new Question();
        question2.setDescription("test_question02");
        question2.setGuideline("test_questionguideline02");
        question2.setQuestionType(new QuestionType(1, "Scale (1-4)"));
        question2.setWeight(20); // here the weight is 20, then the weights of the question don't sum to 100
        question2.setUploadDocument(true);
        question2.setUploadRequired(false);
        section2.addQuestion(question2);
        group2.addSection(section2);
        scorecard.addGroup(group2);
        
        // fill the web form to create the scorecard
        user.createScorecard(scorecard);
        
        String page = user.getPageAsText();
        // the user should get warning
        assertTrue("User does not see the warning.",
        		page.indexOf(Messages.getValidationErrorIncorrectQuestionWeights()) > 0);
    }
    
    /**
     * <p>
     * Scenario #7 (FTC 53):
     * Note: User is logged-in as a "Manager"
	 * 1. User clicks on "Create New Scorecard"
	 * 2. User completes Scorecard Details using sample data below
	 * Expected Outcome:
	 * Validation error is shown indicating Required Fields are MISSING
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario7() throws Exception {
    	// initialize a user, can not create scorecard
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        // click button "Create New Scorecard"
        user.clickCreateNewScorecard();
        // create a scorecard
        Scorecard scorecard = new Scorecard();
        scorecard.setName("test_scorecard07");
        scorecard.setVersion("1.0");
        scorecard.setCategory(1); // Component Design
        scorecard.setScorecardType(new ScorecardType(1, "Screening")); // Screening
        scorecard.setMinScore(0);
        scorecard.setMaxScore(100);
        scorecard.setScorecardStatus(new ScorecardStatus(1, "Active")); // Active
        // add one group with one section with one question
        Group group = new Group();
        group.setName("test_group01");
        group.setWeight(100);
        Section section = new Section();
        section.setName("Test_section01");
        section.setWeight(100);
        Question question = new Question();
        question.setDescription(""); // description is missing
        question.setGuideline(""); // guideline is missing
        question.setQuestionType(new QuestionType(1, "Scale (1-4)"));
        question.setWeight(100);
        question.setUploadDocument(true);
        question.setUploadRequired(false);
        section.addQuestion(question);
        group.addSection(section);
        scorecard.addGroup(group);
        
        // fill the web form to create the scorecard
        user.createScorecard(scorecard);
        
        String page = user.getPageAsText();
        // the user should get warning
        assertTrue("User does not see the warning.",
        		page.indexOf(Messages.getValidationErrorFieldMissing()) > 0);
    }
    
    /**
     * <p>
     * Scenario #8 (FTC 53):
     * Note: User is logged-in as a "Manager"
	 * 1. User clicks on "Create New Scorecard"
	 * 2. User completes Scorecard Details using sample data below
	 * Expected Outcome:
	 * Validation error is shown indicating Scorecard Version Number is INVALID and a VALID
	 * Number needs to be selected before proceeding.
     * </p>
     * @throws Exception to JUnit
     */
    public void testScenario8() throws Exception {
    	// initialize a user, can not create scorecard
        user = new OnlineReviewUser(OnlineReviewUser.USER_SUPER);
        // login
        user.login();
        // click button "Create New Scorecard"
        user.clickCreateNewScorecard();
        // create a scorecard
        Scorecard scorecard = new Scorecard();
        scorecard.setName("test_scorecard08");
        scorecard.setVersion("1.X");
        scorecard.setCategory(1); // Component Design
        scorecard.setScorecardType(new ScorecardType(1, "Screening")); // Screening
        scorecard.setMinScore(0);
        scorecard.setMaxScore(100);
        scorecard.setScorecardStatus(new ScorecardStatus(1, "Active")); // Active
        // add one group with one section with one question
        Group group = new Group();
        group.setName("test_group01");
        group.setWeight(100);
        Section section = new Section();
        section.setName("Test_section01");
        section.setWeight(100);
        Question question = new Question();
        question.setDescription("test_question01");
        question.setGuideline("test_questionguideline01");
        question.setQuestionType(new QuestionType(1, "Scale (1-4)"));
        question.setWeight(100);
        question.setUploadDocument(true);
        question.setUploadRequired(false);
        section.addQuestion(question);
        group.addSection(section);
        scorecard.addGroup(group);
        
        // fill the web form to create the scorecard
        user.createScorecard(scorecard);
        
        String page = user.getPageAsText();
        // the user should get warning
        assertTrue("User does not see the warning.",
        		page.indexOf(Messages.getValidationErrorInvalidVersion()) > 0);
    }
}
