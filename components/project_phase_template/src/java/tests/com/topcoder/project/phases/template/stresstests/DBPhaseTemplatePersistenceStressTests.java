/**
 * Copyright (c) 2010, TopCoder, Inc. All rights reserved
 */
package com.topcoder.project.phases.template.stresstests;


import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.date.workdays.Workdays;
import com.topcoder.project.phases.Project;
import com.topcoder.project.phases.template.Helper;
import com.topcoder.project.phases.template.TestHelper;
import com.topcoder.project.phases.template.persistence.DBPhaseTemplatePersistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;


/**
 * <p> Stress test cases for <code>DBPhaseTemplatePersistence</code>. </p>
 *
 * <p> This class tests the multiple thread situation for the component. </p>
 *
 * @author yuanyeyuanye, TCSDEVELOPER
 * @version 1.0
 */
public class DBPhaseTemplatePersistenceStressTests extends BaseStressTest {

    /**
     * Template name used for test.
     */
    private final String templateName = "New_Design";

    /**
     * <p> instance of <code>DBPhaseTemplatePersistence</code> used for test. </p>
     */
    private DBPhaseTemplatePersistence dbPhaseTemplatePersistence;

    /**
     * <p> Instance of <code>Project</code> used for test. </p>
     */
    private Project project = null;

    /**
     * <p> Instance of <code>Workdays</code> used for test. </p>
     */
    private Workdays workdays = null;

    /**
     * <p> Instance of <code>Date</code> as the start date used for test. </p>
     */
    private Date startDate = null;

    /**
     * Sets the up.
     *
     * @throws Exception the exception occurs
     */
    @Before
    public void setUp() throws Exception {
        TestHelper.initializeDB();
        Helper.loadConfig(StressHelper.CONFIG_FILE);
        dbPhaseTemplatePersistence = new DBPhaseTemplatePersistence(TestHelper.getDBConnectionFactory(), "test");

        // create needed objects
        startDate = Calendar.getInstance().getTime();
        workdays = new DefaultWorkdays();
        project = new Project(startDate, (DefaultWorkdays) workdays);
    }

    /**
     * Tear down.
     *
     * @throws Exception the exception occurs
     */
    @After
    public void tearDown() throws Exception {
        Helper.clearConfig();
        TestHelper.clearDB();
    }

    /**
     * <p> Stress test for <code>DBPhaseTemplatePersistence#generatePhases(String, Project, long[])</code>. </p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_generatePhases_1_5() throws Throwable {
        long startTime = System.currentTimeMillis();

        numThreads = 1;
        timesToExecute = 5;

        Action action =
            new Action() {
                public void act() throws Exception {
                    dbPhaseTemplatePersistence.generatePhases(templateName, project, new long[]{7});

                    // Check the dependencies are correct.
                    Helper.assertDependencies(project, 8L, 6L);
                }
            };

        runMultiThreadActionShouldSuccess(action, timesToExecute);
        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }

    /**
     * <p> Stress test for <code>DBPhaseTemplatePersistence#generatePhases(String, Project, long[])</code>. </p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_generatePhases_1_50() throws Throwable {
        long startTime = System.currentTimeMillis();

        numThreads = 1;
        timesToExecute = 50;

        Action action =
            new Action() {
                public void act() throws Exception {
                    dbPhaseTemplatePersistence.generatePhases(templateName, project, new long[]{7});

                    // Check the dependencies are correct.
                    Helper.assertDependencies(project, 8L, 6L);
                }
            };

        runMultiThreadActionShouldSuccess(action, timesToExecute);
        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }

    /**
     * <p> Stress test for <code>DBPhaseTemplatePersistence#generatePhases(String, Project, long[])</code>. </p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_generatePhases_1_100() throws Throwable {
        long startTime = System.currentTimeMillis();

        numThreads = 1;
        timesToExecute = 100;

        Action action =
            new Action() {
                public void act() throws Exception {
                    dbPhaseTemplatePersistence.generatePhases(templateName, project, new long[]{7});

                    // Check the dependencies are correct.
                    Helper.assertDependencies(project, 8L, 6L);
                }
            };

        runMultiThreadActionShouldSuccess(action, timesToExecute);
        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }

    /**
     * <p> Stress test for <code>DBPhaseTemplatePersistence#generatePhases(String, Project, long[])</code>. </p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_generatePhases_1_500() throws Throwable {
        long startTime = System.currentTimeMillis();

        numThreads = 1;
        timesToExecute = 500;

        Action action =
            new Action() {
                public void act() throws Exception {
                    dbPhaseTemplatePersistence.generatePhases(templateName, project, new long[]{7});

                    // Check the dependencies are correct.
                    Helper.assertDependencies(project, 8L, 6L);
                }
            };

        runMultiThreadActionShouldSuccess(action, timesToExecute);
        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }
}
