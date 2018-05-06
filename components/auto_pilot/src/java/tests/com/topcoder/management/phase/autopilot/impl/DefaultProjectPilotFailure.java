/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot.impl;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.management.phase.DefaultPhaseManager;
import com.topcoder.management.phase.autopilot.ConfigurationException;
import com.topcoder.management.phase.autopilot.PhaseOperationException;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

/**
 * <p>
 * Failure tests for class <code>DefaultProjectPilot</code>.
 * </p>
 * @author abelli
 * @version 1.0
 */
public class DefaultProjectPilotFailure extends TestCase {

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
     * <p>
     * Create the Log instance for test cases.
     * </p>
     * @return the Log instance.
     * @throws Exception - to JUnit.
     */
    protected Log createLog() throws Exception {
        ConfigManager cfg = ConfigManager.getInstance();
        cfg.add("logging.xml");
        return LogFactory.getLog();
    }

    /**
     * <p>
     * Create Project pilot instance for test cases.
     * </p>
     * @return the ProjectPilot instance.
     * @throws Exception - to JUnit.
     */
    protected DefaultProjectPilot createProjectPilot() throws Exception {
        ConfigManager cfg = ConfigManager.getInstance();
        cfg.add(DefaultProjectPilot.class.getName(), "project_pilot.xml",
            ConfigManager.CONFIG_XML_FORMAT);
        cfg.add("logging.xml");
        return new DefaultProjectPilot();
    }

