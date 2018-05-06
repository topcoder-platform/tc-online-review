/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.failuretests.handlers;

import com.cronos.onlinereview.ajax.ConfigurationException;
import com.cronos.onlinereview.ajax.failuretests.ConfigHelper;
import com.cronos.onlinereview.ajax.failuretests.TestDataFactory;
import com.cronos.onlinereview.ajax.failuretests.AbstractTestCase;
import com.cronos.onlinereview.ajax.failuretests.mock.MockResourceManager;
import com.cronos.onlinereview.ajax.handlers.CommonHandler;
import com.cronos.onlinereview.ajax.handlers.ResourceException;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>A failure test for {@link CommonHandler} class. Tests the proper handling of invalid input data by the methods.
 * Passes the invalid arguments to the methods and expects the appropriate exception to be thrown.</p>
 *
 * @author isv
 * @version 1.0
 */
public class CommonHandlerFailureTest extends AbstractTestCase {

    /**
     * <p>The instances of {@link CommonHandler} which are tested. These instances are initialized in {@link #setUp()}
     * method and released in {@link #tearDown()} method. Each instance is initialized using a separate constructor
     * provided by the tested class.<p>
     */
    private CommonHandlerSubclass[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link CommonHandler} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link CommonHandler} class.
     */
    public static Test suite() {
        return new TestSuite(CommonHandlerFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testedInstances = new CommonHandlerSubclass[0];
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
     * <p>Failure test. Tests the {@link CommonHandler#checkResourceHasRole(long,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>role</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testCheckUserHasRole_long_String_role_null() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].checkUserHasRole(null, null);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link CommonHandler#checkResourceHasRole(long,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>role</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testCheckUserHasRole_long_String_role_ZERO_LENGTH_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].checkUserHasRole(null, TestDataFactory.ZERO_LENGTH_STRING);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link CommonHandler#checkResourceHasRole(long,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>role</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testCheckUserHasRole_long_String_role_WHITESPACE_ONLY_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].checkUserHasRole(null, TestDataFactory.WHITESPACE_ONLY_STRING);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link CommonHandler#CommonHandler} for proper behavior if the required configuration
     * namespace is not loaded.</p>
     *
     * <p>Unloads the <code>com.cronos.onlinereview.ajax.factory</code> namespace from Configuration Manager and expects
     * the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testCommonHandler_FactoryNamespaceMissing() {
        ConfigHelper.releaseNamespace("com.cronos.onlinereview.ajax.factory");
        try {
            new CommonHandlerSubclass();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link CommonHandler#CommonHandler} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Removes the <code>com/topcoder/management/resource/ResourceManager</code> configuration property from the
     * configuration namespace and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testCommonHandler_MissingResourceManagerSpec() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.ajax.factory",
                                                      "com/topcoder/management/resource/ResourceManager.type");
        try {
            new CommonHandlerSubclass();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                         "com/topcoder/management/resource/ResourceManager.type", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link CommonHandler#CommonHandler} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/resource/ResourceManager</code> configuration property from the
     * configuration namespace to empty value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testCommonHandler_EmptyResourceManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/resource/ResourceManager.type", "");
        try {
            new CommonHandlerSubclass();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                         "com/topcoder/management/resource/ResourceManager.type", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link CommonHandler#CommonHandler} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/resource/ResourceManager</code> configuration property from the
     * configuration namespace to invalid value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testCommonHandler_InvalidResourceManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/resource/ResourceManager.type",
                                                   "java.lang.String");
        try {
            new CommonHandlerSubclass();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                         "com/topcoder/management/resource/ResourceManager.type", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link CommonHandler#CommonHandler} for proper behavior if the underlying service
     * throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testCommonHandler_ResourceManagerError() {
        MockResourceManager.throwGlobalException(new IllegalStateException());
        try {
            new CommonHandlerSubclass();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link CommonHandler#CommonHandler} for proper behavior if the underlying service
     * returns incorrect result.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to return an invalid value from method and
     * expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testCommonHandler_NoManagerRole() {
        MockResourceManager.setMethodResult("getAllResourceRoles",
                                            TestDataFactory.getResourceRolesWithoutManagerRole());
        try {
            new CommonHandlerSubclass();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link CommonHandler#checkUserHasRole,long,String} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>RoleResolutionException</code> to be thrown.</p>
     */
    public void testCheckUserHasRole_long_String_ResourceManagerError() {
        MockResourceManager.throwGlobalException(new IllegalStateException());
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].checkUserHasRole(null, TestDataFactory.MANAGER_ROLE);
                Assert.fail("RoleResolutionException should have been thrown");
            } catch (ResourceException e) {
                // expected behavior
            } catch (Exception e) {
                fail("RoleResolutionException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link CommonHandler#checkUserHasGlobalManagerRole,long} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>RoleResolutionException</code> to be thrown.</p>
     */
    public void testCheckUserHasGlobalManagerRole_long_ResourceManagerError() {
        MockResourceManager.throwGlobalException(new IllegalStateException());
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].checkUserHasGlobalManagerRole(1);
                Assert.fail("RoleResolutionException should have been thrown");
            } catch (ResourceException e) {
                // expected behavior
            } catch (Exception e) {
                fail("RoleResolutionException was expected but the original exception is : " + e);
            }
        }
    }
}
