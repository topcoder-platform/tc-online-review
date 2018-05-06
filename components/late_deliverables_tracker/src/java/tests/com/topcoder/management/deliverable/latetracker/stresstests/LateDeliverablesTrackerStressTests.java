/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.deliverable.latetracker.stresstests;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.management.deliverable.latetracker.LateDeliverableType;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTracker;
import com.topcoder.management.deliverable.latetracker.processors.LateDeliverableProcessorImpl;
import com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl;
import com.topcoder.util.log.LogFactory;

/**
 * <p>
 * Stress test case of the LateDeliverablesTracker.
 * </p>
 *
 * @author morehappiness
 * @version 1.0
 */
public class LateDeliverablesTrackerStressTests extends BaseStressTest {
    private LateDeliverablesTracker instance;

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception
     *             to jUnit.
     */
    public void setUp() throws Exception {
        super.setUp();

        LateDeliverablesRetrieverImpl retriever = new LateDeliverablesRetrieverImpl();
        retriever.configure(StressTestUtil.getConfig(LateDeliverablesRetrieverImpl.class));

        LateDeliverableProcessorImpl processor = new LateDeliverableProcessorImpl();
        processor.configure(StressTestUtil.getConfig(LateDeliverableProcessorImpl.class));
        
        Set<LateDeliverableType> typesSet = new HashSet<LateDeliverableType>();
        typesSet.add(LateDeliverableType.MISSED_DEADLINE);
        typesSet.add(LateDeliverableType.REJECTED_FINAL_FIX);
        instance = new LateDeliverablesTracker(retriever, processor, typesSet , LogFactory
                .getLog("stress_tests_logger"));
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception
     *             to jUnit.
     */
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a TestSuite for this test case.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(LateDeliverablesTrackerStressTests.class);

        return suite;
    }

    /**
     * <p>
     * Stress test for method <code>execute()</code>.
     * </p>
     * <p>
     * The test will run in the following environment:
     * <li>The database has 4000 projects, including 2000 active projects.</li>
     * </p>
     * The active project
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_execute() throws Exception {
        for (int i = 0; i < testCount; i++) {
            // set server
            int projectsCount = 2;
            int subCount = 1000;
            try {
                StressTestUtil.prepareProjectData(projectsCount, subCount, con);
                con.close();

                instance.execute();

                assertLateDeliverables(projectsCount, subCount);
            } finally {
                System.out.println("Run test: test_execute for " + testCount + " times takes "
                        + (new Date().getTime() - start) + "ms");
            }
        }
    }

    /**
     * <p>
     * Asserts the result of the LateDeliverableData.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    private void assertLateDeliverables(int projectCount, int subCount) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = StressTestUtil.createConnection(StressTestUtil
                    .loadProperties(StressTestUtil.DB_PROPERTIES_FILE));
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM late_deliverable");

            int insertedRecordsCount = 0;
            while (rs.next()) {
                insertedRecordsCount++;

                assertEquals("Should be equal", insertedRecordsCount * 3 + 2, rs.getLong(2)); // project
                                                                                              // phase
                                                                                              // id

                assertEquals("Should be equal", insertedRecordsCount * (subCount + 1) + subCount,
                        rs.getLong(3)); // resource
                // id

                assertEquals("Should be equal", 2, rs.getLong(4)); // deliverable id

                assertFalse("Should be false", rs.getBoolean(8)); // forgive ind
            }

            assertEquals("Should have " + projectCount + " records in the late_deliverable table.",
                    projectCount, insertedRecordsCount);
            System.out.println("The number of records inserted into late_deliverable table is "
                    + insertedRecordsCount);
        } finally {
            StressTestUtil.closeStatement(stmt);
            stmt = null;
        }
    }

}
