/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.failuretests.impl;

import com.topcoder.date.workdays.DefaultWorkdaysFactory;

import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.phase.autopilot.ConfigurationException;
import com.topcoder.management.phase.autopilot.PhaseOperationException;
import com.topcoder.management.phase.autopilot.failuretests.FailureTestsHelper;
import com.topcoder.management.phase.autopilot.failuretests.MockPhase;
import com.topcoder.management.phase.autopilot.failuretests.MockPhaseManager;
import com.topcoder.management.phase.autopilot.impl.DefaultProjectPilot;

import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.Project;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogException;
import com.topcoder.util.log.basic.BasicLog;
import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.impl.IllegalReferenceException;
import com.topcoder.util.objectfactory.impl.SpecificationConfigurationException;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


/**
 * <p>
 * Failure test cases for <code>DefaultProjectPilot</code>.
 * </p>
 *
 * @author skatou
 * @version 1.0
 */
public class DefaultProjectPilotFailureTests extends FailureTestsHelper {
    /**
     * Tests constructor without the configuration loaded. ConfigurationException should be thrown.
     */
    public void testConstructor1NoConfig() {
        try {
            new DefaultProjectPilot();
            fail("ConfigurationException should be thrown");
        } catch (ConfigurationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof SpecificationConfigurationException);
        }
    }

    /**
     * Tests constructor without the logger properly configured. ConfigurationException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor1ConfigNoLogger() throws Exception {
        try {
            unloadConfig();
            ConfigManager.getInstance().add(CONFIG);
            new DefaultProjectPilot();
            fail("ConfigurationException should be thrown");
        } catch (ConfigurationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof LogException);
        }
    }

    /**
     * Tests constructor with a null argument. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2Null1() throws Exception {
        try {
            new DefaultProjectPilot(null, "b", "c", "d", "e");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a null argument. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2Null2() throws Exception {
        try {
            new DefaultProjectPilot("a", null, "c", "d", "e");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a null argument. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2Null3() throws Exception {
        try {
            new DefaultProjectPilot("a", "b", null, "d", "e");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a null argument. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2Null4() throws Exception {
        try {
            new DefaultProjectPilot("a", "b", "c", null, "e");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a null argument. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2Null5() throws Exception {
        try {
            new DefaultProjectPilot("a", "b", "c", "d", null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2EmptyString1() throws Exception {
        try {
            new DefaultProjectPilot("", "b", "c", "d", "e");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2EmptyString2() throws Exception {
        try {
            new DefaultProjectPilot("  ", "b", "c", "d", "e");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2EmptyString3() throws Exception {
        try {
            new DefaultProjectPilot("a", "", "c", "d", "e");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2EmptyString4() throws Exception {
        try {
            new DefaultProjectPilot("a", "  ", "c", "d", "e");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2EmptyString5() throws Exception {
        try {
            new DefaultProjectPilot("a", "b", "", "d", "e");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2EmptyString6() throws Exception {
        try {
            new DefaultProjectPilot("a", "b", " ", "d", "e");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2EmptyString7() throws Exception {
        try {
            new DefaultProjectPilot("a", "b", "c", "", "e");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2EmptyString8() throws Exception {
        try {
            new DefaultProjectPilot("a", "b", "c", "   ", "e");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2EmptyString9() throws Exception {
        try {
            new DefaultProjectPilot("a", "b", "c", "d", "");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2EmptyString10() throws Exception {
        try {
            new DefaultProjectPilot("a", "b", "c", "d", " ");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with namespace that does not exist. ConfigurationException should be thrown.
     */
    public void testConstructor2ConfigNoNamespace() {
        try {
            new DefaultProjectPilot("BadNamespace", "b", "c", "d", "e");
            fail("ConfigurationException should be thrown");
        } catch (ConfigurationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof SpecificationConfigurationException);
        }
    }

    /**
     * Tests constructor with phase manager key that does not exist. ConfigurationException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2ConfigNoProjectManagerKey()
        throws Exception {
        try {
            loadConfig();
            new DefaultProjectPilot(DefaultProjectPilot.class.getName(), "b", "c", "d", "e");
            fail("ConfigurationException should be thrown");
        } catch (ConfigurationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof InvalidClassSpecificationException);
        }
    }

    /**
     * Tests constructor with a namespace that contains loop. ConfigurationException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2ConfigLoop() throws Exception {
        try {
            loadConfig();
            new DefaultProjectPilot(DefaultProjectPilot.class.getName() + ".Loop", PhaseManager.class.getName(), "c",
                "d", "e");
            fail("ConfigurationException should be thrown");
        } catch (ConfigurationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof IllegalReferenceException);
        }
    }

    /**
     * Tests constructor with a namespace that contains wrong type. ConfigurationException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2ConfigWrongType() throws Exception {
        try {
            loadConfig();
            new DefaultProjectPilot(DefaultProjectPilot.class.getName() + ".WrongType",
                PhaseManager.class.getName(), "c", "d", "e");
            fail("ConfigurationException should be thrown");
        } catch (ConfigurationException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a logger name does not exist. ConfigurationException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2ConfigNoLogger() throws Exception {
        try {
            unloadConfig();
            ConfigManager.getInstance().add(CONFIG);
            new DefaultProjectPilot(DefaultProjectPilot.class.getName(), PhaseManager.class.getName(), "c", "d", "e");
            fail("ConfigurationException should be thrown");
        } catch (ConfigurationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof LogException);
        }
    }

    /**
     * Tests constructor with a null argument. IllegalArgumentException should be thrown.
     */
    public void testConstructor3Null1() {
        try {
            Log logger = new BasicLog("");
            new DefaultProjectPilot(null, "a", "b", logger);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a null argument. IllegalArgumentException should be thrown.
     */
    public void testConstructor3Null2() {
        try {
            PhaseManager phaseManager = new MockPhaseManager();
            Log logger = new BasicLog("");
            new DefaultProjectPilot(phaseManager, null, "b", logger);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a null argument. IllegalArgumentException should be thrown.
     */
    public void testConstructor3Null3() {
        try {
            PhaseManager phaseManager = new MockPhaseManager();
            Log logger = new BasicLog("");
            new DefaultProjectPilot(phaseManager, "a", null, logger);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a null argument. IllegalArgumentException should be thrown.
     */
    public void testConstructor3Null4() {
        try {
            PhaseManager phaseManager = new MockPhaseManager();
            new DefaultProjectPilot(phaseManager, "a", "b", null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     */
    public void testConstructor3EmptyString1() {
        try {
            PhaseManager phaseManager = new MockPhaseManager();
            Log logger = new BasicLog("");
            new DefaultProjectPilot(phaseManager, "", "b", logger);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     */
    public void testConstructor3EmptyString2() {
        try {
            PhaseManager phaseManager = new MockPhaseManager();
            Log logger = new BasicLog("");
            new DefaultProjectPilot(phaseManager, "       ", "b", logger);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     */
    public void testConstructor3EmptyString3() {
        try {
            PhaseManager phaseManager = new MockPhaseManager();
            Log logger = new BasicLog("");
            new DefaultProjectPilot(phaseManager, "a", "", logger);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     */
    public void testConstructor3EmptyString4() {
        try {
            PhaseManager phaseManager = new MockPhaseManager();
            Log logger = new BasicLog("");
            new DefaultProjectPilot(phaseManager, "a", "  ", logger);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests advancePhases with a null argument. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testAdvancePhasesNull() throws Exception {
        try {
            loadConfig();

            DefaultProjectPilot projectPilot = new DefaultProjectPilot();
            projectPilot.advancePhases(1, null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests advancePhases with an empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testAdvancePhasesEmptyString1() throws Exception {
        try {
            loadConfig();

            DefaultProjectPilot projectPilot = new DefaultProjectPilot();
            projectPilot.advancePhases(1, "");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests advancePhases with an empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testAdvancePhasesEmptyString2() throws Exception {
        try {
            loadConfig();

            DefaultProjectPilot projectPilot = new DefaultProjectPilot();
            projectPilot.advancePhases(1, "         ");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests advancePhases method. If ProjectManager.getPhases throws a exception.
     * PhaseOperationException ignored in DefaultProjectPilot#advancePhases, so no exception thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testAdvancePhasesGetPhasesException() throws Exception {
        loadConfig();

        DefaultProjectPilot projectPilot = new DefaultProjectPilot();
        MockPhaseManager.setGetPhasesException(true);
        projectPilot.advancePhases(1, "operator");
        // PhaseOperationException ignored in DefaultProjectPilot#advancePhases, so no exception thrown.
    }

    /**
     * Tests processPhase method. If PhaseManager.canEnd throws a exception, PhaseOperationException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testProcessPhaseCanEndException() throws Exception {
        try {
            loadConfig();

            EmptyDefaultProjectPilot projectPilot = new EmptyDefaultProjectPilot();
            MockPhase phase = new MockPhase(new Project(new Date(),
                        new DefaultWorkdaysFactory().createWorkdaysInstance()), 100);
            phase.getPhaseStatus().setName(projectPilot.getOpenStatusName());
            MockPhaseManager.setCanEndException(true);
            projectPilot.processPhase(phase, new HashSet(), "operator");
            fail("PhaseOperationException should be thrown");
        } catch (PhaseOperationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof PhaseManagementException);
        }
    }

    /**
     * Tests processPhase method. If PhaseManager.end throws a exception, PhaseOperationException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testProcessPhaseEndException() throws Exception {
        try {
            loadConfig();

            EmptyDefaultProjectPilot projectPilot = new EmptyDefaultProjectPilot();
            MockPhase phase = new MockPhase(new Project(new Date(),
                        new DefaultWorkdaysFactory().createWorkdaysInstance()), 100);
            phase.getPhaseStatus().setName(projectPilot.getOpenStatusName());
            MockPhaseManager.setCanEndException(false);
            MockPhaseManager.setCanEnd(true);
            MockPhaseManager.setEndException(true);
            projectPilot.processPhase(phase, new HashSet(), "operator");
            fail("PhaseOperationException should be thrown");
        } catch (PhaseOperationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof PhaseManagementException);
        }
    }

    /**
     * Tests processPhase method. If PhaseManager.canStart throws a exception, PhaseOperationException should be
     * thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testProcessPhaseCanStartException() throws Exception {
        try {
            loadConfig();

            EmptyDefaultProjectPilot projectPilot = new EmptyDefaultProjectPilot();
            MockPhase phase = new MockPhase(new Project(new Date(),
                        new DefaultWorkdaysFactory().createWorkdaysInstance()), 100);
            phase.getPhaseStatus().setName(projectPilot.getScheduledStatusName());
            MockPhaseManager.setCanStartException(true);
            projectPilot.processPhase(phase, new HashSet(), "operator");
            fail("PhaseOperationException should be thrown");
        } catch (PhaseOperationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof PhaseManagementException);
        }
    }

    /**
     * Tests processPhase method. If PhaseManager.start throws a exception, PhaseOperationException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testProcessPhaseStartException() throws Exception {
        try {
            loadConfig();

            EmptyDefaultProjectPilot projectPilot = new EmptyDefaultProjectPilot();
            MockPhase phase = new MockPhase(new Project(new Date(),
                        new DefaultWorkdaysFactory().createWorkdaysInstance()), 100);
            phase.getPhaseStatus().setName(projectPilot.getScheduledStatusName());
            MockPhaseManager.setCanStartException(false);
            MockPhaseManager.setCanStart(true);
            MockPhaseManager.setStartException(true);
            projectPilot.processPhase(phase, new HashSet(), "operator");
            fail("PhaseOperationException should be thrown");
        } catch (PhaseOperationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof PhaseManagementException);
        }
    }

    /**
     * Tests doPhaseOperation method. If PhaseManager.canEnd throws a exception, PhaseOperationException should be
     * thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testDoPhaseOperationCanEndException() throws Exception {
        try {
            loadConfig();

            EmptyDefaultProjectPilot projectPilot = new EmptyDefaultProjectPilot();
            MockPhase phase = new MockPhase(new Project(new Date(),
                        new DefaultWorkdaysFactory().createWorkdaysInstance()), 100);
            phase.getPhaseStatus().setName(projectPilot.getOpenStatusName());
            MockPhaseManager.setCanEndException(true);
            projectPilot.doPhaseOperation(phase, "operator");
            fail("PhaseOperationException should be thrown");
        } catch (PhaseOperationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof PhaseManagementException);
        }
    }

    /**
     * Tests doPhaseOperation method. If PhaseManager.end throws a exception, PhaseOperationException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testDoPhaseOperationEndException() throws Exception {
        try {
            loadConfig();

            EmptyDefaultProjectPilot projectPilot = new EmptyDefaultProjectPilot();
            MockPhase phase = new MockPhase(new Project(new Date(),
                        new DefaultWorkdaysFactory().createWorkdaysInstance()), 100);
            phase.getPhaseStatus().setName(projectPilot.getOpenStatusName());
            MockPhaseManager.setCanEndException(false);
            MockPhaseManager.setCanEnd(true);
            MockPhaseManager.setEndException(true);
            projectPilot.doPhaseOperation(phase, "operator");
            fail("PhaseOperationException should be thrown");
        } catch (PhaseOperationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof PhaseManagementException);
        }
    }

    /**
     * Tests doPhaseOperation method. If PhaseManager.canStart throws a exception, PhaseOperationException should be
     * thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testDoPhaseOperationCanStartException()
        throws Exception {
        try {
            loadConfig();

            EmptyDefaultProjectPilot projectPilot = new EmptyDefaultProjectPilot();
            MockPhase phase = new MockPhase(new Project(new Date(),
                        new DefaultWorkdaysFactory().createWorkdaysInstance()), 100);
            phase.getPhaseStatus().setName(projectPilot.getScheduledStatusName());
            MockPhaseManager.setCanStartException(true);
            projectPilot.doPhaseOperation(phase, "operator");
            fail("PhaseOperationException should be thrown");
        } catch (PhaseOperationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof PhaseManagementException);
        }
    }

    /**
     * Tests doPhaseOperation method. If PhaseManager.start throws a exception, PhaseOperationException should be
     * thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testDoPhaseOperationStartException() throws Exception {
        try {
            loadConfig();

            EmptyDefaultProjectPilot projectPilot = new EmptyDefaultProjectPilot();
            MockPhase phase = new MockPhase(new Project(new Date(),
                        new DefaultWorkdaysFactory().createWorkdaysInstance()), 100);
            phase.getPhaseStatus().setName(projectPilot.getScheduledStatusName());
            MockPhaseManager.setCanStartException(false);
            MockPhaseManager.setCanStart(true);
            MockPhaseManager.setStartException(true);
            projectPilot.doPhaseOperation(phase, "operator");
            fail("PhaseOperationException should be thrown");
        } catch (PhaseOperationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof PhaseManagementException);
        }
    }

    /**
     * Mock class extending abstract class DefaultProjectPilot for protected methods failure testing.
     */
    private class EmptyDefaultProjectPilot extends DefaultProjectPilot {
        /**
         * Default constructor.
         *
         * @throws ConfigurationException never.
         */
        public EmptyDefaultProjectPilot() throws ConfigurationException {
            super();
        }

        /**
         * Simply return super.processPhase(phase, processedPhase, operator).
         *
         * @param phase the phase to process.
         * @param processedPhase a set of Long representing phases id that have been processed.
         * @param operator the operator name for auditing.
         *
         * @return result got from super class.
         *
         * @throws PhaseOperationException if any error occurs while processing the phase.
         */
        public int[] processPhase(Phase phase, Set processedPhase, String operator)
            throws PhaseOperationException {
            return super.processPhase(phase, processedPhase, operator);
        }

        /**
         * Simply return super.doPhaseOperation(phase, operator).
         *
         * @param phase the phase to end or start.
         * @param operator the operator name for auditing.
         *
         * @return result got from super class.
         *
         * @throws PhaseOperationException if any error occurs while ending or starting the phase.
         */
        public int[] doPhaseOperation(Phase phase, String operator)
            throws PhaseOperationException {
            return super.doPhaseOperation(phase, operator);
        }

        /**
         * Simply call super.doAudit(phase, isEnd, operator).
         *
         * @param phase the phase to audit.
         * @param isEnd true if the phase was ended, false if the phase was started.
         * @param operator the operator name to audit.
         *
         * @throws PhaseOperationException if any error occurs while auditing the entry.
         */
        public void doAudit(Phase phase, boolean isEnd, String operator)
            throws PhaseOperationException {
            super.doAudit(phase, isEnd, operator);
        }
    }
}