    /**
     * Test method for {@link DefaultProjectPilot#DefaultProjectPilot()}.
     * Fails if no namespace defined.
     * @throws Exception - to JUnit.
     */
    public void testDefaultProjectPilot() throws Exception {
        try {
            new DefaultProjectPilot();
            fail("no namespace defined");
        } catch (ConfigurationException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#DefaultProjectPilot(String, String, String, String, String)}.
     * Fails if null namespace.
     * @throws Exception - to JUnit.
     */
    public void testDefaultProjectPilotStringStringStringStringStringNullNamespace() throws Exception {
        try {
            new DefaultProjectPilot(null, "pmkey", "scheduledStatusName", "openStatusName", "logName");
            fail("null namespace");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#DefaultProjectPilot(String, String, String, String, String)}.
     * Fails if empty namespace.
     * @throws Exception - to JUnit.
     */
    public void testDefaultProjectPilotStringStringStringStringStringEmptyName() throws Exception {
        try {
            new DefaultProjectPilot(" \r\t\n", "pmkey", "scheduledStatusName", "openStatusName", "logName");
            fail("empty namespace");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#DefaultProjectPilot(String, String, String, String, String)}.
     * Fails if null phase manager key.
     * @throws Exception - to JUnit.
     */
    public void testDefaultProjectPilotStringStringStringStringStringNullPmkey() throws Exception {
        try {
            new DefaultProjectPilot("namespace", null, "scheduledStatusName", "openStatusName", "logName");
            fail("null pmkey");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#DefaultProjectPilot(String, String, String, String, String)}.
     * Fails if empty phase manager key.
     * @throws Exception - to JUnit.
     */
    public void testDefaultProjectPilotStringStringStringStringStringEmptyPmkey() throws Exception {
        try {
            new DefaultProjectPilot("namespace", " \r\t\n", "scheduledStatusName", "openStatusName", "logName");
            fail("empty pmkey");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#DefaultProjectPilot(String, String, String, String, String)}.
     * Fails if null scheduledStatusName.
     * @throws Exception - to JUnit.
     */
    public void testDefaultProjectPilotStringStringStringStringStringNullScheduledStatusName() throws Exception {
        try {
            new DefaultProjectPilot("namespace", "pmkey", null, "openStatusName", "logName");
            fail("null scheduledStatusName");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#DefaultProjectPilot(String, String, String, String, String)}.
     * Fails if empty scheduledStatusName.
     * @throws Exception - to JUnit.
     */
    public void testDefaultProjectPilotStringStringStringStringStringEmptyScheduledStatusName() throws Exception {
        try {
            new DefaultProjectPilot("namespace", "pmkey", " \r\t\n", "openStatusName", "logName");
            fail("empty scheduledStatusName");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#DefaultProjectPilot(String, String, String, String, String)}.
     * Fails if null openStatusName.
     * @throws Exception - to JUnit.
     */
    public void testDefaultProjectPilotStringStringStringStringStringNullOpenStatusName() throws Exception {
        try {
            new DefaultProjectPilot("namespace", "pmkey", "scheduledStatusName", null, "logName");
            fail("null openStatusName");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#DefaultProjectPilot(String, String, String, String, String)}.
     * Fails if empty openStatusName.
     * @throws Exception - to JUnit.
     */
    public void testDefaultProjectPilotStringStringStringStringStringEmptyOpenStatusName() throws Exception {
        try {
            new DefaultProjectPilot("namespace", "pmkey", "scheduledStatusName", " \r\t\n", "logName");
            fail("empty openStatusName");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#DefaultProjectPilot(String, String, String, String, String)}.
     * Fails if null logName.
     * @throws Exception - to JUnit.
     */
    public void testDefaultProjectPilotStringStringStringStringStringNullLogName() throws Exception {
        try {
            new DefaultProjectPilot("namespace", "pmkey", "scheduledStatusName", "openStatusName", null);
            fail("null logName");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#DefaultProjectPilot(String, String, String, String, String)}.
     * Fails if empty logName.
     * @throws Exception - to JUnit.
     */
    public void testDefaultProjectPilotStringStringStringStringStringEmptyLogName() throws Exception {
        try {
            new DefaultProjectPilot("namespace", "pmkey", "scheduledStatusName", "openStatusName", " \r\t\n");
            fail("empty logName");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#DefaultProjectPilot(PhaseManager, String, String, com.topcoder.util.log.Log)}.
     * Fails if null phase manager.
     * @throws Exception - to JUnit.
     */
    public void testDefaultProjectPilotPhaseManagerStringStringLogNullPhaseManager() throws Exception {
        try {
            new DefaultProjectPilot(null, "scheduledStatusName", "openStatusName", createLog());
            fail("null phase manager");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#DefaultProjectPilot(PhaseManager, String, String, com.topcoder.util.log.Log)}.
     * Fails if null scheduledStatusName.
     * @throws Exception - to JUnit.
     */
    public void testDefaultProjectPilotPhaseManagerStringStringLogNullScheduledStatusName() throws Exception {
        try {
            new DefaultProjectPilot(new DefaultPhaseManager("pm"), null, "openStatusName", createLog());
            fail("null scheduledStatusName");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#DefaultProjectPilot(PhaseManager, String, String, com.topcoder.util.log.Log)}.
     * Fails if empty scheduledStatusName.
     * @throws Exception - to JUnit.
     */
    public void testDefaultProjectPilotPhaseManagerStringStringLogEmptyScheduledStatusName() throws Exception {
        try {
            new DefaultProjectPilot(new DefaultPhaseManager("pm"), " \r\t\n", "openStatusName", createLog());
            fail("empty scheduledStatusName");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#DefaultProjectPilot(PhaseManager, String, String, com.topcoder.util.log.Log)}.
     * Fails if null openStatusName.
     * @throws Exception - to JUnit.
     */
    public void testDefaultProjectPilotPhaseManagerStringStringLogNullOpenStatusName() throws Exception {
        try {
            new DefaultProjectPilot(new DefaultPhaseManager("pm"), "scheduledStatusName", null, createLog());
            fail("null openStatusName");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#DefaultProjectPilot(PhaseManager, String, String, com.topcoder.util.log.Log)}.
     * Fails if empty openStatusName.
     * @throws Exception - to JUnit.
     */
    public void testDefaultProjectPilotPhaseManagerStringStringLogEmptyOpenStatusName() throws Exception {
        try {
            new DefaultProjectPilot(new DefaultPhaseManager("pm"), "scheduledStatusName", " \r\t\n", createLog());
            fail("empty openStatusName");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link DefaultProjectPilot#DefaultProjectPilot(PhaseManager, String, String, com.topcoder.util.log.Log)}.
     * Fails if null log.
     * @throws Exception - to JUnit.
     */
    public void testDefaultProjectPilotPhaseManagerStringStringLogNullLog() throws Exception {
        try {
            new DefaultProjectPilot(new DefaultPhaseManager("pm"), "scheduledStatusName", "openStatusName", null);
            fail("null log");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link DefaultProjectPilot#advancePhases(long, String)}.
     * Fails if null operator.
     * @throws Exception - to JUnit.
     */
    public void testAdvancePhasesNullOperator() throws Exception {
        try {
            createProjectPilot().advancePhases(1, null);
            fail("null operator");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link DefaultProjectPilot#advancePhases(long, String)}.
     * Fails if null project.
     * PhaseOperationException ignored in DefaultProjectPilot#advancePhases, so no exception thrown.
     * @throws Exception - to JUnit.
     */
    public void testAdvancePhasesNullProject() throws Exception {
        // PhaseOperationException ignored in DefaultProjectPilot#advancePhases, so no exception thrown.
        createProjectPilot().advancePhases(8, "Checker");
    }

    /**
     * Test method for {@link DefaultProjectPilot#advancePhases(long, String)}.
     * Fails if empty operator.
     * @throws Exception - to JUnit.
     */
    public void testAdvancePhasesEmptyOperator() throws Exception {
        try {
            createProjectPilot().advancePhases(1, " \r\t\n");
            fail("empty operator");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Aggregates the test suite.
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(DefaultProjectPilotFailure.class);

        return suite;
    }

}
