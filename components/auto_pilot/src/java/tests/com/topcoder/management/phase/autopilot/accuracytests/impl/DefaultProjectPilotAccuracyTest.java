/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.accuracytests.impl;

import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.phase.autopilot.accuracytests.TestDataFactory;
import com.topcoder.management.phase.autopilot.accuracytests.ConfigHelper;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockLog;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockPhaseManager;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectManagerAlternate;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectPilot;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectManager;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockAutoPilotSource;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectPilotAlternate;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockLogAlternate;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockAutoPilot;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockAutoPilotSourceAlternate;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockPhaseManagerAlternate;
import com.topcoder.management.phase.autopilot.impl.DefaultProjectPilot;
import com.topcoder.project.phases.Phase;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Arrays;
import java.io.File;

/**
 * <p>An accuracy test for {@link DefaultProjectPilot} class. Tests the methods for proper handling of valid input data
 * and producing accurate results. Passes the valid arguments to the methods and verifies that either the state of the
 * tested instance have been changed appropriately or a correct result is produced by the method.</p>
 *
 * @author isv
 * @version 1.0
 */
public class DefaultProjectPilotAccuracyTest extends TestCase {

    /**
     * <p>An <code>int</code> array tobe used for testing.</p>
     */
    private static final int[] ARRAY_00 = new int[] {0, 0};

    /**
     * <p>An <code>int</code> array tobe used for testing.</p>
     */
    private static final int[] ARRAY_01 = new int[] {0, 1};

    /**
     * <p>An <code>int</code> array tobe used for testing.</p>
     */
    private static final int[] ARRAY_10 = new int[] {1, 0};

