/**
 * Copyright (c) 2010, TopCoder, Inc. All rights reserved
 */
package com.topcoder.project.phases.template.stresstests;


import com.topcoder.date.workdays.Workdays;
import com.topcoder.date.workdays.WorkdaysUnitOfTime;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.Project;
import com.topcoder.project.phases.template.DefaultPhaseTemplate;
import com.topcoder.project.phases.template.Helper;
import com.topcoder.project.phases.template.TestHelper;
import com.topcoder.project.phases.template.persistence.DBPhaseTemplatePersistence;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static com.topcoder.project.phases.template.stresstests.StressHelper.CONFIG_FILE;
import static com.topcoder.project.phases.template.stresstests.StressHelper.NAMESPACE;


/**
 * <p> Stress test cases for <code>DefaultProjectPhase</code>. </p>
 *
 * <p> This class tests the multiple thread situation for the component. </p>
 *
 * @author yuanyeyuanye, TCSDEVELOPER
 * @version 1.0
 */
public class ProjectPhaseStressTests extends BaseStressTest {

    /**
     * Work Time used for test.
     */
    private final long workTime = 604800000;


    /**
     * Template name used for test.
     */
    private final String templateName = "New_Design";

    /**
     * <p> instance of <code>DefaultPhaseTemplate</code> used for test cases. </p>
     */
    private DefaultPhaseTemplate defaultPhaseTemplate;

    /**
     * <p> instance Calendar used for test cases. </p>
     */
    private Calendar calendar = null;

    /**
     * <p> Date used for test cases. </p>
     */
    private Date fixedDate = null;


    /**
     * Sets the up.
     *
     * @throws Exception the exception occurs
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();

        // load configurations
        Helper.loadConfig(CONFIG_FILE);

        // create the defaultPhaseTemplate
        defaultPhaseTemplate = new DefaultPhaseTemplate(NAMESPACE);

        // set the db persistence
        defaultPhaseTemplate.setPersistence(new DBPhaseTemplatePersistence(TestHelper.getDBConnectionFactory(), "test"));

        // initializeDB
        TestHelper.initializeDB();

        calendar = Calendar.getInstance();
        calendar.set(2002, 2, 10);
        fixedDate = calendar.getTime();
        calendar.set(2002, 1, 11);
    }

    /**
     * Tears down the test environment.
     *
     * @throws Exception to Junit.
     */
    @After
    public void tearDown() throws Exception {
        super.tearDown();

        Helper.clearConfig();
        TestHelper.clearDB();
    }


