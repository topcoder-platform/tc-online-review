/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot.impl;

import java.util.Iterator;

import com.topcoder.management.phase.autopilot.AutoPilotSource;
import com.topcoder.management.phase.autopilot.AutoPilotSourceException;
import com.topcoder.management.phase.autopilot.ConfigurationException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManagerImpl;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.config.ConfigManager;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Failure tests for class <code>ActiveAutoPilotSource</code>.
 * </p>
 * @author abelli
 * @version 1.0
 */
public class ActiveAutoPilotSourceFailure extends TestCase {

    /**
     * <p>
     * Tear down the test fixture. Remove all namespaces added for test cases.
     * </p>
     * @throws Exception - to JUnit.
     */
    protected void tearDown() throws Exception {
        ConfigManager cfg = ConfigManager.getInstance();
        for (Iterator it = cfg.getAllNamespaces(); it.hasNext();) {
            cfg.removeNamespace((String) it.next());
        }
    }

    /**
     * Test method for {@link ActiveAutoPilotSource#ActiveAutoPilotSource()}.
     * Fails if no namespace defined.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSource() throws Exception {
        try {
            new ActiveAutoPilotSource();
            fail("no namespace defined");
        } catch (ConfigurationException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(String, String, String, String, String)}.
     * Fails if null namespace.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSourceStringStringStringStringStringNullNamespace() throws Exception {
        try {
            new ActiveAutoPilotSource(null, "pmkey", "asname", "extprop", "extpropval");
            fail("null namespace");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(String, String, String, String, String)}.
     * Fails if empty namespace.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSourceStringStringStringStringStringEmptyNamespace() throws Exception {
        try {
            new ActiveAutoPilotSource(" \r\t\n", "pmkey", "asname", "extprop", "extpropval");
            fail("empty namespace");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(String, String, String, String, String)}.
     * Fails if null project manager key.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSourceStringStringStringStringStringNullPmkey() throws Exception {
        try {
            new ActiveAutoPilotSource("namespace", null, "asname", "extprop", "extpropval");
            fail("null project manager key");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(String, String, String, String, String)}.
     * Fails if empty project manager key.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSourceStringStringStringStringStringEmptyPmkey() throws Exception {
        try {
            new ActiveAutoPilotSource("namespace", " \r\t\n", "asname", "extprop", "extpropval");
            fail("empty project manager key");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(String, String, String, String, String)}.
     * Fails if null active status name.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSourceStringStringStringStringStringNullAsname() throws Exception {
        try {
            new ActiveAutoPilotSource("namespace", "pmkey", null, "extprop", "extpropval");
            fail("null active status name");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(String, String, String, String, String)}.
     * Fails if empty active status name.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSourceStringStringStringStringStringEmptyAsname() throws Exception {
        try {
            new ActiveAutoPilotSource("namespace", "pmkey", " \r\t\n", "extprop", "extpropval");
            fail("empty active status name");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(String, String, String, String, String)}.
     * Fails if null extProp.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSourceStringStringStringStringStringNullExtProp() throws Exception {
        try {
            new ActiveAutoPilotSource("namespace", "pmkey", "asname", null, "extpropval");
            fail("null extProp");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(String, String, String, String, String)}.
     * Fails if empty extProp.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSourceStringStringStringStringStringEmptyExtProp() throws Exception {
        try {
            new ActiveAutoPilotSource("namespace", "pmkey", "asname", " \r\t\n", "extpropval");
            fail("empty extProp");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(String, String, String, String, String)}.
     * Fails if null extPropVal.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSourceStringStringStringStringStringNullExtPropVal() throws Exception {
        try {
            new ActiveAutoPilotSource("namespace", "pmkey", "asname", "extprop", null);
            fail("null extPropVal");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(String, String, String, String, String)}.
     * Fails if empty extPropVal.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSourceStringStringStringStringStringEmptyExtPropVal() throws Exception {
        try {
            new ActiveAutoPilotSource("namespace", "pmkey", "asname", "extprop", "\r\t\n ");
            fail("empty extPropVal");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(ProjectManager, String, String, String)}.
     * Fails if null project manager.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSourceProjectManagerStringStringStringNullPm() throws Exception {
        try {
            new ActiveAutoPilotSource(null, "asname", "extprop", "extPropVal");
            fail("null project manager");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(ProjectManager, String, String, String)}.
     * Fails if null active status name.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSourceProjectManagerStringStringStringNullAsname() throws Exception {
        try {
            new ActiveAutoPilotSource(new ProjectManagerImpl("pm"), null, "extprop", "extPropVal");
            fail("null active status name");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(ProjectManager, String, String, String)}.
     * Fails if empty active status name.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSourceProjectManagerStringStringStringEmptyAsname() throws Exception {
        try {
            new ActiveAutoPilotSource(new ProjectManagerImpl("pm"), " \r\t\n", "extprop", "extPropVal");
            fail("empty active status name");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(ProjectManager, String, String, String)}.
     * Fails if null extProp.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSourceProjectManagerStringStringStringNullExtProp() throws Exception {
        try {
            new ActiveAutoPilotSource(new ProjectManagerImpl("pm"), "asname", null, "extPropVal");
            fail("null extProp");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(ProjectManager, String, String, String)}.
     * Fails if empty extProp.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSourceProjectManagerStringStringStringEmptyExtProp() throws Exception {
        try {
            new ActiveAutoPilotSource(new ProjectManagerImpl("pm"), "asname", " \r\t\n", "extPropVal");
            fail("empty extProp");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(ProjectManager, String, String, String)}.
     * Fails if null extPropVal.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSourceProjectManagerStringStringStringNullExtPropVal() throws Exception {
        try {
            new ActiveAutoPilotSource(new ProjectManagerImpl("pm"), "asname", "extProp", null);
            fail("null extPropVal");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(ProjectManager, String, String, String)}.
     * Fails if empty extPropVal.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSourceProjectManagerStringStringStringEmptyExtPropVal() throws Exception {
        try {
            new ActiveAutoPilotSource(new ProjectManagerImpl("pm"), "asname", "extProp", " \r\t\n");
            fail("empty extPropVal");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * <p>
     * Test method for {@link ActiveAutoPilotSource#getProjectIds()}.
     * Fails if project manager reports an error.
     * </p>
     * @throws Exception - to JUnit.
     */
    public void testGetProjectIdsException() throws Exception {
        AutoPilotSource source = new ActiveAutoPilotSource(new ProjectManagerImpl() {
            public Project[] searchProjects(Filter filter) throws PersistenceException {
                throw new PersistenceException("test");
            }
        }, ActiveAutoPilotSource.DEFAULT_ACTIVE_STATUS_NAME,
            ActiveAutoPilotSource.DEFAULT_EXTPROP_AUTOPILOTSWITCH,
            ActiveAutoPilotSource.DEFAULT_EXTPROP_AUTOPILOTSWITCH_VALUE);

        try {
            source.getProjectIds();
            fail("project manager reports an error");
        } catch (AutoPilotSourceException e) {
            // Good.
        }
    }

    /**
     * Aggregates the test suite.
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(ActiveAutoPilotSourceFailure.class);

        return suite;
    }

}
