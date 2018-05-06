/*
 * Copyright (C) 2009-2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.cronos.onlinereview.phases.logging.LogMessageTest;
import com.cronos.onlinereview.phases.lookup.LookupHelperTest;
import com.cronos.onlinereview.phases.lookup.NotificationTypeLookupUtilityTest;
import com.cronos.onlinereview.phases.lookup.PhaseStatusLookupUtilityTest;
import com.cronos.onlinereview.phases.lookup.PhaseTypeLookupUtilityTest;
import com.cronos.onlinereview.phases.lookup.ProjectInfoTypeLookupUtilityTest;
import com.cronos.onlinereview.phases.lookup.ResourceRoleLookupUtilityTest;
import com.cronos.onlinereview.phases.lookup.SubmissionStatusLookupUtilityTest;
import com.cronos.onlinereview.phases.lookup.SubmissionTypeLookupUtilityTest;
import com.cronos.onlinereview.phases.lookup.UploadStatusLookupUtilityTest;
import com.cronos.onlinereview.phases.lookup.UploadTypeLookupUtilityTest;

/**
 * <p>
 * This test case aggregates all Unit test cases.
 * </p>
 *
 * @author myxgyy, microsky
 * @version 1.6
 */
public class UnitTests extends TestCase {
    /**
     * Aggregates all unit test cases.
     *
     * @return unit test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // look up classes
        suite.addTestSuite(LookupHelperTest.class);
        suite.addTestSuite(PhaseStatusLookupUtilityTest.class);
        suite.addTestSuite(PhaseTypeLookupUtilityTest.class);
        suite.addTestSuite(ProjectInfoTypeLookupUtilityTest.class);
        suite.addTestSuite(ResourceRoleLookupUtilityTest.class);
        suite.addTestSuite(SubmissionStatusLookupUtilityTest.class);
        suite.addTestSuite(UploadStatusLookupUtilityTest.class);
        suite.addTestSuite(NotificationTypeLookupUtilityTest.class);
        suite.addTestSuite(UploadTypeLookupUtilityTest.class);

        // log message
        suite.addTestSuite(LogMessageTest.class);
        // email options
        suite.addTestSuite(EmailOptionsTest.class);

        // exception classes
        suite.addTestSuite(ConfigurationExceptionTest.class);
        suite.addTestSuite(PhaseNotSupportedExceptionTest.class);

        // main package classes
        suite.addTestSuite(ManagerHelperTest.class);
        suite.addTestSuite(AbstractPhaseHandlerTest.class);
        suite.addTestSuite(RegistrationPhaseHandlerTest.class);
        suite.addTestSuite(SubmissionPhaseHandlerTest.class);
        suite.addTestSuite(ScreeningPhaseHandlerTest.class);
        suite.addTestSuite(ReviewPhaseHandlerTest.class);
        suite.addTestSuite(AppealsPhaseHandlerTest.class);
        suite.addTestSuite(AppealsResponsePhaseHandlerTest.class);
        suite.addTestSuite(AggregationPhaseHandlerTest.class);
        suite.addTestSuite(AggregationReviewPhaseHandlerTest.class);
        suite.addTestSuite(FinalFixPhaseHandlerTest.class);
        suite.addTestSuite(FinalReviewPhaseHandlerTest.class);
        suite.addTestSuite(ApprovalPhaseHandlerTest.class);
        suite.addTestSuite(PostMortemPhaseHandlerTest.class);

        // added in version 1.4
        suite.addTestSuite(SubmissionTypeLookupUtilityTest.class);
        suite.addTestSuite(SpecificationSubmissionPhaseHandlerTest.class);
        suite.addTestSuite(SpecificationReviewPhaseHandlerTest.class);
        suite.addTestSuite(PhasesHelperTest.class);

        // added in version 1.6
        suite.addTestSuite(CheckpointSubmissionPhaseHandlerTest.class);
        suite.addTestSuite(CheckpointScreeningPhaseHandlerTest.class);
        suite.addTestSuite(CheckpointReviewPhaseHandlerTest.class);

        // demo test
        suite.addTestSuite(DemoTest.class);

        return suite;
    }
}