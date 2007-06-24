/* Copyright (C) 2007 TopCoder Inc., All Rights Reserved. */

package com.cronos.onlinereview.tests.scorecard;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.cronos.onlinereview.AbstractTestCase;
import com.cronos.onlinereview.Configuration;
import com.cronos.onlinereview.tests.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EditScorecardTest extends AbstractTestCase
{
    /**
     * Test Scenario #9 (FTC 55)
     * 
     * <ol>
     *      <li>User selects scorecard to edit</li>
     *      <li>User clicks on "Edit Scorecard"</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation Error indicating user does not have permission to edit a scorecard.
     * </p>
     */
    public void testScenario009()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.OBSERVER);
            assertNotLoginPage(mainPage);
            
            HtmlAnchor anchor = getFirstAnchorByText(mainPage, "test_scorecard01 v 1.0");
            assertNotNull("Expected to find scorecard \"test_scorecard01 v 1.0\", but it was missing.", anchor);
            
            HtmlPage detailPage = (HtmlPage)anchor.click();
            HtmlForm form = detailPage.getFormByName("scorecardForm");
            HtmlInput editButton = getInputByValue(form, "Edit");
            assertNotNull("Expected to find button \"Edit\", but it was missing", editButton);
            HtmlPage editPage = (HtmlPage)editButton.click();

            assertTrue(editPage.asText().indexOf("permission to edit") != -1);
        }
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #10 (FTC 55)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User selects scorecard to edit</li>
     *      <li>User clicks on "Edit Scorecard"</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation Error is show indicating user scorecard is currently linked to a project and cannot be edited.
     * </p>
     */
    public void testScenario010()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);
            
            HtmlAnchor anchor = getFirstAnchorByText(mainPage, "test_scorecard01 v 1.0");
            assertNotNull("Expected to find scorecard \"test_scorecard01 v 1.0\", but it was missing.", anchor);
            
            HtmlPage detailPage = (HtmlPage)anchor.click();
            HtmlForm form = detailPage.getFormByName("scorecardForm");
            HtmlInput editButton = getInputByValue(form, "Edit");
            assertNotNull("Expected to find button \"Edit\", but it was missing", editButton);
            HtmlPage editPage = (HtmlPage)editButton.click();
            
            assertTrue(editPage.asText().indexOf("cannot be edited") != -1);
        }
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #11 (FTC 55)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User selects scorecard to edit</li>
     *      <li>User clicks on "Edit Scorecard"</li>
     *      <li>User attempts to edit scorecard name and version number</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Scorecard name and version number are in read-only format and cannot be edited.
     * </p>
     */
    public void testScenario011()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);
            
            HtmlAnchor anchor = getFirstAnchorByText(mainPage, "test_scorecard01 v 1.0");
            assertNotNull("Expected to find scorecard \"test_scorecard01 v 1.0\", but it was missing.", anchor);
            
            HtmlPage detailPage = (HtmlPage)anchor.click();
            HtmlForm form = detailPage.getFormByName("scorecardForm");
            HtmlInput editButton = getInputByValue(form, "Edit");
            assertNotNull("Expected to find button \"Edit\", but it was missing", editButton);
            HtmlPage editPage = (HtmlPage)editButton.click();
            
            HtmlForm scorecardForm = editPage.getFormByName("scorecardForm");
            assertTrue("Expected scorecard name to be read-only, but it isn't", scorecardForm.getInputByName("scorecard.name").isDisabled());
            assertTrue("Expected scorecard version to be read-only, but it isn't", scorecardForm.getInputByName("scorecard.version").isDisabled());
        }
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #12 (FTC 55-57,59,60)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User selects scorecard to edit</li>
     *      <li>User clicks on "Edit Scorecard"</li>
     *      <li>User changes scorecard using sample data below</li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Project Type</td><td>Component</td></tr>
     *          <tr><td>Category</td><td>Design</td></tr>
     *          <tr><td>Type</td><td>Screening</td></tr>
     *          <tr><td>Min Score</td><td>0</td></tr>
     *          <tr><td>Max Score</td><td>100</td></tr>
     *          <tr><td>Status</td><td>Active</td></tr>
     *          <tr><td>Group</td><td>test_group01_edited</td></tr>
     *          <tr><td>Weight (Group)</td><td>100</td></tr>
     *          <tr><td>Section</td><td>test_section01_edited</td></tr>
     *          <tr><td>Weight (Section)</td><td>100</td></tr>
     *          <tr><td>Question Text</td><td>test_question01_edited</td></tr>
     *          <tr><td>Question Guideline</td><td>test_questionguideline01</td></tr>
     *          <tr><td>Type</td><td>Scale (1-10)</td></tr>
     *          <tr><td>Weight</td><td>100</td></tr>
     *          <tr><td>Document Upload</td><td>Optional</td></tr>
     *      </table>
     *      <li>User clicks on "Save Changes"</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Scorecard name and version number are in read-only format and cannot be edited.
     * </p>
     */
    public void testScenario012()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);
            
            HtmlAnchor anchor = getFirstAnchorByText(mainPage, "test_scorecard01 v 1.0");
            assertNotNull("Expected to find scorecard \"test_scorecard01 v 1.0\", but it was missing.", anchor);
            
            HtmlPage detailPage = (HtmlPage)anchor.click();
            HtmlForm form = detailPage.getFormByName("scorecardForm");
            HtmlInput editButton = getInputByValue(form, "Edit");
            assertNotNull("Expected to find button \"Edit\", but it was missing", editButton);
            HtmlPage editPage = (HtmlPage)editButton.click();
            
            HtmlForm editForm = editPage.getFormByName("scorecardForm");
            editForm.getSelectByName("projectTypeName").setSelectedAttribute("Component", true);
            editForm.getSelectByName("projectCategoryName").setSelectedAttribute("Design", true);
            editForm.getSelectByName("scorecard.scorecardType.name").setSelectedAttribute("Screening", true);
            editForm.getInputByName("minScoreText").setValueAttribute("0");
            editForm.getInputByName("maxScoreText").setValueAttribute("100");
            editForm.getSelectByName("scorecard.scorecardStatus.name").setSelectedAttribute("Active", true);
            editForm.getInputByName("scorecard.allGroups[0].name").setValueAttribute("test_group01_edited");
            editForm.getInputByName("scorecard.allGroups[0].weight").setValueAttribute("100");
            editForm.getInputByName("scorecard.allGroups[0].allSections[0].name").setValueAttribute("test_section01_edited");
            editForm.getInputByName("scorecard.allGroups[0].allSections[0].weight").setValueAttribute("100");
            ((HtmlTextArea)editForm.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].description").get(0)).setText("test_question01_edited");
            ((HtmlTextArea)editForm.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline01");
            editForm.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-10)", true);
            editForm.getInputByName("scorecard.allGroups[0].allSections[0].allQuestions[0].weight").setValueAttribute("100");
            editForm.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            mainPage = (HtmlPage)editForm.getInputByValue("Save Changes").click();
            anchor = mainPage.getFirstAnchorByText("test_scorecard01 v 1.1");
            assertNotNull("Expected to find updated scorecard \"test_scorecard01 v 1.1\", but it was missing.", anchor);
        }
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #13 (FTC 58)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User selects scorecard to edit</li>
     *      <li>User clicks on "Edit Scorecard"</li>
     *      <li>User changes scorecard using sample data below</li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Project Type</td><td></td></tr>
     *          <tr><td>Category</td><td>Design</td></tr>
     *          <tr><td>Type</td><td></td></tr>
     *          <tr><td>Min Score</td><td>0</td></tr>
     *          <tr><td>Max Score</td><td>100</td></tr>
     *          <tr><td>Status</td><td>Active</td></tr>
     *          <tr><td>Group</td><td>test_group01</td></tr>
     *          <tr><td>Weight (Group)</td><td></td></tr>
     *          <tr><td>Section</td><td>test_section01</td></tr>
     *          <tr><td>Weight (Section)</td><td>100</td></tr>
     *          <tr><td>Question Text</td><td></td></tr>
     *          <tr><td>Question Guideline</td><td>test_questionguideline01</td></tr>
     *          <tr><td>Type</td><td>Scale (1-10)</td></tr>
     *          <tr><td>Weight</td><td>100</td></tr>
     *          <tr><td>Document Upload</td><td>Optional</td></tr>
     *      </table>
     *      <li>For the Question Group, enter the data below</li>
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
     *      <li>User clicks on "Save Changes"/li>
     * </ol>
     * <p><strong>NOTE: Project Type and Type fields cannot be blanked since they are drop-down lists</strong></p>
     * <p><strong>NOTE: This implementation tests only question description</strong></p>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation error is shown indicating Required Fields are missing.
     * </p>
     */
    public void testScenario013()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);
            
            HtmlAnchor anchor = getFirstAnchorByText(mainPage, "test_scorecard01 v 1.1");
            assertNotNull("Expected to find scorecard \"test_scorecard01 v 1.1\", but it was missing.  Did the previous scenario fail?", anchor);
            
            HtmlPage detailPage = (HtmlPage)anchor.click();
            HtmlForm form = detailPage.getFormByName("scorecardForm");
            HtmlInput editButton = getInputByValue(form, "Edit");
            assertNotNull("Expected to find button \"Edit\", but it was missing", editButton);
            HtmlPage editPage = (HtmlPage)editButton.click();
            
            HtmlForm editForm = editPage.getFormByName("scorecardForm");
            editForm.getSelectByName("projectTypeName").setSelectedAttribute("Component", true);
            editForm.getSelectByName("projectCategoryName").setSelectedAttribute("Design", true);
            editForm.getSelectByName("scorecard.scorecardType.name").setSelectedAttribute("Screening", true);
            editForm.getInputByName("minScoreText").setValueAttribute("0");
            editForm.getInputByName("maxScoreText").setValueAttribute("100");
            editForm.getSelectByName("scorecard.scorecardStatus.name").setSelectedAttribute("Active", true);
            editForm.getInputByName("scorecard.allGroups[0].name").setValueAttribute("test_group01");
            editForm.getInputByName("scorecard.allGroups[0].weight").setValueAttribute("");
            editForm.getInputByName("scorecard.allGroups[0].allSections[0].name").setValueAttribute("test_section01");
            editForm.getInputByName("scorecard.allGroups[0].allSections[0].weight").setValueAttribute("50");
            ((HtmlTextArea)editForm.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].description").get(0)).setText("");
            ((HtmlTextArea)editForm.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline01");
            editForm.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            editForm.getInputByName("scorecard.allGroups[0].allSections[0].allQuestions[0].weight").setValueAttribute("50");
            editForm.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            editForm.getInputByName("scorecard.allGroups[1].name").setValueAttribute("test_group02");
            editForm.getInputByName("scorecard.allGroups[1].weight").setValueAttribute("50");
            editForm.getInputByName("scorecard.allGroups[1].allSections[0].name").setValueAttribute("test_section02");
            editForm.getInputByName("scorecard.allGroups[1].allSections[0].weight").setValueAttribute("50");
            ((HtmlTextArea)editForm.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].description").get(0)).setText("test_question02");
            ((HtmlTextArea)editForm.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline02");
            editForm.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            editForm.getInputByName("scorecard.allGroups[1].allSections[0].allQuestions[0].weight").setValueAttribute("");
            editForm.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            HtmlPage responsePage = (HtmlPage)editForm.getInputByValue("Save Changes").click();
            
            assertTrue("Expected Validation error indicating required fields are missing", responsePage.asText().toLowerCase().indexOf("question description is required") != -1);
        } 
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #14 (FTC 58)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User selects scorecard to edit</li>
     *      <li>User clicks on "Edit Scorecard"</li>
     *      <li>User changes scorecard using sample data below</li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Project Type</td>Component<td></td></tr>
     *          <tr><td>Category</td><td>Design</td></tr>
     *          <tr><td>Type</td><td>Screening</td></tr>
     *          <tr><td>Min Score</td><td>0</td></tr>
     *          <tr><td>Max Score</td><td>100</td></tr>
     *          <tr><td>Status</td><td>Active</td></tr>
     *          <tr><td>Group</td><td>test_group01</td></tr>
     *          <tr><td>Weight (Group)</td><td>50</td></tr>
     *          <tr><td>Section</td><td>test_section01</td></tr>
     *          <tr><td>Weight (Section)</td><td>50td></tr>
     *          <tr><td>Question Text</td><td>test_question01</td></tr>
     *          <tr><td>Question Guideline</td><td>test_questionguideline01</td></tr>
     *          <tr><td>Type</td><td>Scale (1-4)</td></tr>
     *          <tr><td>Weight</td><td>50</td></tr>
     *          <tr><td>Document Upload</td><td>Optional</td></tr>
     *      </table>
     *      <li>For the Question Group, enter the data below</li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Group</td><td>test_group02</td></tr>
     *          <tr><td>Weight (Group)</td><td>25</td></tr>
     *          <tr><td>Section</td><td>test_section02</td></tr>
     *          <tr><td>Weight (Section)</td><td>25</td></tr>
     *          <tr><td>Question Text</td><td>test_question02</td></tr>
     *          <tr><td>Question Guideline</td><td>test_questionguideline02</td></tr>
     *          <tr><td>Type</td><td>Scale (1-4)</td></tr>
     *          <tr><td>Weight</td><td>25</td></tr>
     *          <tr><td>Document Upload</td><td>Optional</td></tr>
     *      </table>
     *      <li>User clicks on "Save Changes"/li>
     * </ol>
     * <p><strong>NOTE: Project Type and Type fields cannot be blanked since they are drop-down lists</strong></p>
     * <p><strong>NOTE: This implementation tests only question description</strong></p>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation error is shown indicating WEIGHTS MUST SUM to 100.
     * </p>
     */
    public void testScenario014()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);
            
            HtmlAnchor anchor = getFirstAnchorByText(mainPage, "test_scorecard01 v 1.1");
            assertNotNull("Expected to find scorecard \"test_scorecard01 v 1.1\", but it was missing.  Did the previous scenario fail?", anchor);
            
            HtmlPage detailPage = (HtmlPage)anchor.click();
            HtmlForm form = detailPage.getFormByName("scorecardForm");
            HtmlInput editButton = getInputByValue(form, "Edit");
            assertNotNull("Expected to find button \"Edit\", but it was missing", editButton);
            HtmlPage editPage = (HtmlPage)editButton.click();
            
            HtmlForm editForm = editPage.getFormByName("scorecardForm");
            editForm.getSelectByName("projectTypeName").setSelectedAttribute("Component", true);
            editForm.getSelectByName("projectCategoryName").setSelectedAttribute("Design", true);
            editForm.getSelectByName("scorecard.scorecardType.name").setSelectedAttribute("Screening", true);
            editForm.getInputByName("minScoreText").setValueAttribute("0");
            editForm.getInputByName("maxScoreText").setValueAttribute("100");
            editForm.getSelectByName("scorecard.scorecardStatus.name").setSelectedAttribute("Active", true);
            editForm.getInputByName("scorecard.allGroups[0].name").setValueAttribute("test_group01");
            editForm.getInputByName("scorecard.allGroups[0].weight").setValueAttribute("50");
            editForm.getInputByName("scorecard.allGroups[0].allSections[0].name").setValueAttribute("test_section01");
            editForm.getInputByName("scorecard.allGroups[0].allSections[0].weight").setValueAttribute("50");
            ((HtmlTextArea)editForm.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].description").get(0)).setText("test_question01");
            ((HtmlTextArea)editForm.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline01");
            editForm.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            editForm.getInputByName("scorecard.allGroups[0].allSections[0].allQuestions[0].weight").setValueAttribute("50");
            editForm.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            editForm.getInputByName("scorecard.allGroups[1].name").setValueAttribute("test_group02");
            editForm.getInputByName("scorecard.allGroups[1].weight").setValueAttribute("25");
            editForm.getInputByName("scorecard.allGroups[1].allSections[0].name").setValueAttribute("test_section02");
            editForm.getInputByName("scorecard.allGroups[1].allSections[0].weight").setValueAttribute("25");
            ((HtmlTextArea)editForm.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].description").get(0)).setText("test_question02");
            ((HtmlTextArea)editForm.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline02");
            editForm.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            editForm.getInputByName("scorecard.allGroups[1].allSections[0].allQuestions[0].weight").setValueAttribute("25");
            editForm.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            HtmlPage responsePage = (HtmlPage)editForm.getInputByValue("Save Changes").click();
            
            assertTrue("Expected Validation error is shown indicating WEIGHTS MUST SUM to 100.", responsePage.asText().toLowerCase().indexOf("must sum up to 100") != -1);
        } 
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #15 (FTC 61)
     * 
     * <ol>
     *      <li>User navigates to scorecard list</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation Error indicating user does not have permission to view scorecard list.
     * </p>
     */
    public void testScenario015()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.OBSERVER);
            assertNotLoginPage(mainPage);

            assertTrue("Expected Validation error indicatiing user does not have permission to view scorecard list.", mainPage.asText().indexOf("permission to view") != -1);
        }
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #16 (FTC 63)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User navigates to scorecard list</li>
     *      <li>User changes status of first 3 scorecards from Active to Inactive</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Scorecard status is changed from active to inactive.
     * </p>
     */
    public void testScenario016()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);
            
            List checkboxes = findHtmlElementsByAttribute(mainPage.getDocumentElement(), "input", "type", "checkbox");
            
            Iterator iter = checkboxes.iterator();
            List ids = new ArrayList();
            
            while(iter.hasNext())
            {
                HtmlInput checkbox = (HtmlInput)iter.next();
                
                if(checkbox.isChecked())
                {
                    ids.add(checkbox.getId());
                    checkbox.click();
                    
                    if(ids.size() == 3)
                        break;
                }
            }
            
            if(ids.isEmpty())
                fail("Could not find any active scorecards to inactivate");
            
            try
            {
                Thread.sleep(5000L);
            }
            catch (InterruptedException x)
            {
                // Ignore
            }
            
            int failed = 0;
            
            iter = ids.iterator();
            while(iter.hasNext())
                if(((HtmlInput)mainPage.getHtmlElementById((String)iter.next())).isChecked())
                    ++failed;
            
            assertEquals("Some scorecards failed to change to inactive status", 0, failed);
        }
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #17 (FTC 63)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User navigates to scorecard list</li>
     *      <li>User changes status of first 3 scorecards from Inactive to Active</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Scorecard status is changed from inactive to active.
     * </p>
     */
    public void testScenario017()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);
            
            List checkboxes = findHtmlElementsByAttribute(mainPage.getDocumentElement(), "input", "type", "checkbox");
            
            Iterator iter = checkboxes.iterator();
            List ids = new ArrayList();
            
            while(iter.hasNext())
            {
                HtmlInput checkbox = (HtmlInput)iter.next();
                
                if(!checkbox.isChecked())
                {
                    ids.add(checkbox.getId());
                    checkbox.click();
                    
                    if(ids.size() == 3)
                        break;
                }
            }
            
            if(ids.isEmpty())
                fail("Could not find any inactive scorecards to activate");
            
            try
            {
                Thread.sleep(5000L);
            }
            catch (InterruptedException x)
            {
                // Ignore
            }
            
            int failed = 0;
            
            iter = ids.iterator();
            while(iter.hasNext())
                if(!((HtmlInput)mainPage.getHtmlElementById((String)iter.next())).isChecked())
                    ++failed;
            
            assertEquals("Some scorecards failed to change to active status", 0, failed);
        }
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #18 (FTC 64)
     * 
     * <ol>
     *      <li>User navigates to scorecard list</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation Error indicating user does not have permission to view scorecard list.
     * </p>
     */
    public void testScenario018()
    {
        testScenario015();
    }

    /**
     * Test Scenario #19 (FTC 68)
     * 
     * <ol>
     *      <li>User navigates to scorecard list</li>
     *      <li>User clicks on scorecard</li>
     *      <li>User clicks on "Copy"</li>
     *      <li>User clicks on "Save New Scorecard"</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation error is shown indicating scorecard is currently linked to a project and name must be changed.
     * </p>
     */
    public void testScenario019()
    {
        fail("This scenario is in conflict with scenario #18");
    }

    /**
     * Test Scenario #20 (FTC 64-66,70,71)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User navigates to scorecard list</li>
     *      <li>User clicks on scorecard</li>
     *      <li>User clicks on "Copy"</li>
     *      <li>User clicks on "Save New Scorecard"</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      A new Scorecard is created and saved to the database.  Major and minor version numbers is incremented.
     * </p>
     */
    public void testScenario020()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);
            
            HtmlAnchor anchor = getFirstAnchorByText(mainPage, "test_scorecard01 v 1.1");
            assertNotNull("Expected to find scorecard \"test_scorecard01 v 1.1\", but it was missing.", anchor);
            
            HtmlPage detailPage = (HtmlPage)anchor.click();
            HtmlForm form = detailPage.getFormByName("scorecardForm");
            HtmlInput copyButton = getInputByValue(form, "Copy");
            assertNotNull("Expected to find button \"Copy\", but it was missing", copyButton);
            HtmlPage copyPage = (HtmlPage)copyButton.click();
            HtmlInput saveButton = getInputByValue(form, "Save New Scorecard");
            assertNotNull("Expected to find button \"Save New Scorecard\", but it was missing", saveButton);
            mainPage = (HtmlPage)saveButton.click();
            
            anchor = getFirstAnchorByText(mainPage, "test_scorecard01 v 2.2");
            assertNotNull("Expected to find scorecard \"test_scorecard01 v 2.2\", but it was missing.", anchor);
        }
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #21 (FTC 64,65,67,70)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User navigates to scorecard list</li>
     *      <li>User clicks on test_scorecard06</li>
     *      <li>User clicks on "Copy"</li>
     *      <li>User changes scorecard name and version number using the data below</li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Scorecard Name</td><td>test_scorecardcopy01</td></tr>
     *          <tr><td>Version Number</td><td>1.0</td></tr>
     *      </table>
     *      <li>User clicks on "Save New Scorecard"</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      A new Scorecard is created and saved to the database.  Major and minor version numbers is incremented.
     * </p>
     */
    public void testScenario021()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);
            
            HtmlAnchor anchor = getFirstAnchorByText(mainPage, "test_scorecard06 v 1.0");
            assertNotNull("Expected to find scorecard \"test_scorecard06 v 1.0\", but it was missing.", anchor);
            
            HtmlPage detailPage = (HtmlPage)anchor.click();
            HtmlForm form = detailPage.getFormByName("scorecardForm");
            HtmlInput copyButton = getInputByValue(form, "Copy");
            assertNotNull("Expected to find button \"Copy\", but it was missing", copyButton);
            HtmlPage copyPage = (HtmlPage)copyButton.click();
            
            HtmlForm editForm = copyPage.getFormByName("scorecardForm");
            form.getInputByName("scorecard.name").setValueAttribute("test_scorecardcopy01");
            form.getInputByName("scorecard.version").setValueAttribute("1.0");
            
            HtmlInput saveButton = getInputByValue(editForm, "Save New Scorecard");
            assertNotNull("Expected to find button \"Save New Scorecard\", but it was missing", saveButton);
            mainPage = (HtmlPage)saveButton.click();
            
            anchor = getFirstAnchorByText(mainPage, "test_scorecardcopy01 v 2.1");
            assertNotNull("Expected to find scorecard \"test_scorecardcopy01 v 2.1\", but it was missing.", anchor);
        }
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #22 (FTC 64,65,67,68,69)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User navigates to scorecard list</li>
     *      <li>User clicks on scorecard</li>
     *      <li>User clicks on "Copy"</li>
     *      <li>User completes scorecard details using the data below</li>
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
    public void testScenario022()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);
            
            HtmlAnchor anchor = getFirstAnchorByText(mainPage, "test_scorecard01 v 2.2");
            assertNotNull("Expected to find scorecard \"test_scorecard01 v 2.2\", but it was missing.", anchor);
            
            HtmlPage detailPage = (HtmlPage)anchor.click();
            HtmlForm form = detailPage.getFormByName("scorecardForm");
            HtmlInput copyButton = getInputByValue(form, "Copy");
            assertNotNull("Expected to find button \"Copy\", but it was missing", copyButton);
            HtmlPage copyPage = (HtmlPage)copyButton.click();
            
            HtmlForm editForm = copyPage.getFormByName("scorecardForm");
            editForm.getInputByName("scorecard.name").setValueAttribute("test_scorecard04");
            editForm.getInputByName("scorecard.version").setValueAttribute("1.0");
            editForm.getSelectByName("projectTypeName").setSelectedAttribute("Component", true);
            editForm.getSelectByName("projectCategoryName").setSelectedAttribute("Design", true);
            editForm.getSelectByName("scorecard.scorecardType.name").setSelectedAttribute("Screening", true);
            editForm.getInputByName("minScoreText").setValueAttribute("0");
            editForm.getInputByName("maxScoreText").setValueAttribute("100");
            editForm.getSelectByName("scorecard.scorecardStatus.name").setSelectedAttribute("Active", true);
            editForm.getInputByName("scorecard.allGroups[0].name").setValueAttribute("test_group01");
            editForm.getInputByName("scorecard.allGroups[0].weight").setValueAttribute("50");
            editForm.getInputByName("scorecard.allGroups[0].allSections[0].name").setValueAttribute("test_section01");
            editForm.getInputByName("scorecard.allGroups[0].allSections[0].weight").setValueAttribute("50");
            ((HtmlTextArea)editForm.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].description").get(0)).setText("test_question01");
            ((HtmlTextArea)editForm.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline01");
            editForm.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            editForm.getInputByName("scorecard.allGroups[0].allSections[0].allQuestions[0].weight").setValueAttribute("50");
            editForm.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            ((HtmlInput)findOneHtmlElementByAttribute(editForm, "input", "title", "btnAddGroup[0]")).click();
            
            editForm.getInputByName("scorecard.allGroups[1].name").setValueAttribute("test_group02");
            editForm.getInputByName("scorecard.allGroups[1].weight").setValueAttribute("25");
            editForm.getInputByName("scorecard.allGroups[1].allSections[0].name").setValueAttribute("test_section02");
            editForm.getInputByName("scorecard.allGroups[1].allSections[0].weight").setValueAttribute("50");
            ((HtmlTextArea)editForm.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].description").get(0)).setText("test_question02");
            ((HtmlTextArea)editForm.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline02");
            editForm.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            editForm.getInputByName("scorecard.allGroups[1].allSections[0].allQuestions[0].weight").setValueAttribute("50");
            editForm.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            HtmlPage responsePage = (HtmlPage)editForm.getInputByValue("Save New Scorecard").click();
            
            assertTrue("Expected Validation error indicating Group Weights DO NOT Sum to 100", responsePage.asText().toLowerCase().indexOf("weights of groups must sum up to 100") != -1);
        }
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #23 (FTC 64,65,67,68,69)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User navigates to scorecard list</li>
     *      <li>User clicks on scorecard</li>
     *      <li>User clicks on "Copy"</li>
     *      <li>User completes scorecard details using the data below</li>
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
     *      Validation error is shown indicating Group Weights DO NOT Sum to 100.
     * </p>
     */
    public void testScenario023()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);
            
            HtmlAnchor anchor = getFirstAnchorByText(mainPage, "test_scorecard01 v 2.2");
            assertNotNull("Expected to find scorecard \"test_scorecard01 v 2.2\", but it was missing.", anchor);
            
            HtmlPage detailPage = (HtmlPage)anchor.click();
            HtmlForm form = detailPage.getFormByName("scorecardForm");
            HtmlInput copyButton = getInputByValue(form, "Copy");
            assertNotNull("Expected to find button \"Copy\", but it was missing", copyButton);
            HtmlPage copyPage = (HtmlPage)copyButton.click();
            
            HtmlForm editForm = copyPage.getFormByName("scorecardForm");
            editForm.getInputByName("scorecard.name").setValueAttribute("test_scorecard05");
            editForm.getInputByName("scorecard.version").setValueAttribute("1.0");
            editForm.getSelectByName("projectTypeName").setSelectedAttribute("Component", true);
            editForm.getSelectByName("projectCategoryName").setSelectedAttribute("Design", true);
            editForm.getSelectByName("scorecard.scorecardType.name").setSelectedAttribute("Screening", true);
            editForm.getInputByName("minScoreText").setValueAttribute("0");
            editForm.getInputByName("maxScoreText").setValueAttribute("100");
            editForm.getSelectByName("scorecard.scorecardStatus.name").setSelectedAttribute("Active", true);
            editForm.getInputByName("scorecard.allGroups[0].name").setValueAttribute("test_group01");
            editForm.getInputByName("scorecard.allGroups[0].weight").setValueAttribute("50");
            editForm.getInputByName("scorecard.allGroups[0].allSections[0].name").setValueAttribute("test_section01");
            editForm.getInputByName("scorecard.allGroups[0].allSections[0].weight").setValueAttribute("50");
            ((HtmlTextArea)editForm.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].description").get(0)).setText("test_question01");
            ((HtmlTextArea)editForm.getTextAreasByName("scorecard.allGroups[0].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline01");
            editForm.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            editForm.getInputByName("scorecard.allGroups[0].allSections[0].allQuestions[0].weight").setValueAttribute("50");
            editForm.getSelectByName("scorecard.allGroups[0].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            ((HtmlInput)findOneHtmlElementByAttribute(editForm, "input", "title", "btnAddGroup[0]")).click();
            
            editForm.getInputByName("scorecard.allGroups[1].name").setValueAttribute("test_group02");
            editForm.getInputByName("scorecard.allGroups[1].weight").setValueAttribute("50");
            editForm.getInputByName("scorecard.allGroups[1].allSections[0].name").setValueAttribute("test_section02");
            editForm.getInputByName("scorecard.allGroups[1].allSections[0].weight").setValueAttribute("25");
            ((HtmlTextArea)editForm.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].description").get(0)).setText("test_question02");
            ((HtmlTextArea)editForm.getTextAreasByName("scorecard.allGroups[1].allSections[0].allQuestions[0].guideline").get(0)).setText("test_questionguideline02");
            editForm.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].questionType.name").setSelectedAttribute("Scale (1-4)", true);
            editForm.getInputByName("scorecard.allGroups[1].allSections[0].allQuestions[0].weight").setValueAttribute("50");
            editForm.getSelectByName("scorecard.allGroups[1].allSections[0].allQuestions[0].documentUploadValue").setSelectedAttribute("Optional", true);
            
            HtmlPage responsePage = (HtmlPage)editForm.getInputByValue("Save New Scorecard").click();
            
            assertTrue("Expected Validation error indicating Section Weights DO NOT Sum to 100", responsePage.asText().toLowerCase().indexOf("weights of sections must sum up to 100") != -1);
        }
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }
}
