/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.List;

import junit.framework.Assert;

/**
 * <p></p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class Reviewer extends UserSimulator {

    /**
     * <p>Constructs new <code>Reviewer</code> instance.</p>
     *
     * @param user a <code>String</code> providing the username.
     */
    public Reviewer(String user) {
        super(user);
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
                assert (scoreLinks != null) && (scoreLinks.size() == 1) : "The 'Score' column must provide "
                                                                          + "exactly 1 link";
                this.currentPage = (HtmlPage) ((HtmlAnchor) scoreLinks.get(0)).click();
                return;
            }
        }
        Assert.fail("The page does not provide a link for review results for submission ["
                                    + submissionId + "]");
    }

}
