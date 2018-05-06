/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.failuretests.handlers;

import java.util.Iterator;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.ConfigurationException;
import com.cronos.onlinereview.ajax.failuretests.ConfigHelper;
import com.cronos.onlinereview.ajax.failuretests.TestDataFactory;
import com.cronos.onlinereview.ajax.failuretests.AbstractTestCase;
import com.cronos.onlinereview.ajax.failuretests.mock.MockPhaseManager;
import com.cronos.onlinereview.ajax.failuretests.mock.MockReviewManager;
import com.cronos.onlinereview.ajax.handlers.ResolveAppealHandler;
import com.topcoder.util.config.ConfigManager;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>A failure test for {@link ResolveAppealHandler} class. Tests the proper handling of invalid input data by the
 * methods. Passes the invalid arguments to the methods and expects the appropriate exception to be thrown.</p>
 *
 * @author isv
 * @version 1.0
 */
public class ResolveAppealHandlerFailureTest extends AbstractTestCase {

    /**
     * <p>The instances of {@link ResolveAppealHandler} which are tested. These instances are initialized in {@link
     * #setUp()} method and released in {@link #tearDown()} method. Each instance is initialized using a separate
     * constructor provided by the tested class.<p>
     */
    private ResolveAppealHandler[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link ResolveAppealHandler} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link ResolveAppealHandler} class.
     */
    public static Test suite() {
        return new TestSuite(ResolveAppealHandlerFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.tearDown();
        ConfigManager cm = ConfigManager.getInstance();
        cm.add("default.xml");
        cm.add("objectfactory.xml");
        cm.add("scorecalculator.xml");

        this.testedInstances = new ResolveAppealHandler[1];
        this.testedInstances[0] = new ResolveAppealHandler();
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        this.testedInstances = null;
        ConfigManager cm = ConfigManager.getInstance();
        Iterator it = cm.getAllNamespaces();
        while (it.hasNext()) {
            cm.removeNamespace(it.next().toString());
        }
        super.tearDown();
    }

    /**
     * <p>Failure test. Tests the {@link ResolveAppealHandler#service(AjaxRequest,Long)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>request</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testService_AjaxRequest_Long_request_null() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].service(null, new Long(1));
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link ResolveAppealHandler#ResolveAppealHandler} for proper behavior if the required
     * configuration namespace is not loaded.</p>
     *
     * <p>Unloads the <code>com.cronos.onlinereview.ajax.factory</code> namespace from Configuration Manager and expects
     * the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testResolveAppealHandler_FactoryNamespaceMissing() {
        ConfigHelper.releaseNamespace("com.cronos.onlinereview.ajax.factory");
        try {
            new ResolveAppealHandler();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ResolveAppealHandler#ResolveAppealHandler} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Removes the <code>com/topcoder/management/review/scorecalculator/CalculationManager</code> configuration
     * property from the configuration namespace and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testResolveAppealHandler_MissingCalcManagerSpec() {
        String[] values
            = ConfigHelper.removeProperty("com.cronos.onlinereview.ajax.factory",
                                          "com/topcoder/management/review/scorecalculator/CalculationManager.type");
        try {
            new ResolveAppealHandler();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                         "com/topcoder/management/review/scorecalculator/CalculationManager.type",
                                         values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ResolveAppealHandler#ResolveAppealHandler} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/review/scorecalculator/CalculationManager</code> configuration property
     * from the configuration namespace to empty value and expects the <code>ConfigurationException</code> to be
     * thrown.</p>
     */
    public void testResolveAppealHandler_EmptyCalcManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/review/scorecalculator/CalculationManager.type",
                                                   "");
        try {
            new ResolveAppealHandler();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                         "com/topcoder/management/review/scorecalculator/CalculationManager.type",
                                         values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ResolveAppealHandler#ResolveAppealHandler} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/review/scorecalculator/CalculationManager</code> configuration property
     * from the configuration namespace to invalid value and expects the <code>ConfigurationException</code> to be
     * thrown.</p>
     */
    public void testResolveAppealHandler_InvalidCalcManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/review/scorecalculator/CalculationManager.type",
                                                   "java.lang.String");
        try {
            new ResolveAppealHandler();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                         "com/topcoder/management/review/scorecalculator/CalculationManager.type",
                                         values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ResolveAppealHandler#ResolveAppealHandler} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Removes the <code>com/topcoder/management/scorecard/ScorecardManager</code> configuration property from the
     * configuration namespace and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testResolveAppealHandler_MissingScorecardManagerSpec() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.ajax.factory",
                                                      "com/topcoder/management/scorecard/ScorecardManager.type");
        try {
            new ResolveAppealHandler();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                         "com/topcoder/management/scorecard/ScorecardManager.type", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ResolveAppealHandler#ResolveAppealHandler} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/scorecard/ScorecardManager</code> configuration property from the
     * configuration namespace to empty value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testResolveAppealHandler_EmptyScorecardManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/scorecard/ScorecardManager.type", "");
        try {
            new ResolveAppealHandler();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                         "com/topcoder/management/scorecard/ScorecardManager.type", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ResolveAppealHandler#ResolveAppealHandler} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/scorecard/ScorecardManager</code> configuration property from the
     * configuration namespace to invalid value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testResolveAppealHandler_InvalidScorecardManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/scorecard/ScorecardManager.type",
                                                   "java.lang.String");
        try {
            new ResolveAppealHandler();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                         "com/topcoder/management/scorecard/ScorecardManager.type", values);
        }
    }

}
