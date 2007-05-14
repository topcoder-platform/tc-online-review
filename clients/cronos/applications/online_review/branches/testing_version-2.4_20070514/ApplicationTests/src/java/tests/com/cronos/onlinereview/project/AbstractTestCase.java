/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project;

import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import org.dbunit.Assertion;
import org.dbunit.DatabaseTestCase;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.CompositeDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public abstract class AbstractTestCase extends DatabaseTestCase {

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database lookup tables prior to executing any test.</p>
     */
    public static final String LOOKUPS_TEST_DATA_FILE_NAME = "data/split/Lookups.xml";

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database scorecard tables prior to executing any test.</p>
     */
    public static final String SCORECARDS_TEST_DATA_FILE_NAME = "data/split/Scorecards.xml";

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database user tables prior to executing any test.</p>
     */
    public static final String USERS_TEST_DATA_FILE_NAME = "data/split/Users.xml";

    /**
     * <p>A <code>DateFormat</code> to be used to parser/format dates in accordance with date format used by <code>Time
     * Tracker</code> application.</p>
     */
    public static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MM-dd-yyyy");

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data which must exist in database tables
     * when the project advances into <code>Registration</code> phase.</p>
     */
    public static final String REGISTATION_PHASE_TEST_DATA_FILE_NAME = "data/split/RegistrationPhase.xml";

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data which must exist in database tables
     * when the project advances into <code>Submission</code> phase.</p>
     */
    public static final String SUBMISSION_PHASE_TEST_DATA_FILE_NAME = "data/split/SubmissionPhase.xml";

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data which must exist in database tables
     * when the project advances into <code>Screening</code> phase.</p>
     */
    public static final String SCREENING_PHASE_TEST_DATA_FILE_NAME = "data/split/ScreeningPhase.xml";

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data which must exist in database tables
     * when the project advances into <code>Review</code> phase.</p>
     */
    public static final String REVIEW_PHASE_TEST_DATA_FILE_NAME = "data/split/ReviewPhase.xml";

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data which must exist in database tables
     * when the project advances into <code>Appeals</code> phase.</p>
     */
    public static final String APPEALS_PHASE_TEST_DATA_FILE_NAME = "data/split/AppealsPhase.xml";

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data which must exist in database tables
     * when the project advances into <code>Appeals Response</code> phase.</p>
     */
    public static final String APPEALS_RESPONSE_PHASE_TEST_DATA_FILE_NAME = "data/split/AppealsResponsePhase.xml";

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data which must exist in database tables
     * when the project advances into <code>Aggregation</code> phase.</p>
     */
    public static final String AGGREGATION_PHASE_TEST_DATA_FILE_NAME = "data/split/AggregationPhase.xml";

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data which must exist in database tables
     * when the project advances into <code>Aggregation Review</code> phase.</p>
     */
    public static final String AGGREGATION_REVIEW_PHASE_TEST_DATA_FILE_NAME = "data/split/AggregationReviewPhase.xml";

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data which must exist in database tables
     * when the project advances into <code>Final Fix</code> phase.</p>
     */
    public static final String FINAL_FIX_PHASE_TEST_DATA_FILE_NAME = "data/split/FinalFixPhase.xml";

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data which must exist in database tables
     * when the project advances into <code>Final Review</code> phase.</p>
     */
    public static final String FINAL_REVIEW_PHASE_TEST_DATA_FILE_NAME = "data/split/FinalReviewPhase.xml";

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data which must exist in database tables
     * when the project advances into <code>Approval</code> phase.</p>
     */
    public static final String APPROVAL_PHASE_TEST_DATA_FILE_NAME = "data/split/ApprovalPhase.xml";

    /**
     * <p>A <code>String</code> providing the name of the XML file providing the initial test data to be inserted into
     * database tables prior to executing any test. This file provides the data which must exist in database tables
     * when the project advances into <code>Complete Project</code> phase.</p>
     */
    public static final String COMPLETE_PHASE_TEST_DATA_FILE_NAME = "data/split/CompletePhase.xml";

    protected Lookup lookup = new Lookup(getLookupData());

    protected UserSimulator user = null;

    protected AbstractTestCase() {
    }

    protected AbstractTestCase(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        this.user = new UserSimulator(UserSimulator.MANAGER);
//        clearDatabase();
        super.setUp();
    }


    protected void tearDown() throws Exception {
        this.user = null;
        super.tearDown();
//        clearDatabase();
    }

    protected void setUser(String username) {
        this.user = new UserSimulator(username);
    }

    protected void setReviewerUser(String username) {
        this.user = new Reviewer(username);
    }

    /**
     * <p>Gets the connection to database.</p>
     *
     * @return an <code>IDatabaseConnection</code> providing the connection to database.
     * @throws Exception if an unexpected error occurs.
     */
    protected IDatabaseConnection getConnection() throws Exception {
        DBConnectionFactory factory = new DBConnectionFactoryImpl(DBConnectionFactoryImpl.class.getName());
        Connection connection = factory.createConnection();
        DatabaseConnection databaseConnection = new DatabaseConnection(connection);
        DatabaseConfig config = databaseConnection.getConfig();
        config.setFeature("http://www.dbunit.org/features/batchedStatements", true);
        return databaseConnection;
    }

    /**
     * <p>Gets the data to be used to initialize the database before each test execution.</p>
     *
     * @return an <code>IDataSet</code> providing the data to be used to set the database to initial state.
     * @throws Exception if an unexpected error occurs.
     */
    protected IDataSet getDataSet() throws Exception {
//        InputStream lookupsDataStream
//            = getClass().getClassLoader().getResourceAsStream(AbstractTestCase.LOOKUPS_TEST_DATA_FILE_NAME);
        InputStream scorecardsDataStream
            = getClass().getClassLoader().getResourceAsStream(AbstractTestCase.SCORECARDS_TEST_DATA_FILE_NAME);
        InputStream usersDataStream
            = getClass().getClassLoader().getResourceAsStream(AbstractTestCase.USERS_TEST_DATA_FILE_NAME);
        IDataSet[] customDataSets = getDataSets();

        IDataSet[] dataSets = new IDataSet[2 + customDataSets.length];
//        dataSets[0] = new FlatXmlDataSet(lookupsDataStream);
        dataSets[0] = new FlatXmlDataSet(scorecardsDataStream);
        dataSets[1] = new FlatXmlDataSet(usersDataStream);
        for (int i = 0; i < customDataSets.length; i++) {
            dataSets[i + 2] = customDataSets[i];
        }
        return new CompositeDataSet(dataSets);
    }

    /**
     * <p>Gets the data sets specific to test case which must be used to populate the database tables with initial data.
     * </p>
     *
     * @return an <code>IDataSet</code> array providing the data sets specific to test case.
     * @throws Exception if an unexpected error occurs.
     */
    protected abstract IDataSet[] getDataSets() throws Exception;

    /**
     * <p>Gets the database operation to be executed prior executing each test.</p>
     *
     * @return a <code>DatabaseOperation</code> to be executed prior executing each test.
     * @throws Exception if an unexpected error occurs.
     */
    protected DatabaseOperation getSetUpOperation() throws Exception {
//        return new CompositeOperation(DatabaseOperation.DELETE_ALL, DatabaseOperation.REFRESH);
        return DatabaseOperation.REFRESH;
    }

    /**
     * <p>Gets the database operation to be executed after executing each test.</p>
     *
     * @return a <code>DatabaseOperation</code> to be executed after executing each test.
     * @throws Exception if an unexpected error occurs.
     */
    protected DatabaseOperation getTearDownOperation() throws Exception {
        return DatabaseOperation.DELETE;
    }

    /**
     * <p>Asserts that the data contained in specified database table matches the expected state. The specified SQL
     * query is used to obtain the desired data from the specified database table. The specified filename (relative to
     * classpath) is used to locate the XML file providing the expected data. Neither actual nor expected data is sorted
     * before comparison. Therefore the SQL query must return data in same order as provided by the expected data.</p>
     *
     * @param tableName a <code>String</code> providing the name of database table being verified.
     * @param expectedDataFileName a <code>String</code> providing the name of the XML file providing the expected data
     * for the specified table.
     * @param query a <code>String</code> providing the SQL query to be used to obtain the actual data from the
     * specified database table for purpose of current test.
     * @param assertionMessage a <code>String</code> providing the description of the error in case data assertion
     * fails.
     * @throws Exception if an unexpected error occurs.
     */
    protected void assertData(String tableName, String expectedDataFileName, String query, String assertionMessage)
        throws Exception {

        // Load actual data from specified database table using the provided SQL query
        ITable actualData = getConnection().createQueryTable(tableName, query);

        // Load expected data from XML file
        InputStream is = getClass().getClassLoader().getResourceAsStream(expectedDataFileName);
        IDataSet expectedDataSet = new FlatXmlDataSet(is);
        ITable expectedData = expectedDataSet.getTable(tableName);

        // Verify that actual data matches expected data
        try {
            Assertion.assertEquals(expectedData, actualData);
        } catch (AssertionFailedError e) {
            throw new AssertionFailedError(assertionMessage + " : " + e.getMessage());
        }
    }

    /**
     * <p>Asserts that the data contained in database matches the expected state. The specified filename (relative to
     * classpath) is used to locate the XML file providing the expected data.</p>
     *
     * @param expectedDataFileName a <code>String</code> providing the name of the XML file providing the expected data
     * for entire database.
     * @param assertionMessage a <code>String</code> providing the description of the error in case data assertion
     * fails.
     * @throws Exception if an unexpected error occurs.
     */
    protected void assertData(String expectedDataFileName, String assertionMessage) throws Exception {

        // Load expected data from XML file
        InputStream is = getClass().getClassLoader().getResourceAsStream(expectedDataFileName);
        IDataSet expectedDataSet = new FlatXmlDataSet(is);

        // Load actual data from specified database table using the provided SQL query
        IDataSet actualDataSet = getConnection().createDataSet(expectedDataSet.getTableNames());

        // Verify that actual data matches expected data
        try {
            Assertion.assertEquals(expectedDataSet, actualDataSet);
        } catch (AssertionFailedError e) {
            throw new AssertionFailedError(assertionMessage + " : " + e.getMessage());
        }
    }

    /**
     * <p>Verifies that the specified message which is expected to be displayed to user in response to request for
     * performing an action is in fact displayed to user by the page returned from the server.</p>
     *
     * @param message a <code>String</code> providing the message which is expected to be displayed to user by the
     * current page.
     */
    protected void assertDisplayedMessage(String message) {
        Assert.assertTrue(this.user.getCurrentPage().asText().indexOf(message) > 0);
    }

    /**
     * <p>Verifies that the current page displayed to user displays the review scorecard for submission identified by
     * the specified ID in context of specified project correctly.</p>
     *
     * @param projectName a <code>String</code> providing the name of the project.
     * @param submissionId a <code>String</code> providing the ID of selected submission.
     * @param reviewerHandle a <code>String</code> providing the handle for reviewer who have produced the review.
     * @throws Exception if an unexpected error occurs.
     */
    protected void assertReviewScorecardPage(String projectName, String submissionId, String reviewerHandle)
        throws Exception {

        // Verify that the page header is "Review Scorecard"
        Assert.assertEquals("Invalid page content header is displayed", "Review Scorecard ", this.user.getText("h3"));

        // Verify the project name
        HtmlTable headerTable = (HtmlTable) this.user.getContent().getHtmlElementsByTagName("table").get(0);
        Assert.assertEquals("Incorrect project name is displayed",
                            projectName, headerTable.getCellAt(0, 0).asText().trim());

        // Verify that the reviewer handle, submission ID, submitter handle, user role are displayed correctly
        Assert.assertTrue("Submission ID is not displayed correctly",
                          this.user.getCurrentPage().asText().indexOf("Submission: " + submissionId) > 0);
        Assert.assertTrue("Reviewer handle is not displayed correctly",
                          this.user.getCurrentPage().asText().indexOf("Reviewer: " + reviewerHandle) > 0);

        // Verify that the review scorecard is displayed correctly
        Review review = getReview(submissionId, reviewerHandle);
        List groupTables = this.user.getContent().getHtmlElementsByAttribute("table", "class", "scorecard");
        Map[] scorecardGroups = Scorecard.DESIGN_REVIEW.getGroups();
        Assert.assertEquals("Incorrect number of groups is displayed", scorecardGroups.length, groupTables.size());
        for (int i = 0; i < scorecardGroups.length; i++) {
            assertReviewScorecardGroup(scorecardGroups[i], (HtmlTable) groupTables.get(i), review);
        }
    }

    /**
     * <p>Gets the data set providing the initial data for <code>Registration</code> phase>.</p>
     *
     * @return an <code>IDataSet</code> for specified phase.
     * @throws Exception if an unexpected error occurs.
     */
    protected static IDataSet getRegistrationPhaseData() throws Exception {
        InputStream projectDataStream
            = AbstractTestCase.class.getClassLoader().getResourceAsStream(REGISTATION_PHASE_TEST_DATA_FILE_NAME);
        return new FlatXmlDataSet(projectDataStream);
    }

    /**
     * <p>Gets the data set providing the initial data for <code>Submission</code> phase>.</p>
     *
     * @return an <code>IDataSet</code> for specified phase.
     * @throws Exception if an unexpected error occurs.
     */
    protected static IDataSet getSubmissionPhaseData() throws Exception {
        InputStream projectDataStream
            = AbstractTestCase.class.getClassLoader().getResourceAsStream(SUBMISSION_PHASE_TEST_DATA_FILE_NAME);
        return new FlatXmlDataSet(projectDataStream);
    }

    /**
     * <p>Gets the data set providing the initial data for <code>Screening</code> phase>.</p>
     *
     * @return an <code>IDataSet</code> for specified phase.
     * @throws Exception if an unexpected error occurs.
     */
    protected static IDataSet getScreeningPhaseData() throws Exception {
        InputStream projectDataStream
            = AbstractTestCase.class.getClassLoader().getResourceAsStream(SCREENING_PHASE_TEST_DATA_FILE_NAME);
        return new FlatXmlDataSet(projectDataStream);
    }

    /**
     * <p>Gets the data set providing the initial data for <code>Review</code> phase>.</p>
     *
     * @return an <code>IDataSet</code> for specified phase.
     * @throws Exception if an unexpected error occurs.
     */
    protected static IDataSet getReviewPhaseData() throws Exception {
        InputStream projectDataStream
            = AbstractTestCase.class.getClassLoader().getResourceAsStream(REVIEW_PHASE_TEST_DATA_FILE_NAME);
        return new FlatXmlDataSet(projectDataStream);
    }

    /**
     * <p>Gets the data set providing the initial data for <code>Appeals</code> phase>.</p>
     *
     * @return an <code>IDataSet</code> for specified phase.
     * @throws Exception if an unexpected error occurs.
     */
    protected static IDataSet getAppealsPhaseData() throws Exception {
        InputStream projectDataStream
            = AbstractTestCase.class.getClassLoader().getResourceAsStream(APPEALS_PHASE_TEST_DATA_FILE_NAME);
        return new FlatXmlDataSet(projectDataStream);
    }

    /**
     * <p>Gets the data set providing the initial data for <code>Appeals Response</code> phase>.</p>
     *
     * @return an <code>IDataSet</code> for specified phase.
     * @throws Exception if an unexpected error occurs.
     */
    protected static IDataSet getAppealsResponsePhaseData() throws Exception {
        InputStream projectDataStream
            = AbstractTestCase.class.getClassLoader().getResourceAsStream(APPEALS_RESPONSE_PHASE_TEST_DATA_FILE_NAME);
        return new FlatXmlDataSet(projectDataStream);
    }

    /**
     * <p>Gets the data set providing the initial data for <code>Aggregation</code> phase>.</p>
     *
     * @return an <code>IDataSet</code> for specified phase.
     * @throws Exception if an unexpected error occurs.
     */
    protected static IDataSet getAggregationPhaseData() throws Exception {
        InputStream projectDataStream
            = AbstractTestCase.class.getClassLoader().getResourceAsStream(AGGREGATION_PHASE_TEST_DATA_FILE_NAME);
        return new FlatXmlDataSet(projectDataStream);
    }

    /**
     * <p>Gets the data set providing the initial data for <code>Aggregation Review</code> phase>.</p>
     *
     * @return an <code>IDataSet</code> for specified phase.
     * @throws Exception if an unexpected error occurs.
     */
    protected static IDataSet getAggregationReviewPhaseData() throws Exception {
        InputStream projectDataStream
            = AbstractTestCase.class.getClassLoader().getResourceAsStream(AGGREGATION_REVIEW_PHASE_TEST_DATA_FILE_NAME);
        return new FlatXmlDataSet(projectDataStream);
    }

    /**
     * <p>Gets the data set providing the initial data for <code>Final Fix</code> phase>.</p>
     *
     * @return an <code>IDataSet</code> for specified phase.
     * @throws Exception if an unexpected error occurs.
     */
    protected static IDataSet getFinalFixPhaseData() throws Exception {
        InputStream projectDataStream
            = AbstractTestCase.class.getClassLoader().getResourceAsStream(FINAL_FIX_PHASE_TEST_DATA_FILE_NAME);
        return new FlatXmlDataSet(projectDataStream);
    }

    /**
     * <p>Gets the data set providing the initial data for <code>Final Review</code> phase>.</p>
     *
     * @return an <code>IDataSet</code> for specified phase.
     * @throws Exception if an unexpected error occurs.
     */
    protected static IDataSet getFinalReviewPhaseData() throws Exception {
        InputStream projectDataStream
            = AbstractTestCase.class.getClassLoader().getResourceAsStream(FINAL_REVIEW_PHASE_TEST_DATA_FILE_NAME);
        return new FlatXmlDataSet(projectDataStream);
    }

    /**
     * <p>Gets the data set providing the initial data for <code>Approval</code> phase>.</p>
     *
     * @return an <code>IDataSet</code> for specified phase.
     * @throws Exception if an unexpected error occurs.
     */
    protected static IDataSet getApprovalPhaseData() throws Exception {
        InputStream projectDataStream
            = AbstractTestCase.class.getClassLoader().getResourceAsStream(APPROVAL_PHASE_TEST_DATA_FILE_NAME);
        return new FlatXmlDataSet(projectDataStream);
    }

    /**
     * <p>Gets the data set providing the initial data for <code>Registration</code> phase>.</p>
     *
     * @return an <code>IDataSet</code> for specified phase.
     * @throws Exception if an unexpected error occurs.
     */
    protected static IDataSet getCompletePhaseData() throws Exception {
        InputStream projectDataStream
            = AbstractTestCase.class.getClassLoader().getResourceAsStream(COMPLETE_PHASE_TEST_DATA_FILE_NAME);
        return new FlatXmlDataSet(projectDataStream);
    }

    /**
     * <p>Gets the data set providing the initial data for <code>lookup</code> tables.</p>
     *
     * @return an <code>IDataSet</code> for lookup tables.
     */
    protected static IDataSet getLookupData() {
        try {
            InputStream lookupsDataStream
                = AbstractTestCase.class.getClassLoader().getResourceAsStream(AbstractTestCase.LOOKUPS_TEST_DATA_FILE_NAME);
            return new FlatXmlDataSet(lookupsDataStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * <p>Gets the IDs for sample submissions which are used for testing purposes.</p>
     *
     * @return a <code>String</code> array providing the IDs for sample submissions.
     */
    protected static String[] getSubmissionIds() {
        try {
            IDataSet phaseData = getScreeningPhaseData();
            ITable table = phaseData.getTable("submission");
            String[] submissionIds = new String[table.getRowCount()];
            for (int i = 0; i < submissionIds.length; i++) {
                submissionIds[i] = String.valueOf(table.getValue(i, "submission_id"));
            }
            return submissionIds;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not obtain submission IDs");
        }
    }

    /**
     * <p>Gets the name and version for sample project which is used for testing purposes.</p>
     *
     * @return a <code>String</code> combining the name and version for sample project.
     */
    protected static String getProjectNameAndVersion() {
        try {
            IDataSet phaseData = getRegistrationPhaseData();
            ITable table = phaseData.getTable("project_info");
            String projectName = "";
            String projectVersion = "";
            for (int i = 0; i < table.getRowCount(); i++) {
                if (table.getValue(i, "project_info_type_id").equals("6")) {
                    projectName = String.valueOf(table.getValue(i, "value"));
                } else if (table.getValue(i, "project_info_type_id").equals("7")) {
                    projectVersion = String.valueOf(table.getValue(i, "value"));
                }
            }
            return projectName + " version " + projectVersion;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not obtain project name and version");
        }
    }

    /**
     * <p>Gets the screening review for the specified submission.</p>
     *
     * @param submissionId a <code>String</code> providing the ID of requested submission.
     * @return a <code>Review</code> providing the details for the screening review for the specified submission.
     * @throws Exception if an unexpected error occurs.
     */
    protected Review getScreeningReview(String submissionId) throws Exception {
        IDataSet reviewPhaseData = getReviewPhaseData();
        ITable table = reviewPhaseData.getTable("review");
        for (int i = 0; i < table.getRowCount(); i++) {
            if (String.valueOf(table.getValue(i, "submission_id")).equals(submissionId)) {
                return Review.loadReview(reviewPhaseData, String.valueOf(table.getValue(i, "review_id")));
            }
        }
        throw new IllegalStateException("No Screening Review found for submission [" + submissionId +"]");
    }

    /**
     * <p>Gets the review for the specified submission performed by the specified reviewer.</p>
     *
     * @param submissionId a <code>String</code> providing the ID of requested submission.
     * @return a <code>Review</code> providing the details for the review for the specified submission performed by the
     *         specified reviewer.
     * @throws Exception if an unexpected error occurs.
     */
    protected Review getReview(String submissionId, String reviewerHandle) throws Exception {
        Resource reviewer = Resource.loadResourceByHandle(getRegistrationPhaseData(), reviewerHandle);
        IDataSet reviewPhaseData = getAppealsPhaseData();
        ITable table = reviewPhaseData.getTable("review");
        for (int i = 0; i < table.getRowCount(); i++) {
            if (String.valueOf(table.getValue(i, "submission_id")).equals(submissionId)) {
                if (String.valueOf(table.getValue(i, "resource_id")).equals(reviewer.getId())) {
                    return Review.loadReview(reviewPhaseData, String.valueOf(table.getValue(i, "review_id")));
                }
            }
        }
        throw new IllegalStateException("No Review found for submission [" + submissionId +"] and reviewer ["
                                        + reviewerHandle +"]");
    }

    /**
     * <p>Gets the final review for the specified project.</p>
     *
     * @return a <code>Review</code> providing the details for the final review for the specified submission performed
     *         by the specified reviewer.
     * @throws Exception if an unexpected error occurs.
     */
    protected Review getFinalReview() throws Exception {
        IDataSet reviewPhaseData = new CompositeDataSet(getFinalFixPhaseData(), getApprovalPhaseData());
        return Review.loadReview(reviewPhaseData, "14");
    }

    /**
     * <p>Gets the aggregation review for the specified project.</p>
     *
     * @return a <code>Review</code> providing the details for the final review for the specified submission performed
     *         by the specified reviewer.
     * @throws Exception if an unexpected error occurs.
     */
    protected Review getAggregationReview() throws Exception {
        IDataSet reviewPhaseData = new CompositeDataSet(getAggregationReviewPhaseData(), getFinalFixPhaseData());
        return Review.loadReview(reviewPhaseData, "13");
    }

    /**
     * <p>Gets the approval review for the specified project.</p>
     *
     * @return a <code>Review</code> providing the details for the approval review performed by the approver.
     * @throws Exception if an unexpected error occurs.
     */
    protected Review getApprovalReview() throws Exception {
        return Review.loadReview(getCompletePhaseData(), "15");
    }

    /**
     * <p>Gets the details for the resources assigned to project currently opened. The method assumes that the main
     * page for selected project is currently displayed to user.</p>
     *
     * @return a <code>Map</code> mapping the <code>String</code> user handle to <code>String</code> array providing the
     *         details for the resources in order as they are displayed by current page.
     * @throws Exception if an unexpected error occurs.
     */
    protected Map getDisplayedResources() throws Exception {
        HtmlTable table = this.user.findPageSectionTable("Resources");
        Map resources = new LinkedHashMap();
        for (int i = 2; i < table.getRowCount() - 1; i++) {
            String[] registrant = new String[4];
            for (int j = 0; j < 4; j++) {
                registrant[j] = table.getCellAt(i, j).asText().trim();
            }
            resources.put(registrant[1], registrant);
        }
        return resources;
    }

    protected void printCurrentPage() {
        System.out.println("Response as XML : \n" + this.user.getContent().asXml());
        System.out.println("Response As Text : \n" + this.user.getContent().asText());
        System.out.println("Response Fullt : \n" + this.user.getCurrentPage().asXml());
    }
    /**
     * <p>Verifies that the details for specified scorecard group are displayed correctly.</p>
     *
     * @param scorecardGroup a <code>Map</code> providing the details for scorecard group.
     * @param htmlTable an <code>HtmlTable</code> providing the details for displayed scorecard group.
     * @param review a <code>Review</code> providing the details for the review.
     */
    private void assertReviewScorecardGroup(Map scorecardGroup, HtmlTable htmlTable, Review review) {
        Assert.assertEquals("Invalid scorecard group name is displayed",
                            scorecardGroup.get("name"), htmlTable.getCellAt(0, 0).asText().trim());
        LinkedHashMap groupSections = (LinkedHashMap) scorecardGroup.get("sections");
        Iterator groupSectionsIterator = groupSections.entrySet().iterator();
        Map scorecardSection = null;
        LinkedHashMap scorecardSectionQuestions = null;
        Iterator sectionQuestionsIterator = null;
        for (int i = 1; i < htmlTable.getRowCount() - 1; i++) {
            if (htmlTable.getCellAt(i, 0).getAttributeValue("class").equals("subheader")) {
                if (sectionQuestionsIterator != null) {
                    Assert.assertFalse("Not all section questions are displayed", sectionQuestionsIterator.hasNext());
                }
                scorecardSection = (Map) groupSectionsIterator.next();
                scorecardSectionQuestions = (LinkedHashMap) scorecardSection.get("questions");
                sectionQuestionsIterator = scorecardSectionQuestions.entrySet().iterator();
                Assert.assertEquals("Invalid section name is displayed",
                                    scorecardSection.get("name"), htmlTable.getCellAt(i, 0).asText().trim());
            } else {
                // Verify section question
                Map question = (Map) sectionQuestionsIterator.next();
                String questionId = (String) question.get("scorecard_question_id");
                Assert.assertTrue("Incorrect scorecard question is displayed",
                                  htmlTable.getCellAt(i, 0).asText().trim().
                                      endsWith((String) question.get("description")));
                Assert.assertEquals("Incorrect scorecard question weight is displayed",
                                    question.get("weight"), htmlTable.getCellAt(i, 1).asText().trim());
                Assert.assertEquals("Incorrect question score is displayed",
                                    review.getQuestionScore(questionId), htmlTable.getCellAt(i, 2).asText().trim());
                if (review.isAppealed(questionId)) {
                    if (review.isAppealResolved(questionId)) {
                        Assert.assertEquals("Incorrect appeal status",
                                            "Resolved", htmlTable.getCellAt(i, 4).asText().trim());
                    } else {
                        Assert.assertEquals("Incorrect appeal status",
                                            "Unresolved", htmlTable.getCellAt(i, 4).asText().trim());
                    }
                } else {
                    Assert.assertEquals("Incorrect appeal status",
                                        "", htmlTable.getCellAt(i, 4).asText().trim());
                }
            }
        }
        if (sectionQuestionsIterator != null) {
            Assert.assertFalse("Not all section questions are displayed", sectionQuestionsIterator.hasNext());
        }
        Assert.assertFalse("Not all group sections are displayed", groupSectionsIterator.hasNext());
    }

    private void clearDatabase() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("test_files/DeleteAll.sql"));
        IDatabaseConnection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.getConnection().createStatement();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0) {
                    if (!line.startsWith("--")) {
                        statement.executeUpdate(line);
                    }
                }
            }
        } catch (Exception e) {
            statement.close();
            connection.close();
        }
    }


}
