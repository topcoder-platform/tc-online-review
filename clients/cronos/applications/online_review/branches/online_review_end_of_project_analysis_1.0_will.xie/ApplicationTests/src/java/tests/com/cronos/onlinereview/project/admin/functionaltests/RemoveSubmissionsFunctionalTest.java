/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import com.cronos.onlinereview.project.AbstractTestCase;
import com.cronos.onlinereview.project.Messages;
import com.cronos.onlinereview.project.Project;
import com.cronos.onlinereview.project.UserSimulator;
import junit.framework.Assert;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>A test case for <code>Remove  Submissions</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 * @test-status Failed
 * @test-date   10/16/2006
 */
public class RemoveSubmissionsFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> array providing the usernames for users who are not granted the permission to remove the
     * submissions for selected project.</p>
     */
    public static final String[] UNAUTHORIZED_USERS
        = new String[] {UserSimulator.PRIMARY_REVIEWER, UserSimulator.DESIGNER, UserSimulator.WINNING_SUBMITTER,
                        UserSimulator.SECOND_PLACE_SUBMITTER, UserSimulator.THIRD_PLACE_SUBMITTER,
                        UserSimulator.REVIEWER1, UserSimulator.REVIEWER2, UserSimulator.OBSERVER};

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data for all active projects assigned to
     * users.</p>
     */
    public static final String PROJECTS_TEST_DATA_FILE_NAME = "data/initial/DataSet.xml";

    /**
     * <p>Scenario #88</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Submissions/Screening" tab
     * 4.  User selects "Submission" for Removal
     * 5.  User clicks on "Remove"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating User does NOT have permission to delete submission. </p>
     */
    public void testScenario88() throws Exception {
        Map projects = Project.loadAllProjects(getActiveProjectsData());

        for (int i = 0; i < UNAUTHORIZED_USERS.length; i++) {
            setUser(UNAUTHORIZED_USERS[i]);
            this.user.setConfirmStatus(true);
            Iterator iterator = projects.entrySet().iterator();
            Map.Entry entry;
            while (iterator.hasNext()) {
                entry = (Map.Entry) iterator.next();
                Project project = (Project) entry.getValue();
                this.user.openSubmissionScreeningTab(project.getName() + " version " + project.getVersion());
                Map submissions = project.getSubmissions();
                Iterator iterator2 = submissions.entrySet().iterator();
                Map.Entry entry1;
                while (iterator2.hasNext()) {
                    entry1 = (Map.Entry) iterator2.next();
                    this.user.removeSubmission((String) entry1.getKey());
                    assertDisplayedMessage(Messages.getNoPermissionRemoveSubmission());
                }
            }
        }
    }

    /**
     * <p>Scenario #89</p>
     * <pre>
     * Note: User is logged-in as a "Manager"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Submissions/Screening" tab
     * 4.  User selects "Submission" for Removal
     * 5.  User clicks on "Remove"
     * 6.  User is prompted to confirm Removal of Submission
     * 7.  User confirms Removal
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Submission is removed from Submission List. </p>
     */
    public void testScenario89() throws Exception {
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        setUser(UserSimulator.MANAGER);
        this.user.setConfirmStatus(true);
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openSubmissionScreeningTab(project.getName() + " version " + project.getVersion());
            Map submissions = project.getSubmissions();
            Iterator iterator2 = submissions.entrySet().iterator();
            Map.Entry entry1;
            while (iterator2.hasNext()) {
                entry1 = (Map.Entry) iterator2.next();
                this.user.removeSubmission((String) entry1.getKey());
                this.user.openSubmissionScreeningTab(project.getName() + " version " + project.getVersion());
                Assert.assertFalse("The removed submission [" + (String) entry1.getKey() + "] is displayed",
                                   this.user.isSubmissionDisplayed((String) entry1.getKey()));
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
}
