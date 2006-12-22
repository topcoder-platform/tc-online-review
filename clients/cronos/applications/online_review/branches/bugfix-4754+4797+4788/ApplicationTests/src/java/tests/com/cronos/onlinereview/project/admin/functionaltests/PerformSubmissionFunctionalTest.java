/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import com.cronos.onlinereview.project.AbstractTestCase;
import com.cronos.onlinereview.project.Project;
import com.cronos.onlinereview.project.UserSimulator;

import java.io.InputStream;
import java.util.Map;
import java.util.Iterator;

import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.IDataSet;

/**
 * <p>A test case for <code>Perform Submission</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class PerformSubmissionFunctionalTest extends AbstractTestCase {

    // TODO : Tests will require inidividual files

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data for all active projects assigned to
     * users.</p>
     */
    public static final String PROJECTS_TEST_DATA_FILE_NAME = "data/initial/DataSet.xml";

    /**
     * <p>A <code>String</code> array providing the usernames for users who are granted the permission to upload the
     * test cases for selected project.</p>
     */
    public static final String[] SUBMITTERS
        = new String[] {UserSimulator.WINNING_SUBMITTER, UserSimulator.SECOND_PLACE_SUBMITTER,
                        UserSimulator.THIRD_PLACE_SUBMITTER};

    /**
     * <p>Scenario #80</p>
     * <pre>
     * Note: User is logged-in as a "Submitter"
     * 1.  User clicks on "My Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Browse" and browses for file to upload
     * 4.  User selects file to upload
     * 5.  User clicks on "Upload"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Document is submitted and uploaded, automatic screening results are displayed after submission, and a system wide
     * unique ID is generated for submission. </p>
     */
    public void testScenario80() throws Exception {
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        for (int i = 0; i < SUBMITTERS.length; i++) {
            setUser(SUBMITTERS[i]);
            Iterator iterator = projects.entrySet().iterator();
            Map.Entry entry;
            while (iterator.hasNext()) {
                entry = (Map.Entry) iterator.next();
                Project project = (Project) entry.getValue();
                this.user.openSubmissionScreeningTab(project.getName() + " version " + project.getVersion());
                this.user.uploadSubmission(UserSimulator.getSubmissionFile(SUBMITTERS[i]));
            }
        }
        assertData("data/expected/SubmissionsUploaded.xml", "The submissions are not uploaded correctly");
    }

    /**
     * <p>Scenario #81</p>
     * <pre>
     * Note: User is logged-in as a "Submitter" and submission phase is set to require manual
     * screening.
     * 1.  User clicks on "My Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Browse" and browses for file to upload
     * 4.  User selects file to upload
     * 5.  User clicks on "Upload"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Document is submitted and uploaded, manual screening occurs and results are displayed to user, and a system wide
     * unique ID is generated for submission. </p>
     */
    public void testScenario81() throws Exception {
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        for (int i = 0; i < SUBMITTERS.length; i++) {
            setUser(SUBMITTERS[i]);
            Iterator iterator = projects.entrySet().iterator();
            Map.Entry entry;
            while (iterator.hasNext()) {
                entry = (Map.Entry) iterator.next();
                Project project = (Project) entry.getValue();
                this.user.openSubmissionScreeningTab(project.getName() + " version " + project.getVersion());
                this.user.uploadSubmission(UserSimulator.getSubmissionFile(SUBMITTERS[i]));
            }
        }
        assertData("data/expected/SubmissionsUploaded.xml", "The submissions are not uploaded correctly");
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
}
