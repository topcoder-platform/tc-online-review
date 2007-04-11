/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import com.cronos.onlinereview.project.AbstractTestCase;
import com.cronos.onlinereview.project.Messages;
import com.cronos.onlinereview.project.UserSimulator;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import junit.framework.Assert;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.io.InputStream;

/**
 * <p>A test case for <code>Create Project</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 * @test-status Failed
 * @test-date   10/16/2006
 */
public class CreateProjectFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data for all active projects assigned to
     * users.</p>
     */
    public static final String PROJECTS_TEST_DATA_FILE_NAME = "data/initial/DataSet.xml";

    /**
     * <p>A <code>String</code> array providing the usernames for users who are not granted the permission to edit the
     * details for selected project.</p>
     */
    public static final String[] UNAUTHORIZED_USERS
        = new String[] {UserSimulator.PRIMARY_REVIEWER, UserSimulator.DESIGNER, UserSimulator.WINNING_SUBMITTER,
                        UserSimulator.SECOND_PLACE_SUBMITTER, UserSimulator.THIRD_PLACE_SUBMITTER,
                        UserSimulator.REVIEWER1, UserSimulator.REVIEWER2, UserSimulator.OBSERVER,
                        UserSimulator.APPROVER};

    /**
     * <p>Scenario #28</p>
     * <pre>
     * 1.  User clicks on "Create Project"
     * Expected Outcome
     * Validation error is shown indicating user does NOT permission to create a project.
     * <p>
     * <b>Expected Outcome:</b><br/>
     * </p>
     */
    public void testScenario28() throws Exception {
        for (int i = 0; i < UNAUTHORIZED_USERS.length; i++) {
            setUser(UNAUTHORIZED_USERS[i]);
            this.user.openCreateProjectPage();
            assertDisplayedMessage(Messages.getNoPermissionCreateProject());
        }
    }

    /**
     * <p>Scenario #29</p>
     * <pre>
     * Note: User is logged-in as a "Manager"
     * 1.  User clicks on "Create Project"
     * 2.  User enters sample data below:
     *
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * A new project is creates and saved. </p>
     */
    public void atestScenario29() throws Exception {
        setUser(UserSimulator.MANAGER);
        this.user.openCreateProjectPage();
        this.user.createProject("test_projectname", "Component", "Specification", "TopCoder Private", true,
                                1, true, false, false, "1", "2", "3", "https://software.topcoder.com",
                                "https://coder.topcoder.com/svn", "test_notes1", "Project Manager",
                                "test_johnsmith01", "100.00", "Paid");
        assertData("data/expected/ProjectCreated29.xml", "The project is not created as expected");
    }

    /**
     * <p>Scenario #30</p>
     * <pre>
     * Note: User is logged-in as a "Manager"
     * 1.  User clicks on "Create Project"
     * 2.  User enters sample data below:
     *
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating required fields are MISSING and must be completed before proceeding </p>
     */
    public void atestScenario30() throws Exception {
        setUser(UserSimulator.MANAGER);
        this.user.openCreateProjectPage();
        this.user.createProject("test_projectname", "", "Specification", "TopCodr Private", true,
                                1, true, false, false, "1", "2", "3", "https://software.topcoder.com",
                                "https://coder.topcoder.com/svn", "test_notes1", "",
                                "test_johnsmith01", "100.00", "Paid");
        assertDisplayedMessage(Messages.getInvalidInputProjectDetails());
    }

    /**
     * <p>Scenario #31</p>
     * <pre>
     * Note: User is logged-in as a "Manager"
     * 1.  User clicks on "Create Project"
     * 2.  User enters sample data below:
     *
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Registration Date is before current date. </p>
     */
    public void atestScenario31() throws Exception {
        // TODO : Not sure which Registration Date is this test referring to
    }

    /**
     * <p>Scenario #32</p>
     * <pre>
     * Note: User is logged-in as a "Manager"
     * 1.  User clicks on "Create Project"
     * 2.  User enters sample data below:
     *
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating Number is INVALID </p>
     */
    public void atestScenario32() throws Exception {
        setUser(UserSimulator.MANAGER);
        this.user.openCreateProjectPage();
        this.user.createProject("test_projectname", "Component", "Specification", "TopCoder Private", true,
                                1, true, false, false, "1", "2", "3", "https://software.topcoder.com",
                                "https://coder.topcoder.com/svn", "test_notes1", "Project Manager",
                                "test_johnsmith01", "100.XX", "Paid");
        assertDisplayedMessage(Messages.getInvalidInputProjectDetails());
    }

    /**
     * <p>Scenario #33</p>
     * <pre>
     * Note: User is logged-in as a "Manager"
     * 1.  User clicks on "Create Project"
     * 2.  User selects "Design" from "Use Timeline Template:"
     * 3.  User clicks on "Load Template"
     * Expected Outcome
     * Design Template is loaded ­ containing default start time and date, phase start and end
     * dates, and default timeline phases..
     * <p>
     * <b>Expected Outcome:</b><br/>
     * </p>
     */
    public void atestScenario33() throws Exception {
        setUser(UserSimulator.MANAGER);
        this.user.openCreateProjectPage();
        this.user.loadTimelineTemplate("Design");
        assertTimelineTemplateDisplayed("Design");
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
     * <p>Verifies that the project phase template has been loaded.</p>
     *
     * @param message an assertion message.
     * @throws Exception if an unexpected error occurs.
     */
    private void assertTimelineTemplateDisplayed(String message) throws Exception {
        HtmlTable phasesSection = this.user.findPageSectionTable("Phase Name");
        Assert.assertNotNull(message, phasesSection);
    }
}
