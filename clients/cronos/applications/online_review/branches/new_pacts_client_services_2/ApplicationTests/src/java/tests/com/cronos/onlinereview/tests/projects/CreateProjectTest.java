/* Copyright (C) 2007 TopCoder Inc., All Rights Reserved. */

package com.cronos.onlinereview.tests.projects;

import com.cronos.onlinereview.tests.User;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.cronos.onlinereview.AbstractTestCase;
import com.cronos.onlinereview.Configuration;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import java.io.IOException;
import java.net.MalformedURLException;

public class CreateProjectTest extends AbstractTestCase
{
    /**
     * Test Scenario #28 (FTC 76)
     * 
     * <ol>
     *      <li>User clicks on "Create Project"</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation Error indicating user does not have permission to create a project.
     * </p>
     */
    public void testScenario028()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getReviewUrl());
            HtmlPage mainPage = login(loginPage, User.OBSERVER);
            assertNotLoginPage(mainPage);
            
            assertTrue(mainPage.asText().toLowerCase().indexOf("newproject.do") == -1);
        } 
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #29 (FTC 76, 77, 78, 79, 81, 82, 83, 84, 85)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User clicks on "Create Project"</li>
     *      <li>User enters sample data below</li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Name</td>test_projectname02<td></td></tr>
     *          <tr><td>Type</td>Component<td></td></tr>
     *          <tr><td>Category</td><td>Specification</td></tr>
     *          <tr><td>Eligibility</td><td>TopCoder Private</td></tr>
     *          <tr><td>Public</td><td>Yes</td></tr>
     *          <tr><td>Auto Pilot</td><td>Turn Off</td></tr>
     *          <tr><td>Send Email Notifications</td><td>Check</td></tr>
     *          <tr><td>Do Not Rate This Project</td><td>Leave Unchecked</td></tr>
     *          <tr><td>Receive Time Notification</td><td>Leave Unchecked</td></tr>
     *          <tr><td>Screening</td><td>Component Design Screening Scorecard v 1.0</td></tr>
     *          <tr><td>Review</td><td>Component Design Review Scorecard v 1.4.2</td></tr>
     *          <tr><td>Approval</td><td>Component Design Screening Scorecard v 1.4.2</td></tr>
     *          <tr><td>Forum Link</td><td>https://software.topcoder.com/forum/c_forum.jsp?f=21218907</td></tr>
     *          <tr><td>SVN Module</td><td>https://coder.topcoder.com/tcs/clients/cronos/applications/online_review/trunk/docs/specification</td></tr>
     *          <tr><td>Notes</td><td>test_notes01</td></tr>
     *          <tr><td>Create Timeline, Use Timeline Template</td><td>Design (then click "Load Template")</td></tr>
     *          <tr><td>Resources, Role</td><td>Project Manager</td></tr>
     *          <tr><td>Name</td><td>test_johnsmith01</td></tr>
     *          <tr><td>Payment</td><td>Select Option Button and Enter 100.00</td></tr>
     *          <tr><td>Paid</td><td>Paid, and click "Add"</td></tr>
     *      </table>
     *      <li>User clicks on "Save"</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      A new project is created and saved.
     * </p>
     */
    public void testScenario029()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getReviewUrl());
            HtmlPage mainPage = login(loginPage, User.OBSERVER);
            assertNotLoginPage(mainPage);
            
            HtmlAnchor createProjectTab = getFirstAnchorByImage(mainPage, "/i/or/tab_create_project.gif");
            assertNotNull("Expected to find \"Create Project\" tab", createProjectTab);
            HtmlPage projectPage = (HtmlPage)createProjectTab.click();
            
            HtmlForm form = projectPage.getFormByName("projectForm");
            form.getInputByName("project_name").setValueAttribute("test_projectname02");
            form.getSelectByName("project_type").setSelectedAttribute("Component", true);
            form.getSelectByName("project_category").setSelectedAttribute("Specification", true);
            form.getSelectByName("eligibility").setSelectedAttribute("TopCoder Private", true);
            form.getInputByName("public").setChecked(true);
            ((HtmlInput)form.getInputsByName("autopilot").get(1)).setChecked(true);
            form.getInputByName("email_notifications").setChecked(true);
            form.getInputByName("forum_id").setValueAttribute("https://software.topcoder.com/forum/c_forum.jsp?f=21218907");
            form.getInputByName("SVN_module").setValueAttribute("https://coder.topcoder.com/tcs/clients/cronos/applications/online_review/trunk/docs/specification");
            ((HtmlTextArea)form.getTextAreasByName("notes").get(0)).setText("test_notes0");
            form.getSelectByName("template_name").setSelectedAttribute("Design", true);
            getFirstAnchorByImage(mainPage, "/i/or/bttn_load_template.gif").click();
            form.getSelectByName("resources_role[0]").setSelectedAttribute("Manager", true);
            form.getInputByName("resources_name[0]").setValueAttribute("test_johnsmith01");
            form.getInputByName("resources_payment[0]").setChecked(true);
            form.getInputByName("resources_payment_amount[0]").setValueAttribute("100.00");
            form.getSelectByName("resources_paid[0]").setSelectedAttribute("Paid", true);
            
            HtmlPage resultPage = (HtmlPage)form.getInputByName("").click();
            
            assertTrue("Did not expect to stay on the project details page", resultPage.asText().indexOf("Project Details") == -1);
            assertTrue("Expected to be redirected to the open projects list and find the new project listed", resultPage.asText().indexOf("test_projectname02") != -1);
        } 
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #30 (FTC 79, 80)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User clicks on "Create Project"</li>
     *      <li>User enters sample data below</li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Name</td>test_projectname03<td></td></tr>
     *          <tr><td>Type</td><td></td></tr>
     *          <tr><td>Category</td><td>Specification</td></tr>
     *          <tr><td>Eligibility</td><td>TopCoder Private</td></tr>
     *          <tr><td>Public</td><td>Yes</td></tr>
     *          <tr><td>Auto Pilot</td><td>Turn Off</td></tr>
     *          <tr><td>Send Email Notifications</td><td></td></tr>
     *          <tr><td>Do Not Rate This Project</td><td>Leave Unchecked</td></tr>
     *          <tr><td>Receive Time Notification</td><td>Leave Unchecked</td></tr>
     *          <tr><td>Screening</td><td>Component Design Screening Scorecard v 1.0</td></tr>
     *          <tr><td>Review</td><td>Component Design Review Scorecard v 1.4.2</td></tr>
     *          <tr><td>Approval</td><td>Component Design Screening Scorecard v 1.4.2</td></tr>
     *          <tr><td>Forum Link</td><td>https://software.topcoder.com/forum/c_forum.jsp?f=21218907</td></tr>
     *          <tr><td>SVN Module</td><td>https://coder.topcoder.com/tcs/clients/cronos/applications/online_review/trunk/docs/specification</td></tr>
     *          <tr><td>Notes</td><td>test_notes01</td></tr>
     *          <tr><td>Create Timeline, Use Timeline Template</td><td>Design (then click "Load Template")</td></tr>
     *          <tr><td>Resources, Role</td><td></td></tr>
     *          <tr><td>Name</td><td>test_johnsmith01</td></tr>
     *          <tr><td>Payment</td><td>Select Option Button and Enter 100.00</td></tr>
     *          <tr><td>Paid</td><td>Paid, and click "Add"</td></tr>
     *      </table>
     *      <li>User clicks on "Save"</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation Error is shown indicating that required fields are missing.
     * </p>
     */
    public void testScenario030()
    {
        fail("Test conflicts with page design.  Cannot select blank options, as none exist");
    }

    /**
     * Test Scenario #31 (FTC 79, 80)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User clicks on "Create Project"</li>
     *      <li>User enters sample data below</li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Name</td>test_projectname02<td></td></tr>
     *          <tr><td>Type</td>Component<td></td></tr>
     *          <tr><td>Category</td><td>Specification</td></tr>
     *          <tr><td>Eligibility</td><td>TopCoder Private</td></tr>
     *          <tr><td>Public</td><td>Yes</td></tr>
     *          <tr><td>Auto Pilot</td><td>Turn Off</td></tr>
     *          <tr><td>Send Email Notifications</td><td>Check</td></tr>
     *          <tr><td>Do Not Rate This Project</td><td>Leave Unchecked</td></tr>
     *          <tr><td>Receive Time Notification</td><td>Leave Unchecked</td></tr>
     *          <tr><td>Screening</td><td>Component Design Screening Scorecard v 1.0</td></tr>
     *          <tr><td>Review</td><td>Component Design Review Scorecard v 1.4.2</td></tr>
     *          <tr><td>Approval</td><td>Component Design Screening Scorecard v 1.4.2</td></tr>
     *          <tr><td>Forum Link</td><td>https://software.topcoder.com/forum/c_forum.jsp?f=21218907</td></tr>
     *          <tr><td>SVN Module</td><td>https://coder.topcoder.com/tcs/clients/cronos/applications/online_review/trunk/docs/specification</td></tr>
     *          <tr><td>Notes</td><td>test_notes01</td></tr>
     *          <tr><td>Create Timeline, Use Timeline Template</td><td>Design (then click "Load Template")</td></tr>
     *          <tr><td>Resources, Role</td><td>Project Manager</td></tr>
     *          <tr><td>Name</td><td>test_johnsmith01</td></tr>
     *          <tr><td>Payment</td><td>Select Option Button and Enter 100.00</td></tr>
     *          <tr><td>Paid</td><td>Paid, and click "Add"</td></tr>
     *      </table>
     *      <li>User clicks on "Save"</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation Error is shown indicating that registration date is before current date.
     * </p>
     */
    public void testScenario031()
    {
        fail("Test conflicts with Scenario #29.  Same test, different outcome expected.");
    }

    /**
     * Test Scenario #32 (FTC 79, 80)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User clicks on "Create Project"</li>
     *      <li>User enters sample data below</li>
     *      <table>
     *          <tr><th>Field Name</th><th>Sample Value</th></tr>
     *          <tr><td>Name</td>test_projectname02<td></td></tr>
     *          <tr><td>Type</td>Component<td></td></tr>
     *          <tr><td>Category</td><td>Specification</td></tr>
     *          <tr><td>Eligibility</td><td>TopCoder Private</td></tr>
     *          <tr><td>Public</td><td>Yes</td></tr>
     *          <tr><td>Auto Pilot</td><td>Turn Off</td></tr>
     *          <tr><td>Send Email Notifications</td><td>Check</td></tr>
     *          <tr><td>Do Not Rate This Project</td><td>Leave Unchecked</td></tr>
     *          <tr><td>Receive Time Notification</td><td>Leave Unchecked</td></tr>
     *          <tr><td>Screening</td><td>Component Design Screening Scorecard v 1.0</td></tr>
     *          <tr><td>Review</td><td>Component Design Review Scorecard v 1.4.2</td></tr>
     *          <tr><td>Approval</td><td>Component Design Screening Scorecard v 1.4.2</td></tr>
     *          <tr><td>Forum Link</td><td>https://software.topcoder.com/forum/c_forum.jsp?f=21218907</td></tr>
     *          <tr><td>SVN Module</td><td>https://coder.topcoder.com/tcs/clients/cronos/applications/online_review/trunk/docs/specification</td></tr>
     *          <tr><td>Notes</td><td>test_notes01</td></tr>
     *          <tr><td>Create Timeline, Use Timeline Template</td><td>Design (then click "Load Template")</td></tr>
     *          <tr><td>Resources, Role</td><td>Project Manager</td></tr>
     *          <tr><td>Name</td><td>test_johnsmith01</td></tr>
     *          <tr><td>Payment</td><td>Select Option Button and Enter 100.XX</td></tr>
     *          <tr><td>Paid</td><td>Paid, and click "Add"</td></tr>
     *      </table>
     *      <li>User clicks on "Save"</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation Error is shown indicating that number is invalid.
     * </p>
     */
    public void testScenario032()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getReviewUrl());
            HtmlPage mainPage = login(loginPage, User.OBSERVER);
            assertNotLoginPage(mainPage);
            
            HtmlAnchor createProjectTab = getFirstAnchorByImage(mainPage, "/i/or/tab_create_project.gif");
            assertNotNull("Expected to find \"Create Project\" tab", createProjectTab);
            HtmlPage projectPage = (HtmlPage)createProjectTab.click();
            
            HtmlForm form = projectPage.getFormByName("projectForm");
            form.getInputByName("project_name").setValueAttribute("test_projectname02");
            form.getSelectByName("project_type").setSelectedAttribute("Component", true);
            form.getSelectByName("project_category").setSelectedAttribute("Specification", true);
            form.getSelectByName("eligibility").setSelectedAttribute("TopCoder Private", true);
            form.getInputByName("public").setChecked(true);
            ((HtmlInput)form.getInputsByName("autopilot").get(1)).setChecked(true);
            form.getInputByName("email_notifications").setChecked(true);
            form.getInputByName("forum_id").setValueAttribute("https://software.topcoder.com/forum/c_forum.jsp?f=21218907");
            form.getInputByName("SVN_module").setValueAttribute("https://coder.topcoder.com/tcs/clients/cronos/applications/online_review/trunk/docs/specification");
            ((HtmlTextArea)form.getTextAreasByName("notes").get(0)).setText("test_notes0");
            form.getSelectByName("template_name").setSelectedAttribute("Design", true);
            getFirstAnchorByImage(mainPage, "/i/or/bttn_load_template.gif").click();
            form.getSelectByName("resources_role[0]").setSelectedAttribute("Manager", true);
            form.getInputByName("resources_name[0]").setValueAttribute("test_johnsmith01");
            form.getInputByName("resources_payment[0]").setChecked(true);
            form.getInputByName("resources_payment_amount[0]").setValueAttribute("100.XX");
            form.getSelectByName("resources_paid[0]").setSelectedAttribute("Paid", true);
            
            HtmlPage resultPage = (HtmlPage)form.getInputByName("").click();
            
            assertTrue("Expected validation error for invalid number", resultPage.asText().indexOf("invalid") != -1);
        } 
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #32 (FTC 79, 80)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User clicks on "Create Project"</li>
     *      <li>User selects "Design" from "Use Timeline Template"</li>
     *      <li>User clicks on "Load Template" button</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Design Template is loaded – containing default start time and date, phase start and end dates, and default timeline phases..
     * </p>
     */
    public void testScenario033()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getReviewUrl());
            HtmlPage mainPage = login(loginPage, User.OBSERVER);
            assertNotLoginPage(mainPage);
            
            HtmlAnchor createProjectTab = getFirstAnchorByImage(mainPage, "/i/or/tab_create_project.gif");
            assertNotNull("Expected to find \"Create Project\" tab", createProjectTab);
            HtmlPage projectPage = (HtmlPage)createProjectTab.click();
            
            HtmlForm form = projectPage.getFormByName("projectForm");
            form.getSelectByName("template_name").setSelectedAttribute("Design", true);
            getFirstAnchorByImage(mainPage, "/i/or/bttn_load_template.gif").click();
            
            for(int i=11; i < 22; i++)
            {
                assertTrue(!form.getInputByName("phase_start_date[" + i + "]").equals(""));
                assertTrue(!form.getInputByName("phase_start_time[" + i + "]").equals(""));
                assertTrue(!form.getInputByName("phase_end_date[" + i + "]").equals(""));
                assertTrue(!form.getInputByName("phase_end_time[" + i + "]").equals(""));
            }
        } 
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }
}
