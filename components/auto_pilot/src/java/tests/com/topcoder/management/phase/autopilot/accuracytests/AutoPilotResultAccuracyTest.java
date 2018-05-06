/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.accuracytests;

import com.topcoder.management.phase.autopilot.AutoPilotResult;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>An accuracy test for {@link AutoPilotResult} class. Tests the methods for proper handling of valid input data and
 * producing accurate results. Passes the valid arguments to the methods and verifies that either the state of the
 * tested instance have been changed appropriately or a correct result is produced by the method.</p>
 *
 * @author isv
 * @version 1.0
 */
public class AutoPilotResultAccuracyTest extends TestCase {

    /**
     * <p>The instances of {@link AutoPilotResult} which are tested. These instances are initialized in {@link #setUp()}
     * method and released in {@link #tearDown()} method. Each instance is initialized using a separate constructor
     * provided by the tested class.<p>
     */
    private AutoPilotResult[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link AutoPilotResult} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link AutoPilotResult} class.
     */
    public static Test suite() {
        return new TestSuite(AutoPilotResultAccuracyTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testedInstances = new AutoPilotResult[1];
        this.testedInstances[0] = new AutoPilotResult(TestDataFactory.VALID_PROJECT_ID,
                                                      TestDataFactory.VALID_ENDED_PHASES_COUNT,
                                                      TestDataFactory.VALID_STARTED_PHASES_COUNT);
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        this.testedInstances = null;
        super.tearDown();
    }

    /**
     * <p>Accuracy test. Tests the {@link AutoPilotResult#AutoPilotResult(long,int,int)} constructor being provided with
     * valid input arguments.</p>
     *
     * <p>Verifies that the constructor initializes the state of the instances based on provided arguments correctly.
     * The {@link AutoPilotResult#getProjectId()}, {@link AutoPilotResult#getPhaseEndedCount()}, {@link
     * AutoPilotResult#getPhaseStartedCount()} methods are also indirectly tested by this test.</p>
     */
    public void testConstructor_long_int_int() {
        Assert.assertEquals("The project ID is not saved correctly.",
                            TestDataFactory.VALID_PROJECT_ID, this.testedInstances[0].getProjectId());
        Assert.assertEquals("The number of ended phases is not saved correctly.",
                            TestDataFactory.VALID_ENDED_PHASES_COUNT, this.testedInstances[0].getPhaseEndedCount());
        Assert.assertEquals("The number of started phases is not saved correctly.",
                            TestDataFactory.VALID_STARTED_PHASES_COUNT, this.testedInstances[0].getPhaseStartedCount());
    }

    /**
     * <p>Accuracy test. Tests the {@link AutoPilotResult#aggregate(AutoPilotResult)} method for proper behavior.</p>
     *
     * <p>Verifies that the method updates the number of ended and started phases correctly.</p>
     */
    public void testAggregate_AutoPilotResult() {
        int currentEndedPhasesCount;
        int currentStartedPhasesCount;
        AutoPilotResult differentResult = TestDataFactory.getAutoPilotResult();
        for (int i = 0; i < this.testedInstances.length; i++) {
            currentEndedPhasesCount = this.testedInstances[i].getPhaseEndedCount();
            currentStartedPhasesCount = this.testedInstances[i].getPhaseStartedCount();
            this.testedInstances[i].aggregate(differentResult);
            Assert.assertEquals("The number of ended phases is not updated correctly.",
                                currentEndedPhasesCount + differentResult.getPhaseEndedCount(),
                                this.testedInstances[i].getPhaseEndedCount());
            Assert.assertEquals("The number of started phases is not updated correctly.",
                                currentStartedPhasesCount + differentResult.getPhaseStartedCount(),
                                this.testedInstances[i].getPhaseStartedCount());
        }
    }
}
