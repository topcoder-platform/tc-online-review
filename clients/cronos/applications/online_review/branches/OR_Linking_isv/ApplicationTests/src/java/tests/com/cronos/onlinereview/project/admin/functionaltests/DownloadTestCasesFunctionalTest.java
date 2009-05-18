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
 * <p>A test case for <code>Download Test Cases</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 * @test-status Failed
 * @test-date   10/16/2006
 */
public class DownloadTestCasesFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data for all active projects assigned to
     * users.</p>
     */
    public static final String PROJECTS_TEST_DATA_FILE_NAME = "data/initial/DataSet.xml";

    /**
     * <p>A <code>String</code> array providing the usernames for users who are not granted the permission to download
     * the final fix for selected project.</p>
     */
    public static final String[] UNAUTHORIZED_USERS
        = new String[] {UserSimulator.PRIMARY_REVIEWER, UserSimulator.DESIGNER, UserSimulator.WINNING_SUBMITTER,
                        UserSimulator.SECOND_PLACE_SUBMITTER, UserSimulator.THIRD_PLACE_SUBMITTER,
                        UserSimulator.REVIEWER1, UserSimulator.REVIEWER2, UserSimulator.OBSERVER};

    /**
     * <p>A <code>String</code> array providing the usernames for users who are not granted the permission to download
     * the final fix for selected project.</p>
     */
    public static final String[] AUTHORIZED_USERS
        = new String[] {UserSimulator.PRIMARY_REVIEWER, UserSimulator.DESIGNER, UserSimulator.WINNING_SUBMITTER,
                        UserSimulator.SECOND_PLACE_SUBMITTER, UserSimulator.THIRD_PLACE_SUBMITTER,
                        UserSimulator.REVIEWER1, UserSimulator.REVIEWER2, UserSimulator.OBSERVER};

    /**
     * <p>Scenario #202</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  User clicks on Test Case for "Stress:"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating user does not have permission to download selected test case. </p>
     */
    public void testScenario202() throws Exception {
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        for (int i = 0; i < UNAUTHORIZED_USERS.length; i++) {
            setUser(UNAUTHORIZED_USERS[i]);
            Iterator iterator = projects.entrySet().iterator();
            Map.Entry entry;
            while (iterator.hasNext()) {
                entry = (Map.Entry) iterator.next();
                Project project = (Project) entry.getValue();
                this.user.openReviewAppealsTab(project.getName() + " version " + project.getVersion());
                this.user.downloadTestCases("Stress");
                assertDisplayedMessage(Messages.getNoPermissionDownloadTestCases());
            }
        }
    }

    /**
     * <p>Scenario #203</p>
     * <pre>
     * Note: User is logged-in as one of the following: Manager, Observer, Submitter, Reviewer
     * and Approver
     *
     *
     *
     * Test Scenario Details
     *
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review/Appeals" tab
     * 4.  User clicks on Test Case for "Stress:"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Test Case is downloaded to users' machine. </p>
     */
    public void testScenario203() throws Exception {
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        for (int i = 0; i < AUTHORIZED_USERS.length; i++) {
            setUser(AUTHORIZED_USERS[i]);
            Iterator iterator = projects.entrySet().iterator();
            Map.Entry entry;
            while (iterator.hasNext()) {
                entry = (Map.Entry) iterator.next();
                Project project = (Project) entry.getValue();
                this.user.openReviewAppealsTab(project.getName() + " version " + project.getVersion());
                String contentType = this.user.downloadTestCases("Stress");
                Assert.assertEquals("The final fix is not downloaded correctly",
                                    "application/x-java-archive", contentType);
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
