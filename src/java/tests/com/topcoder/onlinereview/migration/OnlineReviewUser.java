/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.ConfirmHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

import junit.framework.Assert;

import java.net.URL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * Utility class to imitate a web user of the online review application. It takes advantage of HtmlUnit to interact
 * with the application, and provides various convenience methods for the functional test cases.
 * </p>
 *
 * @author TCSTESTER
 * @version 1.0
 */
public class OnlineReviewUser implements AlertHandler, ConfirmHandler {
    /** The configuration interface. */
    private static Configuration config = new Configuration(OnlineReviewUser.class.getName());

    /** The web client with the application, which maintains the session. */
    private WebClient client = null;

    /** The current web page. */
    private HtmlPage page = null;

    /** The queue of alert messages. */
    private List alerts = new ArrayList();

    /** The confirm status. */
    private boolean confirm = false;

    /** The queue of confirm messages. */
    private List confirms = new ArrayList();

    /** Username */
    private String username;

    /** Password */
    private String password;

    /**
     * Creates an OnlineReviewUser.
     *
     * @throws Exception if error occurs
     */
    public OnlineReviewUser() throws Exception {
        this(null);
    }

    /**
     * Creates an OnlineReviewUser.
     *
     * @param user the user.
     *
     * @throws Exception if error occurs
     */
    public OnlineReviewUser(String user) throws Exception {
        if (user != null) {
            username = config.getProperty("login." + user + ".username");
            password = config.getProperty("login." + user + ".password");
        }

        // Create web client and register handlers.
        client = new WebClient(BrowserSupport.getTargetBrowser());
        client.setJavaScriptEnabled(true);
        client.setThrowExceptionOnScriptError(false);
        client.setAlertHandler(this);
        client.setConfirmHandler(this);
    }
    /**
     * Perform a login.
     *
     * @throws Exception if login can not be performed.
     */
    public void login() throws Exception {
        // Go to the base URL of the application.
        page = (HtmlPage) client.getPage(new URL(config.getProperty("login_url")));

        setInput("login.username_input", username, true);
        setInput("login.password_input", password, true);
        
        HtmlAnchor link = page.getFirstAnchorByText("Login");
        page = (HtmlPage) link.click();
    }

    /**
     * Perform a login.
     *
     * @throws Exception if login can not be performed.
     */
    public void loginAdmin() throws Exception {
        // Go to the base URL of the application.
        page = (HtmlPage) client.getPage(new URL(config.getProperty("admin_login_url")));

        setInput("login.username_input", username, true);
        setInput("login.password_input", password, true);
        
        HtmlAnchor link = page.getFirstAnchorByText("Login");
        page = (HtmlPage) link.click();
    }

