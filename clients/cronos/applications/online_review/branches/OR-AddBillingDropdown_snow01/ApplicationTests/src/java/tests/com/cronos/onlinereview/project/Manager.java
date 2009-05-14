/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project;

import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

import java.util.List;

/**
 * <p></p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class Manager extends UserSimulator {

    /**
     * <p>Constructs new <code>Manager</code> instance.
     *
     * @param user
     */
    public Manager(String user) {
        super(user);
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

}
