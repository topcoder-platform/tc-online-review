/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.management.phase.DefaultPhaseManager2;
import com.topcoder.management.phase.autopilot.impl.ActiveAutoPilotSource;
import com.topcoder.management.phase.autopilot.impl.DefaultProjectPilot;
import com.topcoder.management.project.ProjectManagerImpl2;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.log.LogFactory;

/**
 * <p>
 * Unit tests for class <code>AutoPilot</code>.
 * </p>
 * @author abelli
 * @version 1.0
 */
public class AutoPilotTest extends TestCase {
    /** AutoPilot instance for test cases.*/
    private AutoPilot autoPilot;

    /**
     * <p>
     * Setup the test fixture. Load namespaces used to create AutoPilot instance,
     * then create AutoPilot instance.
     * </p>
     * @throws Exception - to JUnit.
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        ConfigManager cfg = ConfigManager.getInstance();
        cfg.add(ActiveAutoPilotSource.class.getName(), "active_auto_source_pilot.xml",
            ConfigManager.CONFIG_XML_FORMAT);
        cfg.add(DefaultProjectPilot.class.getName(), "project_pilot.xml",
            ConfigManager.CONFIG_XML_FORMAT);
        cfg.add("logging.xml");
        cfg.add(AutoPilot.class.getName(), "auto_pilot.xml", ConfigManager.CONFIG_XML_FORMAT);
        autoPilot = new AutoPilot();
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
     * Test method for {@link AutoPilot#AutoPilot()}.
     * Verifies ctor works properly.
     */
    public void testAutoPilot() {
        assertTrue(autoPilot instanceof AutoPilot);
    }

    /**
     * Test method for
     * {@link AutoPilot#AutoPilot(java.lang.String, java.lang.String, java.lang.String)}.
     * Verifies ctor with namespace, auto pilot source key, project pilot key works properly.
     * @throws Exception - to JUnit.
     */
    public void testAutoPilotStringStringString() throws Exception {
        autoPilot = new AutoPilot(AutoPilot.class.getName(), AutoPilotSource.class.getName(),
            ProjectPilot.class.getName());
        assertTrue(autoPilot instanceof AutoPilot);
    }

    /**
     * Test method for {@link AutoPilot#AutoPilot(AutoPilotSource, ProjectPilot)}.
     * Verifies ctor with auto pilot source instance and project pilot instance works properly.
     * @throws Exception - to JUnit.
     */
    public void testAutoPilotAutoPilotSourceProjectPilot() throws Exception {
        AutoPilotSource autoPilotSource = new ActiveAutoPilotSource();
        ProjectPilot projectPilot = new DefaultProjectPilot();
        autoPilot = new AutoPilot(autoPilotSource, projectPilot);
        assertTrue(autoPilot instanceof AutoPilot);
    }

    /**
     * Test method for {@link AutoPilot#getAutoPilotSource()}.
     * Verifies return the auto pilot source instance.
     */
    public void testGetAutoPilotSource() {
        assertTrue(autoPilot.getAutoPilotSource() instanceof ActiveAutoPilotSource);
    }

    /**
     * Test method for {@link AutoPilot#getProjectPilot()}.
     * Verifies return the correct project pilot instance.
     */
    public void testGetProjectPilot() {
        assertTrue(autoPilot.getProjectPilot() instanceof DefaultProjectPilot);
    }

    /**
     * Test method for {@link AutoPilot#advanceProjects(java.lang.String)}.
     * Verifies return the correct projects pilot result.
     * @throws Exception - to JUnit.
     */
    public void testAdvanceProjectsString() throws Exception {
        AutoPilotResult[] results = autoPilot.advanceProjects("Checker");
        assertEquals(5, results.length);
        for (int i = 0; i < results.length; i++) {
            assertTrue(results[i].getProjectId() <= 5);
            assertEquals(0, results[i].getPhaseEndedCount());
            assertEquals(0, results[i].getPhaseStartedCount());
        }
    }

