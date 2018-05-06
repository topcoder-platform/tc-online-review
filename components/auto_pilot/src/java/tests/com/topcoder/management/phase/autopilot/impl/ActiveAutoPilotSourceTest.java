/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot.impl;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.management.phase.autopilot.AutoPilotSource;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManagerImpl;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * Unit tests for class <code>ActiveAutoPilotSource</code>.
 * </p>
 * @author abelli
 * @version 1.0
 */
public class ActiveAutoPilotSourceTest extends TestCase {

    /** ActiveAutoPilotSource instance used in test cases.*/
    private ActiveAutoPilotSource source;

    /**
     * <p>
     * Setup the test fixture. Load namespaces used to create ActiveAutoPilotSource instance.
     * Then create the ActiveAutoPilotSource instance.
     * </p>
     * @throws Exception - to JUnit.
     */
    protected void setUp() throws Exception {
        ConfigManager cfg = ConfigManager.getInstance();
        cfg.add(ActiveAutoPilotSource.class.getName(), "active_auto_source_pilot.xml",
            ConfigManager.CONFIG_XML_FORMAT);
        source = new ActiveAutoPilotSource();
    }

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
     * Verifies empty ctor works properly.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSource() throws Exception {
        source = new ActiveAutoPilotSource();
        assertTrue(source instanceof AutoPilotSource);
        assertTrue(source.getProjectManager() instanceof ProjectManagerImpl);
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(String, String, String, String, String)}.
     * Verifies ctor with namespace, project manager key, active status name, extProp, extPropVal works properly.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSourceStringStringStringStringString() throws Exception {
        source = new ActiveAutoPilotSource(ActiveAutoPilotSource.class.getName(), "ProjectManager",
            "Active", "Autopilot Option", "On");
        assertTrue(source instanceof AutoPilotSource);
        assertTrue(source.getProjectManager() instanceof ProjectManagerImpl);
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(ProjectManager, String, String, String)}.
     * Verifies ctor with project manager, active status name, extProp, extPropVal works properly.
     * @throws Exception - to JUnit.
     */
    public void testActiveAutoPilotSourceProjectManagerStringStringString() throws Exception {
        source = new ActiveAutoPilotSource(new ProjectManagerImpl("project_manager"), "Active",
            "Autopilot Option", "On");
        assertTrue(source instanceof AutoPilotSource);
        assertTrue(source.getProjectManager() instanceof ProjectManagerImpl);
    }

    /**
     * Test method for {@link ActiveAutoPilotSource#getProjectManager()}.
     * Verifies return the correct project manager instance.
     */
    public void testGetProjectManager() {
        assertTrue(source.getProjectManager() instanceof ProjectManagerImpl);
    }

    /**
     * Test method for {@link ActiveAutoPilotSource#getActiveStatusName()}.
     * Verifies return the correct active status name.
     */
    public void testGetActiveStatusName() {
        assertEquals("Active", source.getActiveStatusName());
    }

    /**
     * Test method for {@link ActiveAutoPilotSource#getExtPropAutoPilotSwitch()}.
     * Verifies return the correct extProp.
     */
    public void testGetExtPropAutoPilotSwitch() {
        assertEquals("Autopilot Option", source.getExtPropAutoPilotSwitch());
    }

    /**
     * Test method for {@link ActiveAutoPilotSource#getExtPropAutoPilotSwitchValue()}.
     * Verifies return the correct extPropVal.
     */
    public void testGetExtPropAutoPilotSwitchValue() {
        assertEquals("On", source.getExtPropAutoPilotSwitchValue());
    }

    /**
     * Test method for {@link ActiveAutoPilotSource#getProjectIds()}.
     * Verifies return the correct project ids from project manager.
     * @throws Exception - to JUnit.
     */
    public void testGetProjectIds() throws Exception {
        long[] ids = source.getProjectIds();
        assertEquals(5, ids.length);
        assertEquals(1, ids[0]);
        assertEquals(2, ids[1]);
    }

    /**
     * Test method for {@link ActiveAutoPilotSource#buildFilter()}.
     * Verifies return the correct filter.
     */
    public void testBuildFilter() {
        Filter f = source.buildFilter();
        assertEquals(Filter.AND_FILTER, f.getFilterType());
    }

    /**
     * Test method for
     * {@link ActiveAutoPilotSource#processProject(Project[])}.
     * Verifies return the correct project ids from the project array.
     * @throws Exception - to JUnit.
     */
    public void testProcessProject() throws Exception {
        long[] ids = source.processProject(null);
        assertEquals(0, ids.length);

        // some projects.
        Project[] projects = source.getProjectManager().getProjects(new long[] {1, 2});
        ids = source.processProject(projects);
        assertEquals(1, ids[0]);
        assertEquals(2, ids[1]);
    }

    /**
     * Aggregates the test suite.
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(ActiveAutoPilotSourceTest.class);

        return suite;
    }

}
