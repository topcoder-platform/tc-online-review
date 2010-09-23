/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.AbstractTestCase;
import com.cronos.onlinereview.project.Project;
import com.cronos.onlinereview.project.Messages;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

import java.util.Iterator;
import java.util.Map;
import java.io.InputStream;

import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.IDataSet;
import junit.framework.Assert;

/**
 * <p>A test case for <code>Edit Project Details</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class EditProjectDetailsFunctionalTest extends AbstractTestCase {

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
     * <p>Scenario #34</p>
     * <pre>
     * 1.  User navigates to project list
     * 2.  User selects project to edit
     * 3.  User clicks on project
     * 4.  User clicks on "Edit Project""
     * Expected Outcome
     * Validation error is shown indicating user does NOT permission to edit selected project.
     * <p>
     * <b>Expected Outcome:</b><br/>
     * </p>
     */
    public void testScenario34() throws Exception {
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        for (int i = 0; i < UNAUTHORIZED_USERS.length; i++) {
            setUser(UNAUTHORIZED_USERS[i]);
            Iterator iterator = projects.entrySet().iterator();
            Map.Entry entry;
            while (iterator.hasNext()) {
                entry = (Map.Entry) iterator.next();
                Project project = (Project) entry.getValue();
                this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
                assertDisplayedMessage(Messages.getNoPermissionEditProject());
            }
        }
    }

    /**
     * <p>Scenario #35</p>
     * <pre>
     * Note: User is logged-in as a "Manager"
     * 1.  User navigates to project list
     * 2.  User selects project to edit
     * 3.  User clicks on project
     * 4.  User clicks on "Edit Project"
     * 5.  User edits project using sample data below:
     *
     * <p>
     * <b>Expected Outcome:</b><br/>
     * </p>
     */
    public void testScenario35() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.setProjectNotes("test_notes1");
            this.user.setAutoPilot(0);
            this.user.setProjectUnrated(true);
            this.user.setPaymentRequired(true);
            this.user.setProjectUpdateExplanation("test_explanation01");
            this.user.saveProjectChanges();
        }
        assertData("data/expected/ProjectUpdated35.xml", "The projectd details are not saved correctly");
    }

    /**
     * <p>Scenario #36</p>
     * <pre>
     * Note: User is logged-in as a "Manager"
     * 1.  User navigates to project list
     * 2.  User selects project to edit
     * 3.  User clicks on project
     * 4.  User clicks on "Edit Project"
     * 5.  User edits project using sample data below:
     *
     * <p>
     * <b>Expected Outcome:</b><br/>
     * </p>
     */
    public void testScenario36() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.setProjectNotes("test_notes1");
            this.user.setAutoPilot(0);
            this.user.setProjectUnrated(true);
            this.user.setPaymentRequired(true);
            this.user.setProjectUpdateExplanation("");
            this.user.saveProjectChanges();
            assertDisplayedMessage(Messages.getInvalidInputProjectDetails());
        }
    }

    /**
     * <p>Scenario #37</p>
     * <pre>
     * Note: User is logged-in as a `Manager'
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User may view Project Details and edit a Scorecard for Screening, Review, and
     * Approval Scorecards
     * 5.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * User is able to view "Screening, Review, and Approval Scorecards"; change "Scorecard" currently selected
     * Scorecards; or Add a Scorecard if one has not been selected. </p>
     */
    public void testScenario37() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.setScreeningScorecard("2");
            this.user.setReviewScorecard("1");
            this.user.setApprovalScorecard("3");
            this.user.saveProjectChanges();
        }
        assertData("data/expected/ProjectUpdated37.xml", "The projectd details are not saved correctly");
    }

    /**
     * <p>Scenario #38</p>
     * <pre>
     * Note: User is logged-in as a `Manager'
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User selects "Date" option button and changes the "Review" section of timeline
     * using sample data below
     *
     * Field Name                                Sample Data Value
     * Date 01.01.20010
     *
     * 5.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * 1.  Validation Warning is shown indicating a "Gap or Overlap" currently exists within the Timeline.  User may
     * "Accept" Validation Warning and edited timeline will be saved to database; or, user may Reject "Validation
     * Warning" and return to "Project Timeline". 2.  e-Mail notifications are sent to those users having opted to
     * receive `Timeline Notifications" </p>
     */
    public void testScenario38() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.setPhaseFixedStartDate("Review", "01.01.20010");
            this.user.saveProjectChanges();
            Assert.assertEquals("Confirmation dialog not displayed",
                                Messages.getBadPhaseDateConfirmation(), this.user.popConfirm());
        }
    }

    /**
     * <p>Scenario #39</p>
     * <pre>
     * Note: User is logged-in as a `Manager'
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User selects "Date" option button and changes the "Aggregation Review" section
     * of timeline using sample data below
     *
     * Field Name                                Sample Data Value
     * Date 01.01.20010
     *
     * 5.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating a "Gap or Overlap" currently exists within the Timeline.  User is unable to
     * proceed until date is conforms to timeline validation rules </p>
     */
    public void testScenario39() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.setPhaseFixedStartDate("Aggregation Review", "01.01.20010");
            this.user.saveProjectChanges();
            Assert.assertEquals("Confirmation dialog not displayed",
                                Messages.getBadPhaseDateConfirmation(), this.user.popConfirm());
        }
    }

    /**
     * <p>Scenario #40</p>
     * <pre>
     * Note: User is logged-in as a `Manager'
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  Within "Project Timeline" section, user scrolls down to "Add New Phase"
     * 5.  User enters sample data below:
     *
     * Field Name                                Sample Data Value
     * New Phase                                 Registration
     * Placement - Before/After                  After
     * Placement - Select Phase                  Submission
     * Phase Start Date & Time
     * Phase End Date & Time
     * Duration (hrs)                            12:00
     *
     * 6.  User clicks on "Add"
     * 7.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * A new "Registration" phase is added to the "Project Timeline".  System recalculates start and end dates. </p>
     */
    public void testScenario40() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.addNewProjectPhase("Registration", "After", "Submission", "", "", "12:00");
            this.user.saveProjectChanges();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            assertProjectDetailsPhaseListed("Registration", true);
        }
    }

    /**
     * <p>Scenario #41</p>
     * <pre>
     * Note: User is logged-in as a `Manager'
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  Within "Project Timeline" section, user scrolls down to "Add New Phase"
     * 5.  User enters sample data below:
     *
     * Field Name                                Sample Data Value
     * New Phase                                 Appeals Response
     * Placement - Before/After                  Before
     * Placement - Select Phase                  Appeals
     * Duration (hrs)                            12:00
     *
     * 6.  User clicks on "Add"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown location of new phase in Invalid and user must return to Project Timeline and change
     * parameters. </p>
     */
    public void testScenario41() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.addNewProjectPhase("Appeals Response", "Before", "Appeals", null, null, "12:00");
            this.user.saveProjectChanges();
            assertDisplayedMessage(Messages.getInvalidInputProjectDetails());
        }
    }

    /**
     * <p>Scenario #42</p>
     * <pre>
     * Note: User is logged-in as a `Manager'
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User clicks on "Aggregation" option button
     * 5.  User clicks on "Delete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * User is prompted to confirm deletion.  Phase is removed from "Project Timeline" if user confirms deletion.
     * System automatically recalculates Phase Start and End Dates. </p>
     */
    public void testScenario42() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.deletePhase("Aggregation", true);
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            assertProjectDetailsPhaseListed("Aggregation", false);
        }
    }

    /**
     * <p>Scenario #43</p>
     * <pre>
     * Note: User is logged-in as a `Manager'
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User clicks on "Final Fix" option button
     * 5.  User clicks on "Delete"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating "Final Fix Phase CANNOT be deleted from the Project Timeline".  Phase is NOT
     * removed from "Project Timeline" and Phase Start and End Dates are NOT recalculated. </p>
     */
    public void testScenario43() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.deletePhase("Final Fix", true);
            assertDisplayedMessage(Messages.getPhaseNotDeleted());
        }
    }

    /**
     * <p>Scenario #44</p>
     * <pre>
     * Note: User is logged-in as a `Manager'
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User selects phase within "Project Timeline"
     * 5.  User edits start date and selects lag time (+/- `X' # of days/hours) for selected
     * phase
     * 6.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Edited start date for selected Phase is saved. </p>
     */
    public void testScenario44() throws Exception {
        // TODO : Finish
    }

    /**
     * <p>Scenario #45</p>
     * <pre>
     * Note: User is logged-in as a `Manager'
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User selects phase within "Project Timeline"
     * 5.  User edits start date and selects lag time (+/- `X' # of days/hours) for selected
     * phase
     * 6.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is shown indicating edited start date for selected Phase does meet Valid Start Criteria. </p>
     */
    public void testScenario45() throws Exception {
        // TODO : Finish
    }

    /**
     * <p>Scenario #46</p>
     * <pre>
     * Note: User is logged-in as a `Manager'.  These scenarios only apply to "Registration" and
     * "Submission" phases of the "Project Timeline"
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User selects "Registration Phase" within "Project Timeline"
     * 5.  User edits end date and/or user edits `Phase Criteria: Number of Required
     * Registrations'
     * 6.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Edited fields for selected Phase are saved and phase durations are changes automatically. </p>
     */
    public void testScenario46() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.setRequiredRegistrations(1);
            this.user.saveProjectChanges();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            assertRequiredRegistrations(1);
        }
    }

    /**
     * <p>Scenario #47</p>
     * <pre>
     * Note: User is logged-in as a `Manager'.  These scenarios only apply to "Registration" and
     * "Submission" phases of the "Project Timeline"
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User selects "Submission Phase" within "Project Timeline"
     * 5.  User edits end date and/or user edits `Phase Criteria: `Number of Required
     * Passing Submissions'
     * 6.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Edited fields for selected Phase are saved and phase durations are changes automatically. </p>
     */
    public void testScenario47() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.setRequiredSubmissions(10);
            this.user.saveProjectChanges();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            assertRequiredSubmissions(10);
        }
    }

    /**
     * <p>Scenario #48</p>
     * <pre>
     * Note: User is logged-in as a `Manager'.  These scenarios only apply to "Registration" and
     * "Submission" phases of the "Project Timeline"
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User selects "Registration Phase" or "Submission Phase" within "Project
     * Timeline"
     * 5.  User edits end date so that end date is before start date
     * 6.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation error is show indicating "End Date cannot be before Start Date". </p>
     */
    public void testScenario48() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.updatePhase("Submission", "09.01.2006", "08.01.2006", "121:00");
            this.user.saveProjectChanges();
            assertDisplayedMessage(Messages.getInvalidInputEndAfterStart());
        }
    }

    /**
     * <p>Scenario #49</p>
     * <pre>
     * Note: User is logged-in as a `Manager'.
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User selects option button for "Appeals" phase within "Project Timeline"
     * 5.  User edits phase "Duration (hrs)" using sample data below:
     *
     * Field Name                                Sample Data Value
     * Phase Duration                             72:00
     *
     * 6.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Edited field for selected Phase is saved and subsequent "Project Timeline" phase durations are changes
     * automatically. </p>
     */
    public void testScenario49() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.setPhaseDuration("Appeals", "72:00");
            this.user.saveProjectChanges();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            assertPhaseDuration("Appeals", "72:00");
        }
    }

    /**
     * <p>Scenario #50</p>
     * <pre>
     * Note: User is logged-in as a `Manager'.
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User selects option button for "Appeals" phase within "Project Timeline"
     * 5.  User edits phase "Duration (hrs)" using sample data below:
     *
     * Field Name                                Sample Data Value
     * Phase Duration                            72:XX
     *
     * 6.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating phase duration format in invalid. </p>
     */
    public void testScenario50() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.setPhaseDuration("Appeals", "72:XX");
            this.user.saveProjectChanges();
            assertDisplayedMessage(Messages.getInvalidInputPhaseDuration());
        }
    }

    /**
     * <p>Scenario #51</p>
     * <pre>
     * Note: User is logged-in as a `Manager'.
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User scrolls down to "Resources"
     * 5.  User selects first "Submitter" listed
     * 6.  User click on "Edit"
     * 7.  User edits "Submitter" using sample data below:
     *
     * Field Name                                Sample Data Value
     * Payment Option Button                     Selected
     * Payment 10,000.00
     * Paid? Paid
     *
     * 8.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Edited fields for saved, edited fields are returned to a `Read Only' state, and user remains on "Project Details"
     * page. </p>
     */
    public void testScenario51() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.setPayment("Submitter", "10000", "Paid");
            this.user.saveProjectChanges();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            assertPayment("Submitter", "10000", "Paid");
        }
    }

    /**
     * <p>Scenario #52</p>
     * <pre>
     * Note: A "Designer" is already listed as a "Resource"
     * Note: User is logged-in as a `Manager'.
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User scrolls down to "Resources"
     * 5.  User selects first "Submitter" listed
     * 6.  User click on "Edit"
     * 7.  User edits "Submitter" using sample data below:
     *
     * Field Name                                Sample Data Value
     * Role Designer
     * Payment Option Button                     Selected
     * Payment 10,000.00
     * Paid? Paid
     *
     * 8.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating ONLY ONE (1) "Designer" may be listed per project. </p>
     */
    public void testScenario52() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.setPayment("Submitter", "10000", "Paid");
            this.user.updateRole("Submitter", "Designer");
            this.user.saveProjectChanges();
            assertDisplayedMessage(Messages.getInvalidInputOneDesigner());
        }
    }

    /**
     * <p>Scenario #53</p>
     * <pre>
     * Note: User is logged-in as a `Manager'.
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User scrolls down to "Resources"
     * 5.  User selects first "Submitter" listed
     * 6.  User click on "Edit"
     * 7.  User edits "Submitter" using sample data below:
     *
     * Field Name                                Sample Data Value
     * Name test_submittername01
     *
     * 8.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating "Member Name / Handle" does NOT exist. </p>
     */
    public void testScenario53() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.setResourceName("Submitter", "test_submittername01");
            this.user.saveProjectChanges();
            assertDisplayedMessage(Messages.getInvalidInputUnknownHandle());
        }
    }

    /**
     * <p>Scenario #54</p>
     * <pre>
     * Note: Project is a "Component Project" and a "Reviewer - Stress" is already listed as a
     * "Resource"
     * Note: User is logged-in as a `Manager'.
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User scrolls down to "Resources"
     * 5.  User selects first "Submitter" listed
     * 6.  User click on "Edit"
     * 7.  User edits "Submitter" using sample data below:
     *
     * Field Name                                Sample Data Value
     * Role                                      Review ­ Stress
     *
     * 8.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating ONLY ONE (1) "Stress Test Reviewer" may be listed per "Component Project
     * Review Phase". </p>
     */
    public void testScenario54() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.updateRole("Submitter", "Reviewer - Stress");
            this.user.saveProjectChanges();
            assertDisplayedMessage(Messages.getInvalidInputOneReviewer());
        }
    }

    /**
     * <p>Scenario #55</p>
     * <pre>
     * Note: The `Handle' "test_primaryscreener01" is a member.
     * Note: User is logged-in as a `Manager'.
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User scrolls down to "Resources"
     * 5.  User initiates process to "Add" as resource using sample data below:
     *
     * Field Name                                Sample Data Value
     * Role Primary
     * Screener
     * Name test_primaryscreener01
     *
     * 6.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * The "Primary Screener" is assigned as a "Resource" and will be responsible for screening all submissions. </p>
     */
    public void testScenario55() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.addResource("Primary Screener", "test_primaryscreener01");
            this.user.saveProjectChanges();
            assertResourceAdded("Primary Screener", "test_primaryscreener01");
        }
    }

    /**
     * <p>Scenario #56</p>
     * <pre>
     * Note: The `Handle' "test_primaryscreener01" is NOT a member.
     * Note: User is logged-in as a `Manager'.
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User scrolls down to "Resources"
     * 5.  User initiates process to "Add" as resource using sample data below:
     *
     * Field Name                                Sample Data Value
     * Role Primary
     * Screener
     * Name test_primaryscreener01
     *
     * 6.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating "Member Name / Handle" does NOT exist. </p>
     */
    public void testScenario56() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.addResource("Primary Screener", "test_primaryscreener01");
            this.user.saveProjectChanges();
            assertDisplayedMessage(Messages.getInvalidInputUnknownHandle());
        }
    }

    /**
     * <p>Scenario #57</p>
     * <pre>
     * Note: Current phase within the Timeline is set to "Submission"
     * Note: User is logged-in as a `Manager'.
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User clicks on `Option Button' "Review" within "Edit Timeline" section
     * 5.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Project phase is set to the "Review Phase" of the "Project Timeline" </p>
     */
    public void testScenario57() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.advanceToPhase("Review");
            this.user.saveProjectChanges();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            assertCurrentPhase("Review");
        }
    }

    /**
     * <p>Scenario #58</p>
     * <pre>
     * Note: Current phase within the Timeline is set to "Approval"
     * Note: User is logged-in as a `Manager'.
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User clicks on `Option Button' "Review" within "Edit Timeline" section
     * 5.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating "Managers may not move projects to previous phases". </p>
     */
    public void testScenario58() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.advanceToPhase("Review");
            this.user.saveProjectChanges();
            assertDisplayedMessage(Messages.getNoPermissionMovePhaseBack());
        }
    }

    /**
     * <p>Scenario #59</p>
     * <pre>
     * Note: Project status is set to "Active"
     * Note: User is logged-in as a `Manager'.
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User scrolls down to "Status" section
     * 5.  User changes "Status" from "Active" to
     * a. "Completed"
     * or
     * b.  "Cancelled ­ Failed Screening" or
     * c.  "Cancelled ­ Failed Review"
     * 6.  User enters sample data, below, into `Explanation" text box:
     *
     * Field Name                                 Sample Data Value
     * You must provide an explanation              test_explanationmessage01
     * below if making changes to project
     * status:
     *
     * 7.  User clicks on "Save Changes"
     * 8.  User is directed to confirmation screen
     * 9.  User clicks on "Send Payment'
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Edited status is saved, reason for status change is captured, project is removed from the project list, and
     * payment notification is sent. </p>
     */
    public void testScenario59() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.setProjectUpdateExplanation("test_explanationmessage01");
            this.user.setProjectStatus("Complete");
            this.user.saveProjectChanges();
            this.user.sendPayments();
            this.user.openAllProjectsPage();
            assertProjectNotDisplayed(project.getName() + " version " + project.getVersion());
        }
        assertData("data/expected/ProjectUpdated59.xml", "The projects are not completed correctly");
    }

    /**
     * <p>Scenario #60</p>
     * <pre>
     * Note: Project status is set to "Active"
     * Note: User is logged-in as a `Manager'.
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User scrolls down to "Status" section
     * 5.  User changes "Status" from "Active" to "Inactive"
     * 6.  User enters sample data, below, into `Explanation" text box:
     *
     * Field Name                                Sample Data Value
     * You must provide an explanation           test_explanationmessage01
     * below if making changes to project
     * status:
     *
     * 7.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Edited status is saved, reason for status change is captured, and project is marked as "Inactive" is the project
     * list. </p>
     */
    public void testScenario60() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.setProjectUpdateExplanation("test_explanationmessage01");
            this.user.setProjectStatus("Inactive");
            this.user.saveProjectChanges();
            assertProjectDisplayedInactive(project.getName() + " version " + project.getVersion());
        }
        assertData("data/expected/ProjectUpdated60.xml", "The projects are not completed correctly");
    }

    /**
     * <p>Scenario #61</p>
     * <pre>
     * Note: Project status is set to "Active"
     * Note: User is logged-in as a `Manager'.
     * 1.  User navigates to `Project List'
     * 2.  User clicks on project
     * 3.  User scrolls down and clicks on "Edit Project"
     * 4.  User scrolls down to "Status" section
     * 5.  User changes "Status" from "Active" to "Cancelled ­ Zero Submissions"
     * 6.  User enters sample data, below, into `Explanation" text box:
     *
     * Field Name                                Sample Data Value
     * You must provide an explanation           test_explanationmessage01
     * below if making changes to project
     * status:
     *
     * 7.  User clicks on "Save Changes"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Edited status is saved, reason for status change is captured, and project is removed from the project list. </p>
     */
    public void testScenario61() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openEditProjectDetails(project.getName() + " version " + project.getVersion());
            this.user.setProjectUpdateExplanation("test_explanationmessage01");
            this.user.setProjectStatus("Cancelled ­ Zero Submissions");
            this.user.saveProjectChanges();
            this.user.openAllProjectsPage();
            assertProjectNotDisplayed(project.getName() + " version " + project.getVersion());
        }
        assertData("data/expected/ProjectUpdated61.xml", "The projects are not completed correctly");
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
     * <p>Verifies if the specified project phase is listed.</p>
     *
     * @param phase a <code>String</code> providing the phase to check.
     * @param listed <code>true</code> if phase is expected to be listed; <code>false</code> otherwise.
     */
    private void assertProjectDetailsPhaseListed(String phase, boolean listed) throws Exception {
        HtmlTable section = this.user.findPageSectionTable("Edit Timeline");
        if (listed) {
            Assert.assertNotNull("The phase [" + phase + "] is not listed", this.user.findPhaseRow(section, phase));
        } else {
            Assert.assertNull("The phase [" + phase + "] is listed", this.user.findPhaseRow(section, phase));
        }
    }

    private void assertRequiredRegistrations(int i) {
        // TODO
    }

    private void assertRequiredSubmissions(int i) {
        // TODO
    }

    /**
     * <p>Verifies if the duration for the specified phase is correct.</p>
     *
     * @param phase a <code>String</code> providing the phase.
     * @param duration a <code>String</code> providing the expected duration.
     */
    private void assertPhaseDuration(String phase, String duration) throws Exception {
        HtmlTable section = this.user.findPageSectionTable("Edit Timeline");
        HtmlTableRow phaseRow = this.user.findPhaseRow(section, phase);
        HtmlTableCell cell = phaseRow.getCell(4);
        HtmlTextInput input = (HtmlTextInput) cell.getHtmlElementsByTagName("input").get(0);
        Assert.assertEquals("The phase duration is not correct", duration, input.getValueAttribute());
    }

    /**
     * <p>Verifies the payment for the specified role.</p>
     *
     * @param role a <code>String</code> providing the resource role.
     * @param amount a <code>String</code> providing the payment amount.
     * @param status a <code>String</code> providing the payment status.
     */
    private void assertPayment(String role, String amount, String status) throws Exception {
        HtmlTable resourcesSection = this.user.findPageSectionTable("Resources");
        for (int i = 3; i < resourcesSection.getRowCount(); i++) {
            HtmlSelect roles = (HtmlSelect) resourcesSection.getCellAt(i, 0).getHtmlElementsByTagName("select").get(0);
            HtmlOption option = this.user.findOptionByText(roles, role);
            if (option != null) {
                Assert.assertEquals("The payment amount is not correct",
                                    amount,
                                    ((HtmlTextInput) resourcesSection.getCellAt(i, 2).getHtmlElementsByTagName("input")
                                        .get(0)).getValueAttribute());
                HtmlSelect statusSelect
                    = (HtmlSelect) resourcesSection.getCellAt(i, 3).getHtmlElementsByTagName("select").get(0);
                Assert.assertEquals("The payment status is not correct",
                                    status, ((HtmlOption) statusSelect.getSelectedOptions().get(0)).asText().trim());
            }
        }
    }

    /**
     * <p>Verifies if the specified resource has been added to list of resources.</p>
     *
     * @param role a <code>String</code> providing the role.
     * @param handle a <code>String</code> providing the handle.
     */
    private void assertResourceAdded(String role, String handle) throws Exception {
        boolean found = false;
        HtmlTable resourcesSection = this.user.findPageSectionTable("Resources");
        for (int i = 3; i < resourcesSection.getRowCount(); i++) {
            HtmlSelect roles = (HtmlSelect) resourcesSection.getCellAt(i, 0).getHtmlElementsByTagName("select").get(0);
            HtmlOption option = this.user.findOptionByText(roles, role);
            if (option != null) {
                if (((HtmlTextInput) resourcesSection.getCellAt(i, 1).getHtmlElementsByTagName("input").get(0))
                    .getValueAttribute().equals(handle)) {
                    found = true;
                }
            }
        }
        Assert.assertTrue("The resource [" + handle + "] is not added", found);
    }

    /**
     * <p>Verifies that the specified phase is marked as current.</p>
     *
     * @param phase a <code>String</code> providing the phase to check.
     */
    private void assertCurrentPhase(String phase) throws Exception {
        HtmlTable section = this.user.findPageSectionTable("Edit Timeline");
        HtmlTableRow phaseRow = this.user.findPhaseRow(section, phase);
        Assert.assertTrue("The phase [" + phase +"] is not marked as current",
                          ((HtmlRadioButtonInput) phaseRow.getCell(0).getHtmlElementsByTagName("input").get(0))
                              .isChecked());
    }

    /**
     * <p>Verifies that the specified project is not displayed as active project.</p>
     *
     * @param projectName a <code>String</code> providing the name of the project.
     */
    private void assertProjectNotDisplayed(String projectName) throws Exception {
        Assert.assertFalse("The project [" + projectName + "] is still displayed", this.user.navigateTo(projectName));
    }

    /**
     * <p>Verifies that the specified project is displayed as inactive project.</p>
     *
     * @param projectName a <code>String</code> providing the name of the project.
     */
    private void assertProjectDisplayedInactive(String projectName) throws Exception {
        this.user.openInactiveProjectsPage();
        Assert.assertFalse("The project [" + projectName + "] is still displayed", this.user.navigateTo(projectName));
    }

}
