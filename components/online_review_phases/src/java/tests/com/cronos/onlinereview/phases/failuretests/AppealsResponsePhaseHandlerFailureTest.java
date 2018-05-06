/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.failuretests;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.cronos.onlinereview.phases.AppealsResponsePhaseHandler;
import com.cronos.onlinereview.phases.PhaseNotSupportedException;
import com.cronos.onlinereview.phases.failuretests.mock.MockProjectManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockResourceManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockReviewManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockScorecardManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockUploadManager;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>A failure test for {@link AppealsResponsePhaseHandler} class. Tests the proper
 * handling of invalid input data by the methods. Passes the invalid arguments to the methods and expects the
 * appropriate exception to be thrown.</p>
 *
 * @author isv
 * @version 1.0
 */
public class AppealsResponsePhaseHandlerFailureTest extends AbstractTestCase {

    /**
     * <p>The instances of {@link AppealsResponsePhaseHandler} which are tested. These instances are initialized in
     * {@link #setUp()} method and released in {@link #tearDown()} method. Each instance is initialized using a separate
     * constructor provided by the tested class.<p>
     */
    private AppealsResponsePhaseHandler[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link AppealsResponsePhaseHandler} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link AppealsResponsePhaseHandler} class.
     */
    public static Test suite() {
        return new TestSuite(AppealsResponsePhaseHandlerFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testedInstances = new AppealsResponsePhaseHandler[2];
        this.testedInstances[0] = new AppealsResponsePhaseHandler();
        this.testedInstances[1] = new AppealsResponsePhaseHandler(TestDataFactory.NAMESPACE);
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
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#AppealsResponsePhaseHandler(String)} constructor
     * for proper handling the invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>namespace</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testConstructor_String_namespace_null() {
        try {
            new AppealsResponsePhaseHandler(null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#AppealsResponsePhaseHandler(String)} constructor
     * for proper handling the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>namespace</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_namespace_ZERO_LENGTH_STRING() {
        try {
            new AppealsResponsePhaseHandler(TestDataFactory.ZERO_LENGTH_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#AppealsResponsePhaseHandler(String)} constructor
     * for proper handling the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>namespace</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_namespace_WHITESPACE_ONLY_STRING() {
        try {
            new AppealsResponsePhaseHandler(TestDataFactory.WHITESPACE_ONLY_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#canPerform(Phase)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>phase</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testCanPerform_Phase_phase_null() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(null);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#canPerform(Phase)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getSubmissionPhase()} as <code>phase</code> and expects the
     * <code>PhaseNotSupportedException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_phase_SubmissionPhase() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getSubmissionPhase());
                Assert.fail("PhaseNotSupportedException should have been thrown");
            } catch (PhaseNotSupportedException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("PhaseNotSupportedException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#canPerform(Phase)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getClosedAppealsResponsePhase()} as <code>phase</code> and expects the
     * <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_phase_ClosedAppealsResponsePhase() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getClosedAppealsResponsePhase());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#perform(Phase,String)} method for proper handling
     * the invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>phase</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testPerform_Phase_String_phase_null() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(null, TestDataFactory.OPERATOR);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#perform(Phase,String)} method for proper handling
     * the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getSubmissionPhase()} as <code>phase</code> and expects the
     * <code>PhaseNotSupportedException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_phase_SubmissionPhase() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getSubmissionPhase(), TestDataFactory.OPERATOR);
                Assert.fail("PhaseNotSupportedException should have been thrown");
            } catch (PhaseNotSupportedException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("PhaseNotSupportedException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#perform(Phase,String)} method for proper handling
     * the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getClosedAppealsResponsePhase()} as <code>phase</code> and expects the
     * <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_phase_ClosedAppealsResponsePhase() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getClosedAppealsResponsePhase(),
                                                TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#perform(Phase,String)} method for proper handling
     * the invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>operator</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testPerform_Phase_String_operator_null() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getAppealsResponsePhase(), null);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#perform(Phase,String)} method for proper handling
     * the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>operator</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_operator_ZERO_LENGTH_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getAppealsResponsePhase(),
                                                TestDataFactory.ZERO_LENGTH_STRING);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#perform(Phase,String)} method for proper handling
     * the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>operator</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_operator_WHITESPACE_ONLY_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getAppealsResponsePhase(),
                                                TestDataFactory.WHITESPACE_ONLY_STRING);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#canPerform,Phase} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_ResourceManagerError_SearchBuilderConfigurationException() {
        MockResourceManager.throwGlobalException(new SearchBuilderConfigurationException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getClosedAppealsResponsePhase());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#canPerform,Phase} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_ResourceManagerError_ResourcePersistenceException() {
        MockResourceManager.throwGlobalException(new ResourcePersistenceException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getClosedAppealsResponsePhase());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#canPerform,Phase} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_ResourceManagerError_SearchBuilderException() {
        MockResourceManager.throwGlobalException(new SearchBuilderException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getClosedAppealsResponsePhase());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#canPerform,Phase} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockReviewManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_ReviewManagerError_ReviewManagementException() {
        MockReviewManager.throwGlobalException(new ReviewManagementException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getClosedAppealsResponsePhase());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockUploadManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_OpenPhase_UploadManagerError_UploadPersistenceException() {
        MockUploadManager.throwGlobalException(new UploadPersistenceException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getOpenAppealsResponsePhase(),
                                                TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockUploadManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_OpenPhase_UploadManagerError_SearchBuilderException() {
        MockUploadManager.throwGlobalException(new SearchBuilderException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getOpenAppealsResponsePhase(),
                                                TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_OpenPhase_ResourceManagerError_SearchBuilderConfigurationException() {
        MockResourceManager.throwGlobalException(new SearchBuilderConfigurationException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getOpenAppealsResponsePhase(),
                                                TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_OpenPhase_ResourceManagerError_ResourcePersistenceException() {
        MockResourceManager.throwGlobalException(new ResourcePersistenceException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getOpenAppealsResponsePhase(),
                                                TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_OpenPhase_ResourceManagerError_SearchBuilderException() {
        MockResourceManager.throwGlobalException(new SearchBuilderException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getOpenAppealsResponsePhase(),
                                                TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockReviewManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_OpenPhase_ReviewManagerError_ReviewManagementException() {
        MockReviewManager.throwGlobalException(new ReviewManagementException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getOpenAppealsResponsePhase(),
                                                TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockScorecardManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_OpenPhase_ScorecardManagerError_PersistenceException() {
        MockScorecardManager.throwGlobalException(new PersistenceException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getOpenAppealsResponsePhase(),
                                                TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockProjectManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_OpenPhase_ProjectManagerError_PersistenceException() {
        MockProjectManager.throwGlobalException(new PersistenceException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getOpenAppealsResponsePhase(),
                                                TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     * <p>Configures the config manager instance not to have post mortem reviewer number.
     * @throws Exception to JUnit
     */
    public void testPerform_Phase_String_OpenPhase_ReviewerNumberNotSet() throws Exception {
        // remove the old database and use the incorrect one
        ConfigManager cm = ConfigManager.getInstance();
        try {
            cm.removeNamespace("com.cronos.onlinereview.phases.PostMortemPhaseHandler");
        } catch (Exception e) {
            // ignore
        }
        cm.add("failure/Post_Mortem_1.xml");

        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getOpenAppealsResponsePhase(),
                                                TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     * <p>Configures the config manager instance to have invalid post mortem reviewer number.
     * @throws Exception to JUnit
     */
    public void testPerform_Phase_String_OpenPhase_ReviewerNumberInvalid() throws Exception {
        // remove the old database and use the incorrect one
        ConfigManager cm = ConfigManager.getInstance();
        try {
            cm.removeNamespace("com.cronos.onlinereview.phases.PostMortemPhaseHandler");
        } catch (Exception e) {
            // ignore
        }
        cm.add("failure/Post_Mortem_2.xml");

        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getOpenAppealsResponsePhase(),
                                                TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     * <p>Configures the config manager instance not to have post-mortem duration.
     * @throws Exception to JUnit
     */
    public void testPerform_Phase_String_OpenPhase_WrongPostMortemDuration() throws Exception {
        // remove the old database and use the incorrect one
        ConfigManager cm = ConfigManager.getInstance();
        try {
            cm.removeNamespace("com.cronos.onlinereview.phases.PostMortemPhaseHandler");
        } catch (Exception e) {
            // ignore
        }
        cm.add("failure/Post_Mortem_3.xml");

        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getOpenAppealsResponsePhase(),
                                                TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsResponsePhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     * <p>Configures the config manager instance not to have linked project id.
     * @throws Exception to JUnit
     */
    public void testPerform_Phase_String_OpenPhase_WrongLinkedId() throws Exception {
        // remove the old database and use the incorrect one
        ConfigManager cm = ConfigManager.getInstance();
        try {
            cm.removeNamespace("com.cronos.onlinereview.phases.PostMortemPhaseHandler");
        } catch (Exception e) {
            // ignore
        }
        cm.add("failure/Post_Mortem_4.xml");

        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getOpenAppealsResponsePhase(),
                                                TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }
}
