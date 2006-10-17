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
 * <p>A test case for <code>View Most Recent Submissions After Appeals Response</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 * @test-status Failed
 * @test-date   10/16/2006
 */
public class ViewMostRecentSubmissionsafterAppealsResponseFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data for all active projects assigned to
     * users.</p>
     */
    public static final String PROJECTS_TEST_DATA_FILE_NAME = "data/initial/DataSet.xml";

    public static final String[] USERS = new String[] {UserSimulator.PRIMARY_REVIEWER, UserSimulator.REVIEWER1,
        UserSimulator.REVIEWER2, UserSimulator.OBSERVER, UserSimulator.WINNING_SUBMITTER,
        UserSimulator.SECOND_PLACE_SUBMITTER, UserSimulator.THIRD_PLACE_SUBMITTER};

    /**
     * <p>Scenario #87</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * User is able to view the list of "Most Recent Submissions".  Submission are sorted by Submission ID. </p>
     */
    public void testScenario87() throws Exception {
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        for (int i = 0; i < USERS.length; i++) {
            setUser(USERS[i]);
            Iterator iterator = projects.values().iterator();
            Project project;
            while (iterator.hasNext()) {
                project = (Project) iterator.next();
                this.user.openReviewAppealsTab(project.getName() + " version " + project.getVersion());
                Map submissions = project.getSubmissions();
                Iterator iterator2 = submissions.entrySet().iterator();
                Map.Entry entry;
                while (iterator2.hasNext()) {
                    entry = (Map.Entry) iterator2.next();
                    assertSubmissionDisplayed((Map) entry.getValue());
                }
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
            = ViewMyProjectFunctionalTest.class.getClassLoader().getResourceAsStream(PROJECTS_TEST_DATA_FILE_NAME);
        return new FlatXmlDataSet(projectDataStream);
    }

    /**
     * <p>Verifies that all submissions associated with the specified resource are currently displayed.</p>
     *
     * @param submission a <code>Map</code> providing the details for the project submissions.
     */
    private void assertSubmissionDisplayed(Map submission) throws Exception {
        HtmlTable submissionsSection = this.user.findPageSectionTable("Review");
        String submissionId = (String) submission.get("submission_id");
        boolean submissionDisplayed = false;
        for (int i = 0; i < submissionsSection.getRowCount(); i++) {
            if (submissionsSection.getCellAt(i, 0).asText().trim().startsWith(submissionId + " (")) {
                submissionDisplayed = true;
            }
        }
        if ("5".equals(submission.get("submission_status_id"))) {
            Assert.assertTrue("The deleted submission [" + submissionId + "] is displayed", submissionDisplayed);
        } else {
            Assert.assertTrue("The submission [" + submissionId + "] is not displayed", submissionDisplayed);
        }
    }
}
