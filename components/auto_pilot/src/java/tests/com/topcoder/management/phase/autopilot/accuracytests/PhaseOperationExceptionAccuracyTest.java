/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.accuracytests;

import com.topcoder.management.phase.autopilot.PhaseOperationException;
import com.topcoder.project.phases.Phase;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>An accuracy test for {@link PhaseOperationException} class. Tests the methods for proper handling of valid input
 * data and producing accurate results. Passes the valid arguments to the methods and verifies that either the state of
 * the tested instance have been changed appropriately or a correct result is produced by the method.</p>
 *
 * @author isv
 * @version 1.0
 */
public class PhaseOperationExceptionAccuracyTest extends TestCase {

    /**
     * <p>The instances of {@link PhaseOperationException} which are tested. These instances are initialized in {@link
     * #setUp()} method and released in {@link #tearDown()} method. Each instance is initialized using a separate
     * constructor provided by the tested class.<p>
     */
    private PhaseOperationException[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link PhaseOperationException} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link PhaseOperationException} class.
     */
    public static Test suite() {
        return new TestSuite(PhaseOperationExceptionAccuracyTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testedInstances = new PhaseOperationException[3];
        this.testedInstances[0] = new PhaseOperationException();
        this.testedInstances[1] = new PhaseOperationException(TestDataFactory.EXCEPTION_PROJECT_ID,
                                                              TestDataFactory.getExceptionPhase(),
                                                              TestDataFactory.EXCEPTION_MESSAGE);
        this.testedInstances[2] = new PhaseOperationException(TestDataFactory.EXCEPTION_PROJECT_ID,
                                                              TestDataFactory.getExceptionPhase(),
                                                              TestDataFactory.EXCEPTION_MESSAGE,
                                                              TestDataFactory.EXCEPTION_CAUSE);
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
     * <p>Accuracy test. Tests the {@link PhaseOperationException#PhaseOperationException()} constructor being provided
     * with valid input arguments.</p>
     *
     * <p>Verifies that the project ID, phase, message and cause are not initialized.</p>
     */
    public void testConstructor_PhaseOperationException_() {
        Assert.assertEquals("The project ID must not be initialized", -1, this.testedInstances[0].getProjectId());
        Assert.assertNull("The message must not be initialized", this.testedInstances[0].getPhase());
        Assert.assertNull("The message must not be initialized", this.testedInstances[0].getMessage());
        Assert.assertNull("The cause must not be initialized", this.testedInstances[0].getCause());
    }

    /**
     * <p>Accuracy test. Tests the {@link PhaseOperationException#PhaseOperationException(long,Phase,String)}
     * constructor being provided with valid input arguments.</p>
     *
     * <p>Verifies that the project ID, phase, message are initialized and the cause is not initialized.</p>
     */
    public void testConstructor_PhaseOperationException_long_phase_String_() {
        Assert.assertEquals("The project ID must be initialized correctly",
                            TestDataFactory.EXCEPTION_PROJECT_ID, this.testedInstances[1].getProjectId());
        Assert.assertEquals("The phase must be initialized correctly",
                            TestDataFactory.getExceptionPhase(), this.testedInstances[1].getPhase());
        Assert.assertEquals("The message must be initialized correctly",
                            TestDataFactory.EXCEPTION_MESSAGE, this.testedInstances[1].getMessage());
        Assert.assertNull("The cause must not be initialized", this.testedInstances[1].getCause());
    }

    /**
     * <p>Accuracy test. Tests the {@link PhaseOperationException#PhaseOperationException(long,Phase,String,Throwable)}
     * constructor being provided with valid input arguments.</p>
     *
     * <p>Verifies that the project ID, phase, message an dcause are initialized.</p>
     */
    public void testConstructor_PhaseOperationException_long_phase_String_Throwable_() {
        Assert.assertEquals("The project ID must be initialized correctly",
                            TestDataFactory.EXCEPTION_PROJECT_ID, this.testedInstances[2].getProjectId());
        Assert.assertEquals("The phase must be initialized correctly",
                            TestDataFactory.getExceptionPhase(), this.testedInstances[2].getPhase());
        Assert.assertTrue("The message must be initialized correctly",
                            this.testedInstances[2].getMessage().indexOf(TestDataFactory.EXCEPTION_MESSAGE) >= 0);
        Assert.assertSame("The cause must be initialized correctly",
                          TestDataFactory.EXCEPTION_CAUSE, this.testedInstances[2].getCause());
    }
}
