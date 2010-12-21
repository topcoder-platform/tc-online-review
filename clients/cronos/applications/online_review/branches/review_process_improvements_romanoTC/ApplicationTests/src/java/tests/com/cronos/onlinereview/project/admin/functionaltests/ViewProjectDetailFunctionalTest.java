/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import junit.framework.Assert;
import com.cronos.onlinereview.project.AbstractTestCase;
import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.Project;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;

import java.util.Map;
import java.util.Iterator;
import java.io.InputStream;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

/**
 * <p>A test case for <code>View Project Detail</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 * @test-status Passed
 * @test-date   10/16/2006
 */
public class ViewProjectDetailFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data for all active projects assigned to
     * users.</p>
     */
    public static final String PROJECTS_TEST_DATA_FILE_NAME = "data/initial/DataSet.xml";

    /**
     * <p>Scenario #67</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating user does NOT have permission to view selected project and project details
     * </p>
     */
    public void testScenario67() throws Exception {
        // There are no roles which are prohibited to access project details page
    }

    /**
     * <p>Scenario #68</p>
     * <pre>
     * Note: User is logged-in as a "Manager"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * User is able to View "Project Details" </p>
     */
    public void testScenario68() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openProjectDetails(project.getName() + " version " + project.getVersion());
            HtmlSpan span
                = (HtmlSpan) this.user.getContent().getHtmlElementsByAttribute("span", "class", "bodyTitle").get(0);
            Assert.assertEquals("The project name is not correct", project.getName(), span.asText().trim());
        }
    }

    /**
     * <p>Scenario #69</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * User is able to view "Project Timeline", "Phase Statuses", and "Project Statuses". </p>
     */
    public void testScenario69() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openProjectDetails(project.getName() + " version " + project.getVersion());
            Assert.assertNotNull("Notes section is not displayed", this.user.findPageSectionTable("Notes"));
            Assert.assertNotNull("Timeline section is not displayed", this.user.findPageSectionTable("Timeline"));
            Assert.assertNotNull("Project Details section is not displayed",
                                 this.user.findPageSectionTable("Project Details"));
            Assert.assertNotNull("Resources section is not displayed", this.user.findPageSectionTable("Resources"));
        }
    }

    /**
     * <p>Scenario #70</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating user does NOT have permission to view selected project and project details
     * </p>
     */
    public void testScenario70() throws Exception {
        // There are no roles which are prohibited to access project details page
    }

    /**
     * <p>Scenario #71</p>
     * <pre>
     * Note: User is logged-in as a "Manager"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * User is able to view "Project Deliverables" </p>
     */
    public void testScenario71() throws Exception {
    }

    /**
     * <p>Scenario #72</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating user does NOT have permission to view selected project and project details
     * </p>
     */
    public void testScenario72() throws Exception {
        // There are no roles which are prohibited to access project details page
    }

    /**
     * <p>Scenario #73</p>
     * <pre>
     * Note: User is logged-in as a "Manager"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * User is able to view "Outstanding Project Deliverables" </p>
     */
    public void testScenario73() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openProjectDetails(project.getName() + " version " + project.getVersion());
            HtmlTable section = this.user.findPageSectionTable("My Role");
            HtmlTableCell cell = section.getCellAt(0, 2);
            Assert.assertEquals("Project outstanding deliverables are not displayed",
                                "Outstanding Deliverables", cell.asText().trim());
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
