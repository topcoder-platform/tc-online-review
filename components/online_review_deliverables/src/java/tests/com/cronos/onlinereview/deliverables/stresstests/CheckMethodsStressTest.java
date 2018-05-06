/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.deliverables.stresstests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;

import com.cronos.onlinereview.deliverables.AggregationDeliverableChecker;
import com.cronos.onlinereview.deliverables.AggregationReviewDeliverableChecker;
import com.cronos.onlinereview.deliverables.AppealResponsesDeliverableChecker;
import com.cronos.onlinereview.deliverables.CommittedReviewDeliverableChecker;
import com.cronos.onlinereview.deliverables.FinalFixesDeliverableChecker;
import com.cronos.onlinereview.deliverables.FinalReviewDeliverableChecker;
import com.cronos.onlinereview.deliverables.SqlDeliverableChecker;
import com.cronos.onlinereview.deliverables.SubmissionDeliverableChecker;
import com.cronos.onlinereview.deliverables.SubmitterCommentDeliverableChecker;
import com.cronos.onlinereview.deliverables.TestCasesDeliverableChecker;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;

import com.topcoder.management.deliverable.Deliverable;

import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;


/**
 * Stress test for method check of all the sub-classes.
 *
 * @author magicpig
 * @version 1.0
 */
public class CheckMethodsStressTest extends TestCase {
    /**
    * Configuration file to load.
    */
    private static final String CONFIGFILE = "stress/dbconfig.xml";

    /**
     * The namespace for the database connection factory.
     */
    private static final String NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /**
     * Storage for sql statements for inserting values in defaultusers table.
     */
    private static final String PRECONDITIONS_SQL_FILE =
        "test_files/stress/data.sql";
    
    /**
     * Connection name.
     */
    private static final String CNAME = "online";

    /**
     * The number of times for stress testing.
     */
    private static int stressNum = 20;

    /**
     * DBConnectionFactory instance used for testing.
     */
    private DBConnectionFactory dbfactory;

    /**
     * Sets Up the testing environment.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        // import namespace
        ConfigManager configManager = ConfigManager.getInstance();

        if (configManager.existsNamespace(NAMESPACE)) {
            configManager.removeNamespace(NAMESPACE);
        }

        configManager.add(CONFIGFILE);

        dbfactory = new DBConnectionFactoryImpl(NAMESPACE);

        Connection conn = dbfactory.createConnection();
        clearTables(conn);
        addTestData(conn);
        conn.close();
    }

    /**
     * Clears the testing environment.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
      Connection conn = dbfactory.createConnection();
      clearTables(conn);
      conn.close();
      
      ConfigManager configManager = ConfigManager.getInstance();

        if (configManager.existsNamespace(NAMESPACE)) {
            configManager.removeNamespace(NAMESPACE);
        }
    }

    /**
     * Adding data from sql file for testing purposes.
     * @param connection connection to database
     * @throws Exception wrap all exceptions
     */
    public static void addTestData(Connection connection) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(PRECONDITIONS_SQL_FILE));
        String sql;
        while ((sql = reader.readLine()) != null) {
            // ececute each sql insert statement,
            if (sql.length() > 2) {
                connection.createStatement().executeUpdate(sql);
            }
        }
    }
    
    /**
     * Deleting all data from specified table.
     * @param connection connection to database
     * @throws Exception wrap all exceptions
     */
    public static void clearTables(Connection connection) throws Exception {
        String[] tables =
            new String[] {"review_item_comment", "review_comment", "review_item", "review", "submission",
                "upload", "resource", "project_phase", "project", "scorecard_question", "scorecard_section",
                "scorecard_group", "scorecard"};
        for (int i = 0; i < tables.length; i++) {
            String query = "DELETE FROM " + tables[i];
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        }
    }
    
    /**
     * Test of AggregationDeliverableChecker#check.
     *
     * @throws Exception to JUnit
     */
    public void testAggregationDeliverableChecker_Check()
        throws Exception {
        CheckMethodTest(new AggregationDeliverableChecker(dbfactory, CNAME),
            "AggregationDeliverableChecker");
    }

    /**
     * Test of AggregationReviewDeliverableChecker#check.
     *
     * @throws Exception to JUnit
     */
    public void testAggregationReviewDeliverableChecker_Check()
        throws Exception {
        CheckMethodTest(new AggregationReviewDeliverableChecker(dbfactory, CNAME),
            "AggregationReviewDeliverableChecker");
    }

    /**
     * Test of AppealResponsesDeliverableChecker#check.
     *
     * @throws Exception to JUnit
     */
    public void testAppealResponsesDeliverableChecker_Check()
        throws Exception {
        CheckMethodTest(new AppealResponsesDeliverableChecker(dbfactory, CNAME),
            "AppealResponsesDeliverableChecker");
    }

    /**
     * Test of CommittedReviewDeliverableChecker#check.
     *
     * @throws Exception to JUnit
     */
    public void testCommittedReviewDeliverableChecker_Check()
        throws Exception {
        CheckMethodTest(new CommittedReviewDeliverableChecker(dbfactory, CNAME),
            "CommittedReviewDeliverableChecker");
    }

    /**
     * Test of FinalFixesDeliverableChecker#check.
     *
     * @throws Exception to JUnit
     */
    public void testFinalFixesDeliverableChecker_Check()
        throws Exception {
        CheckMethodTest(new FinalFixesDeliverableChecker(dbfactory, CNAME),
            "FinalFixesDeliverableChecker");
    }

    /**
     * Test of FinalReviewDeliverableChecker#check.
     *
     * @throws Exception to JUnit
     */
    public void testFinalReviewDeliverableChecker_Check()
        throws Exception {
        CheckMethodTest(new FinalReviewDeliverableChecker(dbfactory, CNAME),
            "FinalReviewDeliverableChecker");
    }

    /**
     * Test of SubmissionDeliverableChecker#check.
     *
     * @throws Exception to JUnit
     */
    public void testSubmissionDeliverableChecker_Check()
        throws Exception {
        CheckMethodTest(new SubmissionDeliverableChecker(dbfactory, CNAME),
            "SubmissionDeliverableChecker");
    }

    /**
     * Test of SubmitterCommentDeliverableChecker#check.
     *
     * @throws Exception to JUnit
     */
    public void testSubmitterCommentDeliverableChecker_Check()
        throws Exception {
        CheckMethodTest(new SubmitterCommentDeliverableChecker(dbfactory, CNAME),
            "SubmitterCommentDeliverableChecker");
    }

    /**
     * Test of TestCasesDeliverableChecker#check.
     *
     * @throws Exception to JUnit
     */
    public void testTestCasesDeliverableChecker_Check()
        throws Exception {
        CheckMethodTest(new TestCasesDeliverableChecker(dbfactory, CNAME),
            "TestCasesDeliverableChecker");
    }

    /**
     * Stress test for method check.
     *
     * @param tester the SqlDeliverableChecker instance to be tested
     * @param className the sub-class name of SqlDeliverableChecker.
     *
     * @throws Exception to JUnit
     */
    private void CheckMethodTest(SqlDeliverableChecker tester, String className)
        throws Exception {
        // get start time
        long start = System.currentTimeMillis();

        for (int i = 0; i < stressNum; i++) {
            Deliverable deliverable = new Deliverable(1, 1, 1, new Long(1), true);

            tester.check(deliverable);

            assertNotNull("Fail to invoke " + className + "#check",
                deliverable.getCompletionDate());
        }

        System.out.println("Invoking " + className + "#check " + stressNum
            + " times costs: " + (System.currentTimeMillis() - start) + " ms.");
    }
}
