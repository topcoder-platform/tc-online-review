/**
 * Copyright (c) 2010, TopCoder, Inc. All rights reserved
 */
package com.topcoder.project.phases.template.stresstests;


import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.date.workdays.Workdays;
import com.topcoder.project.phases.Project;
import com.topcoder.project.phases.template.Helper;
import com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;


/**
 * <p> Stress test cases for <code>XmlPhaseTemplatePersistence</code>. </p>
 *
 * <p> This class tests the multiple thread situation for the component. </p>
 *
 * @author yuanyeyuanye, TCSDEVELOPER
 * @version 1.0
 */
public class XmlPhaseTemplatePersistenceStressTests extends BaseStressTest {

    /**
     * Design template name used for test.
     */
    private final String templateName = "new_file";

    /**
     * <p> Instance of <code>XmlPhaseTemplatePersistence</code> used in this test. </p>
     */
    private XmlPhaseTemplatePersistence xmlPhaseTemplatePersistence = null;

    /**
     * <p> Instance of <code>Project</code> used for test. </p>
     */
    private Project project = null;

    /**
     * <p> Instance of <code>Workdays</code> used for test. </p>
     */
    private Workdays workdays = null;


    /**
     * <p> Instance of <code>Date</code> as the start date used in this test. </p>
     */
    private Date startDate = null;

    /**
     * Sets the up.
     *
     * @throws Exception the exception occurs
     */
    @Before
    public void setUp() throws Exception {

        // load configurations
        Helper.loadConfig(StressHelper.CONFIG_FILE);

        // create persistence instances
        xmlPhaseTemplatePersistence = new XmlPhaseTemplatePersistence(new String[]{StressHelper.TEMPLATE_FILE_DESIGN});

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
    }


    /**
     * <p> Stress test for <code>XmlPhaseTemplatePersistence#generatePhases(String, Project, long[])</code>. </p>
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
                    // generate phases
                    xmlPhaseTemplatePersistence.generatePhases(templateName, project, new long[]{3, 5});

                    // check the generated phases
                    Helper.assertDependencies(project, 6, 4L, 2L, 1L);
                }
            };

        runMultiThreadActionShouldSuccess(action, timesToExecute);
        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }

    /**
     * <p> Stress test for <code>XmlPhaseTemplatePersistence#generatePhases(String, Project, long[])</code>. </p>
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
                    // generate phases
                    xmlPhaseTemplatePersistence.generatePhases(templateName, project, new long[]{3, 5});

                    // check the generated phases
                    Helper.assertDependencies(project, 6, 4L, 2L, 1L);
                }
            };

        runMultiThreadActionShouldSuccess(action, timesToExecute);
        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }

    /**
     * <p> Stress test for <code>XmlPhaseTemplatePersistence#generatePhases(String, Project, long[])</code>. </p>
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
                    // generate phases
                    xmlPhaseTemplatePersistence.generatePhases(templateName, project, new long[]{3, 5});

                    // check the generated phases
                    Helper.assertDependencies(project, 6, 4L, 2L, 1L);
                }
            };

        runMultiThreadActionShouldSuccess(action, timesToExecute);
        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }

    /**
     * <p> Stress test for <code>XmlPhaseTemplatePersistence#generatePhases(String, Project, long[])</code>. </p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void test_generatePhases_1_500() throws Throwable {
        long startTime = System.currentTimeMillis();

        numThreads = 1;
        timesToExecute = 100;

        Action action =
            new Action() {
                public void act() throws Exception {
                    // generate phases
                    xmlPhaseTemplatePersistence.generatePhases(templateName, project, new long[]{3, 5});

                    // check the generated phases
                    Helper.assertDependencies(project, 6, 4L, 2L, 1L);
                }
            };

        runMultiThreadActionShouldSuccess(action, timesToExecute);
        outputTestResult(getLogMessage(startTime, numThreads, timesToExecute));
    }
}
