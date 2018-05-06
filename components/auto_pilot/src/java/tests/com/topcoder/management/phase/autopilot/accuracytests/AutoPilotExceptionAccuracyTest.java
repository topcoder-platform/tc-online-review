/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.accuracytests;

import com.topcoder.management.phase.autopilot.AutoPilotException;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>An accuracy test for {@link AutoPilotException} class. Tests the methods
 * for proper handling of valid input data and producing accurate results. Passes the valid arguments to the methods and
 * verifies that either the state of the tested instance have been changed appropriately or a correct result is produced
 * by the method.</p>
 *
 * @author isv
 * @version 1.0
 */
public class AutoPilotExceptionAccuracyTest extends TestCase {

    /**
     * <p>The instances of {@link AutoPilotException} which are tested. These instances are initialized in {@link
     * #setUp()} method and released in {@link #tearDown()} method. Each instance is initialized using a separate
     * constructor provided by the tested class.<p>
     */
    private AutoPilotException[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link AutoPilotException} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link AutoPilotException} class.
     */
    public static Test suite() {
        return new TestSuite(AutoPilotExceptionAccuracyTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testedInstances = new AutoPilotException[3];
        this.testedInstances[0] = new AutoPilotException();
        this.testedInstances[1] = new AutoPilotException(TestDataFactory.EXCEPTION_MESSAGE);
        this.testedInstances[2] = new AutoPilotException(TestDataFactory.EXCEPTION_MESSAGE,
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
     * <p>Accuracy test. Tests the {@link AutoPilotException#AutoPilotException()} constructor being
     * provided with valid input arguments.</p>
     *
     * <p>Verifies that the message and cause are not initialized.</p>
     */
    public void testConstructor_AutoPilotException() {
        Assert.assertNull("The message must not be initialized", this.testedInstances[0].getMessage());
        Assert.assertNull("The cause must not be initialized", this.testedInstances[0].getCause());
    }

    /**
     * <p>Accuracy test. Tests the {@link AutoPilotException#AutoPilotException(String)} constructor
     * being provided with valid input arguments.</p>
     *
     * <p>Verifies that the message is initialized and the cause is not initialized.</p>
     */
    public void testConstructor_AutoPilotException_String() {
        Assert.assertEquals("The message must be initialized correctly",
                            TestDataFactory.EXCEPTION_MESSAGE, this.testedInstances[1].getMessage());
        Assert.assertNull("The cause must not be initialized", this.testedInstances[1].getCause());
    }

    /**
     * <p>Accuracy test. Tests the {@link AutoPilotException#AutoPilotException(String,Throwable)}
     * constructor being provided with valid input arguments.</p>
     *
     * <p>Verifies that the message is initialized and the cause is initialized.</p>
     */
    public void testConstructor_AutoPilotException_String_Throwable() {
        Assert.assertTrue("The message must be initialized correctly",
                            this.testedInstances[2].getMessage().indexOf(TestDataFactory.EXCEPTION_MESSAGE) >= 0);
        Assert.assertSame("The cause must be initialized correctly",
                          TestDataFactory.EXCEPTION_CAUSE, this.testedInstances[2].getCause());
    }
}
