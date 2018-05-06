/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.failuretests;

import com.cronos.onlinereview.ajax.AjaxSupportServlet;
import com.cronos.onlinereview.ajax.failuretests.mock.MockServletConfig;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 * <p>A failure test for {@link AjaxSupportServlet} class. Tests the proper handling of invalid input data by the
 * methods. Passes the invalid arguments to the methods and expects the appropriate exception to be thrown.</p>
 *
 * @author isv
 * @version 1.0
 */
public class AjaxSupportServletFailureTest extends AbstractTestCase {

    /**
     * <p>The instances of {@link AjaxSupportServlet} which are tested. These instances are initialized in {@link
     * #setUp()} method and released in {@link #tearDown()} method. Each instance is initialized using a separate
     * constructor provided by the tested class.<p>
     */
    private AjaxSupportServlet[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link AjaxSupportServlet} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link AjaxSupportServlet} class.
     */
    public static Test suite() {
        return new TestSuite(AjaxSupportServletFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testedInstances = new AjaxSupportServlet[1];
        this.testedInstances[0] = new AjaxSupportServlet();
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
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} method for proper handling the invalid
     * input arguments.</p>
     *
     * <p>Passes {@link null} as <code>config</code> and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_config_null() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].init(null);
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("ServletException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the required
     * configuration namespace is not loaded.</p>
     *
     * <p>Unloads the <code>com.cronos.onlinereview.ajax</code> namespace from Configuration Manager and expects the
     * <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_AjaxNamespaceMissing() {
        ConfigHelper.releaseNamespace("com.cronos.onlinereview.ajax");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the required
     * configuration namespace is not loaded.</p>
     *
     * <p>Unloads the <code>com.cronos.onlinereview.ajax.factory</code> namespace from Configuration Manager and expects
     * the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_FactoryNamespaceMissing() {
        ConfigHelper.releaseNamespace("com.cronos.onlinereview.ajax.factory");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Removes the <code>UserIdAttributeName</code> configuration property from the configuration namespace and
     * expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_UserIdAttributeNameMissing() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.ajax", "UserIdAttributeName");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax", "UserIdAttributeName", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>UserIdAttributeName</code> configuration property from the configuration namespace to empty
     * value and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_UserIdAttributeNameEmpty() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax", "UserIdAttributeName", "");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax", "UserIdAttributeName", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>Handlers</code> configuration property from the configuration namespace to invalid value and
     * expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_UnknownHandler() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax", "Handlers", "UnknownHandler");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[0].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax", "Handlers", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Removes the <code>com/topcoder/management/resource/ResourceManager</code> configuration property from the
     * configuration namespace and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_MissingResourceManagerSpec() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.ajax.factory",
                                                      "com/topcoder/management/resource/ResourceManager.type");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/management/resource/ResourceManager.type", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/resource/ResourceManager</code> configuration property from the
     * configuration namespace to empty value and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_EmptyResourceManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/resource/ResourceManager.type", "");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/management/resource/ResourceManager.type", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/resource/ResourceManager</code> configuration property from the
     * configuration namespace to invalid value and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_InvalidResourceManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/resource/ResourceManager.type",
                                                   "java.lang.String");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[0].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/management/resource/ResourceManager.type", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Removes the <code>com/topcoder/project/phases/template/PhaseTemplate</code> configuration property from the
     * configuration namespace and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_MissingPhaseTemplateSpec() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.ajax.factory",
                                                      "com/topcoder/project/phases/template/PhaseTemplate.type");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/project/phases/template/PhaseTemplate.type", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/project/phases/template/PhaseTemplate</code> configuration property from the
     * configuration namespace to empty value and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_EmptyPhaseTemplateSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/project/phases/template/PhaseTemplate.type", "");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/project/phases/template/PhaseTemplate.type", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/project/phases/template/PhaseTemplate</code> configuration property from the
     * configuration namespace to invalid value and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_InvalidPhaseTemplateSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/project/phases/template/PhaseTemplate.type",
                                                   "java.lang.String");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[0].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("XXX" + MockServletConfig.getMethodArguments() + "YYY");
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/project/phases/template/PhaseTemplate.type", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Removes the <code>com/topcoder/management/deliverable/UploadManager</code> configuration property from the
     * configuration namespace and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_MissingUploadManagerSpec() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.ajax.factory",
                                                      "com/topcoder/management/deliverable/UploadManager.type");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/management/deliverable/UploadManager.type", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/deliverable/UploadManager</code> configuration property from the
     * configuration namespace to empty value and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_EmptyUploadManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/deliverable/UploadManager.type", "");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/management/deliverable/UploadManager.type", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/deliverable/UploadManager</code> configuration property from the
     * configuration namespace to invalid value and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_InvalidUploadManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/deliverable/UploadManager.type",
                                                   "java.lang.String");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[0].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/management/deliverable/UploadManager.type", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Removes the <code>com/topcoder/management/review/scorecalculator/CalculationManager</code> configuration
     * property from the configuration namespace and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_MissingCalcManagerSpec() {
        String[] values
            = ConfigHelper.removeProperty("com.cronos.onlinereview.ajax.factory",
                                          "com/topcoder/management/review/scorecalculator/CalculationManager.type");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/management/review/scorecalculator/CalculationManager.type",
                                             values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/review/scorecalculator/CalculationManager</code> configuration property
     * from the configuration namespace to empty value and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_EmptyCalcManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/review/scorecalculator/CalculationManager.type",
                                                   "");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/management/review/scorecalculator/CalculationManager.type",
                                             values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/review/scorecalculator/CalculationManager</code> configuration property
     * from the configuration namespace to invalid value and expects the <code>ServletException</code> to be
     * thrown.</p>
     */
    public void testInit_ServletConfig_InvalidCalcManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/review/scorecalculator/CalculationManager.type",
                                                   "java.lang.String");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[0].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/management/review/scorecalculator/CalculationManager.type",
                                             values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Removes the <code>com/topcoder/management/scorecard/ScorecardManager</code> configuration property from the
     * configuration namespace and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_MissingScorecardManagerSpec() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.ajax.factory",
                                                      "com/topcoder/management/scorecard/ScorecardManager.type");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/management/scorecard/ScorecardManager.type", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/scorecard/ScorecardManager</code> configuration property from the
     * configuration namespace to empty value and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_EmptyScorecardManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/scorecard/ScorecardManager.type", "");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/management/scorecard/ScorecardManager.type", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/scorecard/ScorecardManager</code> configuration property from the
     * configuration namespace to invalid value and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_InvalidScorecardManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/scorecard/ScorecardManager.type",
                                                   "java.lang.String");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[0].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/management/scorecard/ScorecardManager.type", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Removes the <code>com/topcoder/management/review/ReviewManager</code> configuration property from the
     * configuration namespace and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_MissingReviewManagerSpec() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.ajax.factory",
                                                      "com/topcoder/management/review/ReviewManager.type");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/management/review/ReviewManager.type", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/review/ReviewManager</code> configuration property from the
     * configuration namespace to empty value and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_EmptyReviewManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/review/ReviewManager.type", "");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/management/review/ReviewManager.type", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/review/ReviewManager</code> configuration property from the
     * configuration namespace to invalid value and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_InvalidReviewManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/review/ReviewManager.type",
                                                   "java.lang.String");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[0].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/management/review/ReviewManager.type", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Removes the <code>com/topcoder/management/phase/PhaseManager</code> configuration property from the
     * configuration namespace and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_MissingPhaseManagerSpec() {
        String[] values = ConfigHelper.removeProperty("com.cronos.onlinereview.ajax.factory",
                                                      "com/topcoder/management/phase/PhaseManager.type");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/management/phase/PhaseManager.type", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/phase/PhaseManager</code> configuration property from the configuration
     * namespace to empty value and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_EmptyPhaseManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/phase/PhaseManager.type", "");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/management/phase/PhaseManager.type", values);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AjaxSupportServlet#init(ServletConfig)} for proper behavior if the
     * configuration is invalid.</p>
     *
     * <p>Sets the <code>com/topcoder/management/phase/PhaseManager</code> configuration property from the configuration
     * namespace to invalid value and expects the <code>ServletException</code> to be thrown.</p>
     */
    public void testInit_ServletConfig_InvalidPhaseManagerSpec() {
        String[] values = ConfigHelper.setProperty("com.cronos.onlinereview.ajax.factory",
                                                   "com/topcoder/management/phase/PhaseManager.type",
                                                   "java.lang.String");
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[0].init(TestDataFactory.getServletConfig());
                Assert.fail("ServletException should have been thrown");
            } catch (ServletException e) {
                // expected behavior
            } catch (Exception e) {
                fail("ServletException was expected but the original exception is : " + e);
            } finally {
                ConfigHelper.restoreProperty("com.cronos.onlinereview.ajax.factory",
                                             "com/topcoder/management/phase/PhaseManager.type", values);
            }
        }
    }
}