    /**
     * Check if current contest page has project contest list.
     *
     * @return true if there is project contest list 
     *
     * @throws Exception if error occurs
     */
    public boolean hasContestStatusList() throws Exception {
        HtmlTable table = this.findTable("table:class:stat");
        Assert.assertNotNull("Not found contest table", table);

        List list = table.getRows();

        for (Iterator iter = list.iterator(); iter.hasNext();) {
            HtmlTableRow row = (HtmlTableRow) iter.next();

            if ("valueC".equals(row.getCell(0).getClassAttribute())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if current contest page has project contest list.
     *
     * @return true if there is project contest list 
     *
     * @throws Exception if error occurs
     */
    public boolean displayActiveContest() throws Exception {
        HtmlTable table = this.findTable("table:class:stat");
        Assert.assertNotNull("Not found contest table", table);

        List list = table.getRows();

        boolean hasTitle = false;
        boolean hasAllColumns = false;
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            HtmlTableRow row = (HtmlTableRow) iter.next();
            
            if ("title".equals(row.getCell(0).getClassAttribute())) {
            	// check title row
            	Assert.assertEquals("not active page", row.getCell(0).asText(), "Active Component Development Contests");
            	hasTitle = true;
            	continue;
            }
            if ("header".equals(row.getCell(0).getClassAttribute())) {
            	Assert.assertEquals("miss catalog", row.getCell(0).asText(), "Catalog");
            	Assert.assertEquals("miss Component", row.getCell(1).asText(), "Component");
            	Assert.assertEquals("miss Registrants Rated/Unrated", row.getCell(3).asText(), "Registrants Rated/Unrated");
            	Assert.assertEquals("miss Registration Ends", row.getCell(4).asText(), "Registration Ends");
            	Assert.assertEquals("miss Submissions", row.getCell(5).asText(), "Submissions");
            	Assert.assertEquals("miss Payment*", row.getCell(6).asText(), "Payment*");
            	Assert.assertEquals("miss Submit by", row.getCell(7).asText(), "Submit by");
            	hasAllColumns = true;
            	break;
            }
        }

        return (hasAllColumns && hasTitle);
    }

    /**
     * Check if current contest page has project contest list.
     *
     * @return true if there is project contest list 
     *
     * @throws Exception if error occurs
     */
    public boolean displayCatalogPage() throws Exception {
        Assert.assertEquals("Not catalog page", page.getTitleText(), config.getProperty("display_catalog_title"));
        String designScorecard = "Aggregate Design Scorecard";
        String developmentScorecard = "Aggregate Development Scorecard";
        Assert.assertTrue("Miss " + designScorecard + " link", page.asText().indexOf(designScorecard) >= 0);
        Assert.assertTrue("Miss " + developmentScorecard + " link", page.asText().indexOf(developmentScorecard) >= 0);
        HtmlAnchor designLink = this.page.getFirstAnchorByText(designScorecard);
        Assert.assertNotNull("Miss " + designScorecard + " link", designLink);
        HtmlPage nextPage = (HtmlPage) designLink.click();
        Assert.assertEquals("Not scorecard page", nextPage.getTitleText(), "Aggregation Worksheet");
        
        HtmlAnchor developmentLink = this.page.getFirstAnchorByText(developmentScorecard);
        Assert.assertNotNull("Miss " + developmentLink + " link", developmentLink);
        nextPage = (HtmlPage) developmentLink.click();
        Assert.assertEquals("Not scorecard page", nextPage.getTitleText(), "Aggregation Worksheet");
        return true;
    }

    /**
     * Check if current contest page has project contest list.
     *
     * @return true if there is project contest list 
     *
     * @throws Exception if error occurs
     */
    public boolean containsScorecardFullInfo() throws Exception {
    	String[] expects = this.config.getProperties("scorecard_infos");
    	for (int i = 0; i < expects.length; i++) {
    		Assert.assertTrue("Miss info for scorecard page", page.asText().indexOf(expects[i]) >= 0);	
    	}
        
        return true;
    }

    /**
     * Check if current contest page has project contest list.
     *
     * @return true if there is project contest list 
     *
     * @throws Exception if error occurs
     */
    public boolean activeScorecardLinkable() throws Exception {
        HtmlElement[] es = this.findElements("A:class:bcLink", false);
        if (es == null || es.length == 0) {
        	return false;
        }
        return es[0] instanceof HtmlAnchor;
    }

    /**
     * Check if current contest page has project contest list.
     *
     * @return true if there is project contest list 
     *
     * @throws Exception if error occurs
     */
    public void navigateActiveScorecard() throws Exception {
        HtmlElement[] es = this.findElements("A:class:bcLink", false);
        
        Assert.assertFalse("Miss active scorecard link", (es == null || es.length == 0));
        
        this.page = (HtmlPage) ((HtmlAnchor) es[0]).click();
    }

    /**
     * Check if current page is sort by asc.
     *
     * @param isAsc isAsc
     * @param index index
     *
     * @return true if it's asc
     *
     * @throws Exception if error occurs
     */
    public boolean isSort(boolean isAsc, int index) throws Exception {
        HtmlTable table = this.findTable("table:class:stat");
        Assert.assertNotNull("Not found contest table", table);

        List list = table.getRows();

        for (Iterator iter = list.iterator(); iter.hasNext();) {
            HtmlTableRow row = (HtmlTableRow) iter.next();

            if ("headerC".equals(row.getCell(0).getClassAttribute())) {
                HtmlTableCell cell = row.getCell(index);
                String text = isAsc ? "sd=asc" : "sd=desc";
                List archorList = cell.getHtmlElementsByTagName("A");

                if (archorList.size() > 0) {
                    HtmlAnchor e = (HtmlAnchor) archorList.iterator().next();
                    return e.getHrefAttribute().indexOf(text) >= 0;
                }
                
            }
        }

        return false;
    }

    /**
     * Sort contest page by column index.
     *
     * @param index index
     *
     * @throws Exception if error occurs
     */
    public void sortBy(int index) throws Exception {
        HtmlTable table = this.findTable("table:class:stat");
        Assert.assertNotNull("Not found contest table", table);

        List list = table.getRows();

        for (Iterator iter = list.iterator(); iter.hasNext();) {
            HtmlTableRow row = (HtmlTableRow) iter.next();

            if ("headerC".equals(row.getCell(0).getClassAttribute())) {
                HtmlTableCell cell = row.getCell(index);
                List archorList = cell.getHtmlElementsByTagName("A");

                if (archorList.size() > 0) {
                    HtmlAnchor e = (HtmlAnchor) archorList.iterator().next();
                    page = (HtmlPage) e.click();
                }
            }
        }
    }

    /**
     * If current page can navigate to contest page.
     *
     * @return true if it can navigate contest page
     *
     * @throws Exception if error occurs
     */
    public boolean canNavigateContest() throws Exception {
        HtmlTable table = this.findTable("table:class:stat");
        Assert.assertNotNull("Not found contest table", table);

        List list = table.getRows();

        for (Iterator iter = list.iterator(); iter.hasNext();) {
            HtmlTableRow row = (HtmlTableRow) iter.next();

            if ("valueC".equals(row.getCell(0).getClassAttribute())) {
                HtmlTableCell cell = row.getCell(1);
                List archorList = cell.getHtmlElementsByTagName("A");

                if (archorList.size() > 0) {
                    HtmlAnchor e = (HtmlAnchor) archorList.iterator().next();
                    page = (HtmlPage) e.click();

                    if (page.getTitleText().equals(config.getProperty("contest_title"))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Check if current contest page can navigate to component detail page.
     *
     * @return true if it can navigate
     *
     * @throws Exception if error occurs
     */
    public boolean canNavigatorComponentDetail() throws Exception {
        HtmlAnchor anchor = this.page.getFirstAnchorByText(config.getProperty("component_details_text"));

        if (anchor == null) {
            return false;
        }

        page = (HtmlPage) anchor.click();

        return page.getTitleText().equals(config.getProperty("component_details_title"));
    }

    /**
     * Check if current contest page can navigate to ViewRegistrant page.
     *
     * @return true if it can navigate
     *
     * @throws Exception if error occurs
     */
    public boolean canNavigateViewRegistrant() throws Exception {
        HtmlTable table = this.findTable("table:class:stat");
        List list = table.getRows();

        for (Iterator iter = list.iterator(); iter.hasNext();) {
            HtmlTableRow row = (HtmlTableRow) iter.next();

            if ("valueC".equals(row.getCell(0).getClassAttribute())) {
                HtmlTableCell cell = row.getCell(3);

                HtmlAnchor e = getFirstLink(cell);
                if (e != null) {
	                page = (HtmlPage) e.click();
	
	                if (page.asText().indexOf("Registrants") >= 0) {
	                    return true;
	                }
                }
            }
        }

        return false;
    }

    /**
     * Check if current contest page can navigate to ViewRegistrant page.
     *
     * @return true if it can navigate
     *
     * @throws Exception if error occurs
     */
    public boolean canNavigateComponentOverview() throws Exception {
        HtmlTable table = this.findTable("table:class:stat");
        List list = table.getRows();

        for (Iterator iter = list.iterator(); iter.hasNext();) {
            HtmlTableRow row = (HtmlTableRow) iter.next();

            if ("valueC".equals(row.getCell(0).getClassAttribute())) {
                HtmlTableCell cell = row.getCell(1);

                HtmlAnchor e = getFirstLink(cell);
                if (e != null) {
	                page = (HtmlPage) e.click();
	
	                if (page.asText().indexOf("Component Project") >= 0) {
	                    return true;
	                }
                }
            }
        }

        return false;
    }

    /**
     * Check if current contest page can navigate to ViewRegistrant page.
     *
     * @return true if it can navigate
     *
     * @throws Exception if error occurs
     */
    public boolean canNavigateViewRegistrantFromActiveContestPage() throws Exception {
        HtmlTable table = this.findTable("table:class:stat");
        List list = table.getRows();

        for (Iterator iter = list.iterator(); iter.hasNext();) {
            HtmlTableRow row = (HtmlTableRow) iter.next();

            if ("valueC".equals(row.getCell(0).getClassAttribute())) {
                HtmlTableCell cell = row.getCell(3);

                HtmlAnchor e = getFirstLink(cell);
                if (e != null) {
	                page = (HtmlPage) e.click();
	
	                if (page.asText().indexOf("Registrants") >= 0) {
	                    return true;
	                }
                }
            }
        }

        return false;
    }
    
    public HtmlAnchor getFirstLink(HtmlElement element) {
        List archorList = element.getHtmlElementsByTagName("A");

        if (archorList.size() > 0) {
            return (HtmlAnchor) archorList.iterator().next();
        }
        return null;
    }

    /**
     * Check if current contest page can navigate to MemberProfile page.
     *
     * @param winner winner
     *
     * @return true if it can navigate
     *
     * @throws Exception if error occurs
     */
    public boolean canNavigateMemberProfile(boolean winner)
        throws Exception {
        HtmlTable table = this.findTable("table:class:stat");
        List list = table.getRows();

        for (Iterator iter = list.iterator(); iter.hasNext();) {
            HtmlTableRow row = (HtmlTableRow) iter.next();

            if ("valueC".equals(row.getCell(0).getClassAttribute())) {
                HtmlTableCell cell = row.getCell(winner ? 7 : 8);
                List archorList = cell.getHtmlElementsByTagName("A");

                if (archorList.size() > 0) {
                    HtmlAnchor e = (HtmlAnchor) archorList.iterator().next();
                    page = (HtmlPage) e.click();

                    if (page.getTitleText().equals(config.getProperty("MemberProfile_title"))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    
    /**
     * Navigate to contest status page.
     *
     * @throws Exception if error occurs
     */
    public void navigateToContestStatus() throws Exception {
        page = (HtmlPage) client.getPage(new URL(config.getProperty("contest_status_url")));
    }
    
    /**
     * Navigate to contest status page.
     *
     * @throws Exception if error occurs
     */
    public void navigateToActiveContest() throws Exception {
        page = (HtmlPage) client.getPage(new URL(config.getProperty("active_contest_url")));
    }
    
    /**
     * Navigate to display catalog page.
     *
     * @throws Exception if error occurs
     */
    public void navigateToCatalogPage() throws Exception {
        page = (HtmlPage) client.getPage(new URL(config.getProperty("display_catalog_url")));
    }
    
    /**
     * Navigate to display catalog page.
     *
     * @throws Exception if error occurs
     */
    public void navigateToContestDetail() throws Exception {
        page = (HtmlPage) client.getPage(new URL(config.getProperty("contest_detail_url")));
    }
    
    /**
     * Navigate to reviewer_registration_url page.
     *
     * @throws Exception if error occurs
     */
    public void navigateToReviewerRegistration() throws Exception {
        page = (HtmlPage) client.getPage(new URL(config.getProperty("reviewer_registration_url")));
    }
    
    /**
     * Navigate to reviewer_registration_url page.
     *
     * @throws Exception if error occurs
     */
    public void applyReviewer() throws Exception {
    	HtmlAnchor link = this.page.getFirstAnchorByText("Apply Now");

    	Assert.assertNotNull("Miss Apply Now link", link);
    	
    	// Click Apply Now link
    	this.page = (HtmlPage) link.click();
    	
    	// Click Continue submit button
    	this.page = (HtmlPage) ((ClickableElement) this.findElement("input:name:submit", false)).click();
    }
    
    /**
     * Navigate to reviewer_registration_url page.
     *
     * @throws Exception if error occurs
     */
    public void registratorAsCompetitor() throws Exception {
    	HtmlAnchor link = this.page.getFirstAnchorByText("Register");
    	
    	Assert.assertNotNull("Miss Register link", link);

    	// Click Register link
    	this.page = (HtmlPage) link.click();
    	
    	// Confirm page
    	// this.setInput()
    	
    	// Click Continue submit button
    	this.page = (HtmlPage) ((ClickableElement) this.findElement("input:name:submit", false)).click();
    }
    
    /**
     * Navigate to reviewer_registration_url page.
     *
     * @throws Exception if error occurs
     */
    public void createComponent() throws Exception {
    	// TODO Not sure how to create component in new online review app until now
    }

    /**
     * Navigate to competitor_registration_url page.
     *
     * @throws Exception if error occurs
     */
    public void navigateToCompetitorRegistration() throws Exception {
        page = (HtmlPage) client.getPage(new URL(config.getProperty("competitor_registration_url")));
    }

    /**
     * Navigate to competitor_registration_url page.
     *
     * @throws Exception if error occurs
     */
    public void navigateToCreateComponent() throws Exception {
        page = (HtmlPage) client.getPage(new URL(config.getProperty("create_component_url")));
    }

    /**
     * <p>
     * Return if the "Edit" button is visible on the page.
     * </p>
     *
     * @return if the "Edit" button is visible on the page.
     *
     * @throws Exception if the operation failed
     */
    public boolean isEditButtonVisible() throws Exception {
        return (this.findElements("scorecard.edit_scorecard_button", true) != null) &&
        (this.findElements("scorecard.edit_scorecard_button", true).length > 0);
    }

    /**
     * Navigate to a menu item.
     *
     * @param menu the menu item to navigate to. Constants are defined for them.
     *
     * @return whether the navigation is successful (the link exists).
     *
     * @throws Exception if navigation fails.
     */
    public boolean navigateTo(String menu) throws Exception {
        //menu = config.getProperty(menu);
        List anchors = page.getAnchors();

        for (int i = 0; i < anchors.size(); ++i) {
            HtmlAnchor anchor = (HtmlAnchor) anchors.get(i);

            if (anchor.asText().trim().equals(menu)) {
                page = (HtmlPage) anchor.click();

                return true;
            }
        }

        return false;
    }

    /**
     * <p>
     * Click "Save Changes" button on the scorecard edit page.
     * </p>
     *
     * @throws Exception if error occurs
     */
    public void clickSaveChanges() throws Exception {
        this.click("scorecard.save_scorecard_button", true);
    }

    /**
     * <p>
     * Return current page as text.
     * </p>
     *
     * @return current page as text
     */
    public String getPageAsText() {
        return page.asText();
    }

    /**
     * <p>
     * Return current page.
     * </p>
     *
     * @return current page
     */
    public HtmlPage getPage() {
        return page;
    }

    /**
     * Handles alert. Simply add it to the queue.
     *
     * @param page the page for the alert.
     * @param message the alert message.
     */
    public void handleAlert(Page page, String message) {
        alerts.add(message);
    }

    /**
     * Fetch the first alert message.
     *
     * @return the first alert message, or null if none is there.
     */
    public String popAlert() {
        return (alerts.size() > 0) ? (String) alerts.remove(0) : null;
    }

    /**
     * Set confirm status.
     *
     * @param confirm the confirm status.
     */
    public void setConfirmStatus(boolean confirm) {
        this.confirm = confirm;
    }

    /**
     * Handles confirm. Simply add it to the queue and return the predefined status.
     *
     * @param page the page for the alert.
     * @param message the confirm message.
     *
     * @return DOCUMENT ME!
     */
    public boolean handleConfirm(Page page, String message) {
        confirms.add(message);

        return confirm;
    }

    /**
     * Fetch the first confirm message.
     *
     * @return the first confirm message, or null if none is there.
     */
    public String popConfirm() {
        return (confirms.size() > 0) ? (String) confirms.remove(0) : null;
    }

    /**
     * <p>
     * Set the textarea to the given value
     * </p>
     *
     * @param locator the locator for the textarea.
     * @param value the value to set
     * @param resolveFirst if the locator needs resolved first
     *
     * @throws Exception if textarea can not be located.
     */
    private void setTextArea(String locator, String value, boolean resolveFirst)
        throws Exception {
        this.setTextArea(locator, value, 0, resolveFirst);
    }

    private void setTextArea(String locator, String value, int index, boolean resolveFirst)
        throws Exception {
        ((HtmlTextArea) findElement(locator, index, resolveFirst)).setText(value);
    }

    /**
     * Set input to given value.
     *
     * @param locator the locator for the input.
     * @param value the value to set.
     * @param resolveFirst if the locator needs resolved first
     *
     * @throws Exception if input can not be located.
     */
    private void setInput(String locator, String value, boolean resolveFirst)
        throws Exception {
        setInput(locator, value, 0, resolveFirst);
    }

    /**
     * Set input to given value.
     *
     * @param locator the locator for the input.
     * @param value the value to set.
     * @param index the index of the input.
     * @param resolveFirst if the locator needs resolved first
     *
     * @throws Exception if input can not be located.
     */
    private void setInput(String locator, String value, int index, boolean resolveFirst)
        throws Exception {
        ((HtmlInput) findElement(locator, index, resolveFirst)).setValueAttribute(value);
    }

    /**
     * Set option to given value. The value is interpreted as text.
     *
     * @param locator the locator for the select.
     * @param text the text of the option to be selected.
     * @param resolveFirst if the locator needs resolved first
     *
     * @throws Exception if the select can not be located.
     */
    private void selectOption(String locator, String text, boolean resolveFirst)
        throws Exception {
        selectOption(locator, text, 0, resolveFirst);
    }

    /**
     * Set option to given value. The value is interpreted as text.
     *
     * @param locator the locator for the select.
     * @param text the text of the option to be selected.
     * @param index the index of the select.
     * @param resolveFirst if the locator needs resolved first
     *
     * @throws Exception if the select can not be located.
     */
    private void selectOption(String locator, String text, int index, boolean resolveFirst)
        throws Exception {
        HtmlSelect select = (HtmlSelect) findElement(locator, index, resolveFirst);

        for (int i = 0; i < select.getOptionSize(); ++i) {
            if (select.getOption(i).asText().trim().equals(text)) {
                select.setSelectedAttribute(select.getOption(i), true);

                break;
            }
        }
    }

    /**
     * Click a button or a link.
     *
     * @param locator the locator for the button or link.
     * @param resolveFirst if the locator needs resolved first
     *
     * @throws Exception if the button or click can not be located.
     */
    public void click(String locator, boolean resolveFirst)
        throws Exception {
        page = (HtmlPage) ((ClickableElement) findElement(locator, resolveFirst)).click();
    }

    /**
     * Locate a table by summary (the content of the first cell).
     *
     * @param locator the summary to locate the table.
     *
     * @return the located table, or null if not found.
     *
     * @throws Exception if the table can not be located.
     */
    private HtmlTable findTable(String locator) throws Exception {
        return (HtmlTable) this.findElement(locator, true);
    }

    /**
     * Locate an element on the current page. The locator could either be "attribute:value" or "id".
     *
     * @param locator the locator to locate the element. It will first be resolved in configuration.
     * @param resolveFirst if the locator needs resolved first
     *
     * @return the first element that matches the locator, or null if not found.
     *
     * @throws Exception if the page can not be parsed.
     */
    private HtmlElement findElement(String locator, boolean resolveFirst)
        throws Exception {
        return findElement(locator, 0, resolveFirst);
    }

    /**
     * Locate an element on the current page. The locator could either be "attribute:value" or "id".
     *
     * @param locator the locator to locate the element. It will first be resolved in configuration.
     * @param index the index of the element to return.
     * @param resolveFirst if the locator needs resolved first
     *
     * @return the first element that matches the locator, or null if not found.
     *
     * @throws Exception if the page can not be parsed.
     */
    private HtmlElement findElement(String locator, int index, boolean resolveFirst)
        throws Exception {
        return findElements(locator, resolveFirst)[index];
    }

    /**
     * Locate elements on the current page. The locator could either be "attribute:value" or "id".
     *
     * @param locator the locator to locate the element. It will first be resolved in configuration.
     * @param resolveFirst if the locator needs resolved first
     *
     * @return the elements that match the locator.
     *
     * @throws Exception if the page can not be parsed.
     */
    private HtmlElement[] findElements(String locator, boolean resolveFirst)
        throws Exception {
        String orig = locator;

        if (resolveFirst) {
            locator = config.getProperty(locator);
        }

        if (locator == null) {
            locator = orig;
        }

        String[] comp = locator.split(":");

        if (comp.length == 3) {
            List elements = page.getDocumentElement().getHtmlElementsByAttribute(comp[0], comp[1], comp[2]);

            return (HtmlElement[]) elements.toArray(new HtmlElement[elements.size()]);
        } else {
            return new HtmlElement[] { page.getDocumentElement().getHtmlElementById(locator) };
        }
    }
}
