/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import junit.framework.Assert;
import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.AbstractTestCase;
import com.cronos.onlinereview.project.Project;
import com.cronos.onlinereview.project.Resource;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.io.InputStream;

/**
 * <p>A test case for <code>View Winning Submissions</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 * @test-status Failed
 * @test-date   10/16/2006
 */
public class ViewWinningSubmissionsFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data for all active projects assigned to
     * users.</p>
     */
    public static final String PROJECTS_TEST_DATA_FILE_NAME = "data/initial/DataSet.xml";

    public static final String[] USERS = new String[] {UserSimulator.PRIMARY_REVIEWER, UserSimulator.REVIEWER1,
        UserSimulator.REVIEWER2, UserSimulator.OBSERVER, UserSimulator.WINNING_SUBMITTER,
        UserSimulator.SECOND_PLACE_SUBMITTER, UserSimulator.THIRD_PLACE_SUBMITTER};

    /**
     * <p>Scenario #86</p>
     * <pre>
     * 1.  User clicks on "All Open Projects" tab
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * User is able to view the list of most "Most Recent Submissions". </p>
     */
    public void testScenario86() throws Exception {
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        for (int i = 0; i < USERS.length; i++) {
            setUser(USERS[i]);
            Iterator iterator = projects.values().iterator();
            Project project;
            while (iterator.hasNext()) {
                project = (Project) iterator.next();
                this.user.openReviewAppealsTab(project.getName() + " version " + project.getVersion());
                List winnerImage = this.user.getContent().getHtmlElementsByAttribute("img", "alt", "Winner");
                Assert.assertNotNull("The winner is not announced after Appeals Response phase", winnerImage);
                Assert.assertTrue("The winner is not announced after Appeals Response phase", winnerImage.size() > 0);
                Assert.assertEquals("A single winner must be announced after Appeals Response phase",
                                    1, winnerImage.size());
                String winnerExternalId = project.getWinnerExternalId();
                Resource[] resources = Resource.loadResourceByExternalId(getActiveProjectsData(), winnerExternalId);
                for (int j = 0; j < resources.length; j++) {
                    if (resources[j].getProjectId().equals(project.getId())) {
                        Map mostRecentSubmission = resources[j].getMostRecentSubmission();
                        assertWinningSubmissionDisplayed(mostRecentSubmission);
                    }
                }
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

    /**
     * <p>Verifies that all submissions associated with the specified resource are currently displayed.</p>
     *
     * @param submission a <code>Map</code> providing the details for the project submissions.
     */
    private void assertWinningSubmissionDisplayed(Map submission) throws Exception {
        HtmlTable submissionsSection = this.user.findPageSectionTable("Review");
        String submissionId = (String) submission.get("submission_id");
        boolean submissionDisplayed = false;
        for (int i = 3; i < submissionsSection.getRowCount(); i++) {
            if (submissionsSection.getCellAt(i, 0).asText().trim().startsWith(submissionId + " ")) {
                submissionDisplayed = true;
                List images = submissionsSection.getCellAt(i, 0).getHtmlElementsByAttribute("img", "alt", "Winner");
                Assert.assertNotNull("The winning submission is not marked with gold star", images);
                Assert.assertTrue("The winning submission is not marked with gold star", images.size() > 0);
            }
        }
        if ("5".equals(submission.get("submission_status_id"))) {
            Assert.assertTrue("The deleted submission [" + submissionId + "] is displayed", submissionDisplayed);
        } else {
            Assert.assertTrue("The winning submission [" + submissionId + "] is not displayed", submissionDisplayed);
        }
    }
}
