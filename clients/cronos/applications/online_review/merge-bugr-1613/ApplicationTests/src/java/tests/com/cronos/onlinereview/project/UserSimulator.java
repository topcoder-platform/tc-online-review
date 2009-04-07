/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.ConfirmHandler;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import junit.framework.Assert;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Utility class to imitate a web user of the online review application.
 * It takes advantage of HtmlUnit to interact with the application, and provides various
 * convenience methods for the functional test cases.
 * </p>
 * @author TCSTESTER
 * @version 1.0
 * @test-status Failed
 * @test-date   10/16/2006
 */
public class UserSimulator implements AlertHandler, ConfirmHandler {
    /**
     * The configuration interface.
     */
    protected static Configuration config = new Configuration(UserSimulator.class.getName());
    /**
     * The web client with the application, which maintains the session.
     */
    private WebClient client = null;

    /**
     * The current web page.
     */
    protected HtmlPage currentPage = null;

    /**
     * The queue of alert messages.
     */
    private List alerts = new ArrayList();

    /**
     * The confirm status.
     */
    private boolean confirm = false;
    /**
     * The queue of confirm messages.
     */
    private List confirms = new ArrayList();

    /**
     * <p>A <code>String</code> constant corresponding to a user granted the <code>Manager</code> role.</p>
     */
    public static final String MANAGER = "manager";

    /**
     * <p>A <code>String</code> constant corresponding to a user granted the <code>Observer</code> role.</p>
     */
    public static final String OBSERVER = "observer";

    /**
     * <p>A <code>String</code> constant corresponding to a user granted the <code>Primary Screener</code> role.</p>
     */
    public static final String PRIMARY_REVIEWER = "primary_reviewer";

    /**
     * <p>A <code>String</code> constant corresponding to a user granted the <code>Reviewer</code> role.</p>
     */
    public static final String REVIEWER1 = "reviewer1";

    /**
     * <p>A <code>String</code> constant corresponding to a user granted the <code>Reviewer</code> role.</p>
     */
    public static final String REVIEWER2 = "reviewer2";

    /**
     * <p>A <code>String</code> constant corresponding to a user granted the <code>Approver</code> role.</p>
     */
    public static final String APPROVER = "approver";

    /**
     * <p>A <code>String</code> constant corresponding to a user granted the <code>Public</code> role.</p>
     */
    public static final String PUBLIC = "public";

    /**
     * <p>A <code>String</code> constant corresponding to a user granted the <code>Designer</code> role.</p>
     */
    public static final String DESIGNER = "designer";

    /**
     * <p>A <code>String</code> constant corresponding to a user granted the <code>Submitter</code> role.</p>
     */
    public static final String WINNING_SUBMITTER = "winner";

    /**
     * <p>A <code>String</code> constant corresponding to a user granted the <code>Submitter</code> role.</p>
     */
    public static final String SECOND_PLACE_SUBMITTER = "place2submitter";

    /**
     * <p>A <code>String</code> constant corresponding to a user granted the <code>Submitter</code> role.</p>
     */
    public static final String THIRD_PLACE_SUBMITTER = "place3submitter";

    /** Username */
    private String username;
    /** Password */
    private String password;
    /** External Id */
    private String id;

    /**
     * Creates an OnlineReviewUser.
     *
     * @param user the user.
     */
    public UserSimulator(String user) {
        username = config.getProperty("login." + user + ".username");
        password = config.getProperty("login." + user + ".password");
        id = config.getProperty("login." + user + ".id");
        // Create web client and register handlers.
        client = new WebClient(BrowserSupport.getTargetBrowser());
        client.setJavaScriptEnabled(true);
        client.setAlertHandler(this);
        client.setConfirmHandler(this);

	DefaultCredentialsProvider credentials = new DefaultCredentialsProvider();
	credentials.addCredentials("alexdelarge", "cl0ckw0rk");
	client.setCredentialsProvider(credentials);
	
    }
    /**
     * Perform a login.
     *
     * @throws Exception if login can not be performed.
     */
    public void login() throws Exception {
        // Go to the base URL of the application.
        currentPage = (HtmlPage) client.getPage(new URL(config.getProperty("application_url")));

        // Fill username, password and submit the form.
        setInput("login.username_input", username, true);
        setInput("login.password_input", password, true);
        System.out.println("LOGGING AS [" + this.username + "] WITH [" + this.password + "]");
        click("login.login_button", true);
        System.out.println("PAGE = " + currentPage.asXml());
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
        List anchors = currentPage.getAnchors();
        for (int i = 0; i < anchors.size(); ++i) {
            HtmlAnchor anchor = (HtmlAnchor) anchors.get(i);
            if (anchor.asText().trim().equals(menu)) {
                currentPage = (HtmlPage) anchor.click();
//                System.out.println("After clicking link : " + menu + " the page content is:");
//                System.out.println(currentPage.asXml());
                return true;
            }
        }
        return false;
    }

    /**
     * <p>
     * Return current page as text.
     * </p>
     * @return current page as text
     */
    public String getPageAsText() {
        return currentPage.asText();
    }
    /**
     * <p>
     * Return current page.
     * </p>
     * @return current page
     */
    public HtmlPage getCurrentPage() {
        return currentPage;
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
        return alerts.size() > 0 ? (String) alerts.remove(0) : null;
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
        return confirms.size() > 0 ? (String) confirms.remove(0) : null;
    }
    /**
     * Set input to given value.
     *
     * @param locator the locator for the input.
     * @param value the value to set.
     *
     * @throws Exception if input can not be located.
     */
    private void setInput(String locator, String value, boolean resolveFirst) throws Exception {
        setInput(locator, value, 0, resolveFirst);
    }

    /**
     * Set input to given value.
     *
     * @param locator the locator for the input.
     * @param value the value to set.
     * @param index the index of the input.
     *
     * @throws Exception if input can not be located.
     */
    private void setInput(String locator, String value, int index, boolean resolveFirst) throws Exception {
        ((HtmlInput) findElement(locator, index, resolveFirst)).setValueAttribute(value);
    }

    /**
     * Set option to given value. The value is interpreted as text.
     *
     * @param locator the locator for the select.
     * @param text the text of the option to be selected.
     *
     * @throws Exception if the select can not be located.
     */
    private void selectOption(String locator, String text, boolean resolveFirst) throws Exception {
        selectOption(locator, text, 0, resolveFirst);
    }

