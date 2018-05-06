/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.failuretests.handlers;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.ConfigurationException;
import com.cronos.onlinereview.ajax.failuretests.ConfigHelper;
import com.cronos.onlinereview.ajax.failuretests.TestDataFactory;
import com.cronos.onlinereview.ajax.failuretests.AbstractTestCase;
import com.cronos.onlinereview.ajax.failuretests.mock.MockResourceManager;
import com.cronos.onlinereview.ajax.handlers.SetTimelineNotificationHandler;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>A failure test for {@link SetTimelineNotificationHandler} class. Tests the proper handling of invalid input data
 * by the methods. Passes the invalid arguments to the methods and expects the appropriate exception to be thrown.</p>
 *
 * @author isv
 * @version 1.0
 */
public class SetTimelineNotificationHandlerFailureTest extends AbstractTestCase {

    /**
     * <p>The instances of {@link SetTimelineNotificationHandler} which are tested. These instances are initialized in
     * {@link #setUp()} method and released in {@link #tearDown()} method. Each instance is initialized using a separate
     * constructor provided by the tested class.<p>
     */
    private SetTimelineNotificationHandler[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link SetTimelineNotificationHandler} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link SetTimelineNotificationHandler} class.
     */
    public static Test suite() {
        return new TestSuite(SetTimelineNotificationHandlerFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testedInstances = new SetTimelineNotificationHandler[1];
        this.testedInstances[0] = new SetTimelineNotificationHandler();
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        this.testedInstances = null;
        super.tearDown();
    }

    /**
     * <p>Failure test. Tests the {@link SetTimelineNotificationHandler#service(AjaxRequest,Long)} method for proper
     * handling the invalid input arguments.</p>
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
     * <p>Failure test. Tests the {@link SetTimelineNotificationHandler#SetTimelineNotificationHandler} for proper
     * behavior if the required configuration namespace is not loaded.</p>
     *
     * <p>Unloads the <code>com.cronos.onlinereview.ajax.factory</code> namespace from Configuration Manager and expects
     * the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testSetTimelineNotificationHandler_FactoryNamespaceMissing() {
        ConfigHelper.releaseNamespace("com.cronos.onlinereview.ajax.factory");
        try {
            new SetTimelineNotificationHandler();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link SetTimelineNotificationHandler#SetTimelineNotificationHandler} for proper
     * behavior if the configuration is invalid.</p>
     *
     * <p>Removes the <code>com/topcoder/management/project/ProjectManager</code> configuration property from the
     * configuration namespace and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testSetTimelineNotificationHandler_MissingScorecardManagerSpec() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.ajax.factory",
                                                      "com/topcoder/management/project/ProjectManager.type");
        try {
            new SetTimelineNotificationHandler();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                         "com/topcoder/management/project/ProjectManager.type", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link SetTimelineNotificationHandler#SetTimelineNotificationHandler} for proper
     * behavior if the configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/project/ProjectManager</code> configuration property from the
     * configuration namespace to empty value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testSetTimelineNotificationHandler_EmptyScorecardManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/project/ProjectManager.type", "");
        try {
            new SetTimelineNotificationHandler();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                         "com/topcoder/management/project/ProjectManager.type", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link SetTimelineNotificationHandler#SetTimelineNotificationHandler} for proper
     * behavior if the configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/project/ProjectManager</code> configuration property from the
     * configuration namespace to invalid value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testSetTimelineNotificationHandler_InvalidScorecardManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/project/ProjectManager.type",
                                                   "java.lang.String");
        try {
            new SetTimelineNotificationHandler();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                         "com/topcoder/management/project/ProjectManager.type", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link SetTimelineNotificationHandler#SetTimelineNotificationHandler} for proper
     * behavior if the underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testSetTimelineNotificationHandler_ResourceManagerError() {
        MockResourceManager.throwGlobalException(new IllegalStateException());
        try {
            new SetTimelineNotificationHandler();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link SetTimelineNotificationHandler#SetTimelineNotificationHandler} for proper
     * behavior if the underlying service returns incorrect result.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to return an invalid value from method and
     * expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testSetTimelineNotificationHandler_NoManagerRole() {
        MockResourceManager.setMethodResult("getAllNotificationTypes",
                                            TestDataFactory.getNotificationTypesWithoutTimelineNotification());
        try {
            new SetTimelineNotificationHandler();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }
}
