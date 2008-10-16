/* Copyright (C) 2007 TopCoder Inc., All Rights Reserved. */

package com.cronos.onlinereview.tests.scorecard;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.cronos.onlinereview.AbstractTestCase;
import com.cronos.onlinereview.Configuration;
import com.cronos.onlinereview.tests.User;
import java.io.IOException;
import java.net.MalformedURLException;

public class CreateScorecardTest extends AbstractTestCase
{
    /**
     * Test Scenario #1 (FTC 42)
     * 
     * <ol>
     *      <li>User clicks on "Create a Scorecard"</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation Error indicating user does not have permission to create a scorecard.
     * </p>
     */
    public void testScenario001()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.OBSERVER);
            assertNotLoginPage(mainPage);
            HtmlPage scorecardPage = clickCreateScorecard(mainPage);
            assertTrue("Expected: Vaidation Error indicating user does not have permission to create a scorecard", scorecardPage.asText().toLowerCase().indexOf("not have permission") != -1);
        } 
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #2 (FTC 42-52,54)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User clicks on "Create a Scorecard"</li>
     *      <li>User completes Scorecard Details using sample data below</li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Scorecard Name</td><td>test_scorecard01</td></tr>
     *          <tr><td>Version Number</td><td>1.0</td></tr>
     *          <tr><td>Project Type</td><td>Component</td></tr>
     *          <tr><td>Category</td><td>Design</td></tr>
     *          <tr><td>Type</td><td>Screening</td></tr>
     *          <tr><td>Min Score</td><td>0</td></tr>
     *          <tr><td>Max Score</td><td>100</td></tr>
     *          <tr><td>Status</td><td>Active</td></tr>
     *          <tr><td>Group</td><td>test_group01</td></tr>
     *          <tr><td>Weight (Group)</td><td>100</td></tr>
     *          <tr><td>Section</td><td>test_section01</td></tr>
     *          <tr><td>Weight (Section)</td><td>100</td></tr>
     *          <tr><td>Question Text</td><td>test_question01</td></tr>
     *          <tr><td>Question Guideline</td><td>test_questionguideline01</td></tr>
     *          <tr><td>Type</td><td>Scale (1-4)</td></tr>
     *          <tr><td>Weight</td><td>100</td></tr>
     *          <tr><td>Document Upload</td><td>Optional</td></tr>
     *      </table>
     *      <li>User clicks on "Save New Scorecard"/li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      The new Scorecard (test_scorecard1) will be created and saved to the database.
     * </p>
     */
    public void testScenario002()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);
            HtmlPage scorecardPage = clickCreateScorecard(mainPage);
            assertTrue("Expected: Manager should have permission to create a scorecard", scorecardPage.asText().toLowerCase().indexOf("not have permission") == -1);
            
            HtmlForm form = scorecardPage.getFormByName("scorecardForm");
            form.getInputByName("scorecard.name").setValueAttribute("test_scorecard01");
            form.getInputByName("scorecard.version").setValueAttribute("1.0");
            form.getSelectByName("projectTypeName").setSelectedAttribute("Component", true);
            form.getSelectByName("projectCategoryName").setSelectedAttribute("Design", true);
            form.getSelectByName("scorecard.scorecardType.name").setSelectedAttribute("Screening", true);
            form.getInputByName("minScoreText").setValueAttribute("0");
            form.getInputByName("maxScoreText").setValueAttribute("100");
            form.getSelectByName("scorecard.scorecardStatus.name").setSelectedAttribute("Active", true);
            form.getInputByName("scorecard.allGroups[0].name").setValueAttribute("test_group01");
            form.getInputByName("scorecard.allGroups[0].weight").setValueAttribute("100");
            form.getInputByName("scorecard.allGroups[0].allSections[0].name").setValueAttribute("test_section01");
            form.getInputByName("scorecard.allGroups[0].allSections[0].weight").setValueAttribute("100");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].description").get(0)).setText("test_question01");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline01");
            form.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            form.getInputByName("scorecard.allGroups[0].allSections[0].allQuestions[0].weight").setValueAttribute("100");
            form.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            HtmlPage responsePage = (HtmlPage)form.getInputByValue("Save New Scorecard").click();
            
            assertTrue("Expected to be redirected back to the \"Component Scorecards\" listing page", responsePage.asText().toLowerCase().indexOf("filter scorecard view") != -1);
            assertTrue("Expected to find \"test_scorecard01\" in the scorecard list", responsePage.asText().toLowerCase().indexOf("test_scorecard01") != -1);
        } 
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #3 (FTC 53)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User clicks on "Create a Scorecard"</li>
     *      <li>User completes Scorecard Details using sample data below</li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Scorecard Name</td><td>test_scorecard01</td></tr>
     *          <tr><td>Version Number</td><td>1.0</td></tr>
     *          <tr><td>Project Type</td><td>Component</td></tr>
     *          <tr><td>Category</td><td>Design</td></tr>
     *          <tr><td>Type</td><td>Screening</td></tr>
     *          <tr><td>Min Score</td><td>0</td></tr>
     *          <tr><td>Max Score</td><td>100</td></tr>
     *          <tr><td>Status</td><td>Active</td></tr>
     *          <tr><td>Group</td><td>test_group01</td></tr>
     *          <tr><td>Weight (Group)</td><td>50</td></tr>
     *          <tr><td>Section</td><td>test_section01</td></tr>
     *          <tr><td>Weight (Section)</td><td>50</td></tr>
     *          <tr><td>Question Text</td><td>test_question01</td></tr>
     *          <tr><td>Question Guideline</td><td>test_questionguideline01</td></tr>
     *          <tr><td>Type</td><td>Scale (1-4)</td></tr>
     *          <tr><td>Weight</td><td>50</td></tr>
     *          <tr><td>Document Upload</td><td>Optional</td></tr>
     *      </table>
     *      <li>In the group section, click "Add New"/li>
     *      <li>Enter sample data listed below in "New Group Section"/li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Group</td><td>test_group02</td></tr>
     *          <tr><td>Weight (Group)</td><td>50</td></tr>
     *          <tr><td>Section</td><td>test_section02</td></tr>
     *          <tr><td>Weight (Section)</td><td>50</td></tr>
     *          <tr><td>Question Text</td><td>test_question02</td></tr>
     *          <tr><td>Question Guideline</td><td>test_questionguideline02</td></tr>
     *          <tr><td>Type</td><td>Scale (1-4)</td></tr>
     *          <tr><td>Weight</td><td>50</td></tr>
     *          <tr><td>Document Upload</td><td>Optional</td></tr>
     *      </table>
     *      <li>User clicks on "Save New Scorecard"/li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation error is shown indicating Scorecard Name is currently in use and an alternate name needs to be selected.
     * </p>
     */
    public void testScenario003()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);
            HtmlPage scorecardPage = clickCreateScorecard(mainPage);
            assertTrue("Expected: Manager should have permission to create a scorecard", scorecardPage.asText().toLowerCase().indexOf("not have permission") == -1);
            
            HtmlForm form = scorecardPage.getFormByName("scorecardForm");
            form.getInputByName("scorecard.name").setValueAttribute("test_scorecard01");
            form.getInputByName("scorecard.version").setValueAttribute("1.0");
            form.getSelectByName("projectTypeName").setSelectedAttribute("Component", true);
            form.getSelectByName("projectCategoryName").setSelectedAttribute("Design", true);
            form.getSelectByName("scorecard.scorecardType.name").setSelectedAttribute("Screening", true);
            form.getInputByName("minScoreText").setValueAttribute("0");
            form.getInputByName("maxScoreText").setValueAttribute("100");
            form.getSelectByName("scorecard.scorecardStatus.name").setSelectedAttribute("Active", true);
            form.getInputByName("scorecard.allGroups[0].name").setValueAttribute("test_group01");
            form.getInputByName("scorecard.allGroups[0].weight").setValueAttribute("50");
            form.getInputByName("scorecard.allGroups[0].allSections[0].name").setValueAttribute("test_section01");
            form.getInputByName("scorecard.allGroups[0].allSections[0].weight").setValueAttribute("50");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].description").get(0)).setText("test_question01");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline01");
            form.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            form.getInputByName("scorecard.allGroups[0].allSections[0].allQuestions[0].weight").setValueAttribute("50");
            form.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            ((HtmlInput)findOneHtmlElementByAttribute(form, "input", "title", "btnAddGroup[0]")).click();
            
            form.getInputByName("scorecard.allGroups[1].name").setValueAttribute("test_group02");
            form.getInputByName("scorecard.allGroups[1].weight").setValueAttribute("50");
            form.getInputByName("scorecard.allGroups[1].allSections[0].name").setValueAttribute("test_section02");
            form.getInputByName("scorecard.allGroups[1].allSections[0].weight").setValueAttribute("50");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].description").get(0)).setText("test_question02");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline02");
            form.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            form.getInputByName("scorecard.allGroups[1].allSections[0].allQuestions[0].weight").setValueAttribute("50");
            form.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            HtmlPage responsePage = (HtmlPage)form.getInputByValue("Save New Scorecard").click();
            
            assertTrue("Expected Validation error indicating Scorecard Name is currently in use and an alternate name needs to be selected", responsePage.asText().toLowerCase().indexOf("already scorecard with") != -1);
        } 
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #4 (FTC 53)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User clicks on "Create a Scorecard"</li>
     *      <li>User completes Scorecard Details using sample data below</li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Scorecard Name</td><td>test_scorecard04</td></tr>
     *          <tr><td>Version Number</td><td>1.0</td></tr>
     *          <tr><td>Project Type</td><td>Component</td></tr>
     *          <tr><td>Category</td><td>Design</td></tr>
     *          <tr><td>Type</td><td>Screening</td></tr>
     *          <tr><td>Min Score</td><td>0</td></tr>
     *          <tr><td>Max Score</td><td>100</td></tr>
     *          <tr><td>Status</td><td>Active</td></tr>
     *          <tr><td>Group</td><td>test_group01</td></tr>
     *          <tr><td>Weight (Group)</td><td>50</td></tr>
     *          <tr><td>Section</td><td>test_section01</td></tr>
     *          <tr><td>Weight (Section)</td><td>50</td></tr>
     *          <tr><td>Question Text</td><td>test_question01</td></tr>
     *          <tr><td>Question Guideline</td><td>test_questionguideline01</td></tr>
     *          <tr><td>Type</td><td>Scale (1-4)</td></tr>
     *          <tr><td>Weight</td><td>50</td></tr>
     *          <tr><td>Document Upload</td><td>Optional</td></tr>
     *      </table>
     *      <li>In the group section, click "Add New"/li>
     *      <li>Enter sample data listed below in "New Group Section"/li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Group</td><td>test_group02</td></tr>
     *          <tr><td>Weight (Group)</td><td>25</td></tr>
     *          <tr><td>Section</td><td>test_section02</td></tr>
     *          <tr><td>Weight (Section)</td><td>50</td></tr>
     *          <tr><td>Question Text</td><td>test_question02</td></tr>
     *          <tr><td>Question Guideline</td><td>test_questionguideline02</td></tr>
     *          <tr><td>Type</td><td>Scale (1-4)</td></tr>
     *          <tr><td>Weight</td><td>50</td></tr>
     *          <tr><td>Document Upload</td><td>Optional</td></tr>
     *      </table>
     *      <li>User clicks on "Save New Scorecard"/li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation error is shown indicating Group Weights DO NOT Sum to 100.
     * </p>
     */
    public void testScenario004()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);
            HtmlPage scorecardPage = clickCreateScorecard(mainPage);
            assertTrue("Expected: Manager should have permission to create a scorecard", scorecardPage.asText().toLowerCase().indexOf("not have permission") == -1);
            
            HtmlForm form = scorecardPage.getFormByName("scorecardForm");
            form.getInputByName("scorecard.name").setValueAttribute("test_scorecard04");
            form.getInputByName("scorecard.version").setValueAttribute("1.0");
            form.getSelectByName("projectTypeName").setSelectedAttribute("Component", true);
            form.getSelectByName("projectCategoryName").setSelectedAttribute("Design", true);
            form.getSelectByName("scorecard.scorecardType.name").setSelectedAttribute("Screening", true);
            form.getInputByName("minScoreText").setValueAttribute("0");
            form.getInputByName("maxScoreText").setValueAttribute("100");
            form.getSelectByName("scorecard.scorecardStatus.name").setSelectedAttribute("Active", true);
            form.getInputByName("scorecard.allGroups[0].name").setValueAttribute("test_group01");
            form.getInputByName("scorecard.allGroups[0].weight").setValueAttribute("50");
            form.getInputByName("scorecard.allGroups[0].allSections[0].name").setValueAttribute("test_section01");
            form.getInputByName("scorecard.allGroups[0].allSections[0].weight").setValueAttribute("50");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].description").get(0)).setText("test_question01");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline01");
            form.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            form.getInputByName("scorecard.allGroups[0].allSections[0].allQuestions[0].weight").setValueAttribute("50");
            form.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            ((HtmlInput)findOneHtmlElementByAttribute(form, "input", "title", "btnAddGroup[0]")).click();
            
            form.getInputByName("scorecard.allGroups[1].name").setValueAttribute("test_group02");
            form.getInputByName("scorecard.allGroups[1].weight").setValueAttribute("25");
            form.getInputByName("scorecard.allGroups[1].allSections[0].name").setValueAttribute("test_section02");
            form.getInputByName("scorecard.allGroups[1].allSections[0].weight").setValueAttribute("50");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].description").get(0)).setText("test_question02");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline02");
            form.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            form.getInputByName("scorecard.allGroups[1].allSections[0].allQuestions[0].weight").setValueAttribute("50");
            form.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            HtmlPage responsePage = (HtmlPage)form.getInputByValue("Save New Scorecard").click();
            
            assertTrue("Expected Validation error indicating Group Weights DO NOT Sum to 100", responsePage.asText().toLowerCase().indexOf("weights of groups must sum up to 100") != -1);
        } 
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #5 (FTC 53)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User clicks on "Create a Scorecard"</li>
     *      <li>User completes Scorecard Details using sample data below</li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Scorecard Name</td><td>test_scorecard05</td></tr>
     *          <tr><td>Version Number</td><td>1.0</td></tr>
     *          <tr><td>Project Type</td><td>Component</td></tr>
     *          <tr><td>Category</td><td>Design</td></tr>
     *          <tr><td>Type</td><td>Screening</td></tr>
     *          <tr><td>Min Score</td><td>0</td></tr>
     *          <tr><td>Max Score</td><td>100</td></tr>
     *          <tr><td>Status</td><td>Active</td></tr>
     *          <tr><td>Group</td><td>test_group01</td></tr>
     *          <tr><td>Weight (Group)</td><td>50</td></tr>
     *          <tr><td>Section</td><td>test_section01</td></tr>
     *          <tr><td>Weight (Section)</td><td>50</td></tr>
     *          <tr><td>Question Text</td><td>test_question01</td></tr>
     *          <tr><td>Question Guideline</td><td>test_questionguideline01</td></tr>
     *          <tr><td>Type</td><td>Scale (1-4)</td></tr>
     *          <tr><td>Weight</td><td>50</td></tr>
     *          <tr><td>Document Upload</td><td>Optional</td></tr>
     *      </table>
     *      <li>In the group section, click "Add New"/li>
     *      <li>Enter sample data listed below in "New Group Section"/li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Group</td><td>test_group02</td></tr>
     *          <tr><td>Weight (Group)</td><td>50</td></tr>
     *          <tr><td>Section</td><td>test_section02</td></tr>
     *          <tr><td>Weight (Section)</td><td>25</td></tr>
     *          <tr><td>Question Text</td><td>test_question02</td></tr>
     *          <tr><td>Question Guideline</td><td>test_questionguideline02</td></tr>
     *          <tr><td>Type</td><td>Scale (1-4)</td></tr>
     *          <tr><td>Weight</td><td>50</td></tr>
     *          <tr><td>Document Upload</td><td>Optional</td></tr>
     *      </table>
     *      <li>User clicks on "Save New Scorecard"/li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation error is shown indicating Section Weights DO NOT Sum to 100.
     * </p>
     */
    public void testScenario005()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);
            HtmlPage scorecardPage = clickCreateScorecard(mainPage);
            assertTrue("Expected: Manager should have permission to create a scorecard", scorecardPage.asText().toLowerCase().indexOf("not have permission") == -1);
            
            HtmlForm form = scorecardPage.getFormByName("scorecardForm");
            form.getInputByName("scorecard.name").setValueAttribute("test_scorecard05");
            form.getInputByName("scorecard.version").setValueAttribute("1.0");
            form.getSelectByName("projectTypeName").setSelectedAttribute("Component", true);
            form.getSelectByName("projectCategoryName").setSelectedAttribute("Design", true);
            form.getSelectByName("scorecard.scorecardType.name").setSelectedAttribute("Screening", true);
            form.getInputByName("minScoreText").setValueAttribute("0");
            form.getInputByName("maxScoreText").setValueAttribute("100");
            form.getSelectByName("scorecard.scorecardStatus.name").setSelectedAttribute("Active", true);
            form.getInputByName("scorecard.allGroups[0].name").setValueAttribute("test_group01");
            form.getInputByName("scorecard.allGroups[0].weight").setValueAttribute("50");
            form.getInputByName("scorecard.allGroups[0].allSections[0].name").setValueAttribute("test_section01");
            form.getInputByName("scorecard.allGroups[0].allSections[0].weight").setValueAttribute("50");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].description").get(0)).setText("test_question01");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline01");
            form.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            form.getInputByName("scorecard.allGroups[0].allSections[0].allQuestions[0].weight").setValueAttribute("50");
            form.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            ((HtmlInput)findOneHtmlElementByAttribute(form, "input", "title", "btnAddGroup[0]")).click();
            
            form.getInputByName("scorecard.allGroups[1].name").setValueAttribute("test_group02");
            form.getInputByName("scorecard.allGroups[1].weight").setValueAttribute("50");
            form.getInputByName("scorecard.allGroups[1].allSections[0].name").setValueAttribute("test_section02");
            form.getInputByName("scorecard.allGroups[1].allSections[0].weight").setValueAttribute("25");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].description").get(0)).setText("test_question02");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline02");
            form.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            form.getInputByName("scorecard.allGroups[1].allSections[0].allQuestions[0].weight").setValueAttribute("50");
            form.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            HtmlPage responsePage = (HtmlPage)form.getInputByValue("Save New Scorecard").click();
            
            assertTrue("Expected Validation error indicating Section Weights DO NOT Sum to 100", responsePage.asText().toLowerCase().indexOf("weights of sections must sum up to 100") != -1);
        } 
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #6 (FTC 53)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User clicks on "Create a Scorecard"</li>
     *      <li>User completes Scorecard Details using sample data below</li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Scorecard Name</td><td>test_scorecard06</td></tr>
     *          <tr><td>Version Number</td><td>1.0</td></tr>
     *          <tr><td>Project Type</td><td>Component</td></tr>
     *          <tr><td>Category</td><td>Design</td></tr>
     *          <tr><td>Type</td><td>Screening</td></tr>
     *          <tr><td>Min Score</td><td>0</td></tr>
     *          <tr><td>Max Score</td><td>100</td></tr>
     *          <tr><td>Status</td><td>Active</td></tr>
     *          <tr><td>Group</td><td>test_group01</td></tr>
     *          <tr><td>Weight (Group)</td><td>50</td></tr>
     *          <tr><td>Section</td><td>test_section01</td></tr>
     *          <tr><td>Weight (Section)</td><td>50</td></tr>
     *          <tr><td>Question Text</td><td>test_question01</td></tr>
     *          <tr><td>Question Guideline</td><td>test_questionguideline01</td></tr>
     *          <tr><td>Type</td><td>Scale (1-4)</td></tr>
     *          <tr><td>Weight</td><td>50</td></tr>
     *          <tr><td>Document Upload</td><td>Optional</td></tr>
     *      </table>
     *      <li>In the group section, click "Add New"/li>
     *      <li>Enter sample data listed below in "New Group Section"/li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Group</td><td>test_group02</td></tr>
     *          <tr><td>Weight (Group)</td><td>50</td></tr>
     *          <tr><td>Section</td><td>test_section02</td></tr>
     *          <tr><td>Weight (Section)</td><td>50</td></tr>
     *          <tr><td>Question Text</td><td>test_question02</td></tr>
     *          <tr><td>Question Guideline</td><td>test_questionguideline02</td></tr>
     *          <tr><td>Type</td><td>Scale (1-4)</td></tr>
     *          <tr><td>Weight</td><td>25</td></tr>
     *          <tr><td>Document Upload</td><td>Optional</td></tr>
     *      </table>
     *      <li>User clicks on "Save New Scorecard"/li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation error is shown indicating Question Weights DO NOT Sum to 100.
     * </p>
     */
    public void testScenario006()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);
            HtmlPage scorecardPage = clickCreateScorecard(mainPage);
            assertTrue("Expected: Manager should have permission to create a scorecard", scorecardPage.asText().toLowerCase().indexOf("not have permission") == -1);
            
            HtmlForm form = scorecardPage.getFormByName("scorecardForm");
            form.getInputByName("scorecard.name").setValueAttribute("test_scorecard06");
            form.getInputByName("scorecard.version").setValueAttribute("1.0");
            form.getSelectByName("projectTypeName").setSelectedAttribute("Component", true);
            form.getSelectByName("projectCategoryName").setSelectedAttribute("Design", true);
            form.getSelectByName("scorecard.scorecardType.name").setSelectedAttribute("Screening", true);
            form.getInputByName("minScoreText").setValueAttribute("0");
            form.getInputByName("maxScoreText").setValueAttribute("100");
            form.getSelectByName("scorecard.scorecardStatus.name").setSelectedAttribute("Active", true);
            form.getInputByName("scorecard.allGroups[0].name").setValueAttribute("test_group01");
            form.getInputByName("scorecard.allGroups[0].weight").setValueAttribute("50");
            form.getInputByName("scorecard.allGroups[0].allSections[0].name").setValueAttribute("test_section01");
            form.getInputByName("scorecard.allGroups[0].allSections[0].weight").setValueAttribute("50");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].description").get(0)).setText("test_question01");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline01");
            form.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            form.getInputByName("scorecard.allGroups[0].allSections[0].allQuestions[0].weight").setValueAttribute("50");
            form.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            ((HtmlInput)findOneHtmlElementByAttribute(form, "input", "title", "btnAddGroup[0]")).click();
            
            form.getInputByName("scorecard.allGroups[1].name").setValueAttribute("test_group02");
            form.getInputByName("scorecard.allGroups[1].weight").setValueAttribute("50");
            form.getInputByName("scorecard.allGroups[1].allSections[0].name").setValueAttribute("test_section02");
            form.getInputByName("scorecard.allGroups[1].allSections[0].weight").setValueAttribute("50");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].description").get(0)).setText("test_question02");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline02");
            form.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            form.getInputByName("scorecard.allGroups[1].allSections[0].allQuestions[0].weight").setValueAttribute("25");
            form.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            HtmlPage responsePage = (HtmlPage)form.getInputByValue("Save New Scorecard").click();
            
            assertTrue("Expected Validation error indicating Question Weights DO NOT Sum to 100", responsePage.asText().toLowerCase().indexOf("weights of questions must sum up to 100") != -1);
        } 
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #7 (FTC 53)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User clicks on "Create a Scorecard"</li>
     *      <li>User completes Scorecard Details using sample data below</li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Scorecard Name</td><td>test_scorecard07</td></tr>
     *          <tr><td>Version Number</td><td>1.0</td></tr>
     *          <tr><td>Project Type</td><td></td></tr>
     *          <tr><td>Category</td><td>Design</td></tr>
     *          <tr><td>Type</td><td></td></tr>
     *          <tr><td>Min Score</td><td>0</td></tr>
     *          <tr><td>Max Score</td><td>100</td></tr>
     *          <tr><td>Status</td><td>Active</td></tr>
     *          <tr><td>Group</td><td>test_group01</td></tr>
     *          <tr><td>Weight (Group)</td><td></td></tr>
     *          <tr><td>Section</td><td>test_section01</td></tr>
     *          <tr><td>Weight (Section)</td><td>50</td></tr>
     *          <tr><td>Question Text</td><td></td></tr>
     *          <tr><td>Question Guideline</td><td>test_questionguideline01</td></tr>
     *          <tr><td>Type</td><td>Scale (1-4)</td></tr>
     *          <tr><td>Weight</td><td>50</td></tr>
     *          <tr><td>Document Upload</td><td>Optional</td></tr>
     *      </table>
     *      <li>In the group section, click "Add New"/li>
     *      <li>Enter sample data listed below in "New Group Section"/li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Group</td><td>test_group02</td></tr>
     *          <tr><td>Weight (Group)</td><td>50</td></tr>
     *          <tr><td>Section</td><td>test_section02</td></tr>
     *          <tr><td>Weight (Section)</td><td>50</td></tr>
     *          <tr><td>Question Text</td><td>test_question02</td></tr>
     *          <tr><td>Question Guideline</td><td>test_questionguideline02</td></tr>
     *          <tr><td>Type</td><td>Scale (1-4)</td></tr>
     *          <tr><td>Weight</td><td></td></tr>
     *          <tr><td>Document Upload</td><td>Optional</td></tr>
     *      </table>
     *      <li>User clicks on "Save New Scorecard"/li>
     * </ol>
     * <p><strong>NOTE: Project Type and Type fields cannot be blanked since they are drop-down lists</strong></p>
     * <p><strong>NOTE: This implementation tests only question description</strong></p>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation error is shown indicating required fields are missing.
     * </p>
     */
    public void testScenario007()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);
            HtmlPage scorecardPage = clickCreateScorecard(mainPage);
            assertTrue("Expected: Manager should have permission to create a scorecard", scorecardPage.asText().toLowerCase().indexOf("not have permission") == -1);
            
            HtmlForm form = scorecardPage.getFormByName("scorecardForm");
            form.getInputByName("scorecard.name").setValueAttribute("test_scorecard07");
            form.getInputByName("scorecard.version").setValueAttribute("1.0");
            form.getSelectByName("projectTypeName").setSelectedAttribute("Component", true);
            form.getSelectByName("projectCategoryName").setSelectedAttribute("Design", true);
            form.getSelectByName("scorecard.scorecardType.name").setSelectedAttribute("Screening", true);
            form.getInputByName("minScoreText").setValueAttribute("0");
            form.getInputByName("maxScoreText").setValueAttribute("100");
            form.getSelectByName("scorecard.scorecardStatus.name").setSelectedAttribute("Active", true);
            form.getInputByName("scorecard.allGroups[0].name").setValueAttribute("test_group01");
            form.getInputByName("scorecard.allGroups[0].weight").setValueAttribute("50");
            form.getInputByName("scorecard.allGroups[0].allSections[0].name").setValueAttribute("test_section01");
            form.getInputByName("scorecard.allGroups[0].allSections[0].weight").setValueAttribute("50");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].description").get(0)).setText("");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline01");
            form.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            form.getInputByName("scorecard.allGroups[0].allSections[0].allQuestions[0].weight").setValueAttribute("50");
            form.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            ((HtmlInput)findOneHtmlElementByAttribute(form, "input", "title", "btnAddGroup[0]")).click();
            
            form.getInputByName("scorecard.allGroups[1].name").setValueAttribute("test_group02");
            form.getInputByName("scorecard.allGroups[1].weight").setValueAttribute("50");
            form.getInputByName("scorecard.allGroups[1].allSections[0].name").setValueAttribute("test_section02");
            form.getInputByName("scorecard.allGroups[1].allSections[0].weight").setValueAttribute("50");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].description").get(0)).setText("test_question02");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline02");
            form.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            form.getInputByName("scorecard.allGroups[1].allSections[0].allQuestions[0].weight").setValueAttribute("");
            form.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            HtmlPage responsePage = (HtmlPage)form.getInputByValue("Save New Scorecard").click();
            
            assertTrue("Expected Validation error indicating required fields are missing", responsePage.asText().toLowerCase().indexOf("question description is required") != -1);
        } 
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #8 (FTC 53)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User clicks on "Create a Scorecard"</li>
     *      <li>User completes Scorecard Details using sample data below</li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Scorecard Name</td><td>test_scorecard08</td></tr>
     *          <tr><td>Version Number</td><td>1.X</td></tr>
     *          <tr><td>Project Type</td><td>Component</td></tr>
     *          <tr><td>Category</td><td>Design</td></tr>
     *          <tr><td>Type</td><td>Screening</td></tr>
     *          <tr><td>Min Score</td><td>0</td></tr>
     *          <tr><td>Max Score</td><td>100</td></tr>
     *          <tr><td>Status</td><td>Active</td></tr>
     *          <tr><td>Group</td><td>test_group01</td></tr>
     *          <tr><td>Weight (Group)</td><td>50</td></tr>
     *          <tr><td>Section</td><td>test_section01</td></tr>
     *          <tr><td>Weight (Section)</td><td>50</td></tr>
     *          <tr><td>Question Text</td><td>test_question01</td></tr>
     *          <tr><td>Question Guideline</td><td>test_questionguideline01</td></tr>
     *          <tr><td>Type</td><td>Scale (1-4)</td></tr>
     *          <tr><td>Weight</td><td>50</td></tr>
     *          <tr><td>Document Upload</td><td>Optional</td></tr>
     *      </table>
     *      <li>In the group section, click "Add New"/li>
     *      <li>Enter sample data listed below in "New Group Section"/li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Group</td><td>test_group02</td></tr>
     *          <tr><td>Weight (Group)</td><td>50</td></tr>
     *          <tr><td>Section</td><td>test_section02</td></tr>
     *          <tr><td>Weight (Section)</td><td>50</td></tr>
     *          <tr><td>Question Text</td><td>test_question02</td></tr>
     *          <tr><td>Question Guideline</td><td>test_questionguideline02</td></tr>
     *          <tr><td>Type</td><td>Scale (1-4)</td></tr>
     *          <tr><td>Weight</td><td>50</td></tr>
     *          <tr><td>Document Upload</td><td>Optional</td></tr>
     *      </table>
     *      <li>User clicks on "Save New Scorecard"/li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation error is shown indicating Scorecard Version Number is INVALID and a VALID Number needs to be selected before proceeding.
     * </p>
     */
    public void testScenario008()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);
            HtmlPage scorecardPage = clickCreateScorecard(mainPage);
            assertTrue("Expected: Manager should have permission to create a scorecard", scorecardPage.asText().toLowerCase().indexOf("not have permission") == -1);
            
            HtmlForm form = scorecardPage.getFormByName("scorecardForm");
            form.getInputByName("scorecard.name").setValueAttribute("test_scorecard08");
            form.getInputByName("scorecard.version").setValueAttribute("1.X");
            form.getSelectByName("projectTypeName").setSelectedAttribute("Component", true);
            form.getSelectByName("projectCategoryName").setSelectedAttribute("Design", true);
            form.getSelectByName("scorecard.scorecardType.name").setSelectedAttribute("Screening", true);
            form.getInputByName("minScoreText").setValueAttribute("0");
            form.getInputByName("maxScoreText").setValueAttribute("100");
            form.getSelectByName("scorecard.scorecardStatus.name").setSelectedAttribute("Active", true);
            form.getInputByName("scorecard.allGroups[0].name").setValueAttribute("test_group01");
            form.getInputByName("scorecard.allGroups[0].weight").setValueAttribute("50");
            form.getInputByName("scorecard.allGroups[0].allSections[0].name").setValueAttribute("test_section01");
            form.getInputByName("scorecard.allGroups[0].allSections[0].weight").setValueAttribute("50");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].description").get(0)).setText("test_question01");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline01");
            form.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            form.getInputByName("scorecard.allGroups[0].allSections[0].allQuestions[0].weight").setValueAttribute("50");
            form.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            ((HtmlInput)findOneHtmlElementByAttribute(form, "input", "title", "btnAddGroup[0]")).click();
            
            form.getInputByName("scorecard.allGroups[1].name").setValueAttribute("test_group02");
            form.getInputByName("scorecard.allGroups[1].weight").setValueAttribute("50");
            form.getInputByName("scorecard.allGroups[1].allSections[0].name").setValueAttribute("test_section02");
            form.getInputByName("scorecard.allGroups[1].allSections[0].weight").setValueAttribute("50");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].description").get(0)).setText("test_question02");
            ((HtmlTextArea)form.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline02");
            form.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            form.getInputByName("scorecard.allGroups[1].allSections[0].allQuestions[0].weight").setValueAttribute("25");
            form.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            HtmlPage responsePage = (HtmlPage)form.getInputByValue("Save New Scorecard").click();
            
            assertTrue("Expected Validation error indicating Scorecard Version Number is invalid.", responsePage.asText().toLowerCase().indexOf("version is in invalid format") != -1);
        } 
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }
}