    /**
     * <p> Stress test for <code>applyTemplate(String, long[], Date)</code>. </p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_applyTemplate_String_LongArray_Date_5_5() throws Throwable {
        long startTime = System.currentTimeMillis();

        numThreads = 5;
        timesToExecute = 5;

        Action action =
            new Action() {
                public void act() throws Exception {
                    Project project = defaultPhaseTemplate.applyTemplate(templateName, new long[]{3, 4}, new Date());

                    // Verify the defaultPhaseTemplate phases should be 8.
                    Assert.assertEquals(8, project.getAllPhases().length);
                }
            };

        runMultiThreadActionShouldSuccess(action, timesToExecute);

        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }

    /**
     * <p> Stress test for <code>applyTemplate(String, long[], Date)</code>. </p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_applyTemplate_String_LongArray_Date_2_20() throws Throwable {
        long startTime = System.currentTimeMillis();

        numThreads = 2;
        timesToExecute = 20;

        Action action =
            new Action() {
                public void act() throws Exception {
                    Project project = defaultPhaseTemplate.applyTemplate(templateName, new long[]{3, 4}, new Date());

                    // Verify the defaultPhaseTemplate phases should be 8.
                    Assert.assertEquals(8, project.getAllPhases().length);
                }
            };

        runMultiThreadActionShouldSuccess(action, timesToExecute);

        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }

    /**
     * <p> Stress test for <code>applyTemplate(String, long[], Date)</code>. </p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_applyTemplate_String_LongArray_Date_2_100() throws Throwable {
        long startTime = System.currentTimeMillis();

        numThreads = 2;
        timesToExecute = 100;

        Action action =
            new Action() {
                public void act() throws Exception {
                    Project project = defaultPhaseTemplate.applyTemplate(templateName, new long[]{3, 4}, new Date());

                    // Verify the defaultPhaseTemplate phases should be 8.
                    Assert.assertEquals(8, project.getAllPhases().length);
                }
            };

        runMultiThreadActionShouldSuccess(action, timesToExecute);

        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }

    /**
     * <p> Stress test for <code>applyTemplate(String, long[], long, long, Date)</code>. </p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_applyTemplate_String_LongArray_Long_Long_Date_5_5() throws Throwable {
        long startTime = System.currentTimeMillis();

        numThreads = 5;
        timesToExecute = 5;

        Action action =
            new Action() {
                public void act() throws Exception {
                    Workdays workDays = defaultPhaseTemplate.getWorkdays();
                    fixedDate = workDays.add(new Date(), WorkdaysUnitOfTime.MINUTES, (int) workTime / 60000);

                    Project project = defaultPhaseTemplate.applyTemplate("New_Design", new long[]{5}, 1, 2, fixedDate);

                    Assert.assertEquals(9, project.getAllPhases().length);
                }
            };

        runMultiThreadActionShouldSuccess(action, timesToExecute);

        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }


    /**
     * <p> Stress test for <code>applyTemplate(String, long[], long, long, Date)</code>. </p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_applyTemplate_String_LongArray_Long_Long_Date_2_20() throws Throwable {
        long startTime = System.currentTimeMillis();

        numThreads = 2;
        timesToExecute = 20;

        Action action =
            new Action() {
                public void act() throws Exception {
                    Workdays workDays = defaultPhaseTemplate.getWorkdays();
                    fixedDate = workDays.add(new Date(), WorkdaysUnitOfTime.MINUTES, (int) workTime / 60000);

                    Project project = defaultPhaseTemplate.applyTemplate("New_Design", new long[]{5}, 1, 2, fixedDate);

                    Assert.assertEquals(9, project.getAllPhases().length);
                }
            };

        runMultiThreadActionShouldSuccess(action, timesToExecute);

        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }

    /**
     * <p> Stress test for <code>applyTemplate(String, long[], long, long, Date)</code>. </p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_applyTemplate_String_LongArray_Long_Long_Date_2_100() throws Throwable {
        long startTime = System.currentTimeMillis();

        numThreads = 2;
        timesToExecute = 100;

        Action action =
            new Action() {
                public void act() throws Exception {
                    Workdays workDays = defaultPhaseTemplate.getWorkdays();
                    fixedDate = workDays.add(new Date(), WorkdaysUnitOfTime.MINUTES, (int) workTime / 60000);

                    Project project = defaultPhaseTemplate.applyTemplate("New_Design", new long[]{5}, 1, 2, fixedDate);

                    Assert.assertEquals(9, project.getAllPhases().length);
                }
            };

        runMultiThreadActionShouldSuccess(action, timesToExecute);

        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }


    /**
     * <p> Stress test for <code>applyTemplate(String, long[])</code>. </p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_applyTemplate_String_LongArray_5_5() throws Throwable {
        long startTime = System.currentTimeMillis();

        numThreads = 5;
        timesToExecute = 5;

        Action action =
            new Action() {
                public void act() throws Exception {
                    Project project = defaultPhaseTemplate.applyTemplate(templateName, new long[]{3, 4});

                    // Verify the phase number should be 8 after the defaultPhaseTemplate has been applied.
                    Assert.assertEquals(8, project.getAllPhases().length);
                }
            };

        runMultiThreadActionShouldSuccess(
            action, timesToExecute);

        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }

    /**
     * <p> Stress test for <code>applyTemplate(String, long[])</code>. </p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_applyTemplate_String_LongArray_2_20() throws Throwable {
        long startTime = System.currentTimeMillis();

        numThreads = 2;
        timesToExecute = 20;

        Action action =
            new Action() {
                public void act() throws Exception {
                    Project project = defaultPhaseTemplate.applyTemplate(templateName, new long[]{3, 4});

                    // Verify the phase number should be 8 after the defaultPhaseTemplate has been applied.
                    Assert.assertEquals(8, project.getAllPhases().length);
                }
            };

        runMultiThreadActionShouldSuccess(
            action, timesToExecute);

        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }

    /**
     * <p> Stress test for <code>applyTemplate(String, long[])</code>. </p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_applyTemplate_String_LongArray_2_100() throws Throwable {
        long startTime = System.currentTimeMillis();

        numThreads = 2;
        timesToExecute = 100;

        Action action =
            new Action() {
                public void act() throws Exception {
                    Project project = defaultPhaseTemplate.applyTemplate(templateName, new long[]{3, 4});

                    // Verify the phase number should be 8 after the defaultPhaseTemplate has been applied.
                    Assert.assertEquals(8, project.getAllPhases().length);
                }
            };

        runMultiThreadActionShouldSuccess(
            action, timesToExecute);

        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }

    /**
     * <p> Stress test for <code>applyTemplate(String templateName, long[] leftOutPhaseIds, long varPhaseId, long
     * fixedPhaseId, Date fixedPhaseStartDate, Date startDate)</code>. </p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_applyTemplate_5_5() throws Throwable {
        long startTime = System.currentTimeMillis();

        numThreads = 5;
        timesToExecute = 5;

        Action action =
            new Action() {
                public void act() throws Exception {
                    Workdays workDays = defaultPhaseTemplate.getWorkdays();

                    // the fixeDate has been set to the start date of phase 2
                    fixedDate = workDays.add(calendar.getTime(), WorkdaysUnitOfTime.MINUTES, (int) workTime / 60000);

                    Project project = defaultPhaseTemplate.applyTemplate(templateName,
                                                                         new long[]{7}, 1, 2, fixedDate, calendar.getTime());
                    Phase phase = getPhase(project, 2);

                    // Verify the start time should be correct.
                    Assert.assertEquals(0, (phase.calcStartDate().getTime() - fixedDate.getTime()) / 60000);

                    // Verify the phases should be 9.
                    Assert.assertEquals(9, project.getAllPhases().length);
                }
            };

        runMultiThreadActionShouldSuccess(
            action, timesToExecute);

        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }

    /**
     * <p> Stress test for <code>applyTemplate(String templateName, long[] leftOutPhaseIds, long varPhaseId, long
     * fixedPhaseId, Date fixedPhaseStartDate, Date startDate)</code>. Test the action should be correct  in heavy
     * load.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_applyTemplate_heavy_load_2_20() throws Throwable {

        numThreads = 2;
        timesToExecute = 20;

        long startTime = System.currentTimeMillis();
        Action action =
            new Action() {
                public void act() throws Exception {
                    // set the fixeDate to the start date of phase 2
                    Workdays workDays = defaultPhaseTemplate.getWorkdays();

                    // the fixeDate has been set to the start date of phase 2
                    fixedDate = workDays.add(calendar.getTime(), WorkdaysUnitOfTime.MINUTES, (int) workTime / 60000);

                    Project project = defaultPhaseTemplate.applyTemplate(templateName,
                                                                         new long[]{7}, 1, 2, fixedDate, calendar.getTime());
                    // verify the start date is adjustied
                    Phase phase = getPhase(project, 2);

                    // Verify the start time shold be correct.
                    Assert.assertEquals(0, (phase.calcStartDate().getTime() - fixedDate.getTime()) / 60000);
                }
            };

        runMultiThreadActionShouldSuccess(action, timesToExecute);
        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }

    /**
     * <p> Stress test for <code>applyTemplate(String templateName, long[] leftOutPhaseIds, long varPhaseId, long
     * fixedPhaseId, Date fixedPhaseStartDate, Date startDate)</code>. Test the action should be correct  in heavy
     * load.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_applyTemplate_heavy_load_2_100() throws Throwable {

        numThreads = 2;
        timesToExecute = 100;

        long startTime = System.currentTimeMillis();
        Action action =
            new Action() {
                public void act() throws Exception {
                    // set the fixeDate to the start date of phase 2
                    Workdays workDays = defaultPhaseTemplate.getWorkdays();

                    // the fixeDate has been set to the start date of phase 2
                    fixedDate = workDays.add(calendar.getTime(), WorkdaysUnitOfTime.MINUTES, (int) workTime / 60000);

                    Project project = defaultPhaseTemplate.applyTemplate(templateName,
                                                                         new long[]{7}, 1, 2, fixedDate, calendar.getTime());
                    // verify the start date is adjustied
                    Phase phase = getPhase(project, 2);

                    // Verify the start time shold be correct.
                    Assert.assertEquals(0, (phase.calcStartDate().getTime() - fixedDate.getTime()) / 60000);
                }
            };

        runMultiThreadActionShouldSuccess(action, timesToExecute);
        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }


    /**
     * <p> Get a phase from the project with the given id. </p>
     *
     * @param project the project to get the phase
     * @param phaseId the phase id
     *
     * @return the Phase with given id
     */
    private Phase getPhase(Project project, long phaseId) {
        Phase[] phases = project.getAllPhases();
        for (int i = 0; i < phases.length; ++i) {
            if (phases[i].getId() == phaseId) {
                return phases[i];
            }
        }
        return null;
    }
}
