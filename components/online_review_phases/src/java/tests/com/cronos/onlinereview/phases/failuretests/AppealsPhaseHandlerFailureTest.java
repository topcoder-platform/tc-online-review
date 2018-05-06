/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.failuretests;

import com.cronos.onlinereview.phases.AppealsPhaseHandler;
import com.cronos.onlinereview.phases.PhaseNotSupportedException;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>A failure test for {@link AppealsPhaseHandler} class. Tests the proper handling of
 * invalid input data by the methods. Passes the invalid arguments to the methods and expects the appropriate exception
 * to be thrown.</p>
 *
 * @author isv
 * @version 1.0
 */
public class AppealsPhaseHandlerFailureTest extends AbstractTestCase {

    /**
     * <p>The instances of {@link AppealsPhaseHandler} which are tested. These instances are initialized in {@link
     * #setUp()} method and released in {@link #tearDown()} method. Each instance is initialized using a separate
     * constructor provided by the tested class.<p>
     */
    private AppealsPhaseHandler[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link AppealsPhaseHandler} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link AppealsPhaseHandler} class.
     */
    public static Test suite() {
        return new TestSuite(AppealsPhaseHandlerFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testedInstances = new AppealsPhaseHandler[2];
        this.testedInstances[0] = new AppealsPhaseHandler();
        this.testedInstances[1] = new AppealsPhaseHandler(TestDataFactory.NAMESPACE);
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
     * <p>Failure test. Tests the {@link AppealsPhaseHandler#AppealsPhaseHandler(String)} constructor for proper
     * handling the invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>namespace</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testConstructor_String_namespace_null() {
        try {
            new AppealsPhaseHandler(null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsPhaseHandler#AppealsPhaseHandler(String)} constructor for proper
     * handling the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>namespace</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_namespace_ZERO_LENGTH_STRING() {
        try {
            new AppealsPhaseHandler(TestDataFactory.ZERO_LENGTH_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsPhaseHandler#AppealsPhaseHandler(String)} constructor for proper
     * handling the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>namespace</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_namespace_WHITESPACE_ONLY_STRING() {
        try {
            new AppealsPhaseHandler(TestDataFactory.WHITESPACE_ONLY_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsPhaseHandler#canPerform(Phase)} method for proper handling the invalid
     * input arguments.</p>
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
     * <p>Failure test. Tests the {@link AppealsPhaseHandler#canPerform(Phase)} method for proper handling the invalid
     * input arguments.</p>
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
     * <p>Failure test. Tests the {@link AppealsPhaseHandler#canPerform(Phase)} method for proper handling the invalid
     * input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getClosedAppealsPhase()} as <code>phase</code> and expects the
     * <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_phase_ClosedAppealsPhase() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getClosedAppealsPhase());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsPhaseHandler#perform(Phase,String)} method for proper handling the
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
     * <p>Failure test. Tests the {@link AppealsPhaseHandler#perform(Phase,String)} method for proper handling the
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
     * <p>Failure test. Tests the {@link AppealsPhaseHandler#perform(Phase,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getClosedAppealsPhase()} as <code>phase</code> and expects the
     * <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_phase_ClosedAppealsPhase() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getClosedAppealsPhase(), TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsPhaseHandler#perform(Phase,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>operator</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testPerform_Phase_String_operator_null() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getAppealsPhase(), null);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsPhaseHandler#perform(Phase,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>operator</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_operator_ZERO_LENGTH_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getAppealsPhase(), TestDataFactory.ZERO_LENGTH_STRING);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link AppealsPhaseHandler#perform(Phase,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>operator</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_operator_WHITESPACE_ONLY_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getAppealsPhase(),
                                                TestDataFactory.WHITESPACE_ONLY_STRING);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }
}
