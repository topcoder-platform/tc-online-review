/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.AbstractTestCase;
import com.cronos.onlinereview.project.Project;
import com.cronos.onlinereview.project.Lookup;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import java.util.Map;
import java.util.Iterator;
import java.io.InputStream;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import junit.framework.Assert;

/**
 * <p>A test case for <code>View Projects</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class ViewProjectsFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data for all active projects.</p>
     */
    public static final String ACTIVE_PROJECTS_TEST_DATA_FILE_NAME = "data/initial/ViewProjectsActive.xml";

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data for all inactive projects.</p>
     */
    public static final String INACTIVE_PROJECTS_TEST_DATA_FILE_NAME = "data/initial/ViewProjectsInactive.xml";

    /**
     * <p>Scenario #64</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * User is able to view a list of all open projects. </p>
     */
    public void testScenario64() throws Exception {
        setUser(UserSimulator.WINNING_SUBMITTER);
        this.user.openAllProjectsPage();

        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            assertActiveProjectDisplayed(project);
        }
    }

    /**
     * <p>Scenario #65</p>
     * <pre>
     * 1.  User clicks on "Inactive Projects" tab
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * User is able to view a list of all inactive projects. </p>
     */
    public void testScenario65() throws Exception {
        setUser(UserSimulator.MANAGER);
        this.user.openInactiveProjectsPage();

        Map projects = Project.loadAllProjects(getInactiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            assertInactiveProjectDisplayed(project);
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
        return new IDataSet[] {getActiveProjectsData(), getInactiveProjectsData()};
    }

    /**
     * <p>Gets the data set providing the initial data for active projects.</p>
     *
     * @return an <code>IDataSet</code> providing details for active projects.
     * @throws Exception if an unexpected error occurs.
     */
    protected static IDataSet getActiveProjectsData() throws Exception {
        InputStream projectDataStream
            = ViewProjectsFunctionalTest.class.getClassLoader().getResourceAsStream(ACTIVE_PROJECTS_TEST_DATA_FILE_NAME);
        return new FlatXmlDataSet(projectDataStream);
    }

    /**
     * <p>Gets the data set providing the initial data for inactive projects.</p>
     *
     * @return an <code>IDataSet</code> providing details for inactive projects.
     * @throws Exception if an unexpected error occurs.
     */
    protected static IDataSet getInactiveProjectsData() throws Exception {
        InputStream projectDataStream
            = AbstractTestCase.class.getClassLoader().getResourceAsStream(INACTIVE_PROJECTS_TEST_DATA_FILE_NAME);
        return new FlatXmlDataSet(projectDataStream);
    }

    /**
     * <p>Verifies that the specified project is displayed by the currently opened <code>All Open Projects</code> page.
     * </p>
     *
     * @param project a <code>Project</code> providing the details for the existing projects.
     */
    private void assertActiveProjectDisplayed(Project project) throws Exception {
        Lookup lookup = Lookup.getInstance();
        String projectNameVersion = project.getName() + " version " + project.getVersion();
        String projectCategory = lookup.getProjectCategoryName(project.getCategoryId());
        HtmlTable table = this.user.findPageSectionTable(projectCategory);
        for (int i = 2; i < table.getRowCount(); i++) {
            if (table.getCellAt(i, 0).asText().trim().equals(projectNameVersion)) {
                return;
            }
        }
        Assert.fail("The project [" + projectNameVersion + "] is not listed as Active Project");
    }

    /**
     * <p>Verifies that the specified project is displayed by the currently opened <code>All Open Projects</code> page.
     * </p>
     *
     * @param project a <code>Project</code> providing the details for the existing projects.
     */
    private void assertInactiveProjectDisplayed(Project project) throws Exception {
        Lookup lookup = Lookup.getInstance();
        String projectNameVersion = project.getName() + " version " + project.getVersion();
        String projectCategory = lookup.getProjectCategoryName(project.getCategoryId());
        HtmlTable table = this.user.findPageSectionTable(projectCategory);
        for (int i = 2; i < table.getRowCount(); i++) {
            if (table.getCellAt(i, 0).asText().trim().equals(projectNameVersion)) {
                return;
            }
        }
        Assert.fail("The project [" + projectNameVersion + "] is not listed as Inactive Project");
    }
}
