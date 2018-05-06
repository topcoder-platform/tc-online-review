/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.failuretests.handlers;

import com.cronos.onlinereview.ajax.ConfigurationException;
import com.cronos.onlinereview.ajax.failuretests.ConfigHelper;
import com.cronos.onlinereview.ajax.failuretests.AbstractTestCase;
import com.cronos.onlinereview.ajax.handlers.ReviewCommonHandler;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>A failure test for {@link ReviewCommonHandler} class. Tests the proper handling of invalid input data by the
 * methods. Passes the invalid arguments to the methods and expects the appropriate exception to be thrown.</p>
 *
 * @author isv
 * @version 1.0
 */
public class ReviewCommonHandlerFailureTest extends AbstractTestCase {

    /**
     * <p>Gets the test suite for {@link ReviewCommonHandler} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link ReviewCommonHandler} class.
     */
    public static Test suite() {
        return new TestSuite(ReviewCommonHandlerFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    /**
     * <p>Failure test. Tests the {@link ReviewCommonHandler#ReviewCommonHandler} for proper behavior if the required
     * configuration namespace is not loaded.</p>
     *
     * <p>Unloads the <code>com.cronos.onlinereview.ajax.factory</code> namespace from Configuration Manager and expects
     * the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testReviewCommonHandler_FactoryNamespaceMissing() {
        ConfigHelper.releaseNamespace("com.cronos.onlinereview.ajax.factory");
        try {
            new ReviewCommonHandlerSubclass();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ReviewCommonHandler#ReviewCommonHandler} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Removes the <code>com/topcoder/management/review/ReviewManager</code> configuration property from the
     * configuration namespace and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testReviewCommonHandler_MissingReviewManagerSpec() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.ajax.factory",
                                                      "com/topcoder/management/review/ReviewManager.type");
        try {
            new ReviewCommonHandlerSubclass();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                         "com/topcoder/management/review/ReviewManager.type", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ReviewCommonHandler#ReviewCommonHandler} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/review/ReviewManager</code> configuration property from the
     * configuration namespace to empty value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testReviewCommonHandler_EmptyReviewManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/review/ReviewManager.type", "");
        try {
            new ReviewCommonHandlerSubclass();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                         "com/topcoder/management/review/ReviewManager.type", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ReviewCommonHandler#ReviewCommonHandler} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/review/ReviewManager</code> configuration property from the
     * configuration namespace to invalid value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testReviewCommonHandler_InvalidReviewManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/review/ReviewManager.type",
                                                   "java.lang.String");
        try {
            new ReviewCommonHandlerSubclass();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                         "com/topcoder/management/review/ReviewManager.type", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ReviewCommonHandler#ReviewCommonHandler} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Removes the <code>com/topcoder/management/phase/PhaseManager</code> configuration property from the
     * configuration namespace and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testReviewCommonHandler_MissingPhaseManagerSpec() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.ajax.factory",
                                                      "com/topcoder/management/phase/PhaseManager.type");
        try {
            new ReviewCommonHandlerSubclass();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                         "com/topcoder/management/phase/PhaseManager.type", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ReviewCommonHandler#ReviewCommonHandler} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/phase/PhaseManager</code> configuration property from the configuration
     * namespace to empty value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testReviewCommonHandler_EmptyPhaseManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/phase/PhaseManager.type", "");
        try {
            new ReviewCommonHandlerSubclass();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                         "com/topcoder/management/phase/PhaseManager.type", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ReviewCommonHandler#ReviewCommonHandler} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/phase/PhaseManager</code> configuration property from the configuration
     * namespace to invalid value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testReviewCommonHandler_InvalidPhaseManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/phase/PhaseManager.type",
                                                   "java.lang.String");
        try {
            new ReviewCommonHandlerSubclass();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                         "com/topcoder/management/phase/PhaseManager.type", values);
        }
    }
}