    /**
     * <p>The instances of {@link DefaultProjectPilot} which are tested. These instances are initialized in {@link
     * #setUp()} method and released in {@link #tearDown()} method. Each instance is initialized using a separate
     * constructor provided by the tested class.<p>
     */
    private DefaultProjectPilotSubclass[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link DefaultProjectPilot} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link DefaultProjectPilot} class.
     */
    public static Test suite() {
        return new TestSuite(DefaultProjectPilotAccuracyTest.class);
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

        this.testedInstances = new DefaultProjectPilotSubclass[3];
        this.testedInstances[0] = new DefaultProjectPilotSubclass();
        this.testedInstances[1] = new DefaultProjectPilotSubclass(TestDataFactory.DEFAULT_PROJECT_PILOT_NAMESPACE,
                                                                  TestDataFactory.PHASE_MANAGER_CONFIG_PROPERTY,
                                                                  TestDataFactory.SCHEDULED_PHASE_STATUS,
                                                                  TestDataFactory.OPEN_PHASE_STATUS,
                                                                  TestDataFactory.LOG_NAME);
        this.testedInstances[2] = new DefaultProjectPilotSubclass(TestDataFactory.getPhaseManager(),
                                                                  TestDataFactory.SCHEDULED_PHASE_STATUS,
                                                                  TestDataFactory.OPEN_PHASE_STATUS,
                                                                  TestDataFactory.getLog());
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
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#DefaultProjectPilot()} constructor being provided with
     * valid input arguments.</p>
     *
     * <p>Verifies that the instance is initialized with default values. The {@link
     * DefaultProjectPilot#getScheduledStatusName()}, {@link DefaultProjectPilot#getOpenStatusName()}, {@link
     * DefaultProjectPilot#getLogger()}, {@link DefaultProjectPilot#getPhaseManager()} methods are also indirectly
     * tested by this test. </p>
     */
    public void testConstructor_DefaultProjectPilot_() {
        Assert.assertEquals("The scheduled phase status name is not correct.",
                            DefaultProjectPilot.DEFAULT_SCHEDULED_STATUS_NAME,
                            this.testedInstances[0].getScheduledStatusName());
        Assert.assertEquals("The open phase status name is not correct.",
                            DefaultProjectPilot.DEFAULT_OPEN_STATUS_NAME,
                            this.testedInstances[0].getOpenStatusName());
        Assert.assertEquals("The logger is not correct.",
                            TestDataFactory.getLog(), this.testedInstances[0].getLogger());
        Assert.assertEquals("The phase manager is not correct.",
                            TestDataFactory.getPhaseManager(), this.testedInstances[0].getPhaseManager());
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#DefaultProjectPilot(String,String,String,String,String)}
     * constructor being provided with valid input arguments.</p>
     *
     * <p>Verifies that the instance is initialized with the provided values and the phase manager is initialized based
     * on parameters of specified configuration namespace. The {@link DefaultProjectPilot#getScheduledStatusName()},
     * {@link DefaultProjectPilot#getOpenStatusName()}, {@link DefaultProjectPilot#getLogger()}, {@link
     * DefaultProjectPilot#getPhaseManager()} methods are also indirectly tested by this test. </p>
     */
    public void testConstructor_DefaultProjectPilot_String_String_String_String_String_() {
        Assert.assertEquals("The scheduled phase status name is not correct.",
                            TestDataFactory.SCHEDULED_PHASE_STATUS, this.testedInstances[1].getScheduledStatusName());
        Assert.assertEquals("The open phase status name is not correct.",
                            TestDataFactory.OPEN_PHASE_STATUS, this.testedInstances[1].getOpenStatusName());
        Assert.assertEquals("The logger is not correct.",
                            TestDataFactory.getAlternateLog(), this.testedInstances[1].getLogger());
        Assert.assertEquals("The phase manager is not correct.",
                            TestDataFactory.getAlternatePhaseManager(), this.testedInstances[1].getPhaseManager());
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#DefaultProjectPilot(PhaseManager,String,String,Log)}
     * constructor being provided with valid input arguments.</p>
     *
     * <p>Verifies that the instance is initialized with the provided values. The {@link
     * DefaultProjectPilot#getScheduledStatusName()}, {@link DefaultProjectPilot#getOpenStatusName()}, {@link
     * DefaultProjectPilot#getLogger()}, {@link DefaultProjectPilot#getPhaseManager()} methods are also indirectly
     * tested by this test. </p>
     */
    public void testConstructor_DefaultProjectPilot_PhaseManager_String_String_Log_() {
        Assert.assertEquals("The scheduled phase status name is not correct.",
                            TestDataFactory.SCHEDULED_PHASE_STATUS, this.testedInstances[2].getScheduledStatusName());
        Assert.assertEquals("The open phase status name is not correct.",
                            TestDataFactory.OPEN_PHASE_STATUS, this.testedInstances[2].getOpenStatusName());
        Assert.assertEquals("The logger is not correct.",
                            TestDataFactory.getLog(), this.testedInstances[2].getLogger());
        Assert.assertEquals("The phase manager is not correct.",
                            TestDataFactory.getPhaseManager(), this.testedInstances[2].getPhaseManager());
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#doAudit(Phase,boolean,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method logs the audit record correctly, i.e. the appropriate log level is used and the
     * logged message conforms to template specified by design. The phase is ended and phase type is not null.</p>
     */
    public void testDoAudit_Phase_boolean_String_PhaseEnded_NonNullType() throws Exception {
        Phase phase = TestDataFactory.getPhaseWithNonNullType(TestDataFactory.getPhaseProject());
        for (int i = 0; i < this.testedInstances.length; i++) {
            MockLog.releaseState();
            MockLog.init();
            this.testedInstances[i].doAudit(phase, true, TestDataFactory.OPERATOR);
            assertLog(phase, true, TestDataFactory.OPERATOR);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#doAudit(Phase,boolean,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method logs the audit record correctly, i.e. the appropriate log level is used and the
     * logged message conforms to template specified by design. The phase is not ended and phase type is not null.</p>
     */
    public void testDoAudit_Phase_boolean_String_PhaseNotEnded_NonNullType() throws Exception {
        Phase phase = TestDataFactory.getPhaseWithNonNullType(TestDataFactory.getPhaseProject());
        for (int i = 0; i < this.testedInstances.length; i++) {
            MockLog.releaseState();
            MockLog.init();
            this.testedInstances[i].doAudit(phase, false, TestDataFactory.OPERATOR);
            assertLog(phase, false, TestDataFactory.OPERATOR);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#doAudit(Phase,boolean,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method logs the audit record correctly, i.e. the appropriate log level is used and the
     * logged message conforms to template specified by design. The phase is ended and phase type is null.</p>
     */
    public void testDoAudit_Phase_boolean_String_PhaseEnded_NullType() throws Exception {
        Phase phase = TestDataFactory.getPhaseWithNullType(TestDataFactory.getPhaseProject());
        for (int i = 0; i < this.testedInstances.length; i++) {
            MockLog.releaseState();
            MockLog.init();
            this.testedInstances[i].doAudit(phase, true, TestDataFactory.OPERATOR);
            assertLog(phase, true, TestDataFactory.OPERATOR);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#doAudit(Phase,boolean,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method logs the audit record correctly, i.e. the appropriate log level is used and the
     * logged message conforms to template specified by design. The phase is not ended and phase type is null.</p>
     */
    public void testDoAudit_Phase_boolean_String_PhaseNotEnded_NullType() throws Exception {
        Phase phase = TestDataFactory.getPhaseWithNullType(TestDataFactory.getPhaseProject());
        for (int i = 0; i < this.testedInstances.length; i++) {
            MockLog.releaseState();
            MockLog.init();
            this.testedInstances[i].doAudit(phase, false, TestDataFactory.OPERATOR);
            assertLog(phase, false, TestDataFactory.OPERATOR);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#doPhaseOperation(Phase,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method returns correct result if the phase is neither of scheduled nor open status. The
     * phase manager is configured to indicate that the phase can be ended or started.</p>
     */
    public void testDoPhaseOperation_Phase_Operator_NoChange() throws Exception {
        configurePhaseManager(true, true);
        for (int i = 0; i < this.testedInstances.length; i++) {
            MockLog.releaseState();
            MockLog.init();
            int[] result = this.testedInstances[i].doPhaseOperation(TestDataFactory.getClosedPhase(TestDataFactory.getPhaseProject()),
                                                                    TestDataFactory.OPERATOR);
            assertPhaseProcessResults(ARRAY_00, result, false, false);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#doPhaseOperation(Phase,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method returns correct result if the phase is not assigned any status. The phase manager is
     * configured to indicate that the phase can be ended or started. </p>
     */
    public void testDoPhaseOperation_Phase_Operator_NoChange_NullStatus() throws Exception  {
        configurePhaseManager(true, true);
        for (int i = 0; i < this.testedInstances.length; i++) {
            int[] result = this.testedInstances[i].doPhaseOperation(TestDataFactory.getPhaseWithNullStatus(TestDataFactory.getPhaseProject()),
                                                                    TestDataFactory.OPERATOR);
            assertPhaseProcessResults(ARRAY_00, result, false, false);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#doPhaseOperation(Phase,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method returns correct result if the phase is open and can be closed. The phase manager is
     * configured to indicate that the phase can be closed.</p>
     */
    public void testDoPhaseOperation_Phase_Operator_PhaseEnded() throws Exception {
        configurePhaseManager(true, true);
        for (int i = 0; i < this.testedInstances.length; i++) {
            int[] result = this.testedInstances[i].doPhaseOperation(TestDataFactory.getOpenPhase(TestDataFactory.getPhaseProject()),
                                                                    TestDataFactory.OPERATOR);
            assertPhaseProcessResults(ARRAY_10, result, true, false);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#doPhaseOperation(Phase,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method returns correct result if the phase is open but can not be closed. The phase manager
     * is configured to indicate that the phase can not be closed.</p>
     */
    public void testDoPhaseOperation_Phase_Operator_PhaseNotEnded() throws Exception {
        configurePhaseManager(false, true);
        for (int i = 0; i < this.testedInstances.length; i++) {
            int result[] = this.testedInstances[i].doPhaseOperation(TestDataFactory.getOpenPhase(TestDataFactory.getPhaseProject()),
                                                                    TestDataFactory.OPERATOR);
            assertPhaseProcessResults(ARRAY_00, result, false, false);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#doPhaseOperation(Phase,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method returns correct result if the phase is scheduled and can be started. The phase
     * manager is configured to indicate that the phase can be started.</p>
     */
    public void testDoPhaseOperation_Phase_Operator_PhaseStarted() throws Exception {
        configurePhaseManager(true, true);
        for (int i = 0; i < this.testedInstances.length; i++) {
            int[] result = this.testedInstances[i].doPhaseOperation(TestDataFactory.getScheduledPhase(TestDataFactory.getPhaseProject()),
                                                                    TestDataFactory.OPERATOR);
            assertPhaseProcessResults(ARRAY_01, result, false, true);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#doPhaseOperation(Phase,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method returns correct result if the phase is scheduled but can not be started. The phase
     * manager is configured to indicate that the phase can not be started.</p>
     */
    public void testDoPhaseOperation_Phase_Operator_PhaseNotStarted() throws Exception {
        for (int i = 0; i < this.testedInstances.length; i++) {
            configurePhaseManager(true, false);
            int[] result = this.testedInstances[i].doPhaseOperation(TestDataFactory.getScheduledPhase(TestDataFactory.getPhaseProject()),
                                                                    TestDataFactory.OPERATOR);
            assertPhaseProcessResults(ARRAY_00, result, false, false);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#processPhase(Phase,Set,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method returns correct result if the specified phase is NULL.</p>
     */
    public void testProcessPhase_Phase_Set_String_NullPhase() throws Exception {
        configurePhaseManager(true, true);
        for (int i = 0; i < this.testedInstances.length; i++) {
            int[] result = this.testedInstances[i].processPhase(null, new HashSet(), TestDataFactory.OPERATOR);
            assertPhaseProcessResults(ARRAY_00, result, false, false);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#processPhase(Phase,Set,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method returns correct result if the specified phase is already processed.</p>
     */
    public void testProcessPhase_Phase_Set_String_PhaseAlreadyProcessed() throws Exception {
        configurePhaseManager(true, true);
        for (int i = 0; i < this.testedInstances.length; i++) {
            int[] result = this.testedInstances[i].processPhase(TestDataFactory.getProcessedPhase(TestDataFactory.getPhaseProject()),
                                                                TestDataFactory.getProcessedPhases(),
                                                                TestDataFactory.OPERATOR);
            assertPhaseProcessResults(ARRAY_00, result, false, false);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#processPhase(Phase,Set,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method returns correct result if the phase is open and can be closed. The phase manager is
     * configured to indicate that the phase can be closed.</p>
     */
    public void testProcessPhase_Phase_Set_String_PhaseEnded() throws Exception {
        configurePhaseManager(true, true);
        for (int i = 0; i < this.testedInstances.length; i++) {
            int[] result = this.testedInstances[i].processPhase(TestDataFactory.getOpenPhase(TestDataFactory.getPhaseProject()),
                                                                new HashSet(), TestDataFactory.OPERATOR);
            assertPhaseProcessResults(ARRAY_10, result, true, false);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#processPhase(Phase,Set,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method returns correct result if the phase is open but can not be closed. The phase manager
     * is configured to indicate that the phase can not be closed.</p>
     */
    public void testProcessPhase_Phase_Set_String_PhaseNotEnded() throws Exception {
        configurePhaseManager(false, true);
        for (int i = 0; i < this.testedInstances.length; i++) {
            int[] result = this.testedInstances[i].processPhase(TestDataFactory.getOpenPhase(TestDataFactory.getPhaseProject()),
                                                                new HashSet(), TestDataFactory.OPERATOR);
            assertPhaseProcessResults(ARRAY_00, result, false, false);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#processPhase(Phase,Set,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method returns correct result if the phase is scheduled and can be started. The phase
     * manager is configured to indicate that the phase can be started.</p>
     */
    public void testProcessPhase_Phase_Set_String_PhaseStarted() throws Exception {
        configurePhaseManager(true, true);
        for (int i = 0; i < this.testedInstances.length; i++) {
            int[] result = this.testedInstances[i].processPhase(TestDataFactory.getScheduledPhase(TestDataFactory.getPhaseProject()),
                                                                new HashSet(), TestDataFactory.OPERATOR);
            assertPhaseProcessResults(ARRAY_01, result, false, true);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#processPhase(Phase,Set,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method returns correct result if the phase is scheduled but can not be started. The phase
     * manager is configured to indicate that the phase can not be started.</p>
     */
    public void testProcessPhase_Phase_Set_String_PhaseNotStarted() throws Exception {
        configurePhaseManager(true, false);
        for (int i = 0; i < this.testedInstances.length; i++) {
            int[] result = this.testedInstances[i].processPhase(TestDataFactory.getScheduledPhase(TestDataFactory.getPhaseProject()),
                                                                new HashSet(), TestDataFactory.OPERATOR);
            assertPhaseProcessResults(ARRAY_00, result, false, false);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#processPhase(Phase,Set,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method returns correct result if the specified phase is already processed. The provided
     * phase has dependencies.</p>
     */
    public void testProcessPhase_Phase_Set_String_PhaseAlreadyProcessed_WithDependencies() throws Exception {
        configurePhaseManager(true, true);
        for (int i = 0; i < this.testedInstances.length; i++) {
            int[] result = this.testedInstances[i].processPhase(TestDataFactory.getProcessedPhase(TestDataFactory.getPhaseProject()),
                                                                TestDataFactory.getProcessedPhases(),
                                                                TestDataFactory.OPERATOR);
            assertPhaseProcessResults(ARRAY_00, result, false, false);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#processPhase(Phase,Set,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method returns correct result if the phase is open and can be closed. The phase manager is
     * configured to indicate that the phase can be closed. The provided phase has dependencies.</p>
     */
    public void testProcessPhase_Phase_Set_String_PhaseEnded_WithDependencies() throws Exception {
        configurePhaseManager(true, true);
        for (int i = 0; i < this.testedInstances.length; i++) {
            int[] result = this.testedInstances[i].processPhase(TestDataFactory.getOpenPhase(TestDataFactory.getPhaseProject()),
                                                                new HashSet(), TestDataFactory.OPERATOR);
            assertPhaseProcessResults(ARRAY_10, result, true, false);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#processPhase(Phase,Set,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method returns correct result if the phase is open but can not be closed. The phase manager
     * is configured to indicate that the phase can not be closed. The provided phase has dependencies.</p>
     */
    public void testProcessPhase_Phase_Set_String_PhaseNotEnded_WithDependencies() throws Exception {
        configurePhaseManager(false, true);
        for (int i = 0; i < this.testedInstances.length; i++) {
            int[] result = this.testedInstances[i].processPhase(TestDataFactory.getOpenPhase(TestDataFactory.getPhaseProject()),
                                                                new HashSet(), TestDataFactory.OPERATOR);
            assertPhaseProcessResults(ARRAY_00, result, false, false);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#processPhase(Phase,Set,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method returns correct result if the phase is scheduled and can be started. The phase
     * manager is configured to indicate that the phase can be started. The provided phase has dependencies.</p>
     */
    public void testProcessPhase_Phase_Set_String_PhaseStarted_WithDependencies() throws Exception {
        configurePhaseManager(true, true);
        for (int i = 0; i < this.testedInstances.length; i++) {
            int[] result = this.testedInstances[i].processPhase(TestDataFactory.getScheduledPhase(TestDataFactory.getPhaseProject()),
                                                                new HashSet(), TestDataFactory.OPERATOR);
            assertPhaseProcessResults(ARRAY_01, result, false, true);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link DefaultProjectPilot#processPhase(Phase,Set,String)} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method returns correct result if the phase is scheduled but can not be started. The phase
     * manager is configured to indicate that the phase can not be started. The provided phase has dependencies.</p>
     */
    public void testProcessPhase_Phase_Set_String_PhaseNotStarted_WithDependencies() throws Exception {
        configurePhaseManager(true, false);
        for (int i = 0; i < this.testedInstances.length; i++) {
            int[] result = this.testedInstances[i].processPhase(TestDataFactory.getScheduledPhase(TestDataFactory.getPhaseProject()),
                                                                new HashSet(), TestDataFactory.OPERATOR);
            assertPhaseProcessResults(ARRAY_00, result, false, false);
        }
    }

    /**
     * <p>Verifies that the appropriate audit record has been logged.</p>
     *
     * @param phase a <code>Phase</code> providing the details for processed phase.
     * @param isEnd <code>true</code> if phase was ended; <code>false</code> otherwise.
     * @param operator a <code>String</code> providing the operator to be logged.
     */
    private void assertLog(Phase phase, boolean isEnd, String operator) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        formatter.setTimeZone(TimeZone.getDefault());

        List log = MockLog.getMethodArguments("log_Level_Object");
        Assert.assertEquals("A single audit record must be logged", 1, log.size());
        Map record = (Map) log.get(0);
        Assert.assertEquals("The audit record is not logged at INFO level", Level.INFO, record.get("1"));
        String message = (String) record.get("2");
        Assert.assertNotNull("The NULL message is logegd", message);
        String[] parts = message.split("-");
        Assert.assertEquals("The audit record is not correct", 5, parts.length);

        try {
            formatter.parse(parts[0].trim());
        } catch (ParseException e) {
            fail("The logged date is invalid : " + e.getMessage());
        }

        Assert.assertEquals("The logged project ID is not correct",
                            String.valueOf(phase.getProject().getId()), parts[1].trim());
        Assert.assertEquals("The logged phase ID and type are not correct",
                            (phase.getId() + ":" + (phase.getPhaseType() == null ? "Null phase type"
                            : phase.getPhaseType().getName())).toUpperCase(),
                            parts[2].trim().toUpperCase());
        Assert.assertEquals("The logged action is not correct", isEnd ? "END" : "START", parts[3].trim());
        Assert.assertEquals("The logged operator is not correct", operator, parts[4].trim());
    }

    /**
     * <p>Verifies the results of phase processing.</p>
     *
     * @param expectedResult an <code>int</code> array providing the expected result of phase processing.
     * @param result am <code>int</code> array providing the actual result of phase processing.
     * @param shouldBeEnded <code>true</code> if the phase manager is expected to get called to end the phase.
     * @param shouldBeStarted <code>true</code> if the phase manager is expected to get called to start the phase.
     */
    private void assertPhaseProcessResults(int[] expectedResult, int[] result, boolean shouldBeEnded,
                                           boolean shouldBeStarted) {
//        print(expectedResult);
//        print(result);
        Assert.assertTrue("The result is not correct", Arrays.equals(expectedResult, result));
        if (shouldBeEnded) {
            Assert.assertTrue("The phase must be ended", MockPhaseManager.wasMethodCalled("end_Phase_String"));
        } else {
            Assert.assertFalse("The phase must not be ended", MockPhaseManager.wasMethodCalled("end_Phase_String"));
        }
        if (shouldBeStarted) {
            Assert.assertTrue("The phase must be started", MockPhaseManager.wasMethodCalled("start_Phase_String"));
        } else {
            Assert.assertFalse("The phase must not be started", MockPhaseManager.wasMethodCalled("start_Phase_String"));
        }
    }

    /**
     * <p>Configures the behavior of phase manager.</p>
     *
     * @param endPhase <code>true</code> if the manager must indicate that the phase can be ended.
     * @param startPhase <code>true</code> if the manager must indicate that the phase can be started.
     */
    private void configurePhaseManager(boolean endPhase, boolean startPhase) {
        MockPhaseManager.releaseState();
        MockPhaseManager.init();
        MockPhaseManagerAlternate.releaseState();
        MockPhaseManagerAlternate.init();

        MockPhaseManager.setMethodResult("canEnd_Phase", Boolean.valueOf(endPhase));
        MockPhaseManager.setMethodResult("canStart_Phase", Boolean.valueOf(startPhase));
        MockPhaseManagerAlternate.setMethodResult("canEnd_Phase", Boolean.valueOf(endPhase));
        MockPhaseManagerAlternate.setMethodResult("canStart_Phase", Boolean.valueOf(startPhase));
    }

    private void print(int[] array) {
        System.out.print("[");
        for (int i = 0; i < array.length; i++) {
            System.out.print(" " + array[i]);
        }
        System.out.println("]");
    }

}
