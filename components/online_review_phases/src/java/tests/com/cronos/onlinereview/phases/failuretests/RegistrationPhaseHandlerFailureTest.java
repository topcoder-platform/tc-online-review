/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.failuretests;

import com.cronos.onlinereview.phases.PhaseNotSupportedException;
import com.cronos.onlinereview.phases.RegistrationPhaseHandler;
import com.cronos.onlinereview.phases.failuretests.mock.MockPhaseManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockProjectManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockResourceManager;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>A failure test for {@link RegistrationPhaseHandler} class. Tests the proper
 * handling of invalid input data by the methods. Passes the invalid arguments to the methods and expects the
 * appropriate exception to be thrown.</p>
 *
 * @author isv
 * @version 1.0
 */
public class RegistrationPhaseHandlerFailureTest extends AbstractTestCase {

    /**
     * <p>The instances of {@link RegistrationPhaseHandler} which are tested. These instances are initialized in {@link
     * #setUp()} method and released in {@link #tearDown()} method. Each instance is initialized using a separate
     * constructor provided by the tested class.<p>
     */
    private RegistrationPhaseHandler[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link RegistrationPhaseHandler} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link RegistrationPhaseHandler} class.
     */
    public static Test suite() {
        return new TestSuite(RegistrationPhaseHandlerFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testedInstances = new RegistrationPhaseHandler[2];
        this.testedInstances[0] = new RegistrationPhaseHandler();
        this.testedInstances[1] = new RegistrationPhaseHandler(TestDataFactory.NAMESPACE);
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
     * <p>Failure test. Tests the {@link RegistrationPhaseHandler#RegistrationPhaseHandler(String)} constructor for
     * proper handling the invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>namespace</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testConstructor_String_namespace_null() {
        try {
            new RegistrationPhaseHandler(null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link RegistrationPhaseHandler#RegistrationPhaseHandler(String)} constructor for
     * proper handling the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>namespace</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_namespace_ZERO_LENGTH_STRING() {
        try {
            new RegistrationPhaseHandler(TestDataFactory.ZERO_LENGTH_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link RegistrationPhaseHandler#RegistrationPhaseHandler(String)} constructor for
     * proper handling the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>namespace</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_namespace_WHITESPACE_ONLY_STRING() {
        try {
            new RegistrationPhaseHandler(TestDataFactory.WHITESPACE_ONLY_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link RegistrationPhaseHandler#canPerform(Phase)} method for proper handling the
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
     * <p>Failure test. Tests the {@link RegistrationPhaseHandler#canPerform(Phase)} method for proper handling the
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
     * <p>Failure test. Tests the {@link RegistrationPhaseHandler#canPerform(Phase)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getClosedRegistrationPhase()} as <code>phase</code> and expects the
     * <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_phase_ClosedRegistrationPhase() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getClosedRegistrationPhase());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link RegistrationPhaseHandler#canPerform(Phase)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getRegistrationPhaseWithNonIntegerRegistrationNumberAttribute()} as
     * <code>phase</code> and expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_phase_RegistrationPhaseWithNonIntegerRegistrationNumberAttribute() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getRegistrationPhaseWithNonIntegerRegistrationNumberAttribute());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link RegistrationPhaseHandler#canPerform(Phase)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getRegistrationPhaseWithNonIntegerRegistrationNumberAttribute()} as
     * <code>phase</code> and expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_phase_PhaseManagerError() {
        MockPhaseManager.throwGlobalException(new PhaseManagementException("test"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getRegistrationPhaseWithNonIntegerRegistrationNumberAttribute());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link RegistrationPhaseHandler#canPerform(Phase)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getRegistrationPhaseWithNonIntegerRegistrationNumberAttribute()} as
     * <code>phase</code> and expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_phase_ProjectManagerError() {
        MockProjectManager.throwGlobalException(new PhaseManagementException("test"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getRegistrationPhaseWithNonIntegerRegistrationNumberAttribute());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link RegistrationPhaseHandler#perform(Phase,String)} method for proper handling the
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
     * <p>Failure test. Tests the {@link RegistrationPhaseHandler#perform(Phase,String)} method for proper handling the
     * invalid input arguments.</p>
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
     * <p>Failure test. Tests the {@link RegistrationPhaseHandler#perform(Phase,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getClosedRegistrationPhase()} as <code>phase</code> and expects the
     * <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_phase_ClosedRegistrationPhase() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getClosedRegistrationPhase(), TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link RegistrationPhaseHandler#perform(Phase,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>operator</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testPerform_Phase_String_operator_null() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getRegistrationPhase(), null);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link RegistrationPhaseHandler#perform(Phase,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>operator</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_operator_ZERO_LENGTH_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getRegistrationPhase(),
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
     * <p>Failure test. Tests the {@link RegistrationPhaseHandler#perform(Phase,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>operator</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_operator_WHITESPACE_ONLY_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getRegistrationPhase(),
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
     * <p>Failure test. Tests the {@link RegistrationPhaseHandler#canPerform,Phase} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_ResourceManagerError_SearchBuilderConfigurationException() {
        MockResourceManager.throwGlobalException(new SearchBuilderConfigurationException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                Phase phase = TestDataFactory.getOpenRegistrationPhase();
                phase.setAttribute("Registration Number", "1");
                this.testedInstances[i].canPerform(phase);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link RegistrationPhaseHandler#canPerform,Phase} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_ResourceManagerError_ResourcePersistenceException() {
        MockResourceManager.throwGlobalException(new ResourcePersistenceException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                Phase phase = TestDataFactory.getOpenRegistrationPhase();
                phase.setAttribute("Registration Number", "1");
                this.testedInstances[i].canPerform(phase);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link RegistrationPhaseHandler#canPerform,Phase} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_ResourceManagerError_SearchBuilderException() {
        MockResourceManager.throwGlobalException(new SearchBuilderException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                Phase phase = TestDataFactory.getOpenRegistrationPhase();
                phase.setAttribute("Registration Number", "1");
                this.testedInstances[i].canPerform(phase);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }
}
