/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.failuretests;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.cronos.onlinereview.phases.PhaseNotSupportedException;
import com.cronos.onlinereview.phases.SpecificationSubmissionPhaseHandler;
import com.cronos.onlinereview.phases.SubmissionPhaseHandler;
import com.cronos.onlinereview.phases.failuretests.mock.MockUploadManager;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.search.builder.SearchBuilderException;

/**
 * <p>
 * Failure tests for class <code>SpecificationSubmissionPhaseHandler</code>.
 * </p>
 *
 * @author stevenfrog
 * @version 1.0
 */
public class SpecificationSubmissionPhaseHandlerFailureTest extends AbstractTestCase {
    /**
     * <p>
     * Represent the instance that used to call its method for test. It will be initialized in
     * setUp().
     * </p>
     */
    private SpecificationSubmissionPhaseHandler impl;

    /**
     * <p>
     * Gets the test suite for {@link SubmissionPhaseHandler} class.
     * </p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link SubmissionPhaseHandler}
     *         class.
     */
    public static Test suite() {
        return new TestSuite(SpecificationSubmissionPhaseHandlerFailureTest.class);
    }

    /**
     * <p>
     * Set the test environment.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();
        impl = new SpecificationSubmissionPhaseHandler(TestDataFactory.NAMESPACE);
    }

    /**
     * <p>
     * Clear the test environment.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void tearDown() throws Exception {
        super.tearDown();
        impl = null;
    }

    /**
     * <p>
     * Failure test for the constructor <code>SpecificationSubmissionPhaseHandler(String)</code>.<br>
     * The namespace is null.<br>
     * Expect IllegalArgumentException.
     * </p>
     */
    public void testConstructor_String_namespace_null() {
        try {
            new SpecificationSubmissionPhaseHandler(null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>SpecificationSubmissionPhaseHandler(String)</code>.<br>
     * The namespace is ZERO_LENGTH_STRING.<br>
     * Expect IllegalArgumentException.
     * </p>
     */
    public void testConstructor_String_namespace_ZERO_LENGTH_STRING() {
        try {
            new SpecificationSubmissionPhaseHandler(TestDataFactory.ZERO_LENGTH_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the constructor <code>SpecificationSubmissionPhaseHandler(String)</code>.<br>
     * The namespace is WHITESPACE_ONLY_STRING.<br>
     * Expect IllegalArgumentException.
     * </p>
     */
    public void testConstructor_String_namespace_WHITESPACE_ONLY_STRING() {
        try {
            new SpecificationSubmissionPhaseHandler(TestDataFactory.WHITESPACE_ONLY_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>canPerform(Phase)</code>.<br>
     * The phase is null.<br>
     * Expect IllegalArgumentException.
     * </p>
     */
    public void testCanPerform_Phase_phase_null() {
        try {
            impl.canPerform(null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>canPerform(Phase)</code>.<br>
     * The phase is registration phase.<br>
     * Expect PhaseNotSupportedException.
     * </p>
     */
    public void testCanPerform_Phase_phase_RegistrationPhase() {
        try {
            impl.canPerform(TestDataFactory.getRegistrationPhase());
            Assert.fail("PhaseNotSupportedException should have been thrown");
        } catch (PhaseNotSupportedException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseNotSupportedException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>canPerform(Phase)</code>.<br>
     * The phase is closed specification submission phase.<br>
     * Expect PhaseHandlingException.
     * </p>
     */
    public void testCanPerform_Phase_phase_ClosedSpecificationSubmissionPhase() {
        try {
            impl.canPerform(TestDataFactory.getClosedSpecificationSubmissionPhase());
            Assert.fail("PhaseHandlingException should have been thrown");
        } catch (PhaseHandlingException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>canPerform(Phase)</code>.<br>
     * Configures the mock implementation <code>MockUploadManager</code> to throw an exception from
     * any method.<br>
     * Expect PhaseHandlingException.
     * </p>
     */
    public void testCanPerform_Phase_UploadManagerError_UploadPersistenceException() {
        MockUploadManager.throwGlobalException(new UploadPersistenceException("FailureTest"));
        try {
            impl.canPerform(TestDataFactory.getOpenSpecificationSubmissionPhase());
            Assert.fail("PhaseHandlingException should have been thrown");
        } catch (PhaseHandlingException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>canPerform(Phase)</code>.<br>
     * Configures the mock implementation <code>MockUploadManager</code> to throw an exception from
     * any method.<br>
     * Expect PhaseHandlingException.
     * </p>
     */
    public void testCanPerform_Phase_UploadManagerError_SearchBuilderException() {
        MockUploadManager.throwGlobalException(new SearchBuilderException("FailureTest"));
        try {
            impl.canPerform(TestDataFactory.getOpenSpecificationSubmissionPhase());
            Assert.fail("PhaseHandlingException should have been thrown");
        } catch (PhaseHandlingException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>perform(Phase,String)</code>.<br>
     * The phase is null.<br>
     * Expect IllegalArgumentException.
     * </p>
     */
    public void testPerform_Phase_String_phase_null() {
        try {
            impl.perform(null, TestDataFactory.OPERATOR);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>perform(Phase,String)</code>.<br>
     * The operator is null.<br>
     * Expect IllegalArgumentException.
     * </p>
     */
    public void testPerform_Phase_String_operator_null() {
        try {
            impl.perform(TestDataFactory.getSubmissionPhase(), null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>perform(Phase,String)</code>.<br>
     * The operator is ZERO_LENGTH_STRING.<br>
     * Expect IllegalArgumentException.
     * </p>
     */
    public void testPerform_Phase_String_operator_ZERO_LENGTH_STRING() {
        try {
            impl.perform(TestDataFactory.getSpecificationSubmissionPhase(),
                TestDataFactory.ZERO_LENGTH_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>perform(Phase,String)</code>.<br>
     * The operator is WHITESPACE_ONLY_STRING.<br>
     * Expect IllegalArgumentException.
     * </p>
     */
    public void testPerform_Phase_String_operator_WHITESPACE_ONLY_STRING() {
        try {
            impl.perform(TestDataFactory.getSpecificationSubmissionPhase(),
                TestDataFactory.WHITESPACE_ONLY_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>perform(Phase,String)</code>.<br>
     * The phase is registration phase.<br>
     * Expect PhaseNotSupportedException.
     * </p>
     */
    public void testPerform_Phase_String_phase_RegistrationPhase() {
        try {
            impl.perform(TestDataFactory.getRegistrationPhase(), TestDataFactory.OPERATOR);
            Assert.fail("PhaseNotSupportedException should have been thrown");
        } catch (PhaseNotSupportedException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseNotSupportedException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>
     * Failure test for the method <code>perform(Phase,String)</code>.<br>
     * The phase is closed specification submission phase.<br>
     * Expect PhaseHandlingException.
     * </p>
     */
    public void testPerform_Phase_String_phase_ClosedSpecificationSubmissionPhase() {
        try {
            impl.perform(TestDataFactory.getClosedSpecificationSubmissionPhase(), TestDataFactory.OPERATOR);
            Assert.fail("PhaseHandlingException should have been thrown");
        } catch (PhaseHandlingException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
        }
    }
}
