/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.AbstractTestCase;
import com.cronos.onlinereview.project.Project;
import com.cronos.onlinereview.project.Messages;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

/**
 * <p>A test case for <code>Upload Test Cases</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 * @test-status Passed
 * @test-date   10/16/2006
 */
public class UploadTestCasesFunctionalTest extends AbstractTestCase {
    // TODO : Each test requires a separate data file (initial)

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
    public static final String[] REVIEWERS
        = new String[] {UserSimulator.PRIMARY_REVIEWER, UserSimulator.REVIEWER1, UserSimulator.REVIEWER2};

    /**
     * <p>Constructs new <code>UploadTestCasesFunctionalTest</code> with specified test name.</p>
     *
     * @param name a <code>String</code> providing the name of the test.
     */
    public UploadTestCasesFunctionalTest(String name) {
        super(name);
    }

    /**
     * <p>Scenario #104</p>
     * <pre>
     * Note: User is logged-in as a "Reviewer".  Current project phase is "Review"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review & Appeals" tab
     * 4.  User clicks on "Test Case" for Stress, Failure, or Accuracy
     * 5.  User clicks on "Browse" and selects file from directory
     * 6.  User clicks on "Upload"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Test Cases are successfully uploaded and e-mail notifications are sent to active submitters, reviewers and
     * managers. </p>
     */
    public void testScenario104() throws Exception {
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        for (int i = 0; i < REVIEWERS.length; i++) {
            setUser(REVIEWERS[i]);
            Iterator iterator = projects.entrySet().iterator();
            Map.Entry entry;
            while (iterator.hasNext()) {
                entry = (Map.Entry) iterator.next();
                Project project = (Project) entry.getValue();
                this.user.openReviewAppealsTab(project.getName() + " version " + project.getVersion());
                this.user.uploadTestCases(UserSimulator.getTestCaseFile(REVIEWERS[i]));
            }
        }
        assertData("data/expected/TestCasesUploaded.xml", "The test cases are not uploaded correctly");
    }

    /**
     * <p>Scenario #105</p>
     * <pre>
     * Note: User is logged-in as a "Reviewer".
     * Current project phase is "Review"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review & Appeals" tab
     * 4.  User clicks on "Test Case" for Stress, Failure, or Accuracy
     * 5.  User clicks on "Browse" and selects file from directory to Test Cases currently
     * loaded
     * 6.  User clicks on "Upload"
     *
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Test Cases are successfully re-uploaded and e-mail notifications are sent to active submitters, reviewers and
     * managers. </p>
     */
    public void testScenario105() throws Exception {
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        for (int i = 0; i < REVIEWERS.length; i++) {
            setUser(REVIEWERS[i]);
            Iterator iterator = projects.entrySet().iterator();
            Map.Entry entry;
            while (iterator.hasNext()) {
                entry = (Map.Entry) iterator.next();
                Project project = (Project) entry.getValue();
                this.user.openReviewAppealsTab(project.getName() + " version " + project.getVersion());
                this.user.uploadTestCases(UserSimulator.getTestCaseFile(REVIEWERS[i]));
            }
        }
        assertData("data/expected/TestCasesUpdated.xml", "The test cases are not uploaded correctly");
    }

    /**
     * <p>Scenario #106</p>
     * <pre>
     * Note: User is logged-in as a "Reviewer"
     * Current project phase is "Final Fix/Review"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review & Appeals" tab
     * 4.  User clicks on "Test Case" for Stress, Failure, or Accuracy
     * 5.  User clicks on "Browse" and selects file from directory to Test Cases currently
     * loaded
     * 6.  User clicks on "Upload"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Test Cases are successfully re-uploaded and e-mail notifications are sent to active submitters, reviewers and
     * managers. </p>
     */
    public void testScenario106() throws Exception {
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        for (int i = 0; i < REVIEWERS.length; i++) {
            setUser(REVIEWERS[i]);
            Iterator iterator = projects.entrySet().iterator();
            Map.Entry entry;
            while (iterator.hasNext()) {
                entry = (Map.Entry) iterator.next();
                Project project = (Project) entry.getValue();
                this.user.openReviewAppealsTab(project.getName() + " version " + project.getVersion());
                this.user.uploadTestCases(UserSimulator.getTestCaseFile(REVIEWERS[i]));
            }
        }
        assertData("data/expected/TestCasesUpdated.xml", "The test cases are not uploaded correctly");
    }

    /**
     * <p>Scenario #107</p>
     * <pre>
     * Note: User is logged-in as a "Reviewer".
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Review & Appeals" tab
     * 4.  User clicks on "Test Case" for Stress, Failure, or Accuracy
     * 5.  User clicks on "Browse" and selects file from directory
     * 6.  User clicks on "Upload"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating New Test Cases can ONLY be uploaded during the "Review Phase". </p>
     */
    public void testScenario107() throws Exception {
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        for (int i = 0; i < REVIEWERS.length; i++) {
            setUser(REVIEWERS[i]);
            Iterator iterator = projects.entrySet().iterator();
            Map.Entry entry;
            while (iterator.hasNext()) {
                entry = (Map.Entry) iterator.next();
                Project project = (Project) entry.getValue();
                this.user.openReviewAppealsTab(project.getName() + " version " + project.getVersion());
                HtmlTable reviewSection = this.user.findPageSectionTable("Review");
                List links = reviewSection.getHtmlElementsByAttribute("a", "title", "Upload New Test Case");
                HtmlAnchor link = (HtmlAnchor) links.get(0);
                this.user.click(link); // Should open upload test cases page
                assertDisplayedMessage(Messages.getInvalidPhaseForTestCases());
            }
        }
        assertData("data/expected/TestCasesUpdated.xml", "The test cases are not uploaded correctly");
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
