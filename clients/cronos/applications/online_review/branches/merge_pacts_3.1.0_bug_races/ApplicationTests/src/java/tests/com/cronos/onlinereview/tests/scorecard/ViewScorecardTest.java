/* Copyright (C) 2007 TopCoder Inc., All Rights Reserved. */

package com.cronos.onlinereview.tests.scorecard;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.cronos.onlinereview.AbstractTestCase;
import com.cronos.onlinereview.Configuration;
import com.cronos.onlinereview.tests.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ViewScorecardTest extends AbstractTestCase
{
    /**
     * Test Scenario #24 (FTC 72)
     * 
     * <ol>
     *      <li>User navigates to scorecard list</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation Error indicating user does not have permission to view scorecard list.
     * </p>
     */
    public void testScenario024()
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
     * Test Scenario #25 (FTC 72)
     * 
     * <ol>
     *      <li>User navigates to scorecard list</li>
     *      <li>User clicks on scorecard</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Validation Error indicating user does not have permission to view scorecard.
     * </p>
     */
    public void testScenario025()
    {
        fail("This scenario is in conflict with scenario #24");
    }

    /**
     * Test Scenario #26 (FTC 73)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User navigates to scorecard list</li>
     *      <li>User filters scorecards by "Active Only"</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      Only active scorecards are displayed in the scorecard list.
     * </p>
     */
    public void testScenario026()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);

            ((HtmlSelect)mainPage.getHtmlElementById("filterSelect")).setSelectedAttribute("1", true);
            
            List checkboxes = findHtmlElementsByAttribute(mainPage.getDocumentElement(), "input", "type", "checkbox");
            
            Iterator iter = checkboxes.iterator();
            List ids = new ArrayList();
            
            while(iter.hasNext())
            {
                HtmlInput checkbox = (HtmlInput)iter.next();
                assertTrue("Failed to filter out inactive scorecard", checkbox.isChecked());
            }
        }
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }

    /**
     * Test Scenario #27 (FTC 74,75)
     * 
     * <ol>
     *      Note: User is logged in as a Manager
     *      <li>User navigates to scorecard list</li>
     *      <li>User clicks on scorecard</li>
     * </ol>
     * <p>
     *      Expected Outcome:<br/>
     *      User is able to view scorecare in read-only format.
     * </p>
     */
    public void testScenario027()
    {
        try
        {
            HtmlPage loginPage = (HtmlPage)getWebClient().getPage(Configuration.getAdminUrl());
            HtmlPage mainPage = login(loginPage, User.MANAGER);
            assertNotLoginPage(mainPage);

            HtmlAnchor anchor = getFirstAnchorByText(mainPage, "test_scorecard01 v 2.2");
            assertNotNull("Expected to find scorecard \"test_scorecard01 v 2.2\", but it was missing.", anchor);
            HtmlPage viewPage = (HtmlPage)anchor.click();
            
            List inputTextElements = findHtmlElementsByAttribute(viewPage.getDocumentElement(), "input", "type", "text");
            assertTrue("Expected to find no editable text fields on the page", inputTextElements.isEmpty());
        }
        catch(IOException iox)
        {
            fail(iox.getMessage());
        }
    }
}
