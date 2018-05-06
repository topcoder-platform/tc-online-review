/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot.impl;

import java.util.HashSet;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.management.phase.DefaultPhaseManager;
import com.topcoder.management.phase.DefaultPhaseManager2;
import com.topcoder.management.phase.DefaultPhaseManager3;
import com.topcoder.management.phase.autopilot.AutoPilotResult;
import com.topcoder.management.phase.autopilot.ProjectPilot;
import com.topcoder.project.phases.Phase;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.log.LogFactory;
import com.topcoder.util.log.basic.BasicLog;

/**
 * <p>
 * Unit tests for class <code>DefaultProjectPilot</code>.
 * </p>
 * @author abelli
 * @version 1.0
 */
public class DefaultProjectPilotTest extends TestCase {
    /** DefaultProjectPilot instance used in test cases.*/
    private DefaultProjectPilot pilot;

    /**
     * <p>
     * Setup the test fixture. Load namespaces used to create DefaultProjectPilot instance,
     * then create DefaultProjectPilot instance.
     * </p>
     * @throws Exception - to JUnit.
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        ConfigManager cfg = ConfigManager.getInstance();
        cfg.add(DefaultProjectPilot.class.getName(), "project_pilot.xml",
            ConfigManager.CONFIG_XML_FORMAT);
        cfg.add("logging.xml");
        pilot = new DefaultProjectPilot();
    }

    /**
     * <p>
     * Tear down the test fixture. Remove all namespaces added for test cases.
     * </p>
     * @throws Exception - to JUnit.
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        ConfigManager cfg = ConfigManager.getInstance();
        for (Iterator it = cfg.getAllNamespaces(); it.hasNext();) {
            cfg.removeNamespace((String) it.next());
        }
    }

    /**
     * Test method for {@link DefaultProjectPilot#DefaultProjectPilot()}.
     * Verifies empty ctor works properly.
     */
    public void testDefaultProjectPilot() {
        assertTrue(pilot instanceof ProjectPilot);
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#DefaultProjectPilot(String, String, String, String, String)}.
     * Verifies ctor with namespace, phase manager key, schedule status name, open status name, log
     * name works properly.
     * @throws Exception - to JUnit.
     */
    public void testDefaultProjectPilotStringStringStringStringString() throws Exception {
        pilot = new DefaultProjectPilot(DefaultProjectPilot.class.getName(), "ProjectPilot",
            "Scheduled", "Open", "ProjectPilot");
        assertTrue(pilot instanceof ProjectPilot);
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#DefaultProjectPilot(PhaseManager, String, String, com.topcoder.util.log.Log)}.
     * Verifies ctor with phase manager, schedule status name, open status name, log works properly.
     * @throws Exception - to JUnit.
     */
    public void testDefaultProjectPilotPhaseManagerStringStringLog() throws Exception {
        pilot = new DefaultProjectPilot(new DefaultPhaseManager("phase_manager"), "Scheduled",
            "Open", LogFactory.getLog());
        assertTrue(pilot instanceof ProjectPilot);
    }

    /**
     * Test method for {@link DefaultProjectPilot#getPhaseManager()}.
     * Verifies return the correct phase manager.
     */
    public void testGetPhaseManager() {
        assertTrue(pilot.getPhaseManager() instanceof DefaultPhaseManager);
    }

    /**
     * Test method for {@link DefaultProjectPilot#getLogger()}.
     * Verifies return the correct log instance.
     */
    public void testGetLogger() {
        assertTrue(pilot.getLogger() instanceof BasicLog);
    }

    /**
     * Test method for {@link DefaultProjectPilot#getScheduledStatusName()}.
     * Verifies return the correct scheduled status name.
     */
    public void testGetScheduledStatusName() {
        assertEquals("Open", pilot.getOpenStatusName());
    }

    /**
     * Test method for {@link DefaultProjectPilot#getOpenStatusName()}.
     * Verifies return the correct open status name.
     */
    public void testGetOpenStatusName() {
        assertEquals("Open", pilot.getOpenStatusName());
    }

    /**
     * Test method for {@link DefaultProjectPilot#advancePhases(long, String)}.
     * Verifies return the correct advance results for project.
     * @throws Exception - to JUnit.
     */
    public void testAdvancePhases() throws Exception {
        AutoPilotResult result = pilot.advancePhases(1, "Check");
        assertEquals(1, result.getProjectId());
        assertEquals(0, result.getPhaseEndedCount());
        assertEquals(0, result.getPhaseStartedCount());
    }

    /**
     * Test method for {@link DefaultProjectPilot#advancePhases(long, String)}.
     * Verifies return the correct advance results for project. The phases would be started or ended.
     * @throws Exception - to JUnit.
     */
    public void testAdvancePhases2() throws Exception {
        pilot = new DefaultProjectPilot(new DefaultPhaseManager2("pm2"),
            DefaultProjectPilot.DEFAULT_SCHEDULED_STATUS_NAME,
            DefaultProjectPilot.DEFAULT_OPEN_STATUS_NAME, LogFactory.getLog("ProjetPilot"));

        AutoPilotResult result = pilot.advancePhases(1, "Check");
        assertEquals(1, result.getProjectId());
        assertEquals(1, result.getPhaseEndedCount());
        assertEquals(1, result.getPhaseStartedCount());
    }

    /**
     * Test method for {@link DefaultProjectPilot#advancePhases(long, String)}.
     * Verifies return the correct advance results for project which contains null phases array.
     * @throws Exception - to JUnit.
     */
    public void testAdvancePhasesNullPhases() throws Exception {
        pilot = new DefaultProjectPilot(new DefaultPhaseManager3(),
            DefaultProjectPilot.DEFAULT_SCHEDULED_STATUS_NAME,
            DefaultProjectPilot.DEFAULT_OPEN_STATUS_NAME, LogFactory.getLog("ProjetPilot"));

        AutoPilotResult result = pilot.advancePhases(2, "Check");
        assertEquals(2, result.getProjectId());
        assertEquals(0, result.getPhaseEndedCount());
        assertEquals(0, result.getPhaseStartedCount());
    }

    /**
     * Test method for {@link DefaultProjectPilot#advancePhases(long, String)}.
     * Verifies return the correct advance results for project which contains empty phases array.
     * @throws Exception - to JUnit.
     */
    public void testAdvancePhasesEmptyPhases() throws Exception {
        pilot = new DefaultProjectPilot(new DefaultPhaseManager3(),
            DefaultProjectPilot.DEFAULT_SCHEDULED_STATUS_NAME,
            DefaultProjectPilot.DEFAULT_OPEN_STATUS_NAME, LogFactory.getLog("ProjetPilot"));

        AutoPilotResult result = pilot.advancePhases(1, "Check");
        assertEquals(1, result.getProjectId());
        assertEquals(0, result.getPhaseEndedCount());
        assertEquals(0, result.getPhaseStartedCount());
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#processPhase(Phase, java.util.Set, String)}.
     * Verifies return the correct advance results for phase.
     * @throws Exception - to JUnit.
     */
    public void testProcessPhase() throws Exception {
        int[] result = pilot.processPhase(pilot.getPhaseManager().getPhases(1).getAllPhases()[0],
            new HashSet(), "Check");
        assertEquals(2, result.length);
        assertEquals(0, result[0]);
        assertEquals(0, result[1]);
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#doPhaseOperation(Phase, String)}.
     * Verifies return the correct operation results for phase.
     * @throws Exception - to JUnit.
     */
    public void testDoPhaseOperation() throws Exception {
        int[] result = pilot.doPhaseOperation(
            pilot.getPhaseManager().getPhases(1).getAllPhases()[0], "Check");
        assertEquals(2, result.length);
        assertEquals(0, result[0]);
        assertEquals(0, result[1]);
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#doPhaseOperation(Phase, String)}.
     * Verifies return the correct operation results for phase.
     * @throws Exception - to JUnit.
     */
    public void testDoPhaseOperationNullStatus() throws Exception {
        pilot = new DefaultProjectPilot(new DefaultPhaseManager3(),
            DefaultProjectPilot.DEFAULT_SCHEDULED_STATUS_NAME,
            DefaultProjectPilot.DEFAULT_OPEN_STATUS_NAME, LogFactory.getLog("ProjetPilot"));

        int[] result = pilot.doPhaseOperation(new Phase(pilot.getPhaseManager().getPhases(1), 0), "Check");
        assertEquals(2, result.length);
        assertEquals(0, result[0]);
        assertEquals(0, result[0]);
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#doAudit(Phase, boolean, String)}.
     * Verifies audit the phase properly.
     * @throws Exception - to JUnit.
     */
    public void testDoAudit() throws Exception {
        pilot.doAudit(pilot.getPhaseManager().getPhases(1).getAllPhases()[0], true, "Check");
        pilot.doAudit(pilot.getPhaseManager().getPhases(2).getAllPhases()[1], false, "Check");
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#doAudit(Phase, boolean, String)}.
     * Verifies audit the phase properly.
     * @throws Exception - to JUnit.
     */
    public void testDoAuditNullType() throws Exception {
        pilot = new DefaultProjectPilot(new DefaultPhaseManager3(),
            DefaultProjectPilot.DEFAULT_SCHEDULED_STATUS_NAME,
            DefaultProjectPilot.DEFAULT_OPEN_STATUS_NAME, LogFactory.getLog("ProjetPilot"));

        pilot.doAudit(new Phase(pilot.getPhaseManager().getPhases(1), 0), true, "Check");
    }

    /**
     * Aggregates the test suite.
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(DefaultProjectPilotTest.class);

        return suite;
    }

}
