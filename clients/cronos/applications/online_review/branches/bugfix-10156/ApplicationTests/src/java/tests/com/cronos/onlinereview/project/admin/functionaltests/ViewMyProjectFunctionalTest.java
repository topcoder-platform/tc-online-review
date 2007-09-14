/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import com.cronos.onlinereview.project.AbstractTestCase;
import com.cronos.onlinereview.project.Lookup;
import com.cronos.onlinereview.project.Project;
import com.cronos.onlinereview.project.Resource;
import com.cronos.onlinereview.project.UserSimulator;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import junit.framework.Assert;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.io.InputStream;
import java.util.Map;
import java.util.List;

/**
 * <p>A test case for <code>View My Project</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 * @test-status Passed
 * @test-date   10/16/2006
 */
public class ViewMyProjectFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data for all active projects assigned to
     * users.</p>
     */
    public static final String PROJECTS_TEST_DATA_FILE_NAME = "data/initial/DataSet.xml";

    public static final String[] USERS = new String[] {UserSimulator.REVIEWER1, UserSimulator.REVIEWER2,
        UserSimulator.SECOND_PLACE_SUBMITTER, UserSimulator.THIRD_PLACE_SUBMITTER, UserSimulator.WINNING_SUBMITTER};

    /**
     * <p>Scenario #66</p>
     * <pre>
     * 1.  User clicks on "My Open Projects" tab
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * User is able to view his/her list of assigned projects </p>
     */
    public void testScenario66() throws Exception {
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        for (int i = 0; i < USERS.length; i++) {
            setUser(USERS[i]);
            this.user.openMyProjectsPage();
            Resource[] resources = Resource.loadResourceByExternalId(getActiveProjectsData(), this.user.getId());
            for (int j = 0; j < resources.length; j++) {
                Resource resource = resources[j];
                assertActiveProjectDisplayed((Project) projects.get(resource.getProjectId()));
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
     * <p>Verifies that the specified project is displayed by the currently opened <code>My Open Projects</code> page.
     * </p>
     *
     * @param project a <code>Project</code> providing the details for the existing projects.
     */
    private void assertActiveProjectDisplayed(Project project) throws Exception {
        Lookup lookup = Lookup.getInstance();
        String projectNameVersion = project.getName() + " version " + project.getVersion();
        String projectCategory = lookup.getProjectCategoryName(project.getCategoryId());
        HtmlTable table = this.user.findPageSectionTable(projectCategory);
        List links = table.getCellAt(0, 0).getHtmlElementsByTagName("a");
        ((HtmlAnchor) links.get(0)).click();
        table = this.user.findPageSectionTable(projectCategory);
        printCurrentPage();
        HtmlTableBody tbody = (HtmlTableBody) table.getHtmlElementsByTagName("tbody").get(0);
        List rows = tbody.getHtmlElementsByTagName("tr");
        for (int i = 0; i < rows.size(); i++) {
            HtmlTableRow row = (HtmlTableRow) rows.get(i);
            if (row.getCell(0).asText().trim().equals(projectNameVersion)) {
                return;
            }
        }
        Assert.fail("The project [" + projectNameVersion + "] is not listed as Active Project");
    }
}
