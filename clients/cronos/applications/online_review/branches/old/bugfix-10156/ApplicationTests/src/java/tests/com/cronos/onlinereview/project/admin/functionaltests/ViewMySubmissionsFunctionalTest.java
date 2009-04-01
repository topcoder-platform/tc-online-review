/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import com.cronos.onlinereview.project.AbstractTestCase;
import com.cronos.onlinereview.project.Project;
import com.cronos.onlinereview.project.Resource;
import com.cronos.onlinereview.project.UserSimulator;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import junit.framework.Assert;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>A test case for <code>View My Submissions</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 * @test-status Failed
 * @test-date   10/16/2006
 */
public class ViewMySubmissionsFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data for all active projects assigned to
     * users.</p>
     */
    public static final String PROJECTS_TEST_DATA_FILE_NAME = "data/initial/DataSet.xml";

    public static final String[] USERS = new String[] {UserSimulator.SECOND_PLACE_SUBMITTER,
        UserSimulator.THIRD_PLACE_SUBMITTER, UserSimulator.WINNING_SUBMITTER};

    /**
     * <p>Scenario #83</p>
     * <pre>
     * Note: User is logged-in as a "Submitter"
     * 1.  User clicks on "My Open Projects" tab
     * 2.  User views project list and selects a project
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * User is able to view "Submissions", past and present, for selected project. </p>
     */
    public void testScenario83() throws Exception {
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        for (int i = 0; i < USERS.length; i++) {
            setUser(USERS[i]);
            Resource[] resources = Resource.loadResourceByExternalId(getActiveProjectsData(), this.user.getId());
            for (int j = 0; j < resources.length; j++) {
                Resource resource = resources[j];
                Project project = (Project) projects.get(resource.getProjectId());
                this.user.openSubmissionScreeningTab(project.getName() + " version " + project.getVersion());
                assertSubmissionsDisplayed(resource);
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
     * @param resource a <code>Resource</code> providing the details for the project resource.
     */
    private void assertSubmissionsDisplayed(Resource resource) throws Exception {
        HtmlTable submissionsSection = this.user.findPageSectionTable("Submission/Screening");
        Map submissions = resource.getSubmissions();
        Iterator iterator = submissions.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            String submissionId = (String) entry.getKey();
            boolean submissionDisplayed = false;
            for (int i = 2; i < submissionsSection.getRowCount(); i++) {
                if (submissionsSection.getCellAt(i, 0).asText().trim().equals(submissionId)) {
                    submissionDisplayed = true;
                }
            }
            Assert.assertTrue("The submission [" + submissionId + "] is not displayed", submissionDisplayed);
        }
    }
}
