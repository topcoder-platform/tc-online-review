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

/**
 * <p>A test case for <code>Perform Final Fix</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 */
public class PerformFinalFixFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data for all active projects assigned to
     * users.</p>
     */
    public static final String PROJECTS_TEST_DATA_FILE_NAME = "data/initial/DataSet.xml";

    /**
     * <p>Scenario #140</p>
     * <pre>
     * Note: User is logged-in as "Submitter"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Final Fix" tab
     * 4.  User opens "Aggregation Scorecard"
     * 5.  User submits Final Fixes for items marked "Required"
     * 6.  User clicks on "Upload"
     * 7.  User browses directory and selects file to upload
     * 8.  User clicks on "Upload"
     *
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Final Fix is uploaded. </p>
     */
    public void testScenario140() throws Exception {
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        setUser(UserSimulator.WINNING_SUBMITTER);
        Iterator iterator = projects.entrySet().iterator();
        Map.Entry entry;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            Project project = (Project) entry.getValue();
            this.user.openReviewAppealsTab(project.getName() + " version " + project.getVersion());
            this.user.uploadFinalFix(UserSimulator.getFinalFixFile(UserSimulator.WINNING_SUBMITTER));
        }
        assertData("data/expected/FinalFixUploaded.xml", "The test cases are not uploaded correctly");
    }

    /**
     * <p>Scenario #141</p>
     * <pre>
     * Note: User is logged-in as "Submitter"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * 3.  User clicks on "Final Fix" tab
     * 4.  User opens "Aggregation Scorecard"
     * 5.  User clicks on "Upload"
     * 6.  User browses directory and selects file to upload
     * 7.  User clicks on "Upload"
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * Validation Error is shown indicating file cannot be uploaded until User submits Final Fixes for items marked
     * "Required". </p>
     */
    public void testScenario141() throws Exception {
        // TODO : Not sure what this test is about
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