    /**
     * Test method for {@link AutoPilot#advanceProjects(java.lang.String)}.
     * Verifies return the correct projects pilot result. Some phases would be ended and started.
     * @throws Exception - to JUnit.
     */
    public void testAdvanceProjectsString2() throws Exception {
        createAutoPilot2();

        AutoPilotResult[] results = autoPilot.advanceProjects("Checker");
        assertEquals(1, results.length);
        assertEquals(1, results[0].getPhaseEndedCount());
        assertEquals(1, results[0].getPhaseStartedCount());
    }

    /**
     * <p>
     * Create auto pilot instance 2, which contains phases can be ended and started.
     * </p>
     * @throws Exception - to JUnit.
     */
    private void createAutoPilot2() throws Exception {
        AutoPilotSource autoPilotSource = new ActiveAutoPilotSource(new ProjectManagerImpl2(
            "project_manager"), "Active", "Autopilot Option", "On");
        ProjectPilot projectPilot = new DefaultProjectPilot(new DefaultPhaseManager2("pm2"),
            DefaultProjectPilot.DEFAULT_SCHEDULED_STATUS_NAME,
            DefaultProjectPilot.DEFAULT_OPEN_STATUS_NAME, LogFactory.getLog("ProjetPilot"));
        autoPilot = new AutoPilot(autoPilotSource, projectPilot);
    }

    /**
     * Test method for {@link AutoPilot#advanceProjects(long[], java.lang.String)}.
     * Verifies return the empty projects pilot result for empty projectId.
     * @throws Exception - to JUnit.
     */
    public void testAdvanceProjectsLongArrayStringEmptyProjectId() throws Exception {
        AutoPilotResult[] results = autoPilot.advanceProjects(new long[0], "Check");
        assertEquals(0, results.length);
    }

    /**
     * Test method for {@link AutoPilot#advanceProjects(long[], java.lang.String)}.
     * Verifies return the correct projects pilot result.
     * @throws Exception - to JUnit.
     */
    public void testAdvanceProjectsLongArrayString() throws Exception {
        AutoPilotResult[] results = autoPilot.advanceProjects(autoPilot.getAutoPilotSource()
            .getProjectIds(), "Check");
        assertEquals(5, results.length);
        for (int i = 0; i < results.length; i++) {
            assertTrue(results[i].getProjectId() <= 5);
            assertEquals(0, results[i].getPhaseEndedCount());
            assertEquals(0, results[i].getPhaseStartedCount());
        }
    }

    /**
     * Test method for {@link AutoPilot#advanceProjects(long[], java.lang.String)}.
     * Verifies return the correct projects pilot result. Some phases would be ended and started.
     * @throws Exception - to JUnit.
     */
    public void testAdvanceProjectsLongArrayString2() throws Exception {
        createAutoPilot2();

        AutoPilotResult[] results = autoPilot.advanceProjects(autoPilot.getAutoPilotSource()
            .getProjectIds(), "Check");
        assertEquals(1, results.length);
        assertEquals(results[0].getProjectId(), 1);
        assertEquals(1, results[0].getPhaseEndedCount());
        assertEquals(1, results[0].getPhaseStartedCount());
    }

    /**
     * Test method for {@link AutoPilot#advanceProject(long, java.lang.String)}.
     * Verifies return the correct project pilot result.
     * @throws Exception - to JUnit.
     */
    public void testAdvanceProject() throws Exception {
        AutoPilotResult result = autoPilot.advanceProject(1, "Check");
        assertEquals(1, result.getProjectId());
        assertEquals(0, result.getPhaseEndedCount());
        assertEquals(0, result.getPhaseStartedCount());
    }

    /**
     * Test method for {@link AutoPilot#advanceProject(long, java.lang.String)}.
     * Verifies return the correct project pilot result. Some phases would be ended and started.
     * @throws Exception - to JUnit.
     */
    public void testAdvanceProject2() throws Exception {
        createAutoPilot2();

        AutoPilotResult result = autoPilot.advanceProject(1, "Check");
        assertEquals(1, result.getProjectId());
        assertEquals(1, result.getPhaseEndedCount());
        assertEquals(1, result.getPhaseStartedCount());
    }

    /**
     * Aggregates the test suite.
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(AutoPilotTest.class);

        return suite;
    }

}
