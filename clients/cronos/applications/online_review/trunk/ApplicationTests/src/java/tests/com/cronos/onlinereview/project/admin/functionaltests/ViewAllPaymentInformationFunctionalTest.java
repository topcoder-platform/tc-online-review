/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import com.cronos.onlinereview.project.AbstractTestCase;
import com.cronos.onlinereview.project.Project;
import com.cronos.onlinereview.project.Resource;
import com.cronos.onlinereview.project.UserSimulator;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import junit.framework.Assert;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>A test case for <code>View All Payment Information</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 * @test-status Failed
 * @test-date   10/16/2006
 */
public class ViewAllPaymentInformationFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data for all active projects assigned to
     * users.</p>
     */
    public static final String PROJECTS_TEST_DATA_FILE_NAME = "data/initial/DataSet.xml";

    /**
     * <p>Scenario #76</p>
     * <pre>
     * Note: User is logged-in as a "Manager"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * User is able to view "All Payment Information" </p>
     */
    public void testScenario76() throws Exception {
        setUser(UserSimulator.MANAGER);
        Map projects = Project.loadAllProjects(getActiveProjectsData());
        Iterator iterator = projects.values().iterator();
        Project project;
        while (iterator.hasNext()) {
            project = (Project) iterator.next();
            this.user.openProjectDetails(project.getName() + " version " + project.getVersion());
            Resource[] resources = Resource.loadProjectResources(getActiveProjectsData(), project.getId());
            for (int j = 0; j < resources.length; j++) {
                assertResourcePaymentDisplayed(resources[j]);
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
            = ViewProjectResourcesFunctionalTest.class.getClassLoader().getResourceAsStream(PROJECTS_TEST_DATA_FILE_NAME);
        return new FlatXmlDataSet(projectDataStream);
    }

    /**
     * <p>Verifies if the payment for specified resource is displayed by the current page.</p>
     *
     * @param resource a <code>Resource</code> providing the details for the selected resource.
     * @throws Exception if an unexpected error occurs.
     */
    private void assertResourcePaymentDisplayed(Resource resource) throws Exception {
        HtmlTable resourcesSection = this.user.findPageSectionTable("Resources");
        for (int i = 2; i < resourcesSection.getRowCount(); i++) {
            if (resourcesSection.getCellAt(i, 1).asText().trim().equals(resource.getHandle())) {
                String payment = resource.getPayment();
                HtmlTableCell paymentCell = resourcesSection.getCellAt(i, 2);
                HtmlTableCell paymentStatusCell = resourcesSection.getCellAt(i, 3);
                if (payment == null) {
                    Assert.assertTrue("The payment is not displayed correctly",
                                      paymentCell.asText().trim().endsWith("N/A"));
                } else {
                    Assert.assertEquals("The payment is not displayed correctly",
                                        "$" + payment, paymentCell.asText().trim());
                    if ("Yes".equals(resource.getPaymentStatus())) {
                        Assert.assertEquals("The payment status is not displayed correctly",
                                            "Paid", paymentStatusCell.asText().trim());
                    } else {
                        Assert.assertEquals("The payment status is not displayed correctly",
                                            "Not Paid", paymentStatusCell.asText().trim());
                    }
                }
                return;
            }
        }
        Assert.fail("The resource [" + resource.getHandle() + "] is not displayed");
    }
}
