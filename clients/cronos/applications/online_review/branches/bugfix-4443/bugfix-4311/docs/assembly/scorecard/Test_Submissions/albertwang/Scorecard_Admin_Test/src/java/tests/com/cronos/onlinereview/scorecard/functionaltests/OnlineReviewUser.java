/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.scorecard.functionaltests;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.ConfirmHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.QuestionType;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.Section;

/**
 * <p>
 * Utility class to imitate a web user of the online review application.
 * It takes advantage of HtmlUnit to interact with the application, and provides various
 * convenience methods for the functional test cases.
 * </p>
 * @author TCSTESTER
 * @version 1.0
 */
public class OnlineReviewUser implements AlertHandler, ConfirmHandler {
    /**
     * The configuration interface.
     */
    private static Configuration config = new Configuration(OnlineReviewUser.class.getName());
    /**
     * The web client with the application, which maintains the session.
     */
    private WebClient client = null;

    /**
     * The current web page.
     */
    private HtmlPage page = null;
    
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
    
    /** Constant indicating a user with all privileges. */
    public static final String USER_SUPER = "user_super";
    /** Constant indicating a user who can not view scorecard list. */
    public static final String USER_CANNOT_VIEW_SCORECARD_LIST = "user_cannot_view_scorecard_list";
    /** Constant indicating a user who can not view scorecard. */
    public static final String USER_CANNOT_VIEW_SCORECARD = "user_cannot_view_scorecard";
    /** Constant indicating a user who can not create scorecard. */
    public static final String USER_CANNOT_CREATE_SCORECARD = "user_cannot_create_scorecard";
    /** Constant indicating a user who can not copy scorecard. */
    public static final String USER_CANNOT_COPY_SCORECARD = "user_cannot_copy_scorecard";
    /** Constant indicating a user who can not edit scorecard. */
    public static final String USER_CANNOT_EDIT_SCORECARD = "user_cannot_edit_scorecard";
    
