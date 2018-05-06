/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.accuracytests.impl;

import com.topcoder.management.phase.autopilot.accuracytests.TestDataFactory;
import com.topcoder.management.phase.autopilot.accuracytests.ConfigHelper;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockAutoPilot;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockAutoPilotSource;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockAutoPilotSourceAlternate;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockLog;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockLogAlternate;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockPhaseManager;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectManagerAlternate;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectManager;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectPilot;
import com.topcoder.management.phase.autopilot.accuracytests.mock.MockProjectPilotAlternate;
import com.topcoder.management.phase.autopilot.impl.ActiveAutoPilotSource;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Arrays;
import java.util.List;
import java.io.File;

/**
 * <p>An accuracy test for {@link com.topcoder.management.phase.autopilot.impl.ActiveAutoPilotSource} class. Tests the
 * methods for proper handling of valid input data and producing accurate results. Passes the valid arguments to the
 * methods and verifies that either the state of the tested instance have been changed appropriately or a correct result
 * is produced by the method.</p>
 *
 * @author isv
 * @version 1.0
 */
public class ActiveAutoPilotSourceAccuracyTest extends TestCase {

    /**
     * <p>The instances of {@link ActiveAutoPilotSource} which are tested. These instances are initialized in {@link
     * #setUp()} method and released in {@link #tearDown()} method. Each instance is initialized using a separate
     * constructor provided by the tested class.<p>
     */
    private ActiveAutoPilotSourceSubclass[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link ActiveAutoPilotSource} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link ActiveAutoPilotSource} class.
     */
    public static Test suite() {
        return new TestSuite(ActiveAutoPilotSourceAccuracyTest.class);
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

        this.testedInstances = new ActiveAutoPilotSourceSubclass[3];
        this.testedInstances[0] = new ActiveAutoPilotSourceSubclass();
        this.testedInstances[1] = new ActiveAutoPilotSourceSubclass(TestDataFactory.AUTO_PILOT_SOURCE_NAMESPACE,
                                                                    TestDataFactory.PROJECT_MANAGER_CONFIG_PROPERTY,
                                                                    TestDataFactory.ACTIVE_PROJECT_STATUS,
                                                                    TestDataFactory.AUTO_PILOT_PROPERTY_NAME,
                                                                    TestDataFactory.AUTO_PILOT_PROPERTY_VALUE);
        this.testedInstances[2] = new ActiveAutoPilotSourceSubclass(TestDataFactory.getProjectManager(),
                                                                    TestDataFactory.ACTIVE_PROJECT_STATUS,
                                                                    TestDataFactory.AUTO_PILOT_PROPERTY_NAME,
                                                                    TestDataFactory.AUTO_PILOT_PROPERTY_VALUE);
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
     * <p>Accuracy test. Tests the {@link ActiveAutoPilotSource#ActiveAutoPilotSource()} constructor being provided with
     * valid input arguments.</p>
     *
     * <p>Verifies that the instance is initialized with default values. The {@link
     * ActiveAutoPilotSource#getActiveStatusName()}, {@link ActiveAutoPilotSource#getExtPropAutoPilotSwitch()}, {@link
     * ActiveAutoPilotSource#getExtPropAutoPilotSwitchValue()}, {@link ActiveAutoPilotSource#getProjectManager()}
     * methods are also indirectly tested by this test. </p>
     */
    public void testConstructor() {
        Assert.assertEquals("The active project status name is not correct.",
                            ActiveAutoPilotSource.DEFAULT_ACTIVE_STATUS_NAME,
                            this.testedInstances[0].getActiveStatusName());
        Assert.assertEquals("The auto-pilot extended project property name is not correct.",
                            ActiveAutoPilotSource.DEFAULT_EXTPROP_AUTOPILOTSWITCH,
                            this.testedInstances[0].getExtPropAutoPilotSwitch());
        Assert.assertEquals("The auto-pilot extended project property value is not correct.",
                            ActiveAutoPilotSource.DEFAULT_EXTPROP_AUTOPILOTSWITCH_VALUE,
                            this.testedInstances[0].getExtPropAutoPilotSwitchValue());
        Assert.assertEquals("The project manager is not correct.",
                            TestDataFactory.getProjectManager(),
                            this.testedInstances[0].getProjectManager());
    }

    /**
     * <p>Accuracy test. Tests the
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(String,String,String,String,String)} constructor being
     * provided with valid input arguments.</p>
     *
     * <p>Verifies that the instance is initialized with the provided values and the project manager is initialized
     * based on parameters of specified configuration namespace. The
     * {@link ActiveAutoPilotSource#getActiveStatusName()}, {@link ActiveAutoPilotSource#getExtPropAutoPilotSwitch()},
     * {@link ActiveAutoPilotSource#getExtPropAutoPilotSwitchValue()}, {@link ActiveAutoPilotSource#getProjectManager()}
     * methods are also indirectly tested by this test.</p>
     */
    public void testConstructor_ActiveAutoPilotSource_String_String_String_String_String_() {
        Assert.assertEquals("The active project status name is not correct.",
                            TestDataFactory.ACTIVE_PROJECT_STATUS, this.testedInstances[1].getActiveStatusName());
        Assert.assertEquals("The auto-pilot extended project property name is not correct.",
                            TestDataFactory.AUTO_PILOT_PROPERTY_NAME,
                            this.testedInstances[1].getExtPropAutoPilotSwitch());
        Assert.assertEquals("The auto-pilot extended project property value is not correct.",
                            TestDataFactory.AUTO_PILOT_PROPERTY_VALUE,
                            this.testedInstances[1].getExtPropAutoPilotSwitchValue());
        Assert.assertEquals("The project manager is not correct.",
                            TestDataFactory.getAlternateProjectManager(), this.testedInstances[1].getProjectManager());
    }

    /**
     * <p>Accuracy test. Tests the
     * {@link ActiveAutoPilotSource#ActiveAutoPilotSource(ProjectManager,String,String,String)} constructor being
     * provided with valid input arguments.</p>
     *  *
     * <p>Verifies that the instance is initialized with the provided values. The {@link
     * ActiveAutoPilotSource#getActiveStatusName()}, {@link ActiveAutoPilotSource#getExtPropAutoPilotSwitch()}, {@link
     * ActiveAutoPilotSource#getExtPropAutoPilotSwitchValue()}, {@link ActiveAutoPilotSource#getProjectManager()}
     * methods are also indirectly tested by this test.</p>
     */
    public void testConstructor_ActiveAutoPilotSource_ProjectManager_String_String_String_() {
        Assert.assertEquals("The active project status name is not correct.",
                            TestDataFactory.ACTIVE_PROJECT_STATUS, this.testedInstances[2].getActiveStatusName());
        Assert.assertEquals("The auto-pilot extended project property name is not correct.",
                            TestDataFactory.AUTO_PILOT_PROPERTY_NAME,
                            this.testedInstances[2].getExtPropAutoPilotSwitch());
        Assert.assertEquals("The auto-pilot extended project property value is not correct.",
                            TestDataFactory.AUTO_PILOT_PROPERTY_VALUE,
                            this.testedInstances[2].getExtPropAutoPilotSwitchValue());
        Assert.assertEquals("The project manager is not correct.",
                            TestDataFactory.getProjectManager(), this.testedInstances[2].getProjectManager());
    }

    /**
     * <p>Accuracy test. Tests the {@link ActiveAutoPilotSource#getProjectIds()} method for proper behavior.</p>
     *
     * <p>Verifies that the method returns the project IDs as they are returned by the underlying ProjectManager. Also
     * verifies that the appropriate filter is passed to ProjectManager for searching the active projects.</p>
     */
    public void testGetProjectIds() throws Exception {
        long[] expectedProjectIds = TestDataFactory.getActiveProjectIds();
        for (int i = 0; i < this.testedInstances.length; i++) {
            long[] projectIds = this.testedInstances[i].getProjectIds();
            Assert.assertTrue("The incorrect array of project IDs is returned",
                              Arrays.equals(expectedProjectIds, projectIds));
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link ActiveAutoPilotSource#buildFilter()} method for proper behavior.</p>
     *
     * <p>Verifies that the method returns the correct filter to be used for searching the active projects.</p>
     */
    public void testBuildFilter() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            assertFilter(this.testedInstances[i].buildFilter(), this.testedInstances[i]);
        }
    }

    /**
     * <p>Accuracy test. Tests the {@link ActiveAutoPilotSource#processProject(Project[])} method for proper
     * behavior.</p>
     *
     * <p>Verifies that the method returns the correct array of project IDs being provided with the list of active
     * projects.</p>
     */
    public void testProcessProject_ProjectArray() {
        long[] expectedProjectIds = TestDataFactory.getActiveProjectIds();
        for (int i = 0; i < this.testedInstances.length; i++) {
            long[] projectIds = this.testedInstances[i].processProject(TestDataFactory.getActiveProjects());
            Assert.assertTrue("The incorrect array of project IDs is returned",
                              Arrays.equals(expectedProjectIds, projectIds));
        }
    }

    /**
     * <p>Verifies that the specified filter has been produced by the specified instance correctly.</p>
     *
     * @param filter a <code>Filter</code> built by the specified instance.
     * @param testedInstance a <code>ActiveAutoPilotSource</code> instance which is currently tested.
     */
    private void assertFilter(Filter filter, ActiveAutoPilotSource testedInstance) {
        EqualToFilter equalFilter;
        AndFilter andFilter;
        List filters;

        Assert.assertTrue("Non AndFilter is returned", filter instanceof AndFilter);
        andFilter = (AndFilter) filter;
        filters = andFilter.getFilters();
        Assert.assertEquals("The filter must be built of two sub-filters", 2, filters.size());
        Assert.assertTrue("The filter must be of EqualToFilter type",
                          EqualToFilter.class.isAssignableFrom(filters.get(0).getClass()));
        Assert.assertTrue("The filter must be of AndFilter type",
                          AndFilter.class.isAssignableFrom(filters.get(1).getClass()));
        equalFilter = (EqualToFilter) filters.get(0);
        Assert.assertEquals("The filter does not provide correct active project status name",
                            testedInstance.getActiveStatusName(), equalFilter.getValue());
        andFilter = (AndFilter) filters.get(1);
        filters = andFilter.getFilters();
        Assert.assertEquals("The filter must be built of two sub-filters", 2, filters.size());
        Assert.assertTrue("The filter must be of EqualToFilter type",
                          EqualToFilter.class.isAssignableFrom(filters.get(0).getClass()));
        Assert.assertTrue("The filter must be of EqualToFilter type",
                          EqualToFilter.class.isAssignableFrom(filters.get(1).getClass()));
        equalFilter = (EqualToFilter) filters.get(0);
        Assert.assertEquals("The filter does not provide correct auto pilot property name",
                            testedInstance.getExtPropAutoPilotSwitch(), equalFilter.getValue());
        equalFilter = (EqualToFilter) filters.get(1);
        Assert.assertEquals("The filter does not provide correct auto pilot property value",
                            testedInstance.getExtPropAutoPilotSwitchValue(), equalFilter.getValue());
    }
}
