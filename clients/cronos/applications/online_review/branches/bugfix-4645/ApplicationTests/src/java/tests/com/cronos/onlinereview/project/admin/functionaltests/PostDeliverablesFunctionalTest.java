/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import com.cronos.onlinereview.project.AbstractTestCase;
import com.cronos.onlinereview.project.Project;
import com.cronos.onlinereview.project.UserSimulator;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import junit.framework.Assert;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>A test case for <code>Post Deliverables</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 * @test-status Passed
 * @test-date   10/16/2006
 */
public class PostDeliverablesFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data for all active projects assigned to
     * users.</p>
     */
    public static final String PROJECTS_TEST_DATA_FILE_NAME = "data/initial/DataSet.xml";

    /**
     * <p>Scenario #198</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User scrolls through all phases of the Project Timeline
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * System has posted outstanding user deliverables. </p>
     */
    public void testScenario198() throws Exception {
    }

    /**
     * <p>Scenario #199</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User scrolls through all phases of the Project Timeline
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * System has posted outstanding project deliverables. </p>
     */
    public void testScenario199() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openSubmissionScreeningTab(project.getName() + " version " + project.getVersion());
            assertOutstandingDeliverablesDisplayed(true);
            this.user.openReviewAppealsTab(project.getName() + " version " + project.getVersion());
            assertOutstandingDeliverablesDisplayed(true);
            this.user.openFinalFixReviewTab(project.getName() + " version " + project.getVersion());
            assertOutstandingDeliverablesDisplayed(true);
            this.user.openAggregationReviewTab(project.getName() + " version " + project.getVersion());
            assertOutstandingDeliverablesDisplayed(true);
            this.user.openApprovalTab(project.getName() + " version " + project.getVersion());
            assertOutstandingDeliverablesDisplayed(true);
        }
    }

    /**
     * <p>Scenario #200</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User scrolls through all phases of the Project Timeline
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * System has not posted outstanding user deliverables. </p>
     */
    public void testScenario200() throws Exception {
    }

    /**
     * <p>Scenario #201</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User scrolls through all phases of the Project Timeline
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * System has not posted outstanding project deliverables. </p>
     */
    public void testScenario201() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openSubmissionScreeningTab(project.getName() + " version " + project.getVersion());
            assertOutstandingDeliverablesDisplayed(false);
            this.user.openReviewAppealsTab(project.getName() + " version " + project.getVersion());
            assertOutstandingDeliverablesDisplayed(false);
            this.user.openFinalFixReviewTab(project.getName() + " version " + project.getVersion());
            assertOutstandingDeliverablesDisplayed(false);
            this.user.openAggregationReviewTab(project.getName() + " version " + project.getVersion());
            assertOutstandingDeliverablesDisplayed(false);
            this.user.openApprovalTab(project.getName() + " version " + project.getVersion());
            assertOutstandingDeliverablesDisplayed(false);
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
        return new IDataSet[] {getActiveProjectsData()};
    }

    /**
     * <p>Gets the data set providing the initial data for active projects.</p>
     *
     * @return an <code>IDataSet</code> providing details for active projects.
     * @throws Exception if an unexpected error occurs.
     */
    protected static IDataSet getActiveProjectsData() throws Exception {
        InputStream projectDataStream
            = ViewMyProjectFunctionalTest.class.getClassLoader().getResourceAsStream(PROJECTS_TEST_DATA_FILE_NAME);
        return new FlatXmlDataSet(projectDataStream);
    }

    private void assertOutstandingDeliverablesDisplayed(boolean flag) throws Exception {
        HtmlTable section = this.user.findPageSectionTable("My Role");
        System.out.println(section.asXml());
        HtmlTableCell cell = section.getCellAt(0, 2);
        Assert.assertEquals("Project outstanding deliverables are not displayed",
                            "Outstanding Deliverables", cell.asText().trim());
        cell = section.getCellAt(1, 2);
        if (flag) {
            Assert.assertFalse("Project outstanding deliverables are not displayed",
                              cell.asText().trim().length() == 0);
        } else {
            Assert.assertEquals("Project outstanding deliverables are displayed",
                              "There are no outstanding deliverables.", cell.asText().trim());
        }
    }
}
