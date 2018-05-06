/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.accuracytests;

import com.topcoder.management.phase.autopilot.AutoPilot;
import com.topcoder.management.phase.autopilot.AutoPilotJob;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockAutoPilotSource;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectPilotAlternate;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectManagerAlternate;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockPhaseManager;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectManager;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockLogAlternate;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectPilot;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockLog;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockAutoPilot;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockAutoPilotSourceAlternate;
import com.topcoder.util.scheduler.Job;
import com.topcoder.util.scheduler.Scheduler;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.util.Calendar;

/**
 * <p>An accuracy test for {@link AutoPilotJob} class. Tests the methods for proper handling of valid input data and
 * producing accurate results. Passes the valid arguments to the methods and verifies that either the state of the
 * tested instance have been changed appropriately or a correct result is produced by the method.</p>
 *
 * @author isv
 * @version 1.0
 */
public class AutoPilotJobAccuracyTest extends TestCase {

    /**
     * <p>The instances of {@link AutoPilotJob} which are tested. These instances are initialized in {@link #setUp()}
     * method and released in {@link #tearDown()} method. Each instance is initialized using a separate constructor
     * provided by the tested class.<p>
     */
    private AutoPilotJob[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link AutoPilotJob} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link AutoPilotJob} class.
     */
    public static Test suite() {
        return new TestSuite(AutoPilotJobAccuracyTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        ConfigHelper.releaseNamespaces();
        ConfigHelper.loadConfiguration(new File("accuracy/config.xml"));

        initMocks();

        this.testedInstances = new AutoPilotJob[3];
        this.testedInstances[0] = new AutoPilotJob();
        this.testedInstances[1] = new AutoPilotJob(TestDataFactory.AUTO_PILOT_JOB_NAMESPACE,
                                                   AutoPilot.class.getName());
        this.testedInstances[2] = new AutoPilotJob(TestDataFactory.getAutoPilot(), TestDataFactory.OPERATOR);
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        this.testedInstances = null;
        ConfigHelper.releaseNamespaces();
        super.tearDown();
    }

    /**
     * <p>Accuracy test. Tests the {@link AutoPilotJob#AutoPilotJob()} constructor being provided with valid input
     * arguments.</p>
     *
     * <p>Verifies that the instance is initialized with default values. The {@link AutoPilotJob#getAutoPilot()}, {@link
     * AutoPilot#getOperator()} methods are also indirectly tested by this test. </p>
     */
    public void testConstructor_AutoPilotJob() throws Exception {
        Assert.assertEquals("The auto-pilot is not correct.",
                            TestDataFactory.getAutoPilot(), this.testedInstances[0].getAutoPilot());
        Assert.assertEquals("The operator is not correct.",
                            TestDataFactory.OPERATOR, this.testedInstances[0].getOperator());
    }

    /**
     * <p>Accuracy test. Tests the {@link AutoPilotJob#AutoPilotJob(String,String)} constructor being provided with
     * valid input arguments.</p>
     *
     * <p>Verifies that the instance is initialized based on the properties provided by the specified namespace. The
     * {@link AutoPilotJob#getAutoPilot()}, {@link AutoPilotJob#getOperator()} methods are also indirectly tested by
     * this test. </p>
     */
    public void testConstructor_AutoPilotJob_String_String() throws Exception {
        Assert.assertEquals("The auto-pilot is not correct.",
                            TestDataFactory.getAutoPilot(), this.testedInstances[1].getAutoPilot());
        Assert.assertEquals("The operator is not correct.",
                            AutoPilotJob.DEFAULT_OPERATOR, this.testedInstances[1].getOperator());
    }

    /**
     * <p>Accuracy test. Tests the {@link AutoPilotJob#AutoPilotJob(AutoPilot,String)} constructor being provided with
     * valid input arguments.</p>
     *
     * <p>Verifies that the instance is initialized based on the provided parameters. The {@link
     * AutoPilotJob#getAutoPilot()}, {@link AutoPilotJob#getOperator()} methods are also indirectly tested by this test.
     * </p>
     */
    public void testConstructor_AutoPilotJob_AutoPilot_String() throws Exception {
        Assert.assertEquals("The auto-pilot is not correct.",
                            TestDataFactory.getAutoPilot(), this.testedInstances[2].getAutoPilot());
        Assert.assertEquals("The operator is not correct.",
                            TestDataFactory.OPERATOR, this.testedInstances[2].getOperator());
    }

    /**
     * <p>Accuracy test. Tests the {@link AutoPilotJob#createJob(String, int)} method being provided withvalid input
     * arguments.</p>
     *
     * <p>Verifies that the returned job is initialized correctly.</p>
     */
    public void testCreateJob_String_int() {
        Job job = AutoPilotJob.createJob("Accuracy", 100);
        Assert.assertEquals("The interval is not correct", 100, job.getIntervalValue());
        Assert.assertEquals("The name is not correct", "Accuracy", job.getName());
        Assert.assertEquals("The time is not correct", 0, job.getStart().get(Calendar.HOUR_OF_DAY));
        Assert.assertEquals("The time is not correct", 0, job.getStart().get(Calendar.MINUTE));
        Assert.assertEquals("The time is not correct", 0, job.getStart().get(Calendar.SECOND));
        Assert.assertEquals("The time is not correct", 0, job.getStart().get(Calendar.MILLISECOND));
        Assert.assertEquals("The job type is not correct", Scheduler.JOB_TYPE_JAVA_CLASS, job.getJobType());
        Assert.assertEquals("The intervale unit is not correct", Calendar.MINUTE, job.getIntervalUnit());
    }

    private void initMocks() {
        MockAutoPilot.releaseState();
        MockAutoPilotSource.releaseState();
        MockAutoPilotSourceAlternate.releaseState();
        MockLog.releaseState();
        MockLogAlternate.releaseState();
        MockPhaseManager.releaseState();
        MockProjectManagerAlternate.releaseState();
        MockProjectManager.releaseState();
        MockProjectManagerAlternate.releaseState();
        MockProjectPilot.releaseState();
        MockProjectPilotAlternate.releaseState();

        MockAutoPilot.init();
        MockAutoPilotSource.init();
        MockAutoPilotSourceAlternate.init();
        MockLog.init();
        MockLogAlternate.init();
        MockPhaseManager.init();
        MockProjectManagerAlternate.init();
        MockProjectManager.init();
        MockProjectManagerAlternate.init();
        MockProjectPilot.init();
        MockProjectPilotAlternate.init();
    }
}
