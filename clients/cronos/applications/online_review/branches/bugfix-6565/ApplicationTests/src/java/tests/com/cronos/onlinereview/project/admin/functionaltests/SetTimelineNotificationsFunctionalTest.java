/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import com.cronos.onlinereview.project.AbstractTestCase;
import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.Project;

import java.util.Map;
import java.util.Iterator;
import java.io.InputStream;

import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.IDataSet;
import junit.framework.Assert;

/**
 * <p>A test case for <code>Set Timeline Notifications</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 * @test-status Failed
 * @test-date   10/16/2006
 */
public class SetTimelineNotificationsFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data for all active projects assigned to
     * users.</p>
     */
    public static final String PROJECTS_TEST_DATA_FILE_NAME = "data/initial/DataSet.xml";

    /**
     * <p>Constructs a test case with the given name.</p>
     */
    public SetTimelineNotificationsFunctionalTest(String name) {
        super(name);
    }

    /**
     * <p>Scenario #62</p>
     * <pre>
     * Note: "Receive Timeline Notification" box is Unchecked
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User scrolls down to "Preferences" section
     * 5.  User checks the "Receive Timeline Notification" box
     * 6.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Changes to "Preferences" are saved. </p>
     */
    public void testScenario62() throws Exception {
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        setUser(UserSimulator.MANAGER);
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.setTimelineNotification(true);
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            Assert.assertTrue("The Timeline Notifications flag is not set", this.user.getTimelineNotification());
        }
    }

    /**
     * <p>Scenario #63</p>
     * <pre>
     * Note: "Receive Timeline Notification" box is Checked
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User scrolls down to "Preferences" section
     * 5.  User unchecks the "Receive Timeline Notification" box
     * 6.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Changes to "Preferences" are saved. </p>
     */
    public void testScenario63() throws Exception {
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        setUser(UserSimulator.MANAGER);
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.setTimelineNotification(false);
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            Assert.assertFalse("The Timeline Notifications flag not set", this.user.getTimelineNotification());
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
