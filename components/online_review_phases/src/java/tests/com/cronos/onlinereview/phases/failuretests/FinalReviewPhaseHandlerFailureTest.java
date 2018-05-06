/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.failuretests;

import com.cronos.onlinereview.phases.FinalReviewPhaseHandler;
import com.cronos.onlinereview.phases.PhaseNotSupportedException;
import com.cronos.onlinereview.phases.failuretests.mock.MockPhaseManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockResourceManager;
import com.cronos.onlinereview.phases.failuretests.mock.MockReviewManager;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>A failure test for {@link FinalReviewPhaseHandler} class. Tests the proper handling
 * of invalid input data by the methods. Passes the invalid arguments to the methods and expects the appropriate
 * exception to be thrown.</p>
 *
 * @author isv
 * @version 1.0
 */
public class FinalReviewPhaseHandlerFailureTest extends AbstractTestCase {

    /**
     * <p>The instances of {@link FinalReviewPhaseHandler} which are tested. These instances are initialized in {@link
     * #setUp()} method and released in {@link #tearDown()} method. Each instance is initialized using a separate
     * constructor provided by the tested class.<p>
     */
    private FinalReviewPhaseHandler[] testedInstances = null;

    /**
     * <p>Gets the test suite for {@link FinalReviewPhaseHandler} class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for {@link FinalReviewPhaseHandler} class.
     */
    public static Test suite() {
        return new TestSuite(FinalReviewPhaseHandlerFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.testedInstances = new FinalReviewPhaseHandler[2];
        this.testedInstances[0] = new FinalReviewPhaseHandler();
        this.testedInstances[1] = new FinalReviewPhaseHandler(TestDataFactory.NAMESPACE);
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
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#FinalReviewPhaseHandler(String)} constructor for proper
     * handling the invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>namespace</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testConstructor_String_namespace_null() {
        try {
            new FinalReviewPhaseHandler(null);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#FinalReviewPhaseHandler(String)} constructor for proper
     * handling the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>namespace</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_namespace_ZERO_LENGTH_STRING() {
        try {
            new FinalReviewPhaseHandler(TestDataFactory.ZERO_LENGTH_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#FinalReviewPhaseHandler(String)} constructor for proper
     * handling the invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>namespace</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_namespace_WHITESPACE_ONLY_STRING() {
        try {
            new FinalReviewPhaseHandler(TestDataFactory.WHITESPACE_ONLY_STRING);
            Assert.fail("IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#canPerform(Phase)} method for proper handling the
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
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#canPerform(Phase)} method for proper handling the
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
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#canPerform(Phase)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getClosedFinalReviewPhase()} as <code>phase</code> and expects the
     * <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_phase_ClosedFinalReviewPhase() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getClosedFinalReviewPhase());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#perform(Phase,String)} method for proper handling the
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
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#perform(Phase,String)} method for proper handling the
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
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#perform(Phase,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#getClosedFinalReviewPhase()} as <code>phase</code> and expects the
     * <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_phase_ClosedFinalReviewPhase() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getClosedFinalReviewPhase(), TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#perform(Phase,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link null} as <code>operator</code> and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testPerform_Phase_String_operator_null() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getFinalReviewPhase(), null);
                Assert.fail("IllegalArgumentException should have been thrown");
            } catch (IllegalArgumentException e) {
                // expected behavior
            } catch (Exception e) {
                Assert.fail("IllegalArgumentException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#perform(Phase,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#ZERO_LENGTH_STRING} as <code>operator</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_operator_ZERO_LENGTH_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getFinalReviewPhase(),
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
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#perform(Phase,String)} method for proper handling the
     * invalid input arguments.</p>
     *
     * <p>Passes {@link TestDataFactory#WHITESPACE_ONLY_STRING} as <code>operator</code> and expects the
     * <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_operator_WHITESPACE_ONLY_STRING() {
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getFinalReviewPhase(),
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
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#canPerform,Phase} for proper behavior if the underlying
     * service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_OpenPhase_ResourceManagerError_SearchBuilderConfigurationException() {
        MockResourceManager.throwGlobalException(new SearchBuilderConfigurationException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getOpenFinalReviewPhase());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#canPerform,Phase} for proper behavior if the underlying
     * service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_OpenPhase_ResourceManagerError_ResourcePersistenceException() {
        MockResourceManager.throwGlobalException(new ResourcePersistenceException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getOpenFinalReviewPhase());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#canPerform,Phase} for proper behavior if the underlying
     * service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_OpenPhase_ResourceManagerError_SearchBuilderException() {
        MockResourceManager.throwGlobalException(new SearchBuilderException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getOpenFinalReviewPhase());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#canPerform,Phase} for proper behavior if the underlying
     * service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockReviewManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testCanPerform_Phase_OpenPhase_ReviewManagerError_ReviewManagementException() {
        MockReviewManager.throwGlobalException(new ReviewManagementException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].canPerform(TestDataFactory.getFinalFixPhase());
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_OpenPhase_ResourceManagerError_SearchBuilderConfigurationException() {
        MockResourceManager.throwGlobalException(new SearchBuilderConfigurationException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getOpenFinalReviewPhase(), TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_OpenPhase_ResourceManagerError_ResourcePersistenceException() {
        MockResourceManager.throwGlobalException(new ResourcePersistenceException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getOpenFinalReviewPhase(), TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockResourceManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_OpenPhase_ResourceManagerError_SearchBuilderException() {
        MockResourceManager.throwGlobalException(new SearchBuilderException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getOpenFinalReviewPhase(), TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockReviewManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_OpenPhase_ReviewManagerError_ReviewManagementException() {
        MockReviewManager.throwGlobalException(new ReviewManagementException("FailureTest"));
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getOpenFinalReviewPhase(), TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }

    /**
     * <p>Failure test. Tests the {@link FinalReviewPhaseHandler#perform,Phase,String} for proper behavior if the
     * underlying service throws an unexpected exception.</p>
     *
     * <p>Configures the mock implementation <code>MockPhaseManager</code> to throw an exception from any method and
     * expects the <code>PhaseHandlingException</code> to be thrown.</p>
     */
    public void testPerform_Phase_String_OpenPhase_PhaseManagerError_PhaseManagementException() {
        MockPhaseManager.throwGlobalException(new PhaseManagementException("FailureTest"));
        Review[] worksheet = new Review[] {new Review(1)};
        worksheet[0].addComment(new Comment(1, 1, new CommentType(1, "Final Review Comment"), "Rejected"));
        MockReviewManager.setMethodResult("searchReviews_Filter_boolean", worksheet);
        for (int i = 0; i < this.testedInstances.length; i++) {
            try {
                this.testedInstances[i].perform(TestDataFactory.getOpenFinalReviewPhase(), TestDataFactory.OPERATOR);
                Assert.fail("PhaseHandlingException should have been thrown");
            } catch (PhaseHandlingException e) {
                // expected behavior
            } catch (Exception e) {
                fail("PhaseHandlingException was expected but the original exception is : " + e);
            }
        }
    }
}
