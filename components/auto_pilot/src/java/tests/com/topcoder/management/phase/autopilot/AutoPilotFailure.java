/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.management.phase.autopilot.impl.ActiveAutoPilotSource;
import com.topcoder.management.phase.autopilot.impl.DefaultProjectPilot;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * Failure tests for class <code>AutoPilot</code>.
 * </p>
 * @author abelli
 * @version 1.0
 */
public class AutoPilotFailure extends TestCase {

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
     * Create auto pilot for test cases.
     * </p>
     * @return the AutoPilot instance.
     * @throws Exception - to JUnit.
     */
    protected AutoPilot createAutoPilot() throws Exception {
        ConfigManager cfg = ConfigManager.getInstance();
        cfg.add(ActiveAutoPilotSource.class.getName(), "active_auto_source_pilot.xml",
            ConfigManager.CONFIG_XML_FORMAT);
        cfg.add(DefaultProjectPilot.class.getName(), "project_pilot.xml",
            ConfigManager.CONFIG_XML_FORMAT);
        cfg.add("logging.xml");
        cfg.add(AutoPilot.class.getName(), "auto_pilot.xml", ConfigManager.CONFIG_XML_FORMAT);
        return new AutoPilot();
    }

    /**
     * <p>
     * Create project pilot for test cases.
     * </p>
     * @return the ProjectPilot instance.
     * @throws Exception - to JUnit.
     */
    protected ProjectPilot createProjectPilot() throws Exception {
        ConfigManager cfg = ConfigManager.getInstance();
        cfg.add(DefaultProjectPilot.class.getName(), "project_pilot.xml",
            ConfigManager.CONFIG_XML_FORMAT);
        cfg.add("logging.xml");
        return new DefaultProjectPilot();
    }
    /**
     * <p>
     * Create auto pilot source for test cases.
     * </p>
     * @return the AutoPilotSource instance.
     * @throws Exception - to JUnit.
     */
    protected AutoPilotSource createAutoPilotSource() throws Exception {
        ConfigManager cfg = ConfigManager.getInstance();
        cfg.add(ActiveAutoPilotSource.class.getName(), "active_auto_source_pilot.xml",
            ConfigManager.CONFIG_XML_FORMAT);
        return new ActiveAutoPilotSource();
    }

    /**
     * Test method for {@link AutoPilot#AutoPilot()}.
     * Fails if bad configuration.
     * @throws Exception - to JUnit.
     */
    public void testAutoPilot() throws Exception {
        try {
            new AutoPilot();
            fail("bad configuration");
        } catch (ConfigurationException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link AutoPilot#AutoPilot(java.lang.String, java.lang.String, java.lang.String)}.
     * Fails if null namespace.
     * @throws Exception - to JUnit.
     */
    public void testAutoPilotStringStringStringNullNamespace() throws Exception {
        try {
            new AutoPilot(null, "source", "project");
            fail("null namespace");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link AutoPilot#AutoPilot(java.lang.String, java.lang.String, java.lang.String)}.
     * Fails if empty namespace.
     * @throws Exception - to JUnit.
     */
    public void testAutoPilotStringStringStringEmptyNamespace() throws Exception {
        try {
            new AutoPilot(" \r\t\n", "source", "project");
            fail("empty namespace");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link AutoPilot#AutoPilot(java.lang.String, java.lang.String, java.lang.String)}.
     * Fails if null auto pilot source key.
     * @throws Exception - to JUnit.
     */
    public void testAutoPilotStringStringStringNullSource() throws Exception {
        try {
            new AutoPilot("namespace", null, "project");
            fail("null source key");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link AutoPilot#AutoPilot(java.lang.String, java.lang.String, java.lang.String)}.
     * Fails if empty auto pilot source key.
     * @throws Exception - to JUnit.
     */
    public void testAutoPilotStringStringStringEmptySource() throws Exception {
        try {
            new AutoPilot("namespace", " \r\t\n", "project");
            fail("empty source key");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link AutoPilot#AutoPilot(java.lang.String, java.lang.String, java.lang.String)}.
     * Fails if null project pilot key.
     * @throws Exception - to JUnit.
     */
    public void testAutoPilotStringStringStringNullProject() throws Exception {
        try {
            new AutoPilot("namespace", "source", null);
            fail("null project pilot key");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link AutoPilot#AutoPilot(java.lang.String, java.lang.String, java.lang.String)}.
     * Fails if empty project pilot key.
     * @throws Exception - to JUnit.
     */
    public void testAutoPilotStringStringStringEmptyProject() throws Exception {
        try {
            new AutoPilot("namespace", "source", "\r\t\n");
            fail("empty project pilot key");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilot#AutoPilot(AutoPilotSource, ProjectPilot)}.
     * Fails if null auto pilot source.
     * @throws Exception - to JUnit.
     */
    public void testAutoPilotAutoPilotSourceProjectPilotNullSource() throws Exception {
        try {
            new AutoPilot(null, createProjectPilot());
            fail("null auto pilot source");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilot#AutoPilot(AutoPilotSource, ProjectPilot)}.
     * Fails if null project pilot.
     * @throws Exception - to JUnit.
     */
    public void testAutoPilotAutoPilotSourceProjectPilotNullProject() throws Exception {
        try {
            new AutoPilot(createAutoPilotSource(), null);
            fail("null project pilot");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilot#advanceProjects(java.lang.String)}.
     * Fails if null operator.
     * @throws Exception - to JUnit.
     */
    public void testAdvanceProjectsStringNull() throws Exception {
        try {
            createAutoPilot().advanceProjects(null);
            fail("null operator");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilot#advanceProjects(java.lang.String)}.
     * Fails if empty operator.
     * @throws Exception - to JUnit.
     */
    public void testAdvanceProjectsStringEmpty() throws Exception {
        try {
            createAutoPilot().advanceProjects(" \r\t\n");
            fail("empty operator");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilot#advanceProjects(long[], java.lang.String)}.
     * Fails if null project id.
     * @throws Exception - to JUnit.
     */
    public void testAdvanceProjectsLongArrayStringNullId() throws Exception {
        try {
            createAutoPilot().advanceProjects(null, "Operator");
            fail("null projectId");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilot#advanceProjects(long[], java.lang.String)}.
     * Fails if null operator.
     * @throws Exception - to JUnit.
     */
    public void testAdvanceProjectsLongArrayStringNullOperator() throws Exception {
        try {
            createAutoPilot().advanceProjects(new long[0], null);
            fail("null operator");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilot#advanceProjects(long[], java.lang.String)}.
     * Fails if empty operator.
     * @throws Exception - to JUnit.
     */
    public void testAdvanceProjectsLongArrayStringEmptyOperator() throws Exception {
        try {
            createAutoPilot().advanceProjects(new long[0], " \r\t\n");
            fail("empty operator");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilot#advanceProject(long, java.lang.String)}.
     * Fails if null operator.
     * @throws Exception - to JUnit.
     */
    public void testAdvanceProjectNull() throws Exception {
        try {
            createAutoPilot().advanceProject(0, null);
            fail("null operator");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilot#advanceProject(long, java.lang.String)}.
     * Fails if empty operator.
     * @throws Exception - to JUnit.
     */
    public void testAdvanceProjectEmpty() throws Exception {
        try {
            createAutoPilot().advanceProject(0, " \r\n\t");
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
        suite.addTestSuite(AutoPilotFailure.class);

        return suite;
    }

}