    /** Username */
    private String username;
    /** Password */
    private String password;
    /**
     * Creates an OnlineReviewUser.
     *
     * @param user the user.
     */
    public OnlineReviewUser(String user) {
        username = config.getProperty("login." + user + ".username");
        password = config.getProperty("login." + user + ".password");
        // Create web client and register handlers.
        client = new WebClient(BrowserSupport.getTargetBrowser());
        client.setJavaScriptEnabled(true);
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
        page = (HtmlPage) client.getPage(new URL(config.getProperty("application_url")));
        
        // Fill username, password and submit the form.
        setInput("login.username_input", username, true);
//        setInput("login.password_input", password, true);

        page = (HtmlPage)((HtmlForm)page.getForms().get(0)).submit();
//        click("login.submit", true);
        // then navigate to scorecard list page
        // TODO
        page = (HtmlPage) client.getPage(new URL("http://172.16.212.64/Online_Review/scorecardAdmin.do?actionName=listScorecards"));
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
     * Click "Create New Scorecard" button on the scorecard list page.
     * </p>
     * @throws Exception if error occurs
     */
    public void clickCreateNewScorecard() throws Exception {
    	this.click("scorecard.create_new_scorecard_button", true);
    }
    /**
     * <p>
     * Click "Edit Scorecard" button on the scorecard page.
     * </p>
     * @throws Exception if error occurs
     */
    public void clickEditScorecard() throws Exception {
    	this.click("scorecard.edit_scorecard_button", true);
    }
    /**
     * <p>
     * Click "Copy Scorecard" button on the scorecard page.
     * </p>
     * @throws Exception if error occurs
     */
    public void clickCopyScorecard() throws Exception {
    	this.click("scorecard.copy_scorecard_button", true);
    }
    /**
     * <p>
     * Click "Save Changes" button on the scorecard edit page.
     * </p>
     * @throws Exception if error occurs
     */
    public void clickSaveChanges() throws Exception {
    	this.click("scorecard.save_scorecard_button", true);
    }
    /**
     * <p>
     * Click "Save New Scorecard" button on the scorecard creation/copy page.
     * </p>
     * @throws Exception if error occurs
     */
    public void clickSaveNewScorecard() throws Exception {
    	this.click("scorecard.save_new_scorecard_button", true);
    }
    /**
     * <p>
     * Return whether the "scorecard_name" input textbox is disabled.
     * </p>
     * @return true if it is disabled, false otherwise
     * @throws Exception if error occurs
     */
    public boolean isScorecardNameInputDisabled() throws Exception {
    	return ((HtmlInput) this.findElement("scorecard.scorecard_name_input", true)).isDisabled();
    }
    /**
     * <p>
     * Return whether the "scorecard_version" input textbox is disabled.
     * </p>
     * @return true if it is disabled, false otherwise
     * @throws Exception if error occurs
     */
    public boolean isScorecardVersionInputDisabled() throws Exception {
    	return ((HtmlInput) this.findElement("scorecard.scorecard_version_input", true)).isDisabled();
    }

    /**
     * <p>
     * Return current page as text.
     * </p>
     * @return current page as text
     */
    public String getPageAsText() {
        return page.asText();
    }
    /**
     * <p>
     * Return current page.
     * </p>
     * @return current page
     */
    public HtmlPage getPage() {
        return page;
    }
    
    /**
     * <p>
     * Filter out the scorecards by given status.
     * </p>
     * @param status the status
     * @throws Exception if any error occurs
     */
    public void filterScorecardListByStatus(String status) throws Exception {
    	this.selectOption("scorecard_list.filter_select", status, true);
    }
    
    /**
     * <p>
     * Extract and return the scorecard list.
     * </p>
     * @return the scorecard list
     * @throws Exception if any error occurs
     */
    public String[][] getScorecardList() throws Exception {
    	HtmlTable table = (HtmlTable) this.findElement("scorecard_list.table.table_name", true);
    	String[][] temp = new String[table.getRowCount()][3];
    	int current = 0;
    	for (int i = 0, rows = table.getRowCount(); i < rows; i++) {
    		HtmlTableRow row = table.getRow(i);
            String style = row.getStyleAttribute();
    		List cells = row.getCells();
    		if (cells.size() == 3 && style.indexOf("display: none") < 0) {
    			if (row.getCell(0).getClassAttribute().equals(config.getProperty("scorecard_list.table.title_td_class"))) {
    				continue;
    			}
    			// the row is a scorecard entry
    			temp[current] = new String[3];
    			temp[current][0] = row.getCell(0).asText();
    			temp[current][1] = row.getCell(1).asText();
    			temp[current][2] = row.getCell(2).asText();
    			current++;
    		}
    	}
    	String[][] ret = new String[current][3];
    	System.arraycopy(temp, 0, ret, 0, current);
    	return ret;
    	
    }
    /**
     * <p>
     * Activate or inactivate a scorecard.
     * </p>
     * @param scorecardName scorecard name
     * @param isActive status
     * @throws Exception if any error occurs
     */
    public void setScorecardStatus(String scorecardName, boolean isActive) throws Exception {
    	HtmlTable table = (HtmlTable) this.findElement("scorecard_list.table.table_name", true);
    	for (int i = 0, rows = table.getRowCount(); i < rows; i++) {
    		HtmlTableRow row = table.getRow(i);
    		List cells = row.getCells();
    		if (cells.size() == 4) {
    			if (row.getCell(0).getClassAttribute().equals(config.getProperty("scorecard_list.table.title_td_class"))) {
    				continue;
    			}
    			// the row is a scorecard entry
    			if (row.getCell(0).asText().trim().equals(scorecardName)) {
    				((HtmlCheckBoxInput) row.getCell(2).getHtmlElementsByTagName("input")).setChecked(isActive);
    				break;
    			}
    		}
    	}
    }
    
    /**
     * <p>
     * Return the status of a scorecard
     * </p>
     * @param scorecardName scorecard name
     * @return the status
     * @throws Exception if any error occurs
     */
    public boolean getScorecardStatus(String scorecardName) throws Exception {
    	HtmlTable table = (HtmlTable) this.findElement("scorecard_list.table.table_name", true);
    	for (int i = 0, rows = table.getRowCount(); i < rows; i++) {
    		HtmlTableRow row = table.getRow(i);
    		List cells = row.getCells();
    		if (cells.size() == 4) {
    			if (row.getCell(0).getClassAttribute().equals(config.getProperty("scorecard_list.table.title_td_class"))) {
    				continue;
    			}
    			// the row is a scorecard entry
    			if (row.getCell(0).asText().trim().equals(scorecardName)) {
    				return ((HtmlCheckBoxInput) row.getCell(2).getHtmlElementsByTagName("input")).isChecked();
    			}
    		}
    	}
    	return false;
    }
    
    /**
     * <p>
     * Extract and return the scorecard summary(scorecard information without group/section/question informations).
     * </p>
     * @return the scorecard summary
     * @throws Exception if any error occurs
     */
    public String[] getScorecardSummary() throws Exception {
    	HtmlTable table = this.findTable("scorecard.scorecard_details");
    	System.out.println(table.getRowCount());
    	HtmlTableRow row = table.getRow(2);
    	String[] ret = new String[8];
    	for (int i = 0; i < ret.length; i++) {
    		ret[i] = row.getCell(i).asText();
    	}
    	return ret;
    }
    /**
     * <p>
     * Create a scorecard through the web form.
     * </p>
     * @param scorecard the scorecard data
     * @throws Exception if any error occurs
     */
    public void createScorecard(Scorecard scorecard) throws Exception {
    	// click the "Create New Scorecard" button
    	this.click("scorecard.create_new_scorecard_button", true);
    	// fill scorecard information
    	this.fillScorecard(scorecard);
    	
    	// initially there's one group displayed at the page, we need add more groups
    	int groupCount = scorecard.getNumberOfGroups();
    	for (int i = 0; i < groupCount - 1; i++) {
    		this.addGroup(i);
    	}
    	Group[] groups = scorecard.getAllGroups();
    	// fill all groups
    	for (int i = 0; i < groupCount; i++) {
    		Group group = groups[i];
    		// fill current group
    		this.fillGroup(i, group);
    		// initially there's one section displayed at the page for this group, we may need add more sections
    		int sectionCount = group.getNumberOfSections();
    		for (int j = 0; j < sectionCount - 1; j++) {
    			this.addSection(i, j);
    		}
    		Section[] sections = group.getAllSections();
    		for (int j = 0; j < group.getNumberOfSections(); j++) {
    			Section section = sections[j];
    			// fill current section
    			this.fillSection(i, j, section);
    			// initially there's one question displayed at the page for this section, we may need add more questions
    			int questionCount = section.getNumberOfQuestions();
    			for (int k = 0; k < questionCount - 1; k++) {
    				this.addQuestion(i, j);
    			}
    			Question[] questions = section.getAllQuestions();
    			// fill all questions
    			for (int k = 0; k < questionCount; k++) {
    				this.fillQuestion(i, j, k, questions[k]);
    			}
    		}
    	}
    	// save
    	this.clickSaveNewScorecard();
    }
    
    /**
     * <p>
     * Fill the scorecard information.
     * </p>
     * @param scorecard the scorecard
     * @throws Exception if any error occurs
     */
    public void fillScorecard(Scorecard scorecard) throws Exception {
    	this.setInput("scorecard.scorecard_name_input", scorecard.getName() == null ? "" : scorecard.getName(), true);
    	this.setInput("scorecard.scorecard_version_input", scorecard.getVersion() == null ? "" : scorecard.getVersion(), true);
    	this.setInput("scorecard.scorecard_min_score_input", scorecard.getMinScore() + "", true);
    	this.setInput("scorecard.scorecard_max_score_input", scorecard.getMaxScore() + "", true);
    	this.selectOption("scorecard.scorecard_category_select", ProjectCategories.getProjectCategory(scorecard.getCategory()), true);
    	this.selectOption("scorecard.scorecard_project_type_select",ProjectCategories.getProjectType(scorecard.getCategory()), true);
    	this.selectOption("scorecard.scorecard_type_select", scorecard.getScorecardType().getName(), true);
    	this.selectOption("scorecard.scorecard_status_select", scorecard.getScorecardStatus().getName(), true);
    }
    /**
     * <p>
     * Fill scorecard name and version only.
     * </p>
     * @param name the name
     * @param version the version
     * @throws Exception if any error occurs
     */
    public void fillScorecardNameAndVersion(String name, String version) throws Exception {
    	this.setInput("scorecard.scorecard_name_input", name == null ? "" : name, true);
    	this.setInput("scorecard.scorecard_version_input", version == null ? "" : version, true);
    }
    /**
     * <p>
     * Fill the question information at a given position.
     * </p>
     * @param g group index
     * @param s section index
     * @param q question index
     * @param question question
     * @throws Exception if any error occurs
     */
    public void fillQuestion(int g, int s, int q, Question question) throws Exception {
    	String locator = config.getProperty("scorecard.question_text_prefix") + g + "_" + s + "_" + q;
    	this.setInput(locator, question.getDescription(), false);
    	locator = config.getProperty("scorecard.question_guideline_prefix") + g + "_" + s + "_" + q;
    	this.setInput(locator, question.getGuideline(), false);
    	locator = config.getProperty("scorecard.question_type_prefix") + g + "_" + s + "_" + q;
    	this.selectOption(locator, question.getQuestionType().getName(), false);
    	locator = config.getProperty("scorecard.question_weight_prefix") + g + "_" + s + "_" + q;
    	this.setInput(locator, question.getWeight() + "", false);
    	locator = config.getProperty("scorecard.question_doc_upload_prefix") + g + "_" + s + "_" + q;
    	if (question.isUploadRequired()) {
    		this.selectOption(locator, "Required", false);
    	} else if (question.isUploadDocument()) {
    		this.selectOption(locator, "Optional", false);
    	} else {
    		this.selectOption(locator, "N/A", false);
    	}
    }
    /**
     * <p>
     * Fill the question information at a given position.
     * </p>
     * @param g group index
     * @param s section index
     * @param q question index
     * @param description description
     * @param guideLine guide line text
     * @param type type
     * @param weight weight
     * @param isUploadDocument isUploadDocument flag
     * @param isUploadRequired isUploadRequired flag
     * @throws Exception if any error occurs
     */
    public void fillQuestion(int g, int s, int q, String description, String guideLine, String type, float weight, boolean isUploadDocument, boolean isUploadRequired) throws Exception {
    	Question question = new Question();
    	question.setDescription(description);
    	question.setGuideline(guideLine);
    	question.setQuestionType(new QuestionType(1, type));
    	question.setWeight(weight);
    	question.setUploadDocument(isUploadDocument);
    	question.setUploadRequired(isUploadRequired);
    }
    
    /**
     * <p>
     * Add a new question input area in the given position.
     * </p>
     * @param g group index
     * @param s section index
     * @throws Exception if any error occurs
     */
    public void addQuestion(int g, int s) throws Exception {
    	String locator = config.getProperty("scorecard.add_question_button_prefix") + g + "_" + s;
    	this.click(locator, false);
    }
    
    /**
     * <p>
     * Fill the section information at a given position.
     * </p>
     * @param g group index
     * @param s section index
     * @param section section
     * @throws Exception if any error occurs
     */
    public void fillSection(int g, int s, Section section) throws Exception {
    	String locator = config.getProperty("scorecard.section_name_prefix") + g + "_" + s;
    	this.setInput(locator, section.getName() == null ? "" : section.getName(), false);
    	locator = config.getProperty("scorecard.section_weight_prefix") + g + "_" + s;
    	this.setInput(locator, section.getWeight() + "", false);
    }
    /**
     * <p>
     * Fill the section information at a given position.
     * </p>
     * @param g group index
     * @param s section index
     * @param name section name
     * @param weight weight
     * @throws Exception if any error occurs
     */
    public void fillSection(int g, int s, String name, float weight) throws Exception {
    	Section section = new Section();
    	section.setName(name);
    	section.setWeight(weight);
    	fillSection(g, s, section);
    }
    
    /**
     * <p>
     * Add a new section input area in the given position.
     * </p>
     * @param g group index
     * @param s section index(the new area will be added after this index)
     * @throws Exception if any error occurs
     */
    public void addSection(int g, int s) throws Exception {
    	String locator = config.getProperty("scorecard.add_section_button_prefix") + g + "_" + s;
    	this.click(locator, false);
    }
    /**
     * <p>
     * Fill the group information at a given position.
     * </p>
     * @param g group index
     * @param group group
     * @throws Exception if any error occurs
     */
    public void fillGroup(int g, Group group) throws Exception {
    	String locator = config.getProperty("scorecard.group_name_prefix") + g;
    	this.setInput(locator, group.getName() == null ? "" : group.getName(), false);
    	locator = config.getProperty("scorecard.group_weight_prefix") + g;
    	this.setInput(locator, group.getWeight() + "", false);
    }
    /**
     * <p>
     * Fill the group information at a given position.
     * </p>
     * @param g group index
     * @param name name
     * @param weight weight
     * @throws Exception if any error occurs
     */
    public void fillGroup(int g, String name, float weight) throws Exception {
    	Group group = new Group();
    	group.setName(name);
    	group.setWeight(weight);
    	fillGroup(g, group);
    }
    /**
     * <p>
     * Add a new section input area in the given position.
     * </p>
     * @param g group index(the new area will be added after this index)
     * @throws Exception if any error occurs
     */
    public void addGroup(int g) throws Exception {
    	String locator = config.getProperty("scorecard.add_group_button_prefix") + g;
    	this.click(locator, false);
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
     * @parma locator the locator for the input.
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
     * @parma locator the locator for the input.
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
        page = (HtmlPage) ((ClickableElement) findElement(locator, resolveFirst)).click();
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
        List tables = page.getDocumentElement().getHtmlElementsByTagName("table");
System.out.println(tables.size());        
        for (int i = 0; i < tables.size(); ++i) {

            HtmlTable table = (HtmlTable) tables.get(i);
System.out.println("ROWS:"+table.getRowCount());
if (table.getRowCount() == 1) {
    System.out.println(table.getRow(0).getCell(0).asXml());
}
            if (table.getRowCount() > 0 && table.getRow(0).getCells().size() > 0 &&
                table.getRow(0).getCell(0).asText().trim().indexOf(summary) > -1) {
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
    private HtmlElement findElement(String locator, boolean resolveFirst) throws Exception {
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
            List elements = page.getDocumentElement().getHtmlElementsByAttribute(comp[0], comp[1], comp[2]);
            return (HtmlElement[]) elements.toArray(new HtmlElement[elements.size()]);
        } else {
            return new HtmlElement[] {page.getDocumentElement().getHtmlElementById(locator)};
        }
    }
}
