/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import com.cronos.onlinereview.project.AbstractTestCase;
import com.cronos.onlinereview.project.Project;
import com.cronos.onlinereview.project.UserSimulator;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import junit.framework.Assert;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>A test case for <code>View All Submissions</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 * @test-status Failed
 * @test-date   10/16/2006
 */
public class ViewAllSubmissionsFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data for all active projects assigned to
     * users.</p>
     */
    public static final String PROJECTS_TEST_DATA_FILE_NAME = "data/initial/DataSet.xml";

    /**
     * <p>Scenario #82</p>
     * <pre>
     * Note: User is logged-in as a "Manager"
     * 1.  User clicks on "All Open Projects" tab
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * User is able to view "All Submissions" for selected project. </p>
     */
    public void testScenario82() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.values().iterator();
        Project project;
        while (iterator.hasNext()) {
            project = (Project) iterator.next();
            this.user.openProjectDetails(project.getName() + " version " + project.getVersion());
            Map submissions = project.getSubmissions();
            Iterator iterator2 = submissions.entrySet().iterator();
            Map.Entry entry;
            while (iterator2.hasNext()) {
                entry = (Map.Entry) iterator2.next();
                assertSubmissionDisplayed((Map) entry.getValue());
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
            = ViewProjectResourcesFunctionalTest.class.getClassLoader().getResourceAsStream(PROJECTS_TEST_DATA_FILE_NAME);
        return new FlatXmlDataSet(projectDataStream);
    }

    /**
     * <p>Verifies that all submissions associated with the specified resource are currently displayed.</p>
     *
     * @param submission a <code>Map</code> providing the details for the project submissions.
     */
    private void assertSubmissionDisplayed(Map submission) throws Exception {
        HtmlTable submissionsSection = this.user.findPageSectionTable("Submission/Screening");
        String submissionId = (String) submission.get("submission_id");
        boolean submissionDisplayed = false;
        for (int i = 2; i < submissionsSection.getRowCount(); i++) {
            if (submissionsSection.getCellAt(i, 0).asText().trim().startsWith(submissionId + " (")) {
                submissionDisplayed = true;
            }
        }
        Assert.assertTrue("The submission [" + submissionId + "] is not displayed", submissionDisplayed);
    }
}
