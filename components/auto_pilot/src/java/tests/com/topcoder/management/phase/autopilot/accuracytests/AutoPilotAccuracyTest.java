/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.accuracytests;

import com.topcoder.management.phase.autopilot.AutoPilot;
import com.topcoder.management.phase.autopilot.AutoPilotSource;
import com.topcoder.management.phase.autopilot.ProjectPilot;
import com.topcoder.management.phase.autopilot.AutoPilotResult;
import com.topcoder.management.phase.autopilot.AutoPilotSourceException;
import com.topcoder.management.phase.autopilot.PhaseOperationException;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectPilotAlternate;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectManagerAlternate;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockPhaseManager;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectManager;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockLogAlternate;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockLog;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockAutoPilot;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectPilot;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockAutoPilotSource;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockAutoPilotSourceAlternate;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;

/**
 * <p>An accuracy test for {@link AutoPilot} class. Tests the methods for proper handling of valid input data and
 * producing accurate results. Passes the valid arguments to the methods and verifies that either the state of the
 * tested instance have been changed appropriately or a correct result is produced by the method.</p>
 *
 * @author isv
 * @version 1.0
 */
public class AutoPilotAccuracyTest extends TestCase {

    /**
     * <p>The instances of {@link AutoPilot} which are tested. These instances are initialized in {@link #setUp()}
     * method and released in {@link #tearDown()} method. Each instance is initialized using a separate constructor
     * provided by the tested class.<p>
     */
    private AutoPilotSubclass[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link AutoPilot} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link AutoPilot} class.
     */
    public static Test suite() {
        return new TestSuite(AutoPilotAccuracyTest.class);
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

        this.testedInstances = new AutoPilotSubclass[3];
        this.testedInstances[0] = new AutoPilotSubclass();
        this.testedInstances[1] = new AutoPilotSubclass(TestDataFactory.AUTO_PILOT_NAMESPACE, "source", "pilot");
        this.testedInstances[2] = new AutoPilotSubclass(TestDataFactory.getAutoPilotSource(),
                                                        TestDataFactory.getProjectPilot());
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
     * <p>Accuracy test. Tests the {@link AutoPilot#AutoPilot()} constructor being provided with valid input
     * arguments.</p>
     *
     * <p>Verifies that the instance is initialized with default values. The {@link AutoPilot#getAutoPilotSource()},
     * {@link AutoPilot#getProjectPilot()} methods are also indirectly tested by this test. </p>
     */
    public void testConstructor_AutoPilot() {
        Assert.assertEquals("The auto-pilot source is not correct.",
                            TestDataFactory.getAutoPilotSource(), this.testedInstances[0].getAutoPilotSource());
        Assert.assertEquals("The project manager is not correct.",
                            TestDataFactory.getProjectPilot(), this.testedInstances[0].getProjectPilot());
    }

    /**
     * <p>Accuracy test. Tests the {@link AutoPilot#AutoPilot(String,String,String)} constructor being provided with
     * valid input arguments.</p>
     *
     * <p>Verifies that the auto pilot source and project pilot are initialized with based on parameters of the
     * specified namespace. The {@link AutoPilot#getAutoPilotSource()}, {@link AutoPilot#getProjectPilot()} methods are
     * also indirectly tested by this test. </p>
     */
    public void testConstructor_AutoPilot_String_String_String_() {
        Assert.assertEquals("The auto-pilot source is not correct.",
                            TestDataFactory.getAlternateAutoPilotSource(),
                            this.testedInstances[1].getAutoPilotSource());
        Assert.assertEquals("The project manager is not correct.",
                            TestDataFactory.getAlternateProjectPilot(), this.testedInstances[1].getProjectPilot());
    }

    /**
     * <p>Accuracy test. Tests the {@link AutoPilot#AutoPilot(AutoPilotSource,ProjectPilot)} constructor being provided
     * with valid input arguments.</p>
     *
     * <p>Verifies that the instance is initialized with the provided values. The {@link
     * AutoPilot#getAutoPilotSource()}, {@link AutoPilot#getProjectPilot()} methods are also indirectly tested by this
     * test. </p>
     */
    public void testConstructor_AutoPilot_AutoPilotSource_ProjectPilot_() {
        Assert.assertEquals("The auto-pilot source is not correct.",
                            TestDataFactory.getAutoPilotSource(), this.testedInstances[2].getAutoPilotSource());
        Assert.assertEquals("The project manager is not correct.",
                            TestDataFactory.getProjectPilot(), this.testedInstances[2].getProjectPilot());
    }

    /**
     * <p>Accuracy test. Tests the {@link AutoPilot#advanceProjects(String)} method for proper behavior.</p>
     *
     * <p>Verifies that all projects with phases which can be advanced are advanced.</p>
     */
    public void testAdvanceProjects_String_ProjectsAdvanced() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            initMocks();
            try {
                AutoPilotResult[] result = this.testedInstances[i].advanceProjects(TestDataFactory.OPERATOR);
            } catch (Exception e) {
                fail("No exception shoud have been thrown : " + e);
            }
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link AutoPilot#advanceProjects(String)} method for proper behavior.</p>
     *
     * <p>Verifies that all projects with phases which can not be advanced are not advanced.</p>
     */
    public void testAdvanceProjects_String_ProjectsNotAdvanced() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                AutoPilotResult[] result = this.testedInstances[i].advanceProjects(TestDataFactory.OPERATOR);
            } catch (Exception e) {
                fail("No exception shoud have been thrown : " + e);
            }
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link AutoPilot#advanceProjects(long[],String)} method for proper behavior.</p>
     *
     * <p>Verifies that all projects with phases which can be advanced are advanced.</p>
     */
    public void testAdvanceProjects_longArray_String_ProjectsAdvanced() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                AutoPilotResult[] result
                    = this.testedInstances[i].advanceProjects(TestDataFactory.getAdvancableProjectIds(),
                                                              TestDataFactory.OPERATOR);
            } catch (Exception e) {
                fail("No exception shoud have been thrown : " + e);
            }
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link AutoPilot#advanceProjects(long[],String)} method for proper behavior.</p>
     *
     * <p>Verifies that all projects with phases which can not be advanced are not advanced.</p>
     */
    public void testAdvanceProjects_longArray_String_ProjectsNotAdvanced() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                AutoPilotResult[] result
                    = this.testedInstances[i].advanceProjects(TestDataFactory.getNonAdvancableProjectIds(),
                                                              TestDataFactory.OPERATOR);
            } catch (Exception e) {
                fail("No exception shoud have been thrown : " + e);
            }
        }
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
