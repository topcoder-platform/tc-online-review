/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.failuretests;

import com.cronos.onlinereview.phases.ConfigurationException;
import com.cronos.onlinereview.phases.ManagerHelper;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>A failure test for {@link ManagerHelper} class. Tests the proper handling of invalid input data by the methods.
 * Passes the invalid arguments to the methods and expects the appropriate exception to be thrown.</p>
 *
 * @author isv
 * @version 1.0
 */
public class ManagerHelperFailureTest extends AbstractTestCase {

    /**
     * <p>The instances of {@link ManagerHelper} which are tested. These instances are initialized in {@link #setUp()}
     * method and released in {@link #tearDown()} method. Each instance is initialized using a separate constructor
     * provided by the tested class.<p>
     */
    private ManagerHelper[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link ManagerHelper} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link ManagerHelper} class.
     */
    public static Test suite() {
        return new TestSuite(ManagerHelperFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testedInstances = new ManagerHelper[2];
        this.testedInstances[0] = new ManagerHelper();
        this.testedInstances[1] = new ManagerHelper(TestDataFactory.MANAGER_HELPER_NAMESPACE);
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
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper(String)} constructor for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>namespace</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testConstructor_String_namespace_null() {
        try {
            new ManagerHelper(null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper(String)} constructor for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>namespace</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_namespace_ZERO_LENGTH_STRING() {
        try {
            new ManagerHelper(TestDataFactory.ZERO_LENGTH_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper(String)} constructor for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>namespace</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_namespace_WHITESPACE_ONLY_STRING() {
        try {
            new ManagerHelper(TestDataFactory.WHITESPACE_ONLY_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Removes the <code>ProjectManager.ClassName</code> configuration property from the configuration namespace and
     * expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_1() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                      "ProjectManager.ClassName");
        try {
            new ManagerHelper();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "ProjectManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Sets the <code>ProjectManager.ClassName</code> configuration property from the configuration namespace to
     * empty value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_2() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                   "ProjectManager.ClassName", "");
        try {
            new ManagerHelper();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "ProjectManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Sets the <code>ProjectManager.ClassName</code> configuration property from the configuration namespace to
     * invalid value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_3() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                   "ProjectManager.ClassName", "java.lang.String");
        try {
            new ManagerHelper();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "ProjectManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Removes the <code>PhaseManager.ClassName</code> configuration property from the configuration namespace and
     * expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_4() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                      "PhaseManager.ClassName");
        try {
            new ManagerHelper();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "PhaseManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Sets the <code>PhaseManager.ClassName</code> configuration property from the configuration namespace to empty
     * value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_5() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                   "PhaseManager.ClassName", "");
        try {
            new ManagerHelper();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "PhaseManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Sets the <code>PhaseManager.ClassName</code> configuration property from the configuration namespace to
     * invalid value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_6() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                   "PhaseManager.ClassName", "java.lang.String");
        try {
            new ManagerHelper();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "PhaseManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Removes the <code>ReviewManager.ClassName</code> configuration property from the configuration namespace and
     * expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_7() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                      "ReviewManager.ClassName");
        try {
            new ManagerHelper();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "ReviewManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Sets the <code>ReviewManager.ClassName</code> configuration property from the configuration namespace to empty
     * value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_8() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                   "ReviewManager.ClassName", "");
        try {
            new ManagerHelper();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "ReviewManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Sets the <code>ReviewManager.ClassName</code> configuration property from the configuration namespace to
     * invalid value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_9() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                   "ReviewManager.ClassName", "java.lang.String");
        try {
            new ManagerHelper();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "ReviewManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Removes the <code>ScorecardManager.ClassName</code> configuration property from the configuration namespace
     * and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_10() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                      "ScorecardManager.ClassName");
        try {
            new ManagerHelper();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "ScorecardManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Sets the <code>ScorecardManager.ClassName</code> configuration property from the configuration namespace to
     * empty value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_11() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                   "ScorecardManager.ClassName", "");
        try {
            new ManagerHelper();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "ScorecardManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Sets the <code>ScorecardManager.ClassName</code> configuration property from the configuration namespace to
     * invalid value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_12() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                   "ScorecardManager.ClassName", "java.lang.String");
        try {
            new ManagerHelper();
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "ScorecardManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper,String} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Removes the <code>ProjectManager.ClassName</code> configuration property from the configuration namespace and
     * expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_String_1() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                      "ProjectManager.ClassName");
        try {
            new ManagerHelper(TestDataFactory.MANAGER_HELPER_NAMESPACE);
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "ProjectManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper,String} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Sets the <code>ProjectManager.ClassName</code> configuration property from the configuration namespace to
     * empty value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_String_2() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                   "ProjectManager.ClassName", "");
        try {
            new ManagerHelper(TestDataFactory.MANAGER_HELPER_NAMESPACE);
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "ProjectManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper,String} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Sets the <code>ProjectManager.ClassName</code> configuration property from the configuration namespace to
     * invalid value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_String_3() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                   "ProjectManager.ClassName", "java.lang.String");
        try {
            new ManagerHelper(TestDataFactory.MANAGER_HELPER_NAMESPACE);
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "ProjectManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper,String} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Removes the <code>PhaseManager.ClassName</code> configuration property from the configuration namespace and
     * expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_String_4() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                      "PhaseManager.ClassName");
        try {
            new ManagerHelper(TestDataFactory.MANAGER_HELPER_NAMESPACE);
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "PhaseManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper,String} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Sets the <code>PhaseManager.ClassName</code> configuration property from the configuration namespace to empty
     * value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_String_5() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                   "PhaseManager.ClassName", "");
        try {
            new ManagerHelper(TestDataFactory.MANAGER_HELPER_NAMESPACE);
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "PhaseManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper,String} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Sets the <code>PhaseManager.ClassName</code> configuration property from the configuration namespace to
     * invalid value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_String_6() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                   "PhaseManager.ClassName", "java.lang.String");
        try {
            new ManagerHelper(TestDataFactory.MANAGER_HELPER_NAMESPACE);
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "PhaseManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper,String} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Removes the <code>ReviewManager.ClassName</code> configuration property from the configuration namespace and
     * expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_String_7() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                      "ReviewManager.ClassName");
        try {
            new ManagerHelper(TestDataFactory.MANAGER_HELPER_NAMESPACE);
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "ReviewManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper,String} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Sets the <code>ReviewManager.ClassName</code> configuration property from the configuration namespace to empty
     * value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_String_8() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                   "ReviewManager.ClassName", "");
        try {
            new ManagerHelper(TestDataFactory.MANAGER_HELPER_NAMESPACE);
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "ReviewManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper,String} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Sets the <code>ReviewManager.ClassName</code> configuration property from the configuration namespace to
     * invalid value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_String_9() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                   "ReviewManager.ClassName", "java.lang.String");
        try {
            new ManagerHelper(TestDataFactory.MANAGER_HELPER_NAMESPACE);
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "ReviewManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper,String} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Removes the <code>ScorecardManager.ClassName</code> configuration property from the configuration namespace
     * and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_String_10() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                      "ScorecardManager.ClassName");
        try {
            new ManagerHelper(TestDataFactory.MANAGER_HELPER_NAMESPACE);
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "ScorecardManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper,String} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Sets the <code>ScorecardManager.ClassName</code> configuration property from the configuration namespace to
     * empty value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_String_11() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                   "ScorecardManager.ClassName", "");
        try {
            new ManagerHelper(TestDataFactory.MANAGER_HELPER_NAMESPACE);
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "ScorecardManager.ClassName", values);
        }
    }

    /**
     * <p>Failure test. Tests the {@link ManagerHelper#ManagerHelper,String} for proper behavior if the configuration is
     * invalid.</p>
     *
     * <p>Sets the <code>ScorecardManager.ClassName</code> configuration property from the configuration namespace to
     * invalid value and expects the <code>ConfigurationException</code> to be thrown.</p>
     */
    public void testManagerHelper_String_12() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                                   "ScorecardManager.ClassName", "java.lang.String");
        try {
            new ManagerHelper(TestDataFactory.MANAGER_HELPER_NAMESPACE);
            Assert.fail("ConfigurationException should have been thrown");
        } catch (ConfigurationException e) {
            // expected behavior
        } catch (Exception e) {
            fail("ConfigurationException was expected but the original exception is : " + e);
        } finally {
            ConfigHelper.restoreProperty("com.cronos.onlinereview.phases.ManagerHelper",
                                         "ScorecardManager.ClassName", values);
        }
    }
}