    /**
     * Set option to given value. The value is interpreted as text.
     *
     * @param locator the locator for the select.
     * @param text the text of the option to be selected.
     * @param index the index of the select.
     *
     * @throws Exception if the select can not be located.
     */
    private void selectOption(String locator, String text, int index, boolean resolveFirst) throws Exception {
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
     *
     * @throws Exception if the button or click can not be located.
     */
    public void click(String locator, boolean resolveFirst) throws Exception {
        currentPage = (HtmlPage) ((ClickableElement) findElement(locator, resolveFirst)).click();
    }

    /**
     * Click a button or a link.
     *
     * @param link the link for the button or link.
     *
     * @throws Exception if the button or click can not be located.
     */
    public void click(HtmlAnchor link) throws Exception {
        currentPage = (HtmlPage) link.click();
    }

    /**
     * Click a button or a link.
     *
     * @param submit the link for the button or link.
     *
     * @throws Exception if the button or click can not be located.
     */
    public void click(HtmlImageInput submit) throws Exception {
        currentPage = (HtmlPage) submit.click();
    }

    /**
     * Locate a table by summary (the content of the first cell).
     *
     * @param summary the summary to locate the table.
     *
     * @return the located table, or null if not found.
     *
     * @throws Exception if the table can not be located.
     */
    private HtmlTable findTable(String summary) throws Exception {
        summary = config.getProperty(summary);
        List tables = currentPage.getDocumentElement().getHtmlElementsByTagName("table");

        for (int i = 0; i < tables.size(); ++i) {
            HtmlTable table = (HtmlTable) tables.get(i);
            if (table.getRowCount() > 0 && table.getRow(0).getCells().size() > 0 &&
                table.getRow(0).getCell(0).asText().trim().startsWith(summary)) {
                return table;
            }
        }
        return null;
    }

    /**
     * Locate an element on the current page. The locator could either be "attribute:value" or "id".
     *
     * @param locator the locator to locate the element. It will first be resolved in configuration.
     *
     * @return the first element that matches the locator, or null if not found.
     *
     * @throws Exception if the page can not be parsed.
     */
    public HtmlElement findElement(String locator, boolean resolveFirst) throws Exception {
        return findElement(locator, 0, resolveFirst);
    }

    /**
     * Locate an element on the current page. The locator could either be "attribute:value" or "id".
     *
     * @param locator the locator to locate the element. It will first be resolved in configuration.
     * @param index the index of the element to return.
     *
     * @return the first element that matches the locator, or null if not found.
     *
     * @throws Exception if the page can not be parsed.
     */
    private HtmlElement findElement(String locator, int index, boolean resolveFirst) throws Exception {
        return findElements(locator, resolveFirst)[index];
    }

    /**
     * Locate elements on the current page. The locator could either be "attribute:value" or "id".
     *
     * @param locator the locator to locate the element. It will first be resolved in configuration.
     *
     * @return the elements that match the locator.
     *
     * @throws Exception if the page can not be parsed.
     */
    private HtmlElement[] findElements(String locator, boolean resolveFirst) throws Exception {
        if (resolveFirst) {
            locator = config.getProperty(locator);
        }
        String[] comp = locator.split(":");
        if (comp.length == 3) {
            List elements = currentPage.getDocumentElement().getHtmlElementsByAttribute(comp[0], comp[1], comp[2]);
            return (HtmlElement[]) elements.toArray(new HtmlElement[elements.size()]);
        } else {
            return new HtmlElement[] {currentPage.getDocumentElement().getHtmlElementById(locator)};
        }
    }

    /**
     * <p>Clicks the "All Open Projects" tab which is expected to be displayed by the current page.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    public void clickAllOpenProjects() throws Exception {
        HtmlAnchor link = findLinkWithImage("tab_all_open_projects.gif");
        assert (link != null) : "The [All Open Projects] link is not displayed";
        this.currentPage = (HtmlPage) link.click();
    }

    /**
     * <p>Clicks the "Create Project" tab which is expected to be displayed by the current page.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    public void clickCreateProject() throws Exception {
        HtmlAnchor link = findLinkWithImage("tab_create_project.gif");
        if (link != null) {
            this.currentPage = (HtmlPage) link.click();
        } else {
            Assert.assertTrue("[Create Project] tab is not available", isTabOpened("tab_create_project_on.gif"));
        }
    }

    /**
     * <p>Clicks the "My Open Projects" tab which is expected to be displayed by the current page.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    public void clickMyOpenProjects() throws Exception {
        HtmlAnchor link = findLinkWithImage("tab_my_open_projects.gif");
        if (link != null) {
            this.currentPage = (HtmlPage) link.click();
        } else {
            Assert.assertTrue("[My Open Projects] tab is not available", isTabOpened("tab_my_open_projects_on.gif"));
        }
    }

    /**
     * <p>Verifies if the specified tab is currently opened. The tab is identified by the name of the image file.</p>
     *
     * @param imageFile a <code>String</code> providing the name with tab image file.
     * @return <code>true</code> if requested tab is currently opened; <code>false</code> otherwise.
     * @throws Exception if an unexpected error occurs.
     */
    private boolean isTabOpened(String imageFile) throws Exception {
        HtmlElement div = findElement("div:id:mainTabs", false);
        List images = div.getHtmlElementsByTagName("img");
        for (int i = 0; i < images.size(); i++) {
            HtmlImage image = (HtmlImage) images.get(i);
            if (image.getSrcAttribute().endsWith(imageFile)) {
                if (image.getParentNode() == div) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>Clicks the "Inactive Projects" tab which is expected to be displayed by the current page.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    public void clickInactiveProjects() throws Exception {
        HtmlAnchor link = findLinkWithImage("tab_inactive_projects.gif");
        Assert.assertNotNull("The [Inactive Projects] link is not displayed", link);
        this.currentPage = (HtmlPage) link.click();
    }

    /**
     * <p>Opens "All Open Projects" tab. Logs to application and clicks the "All Open Projects" tab.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    public void openAllProjectsPage() throws Exception {
        login();
        clickAllOpenProjects();
    }

    /**
     * <p>Opens "Inactive Projects" tab. Logs to application and clicks the "Inactive Projects" tab.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    public void openInactiveProjectsPage() throws Exception {
        login();
        clickInactiveProjects();
    }

    /**
     * <p>Opens "My Open Projects" tab. Logs to application and clicks the "My Open Projects" tab.</p>
     *
     * @throws Exception if an unexpected error occurs.
     */
    public void openMyProjectsPage() throws Exception {
        login();
        clickMyOpenProjects();
    }

    /**
     * <p>Opens the page with screening results for the specified project. Logs the user into application; opens
     * "All Active Projects" tab; clicks the link with specified project name; clicks "Submission/Screening" tab.</p>
     *
     * @param project a <code>String</code> providing the name of the project to view screening results for.
     * @throws Exception if an unexpected error occurs.
     */
    public void openSubmissionScreeningTab(String project) throws Exception {
        openAllProjectsPage();
        navigateTo(project);
        navigateTo("Submission/Screening");
    }

    /**
     * <p>Opens the tab with final fix and final review results for the specified project. Logs the user into
     * application; opens "All Active Projects" tab; clicks the link with specified project name; clicks
     * "Final Fix/Review" tab.</p>
     *
     * @param project a <code>String</code> providing the name of the project to view screening results for.
     * @throws Exception if an unexpected error occurs.
     */
    public void openFinalFixReviewTab(String project) throws Exception {
        openAllProjectsPage();
        navigateTo(project);
        navigateTo("Final Fix/Review");
    }

    /**
     * <p>Opens the page with screening results for the specified submission in context of the specified project. Logs
     * the user into application; opens "All Active Projects" tab; clicks the link with specified project name; clicks
     * the link corresponding to the specified submission.</p>
     *
     * @param project a <code>String</code> providing the name of the project to view screening results for.
     * @param submissionId a <code>String</code> providing the ID of a submission to view screening results for.
     * @throws Exception if an unexpected error occurs.
     */
    public void openScreeningResultsPage(String project, String submissionId) throws Exception {
        openAllProjectsPage();
        navigateTo(project);
        navigateTo("Submission/Screening");
        this.currentPage = (HtmlPage) (getScreeningScorecardLink(submissionId)).click();
    }

    /**
     * <p>Opens the page with aggregation review results for the specified project. Logs the user into application;
     * opens "All Active Projects" tab; clicks the link with specified project name; clicks the "Aggregation/Review"
     * tab; clicks the "View Results" link in "Aggregation Review" column.</p>
     *
     * @param project a <code>String</code> providing the name of the project to view aggregation review results for.
     * @throws Exception if an unexpected error occurs.
     */
    public void openAggregationReviewResultsPage(String project) throws Exception {
        openAllProjectsPage();
        navigateTo(project);
        navigateTo("Aggregation/Review");
        HtmlElement div = findElement("div:id:sc4", false);
        assert (div != null) : "The page does not contatin a <DIV> element with ID set to 'sc4'";
        List tables = div.getHtmlElementsByTagName("table");
        assert (tables != null) && (tables.size() == 1) : "The page does not contain a single <TABLE> element within " +
                                                          "<DIV ID='sc4'> element";
        HtmlTable table =(HtmlTable) tables.get(0);
        assert (table != null) && (table.getRowCount() == 4) : "The <TABLE> element does not provide exactly 4 rows";
        HtmlTableCell cell = table.getCellAt(2, 5);
        List links = cell.getHtmlElementsByTagName("a");
        assert (links != null) && (links.size() == 1) : "The 'Aggregation Review' column must provide exactly 1 link";
        this.currentPage = (HtmlPage) ((HtmlAnchor) links.get(0)).click();
    }

    /**
     * <p>Opens the page with aggregation results for the specified project. Logs the user into application;
     * opens "All Active Projects" tab; clicks the link with specified project name; clicks the "Aggregation/Review"
     * tab; clicks the "View Results" link in "Aggregation" column.</p>
     *
     * @param project a <code>String</code> providing the name of the project to view aggregation results for.
     * @throws Exception if an unexpected error occurs.
     */
    public void openAggregationResultsPage(String project) throws Exception {
        openAllProjectsPage();
        navigateTo(project);
        navigateTo("Aggregation/Review");
        HtmlElement div = findElement("div:id:sc4", false);
        assert (div != null) : "The page does not contatin a <DIV> element with ID set to 'sc4'";
        List tables = div.getHtmlElementsByTagName("table");
        assert (tables != null) && (tables.size() == 1) : "The page does not contain a single <TABLE> element within " +
                                                          "<DIV ID='sc4'> element";
        HtmlTable table =(HtmlTable) tables.get(0);
        assert (table != null) && (table.getRowCount() == 4) : "The <TABLE> element does not provide exactly 4 rows";
        HtmlTableCell cell = table.getCellAt(2, 3);
        List links = cell.getHtmlElementsByTagName("a");
        assert (links != null) && (links.size() == 1) : "The 'Aggregation' column must provide exactly 1 link";
        this.currentPage = (HtmlPage) ((HtmlAnchor) links.get(0)).click();
    }

    /**
     * <p>Opens the page with approval results for the specified project. Logs the user into application; opens "All
     * Active Projects" tab; clicks the link with specified project name; clicks the "Approval" tab; clicks the
     * "View Results" link in "Approval" column.</p>
     *
     * @param project a <code>String</code> providing the name of the project to view approval results for.
     * @throws Exception if an unexpected error occurs.
     */
    public void openApprovalResultsPage(String project) throws Exception {
        openAllProjectsPage();
        navigateTo(project);
        navigateTo("Approval");
        HtmlElement div = findElement("div:id:sc6", false);
        assert (div != null) : "The page does not contatin a <DIV> element with ID set to 'sc6'";
        List tables = div.getHtmlElementsByTagName("table");
        assert (tables != null) && (tables.size() == 1) : "The page does not contain a single <TABLE> element within " +
                                                          "<DIV ID='sc6'> element";
        HtmlTable table =(HtmlTable) tables.get(0);
        assert (table != null) && (table.getRowCount() == 4) : "The <TABLE> element does not provide exactly 4 rows";
        HtmlTableCell cell = table.getCellAt(2, 3);
        List links = cell.getHtmlElementsByTagName("a");
        assert (links != null) && (links.size() == 1) : "The 'Approval' column must provide exactly 1 link";
        this.currentPage = (HtmlPage) ((HtmlAnchor) links.get(0)).click();
    }

    /**
     * <p>Opens the page with final review results for the specified project. Logs the user into application; opens "All
     * Active Projects" tab; clicks the link with specified project name; clicks the "Final Fix/Review" tab; clicks the
     * "View Results" link in "Final Review Results" column.</p>
     *
     * @param project a <code>String</code> providing the name of the project to view final review results for.
     * @throws Exception if an unexpected error occurs.
     */
    public void openFinalReviewResultsPage(String project) throws Exception {
        openAllProjectsPage();
        navigateTo(project);
        navigateTo("Final Fix/Review");
        HtmlElement div = findElement("div:id:sc5", false);
        assert (div != null) : "The page does not contatin a <DIV> element with ID set to 'sc5'";
        List tables = div.getHtmlElementsByTagName("table");
        assert (tables != null) && (tables.size() == 1) : "The page does not contain a single <TABLE> element within " +
                                                          "<DIV ID='sc5'> element";
        HtmlTable table =(HtmlTable) tables.get(0);
        assert (table != null) && (table.getRowCount() == 4) : "The <TABLE> element does not provide exactly 4 rows";
        HtmlTableCell cell = table.getCellAt(2, 5);
        List links = cell.getHtmlElementsByTagName("a");
        assert (links != null) && (links.size() == 1) : "The 'Final Review Results' column must provide exactly 1 link";
        this.currentPage = (HtmlPage) ((HtmlAnchor) links.get(0)).click();
    }

    /**
     * <p>Opens the page with composite review results for the specified submission in context of the specified project.
     * Logs the user into application; opens "All Active Projects" tab; clicks the link with specified project name;
     * clicks the "Review/Appeals" tab; clicks the "Review Score" link corresponding to the specified submission.</p>
     *
     * @param project a <code>String</code> providing the name of the project to view compsite review results for.
     * @param submissionId a <code>String</code> providing the ID of a submission to view composite reviews results for.
     * @throws Exception if an unexpected error occurs.
     */
    public void openCompositeReviewResultsPage(String project, String submissionId) throws Exception {
        openAllProjectsPage();
        navigateTo(project);
        navigateTo("Review/Appeals");
        HtmlElement div = findElement("div:id:sc3", false);
        assert (div != null) : "The page does not contatin a <DIV> element with ID set to 'sc3'";
        List tables = div.getHtmlElementsByTagName("table");
        assert (tables != null) && (tables.size() == 1) : "The page does not contain a single <TABLE> element within " +
                                                          "<DIV ID='sc3'> element";
        HtmlTable table =(HtmlTable) tables.get(0);
        assert (table != null) && (table.getRowCount() >= 3) : "The <TABLE> element does not provide at least 3 rows";
        List rows = table.getRows();
        for (int i = 3; i < rows.size() - 1; i++) {
            HtmlTableRow row = (HtmlTableRow) rows.get(i);
            HtmlTableCell submissionCell = row.getCell(0);
            List links = submissionCell.getHtmlElementsByAttribute("a", "title", "Download Submission");
            assert (links != null) && (links.size() == 1) : "The cell does not contain a link for submission";
            HtmlAnchor link = (HtmlAnchor) links.get(0);
            if (link.asText().trim().equals(submissionId)) {
                HtmlTableCell scoreCell = row.getCell(3);
                List scoreLinks = scoreCell.getHtmlElementsByTagName("a");
                assert (scoreLinks != null) && (scoreLinks.size() == 1) : "The 'Review Score' column must provide "
                                                                          + "exactly 1 link";
                this.currentPage = (HtmlPage) ((HtmlAnchor) scoreLinks.get(0)).click();
                return;
            }
        }
        Assert.fail("The page does not provide a link for composite review results for submission ["
                    + submissionId + "]");
    }

    /**
     * <p>Opens the page with review results for the specified project. Logs the user into application; opens "All
     * Active Projects" tab; clicks the link with specified project name; clicks the "Review/Appeals" tab.</p>
     *
     * @param project a <code>String</code> providing the name of the project to view compsite review results for.
     * @throws Exception if an unexpected error occurs.
     */
    public void openReviewAppealsTab(String project) throws Exception {
        openAllProjectsPage();
        navigateTo(project);
        navigateTo("Review/Appeals");
    }

    /**
     * <p>Opens the page with review results for the specified project. Logs the user into application; opens "All
     * Active Projects" tab; clicks the link with specified project name; clicks the "Aggregation/Review" tab.</p>
     *
     * @param project a <code>String</code> providing the name of the project to view compsite review results for.
     * @throws Exception if an unexpected error occurs.
     */
    public void openAggregationReviewTab(String project) throws Exception {
        openAllProjectsPage();
        navigateTo(project);
        navigateTo("Aggregation/Review");
    }

    /**
     * <p>Opens the page with review results for the specified project. Logs the user into application; opens "All
     * Active Projects" tab; clicks the link with specified project name; clicks the "Approval" tab.</p>
     *
     * @param project a <code>String</code> providing the name of the project to view compsite review results for.
     * @throws Exception if an unexpected error occurs.
     */
    public void openApprovalTab(String project) throws Exception {
        openAllProjectsPage();
        navigateTo(project);
        navigateTo("Approval");
    }

    /**
     * <p>Opens the page with composite review results for the specified submission in context of the specified project.
     * Logs the user into application; opens "All Active Projects" tab; clicks the link with specified project name;
     * clicks the "Review/Appeals" tab; clicks the "Score" link corresponding to the specified submission and reviewer.
     * </p>
     *
     * @param project a <code>String</code> providing the name of the project to view compsite review results for.
     * @param submissionId a <code>String</code> providing the ID of a submission to view composite reviews results for.
     * @param reviewerHandle a <code>String</code> providing the handle of the reviewer.
     * @throws Exception if an unexpected error occurs.
     */
    public void openReviewAppealsPage(String project, String submissionId, String reviewerHandle) throws Exception {
        openAllProjectsPage();
        navigateTo(project);
        navigateTo("Review/Appeals");

        HtmlElement div = findElement("div:id:sc3", false);
        assert (div != null) : "The page does not contatin a <DIV> element with ID set to 'sc3'";
        List tables = div.getHtmlElementsByTagName("table");
        assert (tables != null) && (tables.size() == 1) : "The page does not contain a single <TABLE> element within " +
                                                          "<DIV ID='sc3'> element";
        HtmlTable table =(HtmlTable) tables.get(0);
        assert (table != null) && (table.getRowCount() >= 3) : "The <TABLE> element does not provide at least 3 rows";

        // Find the order num for the reviewer
        int reviewerNum = -1;
        for (int i = 4; i < 7; i++) {
            HtmlTableCell cell = table.getCellAt(1, i);
            List links = cell.getHtmlElementsByTagName("a");
            assert (links != null) && (!links.isEmpty()) : "No links pointing to reviewer profile";
            HtmlAnchor reviewerHandleLink = (HtmlAnchor) links.get(links.size() - 1);
            if (reviewerHandleLink.asText().equals(reviewerHandle)) {
                reviewerNum = i - 4;
                break;
            }
        }
        assert (reviewerNum >= 0) : "The reviewer [" + reviewerHandle + "] is not displayed.";

        List rows = table.getRows();
        for (int i = 3; i < rows.size() - 1; i++) {
            HtmlTableRow row = (HtmlTableRow) rows.get(i);
            HtmlTableCell submissionCell = row.getCell(0);
            List links = submissionCell.getHtmlElementsByAttribute("a", "title", "Download Submission");
            assert (links != null) && (links.size() == 1) : "The cell does not contain a link for submission";
            HtmlAnchor link = (HtmlAnchor) links.get(0);
            if (link.asText().trim().equals(submissionId)) {
                HtmlTableCell scoreCell = row.getCell(4 + reviewerNum * 2);
                List scoreLinks = scoreCell.getHtmlElementsByTagName("a");
                assert (scoreLinks != null) && (scoreLinks.size() == 1) : "The 'Score' column must provide "
                                                                          + "exactly 1 link";
                this.currentPage = (HtmlPage) ((HtmlAnchor) scoreLinks.get(0)).click();
                return;
            }
        }
        Assert.fail("The page does not provide a link for review results for submission ["
                    + submissionId + "]");
    }

    /**
     * <p>Opens the page with review scorecard filled by the specified reviewer. The method assumes that either
     * "Final Fix/Review" or "Aggregation/Review" page is the current page displayed to user.</p>
     *
     * @param reviewerHandle a <code>String</code> providing the handle of the reviewer.
     * @throws Exception if an unexpected error occurs.
     * @see    #openAggregationReviewResultsPage(String)
     * @see    #openFinalReviewResultsPage(String)
     */
    public void openReviewScorecard(String reviewerHandle) throws Exception {
        HtmlElement div = findElement("div:id:mainMiddleContent", false);
        assert (div != null) : "The page does not contatin a <DIV> element with ID set to 'mainMiddleContent'";
        List tables = div.getHtmlElementsByAttribute("table", "class", "scorecard");
        assert (tables != null) && (tables.size() > 0) : "The page does not contain a <TABLE> elements within " +
                                                          "<DIV ID='mainMiddleContent'> element";
        // Get the TABLE for the first group and locate the link pointing to review scorecard for the specified reviewer
        HtmlTable table =(HtmlTable) tables.get(0);
        List rows = table.getHtmlElementsByAttribute("tr", "class", "dark");
        for (int i = 0; i < rows.size(); i++) {
            HtmlTableRow row = (HtmlTableRow) rows.get(i);
            HtmlTableCell cell = row.getCell(0);
            List links = cell.getHtmlElementsByTagName("a");
            assert (links != null) && (links.size() == 2) : "The review item must provide two links";
            HtmlAnchor reviewerProfileLink = (HtmlAnchor) links.get(0);
            HtmlAnchor reviewerScorecardLink = (HtmlAnchor) links.get(1);
            if (reviewerProfileLink.asText().equals(reviewerHandle)) {
                this.currentPage = (HtmlPage) reviewerScorecardLink.click();
                return;
            }
        }
        Assert.fail("The page does not provide a link for review scorecard for reviewer [" + reviewerHandle + "]");
    }

    /**
     * <p>Opens the page with "Submit Approval" form for the specified project. Logs the user into application; opens
     * "All Active Projects" tab; clicks the link with specified project name; clicks the "Approval" tab; clicks the
     * "Submit Approval" link in "Approval" column.</p>
     *
     * @param project a <code>String</code> providing the name of the project to open "Submit Approval" page for.
     * @throws Exception if an unexpected error occurs.
     */
    public void openApprovalScorecardPage(String project) throws Exception {
        openAllProjectsPage();
        navigateTo(project);
        navigateTo("Approval");
        
        HtmlElement div = findElement("div:id:sc6", false);
        assert (div != null) : "The page does not contatin a <DIV> element with ID set to 'sc6'";
        List tables = div.getHtmlElementsByTagName("table");
        assert (tables != null) && (tables.size() == 1) : "The page does not contain a single <TABLE> element within " +
                                                          "<DIV ID='sc6'> element";
        HtmlTable table =(HtmlTable) tables.get(0);
        assert (table != null) && (table.getRowCount() == 4) : "The <TABLE> element does not provide exactly 4 rows";
        HtmlTableCell cell = table.getCellAt(2, 3);
        List links = cell.getHtmlElementsByTagName("a");
        assert (links != null) && (links.size() == 1) : "The 'Approval' column must provide exactly 1 link";
        HtmlAnchor link = (HtmlAnchor) links.get(0);
        assert (link != null) && (link.asText().equals("Submit Approval")) : "The link does not point to 'Submit "
                                                                             + "Approval' page";
        this.currentPage = (HtmlPage) link.click();
    }

    /**
     * <p>Opens the page with "Submit Final Review" form for the specified project. Logs the user into application;
     * opens "All Active Projects" tab; clicks the link with specified project name; clicks the "Final Fix/Review" tab;
     * clicks the* "Submit Scorecard" link in "Final Review Results" column.</p>
     *
     * @param project a <code>String</code> providing the name of the project to open "Submit Final Review" page for.
     * @throws Exception if an unexpected error occurs.
     */
    public void openFinalReviewScorecardPage(String project) throws Exception {
        openAllProjectsPage();
        navigateTo(project);
        navigateTo("Final Fix/Review");
        HtmlElement div = findElement("div:id:sc5", false);
        assert (div != null) : "The page does not contatin a <DIV> element with ID set to 'sc5'";
        List tables = div.getHtmlElementsByTagName("table");
        assert (tables != null) && (tables.size() == 1) : "The page does not contain a single <TABLE> element within " +
                                                          "<DIV ID='sc5'> element";
        HtmlTable table =(HtmlTable) tables.get(0);
        assert (table != null) && (table.getRowCount() == 4) : "The <TABLE> element does not provide exactly 4 rows";
        HtmlTableCell cell = table.getCellAt(2, 3);
        List links = cell.getHtmlElementsByTagName("a");
        assert (links != null) && (links.size() == 1) : "The 'Final Review Results' column must provide exactly 1 link";
        HtmlAnchor link = (HtmlAnchor) links.get(0);
        assert (link != null) && (link.asText().equals("Submit Scorecard")) : "The link does not point to 'Submit "
                                                                             + "Final Review' page";
        this.currentPage = (HtmlPage) link.click();
    }

    /**
     * <p>Opens the page with "Submit Screening" form for the specified project. Logs the user into application; opens
     * "All Active Projects" tab; clicks the link with specified project name; clicks the "Submission/Screening" tab;
     * clicks the* "Submit Scorecard" link in "Screening Score" column.</p>
     *
     * @param project a <code>String</code> providing the name of the project to open "Submit Approval" page for.
     * @param submissionId a <code>String</code> providing the ID of a requested submission.
     * @throws Exception if an unexpected error occurs.
     */
    public void openScreeningScorecardPage(String project, String submissionId) throws Exception {
        openAllProjectsPage();
        navigateTo(project);
        navigateTo("Submission/Screening");
        this.currentPage = (HtmlPage) (getScreeningScorecardLink(submissionId)).click();
    }

    /**
     * <p>Submits the approval scorecard for currently opened project with specified parameters. The method assumes that
     * the "Approval Scorecard" page is currently displayed to user.</p>
     *
     * @param commentType a <code>String</code> providing the ID of comment type to set.
     * @param score a <code>String</code> providing the scroe to set.
     * @param responseText a <code>String</code> providing the approver comment to set.
     * @param commit <code>true</code> if the scorecard must be committed; <code>false</code> otherwise.
     * @param forAll <code>true</code> if the specified values must be set for all scorecard items; <code>false</code>
     *        if for first item only.
     * @param preview <code>true</code> if the scorecard must be previewed before saving; <code>false</code> otherwise.
     * @throws Exception if an unexpected error occurs.
     * @see    #openApprovalScorecardPage(String)
     */
    public void submitApproval(String commentType, String score, String responseText, boolean commit, boolean forAll,
                               boolean preview) throws Exception {
        int countSetCommentTypes = 0;
        int countSetScores = 0;
        List commentTypes = this.currentPage.getDocumentElement().getHtmlElementsByTagName("select");
        for (int i = 0; i < commentTypes.size(); i++) {
            HtmlSelect select = (HtmlSelect) commentTypes.get(i);
            if (select.getNameAttribute().startsWith(config.getProperty("approval.comment_type_prefix"))) {
                if (forAll || (countSetCommentTypes == 0)) {
                    select.setSelectedAttribute(commentType, true);
                }
                countSetCommentTypes++;
            } else if (select.getNameAttribute().startsWith(config.getProperty("approval.score_prefix"))) {
                if (forAll || (countSetScores == 0)) {
                    select.setSelectedAttribute(score, true);
                }
                countSetScores++;
            }
        }
        List responses = this.currentPage.getDocumentElement().getHtmlElementsByTagName("textarea");
        for (int i = 0; i < responses.size(); i++) {
            HtmlTextArea response = (HtmlTextArea) responses.get(i);
            response.setText(responseText);
        }

        HtmlAnchor link;
        if (preview) {
            link = findLinkWithImage("bttn_preview.gif");
            assert (link != null) : "The [Preview] button is not displayed";
            this.currentPage = (HtmlPage) link.click();
        }
        if (commit) {
            link = findLinkWithImage("bttn_save_and_mark_complete.gif");
            assert (link != null) : "The [Save and Mark Complete] button is not displayed";
        } else {
            link = findLinkWithImage("bttn_save_for_later.gif");
            assert (link != null) : "The [Save for Later] button is not displayed";
        }
        this.currentPage = (HtmlPage) link.click();
    }

    /**
     * <p>Submits the final review scorecard for currently opened project with specified parameters. The method assumes
     * that the "Final Review Scorecard" page is currently displayed to user.</p>
     *
     * @param selection a <code>String</code> providing the fixed/non-fixed selection for scorecard items.
     * @param commit <code>true</code> if the scorecard must be committed; <code>false</code> otherwise.
     * @param forAll <code>true</code> if the specified values must be set for all scorecard items; <code>false</code>
     *        if for first item only.
     * @param preview <code>true</code> if the scorecard must be previewed before saving; <code>false</code> otherwise.
     * @param approve <code>true</code> if the final fixes must be approved; <code>false</code> otherwise.
     * @throws Exception if an unexpected error occurs.
     * @see    #openFinalReviewScorecardPage(String)
     */
    public void submitFinalReview(String selection, boolean commit, boolean forAll, boolean preview, boolean approve)
        throws Exception {
        int countSetCommentTypes = 0;
        List selections = this.currentPage.getDocumentElement().getHtmlElementsByAttribute("input", "type", "radio");
        for (int i = 0; i < selections.size(); i++) {
            HtmlRadioButtonInput radio = (HtmlRadioButtonInput) selections.get(i);
            if (radio.getNameAttribute().startsWith(config.getProperty("final_review.fixed_selection_prefix"))) {
                if (forAll || (countSetCommentTypes == 0)) {
                    if (radio.getValueAttribute().equals(selection)) {
                        radio.setChecked(true);
                    }
                    countSetCommentTypes++;
                }
            }
        }
        List chekboxes = this.currentPage.getDocumentElement().getHtmlElementsByAttribute("input", "type", "checkbox");
        for (int i = 0; i < chekboxes.size(); i++) {
            HtmlCheckBoxInput checkbox = (HtmlCheckBoxInput) chekboxes.get(i);
            if (checkbox.getNameAttribute().equals(config.getProperty("final_review.scorecard_approval"))) {
                if (approve) {
                    checkbox.setChecked(true);
                } else {
                    checkbox.setChecked(false);
                }
            }
        }

        HtmlAnchor link;
        if (preview) {
            link = findLinkWithImage("bttn_preview.gif");
            assert (link != null) : "The [Preview] button is not displayed";
            this.currentPage = (HtmlPage) link.click();
        }
        if (commit) {
            link = findLinkWithImage("bttn_save_and_mark_complete.gif");
            assert (link != null) : "The [Save and Mark Complete] button is not displayed";
        } else {
            link = findLinkWithImage("bttn_save_and_finish_later.gif");
            assert (link != null) : "The [Save and Finish Later] button is not displayed";
        }
        this.currentPage = (HtmlPage) link.click();
    }

    /**
     * <p>Submits the final review scorecard for currently opened project with specified parameters. The method assumes
     * that the "Final Review Scorecard" page is currently displayed to user.</p>
     *
     * @param selection a <code>String</code> providing the fixed/non-fixed selection for scorecard items.
     * @param commentText a <code>String</code> providing the manager comment text.
     * @param commit <code>true</code> if the scorecard must be committed; <code>false</code> otherwise.
     * @param forAll <code>true</code> if the specified values must be set for all scorecard items; <code>false</code>
     *        if for first item only.
     * @param preview <code>true</code> if the scorecard must be previewed before saving; <code>false</code> otherwise.
     * @throws Exception if an unexpected error occurs.
     * @see    #openFinalReviewScorecardPage(String)
     */
    public void submitFinalReview(String selection, String commentText, boolean commit, boolean forAll, boolean preview)
        throws Exception {
        int countSetCommentTypes = 0;
        List selections = this.currentPage.getDocumentElement().getHtmlElementsByAttribute("input", "type", "radio");
        for (int i = 0; i < selections.size(); i++) {
            HtmlRadioButtonInput radio = (HtmlRadioButtonInput) selections.get(i);
            if (radio.getNameAttribute().startsWith(config.getProperty("final_review.manager.fixed_selection_prefix"))) {
                if (forAll || (countSetCommentTypes == 0)) {
                    if (radio.getValueAttribute().equals(selection)) {
                        radio.setChecked(true);
                    }
                    countSetCommentTypes++;
                }
            }
        }
        List textAreas = getContent().getHtmlElementsByTagName("textarea");
        for (int i = 0; i < textAreas.size(); i++) {
            HtmlTextArea textArea = (HtmlTextArea) textAreas.get(i);
            if (textArea.getNameAttribute().startsWith(config.getProperty("final_review.manager.response_text_prefix"))) {
                textArea.setText(commentText);
            }
        }

        HtmlAnchor link;
        if (preview) {
            link = findLinkWithImage("bttn_preview.gif");
            assert (link != null) : "The [Preview] button is not displayed";
            this.currentPage = (HtmlPage) link.click();
        }
        if (commit) {
            link = findLinkWithImage("bttn_save_and_mark_complete.gif");
            assert (link != null) : "The [Save and Mark Complete] button is not displayed";
        } else {
            link = findLinkWithImage("bttn_save_and_finish_later.gif");
            assert (link != null) : "The [Save and Finish Later] button is not displayed";
        }
        this.currentPage = (HtmlPage) link.click();
    }

    /**
     * <p>Submits the screening scorecard for currently opened project with specified parameters. The method assumes
     * that the "Screening Scorecard" page is currently displayed to user.</p>
     *
     * @param commentType a <code>String</code> providing the ID of comment type to set.
     * @param score a <code>String</code> providing the scroe to set.
     * @param responseText a <code>String</code> providing the approver comment to set.
     * @param commit <code>true</code> if the scorecard must be committed; <code>false</code> otherwise.
     * @param forAll <code>true</code> if the specified values must be set for all scorecard items; <code>false</code>
     *        if for first item only.
     * @param preview <code>true</code> if the scorecard must be previewed before saving; <code>false</code> otherwise.
     * @throws Exception if an unexpected error occurs.
     * @see    #openApprovalScorecardPage(String)
     */
    public void submitScreening(String commentType, String score, String responseText, boolean commit, boolean forAll,
                               boolean preview) throws Exception {
        int countSetCommentTypes = 0;
        int countSetScores = 0;
        List commentTypes = this.currentPage.getDocumentElement().getHtmlElementsByTagName("select");
        for (int i = 0; i < commentTypes.size(); i++) {
            HtmlSelect select = (HtmlSelect) commentTypes.get(i);
            if (select.getNameAttribute().startsWith(config.getProperty("screening.comment_type_prefix"))) {
                if (forAll || (countSetCommentTypes == 0)) {
                    select.setSelectedAttribute(commentType, true);
                }
                countSetCommentTypes++;
            } else if (select.getNameAttribute().startsWith(config.getProperty("screening.score_prefix"))) {
                if (forAll || (countSetScores == 0)) {
                    select.setSelectedAttribute(score, true);
                }
                countSetScores++;
            }
        }
        List responses = this.currentPage.getDocumentElement().getHtmlElementsByTagName("textarea");
        for (int i = 0; i < responses.size(); i++) {
            HtmlTextArea response = (HtmlTextArea) responses.get(i);
            response.setText(responseText);
        }

        HtmlAnchor link;
        if (preview) {
            link = findLinkWithImage("bttn_preview.gif");
            assert (link != null) : "The [Preview] button is not displayed";
            this.currentPage = (HtmlPage) link.click();
        }
        if (commit) {
            link = findLinkWithImage("bttn_save_and_mark_complete.gif");
            assert (link != null) : "The [Save and Mark Complete] button is not displayed";
        } else {
            link = findLinkWithImage("bttn_save_for_later.gif");
            assert (link != null) : "The [Save for Later] button is not displayed";
        }
        this.currentPage = (HtmlPage) link.click();
    }

    /**
     * <p>Updates the appeals responses with specified response text and review item score. The method assumes that the
     * <code>Appeals Response</code> page is currently opened.</p>
     *
     * @param responseText a <code>String</code> providing the new appeals response text.</p>
     * @param score a <code>String</code> providing the new review item score.
     * @throws Exception if an unexpected error occurs.
     */
    public void updateAppealResponses(String responseText, String score) throws Exception {
        List editButtons = findLinksWithImage("bttn_edit.gif");
        for (int i = 0; i < editButtons.size(); i++) {
            HtmlAnchor editButton = (HtmlAnchor) editButtons.get(i);
            String href = editButton.getHrefAttribute();
            int index1 = href.indexOf("toggleDisplay('shortQ_");
            int index2 = href.indexOf("'", index1 + 22);
            String questionId = href.substring(index1 + 22, index2);
            this.currentPage = (HtmlPage) editButton.click();
            HtmlTextArea textArea = (HtmlTextArea) getContent().getOneHtmlElementByAttribute("textarea", "name",
                        config.getProperty("appeals_response.response_text_prefix") + questionId);
            textArea.setText(responseText);
            HtmlSelect select = (HtmlSelect) getContent().getOneHtmlElementByAttribute("textarea", "name",
                        config.getProperty("appeals_response.response_score_") + questionId);
            select.setSelectedAttribute(score, true);
            HtmlAnchor saveChanges = (HtmlAnchor) getContent().getOneHtmlElementByAttribute("a", "href",
                "javascript:toggleDisplay('shortQ_" + questionId + "');toggleDisplay('longQ_" + questionId + "');");
            this.currentPage = (HtmlPage) saveChanges.click();
        }
    }

    /**
     * <p>Updates the appeals responses with specified response text and review item score. The method assumes that the
     * <code>Review Scorecard</code> page is currently opened.</p>
     *
     * @param responseText a <code>String</code> providing the new appeals response text.</p>
     * @param score a <code>String</code> providing the new review item score.
     * @param commentType a <code>String</code> providing the comment type.
     * @throws Exception if an unexpected error occurs.
     */
    public void updateReview(String responseText, String score, String commentType) throws Exception {
        List textAreas = getContent().getHtmlElementsByTagName("textarea");
        for (int i = 0; i < textAreas.size(); i++) {
            HtmlTextArea textArea = (HtmlTextArea) textAreas.get(i);
            if (textArea.getNameAttribute().startsWith(config.getProperty("review.response_text_prefix"))) {
                textArea.setText(responseText);
            }
        }
        List selects = getContent().getHtmlElementsByTagName("select");
        for (int i = 0; i < selects.size(); i++) {
            HtmlSelect select = (HtmlSelect) selects.get(i);
            if (select.getNameAttribute().startsWith(config.getProperty("review.comment_type_prefix"))) {
                select.setSelectedAttribute(commentType, true);
            } else if (select.getNameAttribute().startsWith(config.getProperty("review.response_score_"))) {
                select.setSelectedAttribute(score, true);
            }
        }
        HtmlAnchor submitButton = findLinkWithImage("bttn_save_and_mark_complete.gif");
        this.currentPage = (HtmlPage) submitButton.click();
    }

    /**
     * <p>Submits the review with specified response text and review item score. The method assumes that the
     * <code>Review Scorecard</code> page is currently opened.</p>
     *
     * @param responseText a <code>String</code> providing the new appeals response text.</p>
     * @param score a <code>String</code> providing the new review item score.
     * @param commentType a <code>String</code> providing the comment type.
     * @param complete <code>true</code> if the scorecard must be commited; <code>false</code> otherwise.
     * @param full <code>true</code> if the scorecard must be filled fully; <code>false</code> otherwise.
     * @throws Exception if an unexpected error occurs.
     */
    public void submitReview(String responseText, String score, String commentType, boolean complete, boolean full)
        throws Exception {
        int limit = 0;
        if (!full) {
            limit = 3;
        }
        List textAreas = getContent().getHtmlElementsByTagName("textarea");
        for (int i = 0; i < textAreas.size() - limit; i++) {
            HtmlTextArea textArea = (HtmlTextArea) textAreas.get(i);
            if (textArea.getNameAttribute().startsWith(config.getProperty("review.response_text_prefix"))) {
                textArea.setText(responseText);
            }
        }
        List selects = getContent().getHtmlElementsByTagName("select");
        for (int i = 0; i < selects.size() - limit; i++) {
            HtmlSelect select = (HtmlSelect) selects.get(i);
            if (select.getNameAttribute().startsWith(config.getProperty("review.comment_type_prefix"))) {
                select.setSelectedAttribute(commentType, true);
            } else if (select.getNameAttribute().startsWith(config.getProperty("review.response_score_"))) {
                select.setSelectedAttribute(score, true);
            }
        }
        HtmlAnchor submitButton;
        if (complete) {
            submitButton = findLinkWithImage("bttn_save_and_mark_complete.gif");
        } else {
            submitButton = findLinkWithImage("bttn_save_and_finish_later.gif");
        }
        this.currentPage = (HtmlPage) submitButton.click();
    }

    /**
     * <p>Submits the appeals with specified appeal text. The method assumes that the <code>Review Scorecard</code> page
     * is currently opened.</p>
     *
     * @param appealText a <code>String</code> providing the appeals text.</p>
     * @param errorMessage a <code>String</code> providing the error message which is expected to be displayed once the
     *        "Submit Appeal" button is pressed.
     * @throws Exception if an unexpected error occurs.
     */
    public void submitAppeal(String appealText, String errorMessage) throws Exception {
        List appealButtons = findLinksWithImage("bttn_appeal.gif");
        for (int i = 0; i < appealButtons.size(); i++) {
            HtmlAnchor appealButton = (HtmlAnchor) appealButtons.get(i);
            click(appealButton);
        }
        List textAreas = getContent().getHtmlElementsByTagName("textarea");
        for (int i = 0; i < textAreas.size(); i++) {
            HtmlTextArea textArea = (HtmlTextArea) textAreas.get(i);
            if (textArea.getNameAttribute().startsWith(config.getProperty("review.appeal_text_prefix_"))) {
                textArea.setText(appealText);
            }
        }
        List submitAppealButtons = findLinksWithImage("bttn_submit_appeal.gif");
        for (int i = 0; i < submitAppealButtons.size(); i++) {
            HtmlAnchor appealButton = (HtmlAnchor) submitAppealButtons.get(i);
            click(appealButton);
            if (errorMessage != null) {
                 Assert.assertTrue(getCurrentPage().asText().indexOf(errorMessage) > 0);
            }
        }
    }

    /**
     * <p>Submits the aggregation review with specified comment text. The method assumes that the <code>Aggregation
     * Review</code> page is currently opened.</p>
     *
     * @param commentText a <code>String</code> providing the aggregation review comment text.</p>
     * @param commit <code>true</code> if the scorecard must be apoproved; <code>false</code> otherwise.
     * @param full <code>true</code> if the scorecard must be filled fully; <code>false</code> otherwise.
     * @throws Exception if an unexpected error occurs.
     */
    public void submitAggregationReview(String commentText, boolean commit, boolean full) throws Exception {
        int limit = 0;
        if (!full) {
            limit = 3;
        }
        List addCommentButtons = findLinksWithImage("bttn_add_comment.gif");
        for (int i = 0; i < addCommentButtons.size(); i++) {
            HtmlAnchor appealButton = (HtmlAnchor) addCommentButtons.get(i);
            click(appealButton);
        }
        List textAreas = getContent().getHtmlElementsByTagName("textarea");
        for (int i = 0; i < textAreas.size(); i++) {
            HtmlTextArea textArea = (HtmlTextArea) textAreas.get(i);
            if (textArea.getNameAttribute().startsWith(config.getProperty("aggregation_review.comment_text_prefix_"))) {
                textArea.setText(commentText);
            }
        }
        if (commit) {
            List boxes = getContent().getHtmlElementsByAttribute("input", "type", "checkbox");
            for (int i = 0; i < boxes.size(); i++) {
                HtmlCheckBoxInput approveBox = (HtmlCheckBoxInput) boxes.get(i);
                if (approveBox.getNameAttribute().equals(config.getProperty("aggregation_review.approve_box"))) {
                    approveBox.setChecked(true);
                }
            }
        }
        HtmlAnchor acceptButton = findLinkWithImage("bttn_accept.gif");
        click(acceptButton);
    }

    /**
     * <p>Submits the aggregation review with specified comment text. The method assumes that the <code>Aggregation
     * Review</code> page is currently opened.</p>
     *
     * @param commentText a <code>String</code> providing the aggregation review comment text.</p>
     * @param commit <code>true</code> if the scorecard must be apoproved; <code>false</code> otherwise.
     * @param full <code>true</code> if the scorecard must be filled fully; <code>false</code> otherwise.
     * @throws Exception if an unexpected error occurs.
     */
    public void submitAggregationReview(String commentText, String arValue, boolean approve, String approveComment,
                                        boolean commit, boolean full, boolean preview) throws Exception {
        int limit = 0;
        if (!full) {
            limit = 3;
        }
        List textAreas = getContent().getHtmlElementsByTagName("textarea");
        for (int i = 0; i < textAreas.size() - limit; i++) {
            HtmlTextArea textArea = (HtmlTextArea) textAreas.get(i);
            if (textArea.getNameAttribute().startsWith(config.getProperty("aggregation_review_by_reviewer.comment_text_prefix_"))) {
                textArea.setText(commentText);
            } else if (textArea.getNameAttribute().startsWith(config.getProperty("aggregation_review_by_reviewer.approve_text"))) {
                textArea.setText(approveComment);
            }
        }
        List radInputs = getContent().getHtmlElementsByAttribute("input", "type", "radio");
        for (int i = 0; i < radInputs.size() - limit; i++) {
            HtmlRadioButtonInput ar = (HtmlRadioButtonInput) textAreas.get(i);
            if (ar.getNameAttribute().startsWith(config.getProperty("aggregation_review_by_reviewer.AR_prefix"))) {
                if (ar.getValueAttribute().equals(arValue)) {
                    ar.setChecked(true);
                }
            }
        }
        if (approve) {
            List boxes = getContent().getHtmlElementsByAttribute("input", "type", "checkbox");
            for (int i = 0; i < boxes.size(); i++) {
                HtmlCheckBoxInput approveBox = (HtmlCheckBoxInput) boxes.get(i);
                if (approveBox.getNameAttribute().equals(config.getProperty("aggregation_review.approve_box"))) {
                    approveBox.setChecked(true);
                }
            }
        }
        HtmlAnchor link;
        if (preview) {
            link = findLinkWithImage("bttn_preview.gif");
            assert (link != null) : "The [Preview] button is not displayed";
            this.currentPage = (HtmlPage) link.click();
        }
        if (commit) {
            link = findLinkWithImage("bttn_save_and_mark_complete.gif");
            assert (link != null) : "The [Save and Mark Complete] button is not displayed";
        } else {
            link = findLinkWithImage("bttn_save_for_later.gif");
            assert (link != null) : "The [Save for Later] button is not displayed";
        }
        this.currentPage = (HtmlPage) link.click();
    }

    /**
     * <p>Submits the aggregation review with specified comment text. The method assumes that the <code>Aggregation
     * Review</code> page is currently opened.</p>
     *
     * @param commentText a <code>String</code> providing the aggregation review comment text.</p>
     * @param commit <code>true</code> if the scorecard must be apoproved; <code>false</code> otherwise.
     * @param full <code>true</code> if the scorecard must be filled fully; <code>false</code> otherwise.
     * @throws Exception if an unexpected error occurs.
     */
    public void submitAggregation(String commentText, String radValue, String responseType, boolean commit,
                                  boolean full, boolean preview)
        throws Exception {
        int limit = 0;
        if (!full) {
            limit = 3;
        }
        List addResponseButtons = findLinksWithImage("bttn_add_response.gif");
        for (int i = 0; i < addResponseButtons.size(); i++) {
            HtmlAnchor appealButton = (HtmlAnchor) addResponseButtons.get(i);
            click(appealButton);
        }
        List textAreas = getContent().getHtmlElementsByTagName("textarea");
        for (int i = 0; i < textAreas.size(); i++) {
            HtmlTextArea textArea = (HtmlTextArea) textAreas.get(i);
            if (textArea.getNameAttribute().startsWith(config.getProperty("aggregation.comment_text_prefix_"))) {
                textArea.setText(commentText);
            }
        }
        List radInputs = getContent().getHtmlElementsByAttribute("input", "type", "radio");
        for (int i = 0; i < radInputs.size(); i++) {
            HtmlRadioButtonInput rad = (HtmlRadioButtonInput) textAreas.get(i);
            if (rad.getNameAttribute().startsWith(config.getProperty("aggregation.RAD_prefix"))) {
                if (rad.getValueAttribute().equals(radValue)) {
                    rad.setChecked(true);
                }
            }
        }
        List selects = getContent().getHtmlElementsByTagName("select");
        for (int i = 0; i < selects.size(); i++) {
            HtmlSelect select = (HtmlSelect) textAreas.get(i);
            if (select.getNameAttribute().startsWith(config.getProperty("aggregation.response_type_prefix"))) {
                select.setSelectedAttribute(responseType, true);
            }
        }
        HtmlAnchor link;
        if (preview) {
            link = findLinkWithImage("bttn_preview.gif");
            assert (link != null) : "The [Preview] button is not displayed";
            this.currentPage = (HtmlPage) link.click();
        }
        if (commit) {
            link = findLinkWithImage("bttn_save_and_mark_complete.gif");
            assert (link != null) : "The [Save and Mark Complete] button is not displayed";
        } else {
            link = findLinkWithImage("bttn_save_and_finish_later.gif");
            assert (link != null) : "The [Save and Finish Later] button is not displayed";
        }
        this.currentPage = (HtmlPage) link.click();

    }

    /**
     * <p>Gets the textual presentation of the specified HTML element which is expected to be provided by the content of
     * the current page. Only a single occurrence of the specified element is expected to be provided. Otherwise an
     * assertion error is raised.</p>
     *
     * @param elementName a <code>String</code> providing the name of desired HTML element.
     * @return a <code>String</code> providing the textual presentation of the specified element.
     * @see HtmlElement#asText()
     */
    public String getText(String elementName) {
        List elements = this.currentPage.getDocumentElement().getHtmlElementsByTagName(elementName);
        assert (elements != null) && (elements.size() == 1) : "A single <" + elementName + "> is expected to be "
                                                              + "provided by page content";
        HtmlElement element = (HtmlElement) elements.get(0);
        return element.asText();
    }

    /**
     * <p>Gets the DIV element providing the main page content. According to existing prototype the main content of the
     * page is placed within a DIV element with ID attribute set to <code>mainMiddleContent</code>.</p>
     *
     * @return an <code>HtmlElement</code> representing a DIV element containing the main page content.
     */
    public HtmlElement getContent() {
        HtmlElement div = this.currentPage.getDocumentElement().getHtmlElementById("mainMiddleContent");
        assert (div != null) : "The page does not contain DIV element with ID 'mainMiddleContent'";
        return div;
    }

    /**
     * <p>Finds the link with specified image which is expected to be rendered by the current page. If there are
     * multiple links matching this condition then the first one is returned.</p>
     *
     * @param imageFile a <code>String</code> providing the name of the image file which is expected to be wrapped by
     *        link.
     * @return an <code>HtmlAnchor</code> containing the specified image or <code>null</code> if no such link exists.
     */
    public HtmlAnchor findLinkWithImage(String imageFile) {
        List anchors = this.currentPage.getAnchors();
        for (int i = 0; i < anchors.size(); i++) {
            HtmlAnchor anchor = (HtmlAnchor) anchors.get(i);
            List images = anchor.getHtmlElementsByTagName("img");
            for (int j = 0; j < images.size(); j++) {
                HtmlElement image = (HtmlElement) images.get(j);
                if (image.isAttributeDefined("src")) {
                    if (image.getAttributeValue("src").endsWith(imageFile)) {
                        return anchor;
                    }
                }
            }
        }
        return null;
    }

    /**
     * <p>Finds the links with specified image which is expected to be rendered by the current page. If there are
     * multiple links matching this condition then the first one is returned.</p>
     *
     * @param imageFile a <code>String</code> providing the name of the image file which is expected to be wrapped by
     *        link.
     * @return a <code>List</code> of an <code>HtmlAnchor</code> containing the specified image or <code>null</code> if
     *         no such links exist.
     */
    public List findLinksWithImage(String imageFile) {
        List result = new ArrayList();
        List anchors = this.currentPage.getAnchors();
        for (int i = 0; i < anchors.size(); i++) {
            HtmlAnchor anchor = (HtmlAnchor) anchors.get(i);
            List images = anchor.getHtmlElementsByTagName("img");
            for (int j = 0; j < images.size(); j++) {
                HtmlElement image = (HtmlElement) images.get(j);
                if (image.isAttributeDefined("src")) {
                    if (image.getAttributeValue("src").endsWith(imageFile)) {
                        result.add(anchor);
                    }
                }
            }
        }
        if (result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    /**
     * <p>Gets the screening result for the specified submission. The method assumes that "Submission/Screening" tab is
     * currently opened.</p>
     *
     * @param submissionId a <code>String</code> providing the ID of requested submission.
     * @return a <code>String</code> providing the current screening result for specified submission.
     * @throws Exception if an unexpected error occurs.
     */
    public String getScreeningResult(String submissionId) throws Exception {
        HtmlTableRow row = getScreeningTableRow(submissionId);
        HtmlTableCell resultCell = row.getCell(5);
        return resultCell.asText();
    }

    /**
     * <p>Gets the link pointing to screening scorecard for the specified submission. The method assumes that
     * "Submission/Screening" tab is currently opened.</p>
     *
     * @param submissionId a <code>String</code> providing the ID of requested submission.
     * @return a <code>HtmlAnchor</code> providing the link to screening scorecard for specified submission.
     * @throws Exception if an unexpected error occurs.
     */
    public HtmlAnchor getScreeningScorecardLink(String submissionId) throws Exception {
        HtmlTableRow row = getScreeningTableRow(submissionId);
        HtmlTableCell scoreCell = row.getCell(4);
        List scoreLinks = scoreCell.getHtmlElementsByTagName("a");
        assert (scoreLinks != null) && (scoreLinks.size() == 1) : "The 'Score' column must provide exactly 1 link";
        return (HtmlAnchor) scoreLinks.get(0);
    }

    /**
     * <p>Gets the HTML table row providing the screening details for the specified submission. The method assumes that
     * "Submission/Screening" tab is currently opened.</p>
     *
     * @param submissionId a <code>String</code> providing the ID of requested submission.
     * @return a <code>HtmlTableRow</code> providing the cell containing the screening details for specified submission.
     * @throws Exception if an unexpected error occurs.
     */
    private HtmlTableRow getScreeningTableRow(String submissionId) throws Exception {
        HtmlElement div = findElement("div:id:sc2", false);
        assert (div != null) : "The page does not contatin a <DIV> element with ID set to 'sc2'";
        List tables = div.getHtmlElementsByTagName("table");
        assert (tables != null) && (tables.size() == 1) : "The page does not contain a single <TABLE> element within " +
                                                          "<DIV ID='sc2'> element";
        HtmlTable table =(HtmlTable) tables.get(0);
        assert (table != null) && (table.getRowCount() >= 2) : "The <TABLE> element does not provide at least 2 rows";
        List rows = table.getRows();
        for (int i = 2; i < rows.size() - 1; i++) {
            HtmlTableRow row = (HtmlTableRow) rows.get(i);
            HtmlTableCell submissionCell = row.getCell(0);
            List links = submissionCell.getHtmlElementsByAttribute("a", "title", "Download Submission");
            assert (links != null) && (links.size() == 1) : "The cell does not contain a link for submission";
            HtmlAnchor link = (HtmlAnchor) links.get(0);
            if (link.asText().trim().equals(submissionId)) {
                return row;
            }
        }
        Assert.fail("The Submission/Screening tab page does not list submission [" + submissionId + "]");
        return null;
    }

    /**
     * <p>Opens the <code>Project Details</code> page for selected prohect.</p>
     *
     * @param project a <code>String</code> providing the name and version of the project.
     * @throws Exception if an unexpected error occurs.
     */
    public void openProjectDetails(String project) throws Exception {
        openAllProjectsPage();
        navigateTo(project);
    }

    /**
     * <p>Opens the <code>Edit Project Details</code> page for selected prohect.</p>
     *
     * @param project a <code>String</code> providing the name and version of the project.
     * @throws Exception if an unexpected error occurs.
     */
    public void openEditProjectDetails(String project) throws Exception {
        openProjectDetails(project);
        HtmlAnchor editProjectLink = findLinkWithImage("bttn_edit_project.gif");
        click(editProjectLink);
    }

    /**
     * <p>Opens the <code>Registrations</code> tab for selected project.</p>
     *
     * @param project a <code>String</code> providing the name and version of the project.
     * @throws Exception if an unexpected error occurs.
     */
    public void openRegistrationsTab(String project) throws Exception {
        openProjectDetails(project);
        navigateTo("Registration");
    }

    /**
     * <p>Finds the page section which is named as specified.</p>
     *
     * @param sectionHeader a <code>String</code> providing the section header.
     * @return an <code>HtmlTable</code> corresponding to specified page section.
     * @throws Exception if an unexpected error occurs.
     */
    public HtmlTable findPageSectionTable(String sectionHeader) throws Exception {
        List tds = getContent().getHtmlElementsByAttribute("td", "class", "title");
        for (int i = 0; i < tds.size(); i++) {
            HtmlTableCell cell = (HtmlTableCell) tds.get(i);
            if (cell.asText().trim().startsWith(sectionHeader)) {
                return cell.getEnclosingRow().getEnclosingTable();
            }
        }
        Assert.fail("The page does not provide [" + sectionHeader + "] section");
        return null;
    }

    /**
     * <p>Finds all page sections.</p>
     *
     * @return a <code>Map</code> mapping the <code>String</code> table header to  <code>HtmlTable</code> corresponding
     *         to specified page section.
     * @throws Exception if an unexpected error occurs.
     */
    public Map findAllPageSectionTables() throws Exception {
        Map result = new LinkedHashMap();
        List tds = getContent().getHtmlElementsByAttribute("td", "class", "title");
        for (int i = 0; i < tds.size(); i++) {
            HtmlTableCell cell = (HtmlTableCell) tds.get(i);
            HtmlTable table = cell.getEnclosingRow().getEnclosingTable();
            if ("scorecard".equals(table.getClassAttribute())) {
                result.put(cell.asText().trim(), table);
            }
        }
        return result;
    }

    /**
     * <p>Sets the timeline notification checkbox with specified state and saves the changes. The method assumes that
     * <code>Edit Project Details</code> page is currently opened.</p>
     *
     * @param flag <code>true</code> to set the timeline notification flag; <code>false</code> otherwise.
     */
    public void setTimelineNotification(boolean flag) throws Exception {
        List boxes = getContent().getHtmlElementsByAttribute("input", "name",
                                                             config.getProperty("edit_project.timeline_notification"));
//        System.out.println("TTT : " + config.getProperty("edit_project.timeline_notification"));
        HtmlCheckBoxInput checkbox = (HtmlCheckBoxInput) boxes.get(0);
        checkbox.setDefaultChecked(flag);
        HtmlImageInput submitButton
            = (HtmlImageInput) getContent().getHtmlElementsByAttribute("input", "src", "../i/bttn_save.gif").get(0);
        click(submitButton);
    }

    /**
     * <p>Gets the state of timeline notification checkbox. The method assumes that <code>Edit Project Details</code>
     * page is currently opened.</p>
     *
     * @return <code>true</code> if the checkbox is currently checked; <code>false</code> otherwise.
     */
    public boolean getTimelineNotification() throws Exception {
        List boxes = getContent().getHtmlElementsByAttribute("input", "name",
                                                             config.getProperty("edit_project.timeline_notification"));
        HtmlCheckBoxInput checkbox = (HtmlCheckBoxInput) boxes.get(0);
        return checkbox.isDefaultChecked();
    }

    /**
     * <p>Removes the specified submission. The method assumes that <code>Submission/Screening</code> tab of the project
     * details page is currently opened.</p>
     *
     * @param submissionId a <code>String</code> providing the submission ID.
     */
    public void removeSubmission(String submissionId) throws Exception {
        HtmlTable submissionsSection = findPageSectionTable("Submission/Screening");
        for (int i = 0; i < submissionsSection.getRowCount() - 1; i++) {
//            System.out.println("Cell = " + submissionsSection.getCellAt(i, 0).asText().trim());
//            System.out.println("Cell2 = " + submissionsSection.getCellAt(i, 1).asXml().trim());
            if (submissionsSection.getCellAt(i, 0).asText().trim().startsWith(submissionId + " (")) {
                List links = submissionsSection.getCellAt(i, 1).getHtmlElementsByTagName("a");
                HtmlAnchor trashLink = (HtmlAnchor) links.get(0);
                click(trashLink);
            }
        }
    }

    /**
     * <p>Verifies if the specified submission is currently displayed. The method assumes that <code>
     * Submission/Screening</code> tab of the project details page is currently opened.</p>
     *
     * @param submissionId a <code>String</code> providing the submission ID.
     */
    public boolean isSubmissionDisplayed(String submissionId) throws Exception {
        HtmlTable submissionsSection = findPageSectionTable("Submission/Screening");
        for (int i = 2; i < submissionsSection.getRowCount() - 1; i++) {
            if (submissionsSection.getCellAt(i, 0).asText().trim().startsWith(submissionId + " (")) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>Downloads the final fix for the currently opened project. The method assumes that <code>Final Fix/Review
     * </code> tab of the project details page is currently opened.</p>
     *
     * @return a <code>String</code> providing the content type for the downloaded final fix.
     */
    public String downloadFinalFix() throws Exception {
        HtmlTable finalFixSection = findPageSectionTable("Final Review / Fix");
        HtmlTableCell cell = finalFixSection.getCellAt(2, 3);
        List links = cell.getHtmlElementsByTagName("a");
        HtmlAnchor downloadLink = (HtmlAnchor) links.get(0);
        WebResponse response = downloadLink.click().getWebResponse();
        String contentType = response.getContentType();
        InputStream is = response.getContentAsStream();
        while (is.read() != -1) {
        }
        return contentType;
    }

    /**
     * <p>Downloads the test cases of specified type for the currently opened project. The method assumes that <code>
     * Review/Appeals</code> tab of the project details page is currently opened.</p>
     *
     * @return a <code>String</code> providing the content type for the downloaded test cases.
     */
    public String downloadTestCases(String testCaseType) throws Exception {
        HtmlTable reviewSection = findPageSectionTable("Review");
        for (int i = 4; i < 7; i++) {
            if (reviewSection.getCellAt(1, i).asText().trim().startsWith(testCaseType + ":")) {
                HtmlTableCell cell = reviewSection.getCellAt(1, i);
                List links = cell.getHtmlElementsByTagName("a");
                HtmlAnchor downloadLink = (HtmlAnchor) links.get(0);
                WebResponse response = downloadLink.click().getWebResponse();
                String contentType = response.getContentType();
                InputStream is = response.getContentAsStream();
                while (is.read() != -1) {
                }
                return contentType;
            }
        }
        Assert.fail("The test cases of type [" + testCaseType + "] are not provided");
        return null;
    }

    /**
     * <p>Sends a message to a manager of a project currently opened for view.</p>
     *
     * @param category a <code>Strng</code> providing the category of a message.
     * @param message a <code>String</code> providing the message text.
     */
    public void contactManager(String category, String message) throws Exception {
        HtmlTable resourcesSection = findPageSectionTable("Resources");
        for (int i = 2; i < resourcesSection.getRowCount(); i++) {
            if (resourcesSection.getCellAt(i, 0).asText().trim().equals("Manager")) {
                HtmlAnchor link = (HtmlAnchor) resourcesSection.getCellAt(i, 1).getHtmlElementsByTagName("a").get(0);
                click(link);
                setInput(config.getProperty("contact.category"), category, false);
                setInput(config.getProperty("contact.message"), message, false);
                HtmlAnchor submitButton = findLinkWithImage("bttn_submit.gif");
                click(submitButton);
            }
        }
    }

    /**
     * <p>Uploads the specified test case on reviewer's behalf. The method assumes that the project is in <code>Review
     * </code> phase and currently <code>Review/Appeals</code> tab for selected project is opened.</p>
     *
     * @param file a <code>String</code> providing the name of the file with test cases to upload.
     * @throws Exception if an unexpected error occurs.
     */
    public void uploadTestCases(String file) throws Exception {
        HtmlTable reviewSection = findPageSectionTable("Review");
        List links = reviewSection.getHtmlElementsByAttribute("a", "title", "Upload New Test Case");
        HtmlAnchor link = (HtmlAnchor) links.get(0);
        click(link); // Should open upload test cases page
        HtmlTable uploadSection = findPageSectionTable("Upload Test Case");
        List inputs = uploadSection.getHtmlElementsByAttribute("input", "type", "text");
        HtmlTextInput fileInput = (HtmlTextInput) inputs.get(0);
        fileInput.setValueAttribute(file);
        HtmlAnchor submitButton = findLinkWithImage("bttn_upload.gif");
        click(submitButton);
    }

    /**
     * <p>Uploads the specified final fix on submitter's behalf. The method assumes that the project is in <code>Final
     * Fix</code> phase and currently <code>Final Fix/Review</code> tab for selected project is opened.</p>
     *
     * @param file a <code>String</code> providing the name of the file with final fix to upload.
     * @throws Exception if an unexpected error occurs.
     */
    public void uploadFinalFix(String file) throws Exception {
        HtmlTable reviewSection = findPageSectionTable("Final Fix / Review");
        HtmlTableCell cell = reviewSection.getCellAt(2, 3);
        List links = cell.getHtmlElementsByTagName("a");
        HtmlAnchor link = (HtmlAnchor) links.get(0);
        click(link); // Should open upload final fix page
        HtmlTable uploadSection = findPageSectionTable("Upload Final Fix");
        List inputs = uploadSection.getHtmlElementsByAttribute("input", "type", "text");
        HtmlTextInput fileInput = (HtmlTextInput) inputs.get(0);
        fileInput.setValueAttribute(file);
        HtmlAnchor submitButton = findLinkWithImage("bttn_upload.gif");
        click(submitButton);
    }

    /**
     * <p>Uploads the specified submission on submitter's behalf. The method assumes that the project is in <code>
     * Submission</code> phase and currently <code>Submission/Screening</code> tab for selected project is opened.</p>
     *
     * @param file a <code>String</code> providing the name of the file with final fix to upload.
     * @throws Exception if an unexpected error occurs.
     */
    public void uploadSubmission(String file) throws Exception {
        HtmlTable reviewSection = findPageSectionTable("My Role");
        HtmlTableCell cell = reviewSection.getCellAt(1, 1);
        List links = cell.getHtmlElementsByTagName("a");
        HtmlAnchor link = (HtmlAnchor) links.get(0);
        click(link); // Should open upload submission page
        HtmlTable uploadSection = findPageSectionTable("Upload Submission");
        List inputs = uploadSection.getHtmlElementsByAttribute("input", "type", "text");
        HtmlTextInput fileInput = (HtmlTextInput) inputs.get(0);
        fileInput.setValueAttribute(file);
        HtmlAnchor submitButton = findLinkWithImage("bttn_upload.gif");
        click(submitButton);
    }

    /**
     * <p>Gets the name for the existing file with test cases for the specified user.</p>
     *
     * @param user a <code>String</code> providing the username.
     * @return a <code>String</code> providing the path to test cases file.
     */
    public static String getTestCaseFile(String user) {
        return config.getProperty("login." + user + ".test_case");
    }

    /**
     * <p>Gets the name for the existing file with final fix for the specified user.</p>
     *
     * @param user a <code>String</code> providing the username.
     * @return a <code>String</code> providing the path to final fix file.
     */
    public static String getFinalFixFile(String user) {
        return config.getProperty("login." + user + ".final_fix");
    }

    /**
     * <p>Gets the name for the existing file with submission for the specified user.</p>
     *
     * @param user a <code>String</code> providing the username.
     * @return a <code>String</code> providing the path to submission file.
     */
    public static String getSubmissionFile(String user) {
        return config.getProperty("login." + user + ".submission");
    }

    /**
     * <p>Sets the project notes. The method assumes the <code>Edit Project Details</code> page to be currently
     * displayed.</p>
     *
     * @param notes a <code>String</code> providing the new project notes.
     */
    public void setProjectNotes(String notes) throws Exception {
        HtmlTextArea textArea
            = (HtmlTextArea) getContent().getHtmlElementsByAttribute("textarea", "name",
                                                                     config.getProperty("edit_project.notes")).get(0);
        textArea.setText(notes);
    }

    /**
     * <p>Selects the specified auto-pilot option. The method assumes the <code>Edit Project Details</code> page to be
     * currently displayed.</p>
     *
     * @param option an index of auto-pilot option to select.
     */
    public void setAutoPilot(int option) throws Exception {
        HtmlTable prefsSection = findPageSectionTable("Preferences");
        HtmlTableCell cell = prefsSection.getCellAt(1, 1);
        ((HtmlRadioButtonInput) cell.getHtmlElementsByTagName("input").get(option)).setChecked(true);
    }

    /**
     * <p>Selects the project unrated option. The method assumes the <code>Edit Project Details</code> page to be
     * currently displayed.</p>
     *
     * @param selected <code>true</code> if option must be selected; <code>false</code> otherwise.
     */
    public void setProjectUnrated(boolean selected) throws Exception {
        HtmlTable prefsSection = findPageSectionTable("Preferences");
        HtmlTableCell cell = prefsSection.getCellAt(2, 0);
        ((HtmlCheckBoxInput) cell.getHtmlElementsByTagName("input").get(1)).setChecked(true);
    }

    /**
     * <p>Selects the payment required option. The method assumes the <code>Edit Project Details</code> page to be
     * currently displayed.</p>
     *
     * @param selected <code>true</code> if option must be selected; <code>false</code> otherwise.
     */
    public void setPaymentRequired(boolean selected) {
    }

    /**
     * <p>Sets the project update explanation notes. The method assumes the <code>Edit Project Details</code> page to be
     * currently displayed.</p>
     *
     * @param explanation a <code>String</code> providing the project update explanation.
     */
    public void setProjectUpdateExplanation(String explanation) throws Exception {
        HtmlTextArea textArea
            = (HtmlTextArea) getContent().getHtmlElementsByAttribute("textarea", "name",
                                                                     config.getProperty("edit_project.explanation")).get(0);
        textArea.setText(explanation);
    }

    /**
     * <p>Clicks <code>Save Changes</code> button. The method assumes the <code>Edit Project Details</code> page to be
     * currently displayed.</p>
     */
    public void saveProjectChanges() throws Exception {
/*
        HtmlAnchor saveChangesButton = findLinkWithImage("bttn_save_changes.gif");
        click(saveChangesButton);
*/
        HtmlImageInput submitButton
            = (HtmlImageInput) getContent().getHtmlElementsByAttribute("input", "src", "../i/bttn_save.gif").get(0);
        click(submitButton);
    }

    /**
     * <p>Sets the screening scorecard. The method assumes the <code>Edit Project Details</code> page to be currently
     * displayed.</p>
     *
     * @param id the ID of scorecard to set.
     */
    public void setScreeningScorecard(String id) throws Exception {
        HtmlTable detailsSection = findPageSectionTable("Project Details");
        HtmlTableCell cell = detailsSection.getCellAt(4, 1);
        ((HtmlSelect) cell.getHtmlElementsByTagName("select").get(0)).setSelectedAttribute(id, true);
    }

    /**
     * <p>Sets the review scorecard. The method assumes the <code>Edit Project Details</code> page to be currently
     * displayed.</p>
     *
     * @param id the ID of scorecard to set.
     */
    public void setReviewScorecard(String id) throws Exception {
        HtmlTable detailsSection = findPageSectionTable("Project Details");
        HtmlTableCell cell = detailsSection.getCellAt(5, 1);
        ((HtmlSelect) cell.getHtmlElementsByTagName("select").get(0)).setSelectedAttribute(id, true);
    }

    /**
     * <p>Sets the approval scorecard. The method assumes the <code>Edit Project Details</code> page to be currently
     * displayed.</p>
     *
     * @param id the ID of scorecard to set.
     */
    public void setApprovalScorecard(String id) throws Exception {
        HtmlTable detailsSection = findPageSectionTable("Project Details");
        HtmlTableCell cell = detailsSection.getCellAt(6, 1);
        ((HtmlSelect) cell.getHtmlElementsByTagName("select").get(0)).setSelectedAttribute(id, true);
    }

    /**
     * <p>Sets the fixed start date for specified phase. The method assumes the <code>Edit Project Details</code> page
     * to be currently displayed.</p>
     *
     * @param time the time to set.
     */
    public void setPhaseFixedStartDate(String phase, String time) throws Exception {
        HtmlTable timelineSection = findPageSectionTable("Edit Timeline");
        HtmlTableRow phaseRow = findPhaseRow(timelineSection, phase);
        HtmlTableCell cell = phaseRow.getCell(2);
        ((HtmlTextInput) cell.getHtmlElementsByTagName("input").get(1)).setValueAttribute(time);
    }

    /**
     * <p>Adds new phase to project. The method assumes the <code>Edit Project Details</code> page to be currently
     * displayed.</p>
     */
    public void addNewProjectPhase(String phaseType, String placement, String phase2, String startTime, String endTime,
                                   String duration) throws Exception {
//        HtmlTable timelineSection = findPageSectionTable("Add a New Phase");
        HtmlTable timelineSection = (HtmlTable) getContent().getHtmlElementById("addphase_tbl");
        HtmlTableCell cell = timelineSection.getCellAt(1, 0);
        List selects = cell.getHtmlElementsByTagName("select");
        findOptionByText((HtmlSelect) selects.get(0), phaseType).setSelected(true);
        findOptionByText((HtmlSelect) selects.get(1), placement).setSelected(true);
        findOptionByText((HtmlSelect) selects.get(2), phase2).setSelected(true);

        cell = timelineSection.getCellAt(1, 1);

        cell = timelineSection.getCellAt(1, 3);
        ((HtmlTextInput) cell.getHtmlElementsByTagName("input").get(0)).setValueAttribute(duration);

        cell = timelineSection.getCellAt(1, 4);
        click((HtmlAnchor) cell.getHtmlElementsByTagName("a").get(0));
    }

    /**
     * <p>Deletes specified phase. The method assumes the <code>Edit Project Details</code> page to be currently
     * displayed.</p>
     *
     * @param phase a <code>String</code> providing the phase to delete.
     * @param confirm <code>true</code> if phase deletion must be confirmed; <code>false</code> otherwise.
     */
    public void deletePhase(String phase, boolean confirm) throws Exception {
        setConfirmStatus(confirm);
        HtmlTable timelineSection = findPageSectionTable("Edit Timeline");
        HtmlTableRow phaseRow = findPhaseRow(timelineSection, phase);
        HtmlTableCell cell = phaseRow.getCell(5);
        click((HtmlAnchor) cell.getHtmlElementsByTagName("a").get(0));
    }

    public void setRequiredRegistrations(int i) {
    }

    public void setRequiredSubmissions(int i) {
    }

    /**
     * <p>Updates the specified phase. The method assumes the <code>Edit Project Details</code> page to be currently
     * displayed.</p>
     *
     * @param phase a <code>String</code> providing the phase to delete.
     * @param duration a <code>String</code> providing new duration.
     */
    public void updatePhase(String phase, String startTime, String endTime, String duration) throws Exception {
        HtmlTable timelineSection = findPageSectionTable("Edit Timeline");
        HtmlTableRow phaseRow = findPhaseRow(timelineSection, phase);

        HtmlTableCell cell = phaseRow.getCell(2);

        cell = phaseRow.getCell(3);

        cell = phaseRow.getCell(4);
        ((HtmlTextInput) cell.getHtmlElementsByTagName("input").get(0)).setValueAttribute(duration);
    }

    /**
     * <p>Updates duration for specified phase. The method assumes the <code>Edit Project Details</code> page to be
     * currently displayed.</p>
     *
     * @param phase a <code>String</code> providing the phase to delete.
     * @param duration a <code>String</code> providing new duration.
     */
    public void setPhaseDuration(String phase, String duration) throws Exception {
        HtmlTable timelineSection = findPageSectionTable("Edit Timeline");
        HtmlTableRow phaseRow = findPhaseRow(timelineSection, phase);

        HtmlTableCell cell = phaseRow.getCell(4);
        ((HtmlTextInput) cell.getHtmlElementsByTagName("input").get(0)).setValueAttribute(duration);
    }

    /**
     * <p>Updates the payments for resources. The method assumes the <code>Edit Project Details</code> page to be
     * currently displayed.</p>
     *
     * @param role a <code>String</code> providing the role for resource.
     * @param payment a <code>String</code> providing the new payment to set.
     * @param status a <code>String</code> providing the new payment status to set.
     */
    public void setPayment(String role, String payment, String status) throws Exception {
        HtmlTable resourcesSection = findPageSectionTable("Resources");
        for (int i = 3; i < resourcesSection.getRowCount(); i++) {
            HtmlSelect roles = (HtmlSelect) resourcesSection.getCellAt(i, 0).getHtmlElementsByTagName("select").get(0);
            HtmlOption option = findOptionByText(roles, role);
            if (option != null) {
                ((HtmlTextInput) resourcesSection.getCellAt(i, 2).getHtmlElementsByTagName("input").get(0))
                    .setValueAttribute(payment);
                HtmlSelect statusSelect
                    = (HtmlSelect) resourcesSection.getCellAt(i, 3).getHtmlElementsByTagName("select").get(0);
                HtmlOption option2 = findOptionByText(statusSelect, status);
                statusSelect.setSelectedAttribute(option2, true);
            }
        }
    }

    /**
     * <p>Updates the roles for resources. The method assumes the <code>Edit Project Details</code> page to be
     * currently displayed.</p>
     *
     * @param oldRole a <code>String</code> providing the role to update.
     * @param newRole a <code>String</code> providing the new role to set.
     */
    public void updateRole(String oldRole, String newRole) throws Exception {
        HtmlTable resourcesSection = findPageSectionTable("Resources");
        for (int i = 3; i < resourcesSection.getRowCount(); i++) {
            HtmlSelect roles = (HtmlSelect) resourcesSection.getCellAt(i, 0).getHtmlElementsByTagName("select").get(0);
            HtmlOption option = findOptionByText(roles, oldRole);
            if (option != null) {
                HtmlOption option2 = findOptionByText(roles, newRole);
                roles.setSelectedAttribute(option2, true);
            }
        }
    }

    /**
     * <p>Updates the handle for resources. The method assumes the <code>Edit Project Details</code> page to be
     * currently displayed.</p>
     *
     * @param role a <code>String</code> providing the role identifying the resource.
     * @param newName a <code>String</code> providing the new handle to set.
     */
    public void setResourceName(String role, String newName) throws Exception {
        HtmlTable resourcesSection = findPageSectionTable("Resources");
        for (int i = 3; i < resourcesSection.getRowCount(); i++) {
            HtmlSelect roles = (HtmlSelect) resourcesSection.getCellAt(i, 0).getHtmlElementsByTagName("select").get(0);
            HtmlOption option = findOptionByText(roles, role);
            if (option != null) {
                ((HtmlTextInput) resourcesSection.getCellAt(i, 1).getHtmlElementsByTagName("input").get(0))
                    .setValueAttribute(newName);
            }
        }
    }

    /**
     * <p>Adds new resource to the project. The method assumes the <code>Edit Project Details</code> page to be
     * currently displayed.</p>
     *
     * @param role a <code>String</code> specifying the role to be assigned to new resource.
     * @param handle a <code>String</code> providing the handle for new resource.
     */
    public void addResource(String role, String handle) throws Exception {
        HtmlTable resourcesSection = findPageSectionTable("Resources");
        HtmlSelect roles = (HtmlSelect) resourcesSection.getCellAt(2, 0).getHtmlElementsByTagName("select").get(0);
        HtmlOption option = findOptionByText(roles, role);
        option.setSelected(true);
        HtmlTextInput handleInput
            = (HtmlTextInput) resourcesSection.getCellAt(2, 1).getHtmlElementsByTagName("input").get(0);
        handleInput.setValueAttribute(handle);
        HtmlAnchor addButton = (HtmlAnchor) resourcesSection.getCellAt(2, 4).getHtmlElementsByTagName("a").get(0);
        click(addButton);
    }

    /**
     * <p>Advances the project to specified phase. The method assumes the <code>Edit Project Details</code> page to be
     * currently displayed.</p>
     *
     * @param nextPhase a <code>String</code> providing the name of the phase.
     */
    public void advanceToPhase(String nextPhase) throws Exception {
        HtmlTable timelinesSection = findPageSectionTable("Edit Timeline");
        HtmlTableRow row = findPhaseRow(timelinesSection, nextPhase);
        HtmlRadioButtonInput radio = (HtmlRadioButtonInput) row.getCell(0).getHtmlElementsByTagName("input").get(0);
        radio.setChecked(true);
    }

    /**
     * <p>Sets the project status. The method assumes the <code>Edit Project Details</code> page to be currently
     * displayed.</p>
     *
     * @param status a <code>String</code> providing the new project status.
     */
    public void setProjectStatus(String status) throws Exception {
        HtmlSelect select = (HtmlSelect) findElement(config.getProperty("edit_project.notes"), false);
        HtmlOption option = findOptionByText(select, status);
        if (option != null) {
            option.setSelected(true);
        }
    }

    /**
     * <p>Sends payments. The method assumes the <code>Project Status Changed</code> page to be currently displayed.</p>
     */
    public void sendPayments() throws Exception {
        click(findLinkWithImage("bttn_send_payments.gif"));
    }

    /**
     * <p>Opens <code>Create Project</code> page.</p>
     */
    public void openCreateProjectPage() throws Exception {
        login();
        clickCreateProject();
    }

    /**
     * <p>Creates new project with specified data. The method assumes that <code>Create Project</code> page is
     * displayed.</p>
     */
    public void createProject(String name, String type, String category, String eligibility, boolean publicity,
                              int autoPilot, boolean emailNotify, boolean notRate, boolean timelineNotify,
                              String screeningScorecardId, String reviewScorecardId, String approvalScorecardId,
                              String forumName, String svnLink, String notes, String role,
                              String handle, String payment, String paymentStatus) throws Exception {
        HtmlTable pdSection = findPageSectionTable("Project Details");
        ((HtmlTextInput) pdSection.getHtmlElementsByAttribute("input", "type", "text").get(0)).setValueAttribute(name);
        List selects = pdSection.getHtmlElementsByTagName("select");
        findOptionByText((HtmlSelect) selects.get(0), type).setSelected(true);
        findOptionByText((HtmlSelect) selects.get(1), category).setSelected(true);
        findOptionByText((HtmlSelect) selects.get(2), eligibility).setSelected(true);
        ((HtmlCheckBoxInput) pdSection.getHtmlElementsByAttribute("input", "type", "checkbox").get(0))
            .setChecked(publicity);

        HtmlTable prefsSection = findPageSectionTable("Preferences");
        HtmlTableCell cell = prefsSection.getCellAt(1, 1);
        ((HtmlRadioButtonInput) cell.getHtmlElementsByTagName("input").get(autoPilot)).setChecked(true);
        List boxes = prefsSection.getHtmlElementsByAttribute("input", "type", "checkbox");
        ((HtmlCheckBoxInput) boxes.get(0)).setChecked(emailNotify);
        ((HtmlCheckBoxInput) boxes.get(1)).setChecked(notRate);
        ((HtmlCheckBoxInput) boxes.get(2)).setChecked(timelineNotify);

        HtmlTable scorecardsSection = findPageSectionTable("scorecards");
        List scorecards = scorecardsSection.getHtmlElementsByTagName("select");
        ((HtmlSelect) scorecards.get(0)).setSelectedAttribute(screeningScorecardId, true);
        ((HtmlSelect) scorecards.get(1)).setSelectedAttribute(reviewScorecardId, true);
        ((HtmlSelect) scorecards.get(2)).setSelectedAttribute(approvalScorecardId, true);

        HtmlTable refsSection = findPageSectionTable("References");
        List inputs = refsSection.getHtmlElementsByTagName("input");
        ((HtmlTextInput) inputs.get(0)).setValueAttribute(forumName);
        ((HtmlTextInput) inputs.get(1)).setValueAttribute(svnLink);

        HtmlTable notesSection = findPageSectionTable("Notes");
        ((HtmlTextArea) notesSection.getHtmlElementsByTagName("textarea").get(0)).setText(notes);

        HtmlTable resourcesSection = findPageSectionTable("Resources");
        List drops = resourcesSection.getHtmlElementsByTagName("select");
        HtmlOption option = findOptionByText((HtmlSelect) drops.get(0), role);
        ((HtmlSelect) drops.get(0)).setSelectedAttribute(option, true);
        option = findOptionByText((HtmlSelect) drops.get(1), paymentStatus);
        ((HtmlSelect) drops.get(1)).setSelectedAttribute(option, true);
        List inputs2 = resourcesSection.getHtmlElementsByAttribute("input", "type", "text");
        ((HtmlTextInput) inputs2.get(0)).setValueAttribute(handle);
        ((HtmlTextInput) inputs2.get(1)).setValueAttribute(payment);
        click((HtmlAnchor) resourcesSection.getHtmlElementsByTagName("a").get(0));

        HtmlAnchor saveButton = findLinkWithImage("bttn_save.gif");
        click(saveButton);
    }

    /**
     * <p>Loads the specified phase template. The method assumes that <code>Create Project</code> page is displayed.</p>
     *
     * @param templateName a <code>String</code> providing the template name.
     */
    public void loadTimelineTemplate(String templateName) throws Exception {
        HtmlTable createTimelineSection = findPageSectionTable("Create Timeline");
        HtmlSelect select = (HtmlSelect) createTimelineSection.getHtmlElementsByTagName("select").get(0);
        HtmlOption option = findOptionByText(select, templateName);
        select.setSelectedAttribute(option, true);
        click((HtmlAnchor) createTimelineSection.getHtmlElementsByTagName("a").get(0));
    }

    /**
     * <p>Finds the option matching the specified text.</p>
     *
     * @param select a <code>HtmlSelect</code> providing the drop-down list.
     * @param optionText a <code>String</code> providing the option text to find.
     * @return an <code>HtmlOption</code> matching the specified text or <code>null</code>.
     */
    public HtmlOption findOptionByText(HtmlSelect select, String optionText) {
        for (int i = 0; i < select.getOptionSize(); i++) {
            HtmlOption option = select.getOption(i);
            if (option.asText().trim().equals(optionText)) {
                return option;
            }
        }
        return null;
    }

    /**
     * <p>Finds the table row matching the specified phase.</p>
     *
     * @param timelinesSection a <code>HtmlTable</code> representing <code>Edit Timeline</code> section.
     * @param nextPhase a <code>String</code> providing the phase.
     * @return an <code>HtmlTableRow</code> matching the specified phase.
     */
    public HtmlTableRow findPhaseRow(HtmlTable timelinesSection, String nextPhase) throws Exception {
        for (int i = 0; i < timelinesSection.getRowCount(); i++) {
            HtmlTableCell cell = timelinesSection.getCellAt(i, 1);
            if (cell.asText().trim().equals(nextPhase)) {
                return timelinesSection.getRow(i);
            }
        }
        return null;
    }
}
