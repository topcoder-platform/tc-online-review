/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.failuretests;

import com.cronos.onlinereview.autoscreening.management.PersistenceException;
import com.cronos.onlinereview.autoscreening.management.ScreeningTaskDoesNotExistException;
import com.cronos.onlinereview.phases.PhaseNotSupportedException;
import com.cronos.onlinereview.phases.SubmissionPhaseHandler;
import com.cronos.onlinereview.phases.failuretests.mock.MockResourceManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockReviewManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockScreeningManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockUploadManager;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.util.config.ConfigManager;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>A failure test for {@link SubmissionPhaseHandler} class. Tests the proper handling
 * of invalid input data by the methods. Passes the invalid arguments to the methods and expects the appropriate
 * exception to be thrown.</p>
 *
 * @author isv
 * @version 1.0
 */
public class SubmissionPhaseHandlerFailureTest extends AbstractTestCase {

    /**
     * <p>The instances of {@link SubmissionPhaseHandler} which are tested. These instances are initialized in {@link
     * #setUp()} method and released in {@link #tearDown()} method. Each instance is initialized using a separate
     * constructor provided by the tested class.<p>
     */
    private SubmissionPhaseHandler[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link SubmissionPhaseHandler} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link SubmissionPhaseHandler} class.
     */
    public static Test suite() {
        return new TestSuite(SubmissionPhaseHandlerFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testedInstances = new SubmissionPhaseHandler[2];
        this.testedInstances[0] = new SubmissionPhaseHandler();
        this.testedInstances[1] = new SubmissionPhaseHandler(TestDataFactory.NAMESPACE);
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
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#SubmissionPhaseHandler(String)} constructor for proper
     * handling the invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>namespace</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testConstructor_String_namespace_null() {
        try {
            new SubmissionPhaseHandler(null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#SubmissionPhaseHandler(String)} constructor for proper
     * handling the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>namespace</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_namespace_ZERO_LENGTH_STRING() {
        try {
            new SubmissionPhaseHandler(TestDataFactory.ZERO_LENGTH_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#SubmissionPhaseHandler(String)} constructor for proper
     * handling the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>namespace</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_namespace_WHITESPACE_ONLY_STRING() {
        try {
            new SubmissionPhaseHandler(TestDataFactory.WHITESPACE_ONLY_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#canPerform(Phase)} method for proper handling the
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
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#canPerform(Phase)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getRegistrationPhase()} as <code>phase</code> and expects the
     * <code>PhaseNotSupportedException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_phase_RegistrationPhase() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getRegistrationPhase());
                Assert.fail("PhaseNotSupportedException should have been thrown");
            } catch (PhaseNotSupportedException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("PhaseNotSupportedException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#canPerform(Phase)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getClosedSubmissionPhase()} as <code>phase</code> and expects the
     * <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_phase_ClosedSubmissionPhase() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getClosedSubmissionPhase());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#perform(Phase,String)} method for proper handling the
     * invalid input arguments.</p>
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
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#perform(Phase,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getRegistrationPhase()} as <code>phase</code> and expects the
     * <code>PhaseNotSupportedException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_phase_RegistrationPhase() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getRegistrationPhase(), TestDataFactory.OPERATOR);
                Assert.fail("PhaseNotSupportedException should have been thrown");
            } catch (PhaseNotSupportedException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("PhaseNotSupportedException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#perform(Phase,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getClosedSubmissionPhase()} as <code>phase</code> and expects the
     * <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_phase_ClosedSubmissionPhase() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getClosedSubmissionPhase(), TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#perform(Phase,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>operator</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testPerform_Phase_String_operator_null() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getSubmissionPhase(), null);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#perform(Phase,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>operator</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_operator_ZERO_LENGTH_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getSubmissionPhase(),
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
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#perform(Phase,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>operator</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_operator_WHITESPACE_ONLY_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getSubmissionPhase(),
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
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#canPerform,Phase} for proper behavior if the underlying
     * service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_ResourceManagerError_SearchBuilderConfigurationException() {
        MockResourceManager.throwGlobalException(new SearchBuilderConfigurationException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getSubmissionPhaseWithManualScreening());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#canPerform,Phase} for proper behavior if the underlying
     * service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_ResourceManagerError_ResourcePersistenceException() {
        MockResourceManager.throwGlobalException(new ResourcePersistenceException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getSubmissionPhaseWithManualScreening());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#canPerform,Phase} for proper behavior if the underlying
     * service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_ResourceManagerError_SearchBuilderException() {
        MockResourceManager.throwGlobalException(new SearchBuilderException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getSubmissionPhaseWithManualScreening());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#canPerform,Phase} for proper behavior if the underlying
     * service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockReviewManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_ReviewManagerError_ReviewManagementException() {
        MockReviewManager.throwGlobalException(new ReviewManagementException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getSubmissionPhaseWithManualScreening());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#canPerform,Phase} for proper behavior if the underlying
     * service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockUploadManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_UploadManagerError_UploadPersistenceException() {
        MockUploadManager.throwGlobalException(new UploadPersistenceException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getOpenSubmissionPhase());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#canPerform,Phase} for proper behavior if the underlying
     * service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockUploadManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_UploadManagerError_SearchBuilderException() {
        MockUploadManager.throwGlobalException(new SearchBuilderException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getOpenSubmissionPhase());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#canPerform,Phase} for proper behavior if the underlying
     * service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockScreeningManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_ScreeningManagerError_ScreeningTaskDoesNotExistException() {
        MockScreeningManager.throwGlobalException(new ScreeningTaskDoesNotExistException(1));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getClosedSubmissionPhase());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#canPerform,Phase} for proper behavior if the underlying
     * service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockScreeningManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_ScreeningManagerError_PersistenceException() {
        MockScreeningManager.throwGlobalException(new PersistenceException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getClosedSubmissionPhase());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service meets invalid configuration.</p>
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
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service meets invalid configuration.</p>
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
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service meets invalid configuration.</p>
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
     * <p>Failure test. Tests the {@link SubmissionPhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service meets invalid configuration.</p>
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