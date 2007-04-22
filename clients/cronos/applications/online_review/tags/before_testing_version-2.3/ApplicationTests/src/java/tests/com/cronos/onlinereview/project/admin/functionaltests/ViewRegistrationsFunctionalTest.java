/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import junit.framework.Assert;
import com.cronos.onlinereview.project.UserSimulator;
import com.cronos.onlinereview.project.AbstractTestCase;
import com.cronos.onlinereview.project.Resource;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import org.dbunit.dataset.IDataSet;

import java.util.Map;
import java.util.HashMap;

/**
 * <p>A test case for <code>View Registrations</code> Use Case.</p>
 *
 * @author  TCSDEVELOPER
 * @version 1.0
 * @test-status Failed
 * @test-date   10/16/2006 
 */
public class ViewRegistrationsFunctionalTest extends AbstractTestCase {

    /**
     * <p>A <code>String</code> providing the name of the sample project (as displayed by the list of projects) which is
     * used for testing purposes.</p>
     */
    public static final String PROJECT_NAME = "Ajax Timed Survey version 1.0";

    /**
     * <p>A <code>String</code> providing the names of users authorized to view the list of registrants for project.</p>
     */
    private String[] AUTHORIZED_USERS = new String[] {UserSimulator.MANAGER, UserSimulator.OBSERVER};

    /**
     * <p>Scenario #79</p>
     * <pre>
     * Note: User is logged-in as a "Manager" or "Observer"
     * 1.  User clicks on "All Open Projects" tab
     * 2.  User views project list and selects a project
     * </pre>
     * <p> <b>Expected Outcome:</b><br/>
     *
     * User is able to view "Registrations" for selected project. </p>
     */
    public void testScenario79() throws Exception {
        Resource winningSubmitter = Resource.loadResource(getSubmissionPhaseData(), "1");
        Resource secondPlaceSubmitter = Resource.loadResource(getSubmissionPhaseData(), "2");
        Resource thirdPlaceSubmitter = Resource.loadResource(getSubmissionPhaseData(), "3");
        for (int i = 0; i < AUTHORIZED_USERS.length; i++) {
            setUser(AUTHORIZED_USERS[i]);
            this.user.openRegistrationsTab(PROJECT_NAME);
            HtmlTable table = this.user.findPageSectionTable("Registrants");
            Map registrants = parseRegistrants(table);
            Assert.assertEquals("Incorrect number of registrants is displayed", 3, registrants.size());
            assertRegistrant(winningSubmitter, registrants);
            assertRegistrant(secondPlaceSubmitter, registrants);
            assertRegistrant(thirdPlaceSubmitter, registrants);
        }
    }

    /**
     * <p>Verifies if the specified registrant is displayed correctly.</p>
     *
     * @param registrant a <code>Resource</code> providing the details for the requested registrant.
     * @param registrants a <code>Map</code> providing the data for the registrants.
     */
    private void assertRegistrant(Resource registrant, Map registrants) {
        String[] data = (String[]) registrants.get(registrant.getHandle());
        Assert.assertNotNull("The registrant [" + registrant.getHandle() + "] is not listed", data);
        Assert.assertEquals("The email is not correct", registrant.getEmail(), data[1]);
        Assert.assertEquals("The reliability is not correct", registrant.getReliability(), data[2]);
        Assert.assertEquals("The rating is not correct", registrant.getRating(), data[3]);
    }

    /**
     * <p>Parses the data for the registrants listed by the specified HTML table.</p>
     *
     * @param table an <code>HtmlTable</code> listing the registrants.
     * @return a <code>Map</code> mapping the <code>String</code> registrant handle to <code>String</code> array
     *         providing the details for registrant in order as they are displayed.
     */
    private Map parseRegistrants(HtmlTable table) {
        Map registrants = new HashMap();
        for (int i = 2; i < table.getRowCount() - 1; i++) {
            String[] registrant = new String[4];
            for (int j = 0; j < 4; j++) {
                registrant[j] = table.getCellAt(i, j).asText().trim();
            }
            registrants.put(registrant[0], registrant);
        }
        return registrants;
    }

    /**
     * <p>Gets the data sets specific to test case which must be used to populate the database tables with initial data.
     * </p>
     *
     * @return an <code>IDataSet</code> array providing the data sets specific to test case.
     * @throws Exception if an unexpected error occurs.
     */
    protected IDataSet[] getDataSets() throws Exception {
        return new IDataSet[] {getRegistrationPhaseData(), getSubmissionPhaseData()};
    }
}
