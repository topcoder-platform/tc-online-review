/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import junit.framework.Assert;
import com.cronos.onlinereview.project.AbstractTestCase;
import com.cronos.onlinereview.project.UserSimulator;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import org.dbunit.dataset.IDataSet;

/**
 * <p>A test case for <code>View SVN Link</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 * @test-status Passed
 * @test-date   10/16/2006
 */
public class ViewSVNLinkFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the sample project (as displayed by the list of projects) which is
     * used for testing purposes.</p>
     */
    public static final String PROJECT_NAME = "Ajax Timed Survey version 1.0";

    /**
     * <p>A <code>String</code> providing the names of users authorized to view the SVN link for project.</p>
     */
    private String[] AUTHORIZED_USERS
        = new String[] {UserSimulator.MANAGER, UserSimulator.PRIMARY_REVIEWER, UserSimulator.OBSERVER};

    /**
     * <p>Scenario #75</p>
     * <pre>
     * Note: User is logged-in as a "Manager", "Aggregators", "Final Reviewer", or "Observer"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * User is able to view "SVN Link" </p>
     */
    public void testScenario75() throws Exception {
        for (int i = 0; i < AUTHORIZED_USERS.length; i++) {
            setUser(AUTHORIZED_USERS[i]);
            this.user.openProjectDetails(PROJECT_NAME);
            HtmlTable table = this.user.findPageSectionTable("Project Details");
            boolean found = false;
            for (int j = 0; j < table.getRowCount(); j++) {
                String cellText = table.getCellAt(j, 0).asText().trim();
                if (cellText.equals("SVN Module:")) {
                    found = true;
                    String svnUrl = table.getCellAt(j, 1).asText().trim();
                    Assert.assertEquals("Incorrect URL for SVN module is displayed",
                                        "http://svn.acme.org/ajaxtimedsurvey/trunk", svnUrl);
                }
            }
            if (!found) {
                Assert.fail("The SVN link is not displayed");
            }
        }
    }

    /**
     * <p>Gets the data sets specific to test case which must be used to populate the database tables with initial data.
     * </p>
     *
     * @return an <code>IDataSet</code> array providing the data sets specific to test case.
     * @throws Exception if an unexpected error occurs.
     */
    protected IDataSet[] getDataSets() throws Exception {
        return new IDataSet[] {getRegistrationPhaseData(), getSubmissionPhaseData(), getScreeningPhaseData(),
            getReviewPhaseData(), getAppealsPhaseData(), getAppealsResponsePhaseData(), getAggregationPhaseData(),
            getAggregationReviewPhaseData(), getFinalFixPhaseData(), getFinalReviewPhaseData(), getApprovalPhaseData()};
    }
}
