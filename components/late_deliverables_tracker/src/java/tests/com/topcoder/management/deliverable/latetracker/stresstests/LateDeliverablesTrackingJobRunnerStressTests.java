/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.deliverable.latetracker.stresstests;

import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.Date;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackingJobRunner;
import com.topcoder.management.deliverable.latetracker.utility.LateDeliverablesTrackingUtility;
import com.topcoder.util.log.LogFactory;
import com.topcoder.util.scheduler.processor.JobProcessor;
import com.topcoder.util.scheduler.scheduling.Job;
import com.topcoder.util.scheduler.scheduling.ScheduledJobRunner;
import com.topcoder.util.scheduler.scheduling.Scheduler;
import com.topcoder.util.scheduler.scheduling.Second;
import com.topcoder.util.scheduler.scheduling.persistence.ConfigurationObjectScheduler;

/**
 * <p>
 * Stress test case of the LateDeliverablesTrackingJobRunner.
 * </p>
 *
 * @author morehappiness
 * @version 1.0
 */
public class LateDeliverablesTrackingJobRunnerStressTests extends BaseStressTest {

    /**
     * The LateDeliverablesTrackingJobRunner instance used to run as a job.
     */
    private LateDeliverablesTrackingJobRunner jobRunner;

    /**
     * The scheduler to schedule the job.
     */
    private Scheduler scheduler;

    /**
     * The job processor to manage the job.
     */
    private JobProcessor jobProcessor;

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

        ConfigurationObject configObject = StressTestUtil.getConfig(LateDeliverablesTrackingUtility.class);

        ConfigurationObject schedulerConfig = configObject.getChild("schedulerConfig");

        scheduler = new ConfigurationObjectScheduler(schedulerConfig);

        Job job = scheduler.getJob((String) configObject.getPropertyValue("jobName"));
        ConfigurationObject jobConfig = configObject.getChild("jobConfig");

        jobRunner = (LateDeliverablesTrackingJobRunner) job.getRunningJob();
        jobRunner.configure(jobConfig);

        job.setIntervalUnit(new Second());
        job.setIntervalValue(1);

        jobProcessor = new JobProcessor(scheduler, 1000, LogFactory.getLog("stress_tests_logger")); // small reload time
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
        TestSuite suite = new TestSuite(LateDeliverablesTrackingJobRunnerStressTests.class);

        return suite;
    }

    /**
     * <p>
     * Stress test for method <code>run()</code>.
     * </p>
     * <p>
     * The test will run in the following environment:
     * <li>The database has 4000 projects, including 2000 active projects.</li>
     * <li>The job will be intersected with a small interval.</li>
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void test_run() throws Exception {
        StressTestUtil.prepareProjectData(2, 1, con);
        jobProcessor.start();

        // sleep the current thread to wait the job to execute some times
        Thread.sleep(10000);

        assertEquals("The job should still be running", ScheduledJobRunner.NOT_STARTED, jobRunner.getRunningStatus());

        System.out.println("Run test: test_run for " + testCount + " times takes " + (new Date().getTime() - start)
            + "ms");
    }
}
